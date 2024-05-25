package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static GUI.CatalogoProductos.getUserData;

public class PagoPaypal {
    private JPanel panelTitulo;
    private JLabel backButton;
    private JLabel perfilButton;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton pagarButton;
    private JCheckBox guardaCheckBox;
    private JLabel precioLabel;
    private JPanel paypalPanel;
    private InicioSesion iniciosesion;
    private int tipo;
    private int id;
    private double precio;
    private String nombre;
    public PagoPaypal(int t, int id, double precio, String nombre) {
        iniciosesion = new InicioSesion();
        paypalPanel.setPreferredSize(new Dimension(800, 600));
        this.tipo = t;
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        precioLabel.setText("Precio final: " + precio + " €");
        paypalPanel.setFocusable(true);
        paypalPanel.requestFocusInWindow();

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                backMenu();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    public void backMenu(){
        MetodoPago ventanaCatalog = new MetodoPago(tipo, id, precio, nombre);
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaCatalog.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }
    public JPanel getPanel(){
        return paypalPanel;
    }
}
