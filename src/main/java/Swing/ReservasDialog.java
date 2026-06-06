package Swing;

import DAO.UserDAO;
import DAO.MentorshipDAO;
import Model.Booking;
import Model.Mentorship;
import Model.User;
import Service.BookingService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReservasDialog extends JDialog {

    private final BookingService bookingService = new BookingService();
    private final UserDAO userDAO = new UserDAO();
    private final MentorshipDAO mentorshipDAO = new MentorshipDAO();

    // Combos del formulario
    private JComboBox<User> comboAlumno;
    private JComboBox<Mentorship> comboTutoria;

    // Tabla
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private Integer idSeleccionado = null;

    public ReservasDialog(JFrame parent) {
        super(parent, "Gestión de Reservas", true);
        setSize(900, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(crearTabla(),           BorderLayout.CENTER);
        add(crearPanelBotones(),    BorderLayout.SOUTH);

        cargarCombos();
        cargarReservas();
        setVisible(true);
    }

    // ─────────────────────────────────────────
    // FORMULARIO
    // ─────────────────────────────────────────
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Nueva Reserva"));
        panel.setPreferredSize(new Dimension(900, 90));

        panel.add(new JLabel("Alumno:"));
        comboAlumno = new JComboBox<>();
        panel.add(comboAlumno);

        panel.add(new JLabel("Tutoría disponible:"));
        comboTutoria = new JComboBox<>();
        panel.add(comboTutoria);

        return panel;
    }

    // ─────────────────────────────────────────
    // TABLA
    // ─────────────────────────────────────────
    private JScrollPane crearTabla() {
        String[] columnas = {"ID", "Alumno", "Tutoría (tema)", "Mentor", "Fecha Reserva", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getColumnModel().getColumn(0).setMaxWidth(40);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tabla.getSelectedRow();
                idSeleccionado = fila == -1 ? null : (Integer) modeloTabla.getValueAt(fila, 0);
            }
        });

        return new JScrollPane(tabla);
    }

    // ─────────────────────────────────────────
    // BOTONES
    // ─────────────────────────────────────────
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton btnReservar   = new JButton("Reservar");
        JButton btnCancelar   = new JButton("Cancelar reserva");
        JButton btnCompletar  = new JButton("Marcar completada");
        JButton btnCerrar     = new JButton("Cerrar");

        btnReservar.addActionListener(e -> crearReserva());
        btnCancelar.addActionListener(e -> cancelarReserva());
        btnCompletar.addActionListener(e -> completarReserva());
        btnCerrar.addActionListener(e -> dispose());

        panel.add(btnReservar);
        panel.add(btnCancelar);
        panel.add(btnCompletar);
        panel.add(btnCerrar);

        return panel;
    }

    // ─────────────────────────────────────────
    // LÓGICA
    // ─────────────────────────────────────────

    private void cargarCombos() {
        // Alumnos
        comboAlumno.removeAllItems();
        for (User u : userDAO.ListAll()) {
            if ("alumno".equals(u.getRol())) comboAlumno.addItem(u);
        }

        // Tutorías disponibles
        comboTutoria.removeAllItems();
        for (Mentorship m : mentorshipDAO.findAvailable()) {
            comboTutoria.addItem(m);
        }
    }

    private void cargarReservas() {
        modeloTabla.setRowCount(0);
        for (Booking b : bookingService.getAllBookingsEager()) {
            modeloTabla.addRow(new Object[]{
                    b.getId(),
                    b.getCodAlumno().getNombre(),
                    b.getCodMentorship().getTema(),
                    b.getCodMentorship().getCodMentor().getNombre(),
                    b.getFechaReserva(),
                    b.getEstado()
            });
        }
    }

    private void crearReserva() {
        User alumno       = (User) comboAlumno.getSelectedItem();
        Mentorship tutoria = (Mentorship) comboTutoria.getSelectedItem();

        if (alumno == null || tutoria == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un alumno y una tutoría.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Booking booking = new Booking();
        booking.setCodAlumno(alumno);
        booking.setCodMentorship(tutoria);

        try {
            bookingService.createBooking(booking);
            JOptionPane.showMessageDialog(this, "Reserva realizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarCombos();   // refresca tutorías disponibles
            cargarReservas();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cancelarReserva() {
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una reserva de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres cancelar esta reserva?",
                "Confirmar cancelación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                bookingService.cancelBooking(idSeleccionado);
                JOptionPane.showMessageDialog(this, "Reserva cancelada. La tutoría vuelve a estar disponible.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarCombos();
                cargarReservas();
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void completarReserva() {
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una reserva de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            bookingService.completeBooking(idSeleccionado);
            JOptionPane.showMessageDialog(this, "Reserva marcada como completada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarReservas();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        }
    }


}