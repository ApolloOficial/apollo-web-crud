import org.apollo.apollowebcrud.dao.*;
import org.apollo.apollowebcrud.model.*;

import java.util.List;

public class MainLimparv3 {

    public static void main(String[] args) {

        ContratoDAO contratoDAO = new ContratoDAO();
        ManutencaoDAO manutencaoDAO = new ManutencaoDAO();
        PainelDAO painelDAO = new PainelDAO();
        StringSolarDAO stringSolarDAO = new StringSolarDAO();
        InversorDAO inversorDAO = new InversorDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        TipoUsuarioDAO tipoUsuarioDAO = new TipoUsuarioDAO();
        FilialDAO filialDAO = new FilialDAO();
        EmpresaDAO empresaDAO = new EmpresaDAO();
        SegmentoDAO segmentoDAO = new SegmentoDAO();

        // LIMPAR CONTRATO
        List<Contrato> contratos = contratoDAO.listarTodos();
        for (Contrato c : contratos) {
            System.out.println("Contrato: " + contratoDAO.excluir(c.getId()));
        }

        // LIMPAR MANUTENÇÃO
        List<Manutencao> manutencoes = manutencaoDAO.listarTodos();
        for (Manutencao m : manutencoes) {
            System.out.println("Manutencao: " + manutencaoDAO.excluir(m.getId()));
        }

        // LIMPAR PAINEL
        List<Painel> paineis = painelDAO.listarTodos();
        for (Painel p : paineis) {
            System.out.println("Painel: " + painelDAO.excluir(p.getId()));
        }

        // LIMPAR STRING SOLAR
        List<StringSolar> strings = stringSolarDAO.listarTodos();
        for (StringSolar s : strings) {
            System.out.println("StringSolar: " + stringSolarDAO.excluir(s.getId()));
        }

        // LIMPAR INVERSOR
        List<Inversor> inversores = inversorDAO.listarTodos();
        for (Inversor i : inversores) {
            System.out.println("Inversor: " + inversorDAO.excluir(i.getId()));
        }

        // LIMPAR USUARIO: o CRUD não apaga mais (exclusão lógica). Para zerar o banco de testes, rode o script SQL do banco.

        // LIMPAR TIPO USUARIO
        List<TipoUsuario> tipos = tipoUsuarioDAO.listarTodos();
        for (TipoUsuario t : tipos) {
            System.out.println("TipoUsuario: " + tipoUsuarioDAO.excluir(t.getId()));
        }

        // LIMPAR FILIAL: o CRUD não apaga mais (exclusão lógica). Para zerar o banco de testes, rode o script SQL do banco.

        // LIMPAR EMPRESA: o CRUD não apaga mais (exclusão lógica). Para zerar o banco de testes, rode o script SQL do banco.

        // LIMPAR SEGMENTO
        List<Segmento> segmentos = segmentoDAO.listarTodos();
        for (Segmento sg : segmentos) {
            System.out.println("Segmento: " + segmentoDAO.excluir(sg.getId()));
        }
    }
}