package GUI;

import Email.EmailSender;
import Observer.ObserverUserData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CatalogoProductos extends JFrame implements ObserverUserData {
    private JPanel panelCatalogo;
    private JPanel panelInfo;
    private JPanel panelListado;
    private JLabel logoButton;
    private JButton filtroButton;
    private JComboBox perfilButton;
    private JTextField searchTF;
    private JButton ventaProducto;
    private JButton ventasValidas;
    private JLabel lupaButton;
    private JLabel carritoCompraButton;
    private static String name;
    private static String password;
    private static String email;
    private static String iban;
    private static String cif;
    private static String dni;
    private static String city;
    private static String street;
    private static String door;
    private static String flat;
    private static String num;
    private static String prodType;
    private static double prodPrice;
    private static String prodDescription;
    private static String prodName;
    private static String type;
    private JPanel panelProductos;
    private final InicioSesion iniciosesion;
    private int tipo;
    private String[] userData;
    private List searchHistory = new List();
    private int id;
    String[] storableSearchButtonText = {"", "", ""};
    JButton storableSearchButton1 = new JButton(storableSearchButtonText[0]);
    JButton storableSearchButton2 = new JButton(storableSearchButtonText[1]);
    JButton storableSearchButton3 = new JButton(storableSearchButtonText[2]);
    private JDialog filterPopUp;
    private byte[] img;
    private String nombre;
    public CatalogoProductos(int tipo, int id, String nombre) {
        iniciosesion = new InicioSesion();
        iniciosesion.addObserver(this);

        this.tipo = tipo;
        this.id = id;
        this.nombre = nombre;

        ImageIcon fav = new ImageIcon("img/favLleno.jpeg");
        ImageIcon user = new ImageIcon("img/user.png");

        System.out.println("********************************************************************");
        System.out.println("Catalogo de productos");
        System.out.println("El tipo de usuario es: ");
        System.out.println(tipo);
        System.out.println("El id de usuario es: ");
        System.out.println(id);


        fav = imageIconUpdate(fav);
        user = imageIconUpdate(user);
        inicializarComponentes(fav, user);
        organizarInterfaz(tipo);
        getProducts();
        //sendEmail(id, nombre);


        ventaProducto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sellProduct();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                ventaProducto.setCursor(new Cursor(Cursor.HAND_CURSOR));
                ventaProducto.setBackground(new Color(73, 231, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ventaProducto.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                ventaProducto.setBackground(new Color(153, 233, 255));
            }
        });
        ventasValidas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                goToValidated();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                ventasValidas.setCursor(new Cursor(Cursor.HAND_CURSOR));
                ventasValidas.setBackground(new Color(73, 231, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ventasValidas.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                ventasValidas.setBackground(new Color(153, 233, 255));
            }
        });
        logoButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                backMenu(tipo);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                logoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                logoButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        lupaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                /*
                for (int prod = 0; prod < products.length; prod++) {
                    for (int pos = 0; pos < searchTF.getText().toLowerCase().length(); pos++) {
                        if (searchTF.getText().toLowerCase().substring(0, pos)
                                .equals(products.get(i).getName().substring(0, pos))) {
                            //buscar en la bd y mostrar por pantalla los productos que cumplan la condición
                        }
                    }
                }
                searchHistory = stotableSearch(searchHistory, products, searchTF.getText());
                */
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                lupaButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lupaButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        searchTF.addKeyListener(
                new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                            /*
                            for (int prod = 0; prod < products.length; prod++) {
                                for (int pos = 0; pos < searchTF.getText().toLowerCase().length(); pos++) {
                                    if (searchTF.getText().toLowerCase().substring(0, pos)
                                            .equals(products.get(i).getName().substring(0, pos))) {
                                        //buscar en la bd y mostrar por pantalla los productos que cumplan la condición
                                    }
                                }
                            }
                            searchHistory = stotableSearch(searchHistory, products, searchTF.getText());
                            */
                        }
                    }
                }
        );

        filtroButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                filterPopUp = new JDialog();
                JSeparator separator = new JSeparator();
                JComboBox<String> shippingDurationComboBox = new JComboBox<String>();
                final int[] numStars = {0};

                JButton okButton = new JButton("Aceptar");
                JToggleButton ascendingToggleButton = new JToggleButton("Ascendente");
                JToggleButton descendingToggleButton = new JToggleButton("Descendente");
                ButtonGroup storableSearchButtonGroup = new ButtonGroup();
                JButton starButton1 = new JButton();
                JButton starButton2 = new JButton();
                JButton starButton3 = new JButton();
                JButton starButton4 = new JButton();
                JButton starButton5 = new JButton();

                JLabel priceLabel = new JLabel("Precio");
                JLabel minPriceLabel = new JLabel("min");
                JLabel maxPriceLabel = new JLabel("max");
                JLabel sortByCategotyLabel = new JLabel("Ordenar por categoría");
                JLabel valorationLabel = new JLabel("Valoración");
                JLabel shippingDurationLabel = new JLabel("Duración del envío");
                JLabel storableSearchLabel = new JLabel("Búsqueda Almacenable");
                JLabel minPriceErrorLabel = new JLabel("El mínimo no es un número válido");
                JLabel maxPriceErrorLabel = new JLabel("El máximo no es un número válido");
                JLabel greaterOrlowerPriceErrorLabel = new JLabel("El mínimo debe ser menor que el máximo");

                JTextField minPriceTextField = new JTextField();
                JTextField maxPriceTextField = new JTextField();

                ImageIcon filledStarImageIcon = new ImageIcon(System.getProperty("user.dir") +
                        System.getProperty("file.separator") + "src" + System.getProperty("file.separator") + "main" +
                        System.getProperty("file.separator") + "resources" + System.getProperty("file.separator") +
                        "img" + System.getProperty("file.separator") + "favLleno" + ".jpeg");
                ImageIcon blankStarImageIcon = new ImageIcon(System.getProperty("user.dir") +
                        System.getProperty("file.separator") + "src" + System.getProperty("file.separator") + "main" +
                        System.getProperty("file.separator") + "resources" + System.getProperty("file.separator") +
                        "img" + System.getProperty("file.separator") + "favVacio" + ".png");

                Image filledStarImage = filledStarImageIcon.getImage();
                Image blankStarImage = blankStarImageIcon.getImage();
                Image scaledFilledStarImage = filledStarImage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                Image scaledBlankStarImage = blankStarImage.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
                ImageIcon scaledFilledStarImageIcon = new ImageIcon(scaledFilledStarImage);
                ImageIcon scaledBlankStarImageIcon = new ImageIcon(scaledBlankStarImage);

                filterPopUp.setTitle("Filtros y Búsqueda Almacenable");
                filterPopUp.setSize(500, 400);
                filterPopUp.setLayout(null);
                filterPopUp.setLocationRelativeTo(panelCatalogo);
                filterPopUp.add(okButton);
                filterPopUp.add(storableSearchButton1);
                filterPopUp.add(storableSearchButton2);
                filterPopUp.add(storableSearchButton3);
                filterPopUp.add(separator);
                filterPopUp.add(priceLabel);
                filterPopUp.add(minPriceTextField);
                filterPopUp.add(maxPriceTextField);
                filterPopUp.add(minPriceLabel);
                filterPopUp.add(maxPriceLabel);
                filterPopUp.add(sortByCategotyLabel);
                filterPopUp.add(ascendingToggleButton);
                filterPopUp.add(descendingToggleButton);
                filterPopUp.add(valorationLabel);
                filterPopUp.add(starButton1);
                filterPopUp.add(starButton2);
                filterPopUp.add(starButton3);
                filterPopUp.add(starButton4);
                filterPopUp.add(starButton5);
                filterPopUp.add(shippingDurationLabel);
                filterPopUp.add(shippingDurationComboBox);
                filterPopUp.add(storableSearchLabel);
                filterPopUp.add(minPriceErrorLabel);
                filterPopUp.add(maxPriceErrorLabel);
                filterPopUp.add(greaterOrlowerPriceErrorLabel);

                priceLabel.setBounds(115, 0, 40, 30);
                minPriceTextField.setEditable(true);
                maxPriceTextField.setEditable(true);
                minPriceTextField.setBounds(90, 30, 40, 25);
                maxPriceTextField.setBounds(140, 30, 40, 25);
                minPriceLabel.setBounds(100, 50, 40 ,25);
                maxPriceLabel.setBounds(150, 50, 40, 25);
                minPriceErrorLabel.setBounds(35, 65, 250 ,25);
                minPriceErrorLabel.setForeground(Color.RED);
                minPriceErrorLabel.setVisible(false);
                maxPriceErrorLabel.setBounds(35, 65, 250 ,25);
                maxPriceErrorLabel.setForeground(Color.RED);
                maxPriceErrorLabel.setVisible(false);
                greaterOrlowerPriceErrorLabel.setBounds(25, 65, 250 ,25);
                greaterOrlowerPriceErrorLabel.setForeground(Color.RED);
                greaterOrlowerPriceErrorLabel.setVisible(false);
                goBackFromFilterWithEsc(minPriceTextField);
                goBackFromFilterWithEsc(maxPriceTextField);

                sortByCategotyLabel.setBounds(75, 95, 200, 30);
                ascendingToggleButton.setBounds(10, 125, 120, 30);
                descendingToggleButton.setBounds(135, 125, 120, 30);
                storableSearchButtonGroup.add(ascendingToggleButton);
                storableSearchButtonGroup.add(descendingToggleButton);
                ascendingToggleButton.addActionListener(
                        actionEvent -> {
                            ascendingToggleButton.setSelected(true);
                        }
                );
                descendingToggleButton.addActionListener(
                        actionEvent -> {
                            descendingToggleButton.setSelected(true);
                        }
                );
                goBackFromFilterWithEsc(ascendingToggleButton);
                goBackFromFilterWithEsc(descendingToggleButton);

                valorationLabel.setBounds(345, 0, 100, 30);
                starButton1.setIcon(scaledBlankStarImageIcon);
                starButton2.setIcon(scaledBlankStarImageIcon);
                starButton3.setIcon(scaledBlankStarImageIcon);
                starButton4.setIcon(scaledBlankStarImageIcon);
                starButton5.setIcon(scaledBlankStarImageIcon);
                starButton1.setBounds(300, 30, 25, 25);
                starButton2.setBounds(330, 30, 25, 25);
                starButton3.setBounds(360, 30, 25, 25);
                starButton4.setBounds(390, 30, 25, 25);
                starButton5.setBounds(420, 30, 25, 25);
                starButton1.addActionListener(
                        actionEvent -> {
                            if (starButton1.getIcon().equals(scaledFilledStarImageIcon) &&
                                    starButton2.getIcon().equals(scaledBlankStarImageIcon) &&
                                    starButton3.getIcon().equals(scaledBlankStarImageIcon) &&
                                    starButton4.getIcon().equals(scaledBlankStarImageIcon) &&
                                    starButton5.getIcon().equals(scaledBlankStarImageIcon)) {
                                starButton1.setIcon(scaledBlankStarImageIcon);
                                starButton2.setIcon(scaledBlankStarImageIcon);
                                starButton3.setIcon(scaledBlankStarImageIcon);
                                starButton4.setIcon(scaledBlankStarImageIcon);
                                starButton5.setIcon(scaledBlankStarImageIcon);
                                numStars[0] = 0;
                            } else {
                                starButton1.setIcon(scaledFilledStarImageIcon);
                                starButton2.setIcon(scaledBlankStarImageIcon);
                                starButton3.setIcon(scaledBlankStarImageIcon);
                                starButton4.setIcon(scaledBlankStarImageIcon);
                                starButton5.setIcon(scaledBlankStarImageIcon);
                                numStars[0] = 1;
                            }
                        }
                );
                starButton2.addActionListener(
                        actionEvent -> {
                            if (starButton1.getIcon().equals(scaledFilledStarImageIcon) &&
                                    starButton2.getIcon().equals(scaledFilledStarImageIcon) &&
                                    starButton3.getIcon().equals(scaledBlankStarImageIcon) &&
                                    starButton4.getIcon().equals(scaledBlankStarImageIcon) &&
                                    starButton5.getIcon().equals(scaledBlankStarImageIcon)) {
                                starButton1.setIcon(scaledBlankStarImageIcon);
                                starButton2.setIcon(scaledBlankStarImageIcon);
                                starButton3.setIcon(scaledBlankStarImageIcon);
                                starButton4.setIcon(scaledBlankStarImageIcon);
                                starButton5.setIcon(scaledBlankStarImageIcon);
                                numStars[0] = 0;
                            } else {
                                starButton1.setIcon(scaledFilledStarImageIcon);
                                starButton2.setIcon(scaledFilledStarImageIcon);
                                starButton3.setIcon(scaledBlankStarImageIcon);
                                starButton4.setIcon(scaledBlankStarImageIcon);
                                starButton5.setIcon(scaledBlankStarImageIcon);
                                numStars[0] = 2;
                            }
                        }
                );
                starButton3.addActionListener(
                        actionEvent -> {
                            if (starButton1.getIcon().equals(scaledFilledStarImageIcon) &&
                                    starButton2.getIcon().equals(scaledFilledStarImageIcon) &&
                                    starButton3.getIcon().equals(scaledFilledStarImageIcon) &&
                                    starButton4.getIcon().equals(scaledBlankStarImageIcon) &&
                                    starButton5.getIcon().equals(scaledBlankStarImageIcon)) {
                                starButton1.setIcon(scaledBlankStarImageIcon);
                                starButton2.setIcon(scaledBlankStarImageIcon);
                                starButton3.setIcon(scaledBlankStarImageIcon);
                                starButton4.setIcon(scaledBlankStarImageIcon);
                                starButton5.setIcon(scaledBlankStarImageIcon);
                                numStars[0] = 0;
                            } else {
                                starButton1.setIcon(scaledFilledStarImageIcon);
                                starButton2.setIcon(scaledFilledStarImageIcon);
                                starButton3.setIcon(scaledFilledStarImageIcon);
                                starButton4.setIcon(scaledBlankStarImageIcon);
                                starButton5.setIcon(scaledBlankStarImageIcon);
                                numStars[0] = 3;
                            }
                        }
                );
                starButton4.addActionListener(
                        actionEvent -> {
                            if (starButton1.getIcon().equals(scaledFilledStarImageIcon) &&
                                    starButton2.getIcon().equals(scaledFilledStarImageIcon) &&
                                    starButton3.getIcon().equals(scaledFilledStarImageIcon) &&
                                    starButton4.getIcon().equals(scaledFilledStarImageIcon) &&
                                    starButton5.getIcon().equals(scaledBlankStarImageIcon)) {
                                starButton1.setIcon(scaledBlankStarImageIcon);
                                starButton2.setIcon(scaledBlankStarImageIcon);
                                starButton3.setIcon(scaledBlankStarImageIcon);
                                starButton4.setIcon(scaledBlankStarImageIcon);
                                starButton5.setIcon(scaledBlankStarImageIcon);
                                numStars[0] = 0;
                            } else {
                                starButton1.setIcon(scaledFilledStarImageIcon);
                                starButton2.setIcon(scaledFilledStarImageIcon);
                                starButton3.setIcon(scaledFilledStarImageIcon);
                                starButton4.setIcon(scaledFilledStarImageIcon);
                                starButton5.setIcon(scaledBlankStarImageIcon);
                                numStars[0] = 4;
                            }
                        }
                );
                starButton5.addActionListener(
                        actionEvent -> {
                            if (starButton1.getIcon().equals(scaledFilledStarImageIcon) &&
                                    starButton2.getIcon().equals(scaledFilledStarImageIcon) &&
                                    starButton3.getIcon().equals(scaledFilledStarImageIcon) &&
                                    starButton4.getIcon().equals(scaledFilledStarImageIcon) &&
                                    starButton5.getIcon().equals(scaledFilledStarImageIcon)) {
                                starButton1.setIcon(scaledBlankStarImageIcon);
                                starButton2.setIcon(scaledBlankStarImageIcon);
                                starButton3.setIcon(scaledBlankStarImageIcon);
                                starButton4.setIcon(scaledBlankStarImageIcon);
                                starButton5.setIcon(scaledBlankStarImageIcon);
                                numStars[0] = 0;
                            } else {
                                starButton1.setIcon(scaledFilledStarImageIcon);
                                starButton2.setIcon(scaledFilledStarImageIcon);
                                starButton3.setIcon(scaledFilledStarImageIcon);
                                starButton4.setIcon(scaledFilledStarImageIcon);
                                starButton5.setIcon(scaledFilledStarImageIcon);
                                numStars[0] = 5;
                            }
                        }
                );
                goBackFromFilterWithEsc(starButton1);
                goBackFromFilterWithEsc(starButton2);
                goBackFromFilterWithEsc(starButton3);
                goBackFromFilterWithEsc(starButton4);
                goBackFromFilterWithEsc(starButton5);

                shippingDurationLabel.setBounds(320, 95, 150, 30);
                shippingDurationComboBox.addItem("Ninguna selección");
                shippingDurationComboBox.addItem("1 Día");
                shippingDurationComboBox.addItem("2 Días");
                shippingDurationComboBox.addItem("3 Días");
                shippingDurationComboBox.addItem("4 Días");
                shippingDurationComboBox.setBounds(300, 125, 145, 30);
                goBackFromFilterWithEsc(shippingDurationComboBox);

                separator.setOrientation(SwingConstants.HORIZONTAL);
                separator.setBounds(0, 175, 500, 400);

                storableSearchLabel.setBounds(180, 180, 150, 30);
                storableSearchButton1.setBounds(10, 210, 465, 30);
                storableSearchButton2.setBounds(10, 245, 465, 30);
                storableSearchButton3.setBounds(10, 280, 465, 30);
                getStorableSearch();
                storableSearchButton1.addActionListener(
                        actionEvent -> {
                            //ir a la información del producto
                            filterPopUp.dispose();
                        }
                );
                storableSearchButton2.addActionListener(
                        actionEvent -> {
                            //ir a la información del producto
                            filterPopUp.dispose();
                        }
                );
                storableSearchButton3.addActionListener(
                        actionEvent -> {
                            //ir a la información del producto
                            filterPopUp.dispose();
                        }
                );

                okButton.setBounds(395, 325, 80, 30);
                okButton.addActionListener(
                    actionEvent -> {
                        double minPrice = 0.0;
                        double maxPrice = 0.0;
                        greaterOrlowerPriceErrorLabel.setVisible(false);
                        try {
                            minPrice = Double.parseDouble(minPriceTextField.getText());
                            minPriceErrorLabel.setVisible(false);
                        } catch (NumberFormatException nF) {
                            if (minPriceTextField.getText().trim().equals("")) {
                                minPrice = 0.0;
                            } else {
                                if (!(minPriceErrorLabel.isVisible() || maxPriceErrorLabel.isVisible())) {
                                    minPriceErrorLabel.setVisible(true);
                                }
                            }
                        }
                        try {
                            maxPrice = Double.parseDouble(maxPriceTextField.getText());
                            maxPriceErrorLabel.setVisible(false);
                        } catch (NumberFormatException nF) {
                            if (maxPriceTextField.getText().trim().equals("")) {
                                minPrice = 0.0;
                            } else {
                                if (!(minPriceErrorLabel.isVisible() || maxPriceErrorLabel.isVisible())) {
                                    maxPriceErrorLabel.setVisible(true);
                                }
                            }
                        }

                        if (minPrice > maxPrice) {
                            greaterOrlowerPriceErrorLabel.setVisible(true);
                        }

                        if (ascendingToggleButton.isSelected()) {
                            getProductsSortedByAscendingCategory();
                        } else if (descendingToggleButton.isSelected()) {
                            getProductsSortedByDescendingCategory();
                        }

                        getProductsSortedByPrice();
                        //sortByAssesment(products, numStars[0]);
                        //sortByShippingDuration(products, shippingDurationComboBox.getSelectedIndex());

                        if (!(minPriceErrorLabel.isVisible() || maxPriceErrorLabel.isVisible() ||
                                greaterOrlowerPriceErrorLabel.isVisible())) {
                            filterPopUp.dispose();
                        }

                    }
                );

                filterPopUp.setVisible(true);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                filtroButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                filtroButton.setBackground(new Color(73, 231, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                filtroButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                filtroButton.setBackground(new Color(153, 233, 255));
            }
        });
        carritoCompraButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                goCarrito(tipo, id);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                carritoCompraButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                carritoCompraButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        perfilButton.setBackground(new Color(153, 233, 255));
        perfilButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                perfilButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                perfilButton.setBackground(new Color(73, 231, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                perfilButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                perfilButton.setBackground(new Color(153, 233, 255));
            }

        });
    }

    public static void main(String[] args) {
        CatalogoProductos catalogoProductos = new CatalogoProductos(0, 0, "");
        catalogoProductos.setMain();
    }

    public void setMain() {
        CatalogoProductos ventanaCatalogo = new CatalogoProductos(tipo, id, nombre);
        JFrame frame = new JFrame("Smart Trade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(ventanaCatalogo.panelCatalogo);
        frame.pack();
        frame.setVisible(true);
    }

    public JPanel getPanel() {
        return this.panelCatalogo;
    }

    public void goToValidated(){
       VendedorProductosValidos ventanaVendedor = new VendedorProductosValidos(id, tipo, nombre);
        JFrame frame = new JFrame("Smart Trade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(ventanaVendedor.getPanel());
        frame.pack();
        frame.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }

    public void goCarrito(int t, int i){
        CarritoCompra ventanaCarrito = new CarritoCompra(t, i, nombre);
        JFrame frame = new JFrame("Smart Trade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(ventanaCarrito.getPanel());
        frame.pack();
        frame.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }

    public void backMenu(int param) {
        CatalogoProductos ventanaCatalog = new CatalogoProductos(param, id, nombre);
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaCatalog.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }
    public void goFavs(int tipo, int id){
        ListaDeseos ventanaFav = new ListaDeseos(tipo, id, nombre);
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaFav.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }

    public void goGifts(int tipo, int id){
        ListaRegalos ventanaRegalos = new ListaRegalos( tipo, id, nombre);
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaRegalos.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }
    public void sellProduct() {
        VentaProducto ventanaVenta = new VentaProducto(getUserData(), tipo, id, nombre);
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaVenta.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }

    public void infoProduct(String nombre, Double price, String descripcion,  String category, byte[] img) {

        System.out.println("*************************************");
        System.out.println("Nombre: " + nombre);
        System.out.println("Precio: " + price);
        System.out.println("Categoria: " + category);
        System.out.println("Descripción: " + descripcion);

        InfoProducto ventanaInfo = new InfoProducto(nombre, price, category, descripcion, id, tipo, img, nombre);
        JFrame ventanaAtras = new JFrame("Smart Trade");
        ventanaAtras.setContentPane(ventanaInfo.getPanel());
        ventanaAtras.pack();
        ventanaAtras.setVisible(true);
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        ventanaActual.dispose();
    }

    private ImageIcon imageIconUpdate(ImageIcon img){
        Image originalIMG = img.getImage();

        Image nuevaIMG = originalIMG.getScaledInstance(30, 30, Image.SCALE_SMOOTH);

        ImageIcon nuevoIcono = new ImageIcon(nuevaIMG);

        return nuevoIcono;
    }


    private void getProducts() {
        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/product/validated"))
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            if (response.statusCode() == 200) {
                JsonNode jsonResponse = objectMapper.readTree(responseBody);
                if (jsonResponse.isArray()) {
                    for (JsonNode productNode : jsonResponse) {
                        prodName = productNode.get("name").asText();
                        prodPrice = productNode.get("price").asDouble();
                        prodDescription = productNode.get("description").asText();
                        prodType = productNode.get("type").asText();
                        img = productNode.get("image").binaryValue();

                        agregarProducto(prodName, prodPrice, prodDescription, prodType, img);
                    }
                }
            } else {
                System.out.println("Error: " + response.statusCode());
                System.out.println(response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void agregarProducto(String nombre, Double precio, String descripcion, String type, byte[] img) {
        JPanel panelProducto = new JPanel(new BorderLayout());
        panelProducto.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panelProducto.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));



        JLabel labelNombre = new JLabel(nombre);
        labelNombre.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel labelImg = new JLabel();
        labelImg.setHorizontalAlignment(SwingConstants.CENTER);

        ImageIcon originalIcon = new ImageIcon(img);
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        labelImg.setIcon(resizedIcon);

        JLabel labelPrecio = new JLabel(precio + "€");
        JLabel labelDescripcion = new JLabel("<html>" + descripcion + "</html>");

        JPanel panelSec = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        panelSec.add(labelNombre, gbc);
        panelSec.add(labelDescripcion, gbc);
        panelSec.add(labelPrecio, gbc);

        panelProducto.add(labelImg, BorderLayout.NORTH);
        panelProducto.add(panelSec, BorderLayout.CENTER);

        panelProductos.add(panelProducto);
        panelProductos.revalidate();
        panelProductos.repaint();

        panelProducto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                infoProduct(nombre, precio, descripcion, type, img);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                panelProducto.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panelProducto.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    public void getProductsSortedByPrice() {
        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/search/price"))
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            if (response.statusCode() == 200) {
                JsonNode jsonResponse = objectMapper.readTree(responseBody);
                if (jsonResponse.isArray()) {
                    for (JsonNode productNode : jsonResponse) {
                        prodName = productNode.get("name").asText();
                        prodPrice = productNode.get("price").asDouble();
                        prodDescription = productNode.get("description").asText();
                        prodType = productNode.get("type").asText();
                        agregarProducto(prodName, prodPrice, prodDescription, prodType, img);
                    }
                }
            } else {
                System.out.println("Error: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getProductsSortedByAscendingCategory() {
        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/search/ascending"))
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            if (response.statusCode() == 200) {
                JsonNode jsonResponse = objectMapper.readTree(responseBody);
                if (jsonResponse.isArray()) {
                    for (JsonNode productNode : jsonResponse) {
                        prodName = productNode.get("name").asText();
                        prodPrice = productNode.get("price").asDouble();
                        prodDescription = productNode.get("description").asText();
                        prodType = productNode.get("type").asText();
                        agregarProducto(prodName, prodPrice, prodDescription, prodType, img);
                    }
                }
            } else {
                System.out.println("Error: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getProductsSortedByDescendingCategory() {
        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/search/descending"))
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            if (response.statusCode() == 200) {
                JsonNode jsonResponse = objectMapper.readTree(responseBody);
                if (jsonResponse.isArray()) {
                    for (JsonNode productNode : jsonResponse) {
                        prodName = productNode.get("name").asText();
                        prodPrice = productNode.get("price").asDouble();
                        prodDescription = productNode.get("description").asText();
                        prodType = productNode.get("type").asText();
                        agregarProducto(prodName, prodPrice, prodDescription, prodType, img);
                    }
                }
            } else {
                System.out.println("Error: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getStorableSearch() {
        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/search/storable"))
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            if (response.statusCode() == 200) {
                JsonNode jsonResponse = objectMapper.readTree(responseBody);
                if (jsonResponse.isArray()) {
                    for (JsonNode productNode : jsonResponse) {
                        //prodName = productNode.get("name").asText();
                        if (jsonResponse.size() == 1) {
                            storableSearchButtonText[0] = productNode.get(0).get("name").asText();
                            storableSearchButton1.setText(storableSearchButtonText[0]);
                        }
                        if (jsonResponse.size() == 2) {
                            storableSearchButtonText[1] = productNode.get(0).get("name").asText();
                            storableSearchButton2.setText(storableSearchButtonText[1]);
                            storableSearchButtonText[0] = productNode.get(1).get("name").asText();
                            storableSearchButton1.setText(storableSearchButtonText[0]);
                        }
                        if (jsonResponse.size() == 3) {
                            storableSearchButtonText[2] = productNode.get(0).get("name").asText();
                            storableSearchButton3.setText(storableSearchButtonText[2]);
                            storableSearchButtonText[1] = productNode.get(1).get("name").asText();
                            storableSearchButton2.setText(storableSearchButtonText[1]);
                            storableSearchButtonText[0] = productNode.get(2).get("name").asText();
                            storableSearchButton1.setText(storableSearchButtonText[0]);
                        }
                    }
                }
            } else {
                System.out.println("Error: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addObserver(ObserverUserData observer) {
    }

    @Override
    public void removeObserver(ObserverUserData observer) {
    }

    @Override
    public void notifyObservers(String[] data) {
    }

    @Override
    public void update(String[] data) {
        name = data[0];
        email = data[1];
        password = data[2];
        type = data[3];
        iban = data[4];
        cif = data[5];
        dni = data[6];
        city = data[7];
        street = data[8];
        door = data[9];
        flat = data[10];
        num = data[11];
    }

    public static String[] getUserData() {
        return new String[]{name, email, password, type, iban, cif, dni, city, street, door, flat, num};
    }

    private void inicializarComponentes(ImageIcon fav, ImageIcon user) {
        panelCatalogo = new JPanel();
        panelInfo = new JPanel();
        panelListado = new JPanel();
        panelProductos = new JPanel();
        panelProductos.setLayout(new GridLayout(0, 3, 10, 10));
        ventaProducto = new JButton("Vender Producto");
        ventasValidas = new JButton("Mis productos validados");
        ventaProducto.setBackground(new Color(153, 233, 255));
        ventasValidas.setBackground(new Color(153, 233, 255));
        ventaProducto.setPreferredSize(new Dimension(150, 30));
        ventasValidas.setPreferredSize(new Dimension(250, 30));
        JPanel panelVenta = new JPanel();
        panelVenta.setLayout(new FlowLayout(FlowLayout.CENTER));

        perfilButton = new JComboBox<>(new Object[]{"Lista de regalos", "Lista de deseados"});
        perfilButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedValue = (String) perfilButton.getSelectedItem();
                if (selectedValue.equals("Lista de deseados")) {
                    goFavs(tipo, id);
                } else if (selectedValue.equals("Lista de regalos")) {
                    goGifts(tipo, id);
                }
            }
        });

        getContentPane().add(perfilButton);

    }

    private void organizarInterfaz(int param) {

        searchTF.setPreferredSize(new Dimension(120,20));

        panelCatalogo.setLayout(new BorderLayout());

        panelInfo.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.1;
        panelInfo.add(logoButton, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.9;
        panelInfo.add(searchTF, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.1;
        panelInfo.add(lupaButton, gbc);

        gbc.gridx = 3;
        gbc.weightx = 0.1;
        panelInfo.add(filtroButton, gbc);

        gbc.gridx = 4;
        gbc.weightx = 0.1;
        panelInfo.add(carritoCompraButton, gbc);

        gbc.gridx = 5;
        gbc.weightx = 0.1;
        panelInfo.add(perfilButton, gbc);

        panelInfo.setPreferredSize(new Dimension(800, 120));
        panelInfo.setBackground(new Color(183, 183, 183));

        panelListado.setLayout(new BorderLayout());
        panelListado.setBackground(new Color(198, 232, 251));
        panelListado.setPreferredSize(new Dimension(800, 480));

        panelProductos.setLayout(new GridLayout(0, 3, 10, 10));
        panelProductos.setBackground(new Color(198, 232, 251));
        JScrollPane scrollPane = new JScrollPane(panelProductos);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panelListado.add(scrollPane, BorderLayout.CENTER);

        panelCatalogo.add(panelInfo, BorderLayout.NORTH);
        panelCatalogo.add(panelListado, BorderLayout.CENTER);

        if (param == 2) {
            JPanel bottomPanel = new JPanel();
            bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            bottomPanel.setBackground(new Color(198, 232, 251));

            bottomPanel.add(ventaProducto);
            bottomPanel.add(ventasValidas);

            panelCatalogo.add(bottomPanel, BorderLayout.SOUTH);
        }

    }

    public void sendEmail(int identificador, String nombre) {
        String url = "http://localhost:8080/user/email?user_id=" + identificador;
        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            String responseBody = response.body();
            System.out.println(responseBody + " el código es: " + statusCode);
            System.out.println("El nombre es: " + nombre);
            if (statusCode == 200) {
                EmailSender.enviarCorreo(responseBody,nombre);
                JOptionPane.showMessageDialog(null, "El correo se ha enviado con éxito");
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "El correo no se ha podido enviar con éxito");
        }
    }
                public void goBackFromFilterWithEsc(JComponent component){
        component.addKeyListener(
                new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                            filterPopUp.dispose();
                        }
                    }
                }
        );
    }
}

