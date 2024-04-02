package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VentaProducto {

    private JPanel panelProductoVenta;
    private JPanel panelTitulo;
    private JLabel busquedaButton;
    private JTextField textField1;
    private JButton filtroButton;
    private JPanel panelProducto;
    private JLabel perfilButton;

    public VentaProducto(){


        /*PANELPRODUCTOVENTA*/

        panelProductoVenta.setPreferredSize(new Dimension(800,600));

        /*BUSQUEDABUTTON*/

        busquedaButton.setPreferredSize(new Dimension(busquedaButton.getIcon().getIconWidth(),busquedaButton.getIcon().getIconHeight()));
        busquedaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                busquedaButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent e) {
                busquedaButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        /*FILTROBUTTON*/

        filtroButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                filtroButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                filtroButton.setBackground(new Color(72,231,255));
            }
            public void mouseExited(MouseEvent e) {
                filtroButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                filtroButton.setBackground(new Color(153,233,255));
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
    }


    public static void main(String[] args) {
        VentaProducto ventanaVenta = new VentaProducto();
        JFrame frame = new JFrame("Smart Trade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(ventanaVenta.panelProductoVenta);
        frame.pack();
        frame.setVisible(true);
    }

}
