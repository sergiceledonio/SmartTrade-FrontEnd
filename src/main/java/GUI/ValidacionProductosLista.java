package GUI;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class ValidacionProductosLista extends JFrame{
    private JPanel panelTitulo;
    private JLabel logoButton;
    private JPanel panelListaValidos;
    private JPanel panelValidacion;
    private JList validatingList;
    private JLabel backLogin;
    private List<Map<String, Object>> productList;

    public ValidacionProductosLista(){
        panelValidacion.setPreferredSize(new Dimension(800,600));
        panelTitulo.setPreferredSize(new Dimension(800, 200));
        panelListaValidos.setPreferredSize(new Dimension(800, 400));
        getPetitions();
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
    }


    public static void main(String[] args) {
        ValidacionProductosLista ventanaValidacion = new ValidacionProductosLista();
        JFrame frame = new JFrame("Smart Trade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(ventanaValidacion.getPanel());
        frame.pack();
        frame.setVisible(true);
    }


    /*ACCESO A LOS ATRIBUTOS*/
    private JPanel getPanel(){
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
}
