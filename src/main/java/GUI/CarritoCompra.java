package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Observer.ObserverUserData;

import static GUI.CatalogoProductos.getUserData;

public class CarritoCompra extends JFrame implements ObserverUserData{
    private JPanel panelCarrito;
    private JPanel panelInfo;
    private JLabel logoButton;
    private JButton filtroButton;
    private JLabel perfilButton;
    private JLabel lupaButton;
    private JPanel panelCompras;
    private JTable listaCarritoProductos;
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
    private JFrame frame;


    public CarritoCompra() {
        iniciosesion = new InicioSesion();
        iniciosesion.addObserver(this);
        panelCarrito.setPreferredSize(new Dimension(800, 600));

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
        CarritoCompra ventanaCarrito = new CarritoCompra();
        JFrame frame = new JFrame("Smart Trade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(ventanaCarrito.getPanel());
        frame.pack();
        frame.setVisible(true);
    }

    /*ACCESO A LAS VARIABLES LOCALES*/

    public JPanel getPanel(){
        return panelCarrito;
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
}
