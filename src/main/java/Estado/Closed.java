package Estado;

import javax.swing.*;

public class Closed implements StatePattern{
    @Override
    public void changeIcon(JCheckBox visibilityButton) {
        visibilityButton.setIcon(new ImageIcon("src/main/resources/img/ojoCerrado (1).png"));
    }

    @Override
    public void handleVisibilityButton(JPasswordField passTF){
        passTF.setEchoChar('*');
    }
}
