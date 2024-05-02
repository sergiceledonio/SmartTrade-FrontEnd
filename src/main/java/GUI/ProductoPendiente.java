package GUI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ProductoPendiente {
    private JPanel panelTitulo;
    private JLabel logoButton;
    private JLabel backValidating;
    private JPanel validarPanel;
    private JLabel productName;
    private JLabel productImg;
    private JTextArea productDescription;
    private JButton validateButton;
    private JButton cancelButton;
    private String prodName;
    private String prodDescription;
    private String prodType;
    private String prodPrice;

    public ProductoPendiente(String name, String description, String price, String type){
        this.prodName = name;
        this.prodDescription = description;
        this.prodPrice = price;
        this.prodType = type;
        productName = new JLabel();
        //validarPanel = new JPanel();

        productName.setText(prodName);
        productDescription.setText(prodDescription);
        productDescription.setForeground(new Color(0, 0,0));
        productDescription.setEditable(false);

        backValidating.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                backMenuValidation();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                backValidating.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backValidating.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        validateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // addProduct(prodName, prodDescription, prodPrice, prodType);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                validateButton.setBackground(new Color(73, 231, 255));
                validateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                validateButton.setBackground(new Color(153, 233, 255));
                validateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //cancel product
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                cancelButton.setBackground(new Color(73, 231, 255));
                cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cancelButton.setBackground(new Color(153, 233, 255));
                cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
    }

    public static void main(String[] args) {
        ProductoPendiente ventanaInfoValidate = new ProductoPendiente("Caralmendra", "", "", "");
        System.out.println(ventanaInfoValidate.getProdName());
        JFrame frame = new JFrame("Smart Trade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(ventanaInfoValidate.getPanel());
        frame.pack();
        frame.setVisible(true);
    }

    public String getProdName(){
        return prodName;
    }
    public JPanel getPanel(){
        return validarPanel;
    }

    private void backMenuValidation(){
        ValidacionProductosLista ventanaLista = new ValidacionProductosLista();
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaLista.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }


    private void deleteOldProduct(String name){

        //Eliminar producto si no es validado

    }

   /* private void addProduct(String name, String desc, String price, String type){

        //Validar producto


        String url = "http://localhost:8080/product/newproducts";

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonBody = objectMapper.createObjectNode();

        jsonBody.put("type", type);
        jsonBody.put("name", name);
        jsonBody.put("price", price);
        jsonBody.put("description", desc);
        jsonBody.put("pending", false);
        jsonBody.put("validation", true);

        String jsonString = jsonBody.toString();

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Error al enviar la solicitud POST: " + e.getMessage());
        }

    }*/

}

