package DAO;
import Model.User;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;


/**
 * @author Alberto
 */
public class ExampleDAO {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernateConfig");


    public void create(User user) {
         EntityManager em = emf.createEntityManager();

        try {
            //creamos un entity manager único para esta clase
            em.getTransaction().begin();
            if(user.getId() == null) {
                //Crea si no existe
                em.persist(user);
            }
            //verifica que se haya creado el usuario
            em.getTransaction().commit();
        } catch (Exception e) {
            //hace rollback si falla (Rollback: revertir)
            em.getTransaction().rollback();
            throw new IllegalArgumentException("ERROR, algo falló");
        }finally {
        em.close();
    }

    }
    public User read(int id) {
        //crea el entity manager (de nuevo)
        EntityManager em = emf.createEntityManager();
        //busca la coincidencia de usuario por ID
        User user = em.find(User.class, id);
        //cierra el entity manager
        em.close();
        //devuelve el Usuario
        return user;
    }
    public void update(Integer id, User user) {

        EntityManager em = emf.createEntityManager();

        try {

            em.getTransaction().begin();

            User existing = em.find(User.class, id);

            if (existing == null) {
                throw new RuntimeException("Usuario no encontrado");
            }

            user.setId(id);

            em.merge(user);

            em.getTransaction().commit();

        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Error al actualizar usuario", e);
        }finally {

            em.close();
        }
    }

    public void delete(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, id);
            if (user == null) throw new RuntimeException("Usuario no existe");
            em.remove(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
