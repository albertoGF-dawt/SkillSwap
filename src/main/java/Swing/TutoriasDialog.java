package Swing;

import DAO.SubjectDAO;
import DAO.UserDAO;
import Model.Mentorship;
import Model.Subject;
import Model.User;
import Service.MentorshipService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TutoriasDialog extends JDialog {

    private final MentorshipService mentorshipService = new MentorshipService();
    private final UserDAO userDAO = new UserDAO();
    private final SubjectDAO subjectDAO = new SubjectDAO();

    // Campos del formulario
    private JTextField campoTema;
    private JTextField campoFecha;      // formato yyyy-MM-dd
    private JTextField campoHora;       // formato HH:mm
    private JTextField campoDuracion;
    private JTextField campoLugar;
    private JComboBox<User> comboMentor;
    private JComboBox<Subject> comboMateria;

    // Tabla
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private Integer idSeleccionado = null;

    public TutoriasDialog(JFrame parent) {
        super(parent, "Gestión de Tutorías", true);
        setSize(900, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(crearTabla(),           BorderLayout.CENTER);
        add(crearPanelBotones(),    BorderLayout.SOUTH);

        cargarCombos();
        cargarTutorias();
        setVisible(true);
    }

    // ─────────────────────────────────────────
    // FORMULARIO
    // ─────────────────────────────────────────
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(4, 4, 8, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Datos de la Tutoría"));
        panel.setPreferredSize(new Dimension(900, 150));

        panel.add(new JLabel("Tema:"));
        campoTema = new JTextField();
        panel.add(campoTema);

        panel.add(new JLabel("Fecha (yyyy-MM-dd):"));
        campoFecha = new JTextField();
        panel.add(campoFecha);

        panel.add(new JLabel("Hora (HH:mm):"));
        campoHora = new JTextField();
        panel.add(campoHora);

        panel.add(new JLabel("Duración (min):"));
        campoDuracion = new JTextField();
        panel.add(campoDuracion);

        panel.add(new JLabel("Lugar:"));
        campoLugar = new JTextField();
        panel.add(campoLugar);

        panel.add(new JLabel("Mentor:"));
        comboMentor = new JComboBox<>();
        panel.add(comboMentor);

        panel.add(new JLabel("Materia:"));
        comboMateria = new JComboBox<>();
        panel.add(comboMateria);

        // celda vacía
        panel.add(new JLabel());
        panel.add(new JLabel());

        return panel;
    }

    // ─────────────────────────────────────────
    // TABLA
    // ─────────────────────────────────────────
    private JScrollPane crearTabla() {
        String[] columnas = {"ID", "Tema", "Fecha", "Hora", "Duración", "Lugar", "Mentor", "Materia", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getColumnModel().getColumn(0).setMaxWidth(40);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) rellenarFormularioDesdeTabla();
        });

        return new JScrollPane(tabla);
    }

    // ─────────────────────────────────────────
    // BOTONES
    // ─────────────────────────────────────────
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton btnNuevo    = new JButton("Nuevo");
        JButton btnGuardar  = new JButton("Guardar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnCerrar   = new JButton("Cerrar");

        btnNuevo.addActionListener(e -> limpiarFormulario());
        btnGuardar.addActionListener(e -> crearTutoria());
        btnActualizar.addActionListener(e -> actualizarTutoria());
        btnEliminar.addActionListener(e -> eliminarTutoria());
        btnCerrar.addActionListener(e -> dispose());

        panel.add(btnNuevo);
        panel.add(btnGuardar);
        panel.add(btnActualizar);
        panel.add(btnEliminar);
        panel.add(btnCerrar);

        return panel;
    }

    // ─────────────────────────────────────────
    // LÓGICA CRUD
    // ─────────────────────────────────────────

    private void cargarCombos() {
        // Mentores: usuarios con rol mentor
        comboMentor.removeAllItems();
        List<User> usuarios = userDAO.ListAll();
        for (User u : usuarios) {
            if ("mentor".equals(u.getRol())) {
                comboMentor.addItem(u);
            }
        }

        // Materias
        comboMateria.removeAllItems();
        List<Subject> materias = subjectDAO.getSubjects();
        for (Subject s : materias) {
            comboMateria.addItem(s);
        }
    }

    private void cargarTutorias() {
        modeloTabla.setRowCount(0);
        List<Mentorship> lista = mentorshipService.getAllMentorshipsEager();
        for (Mentorship m : lista) {
            modeloTabla.addRow(new Object[]{
                    m.getId(),
                    m.getTema(),
                    m.getFecha(),
                    m.getHora(),
                    m.getDuracion() + " min",
                    m.getLugar(),
                    m.getCodMentor().getNombre(),
                    m.getCodSubject().getNombre(),
                    m.getEstado()
            });
        }
    }

    private void crearTutoria() {
        Mentorship m = construirMentorshipDesdeFormulario();
        if (m == null) return;

        try {
            mentorshipService.createMentorship(m);
            JOptionPane.showMessageDialog(this, "Tutoría creada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarCombos();
            cargarTutorias();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al crear la tutoría:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void actualizarTutoria() {
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una tutoría de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Mentorship m = construirMentorshipDesdeFormulario();
        if (m == null) return;

        // Preserva el estado original en lugar de sobreescribir con "disponible"
        String estadoActual = modeloTabla.getValueAt(tabla.getSelectedRow(), 8).toString();
        m.setEstado(estadoActual);

        try {
            mentorshipService.updateMentorship(idSeleccionado, m);
            JOptionPane.showMessageDialog(this, "Tutoría actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarTutorias();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarTutoria() {
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una tutoría de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres eliminar esta tutoría?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                mentorshipService.deleteMentorship(idSeleccionado);
                JOptionPane.showMessageDialog(this, "Tutoría eliminada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarTutorias();
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "No se puede eliminar", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    // ─────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────

    /** Construye un objeto Mentorship con los datos del formulario. Devuelve null si hay error de formato. */
    private Mentorship construirMentorshipDesdeFormulario() {
        String tema     = campoTema.getText().trim();
        String fechaStr = campoFecha.getText().trim();
        String horaStr  = campoHora.getText().trim();
        String durStr   = campoDuracion.getText().trim();
        String lugar    = campoLugar.getText().trim();
        User mentor     = (User) comboMentor.getSelectedItem();
        Subject materia = (Subject) comboMateria.getSelectedItem();

        if (tema.isEmpty() || fechaStr.isEmpty() || horaStr.isEmpty() || durStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tema, fecha, hora y duración son obligatorios.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (mentor == null || materia == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un mentor y una materia.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        LocalDate fecha;
        LocalTime hora;
        int duracion;

        try {
            fecha = LocalDate.parse(fechaStr);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Usa yyyy-MM-dd (ej: 2026-06-15)", "Error de formato", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        try {
            hora = LocalTime.parse(horaStr);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de hora incorrecto. Usa HH:mm (ej: 10:30)", "Error de formato", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        try {
            duracion = Integer.parseInt(durStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La duración debe ser un número entero.", "Error de formato", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        Mentorship m = new Mentorship();
        m.setTema(tema);
        m.setFecha(fecha);
        m.setHora(hora);
        m.setDuracion(duracion);
        m.setLugar(lugar);
        m.setCodMentor(mentor);
        m.setCodSubject(materia);
        m.setEstado("disponible");

        return m;
    }

    private void rellenarFormularioDesdeTabla() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return;

        idSeleccionado = (Integer) modeloTabla.getValueAt(fila, 0);
        campoTema.setText((String) modeloTabla.getValueAt(fila, 1));
        campoFecha.setText(modeloTabla.getValueAt(fila, 2).toString());
        campoHora.setText(modeloTabla.getValueAt(fila, 3).toString());

        // quita el " min" del final
        String durStr = modeloTabla.getValueAt(fila, 4).toString().replace(" min", "");
        campoDuracion.setText(durStr);

        campoLugar.setText(modeloTabla.getValueAt(fila, 5) != null ? modeloTabla.getValueAt(fila, 5).toString() : "");
    }

    private void limpiarFormulario() {
        idSeleccionado = null;
        campoTema.setText("");
        campoFecha.setText("");
        campoHora.setText("");
        campoDuracion.setText("");
        campoLugar.setText("");
        if (comboMentor.getItemCount() > 0) comboMentor.setSelectedIndex(0);
        if (comboMateria.getItemCount() > 0) comboMateria.setSelectedIndex(0);
        tabla.clearSelection();
    }
}