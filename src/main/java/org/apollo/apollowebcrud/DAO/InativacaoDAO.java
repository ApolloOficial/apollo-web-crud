package org.apollo.apollowebcrud.dao;

import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Exclusão lógica, em que o D do CRUD vira um UPDATE de status e nada é apagado do banco.
 * Também faz a reativação quando ela é permitida.
 */
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

    /**
     * Inativa a filial junto com usuários e equipamentos que estavam ativos nela, tudo em uma transação.
     * Reativar a filial depois não traz nada disso de volta, cada item volta manualmente.
     */
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

    /** Roda um UPDATE com o id da filial como parâmetro e informa quantas linhas mudaram. */
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

    /** A manutenção não possui o status INATIVO, então o equivalente é CANCELADA. */
    public boolean cancelarManutencao(int id) {
        return atualizar("manutencao", "status", "CANCELADA", id);
    }

    /**
     * O nome da tabela e da coluna vêm apenas das constantes da classe e nunca do usuário. Valor e id seguem por parâmetro.
     */
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