package org.apollo.apollowebcrud.util;

import java.util.regex.Pattern;
import org.mindrot.jbcrypt.BCrypt;

public class SenhaUtils {

//    REGEX: pode receber número, letra(maiúscula, minúscula), símbolos(@#$%^&+=!_*), e exige no mínimo 8 dígitos e pelo menos um de cada categoria
    public static final Pattern SENHA_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!.\\-_*]).{8,}$");

//    Aplica o REGEX e verifica se atende a todas as regras
    public static boolean isSenhaValida(String senha) {
        if (senha == null) return false;
        return SENHA_PATTERN.matcher(senha).matches();
    }

//    Método para tranformar a senha em um Hash
    public static String hashSenha(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt(12));
    }

//    Método que verifica se a senha digitada corresponde ao Hash salvo no banco
    public static boolean verificarSenha(String senhaDigitada, String hashSalvo) {
        return BCrypt.checkpw(senhaDigitada, hashSalvo);
    }
}