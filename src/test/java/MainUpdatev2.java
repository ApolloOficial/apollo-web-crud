import org.apollo.apollowebcrud.DAO.SugestaoRealocacaoDAO;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainUpdatev2 {
    static void main() {
        org.apollo.apollowebcrud.DAO.UsuarioDAO userDao = null;
        org.apollo.apollowebcrud.DAO.CadastroDAO cadsDAO = null;
        org.apollo.apollowebcrud.DAO.LeituraDAO leiDAO = null;
        org.apollo.apollowebcrud.DAO.LoteDAO loteDAO = null;
        org.apollo.apollowebcrud.DAO.PainelDAO pnlDAO = null;
        SugestaoRealocacaoDAO sgstReaDAO = null;
        System.out.println("CONECTANDO COM O BANCO DE DADOS...");
        boolean seguir=false;
        try {
            userDao = new org.apollo.apollowebcrud.DAO.UsuarioDAO();
            cadsDAO = new org.apollo.apollowebcrud.DAO.CadastroDAO();
            leiDAO = new org.apollo.apollowebcrud.DAO.LeituraDAO();
            loteDAO = new org.apollo.apollowebcrud.DAO.LoteDAO();
            pnlDAO = new org.apollo.apollowebcrud.DAO.PainelDAO();
            sgstReaDAO = new org.apollo.apollowebcrud.DAO.SugestaoRealocacaoDAO();
            System.out.println("CONEXÃO CONCLUÍDA COM SUCESSO");
            seguir = true;
        } catch (Exception ex){
            Logger.getLogger(MainUpdatev2.class.getName()).log(Level.SEVERE, "Falha geral ao tentar conectar os módulos do sistema: " + ex.getMessage(), ex);
            System.out.println("A inicialização do sistema falhou. Verifique os logs acima.");
        }
        if(seguir) {
            //TESTE USUARIO

            org.apollo.apollowebcrud.model.Usuario user1 = new org.apollo.apollowebcrud.model.Usuario(1, "teste2", "teste@teste2", "222.222.222/22", "#2222222", "teste2");
            System.out.println(userDao.atualizar(user1));
            //TESTE CADASTRO

            org.apollo.apollowebcrud.model.Cadastro cads1 = new org.apollo.apollowebcrud.model.Cadastro(1, LocalDate.of(2002, 9, 11), "teste2.png", 1);
            System.out.println(cadsDAO.atualizar(cads1));
            //TESTE LEITURA

            org.apollo.apollowebcrud.model.Leitura lei1 = new org.apollo.apollowebcrud.model.Leitura(1, LocalDate.of(2002, 9, 11), 1, 1);
            System.out.println(leiDAO.atualizar(lei1));
            //TESTE LOTE

            org.apollo.apollowebcrud.model.Lote lote1 = new org.apollo.apollowebcrud.model.Lote(1, 2, 2, "teste2", 1, "teste2");
            System.out.println(loteDAO.atualizar(lote1));
            //TESTE PAINEL

            org.apollo.apollowebcrud.model.Painel pnl1 = new org.apollo.apollowebcrud.model.Painel(1, "teste2", "teste2", "teste2", 2, 2, "teste2", 1);
            System.out.println(pnlDAO.atualizar(pnl1));
            //TESTE SUGESTAO

            org.apollo.apollowebcrud.model.SugestaoRealocacao sgstRea1 = new org.apollo.apollowebcrud.model.SugestaoRealocacao(1, 2, 2.0);
            System.out.println(sgstReaDAO.atualizar(sgstRea1));
        }
    }
}
