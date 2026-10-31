package org.apollo.apollowebcrud.service;

import org.apollo.apollowebcrud.dao.FilialDAO;
import org.apollo.apollowebcrud.dao.InativacaoDAO;
import org.apollo.apollowebcrud.model.Endereco;
import org.apollo.apollowebcrud.model.Filial;
import org.apollo.apollowebcrud.model.Segmento;
import org.apollo.apollowebcrud.model.StatusGeral;
import org.apollo.apollowebcrud.model.TipoUsuEnum;
import org.apollo.apollowebcrud.model.Usuario;

import org.apollo.apollowebcrud.util.CnpjUtils;
import org.apollo.apollowebcrud.util.EmailUtils;
import org.apollo.apollowebcrud.util.TelefoneUtils;

import java.util.List;

/**
 * Regras de negócio das filiais. Apenas o gerente cadastra, edita, inativa e reativa,
 * sempre dentro da própria empresa. Técnico e operador só consultam a filial a que pertencem.
 */
public class FilialService {

    private final FilialDAO filialDAO = new FilialDAO();
    private final EnderecoService enderecoService = new EnderecoService();
    private final SegmentoService segmentoService = new SegmentoService();
    private final EmpresaService empresaService = new EmpresaService();
    private final InativacaoDAO inativacaoDAO = new InativacaoDAO();

    /** Confere se a filial existe. */
    public boolean verificarFilialExist(int idFilial) {
        return filialDAO.buscarPorId(idFilial) != null;
    }

    /** Busca simples pelo id, sem checar quem está pedindo. */
    public Filial buscarPorId(int idFilial) {
        return filialDAO.buscarPorId(idFilial);
    }

    /** A edição é exclusiva do gerente e limitada às filiais da empresa dele. */
    public Filial buscarParaEdicao(int idFilial, Usuario gerente) {
        return buscarFilialDoGerente(idFilial, gerente);
    }

    /**
     * O gerente consulta as filiais da empresa e técnico ou operador apenas a filial vinculada à sessão.
     */
    public Filial buscarVisivel(int idFilial, Usuario logado) {
        if (logado == null) {
            return null;
        }
        if (logado.getPerfil() == TipoUsuEnum.GERENTE) {
            return buscarFilialDoGerente(idFilial, logado);
        }
        if (logado.getIdFilial() == null) {
            return null;
        }
        Filial filial = filialDAO.buscarPorId(idFilial);
        return filial != null && filial.getId() == logado.getIdFilial() ? filial : null;
    }

    /** Carrega o endereço da filial. */
    public Endereco buscarEndereco(Filial filial) {
        return filial == null ? null : enderecoService.buscarPorId(filial.getIdEndereco());
    }

    /** Carrega o segmento de atuação da filial. */
    public Segmento buscarSegmento(Filial filial) {
        return filial == null ? null : segmentoService.buscarPorId(filial.getIdSegmento());
    }

    /** Gerente recebe todas as filiais da empresa e os demais perfis recebem somente a própria. */
    public List<Filial> listarVisiveis(Usuario logado) {
        if (logado.getPerfil() == TipoUsuEnum.GERENTE) {
            return logado.getIdEmpresa() == null ? List.of() : filialDAO.listarPorEmpresa(logado.getIdEmpresa());
        }
        if (logado.getIdFilial() == null) {
            return List.of();
        }
        Filial propria = filialDAO.buscarPorId(logado.getIdFilial());
        return propria == null ? List.of() : List.of(propria);
    }

    /** Informa se a filial existe e está ativa. */
    public boolean estaAtiva(int idFilial) {
        Filial filial = filialDAO.buscarPorId(idFilial);
        return filial != null && filial.getStatus() == StatusGeral.ATIVO;
    }

    // ---------- CADASTRO (feito pelo gerente, na própria empresa) ----------

    /** Valida e grava a filial com o endereço. Devolve null se cadastrou ou a mensagem de erro. */
    public String cadastrarFilial(Filial filial, Endereco endereco, Usuario gerente) {
        String erro = validarGerente(gerente);
        if (erro == null) erro = validarFilial(filial, 0);
        if (erro == null) erro = enderecoService.validar(endereco);
        if (erro != null) {
            return erro;
        }

        prepararParaGravar(filial, endereco, gerente);

        return filialDAO.inserir(filial, endereco) > 0
                ? null
                : "Erro interno no servidor ao tentar salvar a filial";
    }

    // ---------- EDIÇÃO, INATIVAÇÃO E REATIVAÇÃO (só o gerente, filiais da própria empresa) ----------

