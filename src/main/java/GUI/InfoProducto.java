package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

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
