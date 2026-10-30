package org.apollo.apollowebcrud.dao;

import org.apollo.apollowebcrud.model.Contrato;
import org.apollo.apollowebcrud.model.Empresa;
import org.apollo.apollowebcrud.model.Endereco;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.Connection;
import java.sql.SQLException;

// Cadastro de empresa é uma transação com 4 gravações: endereço + empresa + contrato + vínculo do gerente.
// Ou grava tudo, ou não grava nada.
public class CadastroEmpresaDAO extends ApolloConnection {

    private final EmpresaDAO empresaDAO = new EmpresaDAO();
    private final ContratoDAO contratoDAO = new ContratoDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public CadastroEmpresaDAO() {
        super();
    }

    // Devolve o id da empresa criada, ou -1 se nada foi gravado
    public int cadastrar(Empresa empresa, Endereco endereco, Contrato contrato, int idGerente) {
        try (Connection connection = abrirConexao()) {
            connection.setAutoCommit(false);

            try {
                int idEmpresa = empresaDAO.inserirNaConexao(connection, empresa, endereco);

                contrato.setIdEmpresa(idEmpresa);
                contratoDAO.inserirNaConexao(connection, contrato);

                if (!usuarioDAO.vincularEmpresaNaConexao(connection, idGerente, idEmpresa)) {
                    throw new SQLException("O gerente já está vinculado a uma empresa.");
                }

                connection.commit();
                return idEmpresa;

            } catch (SQLException e) {
                desfazer(connection);
                throw e;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar empresa: " + e.getMessage());
            return -1;
        }
    }
}
