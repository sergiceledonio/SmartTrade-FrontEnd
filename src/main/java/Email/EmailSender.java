package Email;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {
    public static void enviarCorreo(String destinatario, String nombre) {
        String host = ServidorSmtp.HOST;
        int port = ServidorSmtp.PORT;
        boolean tlsEnabled = ServidorSmtp.TLS_ENABLED;
        String username = ServidorSmtp.USERNAME;
        String password = ServidorSmtp.PASSWORD;

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", tlsEnabled ? "true" : "false");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject("Confirmación de pedido");
            message.setText("Gracias por confiar en nosotros " + nombre + " el pedido realizado está en proceso de envio y llegará en los próximos dias.");
            Transport.send(message);
            System.out.println("Correo enviado correctamente.");
        } catch (MessagingException e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
        }

    }
}
