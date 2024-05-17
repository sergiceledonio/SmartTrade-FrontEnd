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

import static GUI.CatalogoProductos.getUserData;

public class ListaRegalos extends JFrame implements ObserverUserData {
    private JPanel panelInfo;
    private JLabel backButton;
    private JLabel perfilButton;
    private JPanel panelRegalos;
    private JPanel panelListaRegalos;
    private static String name;
    private static String password;
    private static String email;
    private static String type;
    private static String iban;
    private static String cif;
    private static String dni;
    private static String city;
    private static String street;
    private static String door;
    private static String flat;
    private static String num;
    private int tipo;
    private int id;
    private InicioSesion iniciosesion;

    public ListaRegalos(int t, int i){
        this.tipo = t;
        this.id = i;

        iniciosesion = new InicioSesion();
        iniciosesion.addObserver(this);


        panelRegalos = new JPanel();
        panelRegalos.setPreferredSize(new Dimension(800, 500));
        panelRegalos.setBackground(new Color(198, 232, 251));

        getGifts(id);
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

    public static void main(String[] args) {
        ListaRegalos ventanaRegalos = new ListaRegalos(0, 0);
        ventanaRegalos.setMain();
    }

    /*ACCESO A LAS VARIABLES LOCALES*/

    public JPanel getPanel(){
        return this.panelListaRegalos;
    }

    private void setMain(){
        ListaRegalos ventanaRegalos = new ListaRegalos(tipo, id);
        JFrame frame = new JFrame("Smart Trade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(ventanaRegalos.getPanel());
        frame.pack();
        frame.setVisible(true);
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

    public void getGifts(int userId){
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
                System.out.println("Ha devuelto la lista de regalos: " + responseBody);

                JPanel panelProductos = new JPanel();
                panelProductos.setLayout(new BoxLayout(panelProductos, BoxLayout.Y_AXIS));

                for (JsonNode productoNode : jsonResponse) {
                    String user = productoNode.get("user_name").asText();
                    String nombre = productoNode.get("name").asText();
                    String descripcion = productoNode.get("description").asText();
                    Double precio  = productoNode.get("price").asDouble();
                }

                panelRegalos.removeAll();
                panelRegalos.add(panelProductos);
                panelRegalos.revalidate();
                panelRegalos.repaint();

            } else {
                System.out.println("No devuelve nada");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void inicializarComponentes(){
        panelListaRegalos.setLayout(new BorderLayout());

        panelListaRegalos.add(panelInfo, BorderLayout.NORTH);
        panelInfo.setPreferredSize(new Dimension(800, 150));



        panelRegalos.setLayout(new BoxLayout(panelRegalos, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(panelRegalos);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        panelListaRegalos.add(scrollPane, BorderLayout.CENTER);
        panelListaRegalos.setBackground(new Color(198, 232, 251));

        JButton newUser = new JButton("Añadir persona");
        newUser.setBackground(new Color(153, 233, 255));

        newUser.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //Añadir nueva lista de productos para un usuario
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                newUser.setCursor(new Cursor(Cursor.HAND_CURSOR));
                newUser.setBackground(new Color(73, 231, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                newUser.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                newUser.setBackground(new Color(153, 233, 255));
            }
        });

        panelListaRegalos.add(newUser, BorderLayout.SOUTH);



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
