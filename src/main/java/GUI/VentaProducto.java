package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

public class VentaProducto {

    private JPanel panelProductoVenta;
    private JPanel panelTitulo;
    private JLabel busquedaButton;
    private JTextField textField1;
    private JButton filtroButton;
    private JPanel panelProducto;
    private JLabel perfilButton;
    private JLabel addImgButton;
    private JTextField productNameTF;
    private JTextField productPriceTF;
    private JComboBox categorySelector;
    private JTextArea productDescription;
    private JButton validateProductButton;

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
        /*VALIDATEPRODUCTBUTTON*/

        validateProductButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                validateProductButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                validateProductButton.setBackground(new Color(72,231,255));
            }
            public void mouseExited(MouseEvent e) {
                validateProductButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                validateProductButton.setBackground(new Color(153,233,255));
            }
        });

        /*CATEGORYSELECTOR*/
        String[] categories = {"--Selecione categoria--", "Comida", "Ropa", "Juguete", "Indormática"};
        for(String opcion : categories){
            categorySelector.addItem(opcion);
        }
        categorySelector.setRenderer(new DisabledFirstElementRenderer());
        //categorySelector.setModel(new DisabledFirstElementComboBoxModel(categories));
        categorySelector.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                categorySelector.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                categorySelector.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        categorySelector.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int index = categorySelector.getSelectedIndex();
                if (index != -1) {
                    categorySelector.setCursor(new Cursor(Cursor.HAND_CURSOR));
                } else {
                    categorySelector.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
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

        /*PRODUCTPRICE*/

        productPriceTF.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                int key = e.getKeyChar();
                boolean numero = key >= 48 && key <= 57;
                if(!numero){
                    e.consume();
                }
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


    /*METODOS DE ACCESO A VARIABLES*/

    static class DisabledFirstElementRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel component = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (index == 0) {
                component.setEnabled(false);
            }
            return component;
        }
    }

}
