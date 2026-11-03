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

/**
 * Regras de negócio dos usuários. Concentra o login, a revalidação da sessão e o gerenciamento da equipe.
 * O gerente cadastra técnicos e o técnico cadastra operadores, cada um dentro do seu escopo de empresa ou filial.
 */
public class UsuarioService {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final FilialService filialService = new FilialService();
    private final EmpresaService empresaService = new EmpresaService();
    private final InativacaoDAO inativacaoDAO = new InativacaoDAO();
    private final TipoUsuarioService tipoUsuarioService = new TipoUsuarioService();

    // Hash usado quando o email não existe, para o tempo de resposta ser parecido com o de um email válido
    private final String hashFicticio = SenhaUtils.gerarHash("senha-ficticia-para-tempo-de-resposta");

    // ---------- LOGIN ----------

    /**
     * Autentica o usuário. O e-mail precisa existir, a senha deve conferir com o hash, o status tem que ser ATIVO
     * e a área escolhida na tela deve corresponder ao perfil. Qualquer falha devolve null. Quando o e-mail não
     * existe, uma verificação fictícia mantém o tempo de resposta parecido. A senha nunca volta no objeto devolvido.
     */
    public Usuario validarLogin(String email, String senha, TipoUsuEnum areaSelecionada) {
        String emailLimpo = email == null ? null : email.trim();

        if (areaSelecionada == null || !dadosLoginPreenchidos(emailLimpo, senha)) {
            return null;
        }

        Usuario usuario = usuarioDAO.buscarParaLogin(emailLimpo);

        if (usuario == null) {
            SenhaUtils.verificar(senha, hashFicticio); // gasta o mesmo tempo que uma senha real
            return null;
        }

        if (!SenhaUtils.verificar(senha, usuario.getSenhaHash()) || !estaAtivo(usuario)) {
            return null;
        }

        usuario.setPerfil(tipoUsuarioService.buscarPerfil(usuario.getIdTipoUsuario()));
        if (usuario.getPerfil() == null || usuario.getPerfil() != areaSelecionada) {
            return null;
        }

        usuario.setSenhaHash(null); // a senha nunca sai do Service

        return usuario;
    }

    /** Recarrega perfil, vínculos e status do banco antes de autorizar cada requisição protegida. */
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

    /** Define a primeira tela após o login. Gerente sem empresa começa pelo cadastro dela. */
    public String paginaInicial(Usuario usuario) {
        boolean gerenteSemEmpresa = usuario.getPerfil() == TipoUsuEnum.GERENTE && usuario.getIdEmpresa() == null;
        return gerenteSemEmpresa ? "/empresa" : "/dashboard";
    }

    /** Exige e-mail e senha preenchidos. */
    private boolean dadosLoginPreenchidos(String email, String senha) {
        return email != null && email.contains("@")
                && senha != null && !senha.isEmpty();
    }

    /** Verdadeiro quando o status do usuário é ATIVO. */
    private boolean estaAtivo(Usuario usuario) {
        return StatusGeral.ATIVO.name().equals(usuario.getStatus());
    }

    // ---------- LISTAGEM ----------

    /**
     * O gerente vê os técnicos e operadores da empresa, enquanto o técnico vê os operadores
     * da própria filial. O operador não possui acesso à tela de gerenciamento de membros.
     */
    public List<Usuario> listarVisiveis(Usuario logado) {

        if (logado.getPerfil() == TipoUsuEnum.GERENTE && logado.getIdEmpresa() != null) {
            List<Usuario> membros = new java.util.ArrayList<>(
                    comPerfil(
                            usuarioDAO.listarPorEmpresaETipo(
                                    logado.getIdEmpresa(), TipoUsuEnum.TECNICO.name()),
                            TipoUsuEnum.TECNICO)
            );

            membros.addAll(comPerfil(
                    usuarioDAO.listarPorEmpresaETipo(
                            logado.getIdEmpresa(), TipoUsuEnum.OPERADOR.name()),
                    TipoUsuEnum.OPERADOR));

            membros.sort(java.util.Comparator.comparing(
                    Usuario::getNome, String.CASE_INSENSITIVE_ORDER));

            return membros;
        }

        if (logado.getPerfil() == TipoUsuEnum.TECNICO && logado.getIdFilial() != null) {
            return comPerfil(
                    usuarioDAO.listarPorFilialETipo(
                            logado.getIdFilial(), TipoUsuEnum.OPERADOR.name()),
                    TipoUsuEnum.OPERADOR);
        }

        return List.of();
    }

