package GUI;

import javax.accessibility.Accessible;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import java.awt.event.KeyEvent;

public class VentaProducto extends JFrame{

    private JPanel panelProductoVenta;
    private JPanel panelTitulo;
    private JLabel busquedaButton;
    private JTextField searchTF;
    private JButton filtroButton;
    private JPanel panelProducto;
    private JLabel perfilButton;
    private JLabel addImgButton;
    private JTextField productNameTF;
    private JTextField productPriceTF;
    private JComboBox categorySelector;
    private JTextArea productDescription;
    private JButton validateProductButton;
    private JLabel añade;
    private JLabel logoButton;
    private JFrame frame;

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
                filtroButton.setBackground(new Color(73,231,255));
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
                validateProductButton.setBackground(new Color(73,231,255));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                validateProductButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                validateProductButton.setBackground(new Color(153,233,255));
            }
            @Override
            public void mouseClicked(MouseEvent e){
                String selection = getComboBox().getSelectedItem().toString();
                if(acceptProduct()){
                    JOptionPane.showMessageDialog(frame, "Producto válido, añadido al servicio", "Validación",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        /*CATEGORYSELECTOR*/
        String[] categories = {"--Selecione categoria--", "Comida", "Ropa", "Juguete", "Informática"};
        for(String opcion : categories){
            categorySelector.addItem(opcion);
        }

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
        Component comp = categorySelector.getComponent(0);
        if(comp instanceof JButton){
            JButton arrow = (JButton)comp;
            arrow.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        categorySelector.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                JComboBox<?> comboBox = (JComboBox<?>) e.getSource();
                Accessible a = comboBox.getUI().getAccessibleChild(comboBox, 0);
                if (a instanceof ComboPopup) {
                    JList<?> list = ((ComboPopup) a).getList();
                        list.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                JComboBox<?> comboBox = (JComboBox<?>) e.getSource();
                comboBox.setCursor(Cursor.getDefaultCursor());
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                JComboBox<?> comboBox = (JComboBox<?>) e.getSource();
                comboBox.setCursor(Cursor.getDefaultCursor());
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
                boolean numero = key >= 48 && key <= 57 || key == 46;
                if(!numero){
                    e.consume();
                }
            }
        });
        ((AbstractDocument) productPriceTF.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                // Verificar si la cadena que se está insertando es válida
                if (isValidInsertion(fb.getDocument(), offset, string)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                // Verificar si el texto que se está reemplazando es válido
                if (isValidInsertion(fb.getDocument(), offset, text)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            private boolean isValidInsertion(Document document, int offset, String text) throws BadLocationException {
                String currentText = document.getText(0, document.getLength());
                StringBuilder newText = new StringBuilder(currentText);
                newText.insert(offset, text);

                // Verificar si el nuevo texto cumple con el formato de precio válido
                return isValidPrice(newText.toString());
            }
        });


        /*IMGBUTTON*/

        addImgButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                addImgButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent e) {
                addImgButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        /*PRODUCTDESCRIPTION*/

        productDescription.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(productDescription.getText().length() > 350){
                    e.consume();
                }
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

            }
            return component;
        }
    }
    public JComboBox getComboBox(){
        return this.categorySelector;
    }
    public boolean validationComboBox(String selection){
        String res = getComboBox().getItemAt(0).toString();
        return selection.equals(res);
    }
    public boolean isValidPrice(String text) {
            return text.matches("^\\d+(\\.\\d{0,2})?$");
    }
    public boolean isValidName(String name){
        return name.matches("[a-zA-Z]+");
    }

    public boolean acceptProduct(){

        if(!validationComboBox(categorySelector.getSelectedItem().toString())){
            if(isValidName(productNameTF.getText())){
                if(isValidPrice(productPriceTF.getText())){
                    return true;
                }else{
                    JOptionPane.showMessageDialog(frame, "Precio con formato incorrecto", "Error",JOptionPane.ERROR_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(frame, "Nombre no válido", "Error",JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(frame, "Categoria seleccionada no válida", "Error",JOptionPane.ERROR_MESSAGE);
        }
    return false;
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

    public JPanel getPanel(){
        return this.panelProductoVenta;
    }

}
