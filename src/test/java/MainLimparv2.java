import org.apollo.apollowebcrud.model.*;
import org.apollo.apollowebcrud.DAO.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainLimparv2 {
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
            Logger.getLogger(MainLimparv2.class.getName()).log(Level.SEVERE, "Falha geral ao tentar conectar os módulos do sistema: " + ex.getMessage(), ex);
            System.out.println("A inicialização do sistema falhou. Verifique os logs acima.");
        }
        if(seguir) {
            //TESTE LEITURA

            List<org.apollo.apollowebcrud.model.Leitura> leiList = leiDAO.listar();
            for (org.apollo.apollowebcrud.model.Leitura l : leiList) {
                System.out.println(leiDAO.deletar(l));
            }
            //TESTE SUGESTAO

            List<org.apollo.apollowebcrud.model.SugestaoRevenda> sgstRevList = sgstRevDAO.listar();
            for (org.apollo.apollowebcrud.model.SugestaoRevenda s : sgstRevList) {
                System.out.println(sgstRevDAO.deletar(s));
            }
            //TESTE PAINEL

            List<org.apollo.apollowebcrud.model.Painel> pnList = pnlDAO.listar();
            for (org.apollo.apollowebcrud.model.Painel p : pnList) {
                System.out.println(pnlDAO.deletar(p));
            }
            //TESTE LOTE

            List<org.apollo.apollowebcrud.model.Lote> loteList = loteDAO.listar();
            for (org.apollo.apollowebcrud.model.Lote l : loteList) {
                System.out.println(loteDAO.deletar(l));
            }
            //TESTE CADASTRO

            List<org.apollo.apollowebcrud.model.Cadastro> cadList = cadsDAO.listar();
            for (org.apollo.apollowebcrud.model.Cadastro c : cadList) {
                System.out.println(cadsDAO.deletar(c));
            }
            //TESTE USUARIO

            List<org.apollo.apollowebcrud.model.Usuario> userList = userDao.listar();
            for (org.apollo.apollowebcrud.model.Usuario u : userList) {
                System.out.println(userDao.deletar(u));
            }
        }
    }
}
