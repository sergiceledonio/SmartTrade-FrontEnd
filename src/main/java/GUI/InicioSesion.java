package GUI;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InicioSesion extends JFrame{
    private JPanel panelInicioSesion;
    private JTextField userTF;
    private JPasswordField passTF;
    private JButton loginButton;
    private JLabel logo;
    private JLabel registrateButton;
    private JFrame   frame;



    /////////

    private boolean primeraUser = true;
    private boolean visiblePass = true;

    private String nombre;
    private String password;
    private String email;
    private Boolean isSeller;
    private String iban;
    private String cif;
    private String dni;

    public InicioSesion(){

        /*USERTF*/
        userTF.setText("Usuario");
        userTF.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if(primeraUser){
                    userTF.setText("");
                    primeraUser = false;
                }
            }
        });

        /*PASSTF*/
        passTF.setText("Contraseña");
        passTF.setEchoChar((char) 0);
        passTF.addFocusListener(new FocusAdapter() {
            private boolean primeraPass = true;
            @Override
            public void focusGained(FocusEvent e) {
                if(primeraPass){
                    passTF.setText("");
                    primeraPass = false;
                    passTF.setEchoChar('*');
                }
            }
        });

        /*LOGINBUTTON*/
        loginButton.setBackground(new Color(153,233,255));
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(73,231,255));
                loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(new Color(153,232,251));
                loginButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e){
                trylogIn();
            }

        });


        // VISIBILIDAD PASSWORD FIELD
        /*if(visiblePass){
            passTF.setEchoChar((char) 0);
            visiblePass = false;
        }else{
            passTF.setEchoChar('*');
            visiblePass = true;
        }*/

        /*REGISTRATEBUTTON*/
        registrateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                accessRegister();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                registrateButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                registrateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

        panelInicioSesion.setFocusable(true);
        panelInicioSesion.requestFocusInWindow();
    }
    public static void main(String[] args) {
        InicioSesion ventana = new InicioSesion();
        JFrame frame = new JFrame("Smart Trade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(ventana.panelInicioSesion);
        frame.pack();
        frame.setVisible(true);
    }


    /*METODOS DE ACCESO A VARIABLES*/

    public JPanel getPanel(){
        return panelInicioSesion;
    }

    public void accessRegister(){
        RegistroUsuario registroUsuario = new RegistroUsuario();
        JFrame registro = new JFrame("Smart Trade");
        registro.setContentPane(registroUsuario.getPanel());
        registro.pack();
        registro.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(registrateButton); // Obtener el marco actual
        ventanaActual.dispose();
    }

    public void trylogIn(){
        String email = userTF.getText();
        String password = new String(passTF.getPassword());
        sendHTTPRequest(email, password);
    }

    private void sendHTTPRequest(String email, String password){
        String url = "http://localhost:8080/User/Login";
        HttpClient client = HttpClient.newHttpClient();

        try {
            Map<String, String> userData = new HashMap<>();
            userData.put("email", email);
            userData.put("password", password);

            String jsonBody = new ObjectMapper().writeValueAsString(userData);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            String responseBody = response.body();
            if(statusCode == 200){
                System.out.println("Client created: " + statusCode);

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonResponse = objectMapper.readTree(responseBody);

                String email_rec = jsonResponse.get("email").asText();
                String name_rec = jsonResponse.get("name").asText();
                boolean isSeller_rec = jsonResponse.get("isSeller").asBoolean();
                String dni_rec = jsonResponse.get("dni").asText();
                String iban_rec = jsonResponse.get("iban").asText();
                String cif_rec = jsonResponse.get("dni").asText();

                goToCatalog(name_rec, password, email_rec,dni_rec, isSeller_rec, iban_rec, cif_rec);
            }else{
                System.out.println("Problem with client: "  + statusCode);
            }
        }catch(IOException | InterruptedException e){
            System.out.println("Error al enviar la petición: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void goToCatalog(String nombre, String password, String email, String dni, Boolean isSeller, String iban, String cif){
        CatalogoProductos ventanaCatalog = new CatalogoProductos(nombre, password, email, isSeller,dni, iban, cif);
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaCatalog.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }


}
