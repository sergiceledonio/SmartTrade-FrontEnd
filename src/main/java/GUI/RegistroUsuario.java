package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.CropImageFilter;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RegistroUsuario extends JFrame{
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
    private JFrame frame;

    public RegistroUsuario(){

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
                                            System.out.println("Crear Vendedor");
                                            sendSellerToBack(nombreTF.getText(), passTF.getText(), emailTF.getText(), ibanTF.getText(),cifTF.getText());
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
                                        System.out.println("Crear comprador");
                                        sendCustomerToBack(nombreTF.getText(), passTF.getText(), dniTF.getText(), emailTF.getText());
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
                /*CONECTAR CON ELEMENTOS DEL SERVICIO PARA CREAR UN USUARIO NUEVO*/
                //nombreLabel.setText(String.join(" ", datos));
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


        panelRegistro.setFocusable(true);
        panelRegistro.requestFocusInWindow();
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
        InicioSesion inicioSesion = new InicioSesion();
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(inicioSesion.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(atrasButton);
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

    private void sendCustomerToBack(String name, String password, String dni, String email) throws JsonProcessingException {
        String url = "http://localhost:8080/User/Client";
        HttpClient client = HttpClient.newHttpClient();
        try{
            Map<String, String> userData = new HashMap<>();
            userData.put("email", email);
            userData.put("name", name);
            userData.put("password", password);
            userData.put("dni", dni);
            String jsonBody = new ObjectMapper().writeValueAsString(userData);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();


                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                System.out.println("Response status code: " + response.statusCode());
        }catch (ConnectException e){
            e.printStackTrace();
        }catch  (Exception e){
            e.printStackTrace();
        }
    }

    private void sendSellerToBack(String name, String password, String email, String iban, String cif) throws JsonProcessingException {
        String url = "http://localhost:8080/User/Seller";
        HttpClient client = HttpClient.newHttpClient();

        Map<String, String> userData = new HashMap<>();
        userData.put("nombre", name);
        userData.put("email", email);
        userData.put("password", password);
        userData.put("iban", iban);
        userData.put("cif", cif);
        String jsonBody = new ObjectMapper().writeValueAsString(userData);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        try{
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();

            System.out.println("Respuesta del servidor: " + status);
        }catch (ConnectException e){
            e.printStackTrace();
        }catch  (Exception e){
            e.printStackTrace();
        }
    }

}


