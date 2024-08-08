import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class AdminAñadirVendedores {
    // Componentes de la interfaz gráfica
    public JPanel mainPanel;
    private JButton crearNuevoVendedorButton; // Botón para crear un nuevo vendedor
    private JTextField correoTextField; // Campo de texto para el correo electrónico
    private JLabel confirmadorVendedorTxt; // Etiqueta para mostrar mensajes de confirmación

    private MongoDatabase database; // Base de datos de MongoDB
    private MongoCollection<Document> usersCollection; // Colección de usuarios en MongoDB

    // Constructor
    public AdminAñadirVendedores() {
        initializeMongoDB(); // Inicializar la conexión con MongoDB
        setupUIComponents(); // Configurar los componentes de la interfaz gráfica
        configureButtons(); // Configurar el botón de creación de vendedor
    }

    // Inicializar la conexión con MongoDB
    private void initializeMongoDB() {
        try {
            var mongoClient = MongoClients.create("mongodb://localhost:27017"); // Crear cliente MongoDB
            database = mongoClient.getDatabase("proyectoIsaacQuinapallo"); // Obtener base de datos
            usersCollection = database.getCollection("userslogin"); // Obtener colección de usuarios
        } catch (Exception e) {
            handleError("Error al conectar con la base de datos: " + e.getMessage(), e); // Manejar errores de conexión
        }
    }

    // Configurar los componentes de la interfaz gráfica
    private void setupUIComponents() {
        correoTextField.getDocument().addDocumentListener(new DocumentListener() {
            // Listener para cambios en el campo de texto del correo electrónico
            @Override
            public void insertUpdate(DocumentEvent e) {
                verificarCorreo(); // Verificar correo al insertar texto
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                verificarCorreo(); // Verificar correo al eliminar texto
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                verificarCorreo(); // Verificar correo al cambiar el documento
            }
        });
        crearNuevoVendedorButton.setEnabled(false); // Deshabilitar el botón inicialmente
    }

    // Configurar el botón para crear un nuevo vendedor
    private void configureButtons() {
        crearNuevoVendedorButton.addActionListener(e -> crearNuevoVendedor()); // Acción al hacer clic en el botón
    }

    // Verificar la validez del correo electrónico
    private void verificarCorreo() {
        String correo = correoTextField.getText().trim(); // Obtener el correo del campo de texto
        if (!esCorreoValido(correo)) {
            updateConfirmadorVendedor("Correo no válido.", Color.RED, false); // Mostrar mensaje de error
            return;
        }

        Document filtro = new Document("Correo", correo);
        Document vendedor = usersCollection.find(filtro).first(); // Buscar en la colección

        if (vendedor != null) {
            updateConfirmadorVendedor("Correo ya registrado.", Color.RED, false); // Mostrar mensaje de error si ya está registrado
        } else {
            updateConfirmadorVendedor("Correo disponible.", Color.GREEN, true); // Mostrar mensaje de éxito si está disponible
        }
    }

    // Verificar si el correo electrónico es válido
    private boolean esCorreoValido(String correo) {
        String patronCorreo = "^[^\\s]+@[\\w-]+\\.[\\w-]+$"; // Patrón de correo electrónico
        return Pattern.matches(patronCorreo, correo);
    }

    // Verificar si el teléfono es válido
    private boolean esTelefonoValido(String telefono) {
        String patronTelefono = "^\\d{10}$"; // Patrón de teléfono de 10 dígitos
        return Pattern.matches(patronTelefono, telefono);
    }

    // Verificar si la contraseña es válida
    private boolean esContrasenaValida(char[] contrasena) {
        String contrasenaStr = new String(contrasena);
        String patronContrasena = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?])[A-Za-z\\d!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]{6,}$";
        return Pattern.matches(patronContrasena, contrasenaStr);
    }

    // Verificar si el nombre es válido
    private boolean esNombreValido(String nombre) {
        String patronNombre = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ]+$"; // Patrón para nombres con letras y acentos
        return Pattern.matches(patronNombre, nombre);
    }

    // Verificar si la dirección es válida
    private boolean esDireccionValida(String direccion) {
        return direccion != null && !direccion.isEmpty(); // Verificar si la dirección no está vacía
    }

    // Actualizar el mensaje de confirmación del vendedor
    private void updateConfirmadorVendedor(String message, Color color, boolean enableButton) {
        confirmadorVendedorTxt.setText(message); // Actualizar el texto del mensaje
        confirmadorVendedorTxt.setForeground(color); // Actualizar el color del mensaje
        crearNuevoVendedorButton.setEnabled(enableButton); // Habilitar o deshabilitar el botón
    }

    // Crear un nuevo vendedor
    private void crearNuevoVendedor() {
        String correo = correoTextField.getText().trim();
        if (correo.isEmpty() || correo.contains(" ")) {
            showErrorMessage("Ingrese un correo válido sin espacios."); // Mostrar mensaje de error si el correo es inválido
            return;
        }

        // Solicitar y validar el nombre del vendedor
        String nombre = "";
        while (true) {
            nombre = JOptionPane.showInputDialog("Ingrese el nombre del nuevo vendedor:").trim();
            if (nombre.isEmpty()) {
                showErrorMessage("El nombre no puede estar vacío.");
            } else if (esNombreValido(nombre) && !nombre.contains(" ")) {
                nombre = capitalizar(nombre); // Capitalizar el nombre
                break;
            } else {
                showErrorMessage("El nombre no debe contener caracteres especiales, números ni espacios.");
            }
        }

        // Solicitar y validar el apellido del vendedor
        String apellido = "";
        while (true) {
            apellido = JOptionPane.showInputDialog("Ingrese el apellido del nuevo vendedor:").trim();
            if (apellido.isEmpty()) {
                showErrorMessage("El apellido no puede estar vacío.");
            } else if (esNombreValido(apellido) && !apellido.contains(" ")) {
                apellido = capitalizar(apellido); // Capitalizar el apellido
                break;
            } else {
                showErrorMessage("El apellido no debe contener caracteres especiales, números ni espacios.");
            }
        }

        // Solicitar y validar la dirección del vendedor
        String direccion = "";
        while (true) {
            direccion = JOptionPane.showInputDialog("Ingrese la dirección del nuevo vendedor:").trim();
            if (direccion.isEmpty()) {
                showErrorMessage("La dirección no puede estar vacía.");
            } else {
                direccion = capitalizar(direccion); // Capitalizar la dirección
                break;
            }
        }

        // Solicitar y validar el teléfono del vendedor
        String telefono = "";
        while (true) {
            telefono = JOptionPane.showInputDialog("Ingrese el teléfono del nuevo vendedor:").trim();
            if (telefono.isEmpty() || telefono.contains(" ")) {
                showErrorMessage("El teléfono no puede estar vacío y no debe contener espacios.");
            } else if (esTelefonoValido(telefono)) {
                break;
            } else {
                showErrorMessage("Ingrese un número de teléfono válido de 10 dígitos.");
            }
        }

        // Solicitar y validar la contraseña del vendedor
        String contrasena = "";
        while (true) {
            JPasswordField passwordField = new JPasswordField();
            JPasswordField confirmPasswordField = new JPasswordField();
            Object[] passwordMessage = {
                    "Ingrese la contraseña del nuevo vendedor:", passwordField,
                    "Confirme la contraseña del nuevo vendedor:", confirmPasswordField
            };
            int option = JOptionPane.showConfirmDialog(null, passwordMessage, "Contraseña", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                char[] password = passwordField.getPassword();
                char[] confirmPassword = confirmPasswordField.getPassword();
                if (java.util.Arrays.equals(password, confirmPassword)) {
                    if (esContrasenaValida(password) && !new String(password).contains(" ")) {
                        contrasena = new String(password);
                        break;
                    } else {
                        showErrorMessage("La contraseña debe contener al menos una letra mayúscula, un número, un caracter especial, tener al menos 6 caracteres y no contener espacios.");
                    }
                } else {
                    showErrorMessage("Las contraseñas no coinciden. Inténtelo de nuevo.");
                }
            } else {
                showErrorMessage("Operación cancelada.");
                return;
            }
        }

        // Registrar la fecha y hora actual
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaRegistro = dtf.format(LocalDateTime.now());

        try {
            int lastUserID = 0;
            Document lastUser = usersCollection.find().sort(new Document("UserID", -1)).first(); // Obtener el último UserID
            if (lastUser != null) {
                lastUserID = lastUser.getInteger("UserID");
            }
            int newUserID = lastUserID + 1; // Generar el nuevo UserID

            // Crear un nuevo documento para el vendedor
            Document nuevoVendedor = new Document("UserID", newUserID)
                    .append("Correo", correo)
                    .append("Contrasena", contrasena)
                    .append("Nombre", nombre)
                    .append("Apellido", apellido)
                    .append("Direccion", direccion)
                    .append("Telefono", telefono)
                    .append("FechaRegistro", fechaRegistro);

            usersCollection.insertOne(nuevoVendedor); // Insertar el nuevo vendedor en la colección
            showInformationMessage("Vendedor creado exitosamente."); // Mostrar mensaje de éxito
        } catch (Exception e) {
            handleError("Error al crear el nuevo vendedor: " + e.getMessage(), e); // Manejar errores de creación
        }
    }

    // Capitalizar la primera letra del texto y poner el resto en minúsculas
    private String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto;
        }
        return texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase();
    }

    // Mostrar un mensaje de error
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Mostrar un mensaje de información
    private void showInformationMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    // Manejar errores y mostrar en la consola
    private void handleError(String message, Exception e) {
        System.err.println(message);
        e.printStackTrace();
    }
}
