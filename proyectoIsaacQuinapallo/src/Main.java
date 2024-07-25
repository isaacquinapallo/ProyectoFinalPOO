import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Login de Usuario");
        frame.setContentPane(new Login().mainPanel); // Envio a Login
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Cerrar
        frame.setSize(400, 300); // Tamaño
        frame.pack();
        frame.setVisible(true);
    }
}
