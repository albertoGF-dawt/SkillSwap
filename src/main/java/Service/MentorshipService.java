package Service;

import DAO.MentorshipDAO;
import Model.Mentorship;
import java.time.LocalDate;
import java.util.List;

public class MentorshipService {

    private final MentorshipDAO mentorshipDAO = new MentorshipDAO();

    public void createMentorship(Mentorship mentorship) {
        validateMentorship(mentorship);
        mentorshipDAO.create(mentorship);
    }

    public Mentorship getMentorship(int id) {
        Mentorship mentorship = mentorshipDAO.read(id);
        if (mentorship == null) {
            throw new RuntimeException("Tutoría no encontrada con ID: " + id);
        }
        return mentorship;
    }

    public void updateMentorship(Integer id, Mentorship mentorship) {
        validateMentorship(mentorship);
        mentorshipDAO.update(id, mentorship);
    }

    // Solo se puede cancelar si está disponible (no reservada)
    public void deleteMentorship(int id) {
        Mentorship mentorship = mentorshipDAO.read(id);
        if (mentorship == null) {
            throw new RuntimeException("Tutoría no encontrada");
        }
        if ("reservada".equals(mentorship.getEstado()) || "pendiente".equals(mentorship.getEstado())) {
            throw new RuntimeException("No se puede eliminar una tutoría ya reservada");
        }
        mentorshipDAO.delete(id);
    }

    public List<Mentorship> getAllMentorships() {
        return mentorshipDAO.findAll();
    }

    public List<Mentorship> getAvailableMentorships() {
        return mentorshipDAO.findAvailable();
    }

    public List<Mentorship> getMentorshipsByMentor(int mentorId) {
        return mentorshipDAO.findByMentorEager(mentorId);
    }

    public List<Mentorship> getMentorshipsBySubject(int subjectId) {
        return mentorshipDAO.findBySubject(subjectId);
    }

    // validaciones
    private void validateMentorship(Mentorship mentorship) {
        if (mentorship.getTema() == null || mentorship.getTema().isEmpty()) {
            throw new IllegalArgumentException("El tema no puede estar vacío");
        }
        if (mentorship.getFecha() == null) {
            throw new IllegalArgumentException("La fecha es obligatoria");
        }
        if (mentorship.getFecha().isBefore(LocalDate.now())
                && "disponible".equals(mentorship.getEstado())) {
            throw new IllegalArgumentException("No se pueden publicar tutorías con fechas pasadas");
        }
        if (mentorship.getHora() == null) {
            throw new IllegalArgumentException("La hora es obligatoria");
        }
        if (mentorship.getDuracion() == null || mentorship.getDuracion() <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor que 0");
        }
        if (mentorship.getCodMentor() == null) {
            throw new IllegalArgumentException("La tutoría debe tener un mentor asignado");
        }
        if (mentorship.getCodSubject() == null) {
            throw new IllegalArgumentException("La tutoría debe tener una materia asignada");
        }
        if (mentorshipDAO.existeDuplicado(mentorship.getCodMentor().getId(), mentorship.getCodSubject().getId(), mentorship.getFecha(), mentorship.getHora())) {
            throw new IllegalArgumentException("Ya existe una tutoría de ese mentor en esa materia, fecha y hora");
        }
    }
    public List<Mentorship> getAllMentorshipsEager() {
        return mentorshipDAO.findAllEager();
    }
}