package com.bernardo.utils;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
*
* @author Bernardo Zardo Mergen
*/
public class EmailService {

    private static final String REMETENTE = "lavagemdanieltcc@gmail.com";
    private static final String SENHA_APP = "lkhe vsew masj cjov"; // a senha de app (16 caracteres)

    public static void enviarEmail(String destinatario, String assunto, String mensagem) {
        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(REMETENTE, SENHA_APP);
                }
            });

        try {

            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(REMETENTE, "Lavagem Daniel"));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            msg.setSubject(assunto);
            msg.setContent(mensagem, "text/html; charset=UTF-8");

            Transport.send(msg);

            System.out.println("Email enviado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao enviar email: " + e.getMessage());
        }
    }
}
