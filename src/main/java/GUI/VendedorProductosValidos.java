package GUI;

import Observer.ObserverUserData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import static GUI.CatalogoProductos.getUserData;

public class VendedorProductosValidos extends JFrame implements ObserverUserData {
    private JPanel panelInfo;
    private JLabel backButton;
    private JLabel perfilButton;
    private JPanel panelValidos;
    private int user_id;
    private JPanel panelVendedor;
    private int tipo;
    private InicioSesion iniciosesion;
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
    private static int id;


    public VendedorProductosValidos(int id, int t){
        iniciosesion = new InicioSesion();
        iniciosesion.addObserver(this);

        this.user_id = id;
        this.tipo = t;
        panelVendedor = new JPanel();
        panelVendedor.setPreferredSize(new Dimension(800, 600));
        panelValidos = new JPanel();

        getProductsById(user_id);
        inicializarComponentes();
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
    }

    public JPanel getPanel(){
        return this.panelVendedor;
    }
    public void getProductsById(int id){
        HttpClient httpClient = HttpClient.newHttpClient();
        String url = "http://localhost:8080/product/validatedByUser";
        ObjectMapper objectMapper = new ObjectMapper();

        try{
            Map<String, Integer> body = new HashMap<>();
            body.put("user_id", id);
            String requestBody = objectMapper.writeValueAsString(body);


            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/json")
                    .method("GET", HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            int statusCode = response.statusCode();

            System.out.println("Código es: " + statusCode);
            if(statusCode == 200){
                JsonNode jsonResponse = objectMapper.readTree(responseBody);
                System.out.println("Ha devuelto los productos validados del usuario con id: " + user_id);

                JPanel panelProductos = new JPanel();
                panelProductos.setLayout(new BoxLayout(panelProductos, BoxLayout.Y_AXIS));

                for (JsonNode productoNode : jsonResponse) {

                    String nombre = productoNode.get("name").asText();
                    String descripcion = productoNode.get("description").asText();
                    Double precio = productoNode.get("price").asDouble();

                    System.out.println("Nombre: " + nombre + " desc: " + descripcion + " precio: " + precio);
                    addProduct(nombre, descripcion, precio, panelProductos);
                }

                panelValidos.removeAll();
                panelValidos.add(panelProductos);
                panelValidos.revalidate();
                panelValidos.repaint();

            } else {
                System.out.println("No devuelve nada");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void inicializarComponentes(){
        panelValidos.setLayout(new BorderLayout());

        panelVendedor.add(panelInfo, BorderLayout.NORTH);
        panelInfo.setPreferredSize(new Dimension(800, 150));

        panelValidos.setLayout(new BoxLayout(panelValidos, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(panelValidos);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        panelVendedor.add(scrollPane, BorderLayout.CENTER);
        panelVendedor.setBackground(new Color(198, 232, 251));
    }

    private void addProduct(String nombre, String desc, Double precio, JPanel panel){
        JPanel panelProducto = new JPanel();
        panelProducto.setLayout(new BorderLayout());
        panelProducto.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panelProducto.setPreferredSize(new Dimension(770, 100));


        JLabel labelNombre = new JLabel("Nombre: " + nombre);
        JLabel labelDescripcion = new JLabel("Descripción: " + desc);
        JLabel labelPrecio = new JLabel("Precio: " + precio + "€");


        panelProducto.add(labelNombre, BorderLayout.NORTH);
        panelProducto.add(labelDescripcion, BorderLayout.WEST);
        panelProducto.add(labelPrecio, BorderLayout.EAST);

        panel.add(panelProducto);
    }
    public void backMenu(){
        CatalogoProductos ventanaCatalog = new CatalogoProductos(getUserData(), tipo, user_id);
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaCatalog.getPanel());
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
}
