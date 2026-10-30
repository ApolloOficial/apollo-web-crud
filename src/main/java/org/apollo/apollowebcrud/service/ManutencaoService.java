package org.apollo.apollowebcrud.service;

import org.apollo.apollowebcrud.dao.ManutencaoDAO;
import org.apollo.apollowebcrud.model.Manutencao;
import org.apollo.apollowebcrud.model.StatusManutencao;
import org.apollo.apollowebcrud.model.Usuario;

import java.time.LocalDate;
import java.util.List;

import static org.apollo.apollowebcrud.util.TextoUtils.excede;
import static org.apollo.apollowebcrud.util.TextoUtils.vazio;

public class ManutencaoService {

    private final ManutencaoDAO manutencaoDAO = new ManutencaoDAO();
    private final PainelService painelService = new PainelService();
    private final UsuarioService usuarioService = new UsuarioService();

    // Técnico e operador só enxergam o que pertence à própria filial
    public List<Manutencao> listarVisiveis(Usuario logado) {
        if (logado.getIdFilial() == null) {
            return List.of();
        }
        return manutencaoDAO.listarPorFilial(logado.getIdFilial());
    }

    // Devolve a manutenção só se o painel dela é da filial do usuário (null caso contrário)
    public Manutencao buscarDaFilial(int idManutencao, Usuario logado) {
        if (!logado.podeOperarEquipamentos()) {
            return null;
        }
        Manutencao manutencao = manutencaoDAO.buscarPorId(idManutencao);
        return manutencao != null && painelService.buscarDaFilial(manutencao.getIdPainel(), logado) != null ? manutencao : null;
    }

    // ---------- CADASTRO ----------

    // Nasce ABERTA, com abertura hoje (se não informada). Sem responsável informado, o responsável é quem cadastrou.
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
        nova.setDataConclusao(null);
        if (nova.getIdUsuarioResponsavel() == null) {
            nova.setIdUsuarioResponsavel(logado.getId());
        }

        String erro = validar(nova, logado);
        if (erro != null) {
            return erro;
        }

        nova.setDescricao(nova.getDescricao().trim());
        nova.setStatus(StatusManutencao.ABERTA);

        return manutencaoDAO.inserir(nova) > 0 ? null : "Erro interno no servidor ao tentar salvar a manutenção";
    }

    // ---------- EDIÇÃO (o painel não muda; status e datas mudam aqui) ----------

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
        if (dados.getStatus() == StatusManutencao.CONCLUIDA && dados.getDataConclusao() == null) {
            dados.setDataConclusao(LocalDate.now());
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

        return manutencaoDAO.atualizar(atual) ? null : "Erro interno no servidor ao tentar salvar a manutenção";
    }

    // Mesmas regras do banco (datas coerentes) + responsável precisa ser técnico/operador da mesma filial
    private String validar(Manutencao m, Usuario logado) {
        if (m.getTipoManutencao() == null) return "O tipo da manutenção não foi informado";
        if (vazio(m.getDescricao())) return "A descrição não foi informada";
        if (excede(m.getDescricao(), 255)) return "A descrição deve ter até 255 caracteres";
        if (m.getDataPrevista() != null && m.getDataPrevista().isBefore(m.getDataAbertura())) {
            return "A data prevista não pode ser anterior à abertura";
        }
        if (m.getDataConclusao() != null && m.getDataConclusao().isBefore(m.getDataAbertura())) {
            return "A data de conclusão não pode ser anterior à abertura";
        }
        if (!usuarioService.ehOperacionalDaFilial(m.getIdUsuarioResponsavel(), logado.getIdFilial())) {
            return "O responsável precisa ser um técnico ou operador da sua filial";
        }
        return null;
    }
}
