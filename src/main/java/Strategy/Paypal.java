package Strategy;

import GUI.PagoPaypal;

import javax.swing.*;

public class Paypal implements PaymentMethod{
    public void execute(int tipo, int id, double precio, String nombre, JPanel panelMetodo) {
        PagoPaypal ventanaCatalog = new PagoPaypal(tipo, id, precio, nombre);
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaCatalog.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(panelMetodo);
        ventanaActual.dispose();
    }
}
