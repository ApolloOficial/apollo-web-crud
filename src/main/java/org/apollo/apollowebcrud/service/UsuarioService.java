package org.apollo.apollowebcrud.service;

import org.apollo.apollowebcrud.dao.UsuarioDAO;
import org.apollo.apollowebcrud.model.Usuario;
import org.apollo.apollowebcrud.util.*;

public class UsuarioService {
    public final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Usuario validarLogin(String email, String cpf, String senha) {
        if (email == null || !email.contains("@") || cpf == null || senha == null) {
            return null;
        }

        //VERIFICAÇÕES DE INTEGRIDADE


        // VALIDAÇÃO NO BANCO
        return usuarioDAO.validarLogin(email, cpf, senha);
    }

    public String cadastrarUsuario(Usuario usuarioNovo) {

        if (!TipoUsuarioUtils.verificarTipoUsuarioExist(usuarioNovo.getIdTipoUsuario())) {
            return "O tipo de usuário selecionado não existe";
        }

        if (!EmpresaUtils.verificarEmpresaExist(usuarioNovo.getIdEmpresa())) {
            return "A empresa selecionada não existe no sistema";
        }

        if (!FilialUtils.verificarFilialExist(usuarioNovo.getIdFilial())) {
            return "A filial selecionada não existe no sistema";
        }

        if (usuarioNovo.getEmail() == null || usuarioNovo.getEmail().trim().isEmpty()) {
            return "Seu email não foi informado";
        }

        if (!EmailUtils.isEmailValido(usuarioNovo.getEmail())) {
            return "Seu email não é válido";
        }

        if (EmailUtils.verificarEmailExist(usuarioNovo.getEmail())) {
            return "Este email já foi cadastrado no sistema";
        }

        if (usuarioNovo.getCpf() == null || usuarioNovo.getCpf().trim().isEmpty()) {
            return "Seu CPF não foi informado";
        }

        if (!CpfUtils.isCpfValido(usuarioNovo.getCpf())) {
            return "Seu CPF não é válido";
        }

        if (CpfUtils.verificarCpfExist(usuarioNovo.getCpf())){
            return "Este CPF já foi cadastrado no sistema";
        }

        if (usuarioNovo.getSenhaHash() == null || usuarioNovo.getSenhaHash().trim().isEmpty()){
            return "Sua senha não foi informada";
        }

        if (!SenhaUtils.isSenhaValida(usuarioNovo.getSenhaHash())) {
            return "Sua senha deve conter: letra minúscula, maiúscula, número, caractere especial (@#$%^&+=!._-*) e no mínimo 8 caracteres";
        }

        boolean sucesso = usuarioDAO.inserir(usuarioNovo) > 0;

        if (!sucesso) {
            return "Erro interno no servidor ao tentar salvar o usuário";
        }

        return null;
    }
}