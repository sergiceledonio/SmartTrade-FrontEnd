package Email;


public class EmailSender {
    public static void enviarCorreo(String destinatario, String asunto, String cuerpo) {
        String host = ServidorSmtp.HOST;
        int port = ServidorSmtp.PORT;
        boolean tlsEnabled = ServidorSmtp.TLS_ENABLED;
        String username = ServidorSmtp.USERNAME;
        String password = ServidorSmtp.PASSWORD;

        System.out.println("El correo tiene que enviarse a: " + destinatario);

        // Envio correo
    }
}
