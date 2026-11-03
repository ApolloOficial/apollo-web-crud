
package org.apollo.apollowebcrud.service;

import org.apollo.apollowebcrud.dao.ManutencaoDAO;
import org.apollo.apollowebcrud.model.Manutencao;
import org.apollo.apollowebcrud.model.StatusManutencao;
import org.apollo.apollowebcrud.model.Usuario;

import java.time.LocalDate;
import java.util.List;

import static org.apollo.apollowebcrud.util.TextoUtils.excede;
import static org.apollo.apollowebcrud.util.TextoUtils.vazio;

/** Regras das manutenções, sempre limitadas aos painéis da filial do usuário logado. */
public class ManutencaoService {

    private final ManutencaoDAO manutencaoDAO = new ManutencaoDAO();
    private final PainelService painelService = new PainelService();
    private final UsuarioService usuarioService = new UsuarioService();

    /** Técnico e operador só enxergam as manutenções da própria filial. */
    public List<Manutencao> listarVisiveis(Usuario logado) {
        if (logado.getIdFilial() == null) {
            return List.of();
        }
        return manutencaoDAO.listarPorFilial(logado.getIdFilial());
    }

    /** Devolve a manutenção só quando o painel dela pertence à filial do usuário. */
    public Manutencao buscarDaFilial(int idManutencao, Usuario logado) {
        if (!logado.podeOperarEquipamentos()) {
            return null;
        }

        Manutencao manutencao = manutencaoDAO.buscarPorId(idManutencao);

        return manutencao != null
                && painelService.buscarDaFilial(manutencao.getIdPainel(), logado) != null
                ? manutencao : null;
    }

    // ---------- CADASTRO ----------

    /**
     * Manutenção com abertura hoje ou no passado nasce EM_ANDAMENTO.
     * Com abertura futura, nasce ABERTA.
     * Sem responsável, assume quem cadastrou.
     */
    public String cadastrarManutencao(Manutencao nova, Usuario logado) {
        if (!logado.podeOperarEquipamentos()) {
            return "Você não tem permissão para cadastrar manutenções";
        }

        if (painelService.buscarDaFilial(nova.getIdPainel(), logado) == null) {
            return "O painel selecionado não existe na sua filial";
        }

        if (nova.getDataAbertura() == null) {
            nova.setDataAbertura(LocalDate.now());
        }

        if (nova.getDataConclusao() != null) {
            return "A data de conclusão não pode ser informada no cadastro de uma manutenção";
        }

        if (nova.getIdUsuarioResponsavel() == null) {
            nova.setIdUsuarioResponsavel(logado.getId());
        }

        nova.setStatus(
                nova.getDataAbertura().isAfter(LocalDate.now())
                        ? StatusManutencao.ABERTA
                        : StatusManutencao.EM_ANDAMENTO
        );

        String erro = validar(nova, logado);
        if (erro != null) {
            return erro;
        }

        nova.setDescricao(nova.getDescricao().trim());

        return manutencaoDAO.inserir(nova) > 0
                ? null
                : "Erro interno no servidor ao tentar salvar a manutenção";
    }

    // ---------- EDIÇÃO ----------

    /** Valida e atualiza os dados da manutenção, incluindo status e datas. */
    public String editarManutencao(Manutencao dados, Usuario logado) {
        Manutencao atual = buscarDaFilial(dados.getId(), logado);

        if (atual == null) {
            return "Manutenção não encontrada ou sem permissão";
        }

        if (dados.getStatus() == null) {
            return "O status não foi informado";
        }

        if (dados.getDataAbertura() == null) {
            dados.setDataAbertura(atual.getDataAbertura());
        }

        if (dados.getIdUsuarioResponsavel() == null) {
            dados.setIdUsuarioResponsavel(atual.getIdUsuarioResponsavel());
        }

        // Atualiza automaticamente apenas manutenções ABERTAS.
        // Não altera manutenções concluídas ou canceladas.
        if (dados.getStatus() == StatusManutencao.ABERTA
                && !dados.getDataAbertura().isAfter(LocalDate.now())) {
            dados.setStatus(StatusManutencao.EM_ANDAMENTO);
        }

        if (dados.getStatus() == StatusManutencao.CONCLUIDA) {
            if (dados.getDataConclusao() == null) {
                dados.setDataConclusao(LocalDate.now());
            }
        } else {
            // Inclui CANCELADA: sua data de conclusão deve ser nula.
            dados.setDataConclusao(null);
        }

        String erro = validar(dados, logado);
        if (erro != null) {
            return erro;
        }

        atual.setTipoManutencao(dados.getTipoManutencao());
        atual.setDescricao(dados.getDescricao().trim());
        atual.setStatus(dados.getStatus());
        atual.setDataAbertura(dados.getDataAbertura());
        atual.setDataPrevista(dados.getDataPrevista());
        atual.setDataConclusao(dados.getDataConclusao());
        atual.setIdUsuarioResponsavel(dados.getIdUsuarioResponsavel());

        return manutencaoDAO.atualizar(atual)
                ? null
                : "Erro interno no servidor ao tentar salvar a manutenção";
    }

    /**
     * Valida as datas, o status e o responsável técnico
     * ou operador pertencente à mesma filial.
     */
    private String validar(Manutencao m, Usuario logado) {
        if (m.getTipoManutencao() == null) {
            return "O tipo da manutenção não foi informado";
        }

        if (vazio(m.getDescricao())) {
            return "A descrição não foi informada";
        }

        if (excede(m.getDescricao(), 255)) {
            return "A descrição deve ter até 255 caracteres";
        }

        if (m.getDataAbertura() == null) {
            return "A data de abertura não foi informada";
        }

        if (m.getDataAbertura().isAfter(LocalDate.now())
                && m.getStatus() == StatusManutencao.EM_ANDAMENTO) {
            return "Uma manutenção com abertura futura não pode estar em andamento";
        }

        if (m.getDataPrevista() != null
                && m.getDataPrevista().isBefore(m.getDataAbertura())) {
            return "A data prevista não pode ser anterior à abertura";
        }

        if (m.getDataConclusao() != null) {
            if (m.getDataConclusao().isAfter(LocalDate.now())) {
                return "A data de conclusão não pode estar no futuro";
            }

            if (m.getDataConclusao().isBefore(m.getDataAbertura())) {
                return "A data de conclusão não pode ser anterior à abertura";
            }
        }

        if (m.getStatus() == StatusManutencao.CONCLUIDA
                && m.getDataAbertura().isAfter(LocalDate.now())) {
            return "Uma manutenção com abertura futura não pode estar concluída";
        }

        if (!usuarioService.ehOperacionalDaFilial(
                m.getIdUsuarioResponsavel(),
                logado.getIdFilial())) {
            return "O responsável precisa ser um técnico ou operador da sua filial";
        }

        return null;
    }
}
