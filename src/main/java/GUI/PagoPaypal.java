package GUI;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static GUI.CatalogoProductos.getUserData;

public class PagoPaypal {
    private JPanel panelTitulo;
    private JLabel backButton;
    private JLabel perfilButton;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton pagarButton;
    private JCheckBox guardaCheckBox;
    private JLabel precioLabel;
    private JPanel paypalPanel;
    private InicioSesion iniciosesion;
    private int tipo;
    private int id;
    private double precio;
    private String nombre;
    public PagoPaypal(int t, int id, double precio, String nombre) {
        iniciosesion = new InicioSesion();
        paypalPanel.setPreferredSize(new Dimension(800, 600));
        this.tipo = t;
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        precioLabel.setText("Precio final: " + precio + " €");
        paypalPanel.setFocusable(true);
        paypalPanel.requestFocusInWindow();

        pagarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                goToFinPago();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                backMenu();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    public void backMenu(){
        MetodoPago ventanaCatalog = new MetodoPago(tipo, id, precio, nombre);
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaCatalog.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }
    public void goToFinPago(){
        if(checkCorrectContent())
        {
            JOptionPane.showMessageDialog(null, "Porfavor compruebe que el email sea valido");
        }else{
            if(guardaCheckBox.isSelected()){ savePaypal();}

            CatalogoProductos ventanaCatalog = new CatalogoProductos(tipo, id, nombre);
            JFrame ventanaAtras = new JFrame("Smart Trade");
            ventanaAtras.setContentPane(ventanaCatalog.getPanel());
            ventanaAtras.pack();
            ventanaAtras.setVisible(true);
            JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
            ventanaActual.dispose();
        }
    }

    public void savePaypal()
    {
        String url = "http://localhost:8080/paypal/addPaypal";
        HttpClient client = HttpClient.newHttpClient();

        Map<String, Object> data = new HashMap<>();

        System.out.println(emailField.getText() + "  " + passwordField.getText());

        data.put("email",emailField.getText());
        data.put("password",passwordField.getText());
        data.put("id",id);

        try{
            String jsonBody = new ObjectMapper().writeValueAsString(data);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Error al enviar la solicitud POST: " + e.getMessage());
        }
    }

    public boolean checkCorrectContent() {
        if(isValidEmail(emailField.getText()))
        {
            return true;
        }
        return false;
    }
    public boolean isValidEmail(String email){
        Pattern pat = null;
        Matcher match = null;
        pat = Pattern.compile("^[\\w\\-\\_\\+]+(\\.[\\w\\-\\_\\+]+)*@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}$");
        match = pat.matcher(email);
        return match.find();
    }
    public JPanel getPanel(){
        return paypalPanel;
    }
}
