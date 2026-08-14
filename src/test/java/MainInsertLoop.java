import org.apollo.apollowebcrud.DAO.SugestaoRealocacaoDAO;

import java.time.LocalDate;

public class MainInsertLoop {
    static void main() {
        org.apollo.apollowebcrud.DAO.UsuarioDAO userDao = new org.apollo.apollowebcrud.DAO.UsuarioDAO();
        org.apollo.apollowebcrud.DAO.CadastroDAO cadsDAO = new org.apollo.apollowebcrud.DAO.CadastroDAO();
        org.apollo.apollowebcrud.DAO.LoteDAO loteDAO = new org.apollo.apollowebcrud.DAO.LoteDAO();
        org.apollo.apollowebcrud.DAO.PainelDAO pnlDAO = new org.apollo.apollowebcrud.DAO.PainelDAO();
        org.apollo.apollowebcrud.DAO.LeituraDAO leiDAO = new org.apollo.apollowebcrud.DAO.LeituraDAO();
        SugestaoRealocacaoDAO sgstRevDAO = new org.apollo.apollowebcrud.DAO.SugestaoRealocacaoDAO();
        for (int i = 0; i < 20; i++) {
            //TESTE USUARIO

            org.apollo.apollowebcrud.model.Usuario user1 = new org.apollo.apollowebcrud.model.Usuario(1, "teste", "teste@teste", "111.111.111/"+i, "#1111111", "teste");
            System.out.println(userDao.inserir(user1));
            //TESTE CADASTRO

            org.apollo.apollowebcrud.model.Cadastro cads1 = new org.apollo.apollowebcrud.model.Cadastro(1, LocalDate.of(2001, 9, 11), "teste.png", i+1);
            System.out.println(cadsDAO.inserir(cads1));
            //TESTE LOTE

            org.apollo.apollowebcrud.model.Lote lote1 = new org.apollo.apollowebcrud.model.Lote(1, 1, 1, "teste", i+1, "teste");
            System.out.println(loteDAO.inserir(lote1));
            //TESTE PAINEL

            org.apollo.apollowebcrud.model.Painel pnl1 = new org.apollo.apollowebcrud.model.Painel(1, "teste", "teste", "teste", 1, 1, "teste", i+1);
            System.out.println(pnlDAO.inserir(pnl1));
            //TESTE LEITURA

            org.apollo.apollowebcrud.model.Leitura lei1 = new org.apollo.apollowebcrud.model.Leitura(1, LocalDate.of(2001, 9, 11), i+1, i+1);
            System.out.println(leiDAO.inserir(lei1));
            //TESTE SUGESTAO

            org.apollo.apollowebcrud.model.SugestaoRevenda sgstRev1 = new org.apollo.apollowebcrud.model.SugestaoRevenda(1, i+1, 1.0);
            System.out.println(sgstRevDAO.inserir(sgstRev1));
        }
    }
}
