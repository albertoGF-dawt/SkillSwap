package DAO;

import Model.Subject;
import Model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class SubjectDAO {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernateConfig");


    public void create(Subject subject) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            if(subject.getId() == null) {
                em.persist(subject);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new IllegalArgumentException("ERROR, algo falló");
        }
        em.close();
    }

    public boolean existeNombre(String nombre) {
        EntityManager em = emf.createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(s) FROM Subject s WHERE LOWER(s.nombre) = LOWER(:nombre)", Long.class).setParameter("nombre", nombre).getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

   public  Subject FindPerId(Integer id) {
        EntityManager em = emf.createEntityManager();
        Subject subject = em.find(Subject.class, id);
        em.close();
        return subject;
   }
   public List<Subject> getSubjects() {
        EntityManager em = emf.createEntityManager();
        List<Subject> subjects = em.createNamedQuery("Subject.findAll").getResultList();
        em.close();
        return subjects;
   }
    public void delete(int id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Subject subject = em.find(Subject.class, id);
        if (subject != null) {
            em.remove(subject);
            em.getTransaction().commit();
        } else {
            throw  new RuntimeException("Este usuario no existe");
        }
        em.close();
    }

    public boolean tieneTutorias(int subjectId) {
        EntityManager em = emf.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(m) FROM Mentorship m WHERE m.codSubject.id = :id", Long.class)
                    .setParameter("id", subjectId)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

}
