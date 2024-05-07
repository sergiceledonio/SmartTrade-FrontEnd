package Estado;

import javax.swing.*;

public class Open implements StatePattern{

    @Override
    public void changeIcon(JCheckBox visibilityButton) {
        visibilityButton.setIcon(new ImageIcon("src/main/resources/img/ojoAbierto (1).png"));
    }

    @Override
    public void handleVisibilityButton(JPasswordField passTF) {
        passTF.setEchoChar((char) 0);
    }
}
