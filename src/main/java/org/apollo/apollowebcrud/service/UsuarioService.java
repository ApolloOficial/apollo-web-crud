package org.apollo.apollowebcrud.service;

import org.apollo.apollowebcrud.dao.InativacaoDAO;
import org.apollo.apollowebcrud.dao.UsuarioDAO;
import org.apollo.apollowebcrud.model.Filial;
import org.apollo.apollowebcrud.model.StatusGeral;
import org.apollo.apollowebcrud.model.TipoUsuEnum;
import org.apollo.apollowebcrud.model.Usuario;
import org.apollo.apollowebcrud.util.CpfUtils;
import org.apollo.apollowebcrud.util.EmailUtils;
import org.apollo.apollowebcrud.util.SenhaUtils;
import org.apollo.apollowebcrud.util.TelefoneUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final FilialService filialService = new FilialService();
    private final EmpresaService empresaService = new EmpresaService();
    private final InativacaoDAO inativacaoDAO = new InativacaoDAO();
    private final TipoUsuarioService tipoUsuarioService = new TipoUsuarioService();

    // ---------- LOGIN ----------

    public Usuario validarLogin(String email, String senha, TipoUsuEnum areaSelecionada) {
        if (areaSelecionada == null || !dadosLoginPreenchidos(email, senha)) {
            return null;
        }

        Usuario usuario = usuarioDAO.buscarParaLogin(email.trim());

        if (usuario == null || !SenhaUtils.verificar(senha, usuario.getSenhaHash()) || !estaAtivo(usuario)) {
            return null;
        }

        usuario.setPerfil(tipoUsuarioService.buscarPerfil(usuario.getIdTipoUsuario()));
        if (usuario.getPerfil() == null || usuario.getPerfil() != areaSelecionada) {
            return null;
        }
        usuario.setSenhaHash(null); // a senha nunca sai do Service

        return usuario;
    }

    // Recarrega perfil, vínculo e status do banco antes de autorizar cada requisição protegida.
    public Usuario revalidarSessao(int idUsuario) {
        if (idUsuario <= 0) {
            return null;
        }

        final Usuario usuario;
        try {
            usuario = usuarioDAO.buscarParaRevalidarSessao(idUsuario);
        } catch (SQLException e) {
            throw new FalhaRevalidacaoSessaoException(e);
        }

        if (usuario == null || !estaAtivo(usuario) || usuario.getPerfil() == null) {
            return null;
        }

        usuario.setSenhaHash(null);
        return usuario;
    }

    // Gerente que ainda não cadastrou a empresa começa pela tela de empresa
    public String paginaInicial(Usuario usuario) {
        boolean gerenteSemEmpresa = usuario.getPerfil() == TipoUsuEnum.GERENTE && usuario.getIdEmpresa() == null;
        return gerenteSemEmpresa ? "/empresa" : "/dashboard";
    }

    private boolean dadosLoginPreenchidos(String email, String senha) {
        return email != null && email.contains("@")
                && senha != null && !senha.isEmpty();
    }

    private boolean estaAtivo(Usuario usuario) {
        return StatusGeral.ATIVO.name().equals(usuario.getStatus());
    }

    // ---------- LISTAGEM ----------

    // Gerente vê os técnicos da empresa; técnico vê os operadores da filial; operador não tem a tela
    public List<Usuario> listarVisiveis(Usuario logado) {
        if (logado.getPerfil() == TipoUsuEnum.GERENTE && logado.getIdEmpresa() != null) {
            return comPerfil(usuarioDAO.listarPorEmpresaETipo(logado.getIdEmpresa(), TipoUsuEnum.TECNICO.name()), TipoUsuEnum.TECNICO);
        }
        if (logado.getPerfil() == TipoUsuEnum.TECNICO && logado.getIdFilial() != null) {
            return comPerfil(usuarioDAO.listarPorFilialETipo(logado.getIdFilial(), TipoUsuEnum.OPERADOR.name()), TipoUsuEnum.OPERADOR);
        }
        return List.of();
    }

    // A lista é sempre de um único tipo, então o perfil de cada linha já é conhecido (a view usa ${u.perfil})
    private List<Usuario> comPerfil(List<Usuario> usuarios, TipoUsuEnum perfil) {
        usuarios.forEach(u -> u.setPerfil(perfil));
        return usuarios;
    }

    // Técnicos e operadores da filial (candidatos a responsável por manutenção)
    public List<Usuario> listarResponsaveisDaFilial(Usuario logado) {
        if (!logado.podeOperarEquipamentos()) {
            return List.of();
        }
        List<Usuario> responsaveis = new java.util.ArrayList<>(usuarioDAO.listarPorFilialETipo(logado.getIdFilial(), TipoUsuEnum.TECNICO.name()));
        responsaveis.addAll(usuarioDAO.listarPorFilialETipo(logado.getIdFilial(), TipoUsuEnum.OPERADOR.name()));
        return responsaveis;
    }

    // O usuário existe, está ativo, é técnico ou operador e é da filial informada
    public boolean ehOperacionalDaFilial(Integer idUsuario, int idFilial) {
        if (idUsuario == null) {
            return false;
        }
        Usuario usuario = usuarioDAO.buscarPorId(idUsuario);
        if (usuario == null || !StatusGeral.ATIVO.name().equals(usuario.getStatus())
                || !Objects.equals(usuario.getIdFilial(), idFilial)) {
            return false;
        }
        TipoUsuEnum perfil = tipoUsuarioService.buscarPerfil(usuario.getIdTipoUsuario());
        return perfil == TipoUsuEnum.TECNICO || perfil == TipoUsuEnum.OPERADOR;
    }

    // ---------- CADASTRO ----------

    // Devolve null se cadastrou, ou a mensagem de erro
    public String cadastrarUsuario(Usuario novo, Usuario cadastrador) {
        TipoUsuEnum tipoPermitido = tipoQueOCadastradorPodeCriar(cadastrador);
        if (tipoPermitido == null) {
            return "Você não tem permissão para cadastrar usuários";
        }

        String erro = definirTipo(novo, tipoPermitido);
        if (erro == null) erro = definirVinculos(novo, cadastrador);
        if (erro == null) erro = validarDadosPessoais(novo);
        if (erro == null) erro = validarSenha(novo.getSenhaHash());
        if (erro != null) {
            return erro;
        }

        prepararParaGravar(novo, cadastrador);

        return usuarioDAO.inserir(novo) > 0
                ? null
                : "Erro interno no servidor ao tentar salvar o usuário";
    }

    // ---------- EDIÇÃO, INATIVAÇÃO E REATIVAÇÃO ----------
    // Gerente gerencia os técnicos da empresa dele; técnico gerencia os operadores da filial dele.

    // dados.getId() identifica quem editar. Não muda tipo, empresa nem filial. Senha nova é opcional.
    public String editarUsuario(Usuario dados, String novaSenha, Usuario logado) {
        Usuario atual = buscarGerenciavel(dados.getId(), logado);
        if (atual == null) {
            return "Usuário não encontrado ou sem permissão";
        }

        String erro = validarDadosPessoais(dados);
        boolean trocaSenha = novaSenha != null && !novaSenha.isEmpty();
        if (erro == null && trocaSenha) erro = validarSenha(novaSenha);
        if (erro != null) {
            return erro;
        }

        atual.setNome(dados.getNome().trim());
        atual.setEmail(dados.getEmail().trim());
        atual.setCpf(CpfUtils.removerMascara(dados.getCpf()));
        atual.setTelefone(telefoneSomenteDigitos(dados.getTelefone()));
        atual.setUrlFoto(dados.getUrlFoto() == null || dados.getUrlFoto().isBlank() ? null : dados.getUrlFoto().trim());

        boolean salvou = usuarioDAO.atualizar(atual)
                && (!trocaSenha || usuarioDAO.atualizarSenha(atual.getId(), SenhaUtils.gerarHash(novaSenha)));

        return salvou ? null : "Erro interno no servidor ao tentar salvar o usuário";
    }

    // Funcionário saiu da empresa: o usuário fica INATIVO (não é apagado) e não consegue mais entrar
    public String inativarUsuario(int idAlvo, Usuario logado) {
        if (buscarGerenciavel(idAlvo, logado) == null) {
            return "Usuário não encontrado ou sem permissão";
        }
        return inativacaoDAO.inativarUsuario(idAlvo) ? null : "Erro interno no servidor ao inativar o usuário";
    }

    // Só reativa se a filial e a empresa do usuário estiverem ativas
    public String reativarUsuario(int idAlvo, Usuario logado) {
        Usuario alvo = buscarGerenciavel(idAlvo, logado);
        if (alvo == null) {
            return "Usuário não encontrado ou sem permissão";
        }
        if (!empresaService.estaAtiva(alvo.getIdEmpresa()) || !filialService.estaAtiva(alvo.getIdFilial())) {
            return "A filial e a empresa precisam estar ativas para reativar o usuário";
        }
        return inativacaoDAO.reativarUsuario(idAlvo) ? null : "Erro interno no servidor ao reativar o usuário";
    }

    // O usuário só é devolvido se é do tipo que o logado gerencia e está no escopo dele
    private Usuario buscarGerenciavel(int idAlvo, Usuario logado) {
        TipoUsuEnum tipoGerenciavel = tipoQueOCadastradorPodeCriar(logado);
        if (tipoGerenciavel == null) {
            return null;
        }

        Usuario alvo = usuarioDAO.buscarPorId(idAlvo);
        if (alvo == null || tipoUsuarioService.buscarPerfil(alvo.getIdTipoUsuario()) != tipoGerenciavel) {
            return null;
        }

        boolean mesmoEscopo = logado.getPerfil() == TipoUsuEnum.GERENTE
                ? Objects.equals(alvo.getIdEmpresa(), logado.getIdEmpresa())
                : Objects.equals(alvo.getIdFilial(), logado.getIdFilial());

        return mesmoEscopo ? alvo : null;
    }

    // Busca para o formulário de edição; não devolve usuários fora do escopo do gerente/técnico.
    public Usuario buscarParaEdicao(int idAlvo, Usuario logado) {
        Usuario alvo = buscarGerenciavel(idAlvo, logado);
        if (alvo != null) {
            alvo.setPerfil(tipoUsuarioService.buscarPerfil(alvo.getIdTipoUsuario()));
            alvo.setSenhaHash(null);
        }
        return alvo;
    }

    // Gerente cadastra técnico; técnico cadastra operador
    private TipoUsuEnum tipoQueOCadastradorPodeCriar(Usuario cadastrador) {
        if (cadastrador.getPerfil() == TipoUsuEnum.GERENTE) return TipoUsuEnum.TECNICO;
        if (cadastrador.getPerfil() == TipoUsuEnum.TECNICO) return TipoUsuEnum.OPERADOR;
        return null;
    }

    // O formulário não envia o tipo: quem cadastra define (gerente → técnico, técnico → operador)
    private String definirTipo(Usuario novo, TipoUsuEnum tipoPermitido) {
        Integer idTipo = tipoUsuarioService.buscarIdPorPerfil(tipoPermitido);
        if (idTipo == null) {
            return "Tipo de usuário " + tipoPermitido.name() + " não encontrado no banco";
        }
        novo.setIdTipoUsuario(idTipo);
        return null;
    }

    // A empresa vem sempre do cadastrador. A filial: do técnico é a dele; o gerente escolhe uma da empresa dele.
    private String definirVinculos(Usuario novo, Usuario cadastrador) {
        if (cadastrador.getIdEmpresa() == null) {
            return "Cadastre a empresa antes de cadastrar usuários";
        }
        novo.setIdEmpresa(cadastrador.getIdEmpresa());

        if (cadastrador.getPerfil() == TipoUsuEnum.TECNICO) {
            novo.setIdFilial(cadastrador.getIdFilial());
            return null;
        }

        if (novo.getIdFilial() == null) {
            return "A filial não foi informada";
        }
        Filial filial = filialService.buscarPorId(novo.getIdFilial());
        if (filial == null || filial.getIdEmpresa() != cadastrador.getIdEmpresa()) {
            return "A filial selecionada não pertence à sua empresa";
        }
        return null;
    }

    private String validarDadosPessoais(Usuario u) {
        if (u.getNome() == null || u.getNome().trim().isEmpty()) {
            return "O nome não foi informado";
        }
        String erro = validarEmail(u.getEmail(), u.getId());
        if (erro == null) erro = validarCpf(u.getCpf(), u.getId());
        if (erro == null) erro = validarTelefone(u.getTelefone());
        return erro;
    }

    // idIgnorar: na edição, o próprio usuário não conta como email/CPF repetido (no cadastro o id é 0)
    private String validarEmail(String email, int idIgnorar) {
        if (email == null || email.trim().isEmpty()) {
            return "O email não foi informado";
        }
        if (!EmailUtils.isEmailValido(email)) {
            return "O email não é válido";
        }
        Usuario mesmoEmail = usuarioDAO.buscarPorEmail(email.trim());
        if (mesmoEmail != null && mesmoEmail.getId() != idIgnorar) {
            return "Este email já foi cadastrado no sistema";
        }
        return null;
    }

    private String validarCpf(String cpf, int idIgnorar) {
        String cpfLimpo = CpfUtils.removerMascara(cpf);

        if (cpfLimpo.isEmpty()) {
            return "O CPF não foi informado";
        }
        if (!CpfUtils.isValido(cpfLimpo)) {
            return "O CPF não é válido";
        }
        Usuario mesmoCpf = usuarioDAO.buscarPorCpf(cpfLimpo);
        if (mesmoCpf != null && mesmoCpf.getId() != idIgnorar) {
            return "Este CPF já foi cadastrado no sistema";
        }
        return null;
    }

    // Telefone é opcional no banco
    private String validarTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return null;
        }
        return TelefoneUtils.isTelefoneValido(telefone.trim()) ? null : "O telefone não é válido";
    }

    // Na entrada, o campo senhaHash do objeto carrega a senha digitada; aqui ela vira hash antes de gravar
    public String validarSenha(String senha) {
        if (senha == null || senha.trim().isEmpty()) {
            return "A senha não foi informada";
        }
        if (senha.length() < 8) {
            return "A senha deve possuir pelo menos 8 caracteres";
        }
        if (!senha.matches(".*[!@#\\[\\]$%*&].*")) {
            return "A senha deve possuir pelo menos um destes caracteres especiais: !@[]#$%*&";
        }
        return null;
    }

    private void prepararParaGravar(Usuario novo, Usuario cadastrador) {
        novo.setNome(novo.getNome().trim());
        novo.setEmail(novo.getEmail().trim());
        novo.setCpf(CpfUtils.removerMascara(novo.getCpf()));
        novo.setTelefone(telefoneSomenteDigitos(novo.getTelefone()));
        novo.setSenhaHash(SenhaUtils.gerarHash(novo.getSenhaHash()));
        novo.setIdUsuarioCadastro(cadastrador.getId());
    }

    private String telefoneSomenteDigitos(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return null;
        }
        return telefone.replaceAll("\\D", "");
    }
}
