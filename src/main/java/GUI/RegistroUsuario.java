package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;
import Observer.ObserverRegister;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RegistroUsuario extends JFrame implements ObserverRegister {
    private JPanel panelRegistro;
    private JLabel logo;
    private JTextField nombreTF;
    private JTextField passTF;
    private JTextField emailTF;
    private JTextField dniTF;
    private JLabel nombreLabel;
    private JRadioButton vendedorRB;
    private JTextField cifTF;
    private JTextField ibanTF;
    private JButton crearCuentaButton;
    private JTextField repetirContraseñaTF;
    private JLabel atrasButton;
    private JLabel cifLabel;
    private JLabel ibanLabel;
    private JLabel dniLabel;
    private JButton registerAddress;
    private JFrame frame;
    private DireccionEnvioRegister direccionEnvioRegister;
    private String[] direccion;
    private String city;
    private String street;
    private String num;
    private String door;
    private String flat;

    public RegistroUsuario(){

        direccionEnvioRegister = new DireccionEnvioRegister();
        direccionEnvioRegister.addObserver(this);

        /*NOMBRETF*/

        /*PASSTF*/

        /*EMAILTF*/

        /*REPETIRCONTRASEÑATF*/

        /*DNITF*/

        /*VENDEDORRB*/

        vendedorRB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (vendedorRB.isSelected()) {
                    cifTF.setEnabled(true);
                    ibanTF.setEnabled(true);
                    cifTF.setBackground(new Color(255, 255, 255));
                    ibanTF.setBackground(new Color(255, 255, 255));
                    cifLabel.setText("CIF(*)");
                    ibanLabel.setText("IBAN(*)");
                    dniTF.setText("");
                    dniLabel.setText("DNI");
                    dniTF.setEnabled(false);
                    dniTF.setBackground(new Color(150, 150, 150));
                } else {
                    cifTF.setEnabled(false);
                    ibanTF.setEnabled(false);
                    cifTF.setText("");
                    ibanTF.setText("");
                    cifTF.setBackground(new Color(150, 150, 150));
                    ibanTF.setBackground(new Color(150, 150, 150));
                    cifLabel.setText("CIF");
                    ibanLabel.setText("IBAN");
                    dniLabel.setText("DNI(*)");
                    dniTF.setText("");
                    dniTF.setEnabled(true);
                    dniTF.setBackground(new Color(255, 255, 255));
                }
            }
        });

        vendedorRB.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                vendedorRB.setCursor(new Cursor(Cursor.HAND_CURSOR));
                vendedorRB.setFont(vendedorRB.getFont().deriveFont(Font.BOLD));
            }
            public void mouseExited(MouseEvent e) {
                vendedorRB.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                vendedorRB.setFont(vendedorRB.getFont().deriveFont(Font.PLAIN));
            }
        });

        /*CIFTF*/

        cifTF.setEnabled(false);
        cifTF.setBackground(new Color(150,150,150));

        /*IBANTF*/

        ibanTF.setEnabled(false);
        ibanTF.setBackground(new Color(150,150,150));

        /*CREARCUENTABUTTON*/

        crearCuentaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                crearCuentaButton.setBackground(new Color(73, 231, 255));
                crearCuentaButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                crearCuentaButton.setBackground(new Color(153, 233, 255));
                crearCuentaButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e){
                if(areAllTextFieldsFilled(panelRegistro)){
                    if(vendedorRB.isSelected()){
                        if(passTF.getText().equals(repetirContraseñaTF.getText())){
                            if(isValidEmail(emailTF.getText())){
                                if(isValidIban(ibanTF.getText())){
                                    if(isValidCif(cifTF.getText())){
                                        try {
                                            JOptionPane.showMessageDialog(frame, "El usuario de tipo vendedor ha sido registrado", "Registro completado", JOptionPane.INFORMATION_MESSAGE);
                                            sendSellerToBack(nombreTF.getText(), passTF.getText(), emailTF.getText(), ibanTF.getText(),cifTF.getText(),city, street, num, flat, door);
                                            accessLogIn();
                                        } catch (JsonProcessingException ex) {
                                            throw new RuntimeException(ex);
                                        }
                                    }else{
                                        JOptionPane.showMessageDialog(frame, "CIF con formato incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                }else{
                                    JOptionPane.showMessageDialog(frame, "IBAN con formato incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }else{
                                JOptionPane.showMessageDialog(frame, "Email con formato incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }else{
                            JOptionPane.showMessageDialog(frame, "Contraseñas no coinciden", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }else if(!vendedorRB.isSelected()){
                        if(passTF.getText().equals(repetirContraseñaTF.getText())){
                            if(isValidDni(dniTF.getText())){
                                if(isValidEmail(emailTF.getText())){
                                    try {
                                        if(isAdmin(passTF.getText(),emailTF.getText())){
                                            sendAdminToBack(nombreTF.getText(), passTF.getText(),emailTF.getText());
                                            accessLogIn();
                                        }else{
                                            JOptionPane.showMessageDialog(frame, "El usuario de tipo comprador ha sido registrado", "Registro completado", JOptionPane.INFORMATION_MESSAGE);
                                            sendCustomerToBack(nombreTF.getText(), passTF.getText(), dniTF.getText(), emailTF.getText(),city, street, num, flat, door);
                                            accessLogIn();
                                        }
                                    } catch (JsonProcessingException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                }else{
                                    JOptionPane.showMessageDialog(frame, "Email con formato incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }else{
                                JOptionPane.showMessageDialog(frame, "Dni con formato incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }else{
                            JOptionPane.showMessageDialog(frame, "Contraseñas no coinciden", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                }else{
                    JOptionPane.showMessageDialog(frame, "Debes rellenar todos los apartados", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        /*ATRASBUTTON*/
        atrasButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                atrasButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                atrasButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                accessLogIn();
            }
        });

        /*REGISTERADDRESS*/

        registerAddress.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                direccionEnvioRegister.pack();
                direccionEnvioRegister.setVisible(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                registerAddress.setBackground(new Color(73, 231, 255));
                registerAddress.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                registerAddress.setBackground(new Color(153, 233, 255));
                registerAddress.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });


        panelRegistro.setFocusable(true);
        panelRegistro.requestFocusInWindow();
        registerAddress.addMouseListener(new MouseAdapter() {
        });
    }


    public static void main(String[] args) {
        RegistroUsuario ventana = new RegistroUsuario();
        JFrame frame = new JFrame("Smart Trade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(ventana.panelRegistro);
        frame.pack();
        frame.setVisible(true);
    }


    /*METODOS DE ACCESO A VARIABLES*/
    public JPanel getPanel(){
        return panelRegistro;
    }

    public void accessLogIn(){
        imprimirDireccion();
        InicioSesion inicioSesion = new InicioSesion();
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(inicioSesion.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }

    public boolean isValidEmail(String email){
        Pattern pat = null;
        Matcher match = null;
        pat = Pattern.compile("^[\\w\\-\\_\\+]+(\\.[\\w\\-\\_\\+]+)*@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}$");
        match = pat.matcher(email);
        return match.find();
    }

    public boolean isValidIban(String iban){
        Pattern pat = null;
        Matcher match = null;
        pat = Pattern.compile("^[a-zA-Z]{2}[0-9]{2}[a-zA-Z0-9]{4}[0-9]{7}([a-zA-Z0-9]?){0,16}$");
        match = pat.matcher(iban);
        return match.find();
    }
    public boolean isValidCif(String cif){
        Pattern pat = null;
        Matcher match = null;
        pat = Pattern.compile("^[ABCDEFGHJNPQRSUVW]{1}[0-9]{7}[0-9A-J]$");
        match = pat.matcher(cif);
        return match.find();
    }

    public boolean isValidDni(String dni){
        Pattern pat = null;
        Matcher match = null;
        pat = Pattern.compile("^[0-9]{8}[A-Za-z]$");
        match = pat.matcher(dni);
        return match.find();
    }
    public boolean areAllTextFieldsFilled(Container container) {

        Boolean verified = true;
        if(!(vendedorRB.isSelected()) && (nombreTF.getText().isEmpty() || passTF.getText().isEmpty() || repetirContraseñaTF.getText().isEmpty() || dniTF.getText().isEmpty() || emailTF.getText().isEmpty())){
            return false;
        }
        if((verified && vendedorRB.isSelected()) && (cifTF.getText().isEmpty() || ibanTF.getText().isEmpty())){
            return false;
        }
        return verified;
    }
    public void deleteFields(){
            nombreTF.setText("");
            passTF.setText("");
            repetirContraseñaTF.setText("");
            dniTF.setText("");
            emailTF.setText("");
            cifTF.setText("");
            ibanTF.setText("");
    }
    private boolean isAdmin(String contra, String gmail){
        if(contra.equals("admin@admin.com") && gmail.equals("12345678")){
            return true;
        }
        return false;
    }

    private void sendCustomerToBack(String name, String password, String dni, String email, String city, String street, String num, String flat, String door) throws JsonProcessingException {
        String url = "http://localhost:8080/user/newclient";
        HttpClient client = HttpClient.newHttpClient();

        try {
            Map<String, String> userData = new HashMap<>();
            userData.put("name", name);
            userData.put("email", email);
            userData.put("password", password);
            userData.put("dni", dni);
            userData.put("city", city);
            userData.put("street", street);
            userData.put("num", num);
            userData.put("flat", flat);
            userData.put("door", door);

            String jsonBody = new ObjectMapper().writeValueAsString(userData);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            if(statusCode == 200){
                System.out.println("Client created: " + statusCode);
            }else{
                System.out.println("Problem with client: "  + statusCode);
            }
        }catch(IOException | InterruptedException e){
            System.out.println("Error al enviar la petición: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendSellerToBack(String name, String password, String email, String iban, String cif, String city, String street, String num, String flat, String door) throws JsonProcessingException {
        String url = "http://localhost:8080/user/newseller";
        HttpClient client = HttpClient.newHttpClient();

        try{
            Map<String, String> userData = new HashMap<>();
            userData.put("name", name);
            userData.put("email", email);
            userData.put("password", password);
            userData.put("iban", iban);
            userData.put("cif", cif);
            userData.put("city", city);
            userData.put("street", street);
            userData.put("num", num);
            userData.put("floor", flat);
            userData.put("door", door);

            String jsonBody = new ObjectMapper().writeValueAsString(userData);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();

            if(statusCode == 200){
                System.out.println("Seller created: " + statusCode);
            }else{
                System.out.println("Problem with seller: "  + statusCode);
            }
        }catch(IOException | InterruptedException e){
            System.out.println("Error al enviar la petición: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendAdminToBack(String name, String password, String email){
        String url = "http://localhost:8080/user/newadmin";
        HttpClient client = HttpClient.newHttpClient();

        try{
            Map<String, String> userData = new HashMap<>();
            userData.put("name", name);
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

            if(statusCode == 200){
                System.out.println("Seller created: " + statusCode);
            }else{
                System.out.println("Problem with seller: "  + statusCode);
            }
        }catch(IOException | InterruptedException e){
            System.out.println("Error al enviar la petición: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void addObserver(ObserverRegister observer) {

    }

    @Override
    public void removeObserver(ObserverRegister observer) {

    }

    @Override
    public void notifyObservers(String[] data) {

    }
    @Override
    public void update(String[] data) {
        direccion = data;
        num = direccion[0];
        street = direccion[1];
        city = direccion[2];
        flat = direccion[3];
        door = direccion[4];
    }
    private void imprimirDireccion() {
        if (direccion != null) {
            System.out.println("Dirección guardada:");
            System.out.println("Número: " + direccion[0]);
            System.out.println("Calle: " + direccion[1]);
            System.out.println("Ciudad: " + direccion[2]);
            System.out.println("Planta: " + direccion[3]);
            System.out.println("Puerta: " + direccion[4]);
        } else {
            System.out.println("No se ha guardado ninguna dirección.");
        }
    }

}


