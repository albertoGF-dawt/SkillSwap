package Service;

import DAO.BookingDAO;
import DAO.MentorshipDAO;
import DAO.UserDAO;
import Model.Booking;
import Model.Mentorship;

import java.util.List;

public class ConsultService {

    private final MentorshipDAO mentorshipDAO = new MentorshipDAO();
    private final BookingDAO bookingDAO = new BookingDAO();

    // Tutorías disponibles por materia
    public List<Mentorship> getTutoriasPorMateria(int subjectId) {
        return mentorshipDAO.findBySubject(subjectId);
    }

    // Tutorías publicadas por un mentor
    public List<Mentorship> getTutoriasPorMentor(int mentorId) {
        return mentorshipDAO.findByMentor(mentorId);
    }

    // Reservas de un alumno
    public List<Booking> getReservasPorAlumno(int alumnoId) {
        return bookingDAO.findByAlumnoEager(alumnoId);
    }

    // Total de horas ofrecidas (todas las tutorías)
    public int getTotalHoras() {
        return mentorshipDAO.findAll().stream().mapToInt(m -> m.getDuracion()).sum() / 60;
    }
}