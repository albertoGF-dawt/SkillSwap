package DAO;

import Model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class UserDAO {
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
        }
        //cierra el entity manager que creamos anteriormente
        em.close();
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
            e.printStackTrace();

        } finally {

            em.close();
        }
    }

    public void delete(int id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        //Busca a un usuario por su id en la Base de Datos
        User user = em.find(User.class, id);
        if (user != null) {
            em.remove(user);
            em.getTransaction().commit();
        } else {
            throw  new RuntimeException("Este usuario no existe");
        }
        em.close();
    }



    public List<User> ListAll() {
        EntityManager em = emf.createEntityManager();
        List<User> users = em.createNamedQuery("User.findAll", User.class).getResultList();
        em.close();
        return users;
    }


    //USAR CON CUIDADO
    public void deleteAll() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            int deleted = em.createQuery("DELETE FROM User u").executeUpdate();
            em.getTransaction().commit();
            System.out.println("Usuarios eliminados: " + deleted);
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Error al eliminar todos los usuarios", e);
        } finally {
            em.close();
        }
    }

}
