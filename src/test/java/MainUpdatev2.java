import org.example.model.*;
import org.example.DAO.*;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainUpdatev2 {
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
            Logger.getLogger(MainUpdatev2.class.getName()).log(Level.SEVERE, "Falha geral ao tentar conectar os módulos do sistema: " + ex.getMessage(), ex);
            System.out.println("A inicialização do sistema falhou. Verifique os logs acima.");
        }
        if(seguir) {
            //TESTE USUARIO

            Usuario user1 = new Usuario(1, "teste2", "teste@teste2", "222.222.222/22", "#2222222", "teste2");
            System.out.println(userDao.atualizar(user1));
            //TESTE CADASTRO

            Cadastro cads1 = new Cadastro(1, LocalDate.of(2002, 9, 11), "teste2.png", 1);
            System.out.println(cadsDAO.atualizar(cads1));
            //TESTE LEITURA

            Leitura lei1 = new Leitura(1, LocalDate.of(2002, 9, 11), 1, 1);
            System.out.println(leiDAO.atualizar(lei1));
            //TESTE LOTE

            Lote lote1 = new Lote(1, 2, 2, "teste2", 1, "teste2");
            System.out.println(loteDAO.atualizar(lote1));
            //TESTE PAINEL

            Painel pnl1 = new Painel(1, "teste2", "teste2", "teste2", 2, 2, "teste2", 1);
            System.out.println(pnlDAO.atualizar(pnl1));
            //TESTE SUGESTAO

            SugestaoRevenda sgstRev1 = new SugestaoRevenda(1, 2, 2.0);
            System.out.println(sgstRevDAO.atualizar(sgstRev1));
        }
    }
}
