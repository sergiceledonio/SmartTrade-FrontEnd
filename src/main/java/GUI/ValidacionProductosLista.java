package GUI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ValidacionProductosLista extends JFrame{
    private JPanel panelTitle;
    private JLabel logoButton;
    private JPanel panelListaValidos;
    private JPanel panelValidacion;
    private JLabel backLogin;
    private JLabel productNameLabel;
    private JButton verInformaciónButton;
    private JPanel panelProducto;
    private String nameProduct;
    private String desc;
    private String price;
    private String type;
    private List<Map<String, Object>> productList;
    private JFrame frame;
    private JScrollPane scrollPane;
    private JPanel panelContenedor;

    public ValidacionProductosLista(){
        iniciarComponentes();

        productList = getPetitions();
        scrollPane = createScrollPane(createProductoPanels(productList));

        panelValidacion.add(panelTitle);
        panelContenedor.add(scrollPane);
        panelListaValidos.add(panelContenedor);
        setContentPane(panelListaValidos);

        backLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                backLogin();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                backLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backLogin.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        verInformaciónButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                verInformaciónButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                verInformaciónButton.setBackground(new Color(73, 231, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                verInformaciónButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                verInformaciónButton.setBackground(new Color(153, 233, 255));
            }
        });

    }



    public static void main(String[] args) {
        invoke();
    }

    private void iniciarComponentes(){
        panelValidacion = new JPanel();
        panelTitle = new JPanel();
        panelContenedor = new JPanel();
        panelListaValidos = new JPanel();

        panelValidacion.setPreferredSize(new Dimension(800,700));
        panelTitle.setPreferredSize(new Dimension(800, 200));
        panelListaValidos.setPreferredSize(new Dimension(800, 400));
    }

    public void goValidate(String nombre, String precio, String categoria, String descripcion) {
        System.out.println(nombre + " " + precio + " " + categoria + " " + descripcion);

        ProductoPendiente ventanaValidar = new ProductoPendiente(nombre, precio, categoria, descripcion);
        JFrame registro = new JFrame("Smart Trade");
        registro.setContentPane(ventanaValidar.getPanel());
        registro.pack();
        registro.setVisible(true);
        this.dispose();
    }

    public static void invoke(){
        SwingUtilities.invokeLater(() -> {
            System.out.println("**************************************");
            ValidacionProductosLista ventanaValidacion = new ValidacionProductosLista();
            ventanaValidacion.setTitle("Smart Trade");
            ventanaValidacion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventanaValidacion.setSize(800, 600);
            ventanaValidacion.setLocationRelativeTo(null);
            ventanaValidacion.setVisible(true);
        });
    }

    /*ACCESO A LOS ATRIBUTOS*/

    private List<JPanel> createProductoPanels(List<Map<String, Object>> productList) {
        List<JPanel> productoPanels = new ArrayList<>();

        for (Map<String, Object> product : productList) {
            JPanel productoPanel = createProductoPanel(product);
            productoPanels.add(productoPanel);
        }

        return productoPanels;
    }
    private JPanel createProductoPanel(Map<String, Object> product) {
        JPanel productoPanel = new JPanel();
        productoPanel.setPreferredSize(new Dimension(700, 100));
        productoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String productName = (String) product.get("name");
        JLabel productNameLabel = new JLabel(productName);
        JButton button = new JButton("Ver Producto");

        button.setBackground(verInformaciónButton.getBackground());
        button.setForeground(verInformaciónButton.getForeground());

        productoPanel.add(productNameLabel);
        productoPanel.add(button);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                System.out.println(productNameLabel.getText());
                getInfoProduct(productNameLabel.getText());
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                button.setBackground(new Color(73, 231, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                button.setBackground(new Color(153, 233, 255));
            }
        });

        return productoPanel;
    }
    private JScrollPane createScrollPane(List<JPanel> productoPanels) {
        JPanel panelContenedor = new JPanel();
        panelContenedor.setLayout(new BoxLayout(panelContenedor, BoxLayout.Y_AXIS));

        for (JPanel productoPanel : productoPanels) {
            panelContenedor.add(productoPanel);
        }

        JScrollPane scrollPane = new JScrollPane(panelContenedor);
        scrollPane.setPreferredSize(new Dimension(750, 400));

        return scrollPane;
    }


    public JPanel getPanel(){
        return panelValidacion;
    }
    private void backLogin(){
        InicioSesion ventanaInicio = new InicioSesion();
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaInicio.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }


    private List<Map<String, Object>> getPetitions(){
        String url = "http://localhost:8080/product/pending";
        HttpClient client = HttpClient.newHttpClient();
        List<Map<String, Object>> productsList = null;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            String responseBody = response.body();
            if (statusCode == 200) {
                System.out.println("GET request successful: " + statusCode);
                ObjectMapper objectMapper = new ObjectMapper();
                productsList = objectMapper.readValue(responseBody,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class));
                System.out.println("Productos pendientes:");
                for (Map<String, Object> product : productsList) {
                    System.out.println(product);
                }
            } else {
                System.out.println("Problem with client: " + statusCode);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error al enviar la petición: " + e.getMessage());
            e.printStackTrace();
        }
        return productsList;
    }

    private void getInfoProduct(String name){
        String encodedName = encodeName(name);

        String baseUrl = "http://localhost:8080/product/getbyname/" + encodedName;
        System.out.println(baseUrl);
        try {

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Código de respuesta: " + response.statusCode());
            System.out.println("Respuesta del servidor: " + response.body());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.body());

            nameProduct = jsonNode.get("name").asText();
            price = jsonNode.get("price").asText();
            type = jsonNode.get("type").asText();
            desc = jsonNode.get("description").asText();

        } catch (Exception e) {
            System.out.println("Error al enviar la solicitud GET: " + e.getMessage());
        }
        goValidate(nameProduct, price, type, desc);
    }

    public static String encodeName(String name) {
        try {
            return URLEncoder.encode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
