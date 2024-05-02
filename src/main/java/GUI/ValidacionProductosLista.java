package GUI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class ValidacionProductosLista extends JFrame{
    private JPanel panelTitulo;
    private JLabel logoButton;
    private JPanel panelListaValidos;
    private JPanel panelValidacion;
    private JLabel backLogin;
    private JLabel productNameLabel;
    private JButton verInformaciónButton;
    private JPanel panelProducto;
    private String desc;
    private String price;
    private String type;
    private List<Map<String, Object>> productList;

    public ValidacionProductosLista(){
        panelValidacion.setPreferredSize(new Dimension(800,600));
        panelTitulo.setPreferredSize(new Dimension(800, 200));
        panelListaValidos.setPreferredSize(new Dimension(800, 400));
        getPetitions();
        getInfoProduct();
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
            public void mouseClicked(MouseEvent e) {
                goValidate();
            }
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
        ValidacionProductosLista ventanaValidacion = new ValidacionProductosLista();
        JFrame frame = new JFrame("Smart Trade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(ventanaValidacion.getPanel());
        frame.pack();
        frame.setVisible(true);
    }

    public void goValidate(){
        ProductoPendiente ventanaPendiente = new ProductoPendiente(productNameLabel.getText(), desc, price, type);
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaPendiente.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();

    }


    /*ACCESO A LOS ATRIBUTOS*/
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


    private void getPetitions(){
        String url = "http://localhost:8080/product/pending";
        HttpClient client = HttpClient.newHttpClient();

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
                List<Map<String, Object>> productList = objectMapper.readValue(responseBody,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class));

                // Hacer algo con la lista obtenida
                System.out.println("Productos pendientes:");
                for (Map<String, Object> product : productList) {
                    System.out.println(product);
                }
            } else {
                System.out.println("Problem with client: " + statusCode);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error al enviar la petición: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void printProductList() {
        if (productList != null) {
            System.out.println("Productos pendientes:");
            for (Map<String, Object> product : productList) {
                System.out.println(product);
            }
        } else {
            System.out.println("La lista de productos está vacía.");
        }
    }

    private void getInfoProduct(){

        String name = productNameLabel.getText();

        String baseUrl = "http://localhost:8080/product/getbyname/" + name;
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

            price = jsonNode.get("price").asText();
            type = jsonNode.get("type").asText();
            desc = jsonNode.get("description").asText();


        } catch (Exception e) {
            System.out.println("Error al enviar la solicitud GET: " + e.getMessage());
        }
    }
}
