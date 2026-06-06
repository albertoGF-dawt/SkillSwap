package Swing;

import DAO.SubjectDAO;
import DAO.UserDAO;
import Model.Booking;
import Model.Mentorship;
import Model.Subject;
import Model.User;
import Service.ConsultService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ConsultasDialog extends JDialog {

    private final ConsultService consultService = new ConsultService();
    private final UserDAO userDAO = new UserDAO();
    private final SubjectDAO subjectDAO = new SubjectDAO();

    // Combos de filtro
    private JComboBox<Subject> comboMateria;
    private JComboBox<User> comboMentor;
    private JComboBox<User> comboAlumno;

    // Tabla y modelo
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    // Label de resumen
    private JLabel labelResumen;

    public ConsultasDialog(JFrame parent) {
        super(parent, "Consultas", true);
        setSize(900, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelFiltros(), BorderLayout.NORTH);
        add(crearTabla(),        BorderLayout.CENTER);
        add(crearPanelResumen(), BorderLayout.SOUTH);

        cargarCombos();
        mostrarTotalHoras(); // muestra el resumen inicial
        setVisible(true);
    }

    // ─────────────────────────────────────────
    // PANEL DE FILTROS
    // ─────────────────────────────────────────
    private JPanel crearPanelFiltros() {
        JPanel panel = new JPanel(new GridLayout(3, 3, 8, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Filtros de consulta"));
        panel.setPreferredSize(new Dimension(900, 130));

        // Fila 1: filtro por materia
        panel.add(new JLabel("Tutorías disponibles por materia:"));
        comboMateria = new JComboBox<>();
        panel.add(comboMateria);
        JButton btnMateria = new JButton("Buscar");
        btnMateria.addActionListener(e -> consultarPorMateria());
        panel.add(btnMateria);

        // Fila 2: filtro por mentor
        panel.add(new JLabel("Tutorías publicadas por mentor:"));
        comboMentor = new JComboBox<>();
        panel.add(comboMentor);
        JButton btnMentor = new JButton("Buscar");
        btnMentor.addActionListener(e -> consultarPorMentor());
        panel.add(btnMentor);

        // Fila 3: filtro por alumno
        panel.add(new JLabel("Reservas de un alumno:"));
        comboAlumno = new JComboBox<>();
        panel.add(comboAlumno);
        JButton btnAlumno = new JButton("Buscar");
        btnAlumno.addActionListener(e -> consultarPorAlumno());
        panel.add(btnAlumno);

        return panel;
    }

    // ─────────────────────────────────────────
    // TABLA
    // ─────────────────────────────────────────
    private JScrollPane crearTabla() {
        modeloTabla = new DefaultTableModel(new String[]{}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return new JScrollPane(tabla);
    }

    // ─────────────────────────────────────────
    // PANEL RESUMEN
    // ─────────────────────────────────────────
    private JPanel crearPanelResumen() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Resumen global"));

        labelResumen = new JLabel("Cargando...");
        labelResumen.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(labelResumen);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());

        JPanel panelCerrar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelCerrar.add(btnCerrar);

        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.add(panel, BorderLayout.CENTER);
        panelSur.add(panelCerrar, BorderLayout.EAST);

        return panelSur;
    }

    // ─────────────────────────────────────────
    // CARGA DE COMBOS
    // ─────────────────────────────────────────
    private void cargarCombos() {
        // Materias
        comboMateria.removeAllItems();
        for (Subject s : subjectDAO.getSubjects()) {
            comboMateria.addItem(s);
        }

        // Mentores y alumnos
        comboMentor.removeAllItems();
        comboAlumno.removeAllItems();
        for (User u : userDAO.ListAll()) {
            if ("mentor".equals(u.getRol())) comboMentor.addItem(u);
            if ("alumno".equals(u.getRol())) comboAlumno.addItem(u);
        }
    }

    // ─────────────────────────────────────────
    // CONSULTAS
    // ─────────────────────────────────────────

    private void consultarPorMateria() {
        Subject materia = (Subject) comboMateria.getSelectedItem();
        if (materia == null) return;

        List<Mentorship> lista = consultService.getTutoriasPorMateria(materia.getId());

        cambiarColumnas(new String[]{"ID", "Tema", "Fecha", "Hora", "Duración", "Lugar", "Mentor", "Estado"});
        modeloTabla.setRowCount(0);

        for (Mentorship m : lista) {
            modeloTabla.addRow(new Object[]{
                    m.getId(),
                    m.getTema(),
                    m.getFecha(),
                    m.getHora(),
                    m.getDuracion() + " min",
                    m.getLugar(),
                    m.getCodMentor().getNombre(),
                    m.getEstado()
            });
        }

        actualizarResumen("Tutorías disponibles en \"" + materia.getNombre() + "\": " + lista.size());
    }

    private void consultarPorMentor() {
        User mentor = (User) comboMentor.getSelectedItem();
        if (mentor == null) return;

        List<Mentorship> lista = consultService.getTutoriasPorMentor(mentor.getId());

        cambiarColumnas(new String[]{"ID", "Tema", "Fecha", "Hora", "Duración", "Materia", "Estado"});
        modeloTabla.setRowCount(0);

        for (Mentorship m : lista) {
            modeloTabla.addRow(new Object[]{
                    m.getId(),
                    m.getTema(),
                    m.getFecha(),
                    m.getHora(),
                    m.getDuracion() + " min",
                    m.getCodSubject().getNombre(),
                    m.getEstado()
            });
        }

        actualizarResumen("Tutorías publicadas por " + mentor.getNombre() + ": " + lista.size());
    }

    private void consultarPorAlumno() {
        User alumno = (User) comboAlumno.getSelectedItem();
        if (alumno == null) return;

        List<Booking> lista = consultService.getReservasPorAlumno(alumno.getId());

        cambiarColumnas(new String[]{"ID Reserva", "Tutoría", "Mentor", "Fecha Reserva", "Estado"});
        modeloTabla.setRowCount(0);

        for (Booking b : lista) {
            modeloTabla.addRow(new Object[]{
                    b.getId(),
                    b.getCodMentorship().getTema(),
                    b.getCodMentorship().getCodMentor().getNombre(),
                    b.getFechaReserva(),
                    b.getEstado()
            });
        }

        actualizarResumen("Reservas de " + alumno.getNombre() + ": " + lista.size());
    }

    private void mostrarTotalHoras() {
        int horas = consultService.getTotalHoras();
        labelResumen.setText("Total de horas de mentoría ofrecidas por la comunidad: " + horas + " h");
    }

    // ─────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────

    /** Cambia las columnas de la tabla dinámicamente según la consulta */
    private void cambiarColumnas(String[] columnas) {
        modeloTabla.setColumnIdentifiers(columnas);
    }

    private void actualizarResumen(String texto) {
        int horas = consultService.getTotalHoras();
        labelResumen.setText(texto + "   |   Total horas comunidad: " + horas + " h");
    }
}