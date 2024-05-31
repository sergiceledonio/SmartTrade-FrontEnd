package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import Observer.ObserverUserData;
import ObserverPattern.Observador;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CarritoCompra extends JFrame implements ObserverUserData{
    private JPanel panelCarrito;
    private JPanel panelInfo;
    private JLabel backButton;
    private JButton filtroButton;
    private JLabel perfilButton;
    private JLabel lupaButton;
    private JPanel panelCompras;
    private JButton pagoButton;
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
    private byte[] img;
    private List<Observador> observadoresPrecio = new ArrayList<>();
    private String nombre;

    private int precioFinal;

    public CarritoCompra(int t, int id, String n) {


        iniciosesion = new InicioSesion();
        iniciosesion.addObserver(this);
        panelCarrito.setPreferredSize(new Dimension(800, 600));

        this.tipo = t;
        this.id = id;
        this.nombre = n;

        panelCompras = new JPanel();
        panelCompras.setBackground(new Color(198, 232, 251));

        getCarritoProducts(id);
        inicializarComponentes();

        panelCarrito.setFocusable(true);
        panelCarrito.requestFocusInWindow();
        goBackWithEsc(panelCarrito);

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
        CarritoCompra carritoCompra = new CarritoCompra(0, 0, "");
        carritoCompra.setMain();
    }

    /*ACCESO A LAS VARIABLES LOCALES*/

    public JPanel getPanel(){
        return panelCarrito;
    }

    private void setMain(){
        CarritoCompra ventanaCarrito = new CarritoCompra(tipo, id, nombre);
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
                    img = productoNode.get("image").binaryValue();
                    int amount = getAmount(nombre, id);

                    addProduct(nombre, descripcion, precio, amount, panelProductos, img);
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
            int statusCode = response.statusCode();
            System.out.println("Código es: " + statusCode);
            System.out.println("El body es: " + response.body());

            if(statusCode == 200){
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


    private void addProduct(String name, String desc, double price, int amount, JPanel panel, byte[] img){
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
        JLabel labelPrecio = new JLabel("Precio: " + price * cantidad + "€");

        ImageIcon originalIcon = new ImageIcon(img);
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        JLabel labelImg = new JLabel(resizedIcon);

        JButton buttonMas = new JButton("+");
        JLabel labelAmount = new JLabel(String.valueOf(amount));
        JButton buttonMenos = new JButton("-");
        JButton buttonEliminar = new JButton("Eliminar");

        buttonMenos.setBackground(new Color(153, 233, 255));
        buttonMas.setBackground(new Color(153, 233, 255));
        buttonEliminar.setBackground(new Color(153, 233, 255));

        buttonMenos.setPreferredSize(new Dimension(45, 35));
        buttonMas.setPreferredSize(new Dimension(45, 35));
        buttonEliminar.setPreferredSize(new Dimension(100, 35));

        panelPrincipal.add(labelImg, BorderLayout.NORTH);
        panelPrincipal.add(labelNombre, BorderLayout.CENTER);

        panelDes.add(labelDescripcion, BorderLayout.CENTER);
        panelDes.add(panelBotones, BorderLayout.SOUTH);

        panelProducto.add(panelPrincipal, BorderLayout.WEST);
        panelProducto.add(panelDes, BorderLayout.CENTER);
        panelProducto.add(labelPrecio, BorderLayout.EAST);

        panelBotones.add(buttonEliminar);
        panelBotones.add(buttonMenos);
        panelBotones.add(labelAmount);
        panelBotones.add(buttonMas);

        buttonEliminar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                nameProd = name;

                int opcion = JOptionPane.showConfirmDialog(null, "¿Seguro que quieres eliminar el producto del carrito?", "Producto del carrito", JOptionPane.YES_NO_OPTION);
                if(opcion == JOptionPane.YES_OPTION){
                    deleteProductFromCart(nameProd);
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                buttonEliminar.setBackground(new Color(73, 231, 255));
                buttonEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonEliminar.setBackground(new Color(153, 233, 255));
                buttonEliminar.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

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
                    labelPrecio.setText("Precio: " + cantidad * price + "€");
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
        goBackWithEsc(buttonMas);

        buttonMas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int cantidad = Integer.parseInt(labelAmount.getText());
                cantidad++;
                labelAmount.setText(String.valueOf(cantidad));
                labelPrecio.setText("Precio: " + cantidad * price + "€");
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
        goBackWithEsc(buttonMenos);

        panel.add(panelProducto);
        precioFinal += price * amount;
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
        panelCarrito.setLayout(new BorderLayout());

        panelCarrito.add(panelInfo, BorderLayout.NORTH);
        panelInfo.setPreferredSize(new Dimension(800, 150));

        panelCompras.setLayout(new BoxLayout(panelCompras, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(panelCompras);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        panelCarrito.add(scrollPane, BorderLayout.CENTER);
        panelCarrito.setBackground(new Color(198, 232, 251));
        JButton pagoButton = new JButton("Pagar");
        panelCarrito.add(pagoButton, BorderLayout.SOUTH);
        pagoButton.setBackground(new Color(153, 233, 255));

        pagoButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                goToMakeOrder();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                pagoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                pagoButton.setBackground(new Color(73, 231, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                pagoButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                pagoButton.setBackground(new Color(153, 233, 255));
            }
        });

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

    private void goToMakeOrder(){
        RealizarPedido ventanaRealizarPedido = new RealizarPedido(tipo, id, precioFinal, nombre);
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaRealizarPedido.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
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
}
