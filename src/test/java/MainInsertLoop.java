import org.apollo.apollowebcrud.DAO.*;
import org.apollo.apollowebcrud.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MainInsertLoop {

    public static void main(String[] args) {

        EmpresaDAO empresaDAO = new EmpresaDAO();
        SegmentoDAO segmentoDAO = new SegmentoDAO();
        FilialDAO filialDAO = new FilialDAO();
        TipoUsuarioDAO tipoUsuarioDAO = new TipoUsuarioDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        InversorDAO inversorDAO = new InversorDAO();
        StringSolarDAO stringSolarDAO = new StringSolarDAO();
        PainelDAO painelDAO = new PainelDAO();
        ManutencaoDAO manutencaoDAO = new ManutencaoDAO();
        ContratoDAO contratoDAO = new ContratoDAO();

        for (int i = 0; i < 1; i++) {

            // TESTE SEGMENTO
            Segmento segmento = new Segmento(0, "Agronegócio", "Segmento agrícola");
            System.out.println("Segmento: " + segmentoDAO.inserir(segmento));

            // TESTE TIPO USUÁRIO
            TipoUsuario tipoUsuario = new TipoUsuario(0, TipoUsuario.Tipo.ADMINISTRADOR, "Acesso total");
            System.out.println("TipoUsuario: " + tipoUsuarioDAO.inserir(tipoUsuario));

            // TESTE EMPRESA
            Empresa empresa = new Empresa();
            empresa.setNome("Empresa Teste " + (i + 1));
            empresa.setDescricao("Descrição teste");
            empresa.setCnpj("0000000000000" + (i + 1));
            empresa.setEmailContato("empresa@teste.com");
            empresa.setUrlFoto(null);

            Endereco enderecoEmpresa = new Endereco();
            enderecoEmpresa.setCep("01310100");
            enderecoEmpresa.setLogradouro("Av. Paulista");
            enderecoEmpresa.setNumero("1000");
            enderecoEmpresa.setComplemento("Sala 1");
            enderecoEmpresa.setBairro("Bela Vista");
            enderecoEmpresa.setCidade("São Paulo");
            enderecoEmpresa.setEstado("SP");

            System.out.println("Empresa: " + empresaDAO.inserir(empresa, enderecoEmpresa));

            // TESTE FILIAL
            Filial filial = new Filial();
            filial.setIdEmpresa(1);
            filial.setIdSegmento(1);
            filial.setNome("Filial Teste " + (i + 1));
            filial.setCnpj("1111111111111" + (i + 1));
            filial.setEmail("filial@teste.com");
            filial.setTelefone("11999999999");

            Endereco enderecoFilial = new Endereco();
            enderecoFilial.setCep("01310200");
            enderecoFilial.setLogradouro("Rua Teste");
            enderecoFilial.setNumero("200");
            enderecoFilial.setComplemento(null);
            enderecoFilial.setBairro("Centro");
            enderecoFilial.setCidade("São Paulo");
            enderecoFilial.setEstado("SP");

            System.out.println("Filial: " + filialDAO.inserir(filial, enderecoFilial));

            // TESTE USUARIO
            Usuario usuario = new Usuario();
            usuario.setNome("Usuario Teste " + (i + 1));
            usuario.setIdTipoUsuario(1);
            usuario.setIdEmpresa(1);
            usuario.setIdFilial(1);
            usuario.setEmail("usuario" + (i + 1) + "@teste.com");
            usuario.setTelefone("11988888888");
            usuario.setCpf("0000000000" + (i + 1));
            usuario.setSenhaHash("hash_teste");
            usuario.setAtivo("ATIVO");
            System.out.println("Usuario: " + usuarioDAO.inserir(usuario));

            // TESTE INVERSOR
            Inversor inversor = new Inversor();
            inversor.setIdFilial(1);
            inversor.setCodigo("INV-" + (i + 1));
            inversor.setNumeroSerie("NS-INV-" + (i + 1));
            inversor.setMarca("Fronius");
            inversor.setModelo("Symo 10");
            inversor.setPotenciaKw(new BigDecimal("10.0"));
            inversor.setDataInstalacao(LocalDate.of(2024, 1, 15));

            System.out.println("Inversor: " + inversorDAO.inserir(inversor));

            // TESTE STRING SOLAR
            StringSolar stringSolar = new StringSolar();
            stringSolar.setIdInversor(1);
            stringSolar.setCodigo("STR-" + (i + 1));
            stringSolar.setNumeroMppt(1);
            stringSolar.setNumeroEntrada(1);

            System.out.println("StringSolar: " + stringSolarDAO.inserir(stringSolar));

            // TESTE PAINEL
            Painel painel = new Painel();
            painel.setIdString(1);
            painel.setNumeroSerie("NS-PNL-" + (i + 1));
            painel.setMarca("Canadian Solar");
            painel.setModelo("CS6R-410");
            painel.setPotenciaW(new BigDecimal("410.0"));
            painel.setNotaFiscal("NF-" + (i + 1));
            painel.setDataInstalacao(LocalDate.of(2024, 1, 15));

            System.out.println("Painel: " + painelDAO.inserir(painel));

            // TESTE MANUTENÇÃO
            Manutencao manutencao = new Manutencao();
            manutencao.setIdPainel(1);
            manutencao.setTipoManutencao(TipoManutencao.CORRETIVA);
            manutencao.setDescricao("Manutenção teste " + (i + 1));
            manutencao.setDataAbertura(LocalDate.now());
            manutencao.setDataPrevista(LocalDate.now().plusDays(7));
            manutencao.setDataConclusao(null);
            manutencao.setIdUsuarioResponsavel(1);

            System.out.println("Manutencao: " + manutencaoDAO.inserir(manutencao));

            // TESTE CONTRATO
            Contrato contrato = new Contrato();
            contrato.setIdEmpresa(1);
            contrato.setDataInicio(LocalDate.of(2024, 1, 1));
            contrato.setValorMensal(new BigDecimal("1500.00"));
            contrato.setIdUsuarioCadastro(1);

            System.out.println("Contrato: " + contratoDAO.inserir(contrato));
        }
    }
}