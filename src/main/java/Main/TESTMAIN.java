package Main;
import DAO.ExampleDAO;
import Model.User;

public class TESTMAIN {
    public static void main(String[] args) {


        ExampleDAO dao = new ExampleDAO();

        try {
            //para crear no es necesario poner ID, el programa lo hace automático
//            dao.create(new User("Alberto21", "Alberto@gmail.com", "12345678", "Alumno", "DAWT"));
            //lectura de el usuario creado
            User user = dao.read(6);

            System.out.println(user);

            //prueba de actualización
            dao.update(6 ,new User("Juan", "Juan@Juan.Juan", "Contraseña", "Alumno", "DAM"));
            //lectura del usuario actualizado
            System.out.println(dao.read(6));
            //elimina al usuario
//            dao.delete(1);
            //debería responder que el usuario no existe
            System.out.println(dao.read(1));

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
