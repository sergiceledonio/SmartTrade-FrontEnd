package GUI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLSyntaxErrorException;
import java.util.*;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import Observer.ObserverUserData;

public class CatalogoProductos implements ObserverUserData {
    private JPanel panelCatalogo;
    private JPanel panelInfo;
    private JPanel panelListado;
    private JLabel logoButton;
    private JButton filtroButton;
    private JLabel perfilButton;
    private JTextField searchTF;
    private JTable tableCatalog;
    private JButton ventaProducto;
    private JLabel lupaButton;
    private JLabel carritoCompraButton;
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

    public CatalogoProductos(String[] userData){

        iniciosesion = new InicioSesion();
        iniciosesion.addObserver(this);
        panelCatalogo.setPreferredSize(new Dimension(800, 600));


        if(dni != null){
            ventaProducto.setVisible(false);
        }else{
            ventaProducto.setVisible(true);
        }

        /*TABLECATALOG*/

        DefaultTableModel model = new DefaultTableModel();
        String[] columnas = {"", "", "", ""};
        for(int i = 0; i < columnas.length; i++){
            model.addColumn(columnas[i]);
        }
        Object[] productComments = getProducts(); //Acceder a la BD y recoger los elementos guardados



        tableCatalog.setEnabled(true);
        tableCatalog.setRowHeight(63);
        tableCatalog.setMaximumSize(new Dimension(200, 120));
        tableCatalog.setPreferredScrollableViewportSize(new Dimension(200, 120));
        tableCatalog.setModel(model);

        tableCatalog.setDefaultEditor(Object.class, null);
        tableCatalog.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableCatalog.rowAtPoint(e.getPoint());
                int col = tableCatalog.columnAtPoint(e.getPoint());
                if (row >= 0 && col >= 0) {
                    Object cellValue = tableCatalog.getValueAt(row, col);
                    if (cellValue != null) {
                        String nombreCelda = cellValue.toString();
                        int price = nombreCelda.charAt(1);
                        String category = "Juego";
                        String descripcion = "Esta es la descripción para el elemento selecionado";
                        infoProduct(nombreCelda, price, category, descripcion);
                    }
                }
            }
        });


        /*LUPABUTTON*/
        lupaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                lupaButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent e) {
                lupaButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        /*PERFILBUTTON*/
        perfilButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                perfilButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent e) {
                perfilButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        /*FILTROSBUTTON*/
        filtroButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                filtroButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                filtroButton.setBackground(new Color(73,231,255));
            }
            public void mouseExited(MouseEvent e) {
                filtroButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                filtroButton.setBackground(new Color(153,233,255));
            }
        });

        /*LOGOBUTTON*/
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

        /*VENTAPRODUCTO*/
        ventaProducto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sellProduct();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                ventaProducto.setCursor(new Cursor(Cursor.HAND_CURSOR));
                ventaProducto.setBackground(new Color(73,233,255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ventaProducto.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                ventaProducto.setBackground(new Color(153,233,255));
            }
        });

        carritoCompraButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CarritoCompra ventanaCarrito = new CarritoCompra();
                JFrame frame = new JFrame("Smart Trade");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setContentPane(ventanaCarrito.getPanel());
                frame.pack();
                frame.setVisible(true);

            }
            @Override
            public void mouseEntered(MouseEvent e) {
                carritoCompraButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                carritoCompraButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    public static void main(String[] args) {
        CatalogoProductos catalogoProductos = new CatalogoProductos(new String[]{});
        catalogoProductos.setMain();
    }

    /*METODOS PARA ACCEDER A VARIABLES*/

    public JPanel getPanel(){
        return this.panelCatalogo;
    }

    public void backMenu(){
        CatalogoProductos ventanaCatalog = new CatalogoProductos(getUserData());
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaCatalog.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }

    public void setMain(){
        CatalogoProductos ventanaCatalogo = new CatalogoProductos(getUserData());
        JFrame frame = new JFrame("Smart Trade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(ventanaCatalogo.panelCatalogo);
        frame.pack();
        frame.setVisible(true);
    }

    public void sellProduct(){
        VentaProducto ventanaVenta = new VentaProducto(getUserData());
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaVenta.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }

    public void infoProduct(String nombreCelda,int price,String category,String descripcion){
        InfoProducto ventanaInfo = new InfoProducto(getUserData());
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaInfo.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }

    public Object[] getProducts() {
        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/product/products"))
                .GET()
                .build();
        Object[] res = null;
        try{

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            if(response.statusCode() == 200){
                System.out.println("Productos recogidos");
                JsonNode jsonResponse = objectMapper.readTree(responseBody);
                res = objectMapper.convertValue(jsonResponse, Object[].class);
                for(int i = 0; i < res.length; i++){
                    System.out.println(res[i]);
                }
            }else{
                System.out.println("Objetos no recogidos");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }

    private Object[] parseResponse(String response){
        Object[] products = null;
        try{
            ObjectMapper objectMapper = new ObjectMapper();
             products = objectMapper.readValue(response, Object[].class);

        }catch(Exception e){
            e.printStackTrace();
        }
        return products;
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
    }

    public static String[] getUserData(){
            return new String[]{name, email, password, type, iban, cif, dni, city, street, door, flat, num};
    }
}


