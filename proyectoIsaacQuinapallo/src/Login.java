import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

// Comentarios de usuarios y contraseñas para pruebas
// Como Administrador: juan.perez@gmail.com, Contrasena123
// Como Vendedor: ana.gomez@gmail.com, Contrasena456

public class Login {
    // Panel principal de la interfaz gráfica
    public JPanel mainPanel;
    private JTextField textField1; // Campo de texto para el correo electrónico
    private JPasswordField passwordField1; // Campo de texto para la contraseña
    private JButton loginButton; // Botón para iniciar sesión
    private JLabel datosIncorrectos; // Etiqueta para mostrar mensajes de error
    private JPanel main; // Panel que contiene todos los componentes

    private MongoClient mongoClient; // Cliente de MongoDB
    private MongoCollection<Document> collection; // Colección de usuarios en MongoDB

    // Clase interna para el panel con fondo personalizado
    class FondoPanel extends JPanel {
        private Image imagen; // Imagen de fondo

        // Constructor que recibe la imagen de fondo
        public FondoPanel(Image imagen) {
            this.imagen = imagen;
            setOpaque(false); // Hacer el panel transparente para mostrar la imagen de fondo
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imagen != null) {
                // Dibujar la imagen de fondo en el panel
                g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public Login() {
        // Conectar a MongoDB
        mongoClient = MongoClients.create("mongodb://localhost:27017"); // Crear cliente MongoDB
        MongoDatabase database = mongoClient.getDatabase("proyectoIsaacQuinapallo"); // Obtener base de datos
        collection = database.getCollection("userslogin"); // Obtener colección de usuarios

        // Configurar la imagen de fondo
        Image fondoImage = new ImageIcon(getClass().getResource("/imagenes/FondoLoginZapatos.PNG")).getImage();
        FondoPanel fondoPanel = new FondoPanel(fondoImage); // Crear panel con fondo
        fondoPanel.setLayout(new BorderLayout()); // Usar BorderLayout para el panel de fondo
        fondoPanel.add(mainPanel, BorderLayout.CENTER); // Agregar el panel principal al panel de fondo

        // Configurar el panel principal para ser transparente
        main.setOpaque(false);
        main.setBackground(new Color(0, 0, 0, 0)); // Fondo completamente transparente

        // Acción del botón de inicio de sesión
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = textField1.getText(); // Obtener el correo electrónico del campo de texto
                System.out.println("Email obtenido de textField1: " + email); // Imprimir correo electrónico en consola

                // Obtener la contraseña del campo de texto
                String password = new String(passwordField1.getPassword());

                // Consultar la base de datos para verificar las credenciales del usuario
                Document query = new Document("Correo", email).append("Contrasena", password);
                Document user = collection.find(query).first();

                if (user != null) {
                    Integer userID = user.getInteger("UserID"); // Obtener el ID del usuario

                    // Guardar el correo en un archivo local
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("usuarioActual.txt"))) {
                        writer.write(email);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al guardar el correo en el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    // Crear un nuevo JFrame dependiendo del tipo de usuario
                    JFrame frameForm2 = new JFrame(userID != null && userID == 1 ? "Opciones Admin" : "Opciones Cajero");
                    if (userID != null && userID == 1) {
                        // Si es un administrador, mostrar el panel de administrador
                        IngresoAdmin formInstance = new IngresoAdmin();
                        frameForm2.setContentPane(formInstance.mainPanel);
                    } else {
                        // Si es un vendedor, mostrar el panel de vendedor
                        CategoriasVendedor formInstance = new CategoriasVendedor();
                        frameForm2.setContentPane(formInstance.mainPanel);
                    }
                    frameForm2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cerrar la aplicación al cerrar el JFrame
                    frameForm2.setSize(600, 400); // Ajustar el tamaño del JFrame
                    frameForm2.pack(); // Ajustar el tamaño del JFrame según el contenido
                    frameForm2.setVisible(true); // Mostrar el JFrame

                    // Cerrar la ventana de inicio de sesión
                    SwingUtilities.getWindowAncestor(mainPanel).dispose();
                } else {
                    // Si el usuario no es válido, mostrar un mensaje de error
                    datosIncorrectos.setText("Datos incorrectos, intente de nuevo");
                    textField1.setText(""); // Limpiar el campo de texto del correo electrónico
                    passwordField1.setText(""); // Limpiar el campo de texto de la contraseña
                }
            }
        });

        // Cerrar la conexión con MongoDB al cerrar la aplicación
        Runtime.getRuntime().addShutdownHook(new Thread(() -> mongoClient.close()));
    }

    // Método para obtener el panel con el fondo personalizado
    public JPanel getFondoPanel() {
        Image fondoImage = new ImageIcon(getClass().getResource("/imagenes/FondoLoginZapatos.PNG")).getImage();
        FondoPanel fondoPanel = new FondoPanel(fondoImage); // Crear panel con fondo
        fondoPanel.setLayout(new BorderLayout()); // Usar BorderLayout para el panel de fondo
        fondoPanel.add(main, BorderLayout.CENTER); // Agregar el panel principal al panel de fondo
        return fondoPanel;
    }

}

