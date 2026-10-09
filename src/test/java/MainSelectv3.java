import org.apollo.apollowebcrud.DAO.*;
import org.apollo.apollowebcrud.model.*;

import java.util.List;

public class MainSelectv3 {

    public static void main(String[] args) {

        SegmentoDAO segmentoDAO = new SegmentoDAO();
        TipoUsuarioDAO tipoUsuarioDAO = new TipoUsuarioDAO();
        EmpresaDAO empresaDAO = new EmpresaDAO();
        FilialDAO filialDAO = new FilialDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        InversorDAO inversorDAO = new InversorDAO();
        StringSolarDAO stringSolarDAO = new StringSolarDAO();
        PainelDAO painelDAO = new PainelDAO();
        ManutencaoDAO manutencaoDAO = new ManutencaoDAO();
        ContratoDAO contratoDAO = new ContratoDAO();

        System.out.println("----------------- Segmentos -----------------");
        List<Segmento> segmentos = segmentoDAO.listarTodos();
        for (Segmento s : segmentos) System.out.println(s);

        System.out.println("----------------- Tipos de Usuário -----------------");
        List<TipoUsuario> tipos = tipoUsuarioDAO.listarTodos();
        for (TipoUsuario t : tipos) System.out.println(t);

        System.out.println("----------------- Empresas -----------------");
        List<Empresa> empresas = empresaDAO.listarTodos();
        for (Empresa e : empresas) System.out.println(e);

        System.out.println("----------------- Filiais -----------------");
        List<Filial> filiais = filialDAO.listarTodos();
        for (Filial f : filiais) System.out.println(f);

        System.out.println("----------------- Usuários -----------------");
        List<Usuario> usuarios = usuarioDAO.listarTodos();
        for (Usuario u : usuarios) System.out.println(u);

        System.out.println("----------------- Inversores -----------------");
        List<Inversor> inversores = inversorDAO.listarTodos();
        for (Inversor i : inversores) System.out.println(i);

        System.out.println("----------------- Strings Solares -----------------");
        List<StringSolar> strings = stringSolarDAO.listarTodos();
        for (StringSolar s : strings) System.out.println(s);

        System.out.println("----------------- Painéis -----------------");
        List<Painel> paineis = painelDAO.listarTodos();
        for (Painel p : paineis) System.out.println(p);

        System.out.println("----------------- Manutenções -----------------");
        List<Manutencao> manutencoes = manutencaoDAO.listarTodos();
        for (Manutencao m : manutencoes) System.out.println(m);

        System.out.println("----------------- Contratos -----------------");
        List<Contrato> contratos = contratoDAO.listarTodos();
        for (Contrato c : contratos) System.out.println(c);
    }
}