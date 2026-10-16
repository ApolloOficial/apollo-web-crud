import org.apollo.apollowebcrud.DAO.*;
import org.apollo.apollowebcrud.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MainUpdatev3 {

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

        // TESTE SEGMENTO
        Segmento segmento = new Segmento(1, "Agronegócio Atualizado", "Descrição atualizada");
        System.out.println("Segmento: " + segmentoDAO.atualizar(segmento));

        // TESTE TIPO USUARIO
        TipoUsuario tipoUsuario = new TipoUsuario(1, TipoUsuEnum.GERENTE, "Descrição atualizada");
        System.out.println("TipoUsuario: " + tipoUsuarioDAO.atualizar(tipoUsuario));

        // TESTE EMPRESA

//      Criação do objeto
        Empresa empresa = new Empresa();
        empresa.setId(1);
        empresa.setNome("Empresa Atualizada");
        empresa.setDescricao("Descrição atualizada");
        empresa.setCnpj("10000000000001");
        empresa.setEmailContato("empresa.atualizada@teste.com");
        empresa.setStatus(StatusGeral.ATIVO);
        empresa.setUrlFoto(null);

//      Criando objeto de endereço
        Endereco enderecoEmpresa = new Endereco();
        enderecoEmpresa.setCep("01310100");
        enderecoEmpresa.setLogradouro("Av. Paulista Atualizada");
        enderecoEmpresa.setNumero("2000");
        enderecoEmpresa.setComplemento("Sala 2");
        enderecoEmpresa.setBairro("Bela Vista");
        enderecoEmpresa.setCidade("São Paulo");
        enderecoEmpresa.setEstado("SP");

//      Inserindo model no banco
        System.out.println("Empresa: " + empresaDAO.atualizar(empresa, enderecoEmpresa));

        // TESTE FILIAL
        Filial filial = new Filial();
        filial.setId(1);
        filial.setIdEmpresa(1);
        filial.setIdSegmento(1);
        filial.setNome("Filial Atualizada");
        filial.setCnpj("11111111111111");
        filial.setEmail("filial.atualizada@teste.com");
        filial.setTelefone("11999999999");
        filial.setStatus(StatusGeral.ATIVO);

        Endereco enderecoFilial = new Endereco();
        enderecoFilial.setCep("01310200");
        enderecoFilial.setLogradouro("Rua Atualizada");
        enderecoFilial.setNumero("300");
        enderecoFilial.setComplemento(null);
        enderecoFilial.setBairro("Centro");
        enderecoFilial.setCidade("São Paulo");
        enderecoFilial.setEstado("SP");

        System.out.println("Filial: " + filialDAO.atualizar(filial, enderecoFilial));

        // TESTE USUARIO
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("Usuario Atualizado");
        usuario.setIdTipoUsuario(1);
        usuario.setIdFilial(1);
        usuario.setEmail("atualizado@teste.com");
        usuario.setTelefone("11977777777");
        usuario.setCpf("99999999999");
        usuario.setStatus("ATIVO");
        System.out.println("Usuario: " + usuarioDAO.atualizar(usuario));

        // TESTE INVERSOR
        Inversor inversor = new Inversor();
        inversor.setId(1);
        inversor.setIdFilial(1);
        inversor.setCodigo("INV-ATUALIZADO");
        inversor.setNumeroSerie("NS-INV-ATUALIZADO");
        inversor.setMarca("SMA");
        inversor.setModelo("Sunny Boy 10");
        inversor.setPotenciaKw(12.0);
        inversor.setStatus(StatusGeral.ATIVO);
        inversor.setDataInstalacao(LocalDate.of(2024, 6, 1));
        System.out.println("Inversor: " + inversorDAO.atualizar(inversor));

        // TESTE STRING SOLAR
        StringSolar stringSolar = new StringSolar();
        stringSolar.setId(1);
        stringSolar.setIdInversor(1);
        stringSolar.setCodigo("STR-ATUALIZADO");
        stringSolar.setNumeroMppt(2);
        stringSolar.setNumeroEntrada(2);
        stringSolar.setStatus(StatusGeral.ATIVO);
        System.out.println("StringSolar: " + stringSolarDAO.atualizar(stringSolar));

        // TESTE PAINEL
        Painel painel = new Painel();
        painel.setId(1);
        painel.setIdString(1);
        painel.setNumeroSerie("NS-PNL-ATUALIZADO");
        painel.setMarca("Risen");
        painel.setModelo("RSM40-8-410M");
        painel.setPotenciaW(420.0);
        painel.setNotaFiscal("NF-ATUALIZADO");
        painel.setStatus(StatusGeral.ATIVO);
        painel.setDataInstalacao(LocalDate.of(2024, 6, 1));
        System.out.println("Painel: " + painelDAO.atualizar(painel));

        // TESTE MANUTENÇÃO
        Manutencao manutencao = new Manutencao();
        manutencao.setId(1);
        manutencao.setIdPainel(1);
        manutencao.setTipoManutencao(TipoManutencao.PREVENTIVA);
        manutencao.setDescricao("Manutenção atualizada");
        manutencao.setStatus(StatusManutencao.EM_ANDAMENTO);
        manutencao.setDataAbertura(LocalDate.now());
        manutencao.setDataPrevista(LocalDate.now().plusDays(5));
        manutencao.setDataConclusao(null);
        manutencao.setIdUsuarioResponsavel(1);
        System.out.println("Manutencao: " + manutencaoDAO.atualizar(manutencao));

        // TESTE CONTRATO
        Contrato contrato = new Contrato();
        contrato.setId(1);
        contrato.setIdEmpresa(1);
        contrato.setDataInicio(LocalDate.of(2024, 6, 1));
        contrato.setValorMensal(new BigDecimal("100000"));
        contrato.setStatus(StatusGeral.ATIVO);
        System.out.println("Contrato: " + contratoDAO.atualizar(contrato));
    }
}