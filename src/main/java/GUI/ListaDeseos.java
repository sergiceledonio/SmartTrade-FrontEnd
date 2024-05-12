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

import static GUI.CatalogoProductos.getUserData;

public class ListaDeseos extends JFrame {

    private JPanel panelDeseos;
    private JPanel panelInfo;
    private JLabel logoButton;
    private JButton filtroButton;
    private JLabel perfilButton;
    private JLabel lupaButton;
    private JPanel panelProductos;
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

        panelDeseos = new JPanel();

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

                panelProductos.removeAll();
                panelProductos.add(panelProduct);
                panelProductos.revalidate();
                panelProductos.repaint();

            } else {
                System.out.println("No devuelve nada");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void addProduct(String name, String desc, double price, JPanel panel){
        JPanel panelProducto = new JPanel();
        panelProducto.setLayout(new BorderLayout());
        panelProducto.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panelProducto.setPreferredSize(new Dimension(770, 100));


        JLabel labelNombre = new JLabel("Nombre: " + name);
        JLabel labelDescripcion = new JLabel("Descripción: " + desc);
        JLabel labelPrecio = new JLabel("Precio: " + price + "€");


        panelProducto.add(labelNombre, BorderLayout.NORTH);
        panelProducto.add(labelDescripcion, BorderLayout.WEST);
        panelProducto.add(labelPrecio, BorderLayout.EAST);

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
        panelDeseos.setLayout(new BorderLayout());

        panelDeseos.add(panelInfo, BorderLayout.NORTH);
        panelInfo.setPreferredSize(new Dimension(800, 150));



        panelProductos.setLayout(new BoxLayout(panelProductos, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(panelProductos);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        panelDeseos.add(scrollPane, BorderLayout.CENTER);
        panelDeseos.setBackground(new Color(198, 232, 251));


    }

}
