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
                } else {
                    cifTF.setEnabled(false);
                    ibanTF.setEnabled(false);
                    cifTF.setText("");
                    ibanTF.setText("");
                    cifTF.setBackground(new Color(150, 150, 150));
                    ibanTF.setBackground(new Color(150, 150, 150));
                    cifLabel.setText("CIF");
                    ibanLabel.setText("IBAN");
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
                crearCuentaButton.setBackground(new Color(153, 233, 255));
                crearCuentaButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                crearCuentaButton.setBackground(new Color(198, 232, 251));
                crearCuentaButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e){
                if(areAllTextFieldsFilled(panelRegistro)) {
                    if (isValidEmail(emailTF.getText())) {
                        if(passTF.getText().equals(repetirContraseñaTF.getText())) {
                            int answer = JOptionPane.showConfirmDialog(frame, "Este es tu usuario: " + emailTF.getText() + " y esta tu contraseña: " + passTF.getText() + "\n correcto?", "Confirmación", JOptionPane.YES_NO_OPTION);
                            if (answer == JOptionPane.YES_OPTION) {
                                String name = nombreTF.getText();
                                String password = repetirContraseñaTF.getText();
                                String dni = dniTF.getText();
                                String email = emailTF.getText();

                                sendCustomerToBack(name, password, dni, email);
                                if (vendedorRB.isSelected()) {
                                    String iban = ibanTF.getText();
                                    String cif = cifTF.getText();

                                    sendSellerToBack(name, password, dni, email, iban, cif);

                                }
                                accessLogIn();
                            } else {
                                deleteFields();
                            }
                        }else{
                            JOptionPane.showMessageDialog(frame, "Contraseñas no coinciden", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Email con formato incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
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
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(atrasButton); // Obtener el marco actual
        ventanaActual.dispose();
    }

    public boolean isValidEmail(String email){
        Pattern pat = null;
        Matcher match = null;
        pat = Pattern.compile("^[\\w\\-\\_\\+]+(\\.[\\w\\-\\_\\+]+)*@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}$");
        match = pat.matcher(email);
        if(match.find()){
            return true;
        }else{
            return false;
        }
    }
    public boolean areAllTextFieldsFilled(Container container) {

        Boolean verified = true;

        if((nombreTF.getText().isEmpty() || passTF.getText().isEmpty() || repetirContraseñaTF.getText().isEmpty() || dniTF.getText().isEmpty() || emailTF.getText().isEmpty())){
            return false;
        }

        if (verified && vendedorRB.isSelected()) {
            if (cifTF.getText().isEmpty() || ibanTF.getText().isEmpty()) {
                return false;
            }
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

    private void sendCustomerToBack(String name, String password, String dni, String email){
        String url = "http://localhost:8080/User/Client";
        HttpClient client = HttpClient.newHttpClient();

        Map<String, String> userData = new HashMap<>();
        userData.put("nombre", name);
        userData.put("email", email);
        userData.put("password", password);
        userData.put("dni", dni);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(userData.toString()))
                .build();

        try{
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response status code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
            int status = response.statusCode();

            System.out.println("Respuesta del servidor: " + status);
        }catch (ConnectException e){
            e.printStackTrace();
        }catch  (Exception e){
            e.printStackTrace();
        }
    }

    private void sendSellerToBack(String name, String password, String dni, String email, String iban, String cif){
        String url = "http://localhost:8080/User/Seller";
        HttpClient client = HttpClient.newHttpClient();

        Map<String, String> userData = new HashMap<>();
        userData.put("nombre", name);
        userData.put("email", email);
        userData.put("password", password);
        userData.put("dni", dni);
        userData.put("iban", iban);
        userData.put("cif", cif);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(userData.toString()))
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


