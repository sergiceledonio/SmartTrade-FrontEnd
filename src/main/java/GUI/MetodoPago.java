package GUI;

import Email.EmailSender;
import Observer.ObserverUserData;
import Strategy.Card;
import Strategy.Payment;
import Strategy.Paypal;
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
    private JPanel panelFinal;
    private InicioSesion iniciosesion;
    private int tipo;
    private int id;
    private double precio;
    private String selectedLabelName;
    private JPanel lastSelectedPanel;
    private JPanel selectedPanel;
    private String nombre;

    private Payment context = new Payment();

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
                context.setMethod(new Paypal());
                context.executePayment(tipo, id, precio, nombre, getPanel());
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
                context.setMethod(new Card());
                context.executePayment(tipo, id, precio, nombre, getPanel());
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

        JPanel listaPanel = new JPanel();
        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));

        // Agregar el contenedor al JScrollPane
        panelLista.setViewportView(listaPanel);

        // Poblar el panel con etiquetas
        String[] strings = getStrings();
        addLabelsToPanel(listaPanel, strings);
    }

    private void addLabelsToPanel(JPanel listaPanel, String[] strings) {
        int auxi = 0;
        for (String str : strings) {
            System.out.println(str);
            JPanel panel = new JPanel();
            panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            panel.setLayout(new BorderLayout());
            panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {

                updateSelectedLabel(panel);

                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });

            JLabel label = new JLabel(str);
            label.setFont(new Font("Arial", Font.BOLD, 16));
            panel.add(label, BorderLayout.CENTER);
            listaPanel.add(panel);
            panel.setName("panel_" + auxi);
        }
        listaPanel.revalidate();
        listaPanel.repaint();
    }

    private void updateSelectedLabel(JPanel panel) {
        panel.setBackground(new Color(128, 191, 255)); // Cambiar el fondo del panel seleccionado al nuevo color
        lastSelectedPanel = selectedPanel; // Actualizar la referencia del último panel seleccionado
        selectedPanel = panel; // Actualizar la referencia del panel actualmente seleccionado
        System.out.println("Selected Label: " + panel.getName());
        if (lastSelectedPanel != null) {
            lastSelectedPanel.setBackground(Color.WHITE); // Restaurar el fondo del último panel seleccionado a blanco
        }
    }

    private String[] getStrings() {
        String[] aux = getCards();
        String[] aux2 = getPaypals();

        if (aux == null) {
            aux = new String[0];
        }
        if (aux2 == null) {
            aux2 = new String[0];
        }

        String[] result = new String[aux.length + aux2.length];

        System.arraycopy(aux, 0, result, 0, aux.length);
        System.arraycopy(aux2, 0, result, aux.length, aux2.length);

        System.out.println(Arrays.toString(result));
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
        try {HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            System.out.println(responseBody);
            if (response.statusCode() == 200) {
                JsonNode jsonResponse = objectMapper.readTree(responseBody);
                int index = 0;
                auxArray = new String[jsonResponse.size()];
                for(JsonNode item : jsonResponse){
                        aux = item.get("number").asText();
                        aux = "Tarjeta: " + aux;
                        auxArray[index++] = aux;
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return auxArray;
    }

    public String[] getPaypals(){
        String aux;
        String[] auxArray = new String[]{};
        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/paypal/paypalsbyuser?id=" + id))
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            System.out.println(responseBody);
            if (response.statusCode() == 200) {
                JsonNode jsonResponse = objectMapper.readTree(responseBody);
                    int index = 0;
                    auxArray = new String[jsonResponse.size()];
                    for(JsonNode item : jsonResponse){
                        aux = item.get("email").asText();
                        aux = "Paypal: " + aux;
                        auxArray[index] = aux;
                        index++;
                    }
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
        if(lastSelectedPanel == null)
        {
            JOptionPane.showMessageDialog(null, "Escoja un metodo de pago o añada uno nuevo");
        }else{

            sendEmail(id, nombre);

            CatalogoProductos ventanaCatalog = new CatalogoProductos(tipo, id, nombre);
            JFrame ventanaAtras = new JFrame("Smart Trade");
            ventanaAtras.setContentPane(ventanaCatalog.getPanel());
            ventanaAtras.pack();
            ventanaAtras.setVisible(true);
            JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
            ventanaActual.dispose();
        }
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
            System.out.println("El nombre es: " + nombre);
            if (statusCode == 200) {
                EmailSender.enviarCorreo(responseBody,nombre, precio);
                JOptionPane.showMessageDialog(null, "El pago se ha realizado correctamente, en breves le llegará un correo confirmándolo");
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "El correo no se ha podido enviar con éxito");
        }
    }
    public JPanel getPanel(){
        return panelMetodo;
    }
}
