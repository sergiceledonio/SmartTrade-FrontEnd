package GUI;

import Observer.ObserverRegister;
import Observer.ObserverUserData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InicioSesion extends JFrame implements ObserverUserData {
    private JPanel panelInicioSesion;
    private JTextField userTF;
    private JPasswordField passTF;
    private JButton loginButton;
    private JLabel logo;
    private JLabel registrateButton;
    private JCheckBox visibilityButton;
    private JFrame   frame;



    /////////

    private boolean primeraUser = true;
    private boolean visiblePass = true;

    private String name;
    private String password;
    private String email;
    private String iban;
    private String cif;
    private String dni;
    private int type;
    private String city;
    private String street;
    private String door;
    private String num;
    private String flat;
    private List<ObserverUserData> observadores = new ArrayList<>();
    public InicioSesion(){

        /*USERTF*/
        userTF.setText("Email");
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

        /*VISIBILITYBUTTON*/

        visibilityButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e) {
                visibilityButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                visibilityButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        ImageIcon abierto = new ImageIcon("src/main/resources/img/ojoAbierto (1).png");
        ImageIcon cerrado = new ImageIcon("src/main/resources/img/ojoCerrado (1).png");
        visibilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(visiblePass){
                    passTF.setEchoChar((char) 0);
                    visiblePass = false;
                    visibilityButton.setIcon(abierto);
                }else{
                    passTF.setEchoChar('*');
                    visiblePass = true;
                    visibilityButton.setIcon(cerrado);
                }
            }
        });


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
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel()); // Obtener el marco actual
        ventanaActual.dispose();
    }

    public void trylogIn(){
        String email = userTF.getText();
        String password = new String(passTF.getPassword());
        sendHTTPRequest(email, password);
    }

    private void sendHTTPRequest(String emailSend, String passwordSend){
        String url = "http://localhost:8080/user/login";
        HttpClient client = HttpClient.newHttpClient();

        try {
            Map<String, String> userData = new HashMap<>();
            userData.put("email", emailSend);
            userData.put("password", passwordSend);

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
                System.out.println("Login successful: " + statusCode);

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonResponse = objectMapper.readTree(responseBody);

                email = jsonResponse.get("email").asText();
                name = jsonResponse.get("name").asText();
                password = jsonResponse.get("password").asText();
                type = jsonResponse.get("type").asInt();
                city = jsonResponse.get("city").asText();
                street = jsonResponse.get("street").asText();
                door = jsonResponse.get("door").asText();
                flat = jsonResponse.get("flat").asText();
                num = jsonResponse.get("num").asText();
                dni = "";
                iban = "";
                cif = "";
                switch(type){

                    case 1:
                        dni = jsonResponse.get("dni").asText();;
                    break;

                    case 2:
                        iban = jsonResponse.get("iban").asText();
                        cif = jsonResponse.get("dni").asText();
                    break;

                    case 3:
                        //Admin
                    break;
                }

                goToCatalog(getUserData());
            }else{
                System.out.println("Problem with client: "  + statusCode);
            }
        }catch(IOException | InterruptedException e){
            System.out.println("Error al enviar la petición: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void goToCatalog(String[] userData){
        CatalogoProductos ventanaCatalog = new CatalogoProductos(userData);
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaCatalog.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }

    public String[] getUserData(){
        String[] res = new String[12];
        res[0] = name;
        res[1] = email;
        res[2] = password;
        res[3] = String.valueOf(type);
        res[4] = city;
        res[5] = street;
        res[6] = door;
        res[7] = flat;
        res[8] = num;
        res[9] = dni;
        res[10] = iban;
        res[11] = cif;
        return res;
    }


    @Override
    public void addObserver(ObserverUserData observer) {
        observadores.add(observer);
    }

    @Override
    public void removeObserver(ObserverUserData observer) {
        observadores.remove(observer);
    }

    @Override
    public void notifyObservers(String[] data) {
        for (ObserverUserData observer : observadores) {
            observer.update(data);
        }
    }

    @Override
    public void update(String[] res) {
        name = res[0];
        email = res[1];
        password = res[2];
        type = Integer.parseInt(res[3]);
        city = res[4];
        street = res[5];
        door = res[6];
        flat = res[7];
        num = res[8];
        dni = res[9];
        iban = res[10];
        cif = res[11];
    }
}
