package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MetodoPago {
    private JPanel panelMetodo;
    private JPanel panelTitulo;
    private JLabel backButton;
    private JLabel perfilButton;
    private JButton addTrajetaButton;
    private JButton addPaypalButton;
    private JButton aceptarButton;
    private JPanel panelInfo;
    private JScrollPane panelLista;
    private JLabel precioLabel;
    private InicioSesion iniciosesion;
    private int tipo;
    private int id;
    private double precio;
    private String nombre;

    public MetodoPago(int t, int id, double precio, String n)
    {
        iniciosesion = new InicioSesion();
        panelMetodo.setPreferredSize(new Dimension(800, 600));
        this.tipo = t;
        this.id = id;
        this.precio = precio;
        this.nombre = n;
        precioLabel.setText("Precio final: " + precio + " €");
        panelMetodo.setFocusable(true);
        panelMetodo.requestFocusInWindow();
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
        CatalogoProductos ventanaCatalog = new CatalogoProductos(tipo, id, nombre);
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaCatalog.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }
    public JPanel getPanel(){
        return panelMetodo;
    }
}
