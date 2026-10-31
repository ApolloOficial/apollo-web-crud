package org.apollo.apollowebcrud.service;

import org.apollo.apollowebcrud.dao.RecuperacaoSenhaDAO;
import org.apollo.apollowebcrud.dao.UsuarioDAO;
import org.apollo.apollowebcrud.model.StatusGeral;
import org.apollo.apollowebcrud.model.Usuario;
import org.apollo.apollowebcrud.util.EmailUtils;
import org.apollo.apollowebcrud.util.SenhaUtils;

import java.security.SecureRandom;

/**
 * Recuperação de senha em duas etapas. Na primeira o usuário pede o código por e-mail e na segunda
 * informa o código e a nova senha. Todos os métodos devolvem null em caso de sucesso ou a mensagem para a tela.
 */
public class RecuperacaoSenhaService {

    private static final int VALIDADE_MINUTOS = 15;
    private static final String ERRO_ENVIO = "Não foi possível enviar o e-mail agora. Tente novamente em instantes";
    private static final String ERRO_CODIGO = "Código inválido ou expirado";

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final RecuperacaoSenhaDAO recuperacaoDAO = new RecuperacaoSenhaDAO();
    private final UsuarioService usuarioService = new UsuarioService();
    private final EmailService emailService = new EmailService();
    private final SecureRandom aleatorio = new SecureRandom();

    /**
     * Primeira etapa. Se o e-mail não existir ou o usuário estiver inativo, a resposta é igual à de sucesso
     * para que ninguém descubra quais e-mails estão cadastrados.
     */
    public String solicitarCodigo(String email) {
        String emailLimpo = email == null ? "" : email.trim();
        if (!EmailUtils.isEmailValido(emailLimpo)) {
            return "Informe um e-mail válido";
        }

        Usuario usuario = usuarioDAO.buscarPorEmail(emailLimpo);
        if (!estaAtivo(usuario)) {
            return null;
        }

        String codigo = gerarCodigo();
        if (!recuperacaoDAO.salvar(usuario.getId(), SenhaUtils.gerarHash(codigo), VALIDADE_MINUTOS)) {
            return ERRO_ENVIO;
        }
        return emailService.enviarCodigoRecuperacao(usuario.getEmail(), codigo, VALIDADE_MINUTOS) ? null : ERRO_ENVIO;
    }

    /**
     * Segunda etapa. Exige o código, a nova senha com confirmação e uma senha dentro das regras.
     * Se o código estiver errado ou vencido a mensagem é a mesma de usuário inexistente.
     */
    public String redefinirSenha(String email, String codigo, String novaSenha, String confirmacao) {
        if (codigo == null || codigo.isBlank()) {
            return "Informe o código recebido por e-mail";
        }
        if (novaSenha == null || !novaSenha.equals(confirmacao)) {
            return "As senhas não conferem";
        }
        String erroSenha = usuarioService.validarSenha(novaSenha);
        if (erroSenha != null) {
            return erroSenha;
        }

        Usuario usuario = usuarioDAO.buscarPorEmail(email == null ? "" : email.trim());
        if (!estaAtivo(usuario) || !codigoConfere(usuario.getId(), codigo.trim())) {
            return ERRO_CODIGO;
        }

        boolean trocou = recuperacaoDAO.redefinirSenha(usuario.getId(), SenhaUtils.gerarHash(novaSenha));
        return trocou ? null : "Não foi possível redefinir a senha. Tente novamente";
    }

    /** Compara o código digitado com o hash guardado e contabiliza a tentativa quando erra. */
    private boolean codigoConfere(int idUsuario, String codigo) {
        String hash = recuperacaoDAO.buscarHashValido(idUsuario);
        if (hash != null && SenhaUtils.verificar(codigo, hash)) {
            return true;
        }
        recuperacaoDAO.registrarTentativa(idUsuario);
        return false;
    }

    /** Indica se o usuário existe e está ativo. */
    private boolean estaAtivo(Usuario usuario) {
        return usuario != null && StatusGeral.ATIVO.name().equals(usuario.getStatus());
    }

    /** Sorteia um código de seis dígitos, completando com zeros à esquerda. */
    private String gerarCodigo() {
        return String.format("%06d", aleatorio.nextInt(1_000_000));
    }
}
