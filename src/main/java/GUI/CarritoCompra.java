package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import Observer.ObserverUserData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;

import static GUI.CatalogoProductos.getUserData;

public class CarritoCompra extends JFrame implements ObserverUserData{
    private JPanel panelCarrito;
    private JPanel panelInfo;
    private JLabel logoButton;
    private JButton filtroButton;
    private JLabel perfilButton;
    private JLabel lupaButton;
    private JPanel panelCompras;
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
    private int tipo;
    private int id;
    private JFrame frame;
    private int cantidad;
    private double precio;


    public CarritoCompra(int t, int id) {
        iniciosesion = new InicioSesion();
        iniciosesion.addObserver(this);
        panelCarrito.setPreferredSize(new Dimension(800, 600));
        this.tipo = t;
        this.id = id;

        panelCompras = new JPanel();

        getCarritoProducts(id);
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
        CarritoCompra carritoCompra = new CarritoCompra(0, 0);
        carritoCompra.setMain();
    }

    /*ACCESO A LAS VARIABLES LOCALES*/

    public JPanel getPanel(){
        return panelCarrito;
    }

    private void setMain(){
        CarritoCompra ventanaCarrito = new CarritoCompra(tipo, id);
        JFrame frame = new JFrame("Smart Trade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(ventanaCarrito.getPanel());
        frame.pack();
        frame.setVisible(true);
    }

    public void getCarritoProducts(int userId){
        HttpClient httpClient = HttpClient.newHttpClient();
        String url = "http://localhost:8080/cart/cartProducts?user_id=" + userId;
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
                System.out.println("Ha devuelto el carrito: " + responseBody);

                JPanel panelProductos = new JPanel();
                panelProductos.setLayout(new BoxLayout(panelProductos, BoxLayout.Y_AXIS));

                for (JsonNode productoNode : jsonResponse) {

                    String nombre = productoNode.get("name").asText();
                    String descripcion = productoNode.get("description").asText();
                    precio = productoNode.get("price").asDouble();
                    precio = productoNode.get("price").asDouble();
                    int amount = getAmount(nombre, id);

                    addProduct(nombre, descripcion, precio, amount, panelProductos);
                }

                panelCompras.removeAll();
                panelCompras.add(panelProductos);
                panelCompras.revalidate();
                panelCompras.repaint();

            } else {
                System.out.println("No devuelve nada");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public int getAmount(String nombreProd, int idUser){
        HttpClient httpClient = HttpClient.newHttpClient();
        String url = "http://localhost:8080/cart/amount";
        ObjectMapper objectMapper = new ObjectMapper();
        cantidad = 0;
        try {

            Map<String, Object> prod = new HashMap<>();
            prod.put("p_name", nombreProd);
            prod.put("u_id", idUser);
            String jsonBody = new ObjectMapper().writeValueAsString(prod);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            int statusCode = response.statusCode();
            System.out.println("Código es: " + statusCode);
            System.out.println("El body es: " + response.body());

            if(statusCode == 200){
                JsonNode jsonResponse = objectMapper.readTree(responseBody);
                try{
                    cantidad = Integer.parseInt(response.body());
                    System.out.println("La cantidad es: " + cantidad);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return cantidad;
    }


    private void addProduct(String name, String desc, double price, int amount, JPanel panel){
        JPanel panelProducto = new JPanel();
        panelProducto.setLayout(new BorderLayout());
        panelProducto.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panelProducto.setPreferredSize(new Dimension(770, 100));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel labelNombre = new JLabel("Nombre: " + name);
        JLabel labelDescripcion = new JLabel("Descripción: " + desc);
        JLabel labelPrecio = new JLabel("Precio: " + price * cantidad + "€");

        JButton buttonMas = new JButton("+");
        JLabel labelAmount = new JLabel(String.valueOf(amount));
        JButton buttonMenos = new JButton("-");

        buttonMenos.setBackground(new Color(153, 233, 255));
        buttonMas.setBackground(new Color(153, 233, 255));

        buttonMenos.setPreferredSize(new Dimension(45, 45));
        buttonMas.setPreferredSize(new Dimension(45, 45));

        panelProducto.add(labelNombre, BorderLayout.NORTH);
        panelProducto.add(labelDescripcion, BorderLayout.WEST);
        panelProducto.add(labelPrecio, BorderLayout.EAST);
        panelProducto.add(panelBotones, BorderLayout.CENTER);


        panelBotones.add(buttonMenos);
        panelBotones.add(labelAmount);
        panelBotones.add(buttonMas);

        buttonMenos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                nameProd = name;
                cantidad = Integer.parseInt(labelAmount.getText());
                cantidad--;
                if(cantidad <= 0){
                    int opcion = JOptionPane.showConfirmDialog(null, "¿Seguro que quieres eliminar el producto del carrito?", "Producto del carrito", JOptionPane.YES_NO_OPTION);

                    if(opcion == JOptionPane.YES_OPTION){
                        deleteProductFromCart(nameProd);
                    }else{
                        labelAmount.setText("1");
                    }
                }else{
                    labelAmount.setText(String.valueOf(cantidad));
                    double precio = price * cantidad;
                    labelPrecio.setText("Precio: " + String.valueOf(precio) + "€");
                    changeItemAmount(name, -1);
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

        buttonMas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int cantidad = Integer.parseInt(labelAmount.getText());
                cantidad++;
                labelAmount.setText(String.valueOf(cantidad));
                double precio = price * cantidad;
                labelPrecio.setText("Precio: " + String.valueOf(precio) + "€");
                changeItemAmount(name, 1);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                buttonMas.setBackground(new Color(73, 231, 255));
                buttonMas.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonMas.setBackground(new Color(153, 233, 255));
                buttonMas.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });


        panel.add(panelProducto);
    }
    public void backMenu(){
        CatalogoProductos ventanaCatalog = new CatalogoProductos(getUserData(), tipo, id);
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaCatalog.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }
    private void inicializarComponentes(){
        panelCarrito.setLayout(new BorderLayout());

        panelCarrito.add(panelInfo, BorderLayout.NORTH);
        panelInfo.setPreferredSize(new Dimension(800, 150));



        panelCompras.setLayout(new BoxLayout(panelCompras, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(panelCompras);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        panelCarrito.add(scrollPane, BorderLayout.CENTER);
        panelCarrito.setBackground(new Color(198, 232, 251));


    }

    private void changeItemAmount(String productName, int op){
        String url = "http://localhost:8080/cart/newAmount";
        HttpClient client = HttpClient.newHttpClient();

        try{
            Map<String, Object> data = new HashMap<>();
            data.put("n", op);
            data.put("p_name", productName);
            data.put("u_id", id);


            String jsonBody = new ObjectMapper().writeValueAsString(data);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 200){
                System.out.println(response.body());
            }else{

            }
        }catch(IOException | InterruptedException e){
            e.printStackTrace();
        }
    }

    private void deleteProductFromCart(String productName){

        String url = "http://localhost:8080/cart/delete";
        HttpClient client = HttpClient.newHttpClient();

        try{
            System.out.println("user id: " + id);
            System.out.println("product name: " + productName);
            Map<String, Object> data = new HashMap<>();
            data.put("p_name", productName);
            data.put("u_id", id);
            String jsonBody = new ObjectMapper().writeValueAsString(data);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            if(statusCode == 200){
                System.out.println("Elemento borrado del carrito correctamente");
            }else{
                System.out.println("Problema con la eliminación del producto, error:" + statusCode);
            }

            getCarritoProducts(id);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void addObserver(ObserverUserData observer) {

    }

    @Override
    public void removeObserver(ObserverUserData observer) {

    }

    @Override
    public void notifyObservers(String[] data) {

    }

    @Override
    public void update(String[] data) {
        name = data[0];
        email = data[1];
        password = data[2];
        type = data[3];
        iban = data[4];
        cif = data[5];
        dni = data[6];
        city = data[7];
        street = data[8];
        door = data[9];
        flat = data[10];
        num = data[11];
        id = Integer.parseInt(data[12]);
    }
}
