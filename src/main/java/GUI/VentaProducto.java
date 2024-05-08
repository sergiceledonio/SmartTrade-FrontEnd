package GUI;

import Observer.ObserverUserData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class VentaProducto extends JFrame implements ObserverUserData {

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
    private JPanel panel;
    private JLabel carritoCompraButton;
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
    private String[] attributes;
    private InicioSesion iniciosesion;
    private int tipo;
    private int id;

    public VentaProducto(String[] userData, int tipo, int id){
        this.tipo = tipo;
        this.id = id;
        iniciosesion = new InicioSesion();
        iniciosesion.addObserver(this);

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
                if(acceptProduct()){
                    tryValidation();
                    System.out.println(tipo);
                    backMenu(tipo);
                }
            }
        });

        /*CATEGORYSELECTOR*/
        String[] categories = {"--Selecione categoria--", "Comida", "Ropa", "Juguete", "Informática", "Turismo", "Cosméticos", "Libros"};
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
                if (isValidInsertion(fb.getDocument(), offset, string)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
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
                backMenu(tipo);
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

        carritoCompraButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CarritoCompra ventanaCarrito = new CarritoCompra(tipo, id);
                JFrame frame = new JFrame("Smart Trade");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setContentPane(ventanaCarrito.getPanel());
                frame.pack();
                frame.setVisible(true);

            }
            @Override
            public void mouseEntered(MouseEvent e) {
                carritoCompraButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                carritoCompraButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }


    public static void main(String[] args) {
        VentaProducto ventanaVenta = new VentaProducto(new String[]{}, 0, 0);
        JFrame frame = new JFrame("Smart Trade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(ventanaVenta.panelProductoVenta);
        frame.pack();
        frame.setVisible(true);
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
        return name.matches("^[a-zA-Z0-9\\s]+$");
    }

    public boolean acceptProduct(){

        if(!validationComboBox(categorySelector.getSelectedItem().toString())){
            if(isValidName(productNameTF.getText())){
                if(isValidPrice(productPriceTF.getText())){
                    attributes = getData(productNameTF.getText(),productPriceTF.getText(), productDescription.getText(), categorySelector.getSelectedItem().toString());
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

    public void backMenu(int param){
        CatalogoProductos ventanaCatalog = new CatalogoProductos(getUserData(), param, id);
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

    private void tryValidation(){

        String url = "http://localhost:8080/product/newproducts";

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonBody = objectMapper.createObjectNode();

        jsonBody.put("name", attributes[0]);
        jsonBody.put("price", attributes[1]);
        jsonBody.put("type", attributes[2]);
        jsonBody.put("description", attributes[3]);
        jsonBody.put("pending", true);
        jsonBody.put("validation", false);

        String jsonString = jsonBody.toString();

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Error al enviar la solicitud POST: " + e.getMessage());
        }


    }
    private String[] getData(String nameProd, String price, String description, String category){
        String[] res = {nameProd, price, description, category};
        return res;
    }

}
