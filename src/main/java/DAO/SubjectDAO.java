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
                //Crea si no existe
                em.persist(subject);
            }
            //verifica que se haya creado el usuario
            em.getTransaction().commit();
        } catch (Exception e) {
            //hace rollback si falla (Rollback: revertir)
            em.getTransaction().rollback();
            throw new IllegalArgumentException("ERROR, algo falló");
        }
        em.close();
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
        //Busca a una asignatura por su id en la Base de Datos
        Subject subject = em.find(Subject.class, id);
        if (subject != null) {
            em.remove(subject);
            em.getTransaction().commit();
        } else {
            throw  new RuntimeException("Este usuario no existe");
        }
        em.close();
    }

}
