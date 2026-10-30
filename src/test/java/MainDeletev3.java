import org.apollo.apollowebcrud.dao.*;

public class MainDeletev3 {
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

        System.out.println("Contrato: " + contratoDAO.excluir(1));
        System.out.println("Manutencao: " + manutencaoDAO.excluir(1));
        System.out.println("Painel: " + painelDAO.excluir(1));
        System.out.println("StringSolar: " + stringSolarDAO.excluir(1));
        System.out.println("Inversor: " + inversorDAO.excluir(1));
        System.out.println("TipoUsuario: " + tipoUsuarioDAO.excluir(1));
        System.out.println("Segmento: " + segmentoDAO.excluir(1));
    }
}