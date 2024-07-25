import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {
    public JPanel mainPanel;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton loginButton;
    private JLabel datosIncorrectos; // Etiqueta para mostrar errores

    private MongoClient mongoClient;
    private MongoCollection<Document> collection;

    public Login() {
        // Crear un cliente MongoDB
        mongoClient = MongoClients.create("mongodb://localhost:27017");

        // Acceder a la base de datos
        MongoDatabase database = mongoClient.getDatabase("proyectoIsaacQuinapallo");

        // Acceder a una colección
        collection = database.getCollection("userslogin");

        // Configurar acción del botón de inicio de sesión
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = textField1.getText();
                String password = new String(passwordField1.getPassword());

                // Consultar la colección para encontrar el usuario con el correo y contraseña proporcionados
                Document query = new Document("Correo", email).append("Contrasena", password);
                Document user = collection.find(query).first();

                if (user != null) {
                    // Si la autenticación es exitosa, abrir la nueva ventana
                    JFrame frameForm2 = new JFrame("Opciones");
                    Categoria formInstance = new Categoria();
                    frameForm2.setContentPane(formInstance.mainPanel);
                    frameForm2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frameForm2.setSize(600, 400); // Tamaño razonable
                    frameForm2.pack();
                    frameForm2.setVisible(true);

                    // Cerrar la ventana de login
                    SwingUtilities.getWindowAncestor(mainPanel).dispose();
                } else {
                    // Mostrar mensaje de error
                    datosIncorrectos.setText("Datos incorrectos, intente de nuevo");
                    textField1.setText("");
                    passwordField1.setText("");
                }
            }
        });

        // Cerrar el cliente cuando se termine
        Runtime.getRuntime().addShutdownHook(new Thread(() -> mongoClient.close()));
    }
}
