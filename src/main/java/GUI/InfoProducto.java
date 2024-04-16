package GUI;

import com.sun.webkit.perf.PerfLogger;
import javafx.css.CssParser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InfoProducto extends JFrame{
    private JPanel panelTitulo;
    private JPanel infoProduct;
    private JTextField searchTF;
    private JButton filtrosButton;
    private JPanel panelInfo;
    private JTextArea productDescription;
    private JButton buyButton;
    private JTable comments;
    private JLabel logoButton;
    private JLabel searchButton;
    private JLabel perfilButton;
    private JLabel nameProduct;
    private JLabel categoryProduct;
    private JLabel priceProduct;
    private JFrame frame;

    public InfoProducto(String name, int price, String category, String description, Color color){
        panelInfo.setPreferredSize(new Dimension(800,600));
        /*SETTING VARIABLES TO WHAT WAS CLICKED*/

        nameProduct.setText(name);
        priceProduct.setText("El precio es: " + price + "€");
        categoryProduct.setText("Categoria: " + category);
        productDescription.setText(description);
        productDescription.setForeground(color);

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

        /*COMMENTS*/

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Comentarios");
        String[] productComments = {"Muy bueno", "Me encanta", "Me encanta", "Me encantan los nachos", "Me encanta el guacamole", "Me encantan las salchichas", "Me gusta el baloncesto", "Me encanta nerea"}; //Metodo que acceda a la BD y recoja comentarios
        for(int i = 0; i < 4; i++) {
            model.addRow(new Object[]{productComments[i]});
            System.out.println();
        }
        comments.setEnabled(false);
        comments.setRowHeight(63);
        comments.setMaximumSize(new Dimension(200, 120));
        comments.setPreferredScrollableViewportSize(new Dimension(200, 120));
        comments.setModel(model);
        comments.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                System.out.println("La altura es: " + comments.getHeight());
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
    }
    public static void main(String[] args) {
        InfoProducto ventanaInfo = new InfoProducto("Producto", 33, "Juguetes", "Este es el inicio del proyecto", new Color(1,1,1));
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
        CatalogoProductos ventanaCatalog = new CatalogoProductos();
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaCatalog.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }

}
