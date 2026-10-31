package org.apollo.apollowebcrud.service;

import org.apollo.apollowebcrud.dao.InversorDAO;
import org.apollo.apollowebcrud.model.Filial;
import org.apollo.apollowebcrud.model.Inversor;
import org.apollo.apollowebcrud.model.StatusGeral;
import org.apollo.apollowebcrud.model.TipoUsuEnum;
import org.apollo.apollowebcrud.model.Usuario;

import java.util.List;

import static org.apollo.apollowebcrud.util.TextoUtils.excede;
import static org.apollo.apollowebcrud.util.TextoUtils.vazio;
import static org.apollo.apollowebcrud.util.TextoUtils.vazioParaNulo;

/**
 * Regras dos inversores. Todas as operações ficam restritas à filial do técnico ou operador logado.
 */
public class InversorService {

    private final InversorDAO inversorDAO = new InversorDAO();
    private final FilialService filialService = new FilialService();

    /** Técnico e operador só enxergam os inversores da própria filial. */
    public List<Inversor> listarVisiveis(Usuario logado) {
        if (logado.getIdFilial() == null) {
            return List.of();
        }
        return inversorDAO.listarPorFilial(logado.getIdFilial());
    }

    /**
     * Usada na visualização. O gerente consulta inversores de qualquer filial da empresa e os demais só os da própria.
     */
    public Inversor buscarVisivel(int idInversor, Usuario logado) {
        if (logado.getPerfil() != TipoUsuEnum.GERENTE) {
            return buscarDaFilial(idInversor, logado);
        }
        Inversor inversor = inversorDAO.buscarPorId(idInversor);
        Filial filial = inversor == null ? null : filialService.buscarPorId(inversor.getIdFilial());
        boolean daEmpresa = filial != null && logado.getIdEmpresa() != null && filial.getIdEmpresa() == logado.getIdEmpresa();
        return daEmpresa ? inversor : null;
    }

    /** Devolve o inversor apenas se ele pertencer à filial do usuário, caso contrário null. */
    public Inversor buscarDaFilial(int idInversor, Usuario logado) {
        if (!logado.podeOperarEquipamentos()) {
            return null;
        }
        Inversor inversor = inversorDAO.buscarPorId(idInversor);
        return inversor != null && inversor.getIdFilial() == logado.getIdFilial() ? inversor : null;
    }

    // ---------- CADASTRO ----------

    /** Valida e cadastra. A filial é sempre a do usuário logado. Devolve null ou a mensagem de erro. */
    public String cadastrarInversor(Inversor novo, Usuario logado) {
        if (!logado.podeOperarEquipamentos()) {
            return "Você não tem permissão para cadastrar inversores";
        }

        String erro = validar(novo, logado.getIdFilial(), 0);
        if (erro != null) {
            return erro;
        }

        novo.setIdFilial(logado.getIdFilial());
        novo.setCodigo(novo.getCodigo().trim());
        novo.setNumeroSerie(novo.getNumeroSerie().trim());
        novo.setMarca(vazioParaNulo(novo.getMarca()));
        novo.setModelo(vazioParaNulo(novo.getModelo()));
        novo.setStatus(StatusGeral.ATIVO);

        return inversorDAO.inserir(novo) > 0 ? null : "Erro interno no servidor ao tentar salvar o inversor";
    }

    // ---------- EDIÇÃO (a filial não muda, mas o status pode mudar aqui) ----------

    /** Valida e atualiza o inversor, respeitando a filial do usuário. */
    public String editarInversor(Inversor dados, Usuario logado) {
        Inversor atual = buscarDaFilial(dados.getId(), logado);
        if (atual == null) {
            return "Inversor não encontrado ou sem permissão";
        }
        if (dados.getStatus() == null) {
            return "O status não foi informado";
        }

        String erro = validar(dados, atual.getIdFilial(), atual.getId());
        if (erro != null) {
            return erro;
        }

        atual.setCodigo(dados.getCodigo().trim());
        atual.setNumeroSerie(dados.getNumeroSerie().trim());
        atual.setMarca(vazioParaNulo(dados.getMarca()));
        atual.setModelo(vazioParaNulo(dados.getModelo()));
        atual.setPotenciaKw(dados.getPotenciaKw());
        atual.setDataInstalacao(dados.getDataInstalacao());
        atual.setStatus(dados.getStatus());

        return inversorDAO.atualizar(atual) ? null : "Erro interno no servidor ao tentar salvar o inversor";
    }

    /**
     * Confere código e número de série (obrigatórios), tamanho máximo dos textos e potência positiva.
     * Também impede código repetido na mesma filial e número de série repetido no sistema.
     * O idIgnorar impede que o próprio inversor seja tratado como repetido durante a edição.
     */
    private String validar(Inversor i, int idFilial, int idIgnorar) {
        if (vazio(i.getCodigo())) return "O código do inversor não foi informado";
        if (excede(i.getCodigo(), 50)) return "O código do inversor deve ter até 50 caracteres";
        if (vazio(i.getNumeroSerie())) return "O número de série não foi informado";
        if (excede(i.getNumeroSerie(), 100)) return "O número de série deve ter até 100 caracteres";
        if (excede(i.getMarca(), 60)) return "A marca deve ter até 60 caracteres";
        if (excede(i.getModelo(), 150)) return "O modelo deve ter até 150 caracteres";
        if (i.getPotenciaKw() != null && (i.getPotenciaKw() <= 0 || i.getPotenciaKw() >= 100000)) {
            return "A potência deve ser maior que zero";
        }

        Inversor mesmoCodigo = inversorDAO.buscarPorCodigoNaFilial(idFilial, i.getCodigo().trim());
        if (mesmoCodigo != null && mesmoCodigo.getId() != idIgnorar) {
            return "Já existe um inversor com este código na filial";
        }
        Inversor mesmaSerie = inversorDAO.buscarPorNumeroSerie(i.getNumeroSerie().trim());
        if (mesmaSerie != null && mesmaSerie.getId() != idIgnorar) {
            return "Este número de série já foi cadastrado no sistema";
        }
        return null;
    }
}
