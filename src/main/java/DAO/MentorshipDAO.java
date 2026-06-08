package DAO;

import Model.Mentorship;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class MentorshipDAO {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernateConfig");

    public void create(Mentorship mentorship) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            if (mentorship.getId() == null) {
                em.persist(mentorship);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Error al crear la tutoría", e);
        } finally {
            em.close();
        }
    }

    public Mentorship read(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Mentorship.class, id);
        } finally {
            em.close();
        }
    }

    public void update(Integer id, Mentorship mentorship) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Mentorship existing = em.find(Mentorship.class, id);
            if (existing == null) {
                throw new RuntimeException("Tutoría no encontrada");
            }
            mentorship.setId(id);
            em.merge(mentorship);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Error al actualizar la tutoría", e);
        } finally {
            em.close();
        }
    }

    public void delete(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Mentorship mentorship = em.find(Mentorship.class, id);
            if (mentorship == null) {
                throw new RuntimeException("Tutoría no encontrada");
            }
            em.remove(mentorship);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Error al eliminar la tutoría", e);
        } finally {
            em.close();
        }
    }

    public List<Mentorship> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createNamedQuery("Mentorship.findAll", Mentorship.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Mentorship> findAvailable() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT m FROM Mentorship m WHERE m.estado = 'disponible' AND m.fecha >= CURRENT_DATE", Mentorship.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Mentorship> findByMentor(int mentorId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT m FROM Mentorship m WHERE m.codMentor.id = :mentorId", Mentorship.class)
                    .setParameter("mentorId", mentorId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Mentorship> findByMentorEager(int mentorId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT m FROM Mentorship m JOIN FETCH m.codMentor JOIN FETCH m.codSubject WHERE m.codMentor.id = :mentorId", Mentorship.class)
                    .setParameter("mentorId", mentorId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Mentorship> findBySubject(int subjectId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT m FROM Mentorship m WHERE m.codSubject.id = :subjectId AND m.estado = 'disponible'", Mentorship.class)
                    .setParameter("subjectId", subjectId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Mentorship> findAllEager() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT m FROM Mentorship m JOIN FETCH m.codMentor JOIN FETCH m.codSubject", Mentorship.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Mentorship> findBySubjectEager(int subjectId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT DISTINCT m FROM Mentorship m JOIN FETCH m.codMentor JOIN FETCH m.codSubject WHERE m.codSubject.id = :subjectId", Mentorship.class)
                    .setParameter("subjectId", subjectId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    public boolean existeDuplicado(int mentorId, int subjectId, LocalDate fecha, LocalTime hora) {
        EntityManager em = emf.createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(m) FROM Mentorship m WHERE m.codMentor.id = :mentorId AND m.codSubject.id = :subjectId AND m.fecha = :fecha AND m.hora = :hora", Long.class)
                    .setParameter("mentorId", mentorId)
                    .setParameter("subjectId", subjectId)
                    .setParameter("fecha", fecha)
                    .setParameter("hora", hora)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }
}