package GUI;

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
    private int tipo;
    private int id;



    public ListaDeseos(int t, int id) {
        iniciosesion = new InicioSesion();
        panelDeseos.setPreferredSize(new Dimension(800, 600));
        this.tipo = t;
        this.id = id;

        panelListDeseos = new JPanel();

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
        ListaDeseos listaDeseos = new ListaDeseos(0, 0);
        listaDeseos.setMain();
    }

    /*ACCESO A LAS VARIABLES LOCALES*/

    public JPanel getPanel(){
        return panelDeseos;
    }

    private void setMain(){
        ListaDeseos ventanaDeseo = new ListaDeseos(tipo, id);
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

                    addProduct(nombre, descripcion, precio, panelProduct);
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

    private void addProduct(String name, String desc, double price, JPanel panel){
        JPanel panelProduco = new JPanel();
        panelProduco.setLayout(new BorderLayout());
        panelProduco.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panelProduco.setPreferredSize(new Dimension(770, 100));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton buttonMenos = new JButton("Eliminar");
        buttonMenos.setBackground(new Color(153, 233, 255));
        buttonMenos.setPreferredSize(new Dimension(105, 45));

        JLabel labelNombre = new JLabel("Nombre: " + name);
        JLabel labelDescripcion = new JLabel("Descripción: " + desc);
        JLabel labelPrecio = new JLabel("Precio: " + price + "€");


        panelProduco.add(labelNombre, BorderLayout.NORTH);
        panelProduco.add(labelDescripcion, BorderLayout.WEST);
        panelProduco.add(labelPrecio, BorderLayout.EAST);
        panelProduco.add(panelBotones, BorderLayout.CENTER);


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

        panel.add(panelProduco);
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
        CatalogoProductos ventanaCatalog = new CatalogoProductos(getUserData(), tipo, id);
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

}
