package Swing;

import Model.User;
import Service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UsuariosDialog extends JDialog {

    private final UserService userService = new UserService();

    // Campos del formulario
    private JTextField campoNombre;
    private JTextField campoEmail;
    private JPasswordField campoPassword;
    private JComboBox<String> comboRol;
    private JTextField campoCiclo;

    // Tabla
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    // ID del usuario seleccionado (para editar/eliminar)
    private Integer idSeleccionado = null;

    public UsuariosDialog(JFrame parent) {
        super(parent, "Gestión de Usuarios", true);
        setSize(750, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(crearTabla(),           BorderLayout.CENTER);
        add(crearPanelBotones(),    BorderLayout.SOUTH);

        cargarUsuarios(); // carga datos reales al abrir
        setVisible(true);
    }
    //crea el Jpanel
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(3, 4, 8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Usuario"));
        panel.setPreferredSize(new Dimension(750, 120));

        panel.add(new JLabel("Nombre:"));
        campoNombre = new JTextField();
        panel.add(campoNombre);

        panel.add(new JLabel("Email:"));
        campoEmail = new JTextField();
        panel.add(campoEmail);

        panel.add(new JLabel("Contraseña:"));
        campoPassword = new JPasswordField();
        panel.add(campoPassword);

        panel.add(new JLabel("Rol:"));
        comboRol = new JComboBox<>(new String[]{"alumno", "mentor"});
        panel.add(comboRol);

        panel.add(new JLabel("Ciclo:"));
        campoCiclo = new JTextField();
        panel.add(campoCiclo);

        panel.add(new JLabel());
        panel.add(new JLabel());

        return panel;
    }

    private JScrollPane crearTabla() {
        String[] columnas = {"ID", "Nombre", "Email", "Rol", "Ciclo"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // no editable directamente
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                rellenarFormularioDesdeTabla();
            }
        });

        return new JScrollPane(tabla);
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton btnNuevo    = new JButton("Nuevo");
        JButton btnGuardar  = new JButton("Guardar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnCerrar   = new JButton("Cerrar");

        btnNuevo.addActionListener(e -> limpiarFormulario());
        btnGuardar.addActionListener(e -> crearUsuario());
        btnActualizar.addActionListener(e -> actualizarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());
        btnCerrar.addActionListener(e -> dispose());

        panel.add(btnNuevo);
        panel.add(btnGuardar);
        panel.add(btnActualizar);
        panel.add(btnEliminar);
        panel.add(btnCerrar);

        return panel;
    }

    private void cargarUsuarios() {
        modeloTabla.setRowCount(0); // limpia la tabla
        List<User> usuarios = userService.getAllUsers();
        for (User u : usuarios) {
            modeloTabla.addRow(new Object[]{
                    u.getId(), u.getNombre(), u.getEmail(), u.getRol(), u.getCiclo()
            });
        }
    }

    private void crearUsuario() {
        if (!validarCampos()) return;

        User user = new User(
                campoNombre.getText().trim(),
                campoEmail.getText().trim(),
                new String(campoPassword.getPassword()).trim(),
                (String) comboRol.getSelectedItem(),
                campoCiclo.getText().trim()
        );

        try {
            userService.createUser(user);
            JOptionPane.showMessageDialog(this, "Usuario creado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarUsuarios();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al crear el usuario:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarUsuario() {
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validarCampos()) return;

        User user = new User(
                campoNombre.getText().trim(),
                campoEmail.getText().trim(),
                new String(campoPassword.getPassword()).trim(),
                (String) comboRol.getSelectedItem(),
                campoCiclo.getText().trim()
        );

        try {
            userService.updateUser(idSeleccionado, user);
            JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarUsuarios();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarUsuario() {
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres eliminar este usuario?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                userService.deleteUser(idSeleccionado);
                JOptionPane.showMessageDialog(this, "Usuario eliminado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarUsuarios();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void rellenarFormularioDesdeTabla() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return;

        idSeleccionado = (Integer) modeloTabla.getValueAt(fila, 0);
        campoNombre.setText((String) modeloTabla.getValueAt(fila, 1));
        campoEmail.setText((String) modeloTabla.getValueAt(fila, 2));
        comboRol.setSelectedItem(modeloTabla.getValueAt(fila, 3));
        campoCiclo.setText((String) modeloTabla.getValueAt(fila, 4));
        campoPassword.setText(""); // la contraseña no se muestra por seguridad
    }

    private void limpiarFormulario() {
        idSeleccionado = null;
        campoNombre.setText("");
        campoEmail.setText("");
        campoPassword.setText("");
        comboRol.setSelectedIndex(0);
        campoCiclo.setText("");
        tabla.clearSelection();
    }

    private boolean validarCampos() {
        if (campoNombre.getText().trim().isEmpty() ||
                campoEmail.getText().trim().isEmpty() ||
                new String(campoPassword.getPassword()).trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nombre, email y contraseña son obligatorios.",
                    "Campos vacíos",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}