import org.example.model.*;
import org.example.DAO.*;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainDeletev2{
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
            Logger.getLogger(MainDeletev2.class.getName()).log(Level.SEVERE, "Falha geral ao tentar conectar os módulos do sistema: " + ex.getMessage(), ex);
            System.out.println("A inicialização do sistema falhou. Verifique os logs acima.");
        }
        if(seguir) {
            //TESTE LEITURA

            Leitura lei1 = new Leitura(4, LocalDate.of(2001, 9, 11), 1, 1);
            System.out.println(leiDAO.deletar(lei1));
            //TESTE SUGESTA

            SugestaoRevenda sgstRev1 = new SugestaoRevenda(4, 1, 1.0);
            System.out.println(sgstRevDAO.deletar(sgstRev1));
            //TESTE PAINEL

            Painel pnl1 = new Painel(4, "teste", "teste", "teste", 1, 1, "teste", 1);
            System.out.println(pnlDAO.deletar(pnl1));
            //TESTE LOTE

            Lote lote1 = new Lote(4, 1, 1, "teste", 1, "teste");
            System.out.println(loteDAO.deletar(lote1));
            //TESTE CADASTRO

            Cadastro cads1 = new Cadastro(4, LocalDate.of(2001, 9, 11), "teste.png", 1);
            System.out.println(cadsDAO.deletar(cads1));
            //TESTE USUARIO

            Usuario user1 = new Usuario(4, "teste", "teste@teste", "111.111.111/11", "11111", "teste");
            System.out.println(userDao.deletar(user1));
        }
    }
}