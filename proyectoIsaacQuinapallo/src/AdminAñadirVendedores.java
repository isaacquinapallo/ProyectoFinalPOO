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
    public JPanel mainPanel;
    private JButton crearNuevoVendedorButton;
    private JTextField correoTextField;
    private JLabel confirmadorVendedorTxt;

    private MongoDatabase database;
    private MongoCollection<Document> usersCollection;

    public AdminAñadirVendedores() {
        initializeMongoDB();
        setupUIComponents();
        configureButtons();
    }

    private void initializeMongoDB() {
        try {
            var mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("proyectoIsaacQuinapallo");
            usersCollection = database.getCollection("userslogin");
        } catch (Exception e) {
            handleError("Error al conectar con la base de datos: " + e.getMessage(), e);
        }
    }

    private void setupUIComponents() {
        correoTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                verificarCorreo();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                verificarCorreo();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                verificarCorreo();
            }
        });
        crearNuevoVendedorButton.setEnabled(false);
    }

    private void configureButtons() {
        crearNuevoVendedorButton.addActionListener(e -> crearNuevoVendedor());
    }

    private void verificarCorreo() {
        String correo = correoTextField.getText().trim();
        if (!esCorreoValido(correo)) {
            updateConfirmadorVendedor("Correo no válido.", Color.RED, false);
            return;
        }

        Document filtro = new Document("Correo", correo);
        Document vendedor = usersCollection.find(filtro).first();

        if (vendedor != null) {
            updateConfirmadorVendedor("Correo ya registrado.", Color.RED, false);
        } else {
            updateConfirmadorVendedor("Correo disponible.", Color.GREEN, true);
        }
    }

    private boolean esCorreoValido(String correo) {
        String patronCorreo = "^[^\\s]+@[\\w-]+\\.[\\w-]+$";
        return Pattern.matches(patronCorreo, correo);
    }

    private boolean esTelefonoValido(String telefono) {
        String patronTelefono = "^\\d{10}$";
        return Pattern.matches(patronTelefono, telefono);
    }

    private boolean esContrasenaValida(char[] contrasena) {
        String contrasenaStr = new String(contrasena);
        String patronContrasena = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?])[A-Za-z\\d!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]{6,}$";
        return Pattern.matches(patronContrasena, contrasenaStr);
    }

    private boolean esNombreValido(String nombre) {
        String patronNombre = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ]+$";
        return Pattern.matches(patronNombre, nombre);
    }

    private boolean esDireccionValida(String direccion) {
        return direccion != null && !direccion.isEmpty();
    }

    private void updateConfirmadorVendedor(String message, Color color, boolean enableButton) {
        confirmadorVendedorTxt.setText(message);
        confirmadorVendedorTxt.setForeground(color);
        crearNuevoVendedorButton.setEnabled(enableButton);
    }

    private void crearNuevoVendedor() {
        String correo = correoTextField.getText().trim();
        if (correo.isEmpty() || correo.contains(" ")) {
            showErrorMessage("Ingrese un correo válido sin espacios.");
            return;
        }

        String nombre = "";
        while (true) {
            nombre = JOptionPane.showInputDialog("Ingrese el nombre del nuevo vendedor:").trim();
            if (nombre.isEmpty()) {
                showErrorMessage("El nombre no puede estar vacío.");
            } else if (esNombreValido(nombre) && !nombre.contains(" ")) {
                nombre = capitalizar(nombre);
                break;
            } else {
                showErrorMessage("El nombre no debe contener caracteres especiales, números ni espacios.");
            }
        }

        String apellido = "";
        while (true) {
            apellido = JOptionPane.showInputDialog("Ingrese el apellido del nuevo vendedor:").trim();
            if (apellido.isEmpty()) {
                showErrorMessage("El apellido no puede estar vacío.");
            } else if (esNombreValido(apellido) && !apellido.contains(" ")) {
                apellido = capitalizar(apellido);
                break;
            } else {
                showErrorMessage("El apellido no debe contener caracteres especiales, números ni espacios.");
            }
        }

        String direccion = "";
        while (true) {
            direccion = JOptionPane.showInputDialog("Ingrese la dirección del nuevo vendedor:").trim();
            if (direccion.isEmpty()) {
                showErrorMessage("La dirección no puede estar vacía.");
            } else {
                direccion = capitalizar(direccion);
                break;
            }
        }

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

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaRegistro = dtf.format(LocalDateTime.now());

        try {
            int lastUserID = 0;
            Document lastUser = usersCollection.find().sort(new Document("UserID", -1)).first();
            if (lastUser != null) {
                lastUserID = lastUser.getInteger("UserID");
            }
            int newUserID = lastUserID + 1;

            Document nuevoVendedor = new Document("UserID", newUserID)
                    .append("Correo", correo)
                    .append("Contrasena", contrasena)
                    .append("Nombre", nombre)
                    .append("Apellido", apellido)
                    .append("Direccion", direccion)
                    .append("Telefono", telefono)
                    .append("FechaRegistro", fechaRegistro);

            usersCollection.insertOne(nuevoVendedor);
            showInformationMessage("Vendedor creado exitosamente.");
        } catch (Exception e) {
            handleError("Error al crear el nuevo vendedor: " + e.getMessage(), e);
        }
    }

    private String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto;
        }
        return texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase();
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInformationMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleError(String message, Exception e) {
        System.err.println(message);
        e.printStackTrace();
    }
}
