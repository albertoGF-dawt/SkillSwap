package DAO;

import Model.Booking;
import jakarta.persistence.*;
import java.util.List;

public class BookingDAO {

    private final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("hibernateConfig");

    public void create(Booking booking) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            if (booking.getId() == null) {
                em.persist(booking);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Error al crear la reserva", e);
        } finally {
            em.close();
        }
    }

    public Booking read(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Booking.class, id);
        } finally {
            em.close();
        }
    }

    public void update(Integer id, Booking booking) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Booking existing = em.find(Booking.class, id);
            if (existing == null) {
                throw new RuntimeException("Reserva no encontrada");
            }
            booking.setId(id);
            em.merge(booking);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Error al actualizar la reserva", e);
        } finally {
            em.close();
        }
    }

    public void delete(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Booking booking = em.find(Booking.class, id);
            if (booking == null) {
                throw new RuntimeException("Reserva no encontrada");
            }
            em.remove(booking);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Error al eliminar la reserva", e);
        } finally {
            em.close();
        }
    }

    public List<Booking> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createNamedQuery("Booking.findAll", Booking.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Booking> findByAlumno(int alumnoId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT b FROM Booking b WHERE b.codAlumno.id = :alumnoId",
                            Booking.class
                    ).setParameter("alumnoId", alumnoId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public boolean isMentorshipAlreadyBooked(int mentorshipId) {
        EntityManager em = emf.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(b) FROM Booking b WHERE b.codMentorship.id = :mentorshipId",
                            Long.class
                    ).setParameter("mentorshipId", mentorshipId)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public List<Booking> findByMentorship(int mentorshipId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT b FROM Booking b WHERE b.codMentorship.id = :mentorshipId",
                            Booking.class
                    ).setParameter("mentorshipId", mentorshipId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    public List<Booking> findAllEager() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT b FROM Booking b " + "JOIN FETCH b.codAlumno " + "JOIN FETCH b.codMentorship m " + "JOIN FETCH m.codMentor " + "JOIN FETCH m.codSubject",
                    Booking.class
            ).getResultList();
        } finally {
            em.close();
        }
    }
}