package Strategy;

import javax.swing.*;

public interface PaymentMethod {
    public void execute(int tipo, int id, double precio, String nombre, JPanel panelMetodo);
}
