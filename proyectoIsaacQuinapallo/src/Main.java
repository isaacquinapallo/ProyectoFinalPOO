import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Login de Usuario");
        frame.setContentPane(new Login().mainPanel); // Cambiado a Login con la 'L' mayúscula
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Cerrar procesos
        frame.setSize(400, 300); // Tamaño razonable para visualizar el JFrame
        frame.pack();
        frame.setVisible(true);
    }
}
