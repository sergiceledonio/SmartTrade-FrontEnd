package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PagoTarjeta {
    private JPanel panelTitulo;
    private JLabel backButton;
    private JLabel perfilButton;
    private JTextField numeroField;
    private JCheckBox guardaCheckBox;
    private JButton pagarButton;
    private JTextField nombreField;
    private JTextField cvvField;
    private JTextField fechaField;
    private JLabel precioLabel;
    private JPanel cardPanel;
    private InicioSesion iniciosesion;
    private int tipo;
    private int id;
    private double precio;
    private String nombre;

    public PagoTarjeta(int t, int id, double precio, String nombre) {
        iniciosesion = new InicioSesion();
        cardPanel.setPreferredSize(new Dimension(800, 600));
        this.tipo = t;
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        precioLabel.setText("Precio final: " + precio + " €");
        cardPanel.setFocusable(true);
        cardPanel.requestFocusInWindow();

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
        return cardPanel;
    }
}
