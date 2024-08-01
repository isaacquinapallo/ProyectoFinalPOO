import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Login de Usuario");
        Login login = new Login();
        frame.setContentPane(login.getFondoPanel()); // Use getFondoPanel to get the panel with background
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setVisible(true);
    }
}