    /** Valida e atualiza filial e endereço. Devolve null em caso de sucesso. */
    public String editarFilial(Filial dados, Endereco endereco, Usuario gerente) {
        Filial atual = buscarFilialDoGerente(dados.getId(), gerente);
        if (atual == null) {
            return "Filial não encontrada ou sem permissão";
        }

        String erro = validarFilial(dados, atual.getId());
        if (erro == null) erro = enderecoService.validar(endereco);
        if (erro != null) {
            return erro;
        }

        atual.setNome(dados.getNome().trim());
        atual.setIdSegmento(dados.getIdSegmento());
        atual.setCnpj(CnpjUtils.removerMascara(dados.getCnpj()));
        atual.setEmail(dados.getEmail().trim());
        atual.setTelefone(vazio(dados.getTelefone()) ? null : dados.getTelefone().replaceAll("\\D", ""));
        enderecoService.normalizar(endereco);

        return filialDAO.atualizar(atual, endereco) ? null : "Erro interno no servidor ao tentar salvar a filial";
    }

    /** Inativa a filial e também os usuários e equipamentos que ainda estavam ativos nela. */
    public String inativarFilial(int idFilial, Usuario gerente) {
        if (buscarFilialDoGerente(idFilial, gerente) == null) {
            return "Filial não encontrada ou sem permissão";
        }
        return inativacaoDAO.inativarFilialComDependentes(idFilial) ? null : "Erro interno no servidor ao inativar a filial";
    }

    /**
     * Reativa apenas a filial. Usuários e equipamentos precisam voltar manualmente,
     * e a empresa tem que estar ativa para que isso seja permitido.
     */
    public String reativarFilial(int idFilial, Usuario gerente) {
        if (buscarFilialDoGerente(idFilial, gerente) == null) {
            return "Filial não encontrada ou sem permissão";
        }
        if (!empresaService.estaAtiva(gerente.getIdEmpresa())) {
            return "A empresa precisa estar ativa para reativar a filial";
        }
        return inativacaoDAO.reativarFilial(idFilial) ? null : "Erro interno no servidor ao reativar a filial";
    }

    /** Só devolve a filial se o usuário for gerente e ela pertencer à empresa dele. */
    private Filial buscarFilialDoGerente(int idFilial, Usuario gerente) {
        if (gerente == null || gerente.getPerfil() != TipoUsuEnum.GERENTE || gerente.getIdEmpresa() == null) {
            return null;
        }
        Filial filial = filialDAO.buscarPorId(idFilial);
        return filial != null && filial.getIdEmpresa() == gerente.getIdEmpresa() ? filial : null;
    }

    /** Confirma que o usuário é gerente e que a empresa dele já foi cadastrada. */
    private String validarGerente(Usuario gerente) {
        if (gerente.getPerfil() != TipoUsuEnum.GERENTE) {
            return "Somente o gerente pode cadastrar filiais";
        }
        if (gerente.getIdEmpresa() == null) {
            return "Cadastre a empresa antes de cadastrar filiais";
        }
        return null;
    }

    /**
     * Confere nome, segmento, CNPJ (formato e duplicidade), e-mail e telefone. O idIgnorar permite que
     * a filial editada não conte como CNPJ duplicado, e vale 0 no cadastro.
     */
    private String validarFilial(Filial f, int idIgnorar) {
        if (vazio(f.getNome())) {
            return "O nome da filial não foi informado";
        }
        if (f.getIdSegmento() <= 0 || !segmentoService.verificarSegmentoExist(f.getIdSegmento())) {
            return "O segmento selecionado não existe";
        }
        String cnpj = CnpjUtils.removerMascara(f.getCnpj());
        if (cnpj.isEmpty()) {
            return "O CNPJ não foi informado";
        }
        if (!CnpjUtils.isCnpjValido(cnpj)) {
            return "O CNPJ não é válido";
        }
        Filial mesmoCnpj = filialDAO.buscarPorCnpj(cnpj);
        if (mesmoCnpj != null && mesmoCnpj.getId() != idIgnorar) {
            return "Este CNPJ já foi cadastrado no sistema";
        }
        if (vazio(f.getEmail())) {
            return "O email da filial não foi informado";
        }
        if (!EmailUtils.isEmailValido(f.getEmail())) {
            return "O email da filial não é válido";
        }
        if (!vazio(f.getTelefone()) && !TelefoneUtils.isTelefoneValido(f.getTelefone().trim())) {
            return "O telefone não é válido";
        }
        return null;
    }

    /** A empresa da filial vem sempre do gerente logado e nunca do formulário. */
    private void prepararParaGravar(Filial filial, Endereco endereco, Usuario gerente) {
        filial.setIdEmpresa(gerente.getIdEmpresa());
        filial.setNome(filial.getNome().trim());
        filial.setCnpj(CnpjUtils.removerMascara(filial.getCnpj()));
        filial.setEmail(filial.getEmail().trim());
        filial.setTelefone(vazio(filial.getTelefone()) ? null : filial.getTelefone().replaceAll("\\D", ""));
        filial.setStatus(StatusGeral.ATIVO);
        filial.setIdUsuarioCadastro(gerente.getId());

        enderecoService.normalizar(endereco);
    }

    /** Testa texto nulo ou em branco. */
    private boolean vazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
