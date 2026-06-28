import org.example.model.*;
import org.example.DAO.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainLimparv2 {
    static void main() {
        UsuarioDAO userDao = null;
        CadastroDAO cadsDAO = null;
        LeituraDAO leiDAO = null;
        LoteDAO loteDAO = null;
        PainelDAO pnlDAO = null;
        SugestaoRevendaDAO sgstRevDAO = null;
        System.out.println("CONECTANDO COM O BANCO DE DADOS...");
        boolean seguir=false;
        try {
            userDao = new UsuarioDAO();
            cadsDAO = new CadastroDAO();
            leiDAO = new LeituraDAO();
            loteDAO = new LoteDAO();
            pnlDAO = new PainelDAO();
            sgstRevDAO = new SugestaoRevendaDAO();
            System.out.println("CONEXÃO CONCLUÍDA COM SUCESSO");
            seguir = true;
        } catch (Exception ex){
            Logger.getLogger(MainLimparv2.class.getName()).log(Level.SEVERE, "Falha geral ao tentar conectar os módulos do sistema: " + ex.getMessage(), ex);
            System.out.println("A inicialização do sistema falhou. Verifique os logs acima.");
        }
        if(seguir) {
            //TESTE LEITURA

            List<Leitura> leiList = leiDAO.listar();
            for (Leitura l : leiList) {
                System.out.println(leiDAO.deletar(l));
            }
            //TESTE SUGESTAO

            List<SugestaoRevenda> sgstRevList = sgstRevDAO.listar();
            for (SugestaoRevenda s : sgstRevList) {
                System.out.println(sgstRevDAO.deletar(s));
            }
            //TESTE PAINEL

            List<Painel> pnList = pnlDAO.listar();
            for (Painel p : pnList) {
                System.out.println(pnlDAO.deletar(p));
            }
            //TESTE LOTE

            List<Lote> loteList = loteDAO.listar();
            for (Lote l : loteList) {
                System.out.println(loteDAO.deletar(l));
            }
            //TESTE CADASTRO

            List<Cadastro> cadList = cadsDAO.listar();
            for (Cadastro c : cadList) {
                System.out.println(cadsDAO.deletar(c));
            }
            //TESTE USUARIO

            List<Usuario> userList = userDao.listar();
            for (Usuario u : userList) {
                System.out.println(userDao.deletar(u));
            }
        }
    }
}
