package Swing;

import javax.swing.SwingUtilities;

public class MainSwing {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow());
    }
}