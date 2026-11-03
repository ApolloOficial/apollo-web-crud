package org.apollo.apollowebcrud.service;

import org.apollo.apollowebcrud.dao.CadastroEmpresaDAO;
import org.apollo.apollowebcrud.dao.EmpresaDAO;
import org.apollo.apollowebcrud.model.Contrato;
import org.apollo.apollowebcrud.model.Empresa;
import org.apollo.apollowebcrud.model.Endereco;
import org.apollo.apollowebcrud.model.StatusGeral;
import org.apollo.apollowebcrud.model.TipoUsuEnum;
import org.apollo.apollowebcrud.model.Usuario;
import org.apollo.apollowebcrud.util.CnpjUtils;
import org.apollo.apollowebcrud.util.EmailUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Regras de negócio da empresa. Cada gerente possui uma única empresa e o cadastro dela é feito
 * em transação junto com endereço, contrato inicial e o vínculo do próprio gerente.
 */
public class EmpresaService {

    private final EmpresaDAO empresaDAO = new EmpresaDAO();
    private final CadastroEmpresaDAO cadastroEmpresaDAO = new CadastroEmpresaDAO();
    private final EnderecoService enderecoService = new EnderecoService();

    /** Confere se existe uma empresa com o id informado. */
    public boolean verificarEmpresaExist(int idEmpresa) {
        return empresaDAO.buscarPorId(idEmpresa) != null;
    }

    /** Cada usuário só enxerga a própria empresa, então um gerente sem empresa recebe uma lista vazia. */
    public List<Empresa> listarVisiveis(Usuario logado) {
        if (logado.getIdEmpresa() == null) {
            return List.of();
        }
        Empresa propria = empresaDAO.buscarPorId(logado.getIdEmpresa());
        return propria == null ? List.of() : List.of(propria);
    }

    /** Busca a empresa vinculada ao gerente, ou null se não houver. */
    public Empresa buscarDoGerente(Usuario gerente) {
        if (gerente == null || gerente.getPerfil() != TipoUsuEnum.GERENTE || gerente.getIdEmpresa() == null) {
            return null;
        }
        return empresaDAO.buscarPorId(gerente.getIdEmpresa());
    }

    /** Carrega o endereço da empresa. */
    public Endereco buscarEndereco(Empresa empresa) {
        return empresa == null ? null : enderecoService.buscarPorId(empresa.getIdEndereco());
    }

    /** Informa se a empresa existe e está com status ATIVO. */
    public boolean estaAtiva(int idEmpresa) {
        Empresa empresa = empresaDAO.buscarPorId(idEmpresa);
        return empresa != null && empresa.getStatus() == StatusGeral.ATIVO;
    }

    // ---------- CADASTRO (feito pelo gerente, uma única vez) ----------

    /**
     * Valida os dados e cadastra a empresa. Devolve null em caso de sucesso ou a mensagem de erro.
     * Quando dá certo, o gerente da sessão já passa a ter o idEmpresa preenchido.
     */
    public String cadastrarEmpresa(Empresa empresa, Endereco endereco, Usuario gerente) {
        String erro = validarGerente(gerente);
        if (erro == null) erro = validarEmpresa(empresa, 0);
        if (erro == null) erro = enderecoService.validar(endereco);
        if (erro != null) {
            return erro;
        }

        prepararParaGravar(empresa, endereco, gerente);

        int idEmpresa = cadastroEmpresaDAO.cadastrar(empresa, endereco, contratoInicial(gerente), gerente.getId());
        if (idEmpresa <= 0) {
            return "Erro interno no servidor ao tentar salvar a empresa";
        }

        gerente.setIdEmpresa(idEmpresa);
        return null;
    }

    // ---------- EDIÇÃO (só o gerente, da própria empresa) ----------

