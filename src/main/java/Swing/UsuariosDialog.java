package Swing;

// UsuariosDialog.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UsuariosDialog extends JDialog {

    private JTextField campoNombre;
    private JTextField campoEmail;
    private DefaultTableModel modeloTabla;

    public UsuariosDialog(JFrame parent) {
        super(parent, "Gestión de Usuarios", true); // true = modal (bloquea la ventana padre)
        setSize(600, 400);
        setLocationRelativeTo(parent);

        // Layout principal
        setLayout(new BorderLayout(10, 10));

        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(crearTabla(),           BorderLayout.CENTER);
        add(crearPanelBotones(),    BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Usuario"));

        panel.add(new JLabel("Nombre:"));
        campoNombre = new JTextField();
        panel.add(campoNombre);

        panel.add(new JLabel("Email:"));
        campoEmail = new JTextField();
        panel.add(campoEmail);

        return panel;
    }

    private JScrollPane crearTabla() {
        String[] columnas = {"ID", "Nombre", "Email"};
        modeloTabla = new DefaultTableModel(columnas, 0); // 0 filas iniciales
        JTable tabla = new JTable(modeloTabla);
        return new JScrollPane(tabla);
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardarUsuario());

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose()); // Cierra solo este diálogo

        panel.add(btnGuardar);
        panel.add(btnCerrar);
        return panel;
    }

    private void guardarUsuario() {
        // Validación de datos
        String nombre = campoNombre.getText().trim();
        String email  = campoEmail.getText().trim();

        if (nombre.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Todos los campos son obligatorios",
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Añadir fila a la tabla
        modeloTabla.addRow(new Object[]{
                modeloTabla.getRowCount() + 1, nombre, email
        });

        // Limpiar campos
        campoNombre.setText("");
        campoEmail.setText("");
    }
}