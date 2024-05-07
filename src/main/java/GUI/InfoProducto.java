package GUI;

import Observer.ObserverUserData;
import com.sun.webkit.perf.PerfLogger;
import javafx.css.CssParser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
    private InicioSesion iniciosesion;
    public InfoProducto(String prodName, Double prodPrice, String prodType, String prodDescription){
        panelInfo.setPreferredSize(new Dimension(800,600));
        /*SETTING VARIABLES TO WHAT WAS CLICKED*/

        nameProduct.setText(prodName);
        priceProduct.setText("El precio es de: " + prodPrice + "€");
        categoryProduct.setText("Categoria: " + prodType);
        productDescription.setText(prodDescription);

        /*BUYBUTTON*/

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
                super.mouseClicked(e);
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
    }
    public static void main(String[] args) {
        InfoProducto ventanaInfo = new InfoProducto("",(double) 0,"","");
        JFrame frame = new JFrame("Smart Trade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(ventanaInfo.panelInfo);
        frame.pack();
        frame.setVisible(true);
    }

    /*METODOS PARA ACCEDER A VARIABLES*/

    public JPanel getPanel(){
        return this.panelInfo;
    }

    public void backMenu(){
        CatalogoProductos ventanaCatalog = new CatalogoProductos(getUserData(), tipo);
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
    public String[] getUserData(){
        return new String[]{name, email, password, type, iban, cif, dni, city, street, door, flat, num};
    }

    public boolean isFav(Boolean fav){
        if(fav){
            return true;
        }else{
            return false;
        }
    }
}
