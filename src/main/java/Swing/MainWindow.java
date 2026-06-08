package Swing;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    public MainWindow() {
        // Configuración básica del JFrame
        setTitle("Sistema de Tutorías");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Centrar en pantalla
        setLocationRelativeTo(null);

        // Crear y añadir el menú
        setJMenuBar(crearMenuBar());

        //modificas el fondo del panel
        JPanel panelCentral = new JPanel(new GridLayout(3, 1)) {
            private final Image imagen = new ImageIcon(
                    "src/main/java/Swing/Images/dab809b4-d9d4-4e7e-b8d4-aee75e6bdf52.png").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            }
        };
        //mensajes del Jframe
        panelCentral.setOpaque(false);
        JLabel bienvenida  = new JLabel("Te damos la bienvenida a Skillswap", SwingConstants.CENTER);
        JLabel descripcion = new JLabel("Elige qué opción quieres elegir en el menú superior.", SwingConstants.CENTER);
        JLabel gracias     = new JLabel("Gracias por usar nuestros servicios.", SwingConstants.CENTER);

        bienvenida.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 40));
        descripcion.setFont(new Font("Arial", Font.BOLD, 20));
        gracias.setFont(new Font("Arial", Font.BOLD, 14));

        panelCentral.add(bienvenida);
        panelCentral.add(descripcion);
        panelCentral.add(gracias, BorderLayout.SOUTH);

        add(panelCentral);

        setVisible(true);
    }

    private JMenuBar crearMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // menú Usuarios
        JMenu menuUsuarios = new JMenu("Usuarios");

        JMenuItem itemUsuarios = new JMenuItem("Gestionar Usuarios");
        itemUsuarios.addActionListener(e -> new UsuariosDialog(this));
        menuUsuarios.add(itemUsuarios);

        // menú de Materias
        JMenu menuMaterias = new JMenu("Materias");
        JMenuItem itemMaterias = new JMenuItem("Gestionar Materias");
        itemMaterias.addActionListener(e -> new MateriasDialog(this));
        menuMaterias.add(itemMaterias);

        // menú de Tutorías
        JMenu menuTutorias = new JMenu("Tutorías");
        JMenuItem itemTutorias = new JMenuItem("Gestionar Tutorías");
        itemTutorias.addActionListener(e -> new TutoriasDialog(this));
        menuTutorias.add(itemTutorias);

        // menú de Reservas
        JMenu menuReservas = new JMenu("Reservas");
        JMenuItem itemReservas = new JMenuItem("Gestionar Reservas");
        itemReservas.addActionListener(e -> new ReservasDialog(this));
        menuReservas.add(itemReservas);

        // menú de Consultas
        JMenu menuConsultas = new JMenu("Consultas");
        JMenuItem itemConsultas = new JMenuItem("Ver Consultas");
        itemConsultas.addActionListener(e -> new ConsultasDialog(this));
        menuConsultas.add(itemConsultas);

        menuBar.add(menuConsultas);

        // menú de Salir
        JMenu menuSalir = new JMenu("Salir");
        JMenuItem itemSalir = new JMenuItem("Cerrar aplicación");
        itemSalir.addActionListener(e -> System.exit(0));
        menuSalir.add(itemSalir);

        menuBar.add(menuUsuarios);
        menuBar.add(menuMaterias);
        menuBar.add(menuTutorias);
        menuBar.add(menuReservas);
        menuBar.add(menuSalir);

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // fondo de la barra superior de menus/Dialogs
        menuBar.setBackground(new Color(190, 149, 223));
        menuBar.setOpaque(true);
        return menuBar;
    }
}