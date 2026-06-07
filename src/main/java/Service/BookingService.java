package Service;

import DAO.BookingDAO;
import DAO.MentorshipDAO;
import Model.Booking;
import Model.Mentorship;
import java.time.LocalDate;
import java.util.List;

public class BookingService {

    private final BookingDAO bookingDAO = new BookingDAO();
    private final MentorshipDAO mentorshipDAO = new MentorshipDAO();

    public void createBooking(Booking booking) {
        validateBooking(booking);

        Mentorship mentorship = mentorshipDAO.read(booking.getCodMentorship().getId());

        // asegura que la tutoría exista
        if (mentorship == null) {
            throw new RuntimeException("La tutoría no existe");
        }
        if (!"disponible".equals(mentorship.getEstado())) {
            throw new RuntimeException("Esta tutoría ya no está disponible");
        }

        // evita que el alumno reserve tutoria
        if (mentorship.getCodMentor().getId().equals(booking.getCodAlumno().getId())) {
            throw new RuntimeException("No puedes reservar tu propia tutoría");
        }

        // evita robar reservas
        if (bookingDAO.isMentorshipAlreadyBooked(mentorship.getId())) {
            throw new RuntimeException("Esta tutoría ya ha sido reservada");
        }

        booking.setFechaReserva(LocalDate.now());
        booking.setEstado("pendiente");
        bookingDAO.create(booking);

        mentorship.setEstado("reservada");
        mentorshipDAO.update(mentorship.getId(), mentorship);
    }

    public Booking getBooking(int id) {
        Booking booking = bookingDAO.read(id);
        if (booking == null) {
            throw new RuntimeException("Reserva no encontrada con ID: " + id);
        }
        return booking;
    }

    // Cancelar una reserva devuelve la tutoría a "disponible" (Solución de errores mencionados)
    public void cancelBooking(int id) {
        Booking booking = bookingDAO.read(id);
        if (booking == null) {
            throw new RuntimeException("Reserva no encontrada");
        }
        if ("completada".equals(booking.getEstado())) {
            throw new RuntimeException("No se puede cancelar una reserva ya completada");
        }

        // Busca la tutoría directamente por ID en lugar de cogerla del booking
        int mentorshipId = booking.getCodMentorship().getId();
        Mentorship mentorship = mentorshipDAO.read(mentorshipId);
        mentorship.setEstado("disponible");
        mentorshipDAO.update(mentorship.getId(), mentorship);

        bookingDAO.delete(id);
    }

    // Marcar una reserva como completada
    public void completeBooking(int id) {
        Booking booking = bookingDAO.read(id);
        if (booking == null) {
            throw new RuntimeException("Reserva no encontrada");
        }
        if (!"pendiente".equals(booking.getEstado())) {
            throw new RuntimeException("Solo se pueden completar reservas en estado pendiente");
        }
        booking.setEstado("completada");
        bookingDAO.update(id, booking);

        // Actualiza también la tutoría a completada
        Mentorship mentorship = booking.getCodMentorship();
        mentorship.setEstado("completada");
        mentorshipDAO.update(mentorship.getId(), mentorship);
    }

    public List<Booking> getAllBookings() {
        return bookingDAO.findAll();
    }

    public List<Booking> getBookingsByAlumno(int alumnoId) {
        return bookingDAO.findByAlumno(alumnoId);
    }

    public List<Booking> getBookingsByMentorship(int mentorshipId) {
        return bookingDAO.findByMentorship(mentorshipId);
    }

    private void validateBooking(Booking booking) {
        if (booking.getCodAlumno() == null) {
            throw new IllegalArgumentException("La reserva debe tener un alumno asignado");
        }
        if (booking.getCodMentorship() == null) {
            throw new IllegalArgumentException("La reserva debe tener una tutoría asignada");
        }
    }
    public List<Booking> getAllBookingsEager() {
        return bookingDAO.findAllEager();
    }
}