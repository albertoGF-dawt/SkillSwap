package Swing;

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
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));

        JLabel bienvenida = new JLabel("Te damos la bienvenida a Skillswap");
        JLabel descripcion = new JLabel("Elige qué opción quieres elegir en el menú superior.");
        JLabel gracias = new JLabel("Gracias por usar nuestros servicios.");

        bienvenida.setFont(new Font("Comfortaa", Font.BOLD, 24));
        descripcion.setFont(new Font("Comfortaa", Font.BOLD, 18));
        gracias.setFont(new Font("Comfortaa", Font.BOLD, 14));

// Centrar cada label horizontalmente dentro del BoxLayout
        bienvenida.setAlignmentX(Component.CENTER_ALIGNMENT);
        descripcion.setAlignmentX(Component.CENTER_ALIGNMENT);
        gracias.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelCentral.add(Box.createVerticalGlue()); // empuja hacia el centro
        panelCentral.add(bienvenida);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 12))); // espaciado
        panelCentral.add(descripcion);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 8)));
        panelCentral.add(gracias);
        panelCentral.add(Box.createVerticalGlue()); // empuja desde abajo

        add(panelCentral);

        setVisible(true); // Siempre al final
    }

    private JMenuBar crearMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // ── Usuarios ──
        JMenu menuUsuarios = new JMenu("Usuarios");

        JMenuItem itemUsuarios = new JMenuItem("Gestionar Usuarios");
        itemUsuarios.addActionListener(e -> new UsuariosDialog(this));
        menuUsuarios.add(itemUsuarios);

        // ── Materias ──
        JMenu menuMaterias = new JMenu("Materias");
        JMenuItem itemMaterias = new JMenuItem("Gestionar Materias");
        itemMaterias.addActionListener(e -> new MateriasDialog(this));
        menuMaterias.add(itemMaterias);

        // ── Tutorías ──
        JMenu menuTutorias = new JMenu("Tutorías");
        JMenuItem itemTutorias = new JMenuItem("Gestionar Tutorías");
        itemTutorias.addActionListener(e -> new TutoriasDialog(this));
        menuTutorias.add(itemTutorias);

        // ── Reservas ──
        JMenu menuReservas = new JMenu("Reservas");
        JMenuItem itemReservas = new JMenuItem("Gestionar Reservas");
        itemReservas.addActionListener(e -> new ReservasDialog(this));
        menuReservas.add(itemReservas);

        // ── Consultas ──
        JMenu menuConsultas = new JMenu("Consultas");
        JMenuItem itemConsultas = new JMenuItem("Ver Consultas");
        itemConsultas.addActionListener(e -> new ConsultasDialog(this));
        menuConsultas.add(itemConsultas);

        menuBar.add(menuConsultas); // antes del de Salir

        // ── Salir ──
        JMenu menuSalir = new JMenu("Salir");
        JMenuItem itemSalir = new JMenuItem("Cerrar aplicación");
        itemSalir.addActionListener(e -> System.exit(0));
        menuSalir.add(itemSalir);

        menuBar.add(menuUsuarios);
        menuBar.add(menuMaterias);
        menuBar.add(menuTutorias);
        menuBar.add(menuReservas);
        menuBar.add(menuSalir);
        return menuBar;
    }
}