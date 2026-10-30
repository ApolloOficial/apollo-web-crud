package org.apollo.apollowebcrud.dao;

import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Exclusão lógica: o "D" do CRUD vira UPDATE do status (o banco não é apagado). Também reativa.
// O cascade (inativar empresa -> filiais, usuários, equipamentos) será feito no Service.
public class InativacaoDAO extends ApolloConnection {

    private static final String INATIVO = "INATIVO";
    private static final String ATIVO = "ATIVO";

    public InativacaoDAO() {
        super();
    }

    // Tabelas que usam a coluna "ativo"

    public boolean inativarEmpresa(int id) {
        return atualizar("empresa", "ativo", INATIVO, id);
    }

    public boolean inativarFilial(int id) {
        return atualizar("filial", "ativo", INATIVO, id);
    }

    public boolean inativarUsuario(int id) {
        return atualizar("usuario", "ativo", INATIVO, id);
    }

    public boolean reativarUsuario(int id) {
        return atualizar("usuario", "ativo", ATIVO, id);
    }

    public boolean reativarFilial(int id) {
        return atualizar("filial", "ativo", ATIVO, id);
    }

    // Inativar a filial inativa junto o que depende dela (usuários e equipamentos que estavam ATIVOS),
    // tudo numa transação. Reativar a filial NÃO reativa nada disso: volta um a um, manualmente.
    public boolean inativarFilialComDependentes(int idFilial) {
        String[] comandos = {
                "UPDATE filial SET ativo = 'INATIVO' WHERE id = ? AND ativo = 'ATIVO'",
                "UPDATE usuario SET ativo = 'INATIVO' WHERE id_filial = ? AND ativo = 'ATIVO'",
                "UPDATE painel SET status = 'INATIVO' WHERE status = 'ATIVO' AND id_string IN "
                        + "(SELECT s.id FROM string s JOIN inversor i ON i.id = s.id_inversor WHERE i.id_filial = ?)",
                "UPDATE string SET status = 'INATIVO' WHERE status = 'ATIVO' AND id_inversor IN "
                        + "(SELECT id FROM inversor WHERE id_filial = ?)",
                "UPDATE inversor SET status = 'INATIVO' WHERE status = 'ATIVO' AND id_filial = ?"
        };

        try (Connection connection = abrirConexao()) {
            connection.setAutoCommit(false);

            try {
                for (int i = 0; i < comandos.length; i++) {
                    int linhas = executar(connection, comandos[i], idFilial);
                    if (i == 0 && linhas == 0) {
                        desfazer(connection);
                        return false;
                    }
                }
                connection.commit();
                return true;

            } catch (SQLException e) {
                desfazer(connection);
                throw e;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inativar filial: " + e.getMessage());
            return false;
        }
    }

    private int executar(Connection connection, String sql, int idFilial) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idFilial);
            return stmt.executeUpdate();
        }
    }

    // Tabelas que usam a coluna "status"

    public boolean inativarContrato(int id) {
        return atualizar("contrato", "status", INATIVO, id);
    }

    public boolean inativarInversor(int id) {
        return atualizar("inversor", "status", INATIVO, id);
    }

    public boolean inativarString(int id) {
        return atualizar("string", "status", INATIVO, id);
    }

    public boolean inativarPainel(int id) {
        return atualizar("painel", "status", INATIVO, id);
    }

    // A manutenção não tem INATIVO: o estado equivalente é CANCELADA
    public boolean cancelarManutencao(int id) {
        return atualizar("manutencao", "status", "CANCELADA", id);
    }

    // Tabela e coluna vêm só das constantes acima (nunca do usuário); o valor e o id vão por parâmetro
    private boolean atualizar(String tabela, String coluna, String novoValor, int id) {
        String sql = "UPDATE " + tabela + " SET " + coluna + " = ? WHERE id = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, novoValor);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao inativar em " + tabela + ": " + e.getMessage());
            return false;
        }
    }
}