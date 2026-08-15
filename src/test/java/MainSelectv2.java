import org.apollo.apollowebcrud.DAO.SugestaoRealocacaoDAO;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainSelectv2 {
    static void main() {
        org.apollo.apollowebcrud.DAO.UsuarioDAO userDao = null;
        org.apollo.apollowebcrud.DAO.CadastroDAO cadsDAO = null;
        org.apollo.apollowebcrud.DAO.LeituraDAO leiDAO = null;
        org.apollo.apollowebcrud.DAO.LoteDAO loteDAO = null;
        org.apollo.apollowebcrud.DAO.PainelDAO pnlDAO = null;
        SugestaoRealocacaoDAO sgstReaDAO = null;
        System.out.println("CONECTANDO COM O BANCO DE DADOS...");
        boolean seguir = false;
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
            Logger.getLogger(MainSelectv2.class.getName()).log(Level.SEVERE, "Falha geral ao tentar conectar os módulos do sistema: " + ex.getMessage(), ex);
            System.out.println("A inicialização do sistema falhou. Verifique os logs acima.");
        }
        if(seguir) {
            //TESTE USUARIO

            System.out.println("-----------------------------------------Usuários-----------------------------------------");
            List<org.apollo.apollowebcrud.model.Usuario> userList = userDao.listar();
            for (int i = 0; i < userList.size(); i++) {
                System.out.println(userList.get(i));
            }
            System.out.println();
            //TESTE CADASTRO

            System.out.println("-----------------------------------------Cadastros-----------------------------------------");
            List<org.apollo.apollowebcrud.model.Cadastro> cadList = cadsDAO.listar();
            for (int i = 0; i < cadList.size(); i++) {
                System.out.println(cadList.get(i));
            }
            System.out.println();
            //TESTE LEITURA

            System.out.println("-----------------------------------------Leituras-----------------------------------------");
            List<org.apollo.apollowebcrud.model.Leitura> leiList = leiDAO.listar();
            for (int i = 0; i < leiList.size(); i++) {
                System.out.println(leiList.get(i));
            }
            System.out.println();
            //TESTE LOTE

            System.out.println("-----------------------------------------Lotes-----------------------------------------");
            List<org.apollo.apollowebcrud.model.Lote> loteList = loteDAO.listar();
            for (int i = 0; i < loteList.size(); i++) {
                System.out.println(loteList.get(i));
            }
            System.out.println();
            //TESTE PAINEL

            System.out.println("-----------------------------------------Painéis-----------------------------------------");
            List<org.apollo.apollowebcrud.model.Painel> pnList = pnlDAO.listar();
            for (int i = 0; i < pnList.size(); i++) {
                System.out.println(pnList.get(i));
            }
            System.out.println();
            //TESTE SUGESTAO

            System.out.println("-----------------------------------------Sugestões de Revenda-----------------------------------------");
            List<org.apollo.apollowebcrud.model.SugestaoRealocacao> sgstReaList = sgstReaDAO.listar();
            for (int i = 0; i < sgstReaList.size(); i++) {
                System.out.println(sgstReaList.get(i));
            }
        }
    }
}
