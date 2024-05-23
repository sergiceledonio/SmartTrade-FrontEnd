package GUI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class VerListaAmigo extends JDialog {
    private JPanel contentPane;
    private JPanel panelListaAmigo;
    private JLabel title;
    private String friend;
    private int id;
    private int prodId;
    private byte[] img;


    public VerListaAmigo(int i, String f) {

        this.friend = f;
        this.id = i;

        panelListaAmigo = new JPanel();
        panelListaAmigo.setBackground(new Color(198,232,251));
        panelListaAmigo.setLayout(new BoxLayout(panelListaAmigo, BoxLayout.Y_AXIS));

        contentPane = new JPanel();
        contentPane.setPreferredSize(new Dimension(700, 700));
        contentPane.setLayout(new BorderLayout());

        setContentPane(contentPane);
        title.setText("Lista de " + friend);
        contentPane.add(title, BorderLayout.PAGE_START);
        contentPane.add(new JScrollPane(panelListaAmigo), BorderLayout.CENTER);

        setModal(true);
        getGifts(id, friend);

    }



    public static void main(String[] args) {
        VerListaAmigo dialog = new VerListaAmigo(0,"Mi colegón");
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public void getGifts(int userId, String friend) {
        HttpClient httpClient = HttpClient.newHttpClient();
        String url = "http://localhost:8080/gift/giftList";
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            System.out.println("Amigo: " + friend + ", ID user: " + userId);
            Map<String, Object> body = new HashMap<>();
            body.put("user_id", userId);
            body.put("friend", friend);

            String jsonBody = new ObjectMapper().writeValueAsString(body);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            int statusCode = response.statusCode();
            System.out.println("Código es: " + statusCode);
            System.out.println(responseBody);
            if (statusCode == 200) {
                JsonNode jsonResponse = objectMapper.readTree(responseBody);
                System.out.println("Ha devuelto la lista de regalos: " + responseBody);

                panelListaAmigo.removeAll();

                for (JsonNode productoNode : jsonResponse) {
                    String nombre = productoNode.get("name").asText();
                    int price = productoNode.get("price").asInt();
                    String description = productoNode.get("description").asText();
                    prodId = productoNode.get("id").asInt();
                    img = productoNode.get("image").binaryValue();

                    addProduct(nombre, description, price, panelListaAmigo, img);
                }

                panelListaAmigo.revalidate();
                panelListaAmigo.repaint();
            } else {
                System.out.println("No devuelve nada");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    private void addProduct(String name, String desc, int price, JPanel panel, byte[] img){
        JPanel panelProducto = new JPanel();
        panelProducto.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        panelProducto.setLayout(new BorderLayout());
        panelProducto.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        JPanel panelDes = new JPanel(new BorderLayout());

        JLabel labelNombre = new JLabel(name);
        labelNombre.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel labelDescripcion = new JLabel(desc);
        JLabel labelPrecio = new JLabel("Precio: " + price + "€");

        ImageIcon originalIcon = new ImageIcon(img);
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        JLabel labelImg = new JLabel(resizedIcon);

        JButton addProduct = new JButton("Añadir al carrito");
        JButton delete = new JButton("Eliminar de la lista");

        delete.setBackground(new Color(153, 233, 255));
        addProduct.setBackground(new Color(153, 233, 255));

        addProduct.setPreferredSize(new Dimension(150, 35));
        delete.setPreferredSize(new Dimension(150, 35));

        panelPrincipal.add(labelImg, BorderLayout.NORTH);
        panelPrincipal.add(labelNombre, BorderLayout.CENTER);

        panelDes.add(labelDescripcion, BorderLayout.CENTER);
        panelDes.add(panelBotones, BorderLayout.SOUTH);

        panelProducto.add(panelPrincipal, BorderLayout.WEST);
        panelProducto.add(panelDes, BorderLayout.CENTER);
        panelProducto.add(labelPrecio, BorderLayout.EAST);

        panelBotones.add(addProduct);
        panelBotones.add(delete);

        delete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int opcion = JOptionPane.showConfirmDialog(null, "¿Seguro que quieres eliminar el producto de la lista?", "Producto de amigo", JOptionPane.YES_NO_OPTION);
                if(opcion == JOptionPane.YES_OPTION){
                    deleteProductFriend(name);
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                delete.setBackground(new Color(73, 231, 255));
                delete.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                delete.setBackground(new Color(153, 233, 255));
                delete.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        addProduct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int opcion = JOptionPane.showConfirmDialog(null, "¿Quieres añadir el producto al carrito?", "Producto de amigo", JOptionPane.YES_NO_OPTION);
                if(opcion == JOptionPane.YES_OPTION){

                    newCartProduct(id, prodId,1);
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                addProduct.setBackground(new Color(73, 231, 255));
                addProduct.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                addProduct.setBackground(new Color(153, 233, 255));
                addProduct.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        panel.add(panelProducto, BorderLayout.SOUTH);
    }


    private void deleteProductFriend(String prodName){

        String url = "http://localhost:8080/gift/delete";
        HttpClient client = HttpClient.newHttpClient();

        try{
            Map<String, Object> data = new HashMap<>();
            data.put("product_name", prodName);
            data.put("user_id", id);
            String jsonBody = new ObjectMapper().writeValueAsString(data);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();

            if(statusCode == 200){
                System.out.println("Elemento borrado de la lista correctamente");
            }else{
                System.out.println("Problema con la eliminación del producto, error:" + statusCode);
            }

            getGifts(id, friend);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void newCartProduct(int userId, int prodId, int amount){

        String url = "http://localhost:8080/cart/newCartProduct";
        HttpClient client = HttpClient.newHttpClient();

        try{
            Map<String, Integer> data = new HashMap<>();
            data.put("user_id", userId);
            data.put("p_id", prodId);
            data.put("amount", amount);
            String jsonBody = new ObjectMapper().writeValueAsString(data);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 200){
                System.out.println("La respuesta es: " + response.body());
                if(Integer.parseInt(response.body()) == 1){
                    System.out.println("Se añade el producto sin problemas");
                    JOptionPane.showMessageDialog(null,"El producto se ha añadido al carrito correctamente", "Producto añadido al carrito", JOptionPane.INFORMATION_MESSAGE );
                }else{
                    System.out.println("El producto ya está añadido al carrito");
                    JOptionPane.showMessageDialog(null,"El producto estaba ya añadido al carrito", "Producto ya añadido anteriormentegitgit ", JOptionPane.INFORMATION_MESSAGE );
                }
            }else{
                System.out.println("ERROR EN LA PETICIÓN");
            }
        }catch(IOException | InterruptedException e){
            e.printStackTrace();
        }


    }
}
