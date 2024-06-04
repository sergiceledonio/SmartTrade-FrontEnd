package Strategy;

import GUI.PagoTarjeta;

import javax.swing.*;

public class Card implements PaymentMethod{

    public void execute(int tipo, int id, double precio, String nombre, JPanel panelMetodo) {
        PagoTarjeta ventanaCatalog = new PagoTarjeta(tipo, id, precio, nombre);
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaCatalog.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(panelMetodo);
        ventanaActual.dispose();
    }

}
