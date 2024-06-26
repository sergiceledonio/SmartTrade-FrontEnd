package GUI;

import Estado.Closed;
import Estado.StatePattern;
import Estado.Open;
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
    private JLabel ayudaLabel;
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
    private String type;
    private String city;
    private String street;
    private String door;
    private String num;
    private String flat;
    private int tipo;
    private int id;
    private List<ObserverUserData> observadores = new ArrayList<>();
    private StatePattern currentState;

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
        logInWithEnter(userTF);

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
        logInWithEnter(passTF);

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
        currentState = new Closed();

        visibilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                currentState.changeIcon(visibilityButton);
                currentState.handleVisibilityButton(passTF);
                changeState();
            }
        });
        visibilityButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                visibilityButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                visibilityButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        ayudaLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                getHelp();
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
                try{

                    email = jsonResponse.get("email").asText();
                    name = jsonResponse.get("name").asText();
                    password = jsonResponse.get("password").asText();
                    type = jsonResponse.get("type").asText();
                    id = jsonResponse.get("id").asInt();
                    dni = "";
                    iban = "";
                    cif = "";
                    city = "";
                    street = "";
                    door = "";
                    flat = "";
                    num = "";

                    System.out.println("Type= " + type);
                    System.out.println("ID= " + id);

                    JsonNode cityNode = jsonResponse.get("city");
                    if(cityNode != null){
                        switch(Integer.parseInt(type)){

                            case 1:
                                System.out.println("Cliente");
                                dni = jsonResponse.get("dni").asText();
                                city = jsonResponse.get("city").asText();
                                street = jsonResponse.get("street").asText();
                                door = jsonResponse.get("door").asText();
                                flat = jsonResponse.get("flat").asText();
                                num = jsonResponse.get("num").asText();
                                tipo = 1;
                                goToCatalog(getUserData(), tipo);
                                break;

                            case 2:
                                System.out.println("Vendedor");
                                iban = jsonResponse.get("iban").asText();
                                cif = jsonResponse.get("cif").asText();
                                city = jsonResponse.get("city").asText();
                                street = jsonResponse.get("street").asText();
                                door = jsonResponse.get("door").asText();
                                flat = jsonResponse.get("flat").asText();
                                num = jsonResponse.get("num").asText();
                                tipo = 2;
                                goToCatalog(getUserData(), tipo);
                                break;

                            case 3:
                                System.out.println("Admin");
                                goToAdmin();
                                break;
                        }
                    }else{
                        switch(Integer.parseInt(type)){

                            case 1:
                                System.out.println("Cliente");
                                dni = jsonResponse.get("dni").asText();
                                tipo = 1;
                                goToCatalog(getUserData(), tipo);
                                break;

                            case 2:
                                System.out.println("Vendedor");
                                iban = jsonResponse.get("iban").asText();
                                cif = jsonResponse.get("cif").asText();
                                tipo = 2;
                                goToCatalog(getUserData(), tipo);
                                break;

                            case 3:
                                System.out.println("Admin");
                                goToAdmin();
                                break;
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }else{
                JOptionPane.showMessageDialog(frame, "El correo o la contraseña son erróneos, comprueba otra vez tus datos", "Error", JOptionPane.ERROR_MESSAGE);
                cleanFields();
                System.out.println("Problem with client: "  + statusCode);
            }
        }catch(IOException | InterruptedException e){
            System.out.println("No hay conexión");
            e.printStackTrace();
        }
    }

    public void goToCatalog(String[] userData, int tipo){
        CatalogoProductos ventanaCatalog = new CatalogoProductos(tipo, id, name);
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaCatalog.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }

    public void goToAdmin(){
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        SwingUtilities.invokeLater(() -> {
            ValidacionProductosLista nuevaVentanaValidacion = new ValidacionProductosLista();
            nuevaVentanaValidacion.setTitle("Smart Trade");
            nuevaVentanaValidacion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            nuevaVentanaValidacion.setSize(800, 600);
            nuevaVentanaValidacion.setLocationRelativeTo(null);
            nuevaVentanaValidacion.setVisible(true);
        });
        ventanaActual.dispose();
    }

    public void cleanFields(){
        userTF.setText("");
        passTF.setText("");
    }

    public String[] getUserData(){
        String[] res = new String[13];
        res[0] = name;
        res[1] = email;
        res[2] = password;
        res[3] = type;
        res[4] = city;
        res[5] = street;
        res[6] = door;
        res[7] = flat;
        res[8] = num;
        res[9] = dni;
        res[10] = iban;
        res[11] = cif;
        res[12] =String.valueOf(id);
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
        type = res[3];
        city = res[4];
        street = res[5];
        door = res[6];
        flat = res[7];
        num = res[8];
        dni = res[9];
        iban = res[10];
        cif = res[11];
        id = Integer.parseInt(res[12]);
    }

    private void changeState() {
        if (currentState instanceof Open) {
            currentState = new Closed();
        } else {
            currentState = new Open();
        }
    }

    public void logInWithEnter(JComponent component) {
        component.addKeyListener(
                new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        if (e.getKeyChar() == KeyEvent.VK_ENTER){
                            trylogIn();                    }
                    }
                }
        );
    }
    public void getHelp()
    {   //no es un rickrol lo juro
        try {
            URI uri = new URI("https://youtu.be/qa6yUlkc6wA?si=NlbnjKlRjCnbEHkk");
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
