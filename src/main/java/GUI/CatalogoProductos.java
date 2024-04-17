package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class CatalogoProductos {
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
    private Boolean isSeller;
    private String name;
    private String password;
    private String email;

    public CatalogoProductos(String nombre, String password, String email, Boolean isSeller){
        panelCatalogo.setPreferredSize(new Dimension(800, 600));
        this.isSeller = isSeller;
        this.name = nombre;
        this.password = password;
        this.email = email;

        ventaProducto.setVisible(isSeller);

        /*TABLECATALOG*/

        DefaultTableModel model = new DefaultTableModel();
        String[] columnas = {"Este", "Es", "El", "Catalogo"};
        for(int i = 0; i < columnas.length; i++){
            model.addColumn(columnas[i]);
        }
        String[][] productComments = {{"Pelota", "Libro", "Gafas", "Nachos"}, {"Falda", "Movil", "Zapatos", "Cuerda"}}; //Acceder a la BD y recoger los elementos guardados


        for(int i = 0; i < productComments.length; i++) {
            model.addRow(productComments[i]);
            System.out.println(Arrays.toString(productComments[i]));
        }
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
                        Color color = Color.black;
                        infoProduct(nombreCelda, price, category, descripcion, color);
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

    }

    public static void main(String[] args) {
        CatalogoProductos catalogoProductos = new CatalogoProductos("Nombre", "Contraseña", "email@example.com", true);
        catalogoProductos.setMain();
    }

    /*METODOS PARA ACCEDER A VARIABLES*/

    public JPanel getPanel(){
        return this.panelCatalogo;
    }

    public void backMenu(){
        CatalogoProductos ventanaCatalog = new CatalogoProductos(name, password, email, isSeller);
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaCatalog.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }

    public void setMain(){
        CatalogoProductos ventanaCatalogo = new CatalogoProductos(name, password,email,isSeller);
        JFrame frame = new JFrame("Smart Trade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(ventanaCatalogo.panelCatalogo);
        frame.pack();
        frame.setVisible(true);
    }

    public void sellProduct(){
        VentaProducto ventanaVenta = new VentaProducto(name, email, password, isSeller);
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaVenta.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }

    public void infoProduct(String name, int price, String category, String description, Color color){
        InfoProducto ventanaInfo = new InfoProducto(name, price, category, description, color);
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaInfo.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }

    public String getName(){
        return name;
    }
    public String getPassword(){
        return password;
    }
    public String getEmail(){
        return email;
    }
    public Boolean getIsSeller(){
        return  isSeller;
    }
}


