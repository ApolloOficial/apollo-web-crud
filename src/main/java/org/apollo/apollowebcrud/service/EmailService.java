package org.apollo.apollowebcrud.service;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

// Envio de e-mail via SMTP. Chaves no .env: SMTP_HOST, SMTP_PORT (padrão 587), SMTP_USER, SMTP_PASSWORD
// e, opcionalmente, SMTP_FROM (padrão: o próprio SMTP_USER).
public class EmailService {

    public boolean enviarCodigoRecuperacao(String destinatario, String codigo, int minutosValidade) {
        String texto = "Seu código de recuperação de senha do Apollo é: " + codigo + "\n\n"
                + "Ele vale por " + minutosValidade + " minutos. "
                + "Se você não pediu a recuperação, ignore este e-mail.";
        return enviar(destinatario, "Apollo - código de recuperação de senha", texto);
    }

    private boolean enviar(String destinatario, String assunto, String texto) {
        Dotenv env = Dotenv.load();
        String host = env.get("SMTP_HOST");
        String usuario = env.get("SMTP_USER");
        String senha = env.get("SMTP_PASSWORD");

        if (host == null || usuario == null || senha == null) {
            System.err.println("SMTP não configurado no .env (SMTP_HOST, SMTP_USER, SMTP_PASSWORD)");
            return false;
        }

        try {
            Message mensagem = new MimeMessage(criarSessao(env, host, usuario, senha));
            mensagem.setFrom(new InternetAddress(env.get("SMTP_FROM", usuario)));
            mensagem.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensagem.setSubject(assunto);
            mensagem.setText(texto);
            Transport.send(mensagem);
            return true;
        } catch (MessagingException e) {
            System.err.println("Erro ao enviar e-mail: " + e.getMessage());
            return false;
        }
    }

    private Session criarSessao(Dotenv env, String host, String usuario, String senha) {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", env.get("SMTP_PORT", "587"));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuario, senha);
            }
        });
    }
}
