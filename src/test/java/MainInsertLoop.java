import org.example.model.*;
import org.example.DAO.*;

import java.time.LocalDate;

public class MainInsertLoop {
    static void main() {
        UsuarioDAO userDao = new UsuarioDAO();
        CadastroDAO cadsDAO = new CadastroDAO();
        LoteDAO loteDAO = new LoteDAO();
        PainelDAO pnlDAO = new PainelDAO();
        LeituraDAO leiDAO = new LeituraDAO();
        SugestaoRevendaDAO sgstRevDAO = new SugestaoRevendaDAO();
        for (int i = 0; i < 10; i++) {
            //TESTE USUARIO

            Usuario user1 = new Usuario(1, "teste", "teste@teste", "111.111.111/"+i, "#1111111", "teste");
            System.out.println(userDao.inserir(user1));
            //TESTE CADASTRO

            Cadastro cads1 = new Cadastro(1, LocalDate.of(2001, 9, 11), "teste.png", i+1);
            System.out.println(cadsDAO.inserir(cads1));
            //TESTE LOTE

            Lote lote1 = new Lote(1, 1, 1, "teste", i+1, "teste");
            System.out.println(loteDAO.inserir(lote1));
            //TESTE PAINEL

            Painel pnl1 = new Painel(1, "teste", "teste", "teste", 1, 1, "teste", i+1);
            System.out.println(pnlDAO.inserir(pnl1));
            //TESTE LEITURA

            Leitura lei1 = new Leitura(1, LocalDate.of(2001, 9, 11), i+1, i+1);
            System.out.println(leiDAO.inserir(lei1));
            //TESTE SUGESTAO

            SugestaoRevenda sgstRev1 = new SugestaoRevenda(1, i+1, 1.0);
            System.out.println(sgstRevDAO.inserir(sgstRev1));
        }
    }
}
