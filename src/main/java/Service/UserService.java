package Service;

import DAO.UserDAO;
import Model.User;
import java.util.List;

public class UserService {

    private final UserDAO userDAO = new UserDAO();

    public void createUser(User user) {
        validateUser(user);
        userDAO.create(user);
    }

    public User getUser(int id) {
        User user = userDAO.read(id);
        if (user == null) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        return user;
    }

    public void updateUser(Integer id, User user) {
        validateUser(user);
        userDAO.update(id, user);
    }

    public void deleteUser(int id) {
        User user = userDAO.read(id);
        if (user == null) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        userDAO.delete(id);
    }
    //método de pruebas no está en el resultado final
    public void deleteAllUsers() {
        userDAO.deleteAll();
    }

    public List<User> getAllUsers() {
        return userDAO.ListAll();
    }

    private void validateUser(User user) {
        validateNombre(user.getNombre());
        validateEmail(user.getEmail());
        validatePassword(user.getPassword());
        validateRol(user.getRol());
    }

    private void validateNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("El email debe contener '@' y '.'");
        }
        int atIndex = email.indexOf("@");
        if (atIndex == 0 || atIndex == email.length() - 1) {
            throw new IllegalArgumentException("El email no tiene un formato válido");
        }
        int dotIndex = email.lastIndexOf(".");
        if (dotIndex < atIndex || dotIndex == email.length() - 1) {
            throw new IllegalArgumentException("El email no tiene un formato válido");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }

        boolean tieneNumero = false;
        boolean tieneMayus  = false;
        boolean tieneMinus  = false;

        for (char c : password.toCharArray()) {
            if (Character.isDigit(c))      tieneNumero = true;
            if (Character.isUpperCase(c))  tieneMayus  = true;
            if (Character.isLowerCase(c))  tieneMinus  = true;
        }

        if (!tieneNumero) {
            throw new IllegalArgumentException("La contraseña debe contener al menos un número");
        }
        if (!tieneMayus) {
            throw new IllegalArgumentException("La contraseña debe contener al menos una mayúscula");
        }
        if (!tieneMinus) {
            throw new IllegalArgumentException("La contraseña debe contener al menos una minúscula");
        }
    }

    private void validateRol(String rol) {
        if (rol == null || rol.isEmpty()) {
            throw new IllegalArgumentException("El rol no puede estar vacío");
        }
        if (!rol.equals("alumno") && !rol.equals("mentor")) {
            throw new IllegalArgumentException("El rol debe ser 'alumno' o 'mentor'");
        }
    }
}