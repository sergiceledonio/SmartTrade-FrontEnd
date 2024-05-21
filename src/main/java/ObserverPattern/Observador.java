package ObserverPattern;

import javax.swing.*;

public interface Observador {
    void updatePrecio(double precio, JLabel label);
}