    /** Preenche o perfil dos usuários retornados pelo DAO. */
    private List<Usuario> comPerfil(List<Usuario> usuarios, TipoUsuEnum perfil) {
        usuarios.forEach(u -> u.setPerfil(perfil));
        return usuarios;
    }

    /** Técnicos e operadores da filial, candidatos a responsável por uma manutenção. */
    public List<Usuario> listarResponsaveisDaFilial(Usuario logado) {
        if (!logado.podeOperarEquipamentos()) {
            return List.of();
        }

        List<Usuario> responsaveis = new java.util.ArrayList<>(
                comPerfil(
                        usuarioDAO.listarPorFilialETipo(
                                logado.getIdFilial(), TipoUsuEnum.TECNICO.name()),
                        TipoUsuEnum.TECNICO)
        );

        responsaveis.addAll(comPerfil(
                usuarioDAO.listarPorFilialETipo(
                        logado.getIdFilial(), TipoUsuEnum.OPERADOR.name()),
                TipoUsuEnum.OPERADOR));

        return responsaveis;
    }

    /** Confere se o usuário existe, está ativo, é técnico ou operador e pertence à filial informada. */
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

    /** Valida e cadastra o novo usuário. Devolve null ou a mensagem de erro. */
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
    // O gerente gerencia os técnicos da empresa dele e o técnico gerencia os operadores da filial dele.

    /**
     * Atualiza os dados pessoais de um usuário gerenciado pelo usuário logado.
     * O gerente edita técnicos e o técnico edita operadores, dentro dos seus respectivos escopos.
     */
    public String editarUsuario(Usuario dados, String novaSenha, Usuario logado) {
        Usuario atual = buscarGerenciavel(dados.getId(), logado);

        if (atual == null) {
            return "Usuário não encontrado ou sem permissão";
        }

        return salvarDadosPessoais(dados, novaSenha, atual);
    }

    /**
     * Permite que o usuário logado altere seus próprios dados pessoais.
     * O usuário é buscado novamente no banco para preservar seus vínculos e permissões.
     */
    public String editarProprioPerfil(Usuario dados, String novaSenha, Usuario logado) {
        if (logado == null || logado.getId() <= 0) {
            return "Sessão inválida";
        }

        Usuario atual = usuarioDAO.buscarPorId(logado.getId());

        if (atual == null || !estaAtivo(atual)) {
            return "Usuário não encontrado ou inativo";
        }

        return salvarDadosPessoais(dados, novaSenha, atual);
    }

    /**
     * Valida e atualiza os dados pessoais do usuário. Tipo, empresa, filial e status não mudam.
     * A troca de senha é opcional e só acontece quando uma nova senha é informada.
     */
    private String salvarDadosPessoais(Usuario dados, String novaSenha, Usuario atual) {
        dados.setId(atual.getId());

        String erro = validarDadosPessoais(dados);
        boolean trocaSenha = novaSenha != null && !novaSenha.isEmpty();

        if (erro == null && trocaSenha) {
            erro = validarSenha(novaSenha);
        }

        if (erro != null) {
            return erro;
        }

        atual.setNome(dados.getNome().trim());
        atual.setEmail(dados.getEmail().trim());
        atual.setCpf(CpfUtils.removerMascara(dados.getCpf()));
        atual.setTelefone(telefoneSomenteDigitos(dados.getTelefone()));
        atual.setUrlFoto(dados.getUrlFoto() == null || dados.getUrlFoto().isBlank()
                ? null : dados.getUrlFoto().trim());

        boolean salvou = usuarioDAO.atualizar(atual)
                && (!trocaSenha || usuarioDAO.atualizarSenha(
                atual.getId(), SenhaUtils.gerarHash(novaSenha)));

        return salvou ? null : "Erro interno no servidor ao tentar salvar o usuário";
    }

