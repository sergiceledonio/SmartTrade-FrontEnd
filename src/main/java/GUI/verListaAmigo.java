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

public class verListaAmigo extends JDialog {
    private JPanel contentPane;
    private JPanel panelListaAmigo;
    private JLabel title;
    private String friend;
    private int id;
    private int prodId;


    public verListaAmigo(int i, String f) {
        this.friend = f;
        this.id = i;
        panelListaAmigo = new JPanel();
        contentPane.setPreferredSize(new Dimension(500, 500));
        setContentPane(contentPane);
        setModal(true);
        title.setText("Lista de " + friend);
        getGifts(id, friend);

    }



    public static void main(String[] args) {
        verListaAmigo dialog = new verListaAmigo(0,"Mi colegón");
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public void getGifts(int userId, String friend){
        HttpClient httpClient = HttpClient.newHttpClient();
        String url = "http://localhost:8080/gift/giftList";
        ObjectMapper objectMapper = new ObjectMapper();

        try{

            System.out.println("Amigo: " + friend + " ID user: " + userId);
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
            if(statusCode == 200){
                JsonNode jsonResponse = objectMapper.readTree(responseBody);
                System.out.println("Ha devuelto la lista de regalos: " + responseBody);

                JPanel panelProductos = new JPanel();
                panelProductos.setLayout(new BoxLayout(panelProductos, BoxLayout.Y_AXIS));

                for (JsonNode productoNode : jsonResponse) {
                String nombre = productoNode.get("name").asText();
                int price = productoNode.get("price").asInt();
                String description = productoNode.get("description").asText();
                prodId = productoNode.get("id").asInt();

                addProduct(nombre, description, price, panelProductos);
                }

                panelListaAmigo.removeAll();
                panelListaAmigo.add(panelProductos);
                panelListaAmigo.revalidate();
                panelListaAmigo.repaint();

            } else {
                System.out.println("No devuelve nada");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void addProduct(String name, String desc, int price, JPanel panel){

        JPanel panelProducto = new JPanel();
        panelProducto.setLayout(new BorderLayout());
        panelProducto.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panelProducto.setPreferredSize(new Dimension(770, 100));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel labelNombre = new JLabel("Nombre: " + name);
        JLabel labelDescripcion = new JLabel("Descripción: " + desc);
        JLabel labelPrecio = new JLabel("Precio: " + price + "€");

        JButton addToCart = new JButton("Añadir al carrito");
        JButton delete = new JButton("Eliminar de la lista");

        addToCart.setBackground(new Color(153, 233, 255));
        delete.setBackground(new Color(153, 233, 255));

        panelProducto.add(labelNombre, BorderLayout.NORTH);
        panelProducto.add(labelDescripcion, BorderLayout.WEST);
        panelProducto.add(labelPrecio, BorderLayout.EAST);
        panelProducto.add(panelBotones, BorderLayout.CENTER);

        panelBotones.add(addToCart);
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

        addToCart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int opcion = JOptionPane.showConfirmDialog(null, "¿Quieres añadir el producto al carrito?", "Producto de amigo", JOptionPane.YES_NO_OPTION);
                if(opcion == JOptionPane.YES_OPTION){

                    newCartProduct(id, prodId,1);
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                addToCart.setBackground(new Color(73, 231, 255));
                addToCart.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                addToCart.setBackground(new Color(153, 233, 255));
                addToCart.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        panel.add(panelProducto);
    }


    private void deleteProductFriend(String prodName){

        String url = "http://localhost:8080/gift/delete";
        HttpClient client = HttpClient.newHttpClient();

        try{
            System.out.println("user id: " + id);
            System.out.println("product name: " + prodName);
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
