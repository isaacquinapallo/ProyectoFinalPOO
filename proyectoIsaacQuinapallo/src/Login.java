import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// ana.gomez@gmail.com          Contrasena456
public class Login {
    public JPanel mainPanel;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton loginButton;
    private JLabel datosIncorrectos;
    private JPanel main;

    private MongoClient mongoClient;
    private MongoCollection<Document> collection;

    // Panel Fondo
    class FondoPanel extends JPanel {
        private Image imagen;

        public FondoPanel(Image imagen) {
            this.imagen = imagen;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imagen != null) {
                g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public Login() {
        // MongoDB
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("proyectoIsaacQuinapallo");
        collection = database.getCollection("userslogin");

        // background
        Image fondoImage = new ImageIcon(getClass().getResource("/imagenes/FondoLoginZapatos.PNG")).getImage();

        FondoPanel fondoPanel = new FondoPanel(fondoImage);
        fondoPanel.setLayout(new BorderLayout());
        fondoPanel.add(mainPanel, BorderLayout.CENTER);

        main.setOpaque(false);
        main.setBackground(new Color(0, 0, 0, 0)); // Set background to fully transparent

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = textField1.getText();
                String password = new String(passwordField1.getPassword());

                Document query = new Document("Correo", email).append("Contrasena", password);
                Document user = collection.find(query).first();

                if (user != null) {
                    Integer userID = user.getInteger("UserID");

                    JFrame frameForm2 = new JFrame(userID != null && userID == 1 ? "Opciones Admin" : "Opciones Cajero");
                    if (userID != null && userID == 1) {
                        IngresoAdmin formInstance = new IngresoAdmin();
                        frameForm2.setContentPane(formInstance.mainPanel);
                    } else {
                        CategoriasVendedor formInstance = new CategoriasVendedor();
                        frameForm2.setContentPane(formInstance.mainPanel);
                    }
                    frameForm2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frameForm2.setSize(600, 400);
                    frameForm2.pack();
                    frameForm2.setVisible(true);

                    SwingUtilities.getWindowAncestor(mainPanel).dispose();
                } else {
                    datosIncorrectos.setText("Datos incorrectos, intente de nuevo");
                    textField1.setText("");
                    passwordField1.setText("");
                }
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> mongoClient.close()));
    }

    // Getter FondoPanel
    public JPanel getFondoPanel() {
        Image fondoImage = new ImageIcon(getClass().getResource("/imagenes/FondoLoginZapatos.PNG")).getImage();
        FondoPanel fondoPanel = new FondoPanel(fondoImage);
        fondoPanel.setLayout(new BorderLayout());
        fondoPanel.add(main, BorderLayout.CENTER);
        return fondoPanel;
    }
}
