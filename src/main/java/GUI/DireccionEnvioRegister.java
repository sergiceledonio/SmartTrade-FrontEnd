package GUI;

import Observer.ObserverRegister;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class DireccionEnvioRegister extends JDialog  implements ObserverRegister {
    private JPanel contentPane;
    private JButton buttonCancel;
    private JButton buttonOK;
    private JTextField numTF;
    private JTextField streetTF;
    private JTextField cityTF;
    private JTextField floorTF;
    private JTextField doorTF;
    private List<ObserverRegister> observadores = new ArrayList<>();
    private String[] direccion;
    private String num;
    private String floor;
    private String door;
    private String city;
    private String street;
    public DireccionEnvioRegister() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonCancel);
        this.setTitle("Añadir dirección de envío");
        contentPane.setPreferredSize(new Dimension(400, 400));

        buttonCancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onOK();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                buttonCancel.setBackground(new Color(73, 231, 255));
                buttonCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonCancel.setBackground(new Color(153, 233, 255));
                buttonCancel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        buttonOK.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onCancel();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                buttonOK.setBackground(new Color(73, 231, 255));
                buttonOK.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonOK.setBackground(new Color(153, 233, 255));
                buttonOK.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        if(isValidContent()){
            direccion = getDireccion();
            notifyObservers(direccion);
            dispose();
        }else{
            clearRegisters();
        }
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        DireccionEnvioRegister dialog = new DireccionEnvioRegister();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    /*VALIDACIÓN CAMPOS*/

    public boolean isValidContent(){

        if(allFieldsFilled(contentPane)){
            if(areInts(numTF.getText(),doorTF.getText(), floorTF.getText())){
                if(areLetters(cityTF.getText(), streetTF.getText())){

                }else{
                    JOptionPane.showMessageDialog(contentPane, "El formato de \"Ciudad\" y \"Calle\" debe ser una letra", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }else{
                JOptionPane.showMessageDialog(contentPane, "El formato de \"Número\", \"Planta\" y \"Puerta\" debe ser un número", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }else{
            JOptionPane.showMessageDialog(contentPane, "Tienes que rellenar todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean allFieldsFilled(Container container){
        Boolean verified = true;

        if(cityTF.getText().isEmpty() || streetTF.getText().isEmpty() || numTF.getText().isEmpty() || doorTF.getText().isEmpty() || floorTF.getText().isEmpty()){
            verified = false;
        }

        return verified;
    }

    public boolean areInts(String num, String door, String floor){

        boolean valid = true;

        for (int i = 0; i < num.length(); i++) {
            if (!Character.isDigit(num.charAt(i))) {
                return false;
            }
        }
        for (int i = 0; i < door.length(); i++) {
            if (!Character.isDigit(door.charAt(i))) {
                return false;
            }
        }
        for (int i = 0; i < floor.length(); i++) {
            if (!Character.isDigit(floor.charAt(i))) {
                return false;
            }
        }

        return valid;
    }

    public boolean areLetters(String street, String city){

        boolean valid = true;

        for (int i = 0; i < street.length(); i++) {
            if (!Character.isLetter(street.charAt(i))) {
                return false;
            }
        }
        for (int i = 0; i < city.length(); i++) {
            if (!Character.isLetter(city.charAt(i))) {
                return false;
            }
        }
        return valid;
    }

    public void clearRegisters(){
        cityTF.setText("");
        streetTF.setText("");
        numTF.setText("");
        doorTF.setText("");
        floorTF.setText("");
    }

    public String[] getDireccion() {
        String num = numTF.getText();
        String street = streetTF.getText();
        String city = cityTF.getText();
        String floor = floorTF.getText();
        String door = doorTF.getText();

        return new String[]{num, street, city, floor, door};
    }
    @Override
    public void addObserver(ObserverRegister observer) {
        observadores.add(observer);
    }

    @Override
    public void removeObserver(ObserverRegister observer) {
        observadores.remove(observer);
    }

    @Override
    public void notifyObservers(String[] data) {
        for (ObserverRegister observer : observadores) {
            observer.update(data);
        }
    }
    @Override
    public void update(String[] data) {
        num = data[0];
        street = data[1];
        city = data[2];
        floor = data[3];
        door = data[4];
    }
}
