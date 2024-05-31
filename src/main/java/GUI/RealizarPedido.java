package GUI;

import Observer.ObserverUserData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static GUI.CatalogoProductos.getUserData;

public class RealizarPedido extends JFrame implements ObserverUserData {
    private JPanel panelPedido;
    private JPanel panelInfo;
    private JLabel backButton;
    private JLabel perfilButton;
    private JPanel panelFondo;
    private int tipo;
    private int id;
    private InicioSesion inicioSesion;
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
    private String[] addressNames = new String[2];
    private int orderNumber = 1;
    private double precio;
    private String nombre;

    public RealizarPedido(int t, int id, double precio, String nombre) {
        System.out.println("El nombre en realizar pedido es: " + nombre);
        inicioSesion = new InicioSesion();
        inicioSesion.addObserver(this);
        panelPedido.setPreferredSize(new Dimension(800, 600));
        this.tipo = t;
        this.id = id;
        this.precio = precio;
        this.nombre = nombre;

        inicializarComponentes();
        goBackWithEsc(panelPedido);
    }

    public static void main(String[] args) {
        RealizarPedido realizarPedido = new RealizarPedido(0, 0, 0, "");
        realizarPedido.setMain();
    }

    private void setMain(){
        RealizarPedido ventanaPedido = new RealizarPedido(tipo, id, precio, nombre);
        JFrame frame = new JFrame("Smart Trade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(ventanaPedido.getPanel());
        frame.pack();
        frame.setVisible(true);
    }

    public JPanel getPanel(){
        return panelPedido;
    }

    private void inicializarComponentes(){
        panelPedido.setLayout(new BorderLayout());

        panelPedido.add(panelInfo, BorderLayout.NORTH);
        panelInfo.setPreferredSize(new Dimension(800, 100));

        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(panelFondo);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        panelPedido.add(scrollPane, BorderLayout.CENTER);
        panelPedido.setBackground(new Color(198, 232, 251));

        JButton pagoButton = new JButton("Elegir método de pago");
        panelFondo.add(pagoButton);
        panelFondo.setLayout(null);
        pagoButton.setBackground(new Color(198, 232, 251));
        pagoButton.setBounds(450, 400, 200, 30);

        JButton seguirComprandoButton = new JButton("Seguir comprando");
        panelFondo.add(seguirComprandoButton);
        seguirComprandoButton.setBackground(new Color(153, 233, 255));
        seguirComprandoButton.setBounds(150, 400, 200, 30);

        JLabel orderLabel = new JLabel();
        panelFondo.add(orderLabel);
        orderLabel.setText("Pedido nº" + orderNumber);
        orderLabel.setFont(orderLabel.getFont().deriveFont(Font.BOLD, 28.0f));
        orderLabel.setBounds(50, 50, 250, 30);

        JLabel addressLabel = new JLabel();
        panelFondo.add(addressLabel);
        addressLabel.setText("Editar direcciones");
        addressLabel.setFont(addressLabel.getFont().deriveFont(Font.BOLD, 20.0f));
        addressLabel.setBounds(50, 155, 250, 30);

        JTextArea addressText = new JTextArea();
        panelFondo.add(addressText);
        addressText.setBounds(50, 200, 250, 150);
        goBackWithEsc(addressText);

        JButton addAddressButton = new JButton("Añadir");
        panelFondo.add(addAddressButton);
        addAddressButton.setBackground(new Color(153, 233, 255));
        addAddressButton.setBounds(355, 200, 90, 30);
        goBackWithEsc(addAddressButton);

        JButton deleteAddressButton = new JButton("Eliminar");
        panelFondo.add(deleteAddressButton);
        deleteAddressButton.setBackground(new Color(153, 233, 255));
        deleteAddressButton.setBounds(355, 250, 90, 30);
        goBackWithEsc(deleteAddressButton);

        JToggleButton addressButton1 = new JToggleButton();
        JToggleButton addressButton2 = new JToggleButton();
        panelFondo.add(addressButton1);
        panelFondo.add(addressButton2);
        addressButton1.setVisible(false);
        addressButton2.setVisible(false);
        addressButton1.setBackground(new Color(153, 233, 255));
        addressButton1.setBounds(500, 200, 210, 30);
        addressButton2.setBackground(new Color(153, 233, 255));
        addressButton2.setBounds(500, 235, 210, 30);
        goBackWithEsc(addressButton1);
        goBackWithEsc(addressButton2);

        addAddressButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addressNames = addressText.getText().split("\n");
                if (addressNames.length >= 1 && addressNames.length <= 2) {
                    for (int i = 0; i < addressNames.length; i++) {
                        if (!(addressNames[i].equals(""))) {
                            if (addressButton1.isVisible() == false && addressButton2.isVisible() == false) {
                                addressButton1.setVisible(true);
                                addressButton1.setText(addressNames[i]);
                            } else if (addressButton1.isVisible() && addressButton2.isVisible() == false) {
                                addressButton2.setVisible(true);
                                addressButton2.setText(addressNames[i]);
                            } else if (addressButton1.isVisible() && addressButton2.isVisible()) {
                                JOptionPane.showMessageDialog(
                                        panelFondo,
                                        "No puede haber más de dos direcciones",
                                        "Error de direccciones",
                                        JOptionPane.ERROR_MESSAGE,
                                        null
                                );
                            }
                        } else {
                            JOptionPane.showMessageDialog(
                                    panelFondo,
                                    "Dirección no válida",
                                    "Error de direccciones",
                                    JOptionPane.ERROR_MESSAGE,
                                    null
                            );
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            panelFondo,
                            "No puede haber más de dos direcciones",
                            "Error de direccciones",
                            JOptionPane.ERROR_MESSAGE,
                            null
                    );
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                addAddressButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                addAddressButton.setBackground(new Color(73, 231, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                addAddressButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                addAddressButton.setBackground(new Color(153, 233, 255));
            }
        });

        deleteAddressButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (addressButton1.isSelected() && !addressButton2.isSelected()) {
                    addressButton1.setText(addressButton2.getText());
                    addressButton2.setText("");
                    addressButton2.setVisible(false);
                } else if (!addressButton1.isSelected() && addressButton2.isSelected()) {
                    addressButton2.setText("");
                    addressButton2.setVisible(false);
                } else if (addressButton1.isSelected() && addressButton2.isSelected()) {
                    addressButton1.setText("");
                    addressButton1.setVisible(false);
                    addressButton2.setText("");
                    addressButton2.setVisible(false);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                deleteAddressButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                deleteAddressButton.setBackground(new Color(73, 231, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                deleteAddressButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                deleteAddressButton.setBackground(new Color(153, 233, 255));
            }
        });

        pagoButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                goToPayment();
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

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                backMenu();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                backButton.setBackground(new Color(73, 231, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                backButton.setBackground(new Color(153, 233, 255));
            }
        });

        seguirComprandoButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                backMenu();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                seguirComprandoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                seguirComprandoButton.setBackground(new Color(73, 231, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                seguirComprandoButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                seguirComprandoButton.setBackground(new Color(153, 233, 255));
            }
        });
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

    public void backMenu(){
        CarritoCompra ventanaCarrito = new CarritoCompra(tipo, id, nombre);
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaCarrito.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }

    private void goToPayment(){
        MetodoPago ventanaCatalog = new MetodoPago(tipo, id, precio, nombre);
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaCatalog.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
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

    public int getOrderNumber() {
        return this.orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }
}
