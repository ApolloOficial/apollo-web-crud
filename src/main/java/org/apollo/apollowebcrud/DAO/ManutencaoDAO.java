package org.apollo.apollowebcrud.dao;

import org.apollo.apollowebcrud.model.Manutencao;
import org.apollo.apollowebcrud.model.StatusManutencao;
import org.apollo.apollowebcrud.model.TipoManutencao;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManutencaoDAO extends ApolloConnection {

    public ManutencaoDAO() {
        super();
    }

    private Manutencao montar(ResultSet rs) throws SQLException {
        Manutencao manutencao = new Manutencao();
        manutencao.setId(rs.getInt("id"));
        manutencao.setIdPainel(rs.getInt("id_painel"));
        manutencao.setTipoManutencao(TipoManutencao.valueOf(rs.getString("tipo_manutencao")));
        manutencao.setDescricao(rs.getString("descricao"));
        manutencao.setStatus(StatusManutencao.valueOf(rs.getString("status")));
        manutencao.setDataAbertura(rs.getDate("data_abertura").toLocalDate());

        Date dataPrevista = rs.getDate("data_prevista");
        manutencao.setDataPrevista(dataPrevista != null ? dataPrevista.toLocalDate() : null);

        Date dataConclusao = rs.getDate("data_conclusao");
        manutencao.setDataConclusao(dataConclusao != null ? dataConclusao.toLocalDate() : null);

        manutencao.setIdUsuarioResponsavel(rs.getObject("id_usuario_responsavel", Integer.class));
        return manutencao;
    }

// CREATE

    public int inserir(Manutencao manutencao) {
        String sql = "INSERT INTO manutencao (id_painel, tipo_manutencao, descricao, status, data_abertura, data_prevista, data_conclusao, id_usuario_responsavel) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        // retorna o id gerado pelo banco
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, manutencao.getIdPainel());
            stmt.setString(2, manutencao.getTipoManutencao().name());
            stmt.setString(3, manutencao.getDescricao());
            stmt.setString(4, StatusManutencao.ABERTA.name());
            stmt.setDate(5, Date.valueOf(manutencao.getDataAbertura()));

            if (manutencao.getDataPrevista() != null) {
                stmt.setDate(6, Date.valueOf(manutencao.getDataPrevista()));
            } else {
                stmt.setNull(6, Types.DATE);
            }

            if (manutencao.getDataConclusao() != null) {
                stmt.setDate(7, Date.valueOf(manutencao.getDataConclusao()));
            } else {
                stmt.setNull(7, Types.DATE);
            }

            if (manutencao.getIdUsuarioResponsavel() != null) {
                stmt.setInt(8, manutencao.getIdUsuarioResponsavel());
            } else {
                stmt.setNull(8, Types.INTEGER);
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            return -1;

        } catch (SQLException e) {
            System.err.println("Erro ao inserir manutenção: " + e.getMessage());
            return -1;
        }
    }

    // READ por id

    public Manutencao buscarPorId(int id) {
        String sql = "SELECT * FROM manutencao WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar manutenção: " + e.getMessage());
        }

        return null;
    }

    // READ todos

    public List<Manutencao> listarTodos() {
        List<Manutencao> lista = new ArrayList<>();
        String sql = "SELECT * FROM manutencao ORDER BY data_abertura DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(montar(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar manutenções: " + e.getMessage());
        }

        return lista;
    }

    // UPDATE

    public boolean atualizar(Manutencao manutencao) {
        String sql = "UPDATE manutencao SET id_painel = ?, tipo_manutencao = ?, descricao = ?, status = ?, data_abertura = ?, data_prevista = ?, data_conclusao = ?, id_usuario_responsavel = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, manutencao.getIdPainel());
            stmt.setString(2, manutencao.getTipoManutencao().name());
            stmt.setString(3, manutencao.getDescricao());
            stmt.setString(4, manutencao.getStatus().name());
            stmt.setDate(5, Date.valueOf(manutencao.getDataAbertura()));

            if (manutencao.getDataPrevista() != null) {
                stmt.setDate(6, Date.valueOf(manutencao.getDataPrevista()));
            } else {
                stmt.setNull(6, Types.DATE);
            }

            if (manutencao.getDataConclusao() != null) {
                stmt.setDate(7, Date.valueOf(manutencao.getDataConclusao()));
            } else {
                stmt.setNull(7, Types.DATE);
            }

            if (manutencao.getIdUsuarioResponsavel() != null) {
                stmt.setInt(8, manutencao.getIdUsuarioResponsavel());
            } else {
                stmt.setNull(8, Types.INTEGER);
            }

            stmt.setInt(9, manutencao.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar manutenção: " + e.getMessage());
            return false;
        }
    }

    // DELETE

    public boolean excluir(int id) {
        String sql = "DELETE FROM manutencao WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir manutenção: " + e.getMessage());
            return false;
        }
    }
}