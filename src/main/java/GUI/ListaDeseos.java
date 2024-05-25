package GUI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ListaDeseos extends JFrame {

    private JPanel panelDeseos;
    private JPanel panelInfo;
    private JLabel logoButton;
    private JButton filtroButton;
    private JLabel perfilButton;
    private JLabel lupaButton;
    private JPanel panelListDeseos;
    private static String name;
    private static String password;
    private static String email;
    private static String iban;
    private static String cif;
    private static String dni;
    private static String city;
    private static String street;
    private static String door;
    private static String flat;
    private static String num;
    private static String type;
    private static String price;
    private static String description;
    private static String nameProd;
    private static String category;
    private InicioSesion iniciosesion;
    private byte[] img;
    private int tipo;
    private int id;
    private String nombre;



    public ListaDeseos(int t, int id, String n) {
        iniciosesion = new InicioSesion();
        panelDeseos.setPreferredSize(new Dimension(800, 600));

        this.tipo = t;
        this.id = id;
        this.nombre = n;

        panelListDeseos = new JPanel();
        panelDeseos.setFocusable(true);
        panelDeseos.requestFocusInWindow();
        goBackWithEsc(panelDeseos);

        getDeseosProducts(id);
        inicializarComponentes();


        logoButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                backMenu();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                logoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                logoButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        perfilButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                perfilButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent e) {
                perfilButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

    }


    public static void main(String[] args) {
        ListaDeseos listaDeseos = new ListaDeseos(0, 0, "");
        listaDeseos.setMain();
    }

    /*ACCESO A LAS VARIABLES LOCALES*/

    public JPanel getPanel(){
        return panelDeseos;
    }

    private void setMain(){
        ListaDeseos ventanaDeseo = new ListaDeseos(tipo, id, nombre);
        JFrame frame = new JFrame("Smart Trade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(ventanaDeseo.getPanel());
        frame.pack();
        frame.setVisible(true);
    }

    public void getDeseosProducts(int userId){
        HttpClient httpClient = HttpClient.newHttpClient();
        String url = "http://localhost:8080/wish/wishList?user_id=" + userId;
        ObjectMapper objectMapper = new ObjectMapper();

        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            int statusCode = response.statusCode();
            System.out.println("Código es: " + statusCode);
            if(statusCode == 200){
                JsonNode jsonResponse = objectMapper.readTree(responseBody);
                System.out.println("Ha devuelto el deseo: " + responseBody);

                JPanel panelProduct = new JPanel();
                panelProduct.setLayout(new BoxLayout(panelProduct, BoxLayout.Y_AXIS));

                for (JsonNode productoNode : jsonResponse) {
                    String nombre = productoNode.get("name").asText();
                    String descripcion = productoNode.get("description").asText();
                    double precio = productoNode.get("price").asDouble();
                    img = productoNode.get("image").binaryValue();

                    addProduct(nombre, descripcion, precio, panelProduct, img);
                }

                panelListDeseos.removeAll();
                panelListDeseos.add(panelProduct);
                panelListDeseos.revalidate();
                panelListDeseos.repaint();

            } else {
                System.out.println("No devuelve nada");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void addProduct(String name, String desc, double price, JPanel panel, byte[] img){
        JPanel panelProducto = new JPanel();
        panelProducto.setLayout(new BorderLayout());
        panelProducto.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panelProducto.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        JPanel panelDes = new JPanel(new BorderLayout());

        JButton buttonMenos = new JButton("Eliminar");
        JButton buyButton = new JButton("Añadir al carrito");

        buyButton.setBackground(new Color(153, 233, 255));
        buyButton.setPreferredSize(new Dimension(185, 45));
        buttonMenos.setBackground(new Color(153, 233, 255));
        buttonMenos.setPreferredSize(new Dimension(105, 45));

        JLabel labelNombre = new JLabel(name);
        labelNombre.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel labelDescripcion = new JLabel(desc);
        JLabel labelPrecio = new JLabel("Precio: " + price + "€");

        ImageIcon originalIcon = new ImageIcon(img);
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        JLabel labelImg = new JLabel(resizedIcon);

        panelPrincipal.add(labelImg, BorderLayout.NORTH);
        panelPrincipal.add(labelNombre, BorderLayout.CENTER);

        panelDes.add(labelDescripcion, BorderLayout.CENTER);
        panelDes.add(panelBotones, BorderLayout.SOUTH);

        panelProducto.add(panelPrincipal, BorderLayout.WEST);
        panelProducto.add(panelDes, BorderLayout.CENTER);
        panelProducto.add(labelPrecio, BorderLayout.EAST);

        panelBotones.add(buyButton);
        panelBotones.add(buttonMenos);

        buttonMenos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                    int opcion = JOptionPane.showConfirmDialog(null, "¿Seguro que quieres eliminar el producto del carrito?", "Producto del carrito", JOptionPane.YES_NO_OPTION);
                    if(opcion == JOptionPane.YES_OPTION){
                        deleteProductFromWish(name);
                    }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                buttonMenos.setBackground(new Color(73, 231, 255));
                buttonMenos.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonMenos.setBackground(new Color(153, 233, 255));
                buttonMenos.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        buyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                 Integer prodId = getProductId(name);
                 addToCart(id, prodId, 1);
            }

            @Override
            public void mouseEntered(MouseEvent e){
                 buyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                 buyButton.setBackground(new Color(73,231,255));
            }
            @Override
            public void mouseExited(MouseEvent e){
                  buyButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                  buyButton.setBackground(new Color(153,233,255));
            }
        });

        panel.add(panelProducto);
    }

    public void deleteProductFromWish(String productName){

        String url = "http://localhost:8080/wish/delete";
        HttpClient client = HttpClient.newHttpClient();

        try{
            Map<String, Object> productData = new HashMap<>();
            productData.put("user_id", id);
            productData.put("product_name", productName);
            String jsonBody = new ObjectMapper().writeValueAsString(productData);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            if(statusCode == 200){
                System.out.println("Elemento borrado de la lista de deseos correctamente");
            }else{
                System.out.println("Problema con la eliminación del producto, error:" + statusCode);
            }

            getDeseosProducts(id);

        }catch(Exception e){
            e.printStackTrace();
        }
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

    private void inicializarComponentes(){
        panelDeseos.setLayout(new BorderLayout());

        panelDeseos.add(panelInfo, BorderLayout.NORTH);
        panelInfo.setPreferredSize(new Dimension(800, 150));



        panelListDeseos.setLayout(new BoxLayout(panelListDeseos, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(panelListDeseos);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        panelDeseos.add(scrollPane, BorderLayout.CENTER);
        panelDeseos.setBackground(new Color(198, 232, 251));
    }

    public void goBackWithEsc(JComponent component){
        component.addKeyListener(
                new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                            backMenu();
                        }
                    }
                }
        );
    }
    public void addToCart(int userId, int prodId, int amount){

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
    public int getProductId(String pName){

        int productId = 0;
        try {

            String encodedProductName = URLEncoder.encode(pName , StandardCharsets.UTF_8.toString());
            String url = "http://localhost:8080/product/getbyname/" + encodedProductName;

            System.out.println(url);
            HttpClient client = HttpClient.newHttpClient();


            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            String responseBody = response.body();
            if (statusCode == 200) {
                System.out.println("GET request successful: " + statusCode);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(responseBody);

                productId = jsonNode.get("id").asInt();
            } else {
                System.out.println("Problem with client: " + statusCode);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error al enviar la petición: " + e.getMessage());
            e.printStackTrace();
        }

        return productId;

    }
}
