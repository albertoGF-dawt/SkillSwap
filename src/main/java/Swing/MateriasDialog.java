package Swing;

import DAO.SubjectDAO;
import Model.Subject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MateriasDialog extends JDialog {

    private final SubjectDAO subjectDAO = new SubjectDAO();

    private JTextField campoNombre;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private Integer idSeleccionado = null;

    //se ajusta el Jframe
    public MateriasDialog(JFrame parent) {
        super(parent, "Gestión de Materias", true);
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(crearTabla(),           BorderLayout.CENTER);
        add(crearPanelBotones(),    BorderLayout.SOUTH);

        cargarMaterias();
        setVisible(true);
    }

    //panel de formulario
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Datos de la Materia"));
        panel.setPreferredSize(new Dimension(500, 60));

        panel.add(new JLabel("Nombre:"));
        campoNombre = new JTextField();
        panel.add(campoNombre);

        return panel;
    }
    //columnas de ID y nombre de las materias
    private JScrollPane crearTabla() {
        String[] columnas = {"ID", "Nombre"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) rellenarFormularioDesdeTabla();
        });

        return new JScrollPane(tabla);
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton btnNuevo   = new JButton("Nuevo");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnCerrar  = new JButton("Cerrar");

        btnNuevo.addActionListener(e -> limpiarFormulario());
        btnGuardar.addActionListener(e -> guardarMateria());
        btnEliminar.addActionListener(e -> eliminarMateria());
        btnCerrar.addActionListener(e -> dispose());

        panel.add(btnNuevo);
        panel.add(btnGuardar);
        panel.add(btnEliminar);
        panel.add(btnCerrar);

        return panel;
    }

    //método que llama al DAO
    private void cargarMaterias() {
        modeloTabla.setRowCount(0);
        List<Subject> subjects = subjectDAO.getSubjects();
        for (Subject s : subjects) {
            modeloTabla.addRow(new Object[]{s.getId(), s.getNombre()});
        }
    }

    //las validaciones se hacen en subjectDAO porque no hay un serviceSubject y sería una tontería hacer uno para una validación
    private void guardarMateria() {
        String nombre = campoNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (subjectDAO.existeNombre(nombre)) {
            JOptionPane.showMessageDialog(this, "Ya existe una materia con ese nombre.", "Duplicado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Subject subject = new Subject();
        subject.setNombre(nombre);

        try {
            subjectDAO.create(subject);
            JOptionPane.showMessageDialog(this, "Materia creada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarMaterias();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarMateria() {
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una materia de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (subjectDAO.tieneTutorias(idSeleccionado)) {
            JOptionPane.showMessageDialog(this,
                    "No se puede eliminar esta materia porque tiene tutorías asociadas.",
                    "No se puede eliminar", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres eliminar esta materia?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                subjectDAO.delete(idSeleccionado);
                JOptionPane.showMessageDialog(this, "Materia eliminada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarMaterias();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //recupera los datos de la materia al hacerle click en la tabla
    private void rellenarFormularioDesdeTabla() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return;
        idSeleccionado = (Integer) modeloTabla.getValueAt(fila, 0);
        campoNombre.setText((String) modeloTabla.getValueAt(fila, 1));
    }

    private void limpiarFormulario() {
        idSeleccionado = null;
        campoNombre.setText("");
        tabla.clearSelection();
    }
}