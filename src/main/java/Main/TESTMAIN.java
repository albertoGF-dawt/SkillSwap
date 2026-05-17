package Main;
import DAO.ExampleDAO;
import DAO.UserDAO;
import Model.User;

import java.awt.*;

public class TESTMAIN {
    public static void main(String[] args) {


        UserDAO dao = new UserDAO();

        try {
            //para crear no es necesario poner ID, el programa lo hace automático
//            dao.create(new User("Alberto21", "Alberto@gmail.com", "12345678", "Alumno", "DAWT"));
            //lectura de el usuario creado
            User user = dao.read(6);

            System.out.println("ID: " + user.getId() + " | nombre " + user.getNombre() + " | Email: " + user.getEmail());

            //prueba de actualización
//            dao.update(6 ,new User("Juan", "Juan@Juan.X", "Contraseñax", "Alumnox", "DAM"));
            //lectura del usuario actualizado
            System.out.println("ID: " + user.getId() + " | nombre " + user.getNombre() + " | Email: " + user.getEmail());
            //elimina al usuario
//            dao.delete(1);
            //debería responder que el usuario no existe
            System.out.println("ID: " + user.getId() + " | nombre " + user.getNombre() + " | Email: " + user.getEmail());

            System.out.println("USUARIOS");
            System.out.println(dao.ListAll());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
