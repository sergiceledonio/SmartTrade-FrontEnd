package GUI;

import Email.EmailSender;
import Observer.ObserverUserData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static GUI.CatalogoProductos.getUserData;

public class MetodoPago {
    private JPanel panelMetodo;
    private JPanel panelTitulo;
    private JLabel backButton;
    private JLabel perfilButton;
    private JButton addTrajetaButton;
    private JButton addPaypalButton;
    private JButton aceptarButton;
    private JPanel panelInfo;
    private JScrollPane panelLista;
    private JLabel precioLabel;
    private InicioSesion iniciosesion;
    private int tipo;
    private int id;
    private double precio;
    private String selectedLabelName;
    private JLabel lastSelectedLabel;
    private String nombre;

    public MetodoPago(int t, int id, double precio, String nombre) {
        System.out.println("El nombre es: " + nombre);
        iniciosesion = new InicioSesion();
        panelMetodo.setPreferredSize(new Dimension(800, 600));
        this.tipo = t;
        this.id = id;
        this.precio = precio;
        this.nombre = nombre;
        precioLabel.setText("Precio final: " + precio + " €");
        panelMetodo.setFocusable(true);
        panelMetodo.requestFocusInWindow();
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

        aceptarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                goToFinPago();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                aceptarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                aceptarButton.setBackground(new Color(73, 231, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                aceptarButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                aceptarButton.setBackground(new Color(153, 233, 255));
            }
        });

        addPaypalButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                goToPaypal();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                addPaypalButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                addPaypalButton.setBackground(new Color(73, 231, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                addPaypalButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                addPaypalButton.setBackground(new Color(153, 233, 255));
            }
        });

        addTrajetaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                goToCard();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                addTrajetaButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                addTrajetaButton.setBackground(new Color(73, 231, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                addTrajetaButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                addTrajetaButton.setBackground(new Color(153, 233, 255));
            }
        });

       // panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));

        panelLista.setLayout(new ScrollPaneLayout());

        // Populate the panel with labels
        String[] strings = getStrings();
        addLabelsToPanel(strings);
    }

    private void addLabelsToPanel(String[] strings) {
        for (String str : strings) {
            JLabel label = new JLabel(str);
            label.setOpaque(true);
            label.setBackground(Color.LIGHT_GRAY);
            label.setPreferredSize(new Dimension(250, 30));
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    updateSelectedLabel(label);
                }
            });
            panelLista.add(label);
        }
        panelLista.revalidate();
        panelLista.repaint();
    }

    private void updateSelectedLabel(JLabel label) {
        if (lastSelectedLabel != null) {
            lastSelectedLabel.setBackground(Color.LIGHT_GRAY);
        }
        label.setBackground(Color.YELLOW);
        selectedLabelName = label.getText();
        lastSelectedLabel = label;
        System.out.println("Selected Label: " + selectedLabelName);
    }

    private String[] getStrings() {
        String[] aux = getCards();
        String[] aux2 = getPaypals();
        String[] result = Arrays.copyOf(aux, aux.length + aux2.length);
        System.arraycopy(aux2, 0, result, aux.length, aux2.length);
        return result;
    }
    public String[] getCards(){
        String aux;
        String[] auxArray = null;
        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/card/cardsbyuser?user_id=" + id))
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            if (response.statusCode() == 200) {
                JsonNode jsonResponse = objectMapper.readTree(responseBody);
                if (jsonResponse.isArray()) {
                    int size = jsonResponse.size();
                    auxArray = new String[size];
                    int index = 0;
                    for (JsonNode cardNode : jsonResponse) {
                        aux = cardNode.get("number").asText();
                        aux = "Tarjeta: " + aux;
                        auxArray[index++] = aux;
                    }
                }
            } else {
                System.out.println("Error: " + response.statusCode());
                System.out.println(response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return auxArray;
    }

    public String[] getPaypals(){
        String aux;
        String[] auxArray = null;
        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/paypal/paypalsbyuser?id=" + id))
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            if (response.statusCode() == 200) {
                JsonNode jsonResponse = objectMapper.readTree(responseBody);
                if (jsonResponse.isArray()) {
                    int size = jsonResponse.size();
                    auxArray = new String[size];
                    int index = 0;
                    for (JsonNode paypalNode : jsonResponse) {
                        aux = paypalNode.get("email").asText();
                        aux = "Paypal: " + aux;
                        auxArray[index++] = aux;
                    }
                }
            } else {
                System.out.println("Error: " + response.statusCode());
                System.out.println(response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return auxArray;
    }
    public void backMenu(){
        CatalogoProductos ventanaCatalog = new CatalogoProductos(tipo, id, nombre);
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaCatalog.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }
    public void goToPaypal(){
        PagoPaypal ventanaCatalog = new PagoPaypal(tipo, id, precio, nombre);
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaCatalog.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }

    public void goToCard(){
        PagoTarjeta ventanaCatalog = new PagoTarjeta(tipo, id, precio, nombre);
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaCatalog.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }
    public void goToFinPago(){
        if(lastSelectedLabel == null)
        {
            JOptionPane.showMessageDialog(null, "Escoja un metodo de pago o añada uno nuevo");
        }else{

            sendEmail(id);

            CatalogoProductos ventanaCatalog = new CatalogoProductos(tipo, id, nombre);
            JFrame ventanaAtras = new JFrame("Smart Trade");
            ventanaAtras.setContentPane(ventanaCatalog.getPanel());
            ventanaAtras.pack();
            ventanaAtras.setVisible(true);
            JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
            ventanaActual.dispose();
        }
    }
    public void sendEmail(int identificador) {
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
                EmailSender.enviarCorreo(responseBody, nombre);
            } else {
                JOptionPane.showMessageDialog(null, "El correo no se ha podido enviar con éxito");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public JPanel getPanel(){
        return panelMetodo;
    }
}
