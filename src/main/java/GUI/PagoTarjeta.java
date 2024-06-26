package GUI;

import Email.EmailSender;
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

public class PagoTarjeta {
    private JPanel panelTitulo;
    private JLabel backButton;
    private JLabel perfilButton;
    private JTextField numeroField;
    private JCheckBox guardaCheckBox;
    private JButton pagarButton;
    private JTextField nombreField;
    private JTextField cvvField;
    private JTextField fechaField;
    private JLabel precioLabel;
    private JPanel cardPanel;
    private InicioSesion iniciosesion;
    private int tipo;
    private int id;
    private double precio;
    private String errorMes;
    private String nombre;

    public PagoTarjeta(int t, int id, double precio, String nombre) {
        System.out.println("El nombre es: " + nombre);
        iniciosesion = new InicioSesion();
        cardPanel.setPreferredSize(new Dimension(800, 600));
        this.tipo = t;
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        precioLabel.setText("Precio final: " + precio + " €");
        cardPanel.setFocusable(true);
        cardPanel.requestFocusInWindow();

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                backMenu();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                backButton.setBackground(new Color(73, 231, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                backButton.setBackground(new Color(153, 233, 255));
            }
        });

        pagarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                goToFinPago();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                pagarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                pagarButton.setBackground(new Color(73, 231, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                pagarButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                pagarButton.setBackground(new Color(153, 233, 255));
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
    public void sendEmail(int identificador, String nombre) {
        String url = "http://localhost:8080/user/email?user_id=" + identificador;
        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            String responseBody = response.body();
            System.out.println(responseBody + " el código es: " + statusCode);
            if (statusCode == 200) {
                EmailSender.enviarCorreo(responseBody,nombre, precio);
                JOptionPane.showMessageDialog(null, "El pago se ha realizado correctamente, en breves le llegará un correo confirmándolo");
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "El pedido no es correcto");
        }
    }

    public void goToFinPago(){
        if(checkCorrectContent())
        {
            JOptionPane.showMessageDialog(null, errorMes);
        }else{
            if(guardaCheckBox.isSelected()){
                saveCard(numeroField.getText(), nombreField.getText(), cvvField.getText(), fechaField.getText(), id);
            }
            sendEmail(id, nombre);
            deleteProducts(id);
            CatalogoProductos ventanaCatalog = new CatalogoProductos(tipo, id, nombre);
            JFrame ventanaAtras = new JFrame("Smart Trade");
            ventanaAtras.setContentPane(ventanaCatalog.getPanel());
            ventanaAtras.pack();
            ventanaAtras.setVisible(true);
            JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
            ventanaActual.dispose();
        }
    }
    public void saveCard(String number, String name, String cvv, String date, int id)
    {
        String url = "http://localhost:8080/card/addcard";
        HttpClient client = HttpClient.newHttpClient();

        Map<String, Object> data = new HashMap<>();

        System.out.println(numeroField.getText() + " " + nombreField.getText() + " " + cvvField.getText() + " " + fechaField.getText());

        data.put("number", number);
        data.put("name",name);
        data.put("cvv",cvv);
        data.put("expireDate",date);
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
        int aux = 0;
        if(isValidCreditCard(numeroField.getText()))
        {
            aux++;
            errorMes = "El numero de la tarjeta no es valido";
        }
        if(isValidDate(fechaField.getText()))
        {
            if(aux == 0)
            {
                aux++;
                errorMes = "La fecha no es valida";
            }else
            {
                errorMes = errorMes + "/n" + "La fecha no es valida";
            }
        }
        if(isValidThreeDigits(cvvField.getText()))
        {
            if(aux == 0)
            {
                aux++;
                errorMes = "El cvv no es valido";
            }else
            {
                errorMes = errorMes + "/n" + "El cvv no es valido";
            }
        }
        return 0 == aux;
    }
    public void deleteProducts(int ident){
        String url = "http://localhost:8080/cart/deleteCart";
        HttpClient client = HttpClient.newHttpClient();

        try {
            Map<String, Object> productData = new HashMap<>();
            productData.put("u_id", ident);
            String jsonBody = new ObjectMapper().writeValueAsString(productData);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            String responseBody = response.body();
            System.out.println(responseBody + " el código es: " + statusCode);
            if (statusCode == 200) {
                System.out.println("Los productos se han eliminado del carrito");
            }
        }catch(Exception e){
            e.printStackTrace();}

    }
    public boolean isValidCreditCard(String cardNumber){
        Pattern pat = null;
        Matcher match = null;
        // La expresión regular para números de tarjeta de crédito (13 a 19 dígitos)
        pat = Pattern.compile("^\\d{13,19}$");
        match = pat.matcher(cardNumber);
        return match.find();
    }
    public boolean isValidDate(String date){
        Pattern pat = null;
        Matcher match = null;
        // La expresión regular para el formato "MM/YY"
        pat = Pattern.compile("^(0[0-1]|1[1-9])/\\d{2}$");
        match = pat.matcher(date);
        return match.find();
    }
    public boolean isValidThreeDigits(String input){
        Pattern pat = null;
        Matcher match = null;
        // La expresión regular para exactamente 3 dígitos
        pat = Pattern.compile("^\\d{3}$");
        match = pat.matcher(input);
        return match.find();
    }
    public JPanel getPanel(){
        return cardPanel;
    }
}
