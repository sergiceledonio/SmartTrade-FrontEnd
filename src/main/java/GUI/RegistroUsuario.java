package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.CropImageFilter;

public class RegistroUsuario extends JFrame{
    private JPanel panelRegistro;
    private JLabel logo;
    private JTextField nombreTF;
    private JTextField passTF;
    private JTextField emailTF;
    private JTextField dniTF;
    private JLabel nombreLabel;
    private JRadioButton vendedorRB;
    private JTextField cifTF;
    private JTextField ibanTF;
    private JButton crearCuentaButton;
    private JTextField repetirContraseñaTF;
    private JLabel atrasButton;
    private JFrame frame;

    public RegistroUsuario(){

        /*NOMBRETF*/

        /*PASSTF*/

        /*EMAILTF*/

        /*REPETIRCONTRASEÑATF*/

        /*DNITF*/

        /*VENDEDORRB*/

        vendedorRB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (vendedorRB.isSelected()) {
                    cifTF.setEnabled(true);
                    ibanTF.setEnabled(true);
                    cifTF.setBackground(new Color(255, 255, 255));
                    ibanTF.setBackground(new Color(255, 255, 255));
                } else {
                    cifTF.setEnabled(false);
                    ibanTF.setEnabled(false);
                    cifTF.setText("");
                    ibanTF.setText("");
                    cifTF.setBackground(new Color(150, 150, 150));
                    ibanTF.setBackground(new Color(150, 150, 150));
                }
            }
        });

        vendedorRB.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                vendedorRB.setCursor(new Cursor(Cursor.HAND_CURSOR));
                vendedorRB.setFont(vendedorRB.getFont().deriveFont(Font.BOLD));
            }
            public void mouseExited(MouseEvent e) {
                vendedorRB.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                vendedorRB.setFont(vendedorRB.getFont().deriveFont(Font.PLAIN));
            }
        });

        /*CIFTF*/

        cifTF.setEnabled(false);
        cifTF.setBackground(new Color(150,150,150));

        /*IBANTF*/

        ibanTF.setEnabled(false);
        ibanTF.setBackground(new Color(150,150,150));

        /*CREARCUENTABUTTON*/

        crearCuentaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                crearCuentaButton.setBackground(new Color(153, 233, 255));
                crearCuentaButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                crearCuentaButton.setBackground(new Color(198, 232, 251));
                crearCuentaButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e){
                String[] datos = createUser();
                /*CONECTAR CON ELEMENTOS DEL SERVICIO PARA CREAR UN USUARIO NUEVO*/
                //nombreLabel.setText(String.join(" ", datos));
            }
        });

        /*ATRASBUTTON*/
        atrasButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                atrasButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                atrasButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                accessLogIn();
            }
        });


        panelRegistro.setFocusable(true);
        panelRegistro.requestFocusInWindow();
    }


    public static void main(String[] args) {
        RegistroUsuario ventana = new RegistroUsuario();
        JFrame frame = new JFrame("Smart Trade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(ventana.panelRegistro);
        frame.pack();
        frame.setVisible(true);
    }


    /*METODOS DE ACCESO A VARIABLES*/
    public JPanel getPanel(){
        return panelRegistro;
    }

    public String[] createUser(){
        String[] newUser = {nombreTF.getText(), passTF.getText(), dniTF.getText(), emailTF.getText(), ibanTF.getText(), cifTF.getText()};
        return newUser;
    }
    public void accessLogIn(){
        InicioSesion inicioSesion = new InicioSesion();
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(inicioSesion.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(atrasButton); // Obtener el marco actual
        ventanaActual.dispose();
    }
}


