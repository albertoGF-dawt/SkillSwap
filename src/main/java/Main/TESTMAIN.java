package Main;

import Model.User;
import Service.UserService;

import java.util.List;
import java.util.Scanner;

public class TESTMAIN {

    private static final UserService userService = new UserService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n===== MENÚ DE PRUEBAS - SkillSwap =====");
            System.out.println("1. Crear usuario");
            System.out.println("2. Leer usuario por ID");
            System.out.println("3. Actualizar usuario");
            System.out.println("4. Eliminar usuario por ID");
            System.out.println("5. Listar todos los usuarios");
            System.out.println("6. Eliminar TODOS los usuarios");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    crearUsuario();
                    break;
                case "2":
                    leerUsuario();
                    break;
                case "3":
                    actualizarUsuario();
                    break;
                case "4":
                    eliminarUsuario();
                    break;
                case "5":
                    listarUsuarios();
                    break;
                case "6":
                    eliminarTodos();
                    break;
                case "0":
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida");
                    break;
            }
        }

        System.out.println("Saliendo...");
        scanner.close();
    }

    private static void crearUsuario() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Rol (alumno/mentor): ");
        String rol = scanner.nextLine();
        System.out.print("Ciclo: ");
        String ciclo = scanner.nextLine();

        User user = new User(nombre, email, password, rol, ciclo);
        try {
            userService.createUser(user);
            System.out.println("✔ Usuario creado correctamente");
        } catch (IllegalArgumentException e) {
            System.out.println("✘ Validación: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✘ Error: " + e.getMessage());
        }
    }

    private static void leerUsuario() {
        System.out.print("ID del usuario: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            User user = userService.getUser(id);
            System.out.println("─────────────────────────");
            System.out.println("ID:     " + user.getId());
            System.out.println("Nombre: " + user.getNombre());
            System.out.println("Email:  " + user.getEmail());
            System.out.println("Rol:    " + user.getRol());
            System.out.println("Ciclo:  " + user.getCiclo());
            System.out.println("─────────────────────────");
        } catch (NumberFormatException e) {
            System.out.println("✘ ID no válido");
        } catch (RuntimeException e) {
            System.out.println("✘ " + e.getMessage());
        }
    }

    private static void actualizarUsuario() {
        System.out.print("ID del usuario a actualizar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            User existente = userService.getUser(id);
            System.out.println("Datos actuales → Nombre: " + existente.getNombre() + " | Rol: " + existente.getRol());

            System.out.print("Nuevo nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("Nuevo email: ");
            String email = scanner.nextLine();
            System.out.print("Nueva password: ");
            String password = scanner.nextLine();
            System.out.print("Nuevo rol: ");
            String rol = scanner.nextLine();
            System.out.print("Nuevo ciclo: ");
            String ciclo = scanner.nextLine();

            User actualizado = new User(nombre, email, password, rol, ciclo);
            userService.updateUser(id, actualizado);
            System.out.println("✔ Usuario actualizado correctamente");
        } catch (NumberFormatException e) {
            System.out.println("✘ ID no válido");
        } catch (IllegalArgumentException e) {
            System.out.println("✘ Validación: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("✘ " + e.getMessage());
        }
    }

    private static void eliminarUsuario() {
        System.out.print("ID del usuario a eliminar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            userService.deleteUser(id);
            System.out.println("✔ Usuario eliminado correctamente");
        } catch (NumberFormatException e) {
            System.out.println("✘ ID no válido");
        } catch (RuntimeException e) {
            System.out.println("✘ " + e.getMessage());
        }
    }

    private static void listarUsuarios() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No hay usuarios registrados");
            return;
        }
        System.out.println("─────────────────────────────────────────────");
        for (User u : users) {
            System.out.printf("ID: %-3d | %-20s | %-30s | %-8s | %s%n",
                    u.getId(), u.getNombre(), u.getEmail(), u.getRol(), u.getCiclo());
        }
        System.out.println("─────────────────────────────────────────────");
        System.out.println("Total: " + users.size() + " usuarios");
    }

    private static void eliminarTodos() {
        System.out.print("¿Seguro que quieres eliminar TODOS los usuarios? (s/n): ");
        String confirmacion = scanner.nextLine();
        if ("s".equalsIgnoreCase(confirmacion)) {
            try {
                userService.deleteAllUsers();
                System.out.println("✔ Todos los usuarios han sido eliminados");
            } catch (RuntimeException e) {
                System.out.println("✘ " + e.getMessage());
            }
        } else {
            System.out.println("Operación cancelada");
        }
    }
}