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
            //creamos un entity manager único para esta clase
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
        //cierra el entity manager que creamos anteriormente
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
   public void Delete(int id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.remove(subject);
        em.getTransaction().commit();
   }

}
