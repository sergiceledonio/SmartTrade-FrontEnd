package GUI;

import Observer.ObserverUserData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CatalogoProductos extends JFrame implements ObserverUserData {
    private JPanel panelCatalogo;
    private JPanel panelInfo;
    private JPanel panelListado;
    private JLabel logoButton;
    private JButton filtroButton;
    private JLabel perfilButton;
    private JTextField searchTF;
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
    private static String prodType;
    private static double prodPrice;
    private static String prodDescription;
    private static String prodName;
    private static String type;
    private JPanel panelProductos;
    private final InicioSesion iniciosesion;
    private int tipo;
    private String[] userData;

    public CatalogoProductos(String[] userData, int tipo) {
        iniciosesion = new InicioSesion();
        iniciosesion.addObserver(this);

        this.tipo = tipo;

        System.out.println("********************************************************************");
        System.out.println("Catalogo de productos");
        System.out.println("El tipo de usuario es: ");
        System.out.println(tipo);

        inicializarComponentes();
        organizarInterfaz(tipo);
        getProducts();



        ventaProducto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sellProduct();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                ventaProducto.setCursor(new Cursor(Cursor.HAND_CURSOR));
                ventaProducto.setBackground(new Color(73, 231, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ventaProducto.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                ventaProducto.setBackground(new Color(153, 233, 255));
            }
        });
        logoButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                backMenu(tipo);
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
        lupaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                lupaButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lupaButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        filtroButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                filtroButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                filtroButton.setBackground(new Color(73, 231, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                filtroButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                filtroButton.setBackground(new Color(153, 233, 255));
            }
        });
        carritoCompraButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //ir al carrito
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
        perfilButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //ir al perfil
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                perfilButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                perfilButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    public static void main(String[] args) {
        CatalogoProductos catalogoProductos = new CatalogoProductos(new String[]{}, 0);
        catalogoProductos.setMain();
    }

    public void setMain() {
        CatalogoProductos ventanaCatalogo = new CatalogoProductos(getUserData(), tipo);
        JFrame frame = new JFrame("Smart Trade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(ventanaCatalogo.panelCatalogo);
        frame.pack();
        frame.setVisible(true);
    }

    public JPanel getPanel() {
        return this.panelCatalogo;
    }

    public void backMenu(int param) {
        CatalogoProductos ventanaCatalog = new CatalogoProductos(getUserData(), param);
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaCatalog.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }

    public void sellProduct() {
        VentaProducto ventanaVenta = new VentaProducto(getUserData(), tipo);
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaVenta.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }

    public void infoProduct(String nombre, Double price, String descripcion,  String category) {

        System.out.println("*************************************");
        System.out.println("Nombre: " + nombre);
        System.out.println("Precio: " + price);
        System.out.println("Categoria: " + category);
        System.out.println("Descripción: " + descripcion);

        InfoProducto ventanaInfo = new InfoProducto(nombre, price, category, descripcion);
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaInfo.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }

    private void getProducts() {
        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/product/validated"))
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            if (response.statusCode() == 200) {
                JsonNode jsonResponse = objectMapper.readTree(responseBody);
                if (jsonResponse.isArray()) {
                    for (JsonNode productNode : jsonResponse) {
                        prodName = productNode.get("name").asText();
                        prodPrice = productNode.get("price").asDouble();
                        prodDescription = productNode.get("description").asText();
                        prodType = productNode.get("type").asText();

                        System.out.println("Producto: " + prodName + " Precio: " + prodPrice + " Descripción: " + prodDescription + " Tipo: " + prodType);

                        agregarProducto(prodName, prodPrice, prodDescription, prodType);
                    }
                }
            } else {
                System.out.println("Error: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void agregarProducto(String nombre, Double precio, String descripcion, String type) {
        JPanel panelProducto = new JPanel(new BorderLayout());
        panelProducto.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel labelNombre = new JLabel(nombre);
        JLabel labelPrecio = new JLabel(precio + "€");
        JLabel labelDescripcion = new JLabel("<html>" + descripcion + "</html>");
        panelProducto.setMinimumSize(new Dimension(300,200));
        panelProducto.setMaximumSize(new Dimension(300,200));
        panelProducto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                infoProduct(nombre, precio, descripcion, type);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                panelProducto.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panelProducto.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        labelNombre.setHorizontalAlignment(JLabel.CENTER);
        labelDescripcion.setHorizontalAlignment(JLabel.CENTER);
        labelPrecio.setHorizontalAlignment(JLabel.CENTER);
        labelNombre.setPreferredSize(new Dimension(200, 30));
        labelPrecio.setPreferredSize(new Dimension(200, 30));
        labelDescripcion.setPreferredSize(new Dimension(200, 30));
        panelProducto.add(labelNombre, BorderLayout.NORTH);
        panelProducto.add(labelPrecio, BorderLayout.CENTER);
        panelProducto.add(labelDescripcion, BorderLayout.SOUTH);
        panelProductos.add(panelProducto);
        panelProductos.revalidate();
        panelProductos.repaint();
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

    public static String[] getUserData() {
        return new String[]{name, email, password, type, iban, cif, dni, city, street, door, flat, num};
    }

    private void inicializarComponentes() {
        panelCatalogo = new JPanel();
        panelInfo = new JPanel();
        panelListado = new JPanel();
        panelProductos = new JPanel();
        panelProductos.setLayout(new GridLayout(0, 3, 10, 10));
        ventaProducto = new JButton("Vender Producto");
        ventaProducto.setBackground(new Color(153, 233, 255));
        ventaProducto.setPreferredSize(new Dimension(150, 30));
        JPanel panelVenta = new JPanel();
        panelVenta.setLayout(new FlowLayout(FlowLayout.CENTER));
    }

    private void organizarInterfaz(int param) {

        searchTF.setPreferredSize(new Dimension(120,20));

        panelCatalogo.setLayout(new BorderLayout());

        panelInfo.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.1;
        panelInfo.add(logoButton, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.9;
        panelInfo.add(searchTF, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.1;
        panelInfo.add(lupaButton, gbc);

        gbc.gridx = 3;
        gbc.weightx = 0.1;
        panelInfo.add(filtroButton, gbc);

        gbc.gridx = 4;
        gbc.weightx = 0.1;
        panelInfo.add(carritoCompraButton, gbc);

        gbc.gridx = 5;
        gbc.weightx = 0.1;
        panelInfo.add(perfilButton, gbc);

        panelInfo.setPreferredSize(new Dimension(800, 120));
        panelInfo.setBackground(new Color(183, 183, 183));

        panelListado.setLayout(new BorderLayout());
        panelListado.setBackground(new Color(198, 232, 251));
        panelListado.setPreferredSize(new Dimension(800, 480));

        panelProductos.setLayout(new GridLayout(0, 3, 10, 10));
        panelProductos.setBackground(new Color(198, 232, 251));
        JScrollPane scrollPane = new JScrollPane(panelProductos);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panelListado.add(scrollPane, BorderLayout.CENTER);

        panelCatalogo.add(panelInfo, BorderLayout.NORTH);
        panelCatalogo.add(panelListado, BorderLayout.CENTER);

        if(param == 2){
            panelCatalogo.add(ventaProducto, BorderLayout.SOUTH);
        }

    }
}
