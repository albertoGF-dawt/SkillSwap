package Swing;

// MainWindow.java
import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    public MainWindow() {
        // Configuración básica del JFrame
        setTitle("Sistema de Tutorías");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla

        // Crear y añadir el menú
        setJMenuBar(crearMenuBar());

        // Panel central de bienvenida
        JPanel panelCentral = new JPanel(new BorderLayout());
        JLabel bienvenida = new JLabel("Te damos la bienvenida a Skillswap", SwingConstants.CENTER);
        bienvenida.setFont(new Font("Comfortaa", Font.BOLD, 24));
        panelCentral.add(bienvenida, BorderLayout.CENTER);
        add(panelCentral);

        setVisible(true); // Siempre al final
    }

    private JMenuBar crearMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Menú "Usuarios"
        JMenu menuUsuarios = new JMenu("Usuarios");
        JMenuItem itemGestionar = new JMenuItem("Gestionar Usuarios");
        itemGestionar.addActionListener(e -> new UsuariosDialog(this));
        menuUsuarios.add(itemGestionar);

        // Menú "Salir"
        JMenu menuSalir = new JMenu("Salir");
        JMenuItem itemSalir = new JMenuItem("Cerrar aplicación");
        itemSalir.addActionListener(e -> System.exit(0));
        menuSalir.add(itemSalir);

        menuBar.add(menuUsuarios);
        menuBar.add(menuSalir);
        return menuBar;
    }
}