    /**
     * Quando alguém sai da empresa o usuário fica INATIVO e não é apagado, mas deixa de conseguir entrar.
     */
    public String inativarUsuario(int idAlvo, Usuario logado) {
        if (buscarGerenciavel(idAlvo, logado) == null) {
            return "Usuário não encontrado ou sem permissão";
        }
        return inativacaoDAO.inativarUsuario(idAlvo) ? null : "Erro interno no servidor ao inativar o usuário";
    }

    /** A reativação só é permitida se a filial e a empresa do usuário estiverem ativas. */
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

    /** Devolve o usuário somente se for do tipo que o logado gerencia e estiver dentro do escopo dele. */
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

    /**
     * Busca o usuário para o formulário de edição, ignorando quem está fora do escopo do gerente ou técnico.
     */
    public Usuario buscarParaEdicao(int idAlvo, Usuario logado) {
        Usuario alvo = buscarGerenciavel(idAlvo, logado);
        if (alvo != null) {
            alvo.setPerfil(tipoUsuarioService.buscarPerfil(alvo.getIdTipoUsuario()));
            alvo.setSenhaHash(null);
        }
        return alvo;
    }

    /** O gerente cadastra técnicos e o técnico cadastra operadores. */
    private TipoUsuEnum tipoQueOCadastradorPodeCriar(Usuario cadastrador) {
        if (cadastrador.getPerfil() == TipoUsuEnum.GERENTE) return TipoUsuEnum.TECNICO;
        if (cadastrador.getPerfil() == TipoUsuEnum.TECNICO) return TipoUsuEnum.OPERADOR;
        return null;
    }

    /** O formulário não envia o tipo, então quem cadastra é que o define. */
    private String definirTipo(Usuario novo, TipoUsuEnum tipoPermitido) {
        Integer idTipo = tipoUsuarioService.buscarIdPorPerfil(tipoPermitido);
        if (idTipo == null) {
            return "Tipo de usuário " + tipoPermitido.name() + " não encontrado no banco";
        }
        novo.setIdTipoUsuario(idTipo);
        return null;
    }

    /**
     * A empresa vem sempre de quem cadastra. A filial do técnico é a dele e o gerente escolhe uma da própria empresa.
     */
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

    /** Valida nome, e-mail, CPF e telefone. */
    private String validarDadosPessoais(Usuario u) {
        if (u.getNome() == null || u.getNome().trim().isEmpty()) {
            return "O nome não foi informado";
        }

        String erro = validarEmail(u.getEmail(), u.getId());
        if (erro == null) erro = validarCpf(u.getCpf(), u.getId());
        if (erro == null) erro = validarTelefone(u.getTelefone());
        return erro;
    }

    /**
     * O idIgnorar faz o próprio usuário não ser contado como e-mail repetido na edição e vale 0 no cadastro.
     */
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

    /** Mesma lógica do e-mail, aplicada ao CPF. */
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

    /** O telefone é opcional no banco, mas se vier preenchido precisa ter formato válido. */
    private String validarTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return null;
        }
        return TelefoneUtils.isTelefoneValido(telefone.trim()) ? null : "O telefone não é válido";
    }

    /**
     * Exige pelo menos 8 caracteres e um caractere especial. No cadastro o campo senhaHash do objeto ainda guarda a senha digitada.
     */
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

    /**
     * Remove espaços de nome e e-mail, deixa CPF e telefone só com dígitos, gera o hash da senha e registra quem cadastrou.
     */
    private void prepararParaGravar(Usuario novo, Usuario cadastrador) {
        novo.setNome(novo.getNome().trim());
        novo.setEmail(novo.getEmail().trim());
        novo.setCpf(CpfUtils.removerMascara(novo.getCpf()));
        novo.setTelefone(telefoneSomenteDigitos(novo.getTelefone()));
        novo.setSenhaHash(SenhaUtils.gerarHash(novo.getSenhaHash()));
        novo.setIdUsuarioCadastro(cadastrador.getId());
    }

    /** Remove máscara do telefone e devolve null quando ele estiver vazio. */
    private String telefoneSomenteDigitos(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return null;
        }
        return telefone.replaceAll("\\D", "");
    }
}