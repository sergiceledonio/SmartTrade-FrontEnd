package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InicioSesion extends JFrame{
    private JPanel panelInicioSesion;
    private JTextField userTF;
    private JPasswordField passTF;
    private JButton loginButton;
    private JLabel logo;
    private JLabel registrateButton;
    private JFrame   frame;



    /////////

    private boolean primeraUser = true;
    private boolean visiblePass = true;

    public InicioSesion(){

        /*USERTF*/
        userTF.setText("Usuario");
        userTF.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if(primeraUser){
                    userTF.setText("");
                    primeraUser = false;
                }
            }
        });

        /*PASSTF*/
        passTF.setText("Contraseña");
        passTF.setEchoChar((char) 0);
        passTF.addFocusListener(new FocusAdapter() {
            private boolean primeraPass = true;
            @Override
            public void focusGained(FocusEvent e) {
                if(primeraPass){
                    passTF.setText("");
                    primeraPass = false;
                    passTF.setEchoChar('*');
                }
            }
        });

        /*LOGINBUTTON*/
        loginButton.setBackground(new Color(198,232,251));
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(153,233,255));
                loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(new Color(198,232,251));
                loginButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e){
                logIn();
            }

        });


        // VISIBILIDAD PASSWORD FIELD
        /*if(visiblePass){
            passTF.setEchoChar((char) 0);
            visiblePass = false;
        }else{
            passTF.setEchoChar('*');
            visiblePass = true;
        }*/

        /*REGISTRATEBUTTON*/
        registrateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                accessRegister();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                registrateButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                registrateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

        panelInicioSesion.setFocusable(true);
        panelInicioSesion.requestFocusInWindow();
    }
    public static void main(String[] args) {
        InicioSesion ventana = new InicioSesion();
        JFrame frame = new JFrame("Smart Trade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(ventana.panelInicioSesion);
        frame.pack();
        frame.setVisible(true);
    }


    /*METODOS DE ACCESO A VARIABLES*/

    public JPanel getPanel(){
        return panelInicioSesion;
    }
    public void logIn(){
        String email = userTF.getText();
        String password = new String(passTF.getPassword());
        /*
        *
        *
        * COMPROBAR SI EL USUARIO EXISTE Y LA CONTRASEÑA ES CORRECTA
        *
        * AL COMPROBAR USAR EL MÉTODO accessRegister();
        * */

    }

    public void accessRegister(){
        RegistroUsuario registroUsuario = new RegistroUsuario();
        JFrame registro = new JFrame("Smart Trade");
        registro.setContentPane(registroUsuario.getPanel());
        registro.pack();
        registro.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(registrateButton); // Obtener el marco actual
        ventanaActual.dispose();
    }


}
