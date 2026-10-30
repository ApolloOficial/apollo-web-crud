package org.apollo.apollowebcrud.dao;

import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Códigos de recuperação de senha. Só o hash do código é guardado, nunca o código em si.
public class RecuperacaoSenhaDAO extends ApolloConnection {

    private static final int MAX_TENTATIVAS = 5;

    // Invalida códigos anteriores do usuário e grava o novo, na mesma transação
    public boolean salvar(int idUsuario, String codigoHash, int minutosValidade) {
        String invalidar = "UPDATE recuperacao_senha SET usado = TRUE WHERE id_usuario = ? AND usado = FALSE";
        String inserir = "INSERT INTO recuperacao_senha (id_usuario, codigo_hash, expira_em) "
                + "VALUES (?, ?, CURRENT_TIMESTAMP + (? * INTERVAL '1 minute'))";

        try (Connection connection = abrirConexao()) {
            connection.setAutoCommit(false);
            try (PreparedStatement stmtInvalidar = connection.prepareStatement(invalidar);
                 PreparedStatement stmtInserir = connection.prepareStatement(inserir)) {

                stmtInvalidar.setInt(1, idUsuario);
                stmtInvalidar.executeUpdate();

                stmtInserir.setInt(1, idUsuario);
                stmtInserir.setString(2, codigoHash);
                stmtInserir.setInt(3, minutosValidade);
                stmtInserir.executeUpdate();

                connection.commit();
                return true;
            } catch (SQLException e) {
                desfazer(connection);
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao salvar código de recuperação: " + e.getMessage());
            return false;
        }
    }

    // Hash do código mais recente que ainda vale (não usado, não expirado, poucas tentativas)
    public String buscarHashValido(int idUsuario) {
        String sql = "SELECT codigo_hash FROM recuperacao_senha "
                + "WHERE id_usuario = ? AND usado = FALSE AND expira_em > CURRENT_TIMESTAMP AND tentativas < ? "
                + "ORDER BY id DESC LIMIT 1";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, MAX_TENTATIVAS);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("codigo_hash");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar código de recuperação: " + e.getMessage());
        }
        return null;
    }

    public void registrarTentativa(int idUsuario) {
        String sql = "UPDATE recuperacao_senha SET tentativas = tentativas + 1 WHERE id_usuario = ? AND usado = FALSE";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao registrar tentativa: " + e.getMessage());
        }
    }

    // Troca a senha e queima o código na mesma transação
    public boolean redefinirSenha(int idUsuario, String senhaHash) {
        String trocar = "UPDATE usuario SET senha_hash = ? WHERE id = ?";
        String queimar = "UPDATE recuperacao_senha SET usado = TRUE WHERE id_usuario = ? AND usado = FALSE";

        try (Connection connection = abrirConexao()) {
            connection.setAutoCommit(false);
            try (PreparedStatement stmtTrocar = connection.prepareStatement(trocar);
                 PreparedStatement stmtQueimar = connection.prepareStatement(queimar)) {

                stmtTrocar.setString(1, senhaHash);
                stmtTrocar.setInt(2, idUsuario);
                if (stmtTrocar.executeUpdate() == 0) {
                    desfazer(connection);
                    return false;
                }

                stmtQueimar.setInt(1, idUsuario);
                stmtQueimar.executeUpdate();

                connection.commit();
                return true;
            } catch (SQLException e) {
                desfazer(connection);
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao redefinir senha: " + e.getMessage());
            return false;
        }
    }
}