    /**
     * Valida e atualiza empresa e endereço. O status não é alterado por aqui. Devolve null ou a mensagem de erro.
     */
    public String editarEmpresa(Empresa dados, Endereco endereco, Usuario gerente) {
        if (gerente.getPerfil() != TipoUsuEnum.GERENTE || gerente.getIdEmpresa() == null) {
            return "Somente o gerente pode editar a empresa";
        }

        Empresa atual = empresaDAO.buscarPorId(gerente.getIdEmpresa());
        if (atual == null) {
            return "Empresa não encontrada";
        }

        String erro = validarEmpresa(dados, atual.getId());
        if (erro == null) erro = enderecoService.validar(endereco);
        if (erro != null) {
            return erro;
        }

        atual.setNome(dados.getNome().trim());
        atual.setCnpj(CnpjUtils.removerMascara(dados.getCnpj()));
        atual.setEmailContato(dados.getEmailContato().trim());
        atual.setDescricao(vazioParaNulo(dados.getDescricao()));
        atual.setUrlFoto(vazioParaNulo(dados.getUrlFoto()));
        enderecoService.normalizar(endereco);

        return empresaDAO.atualizar(atual, endereco) ? null : "Erro interno no servidor ao tentar salvar a empresa";
    }

    /** Só um gerente que ainda não tem empresa vinculada pode fazer o cadastro. */
    private String validarGerente(Usuario gerente) {
        if (gerente.getPerfil() != TipoUsuEnum.GERENTE) {
            return "Somente o gerente pode cadastrar a empresa";
        }
        if (gerente.getIdEmpresa() != null) {
            return "Você já possui uma empresa cadastrada";
        }
        return null;
    }

    /**
     * Confere nome, CNPJ (formato, dígitos verificadores e duplicidade) e e-mail de contato. O parâmetro
     * idIgnorar evita que a própria empresa seja vista como CNPJ repetido durante a edição, e vale 0 no cadastro.
     */
    private String validarEmpresa(Empresa e, int idIgnorar) {
        if (vazio(e.getNome())) {
            return "O nome da empresa não foi informado";
        }
        String cnpj = CnpjUtils.removerMascara(e.getCnpj());
        if (cnpj.isEmpty()) {
            return "O CNPJ não foi informado";
        }
        if (!CnpjUtils.isCnpjValido(cnpj)) {
            return "O CNPJ não é válido";
        }
        Empresa mesmoCnpj = empresaDAO.buscarPorCnpj(cnpj);
        if (mesmoCnpj != null && mesmoCnpj.getId() != idIgnorar) {
            return "Este CNPJ já foi cadastrado no sistema";
        }
        if (vazio(e.getEmailContato())) {
            return "O email de contato não foi informado";
        }
        if (!EmailUtils.isEmailValido(e.getEmailContato())) {
            return "O email de contato não é válido";
        }
        return null;
    }

    /**
     * Padroniza os dados para o formato do banco. O CNPJ fica só com dígitos e campos opcionais vazios viram null.
     */
    private void prepararParaGravar(Empresa empresa, Endereco endereco, Usuario gerente) {
        empresa.setNome(empresa.getNome().trim());
        empresa.setCnpj(CnpjUtils.removerMascara(empresa.getCnpj()));
        empresa.setEmailContato(empresa.getEmailContato().trim());
        empresa.setDescricao(vazioParaNulo(empresa.getDescricao()));
        empresa.setUrlFoto(vazioParaNulo(empresa.getUrlFoto()));
        empresa.setStatus(StatusGeral.ATIVO);
        empresa.setIdUsuarioCadastro(gerente.getId());

        enderecoService.normalizar(endereco);
    }

    /**
     * Cria o contrato que nasce junto com a empresa. O gerente só consulta, por isso o valor mensal
     * começa em zero até que a equipe da Apollo defina o valor real.
     */
    private Contrato contratoInicial(Usuario gerente) {
        Contrato contrato = new Contrato();
        contrato.setDataInicio(LocalDate.now());
        contrato.setValorMensal(BigDecimal.ZERO);
        contrato.setStatus(StatusGeral.ATIVO);
        contrato.setIdUsuarioCadastro(gerente.getId());
        return contrato;
    }

    /** Atalho para testar texto nulo ou em branco. */
    private boolean vazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    /** Troca texto em branco por null e remove espaços das pontas. */
    private String vazioParaNulo(String valor) {
        return vazio(valor) ? null : valor.trim();
    }
}
