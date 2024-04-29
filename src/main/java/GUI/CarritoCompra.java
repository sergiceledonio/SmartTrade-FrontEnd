package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Observer.ObserverUserData;
public class CarritoCompra extends JFrame implements ObserverUserData{
    private JPanel panelCarrito;
    private JPanel panelInfo;
    private JLabel logoButton;
    private JButton filtroButton;
    private JLabel perfilButton;
    private JLabel lupaButton;
    private JPanel panelCompras;
    private JTable listaCarritoProductos;
    private static String name;
    private static String password;
    private static String email;
    private static String iban;
    private static String cif;
    private static String dni;
    private static String city;
    private static String street;
    private static String door;
    private static String flat;
    private static String num;
    private static String type;
    private static String price;
    private static String description;
    private static String nameProd;
    private static String category;
    private InicioSesion iniciosesion;
    private JFrame frame;


    public CarritoCompra() {
        iniciosesion = new InicioSesion();
        iniciosesion.addObserver(this);
        panelCarrito.setPreferredSize(new Dimension(800, 600));


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
        perfilButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                perfilButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent e) {
                perfilButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        panelCarrito = new JPanel();
        panelCarrito.setLayout(new BorderLayout());

        String[] productos = {"",""};//PRODUCTOS DEL CARRITO
        String[] columnNames = {"Producto", "Cantidad", "Precio"};
        DefaultTableModel modelo = new DefaultTableModel(columnNames,0);
        listaCarritoProductos.setModel(modelo);
        panelCarrito.add(new JScrollPane(listaCarritoProductos));

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CarritoCompra::new);
    }

    /*ACCESO A LAS VARIABLES LOCALES*/


    public void crearTabla(){
        frame = new JFrame("Table Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 300);

        String[] columnNames = {"PRODUCTO", "CANTIDAD", "PRECIO"};
        Object[][] data = {{"Product 1", 0, 10.0}, {"Product 2", 0, 20.0}};

        model = new DefaultTableModel(data, columnNames);
        table = new JTable(model);

        // Custom renderer for the 'CANTIDAD' column
        table.getColumnModel().getColumn(1).setCellRenderer(new QuantityRenderer());
        // Custom editor for the 'CANTIDAD' column
        table.getColumnModel().getColumn(1).setCellEditor(new QuantityEditor(new JTextField()));

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private class QuantityRenderer extends JPanel implements TableCellRenderer {
        private JButton plusButton;
        private JButton minusButton;
        private JLabel quantityLabel;

        public QuantityRenderer() {
            setLayout(new GridLayout(1, 3));
            plusButton = new JButton("+");
            minusButton = new JButton("-");
            quantityLabel = new JLabel("0");

            add(minusButton);
            add(quantityLabel);
            add(plusButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            int quantity = (int) value;
            quantityLabel.setText(String.valueOf(quantity));
            return this;
        }
    }

    private class QuantityEditor extends DefaultCellEditor {
        private JButton plusButton;
        private JButton minusButton;
        private JLabel quantityLabel;
        private JPanel panel;

        public QuantityEditor(JTextField textField) {
            super(textField);
            plusButton = new JButton("+");
            minusButton = new JButton("-");
            quantityLabel = new JLabel("0");

            panel = new JPanel();
            panel.setLayout(new GridLayout(1, 3));
            panel.add(minusButton);
            panel.add(quantityLabel);
            panel.add(plusButton);

            // Add action listeners for the buttons
            plusButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int row = table.getSelectedRow();
                    int quantity = (int) model.getValueAt(row, 1);
                    model.setValueAt(quantity + 1, row, 1);
                }
            });

            minusButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int row = table.getSelectedRow();
                    int quantity = (int) model.getValueAt(row, 1);
                    if (quantity > 0) {
                        model.setValueAt(quantity - 1, row, 1);
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            int quantity = (int) value;
        }
    }

    public JPanel getPanel(){
        return panelCarrito;
    }

    public void backMenu(){
        CatalogoProductos ventanaCatalog = new CatalogoProductos(CatalogoProductos.getUserData());
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

    public String[] getInfoProducts(){
        return new String[]{};
    }

}
