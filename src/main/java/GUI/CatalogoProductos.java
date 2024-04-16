package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;

public class CatalogoProductos {
    private JPanel panelCatalogo;
    private JPanel panelInfo;
    private JPanel panelListado;
    private JLabel logoLabel;
    private JButton filtrosButton;
    private JLabel perfilLabel;
    private JTextField searchTF;
    private JTable tableCatalog;

    public CatalogoProductos(){
        panelCatalogo.setPreferredSize(new Dimension(800, 600));

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

    }

    public static void main(String[] args) {
        CatalogoProductos ventanaCatalogo = new CatalogoProductos();
        JFrame frame = new JFrame("Smart Trade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(ventanaCatalogo.panelCatalogo);
        frame.pack();
        frame.setVisible(true);
    }

}


