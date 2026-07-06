import org.apollo.apollowebcrud.model.*;
import org.apollo.apollowebcrud.DAO.*;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainDeletev2{
    static void main() {
        org.apollo.apollowebcrud.DAO.UsuarioDAO userDao = null;
        org.apollo.apollowebcrud.DAO.CadastroDAO cadsDAO = null;
        org.apollo.apollowebcrud.DAO.LeituraDAO leiDAO = null;
        org.apollo.apollowebcrud.DAO.LoteDAO loteDAO = null;
        org.apollo.apollowebcrud.DAO.PainelDAO pnlDAO = null;
        org.apollo.apollowebcrud.DAO.SugestaoRevendaDAO sgstRevDAO = null;
        System.out.println("CONECTANDO COM O BANCO DE DADOS...");
        boolean seguir=false;
        try {
            userDao = new org.apollo.apollowebcrud.DAO.UsuarioDAO();
            cadsDAO = new org.apollo.apollowebcrud.DAO.CadastroDAO();
            leiDAO = new org.apollo.apollowebcrud.DAO.LeituraDAO();
            loteDAO = new org.apollo.apollowebcrud.DAO.LoteDAO();
            pnlDAO = new org.apollo.apollowebcrud.DAO.PainelDAO();
            sgstRevDAO = new org.apollo.apollowebcrud.DAO.SugestaoRevendaDAO();
            System.out.println("CONEXÃO CONCLUÍDA COM SUCESSO");
            seguir = true;
        } catch (Exception ex){
            Logger.getLogger(MainDeletev2.class.getName()).log(Level.SEVERE, "Falha geral ao tentar conectar os módulos do sistema: " + ex.getMessage(), ex);
            System.out.println("A inicialização do sistema falhou. Verifique os logs acima.");
        }
        if(seguir) {
            //TESTE LEITURA

            org.apollo.apollowebcrud.model.Leitura lei1 = new org.apollo.apollowebcrud.model.Leitura(4, LocalDate.of(2001, 9, 11), 1, 1);
            System.out.println(leiDAO.deletar(lei1));
            //TESTE SUGESTA

            org.apollo.apollowebcrud.model.SugestaoRevenda sgstRev1 = new org.apollo.apollowebcrud.model.SugestaoRevenda(4, 1, 1.0);
            System.out.println(sgstRevDAO.deletar(sgstRev1));
            //TESTE PAINEL

            org.apollo.apollowebcrud.model.Painel pnl1 = new org.apollo.apollowebcrud.model.Painel(4, "teste", "teste", "teste", 1, 1, "teste", 1);
            System.out.println(pnlDAO.deletar(pnl1));
            //TESTE LOTE

            org.apollo.apollowebcrud.model.Lote lote1 = new org.apollo.apollowebcrud.model.Lote(4, 1, 1, "teste", 1, "teste");
            System.out.println(loteDAO.deletar(lote1));
            //TESTE CADASTRO

            org.apollo.apollowebcrud.model.Cadastro cads1 = new org.apollo.apollowebcrud.model.Cadastro(4, LocalDate.of(2001, 9, 11), "teste.png", 1);
            System.out.println(cadsDAO.deletar(cads1));
            //TESTE USUARIO

            org.apollo.apollowebcrud.model.Usuario user1 = new org.apollo.apollowebcrud.model.Usuario(4, "teste", "teste@teste", "111.111.111/11", "11111", "teste");
            System.out.println(userDao.deletar(user1));
        }
    }
}