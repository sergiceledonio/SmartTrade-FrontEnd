package Strategy;

import javax.swing.*;

public class Payment {

    private PaymentMethod method;

    public Payment() {

    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public void executePayment(int tipo, int id, double precio, String nombre, JPanel panelMetodo) {
        method.execute(tipo, id, precio, nombre, panelMetodo);
    }
}
