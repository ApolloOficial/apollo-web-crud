import org.example.model.*;
import org.example.DAO.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainSelectv2 {
    static void main() {
        UsuarioDAO userDao = null;
        CadastroDAO cadsDAO = null;
        LeituraDAO leiDAO = null;
        LoteDAO loteDAO = null;
        PainelDAO pnlDAO = null;
        SugestaoRevendaDAO sgstRevDAO = null;
        System.out.println("CONECTANDO COM O BANCO DE DADOS...");
        boolean seguir = false;
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
            Logger.getLogger(MainSelectv2.class.getName()).log(Level.SEVERE, "Falha geral ao tentar conectar os módulos do sistema: " + ex.getMessage(), ex);
            System.out.println("A inicialização do sistema falhou. Verifique os logs acima.");
        }
        if(seguir) {
            //TESTE USUARIO

            System.out.println("-----------------------------------------Usuários-----------------------------------------");
            List<Usuario> userList = userDao.listar();
            for (int i = 0; i < userList.size(); i++) {
                System.out.println(userList.get(i));
            }
            System.out.println();
            //TESTE CADASTRO

            System.out.println("-----------------------------------------Cadastros-----------------------------------------");
            List<Cadastro> cadList = cadsDAO.listar();
            for (int i = 0; i < cadList.size(); i++) {
                System.out.println(cadList.get(i));
            }
            System.out.println();
            //TESTE LEITURA

            System.out.println("-----------------------------------------Leituras-----------------------------------------");
            List<Leitura> leiList = leiDAO.listar();
            for (int i = 0; i < leiList.size(); i++) {
                System.out.println(leiList.get(i));
            }
            System.out.println();
            //TESTE LOTE

            System.out.println("-----------------------------------------Lotes-----------------------------------------");
            List<Lote> loteList = loteDAO.listar();
            for (int i = 0; i < loteList.size(); i++) {
                System.out.println(loteList.get(i));
            }
            System.out.println();
            //TESTE PAINEL

            System.out.println("-----------------------------------------Painéis-----------------------------------------");
            List<Painel> pnList = pnlDAO.listar();
            for (int i = 0; i < pnList.size(); i++) {
                System.out.println(pnList.get(i));
            }
            System.out.println();
            //TESTE SUGESTAO

            System.out.println("-----------------------------------------Sugestões de Revenda-----------------------------------------");
            List<SugestaoRevenda> sgstRevList = sgstRevDAO.listar();
            for (int i = 0; i < sgstRevList.size(); i++) {
                System.out.println(sgstRevList.get(i));
            }
        }
    }
}
