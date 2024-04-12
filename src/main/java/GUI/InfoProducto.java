package GUI;

import kotlin.random.Random;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class InfoProducto extends JFrame{
    private JPanel panelTitulo;
    private JPanel infoProduct;
    private JTextField textField1;
    private JButton filtrosButton;
    private JPanel panelInfo;
    private JTextArea productDescription;
    private JButton buyButton;
    private JTable comments;
    private JFrame frame;

    public InfoProducto(){
        panelInfo.setPreferredSize(new Dimension(800,600));

        /*BUYBUTTON*/

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
    }
    public static void main(String[] args) {
        InfoProducto ventanaInfo = new InfoProducto();
        JFrame frame = new JFrame("Smart Trade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(ventanaInfo.panelInfo);
        frame.pack();
        frame.setVisible(true);
    }

    /*METODOS PARA ACCEDER A VARIABLES*/

    private void fillComments(){

    }

}
