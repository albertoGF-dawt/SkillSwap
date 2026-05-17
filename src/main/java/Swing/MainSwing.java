package Swing;

// Main.java
import javax.swing.SwingUtilities;

public class MainSwing {
    public static void main(String[] args) {
        // Siempre lanzar Swing en su propio hilo (EDT)
        SwingUtilities.invokeLater(() -> new MainWindow());
    }
}