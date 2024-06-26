package GUI;

import Observer.ObserverUserData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class InfoProducto extends JFrame implements ObserverUserData {
    private JPanel panelTitulo;
    private JPanel infoProduct;
    private JTextField searchTF;
    private JButton filtrosButton;
    private JPanel panelInfo;
    private JTextArea productDescription;
    private JButton buyButton;
    private JLabel logoButton;
    private JLabel searchButton;
    private JLabel perfilButton;
    private JLabel nameProduct;
    private JLabel categoryProduct;
    private JLabel priceProduct;
    private JLabel carritoCompraButton;
    private JLabel favouriteButton;
    private JComboBox comboboxFriends;
    private JButton addFriendList;
    private JLabel imageProduct;
    private JFrame frame;
    private String name;
    private String email;
    private String password;
    private String iban;
    private String cif;
    private String dni;
    private String city;
    private String street;
    private String door;
    private String flat;
    private String num;
    private String type;
    private int tipo;
    private String price;
    private String description;
    private String category;
    private int prodId;
    private int id;
    private byte[] img;
    private String nombre;
    private InicioSesion iniciosesion;
    public InfoProducto(String prodName, Double prodPrice, String prodType, String prodDescription, int id, int tipoUser, byte[] img, String nombre){

        panelInfo.setFocusable(true);
        panelInfo.requestFocusInWindow();
        panelInfo.setPreferredSize(new Dimension(800,600));
        goBackWithEsc(panelInfo);
        goBackWithEsc(searchTF);

        this.id = id;
        this.tipo = tipoUser;
        this.img = img;
        this.nombre = nombre;

        System.out.println("nombre: " + nombre);

        ImageIcon originalIcon = new ImageIcon(img);
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        imageProduct.setIcon(resizedIcon);
        imageProduct.setText("");

        initiateCB(id);


        nameProduct.setText(prodName);
        priceProduct.setText("El precio es de: " + prodPrice + "€");
        categoryProduct.setText("Categoria: " + prodType);
        productDescription.setText(prodDescription);
        prodId = getProductId(prodName);

        /*BUYBUTTON*/

        /*LOGOBUTTON*/

        logoButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                backMenu(id);
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

        /*FILTROSBUTTON*/
        filtrosButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                filtrosButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                filtrosButton.setBackground(new Color(73,231,255));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                filtrosButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                filtrosButton.setBackground(new Color(153,233,255));
            }
        });

        /*SEARCHBUTTON*/
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //Aplicar filtros y hacer búsqueda
                super.mouseClicked(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                searchButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

        /*PERFILBUTTON*/
        perfilButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }

            @Override
            public void mouseEntered(MouseEvent e){
                perfilButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e){
                perfilButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        buyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addToCart(id, prodId, 1);
                backMenu(tipo);
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
        favouriteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                addToFav(id, prodId);
                backMenu(tipo);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                favouriteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                favouriteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
        addFriendList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addFriendProduct();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                addFriendList.setBackground(new Color(73,231,255));
                addFriendList.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                addFriendList.setBackground(new Color(153,233,255));
                addFriendList.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }
    public static void main(String[] args) {
        InfoProducto ventanaInfo = new InfoProducto("",(double) 0,"","", 1, 1, new byte[]{}, "");
        JFrame frame = new JFrame("Smart Trade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(ventanaInfo.panelInfo);
        frame.pack();
        frame.setVisible(true);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
    }

    /*METODOS PARA ACCEDER A VARIABLES*/

    public JPanel getPanel(){
        return this.panelInfo;
    }

    private void addFriendProduct(){
        String selectedFriend = (String) comboboxFriends.getSelectedItem();

        if ("Nuevo amigo".equals(selectedFriend)) {

            String nuevoAmigoNombre = JOptionPane.showInputDialog(null, "Introduce el nombre del nuevo amigo:");
            newGift(id, nuevoAmigoNombre, prodId);

        }else{
            newGift(id, selectedFriend, prodId);
        }
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

    public void addToFav(int userId, int prodId){

        String url = "http://localhost:8080/wish/newWishProduct";
        HttpClient client = HttpClient.newHttpClient();

        try{
            Map<String, Integer> data = new HashMap<>();
            data.put("user_id", userId);
            data.put("p_id", prodId);
            String jsonBody = new ObjectMapper().writeValueAsString(data);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Código de lista de deseados " + response.statusCode());
            System.out.println(response.body());
            if(response.statusCode() == 200){
                if(Integer.parseInt(response.body()) == 1){
                    System.out.println("Se añade el producto sin problemas");
                    JOptionPane.showMessageDialog(null,"El producto se ha añadido a la lista de deseados", "Producto añadido a lista", JOptionPane.INFORMATION_MESSAGE );
                }else{
                    System.out.println("El producto ya está añadido a la lista de deseados");
                    JOptionPane.showMessageDialog(null,"El producto estaba ya añadido a la lista de deseados", "Producto ya añadido anteriormente", JOptionPane.INFORMATION_MESSAGE );
                }
            }else{
                System.out.println("ERROR EN LA PETICIÓN");
            }
        }catch(IOException | InterruptedException e){
            e.printStackTrace();
        }

    }

    public void backMenu(int tipo){
        CatalogoProductos ventanaCatalog = new CatalogoProductos(tipo, id, nombre);
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaCatalog.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }

    private void newGift(int userId, String friend, int prod) {
        String url = "http://localhost:8080/gift/newGiftProduct";
        HttpClient client = HttpClient.newHttpClient();
        String friendName = "";
        HashSet<String> nombres = new HashSet<>();

        try {
            Map<String, Object> body = new HashMap<>();
            body.put("user_id", userId);
            body.put("p_id", prod);
            body.put("friend", friend);

            ObjectMapper mapper = new ObjectMapper();
            String jsonBody = mapper.writeValueAsString(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            String responseBody = response.body();

            System.out.println("StatusCode: " + statusCode);

            if (statusCode == 200) {
                ObjectMapper responseMapper = new ObjectMapper();
                JsonNode jsonNode = responseMapper.readTree(responseBody);

                if(Integer.parseInt(responseBody) == 1){
                    JOptionPane.showMessageDialog(null, "El producto se ha añadido a la lista de " + friend);
                }else if(Integer.parseInt(responseBody) == 0){
                    JOptionPane.showMessageDialog(null, "Producto añadido a tu nuevo amigo " + friend);
                }else{
                    JOptionPane.showMessageDialog(null, "El producto se ha añadido a la lista de " + friend);
                }

                if (jsonNode.isArray()) {
                    for (JsonNode friendNode : jsonNode) {
                        friendName = friendNode.get("name").asText();
                        nombres.add(friendName);
                    }
                    for(String amigo : nombres){
                        comboboxFriends.addItem(amigo);
                    }
                }
                initiateCB(userId);
                backMenu(tipo);
            } else {
                System.out.println("Problem with client: " + statusCode);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error al enviar la petición: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void initiateCB(int identificador){

        comboboxFriends.addItem("Nuevo amigo");

        HttpClient httpClient = HttpClient.newHttpClient();
        String url = "http://localhost:8080/gift/friends";
        HashSet<String> amigos = new HashSet<>();

        try {
            Map<String, Integer> requestBody = new HashMap<>();
            System.out.println(identificador);
            requestBody.put("user_id", identificador);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonRequestBody = objectMapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Código de estado del CB: " + response.statusCode());
            System.out.println("Cuerpo de la respuesta del CB: " + response.body());

            if (response.statusCode() == 200) {
                JsonNode jsonResponse = objectMapper.readTree(response.body());
                for(JsonNode friend : jsonResponse) {
                    amigos.add(friend.asText());
                }
                for(String nombre : amigos){
                    comboboxFriends.addItem(nombre);
                }
            } else {
                System.out.println("Error en la solicitud: " + response.statusCode());
            }

        } catch (Exception e) {
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
    }
    public String[] getUserData(){
        return new String[]{name, email, password, type, iban, cif, dni, city, street, door, flat, num};
    }
    public int getProductId(String pName){

        int productId = 0;
        try {

            String encodedProductName = URLEncoder.encode(pName , StandardCharsets.UTF_8.toString());
            String url = "http://localhost:8080/product/getbyname/" + encodedProductName;

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

    public void goBackWithEsc(JComponent component){
        component.addKeyListener(
                new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                            backMenu(tipo);
                        }
                    }
                }
        );
    }



}
