package GUI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

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
    private JPanel panelInfo;
    private String prodName;
    private String prodDescription;
    private String prodType;
    private String prodPrice;
    private int prodId;

    public ProductoPendiente(String name, String price, String type, String description, int id){
        this.prodName = name;
        this.prodDescription = description;
        this.prodPrice = price;
        this.prodType = type;
        this.prodId = id;
        validarPanel.setPreferredSize(new Dimension(800,600));
        productDescription.setPreferredSize(new Dimension(500, 200));

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
                validateProduct(true);
                backMenuValidation();
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
                validateProduct(false);
                backMenuValidation();
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
        ProductoPendiente ventanaInfoValidate = new ProductoPendiente("Caralmendra", "", "", "", 0);
        JFrame frame = new JFrame("Smart Trade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(ventanaInfoValidate.getPanel());
        frame.pack();
        frame.setVisible(true);
    }

    public JPanel getPanel(){
        return validarPanel;
    }

    private void backMenuValidation(){
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        SwingUtilities.invokeLater(() -> {
            ValidacionProductosLista nuevaVentanaValidacion = new ValidacionProductosLista();
            nuevaVentanaValidacion.setTitle("Smart Trade");
            nuevaVentanaValidacion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            nuevaVentanaValidacion.setSize(800, 600);
            nuevaVentanaValidacion.setLocationRelativeTo(null);
            nuevaVentanaValidacion.setVisible(true);
        });
        ventanaActual.dispose();
    }

    private void validateProduct(Boolean valid){

        String url = "http://localhost:8080/product/validate";
        HttpClient client = HttpClient.newHttpClient();

        try{
            ObjectNode productData = new ObjectMapper().createObjectNode();
            productData.put("id", prodId);
            productData.put("valid", valid);

            String jsonBody = productData.toString();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            System.out.println("El código es: " + statusCode);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

}

