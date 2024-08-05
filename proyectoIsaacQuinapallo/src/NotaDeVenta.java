import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NotaDeVenta {

    // Panel principal y componentes de la interfaz de usuario
    public JPanel mainPanel;
    private JButton generarNotaDeVentaButton;
    private JButton crearNuevoClienteButton;
    private JButton generarSinClienteRegistradoButton;
    private JTextField ClienteCedulaField1;
    private JLabel confirmadorClientetxt;

    // Variables de conexión con MongoDB
    private String email;
    private MongoDatabase database;
    private MongoCollection<org.bson.Document> clientesCollection;
    private MongoCollection<org.bson.Document> carritoCollection;
    private MongoCollection<org.bson.Document> productosCollection;
    private MongoCollection<org.bson.Document> comprasCollection;
    private Carrito carrito;

    public NotaDeVenta(Carrito carrito) {
        this.carrito = carrito;
        initializeMongoDB();
        readUserEmail();
        setupUIComponents();
        configureButtons();
    }

    // Inicialización de la base de datos MongoDB
    private void initializeMongoDB() {
        try {
            var mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("proyectoIsaacQuinapallo");
            clientesCollection = database.getCollection("clientes");
            carritoCollection = database.getCollection("carrito");
            productosCollection = database.getCollection("productos");
            comprasCollection = database.getCollection("compras");
        } catch (Exception e) {
            handleError("Error al conectar con la base de datos: " + e.getMessage(), e);
        }
    }

    // Leer el email del usuario desde un archivo
    private String readUserEmail() {
        try (BufferedReader reader = new BufferedReader(new FileReader("usuarioActual.txt"))) {
            return reader.readLine();
        } catch (IOException e) {
            handleError("Error al leer el archivo de usuario actual.", e);
            return null; // Retorna null si hay un error
        }
    }

    // Vaciar el archivo del usuario actual
    private void clearUserFile() {
        try (FileWriter writer = new FileWriter("usuarioActual.txt")) {
            writer.write("");
        } catch (IOException e) {
            handleError("Error al vaciar el archivo de usuario actual.", e);
        }
    }

    // Configurar los componentes de la interfaz de usuario
    private void setupUIComponents() {
        ClienteCedulaField1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                verificarCliente();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                verificarCliente();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                verificarCliente();
            }
        });
        generarNotaDeVentaButton.setEnabled(false);
    }

    // Configurar los botones de la interfaz de usuario
    private void configureButtons() {
        generarNotaDeVentaButton.addActionListener(e -> generarNotaDeVenta());
        crearNuevoClienteButton.addActionListener(e -> crearNuevoCliente());
        generarSinClienteRegistradoButton.addActionListener(e -> generarSinClienteRegistrado());
    }

    // Verificar si el cliente existe en la base de datos
    private void verificarCliente() {
        String cedulaStr = ClienteCedulaField1.getText().trim();
        if (cedulaStr.isEmpty()) {
            updateConfirmadorCliente("Ingrese una cédula.", Color.RED, false);
            return;
        }

        try {
            int cedula = Integer.parseInt(cedulaStr);
            org.bson.Document filtro = new org.bson.Document("Cedula", cedula);
            org.bson.Document cliente = clientesCollection.find(filtro).first();

            if (cliente != null) {
                updateConfirmadorCliente("Cliente registrado.", Color.GREEN, true);
            } else {
                updateConfirmadorCliente("Cliente no registrado.", Color.RED, false);
            }
        } catch (NumberFormatException e) {
            updateConfirmadorCliente("Cédula inválida.", Color.RED, false);
        }
    }

    // Actualizar el mensaje y el estado del botón de confirmación
    private void updateConfirmadorCliente(String message, Color color, boolean enableButton) {
        confirmadorClientetxt.setText(message);
        confirmadorClientetxt.setForeground(color);
        generarNotaDeVentaButton.setEnabled(enableButton);
    }

    // Generar la nota de venta
    private void generarNotaDeVenta() {
        String cedulaStr = ClienteCedulaField1.getText().trim();
        if (cedulaStr.isEmpty()) {
            showErrorMessage("Ingrese una cédula válida.");
            return;
        }

        try {
            int cedula = Integer.parseInt(cedulaStr);
            org.bson.Document filtro = new org.bson.Document("Cedula", cedula);
            org.bson.Document cliente = clientesCollection.find(filtro).first();

            if (cliente != null) {
                mostrarBotonesConfirmacionCompra(cedulaStr);
            } else {
                showErrorMessage("Cliente no registrado.");
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Cédula inválida.");
        }
    }

    // Mostrar el cuadro de confirmación de compra
    private void mostrarBotonesConfirmacionCompra(String cedula) {
        JPanel panelBotones = new JPanel();
        JButton confirmarButton = new JButton("Confirmar Compra");
        JButton cancelarButton = new JButton("Cancelar Compra");

        panelBotones.add(confirmarButton);
        panelBotones.add(cancelarButton);

        JFrame frameBotones = new JFrame("Confirmación de Compra");
        frameBotones.setContentPane(panelBotones);
        frameBotones.pack();
        frameBotones.setVisible(true);

        confirmarButton.addActionListener(e -> {
            registrarCompra(cedula);
            generarPDF();
            frameBotones.dispose();
            showInformationMessage("Compra confirmada.");
            clearUserFile();
        });

        cancelarButton.addActionListener(e -> frameBotones.dispose());
    }

    // Generar el PDF de la nota de venta
    private void generarPDF() {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("NotaDeVenta.pdf"));
            document.open();
            document.add(new Paragraph("NOTA DE VENTA"));
            document.add(new Paragraph("------------------------"));
            document.add(new Paragraph("Datos del Vendedor:"));
            document.add(new Paragraph("------------------------"));
            addSellerDataToPDF(document);
            addClientDataToPDF(document);
            addProductsDataToPDF(document);
            document.close();
            showInformationMessage("PDF generado exitosamente.");
            notificarStockReducido();
        } catch (DocumentException | IOException e) {
            handleError("Error al generar el PDF: " + e.getMessage(), e);
        }
    }

    // Agregar datos del vendedor al PDF
    private void addSellerDataToPDF(Document document) {
        try {
            org.bson.Document userLogin = database.getCollection("userslogin")
                    .find(new org.bson.Document("Correo", email))
                    .first();
            if (userLogin != null) {
                document.add(new Paragraph("Correo: " + email));
                document.add(new Paragraph("Nombre: " + userLogin.getString("Nombre")));
                document.add(new Paragraph("Apellido: " + userLogin.getString("Apellido")));
                document.add(new Paragraph("Teléfono: " + userLogin.getString("Telefono")));
                updateLastSaleDate();
            } else {
                document.add(new Paragraph("No se encontraron datos para el correo: " + email));
            }
        } catch (Exception e) {
            handleError("Error al recuperar datos de userslogin: " + e.getMessage(), e);
        }
    }

    // Actualizar la fecha de la última venta en la base de datos
    private void updateLastSaleDate() {
        database.getCollection("userslogin").updateOne(
                new org.bson.Document("Correo", email),
                new org.bson.Document("$set", new org.bson.Document("UltimaVenta", new java.util.Date()))
        );
    }

    // Agregar datos del cliente al PDF
    private void addClientDataToPDF(Document document) {
        try {
            org.bson.Document cliente = clientesCollection.find().first();
            if (cliente != null) {
                document.add(new Paragraph("------------------------"));
                document.add(new Paragraph("Datos del Cliente:"));
                document.add(new Paragraph("------------------------"));
                document.add(new Paragraph("Cédula del Cliente: " + cliente.getInteger("Cedula")));
                document.add(new Paragraph("Nombre: " + cliente.getString("Nombre")));
                document.add(new Paragraph("Apellido: " + cliente.getString("Apellido")));
                document.add(new Paragraph("Dirección: " + cliente.getString("Direccion")));
                document.add(new Paragraph("-----------------------------------------------------"));
                document.add(new Paragraph(" "));
            } else {
                document.add(new Paragraph("No se encontraron datos del cliente."));
            }
        } catch (Exception e) {
            handleError("Error al recuperar datos del cliente: " + e.getMessage(), e);
        }
    }

    // Agregar datos de los productos al PDF
    private void addProductsDataToPDF(Document document) {
        try {
            List<org.bson.Document> carritoItems = carritoCollection.find().into(new ArrayList<>());
            if (carritoItems.isEmpty()) {
                document.add(new Paragraph("No se encontraron productos en el carrito."));
            } else {
                document.add(new Paragraph("Productos Comprados:"));
                for (org.bson.Document item : carritoItems) {
                    String productID = item.getString("ProductID");
                    int cantidad = item.getInteger("Cantidad");
                    double total = item.getDouble("Total");
                    org.bson.Document filtroProducto = new org.bson.Document("ProductID", productID);
                    org.bson.Document producto = productosCollection.find(filtroProducto).first();
                    if (producto != null) {
                        document.add(new Paragraph("Producto ID: " + productID));
                        document.add(new Paragraph("Nombre: " + producto.getString("Nombre")));
                        document.add(new Paragraph("Marca: " + producto.getString("Marca")));
                        document.add(new Paragraph("Color: " + producto.getString("Color")));
                        document.add(new Paragraph("Cantidad: " + cantidad));
                        document.add(new Paragraph("Total: $" + String.format("%.2f", total)));
                        document.add(new Paragraph("------------------------"));
                    } else {
                        document.add(new Paragraph("No se encontró el producto con ID: " + productID));
                    }
                }
            }
        } catch (Exception e) {
            handleError("Error al recuperar datos de productos: " + e.getMessage(), e);
        }
    }

    // Notificar sobre la reducción del stock
    private void notificarStockReducido() {
        try {
            carrito.reducirStock();
        } catch (Exception e) {
            handleError("Error al reducir el stock: " + e.getMessage(), e);
        }
    }

    // Crear un nuevo cliente
    private void crearNuevoCliente() {
        try {
            String cedulaStr = JOptionPane.showInputDialog("Ingrese la cédula del nuevo cliente:");
            if (cedulaStr != null) {
                int cedula = Integer.parseInt(cedulaStr);
                org.bson.Document filtro = new org.bson.Document("Cedula", cedula);
                org.bson.Document clienteExistente = clientesCollection.find(filtro).first();
                if (clienteExistente != null) {
                    showErrorMessage("Cliente ya registrado.");
                } else {
                    String nombre = JOptionPane.showInputDialog("Ingrese el nombre del nuevo cliente:");
                    String apellido = JOptionPane.showInputDialog("Ingrese el apellido del nuevo cliente:");
                    String correoElectronico = JOptionPane.showInputDialog("Ingrese el correo electrónico del nuevo cliente:");
                    String telefono = JOptionPane.showInputDialog("Ingrese el teléfono del nuevo cliente:");
                    String direccion = JOptionPane.showInputDialog("Ingrese la dirección del nuevo cliente:");
                    String ciudad = JOptionPane.showInputDialog("Ingrese la ciudad del nuevo cliente:");
                    String codigoPostal = JOptionPane.showInputDialog("Ingrese el código postal del nuevo cliente:");

                    // Obtén la fecha de registro actual
                    java.util.Date fechaRegistro = new java.util.Date();

                    // Crear y agregar el nuevo cliente
                    org.bson.Document nuevoCliente = new org.bson.Document("Cedula", cedula)
                            .append("Nombre", nombre)
                            .append("Apellido", apellido)
                            .append("CorreoElectronico", correoElectronico)
                            .append("Telefono", telefono)
                            .append("Direccion", direccion)
                            .append("Ciudad", ciudad)
                            .append("CodigoPostal", codigoPostal)
                            .append("FechaRegistro", fechaRegistro);
                    clientesCollection.insertOne(nuevoCliente);
                    showInformationMessage("Cliente creado exitosamente.");
                }
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Cédula inválida.");
        } catch (Exception e) {
            handleError("Error al crear el nuevo cliente: " + e.getMessage(), e);
        }
    }

    // Generar la nota de venta sin cliente registrado
    private void generarSinClienteRegistrado() {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("NotaDeVenta.pdf"));
            document.open();

            // Añadir info del Vendedor
            document.add(new Paragraph("------------------------"));
            document.add(new Paragraph("Datos del Vendedor:"));
            document.add(new Paragraph("------------------------"));
            addSellerDataToPDF(document);

            // Añadir datos del cliente como "Consumidor Final"
            document.add(new Paragraph("------------------------"));
            document.add(new Paragraph("Datos del Cliente:"));
            document.add(new Paragraph("------------------------"));
            document.add(new Paragraph("Cédula del Cliente: 9999999999"));
            document.add(new Paragraph("Nombre: Consumidor Final"));
            document.add(new Paragraph("Apellido: Consumidor Final"));
            document.add(new Paragraph("Dirección: Consumidor Final"));
            document.add(new Paragraph("-----------------------------------------------------"));
            document.add(new Paragraph(" "));

            // Agregar los productos vendidos
            addProductsDataToPDF(document);

            document.close();

            // Mostrar el cuadro de confirmación de compra con cédula "9999999999"
            mostrarBotonesConfirmacionCompra("9999999999");
        } catch (Exception e) {
            handleError("Error al generar la nota de venta sin cliente registrado: " + e.getMessage(), e);
        }
    }

    // Mostrar mensajes de error
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Mostrar mensajes de información
    private void showInformationMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    // Manejar errores
    private void handleError(String message, Exception e) {
        System.err.println(message);
        e.printStackTrace();
    }

    // Subir Compra al Sistema compras
    private void registrarCompra(String cedula) {
        // Obtener el UserID desde el archivo
        String userID = readUserEmail();
        // Revisar si se envía
        System.out.println("UserID: " + userID);

        // Obtener el ClienteID
        String clienteID = cedula != null ? cedula : "Sin Registro";
        System.out.println("ClienteID: " + clienteID);

        try {
            // Obtener el último CompraID
            int lastCompraID = 0;
            org.bson.Document lastCompra = comprasCollection.find().sort(new org.bson.Document("CompraID", -1)).first();
            if (lastCompra != null) {
                lastCompraID = lastCompra.getInteger("CompraID");
            }
            int newCompraID = lastCompraID + 1; // Nuevo CompraID
            System.out.println("NewCompraID: " + newCompraID);

            // Obtener los items del carrito
            List<org.bson.Document> carritoItems = carritoCollection.find().into(new ArrayList<>());
            System.out.println("Número de items en el carrito: " + carritoItems.size());

            if (carritoItems.isEmpty()) {
                showErrorMessage("El carrito está vacío.");
                return;
            }

            // Registrar cada item de la compra
            for (org.bson.Document item : carritoItems) {
                String productID = item.getString("ProductID");
                int cantidad = item.getInteger("Cantidad");
                double total = item.getDouble("Total");

                // Mostrar información del item
                System.out.println("Registrando item:");
                System.out.println("ProductID: " + productID);
                System.out.println("Cantidad: " + cantidad);
                System.out.println("Total: " + total);

                org.bson.Document compra = new org.bson.Document("CompraID", newCompraID)
                        .append("UserID", userID)
                        .append("ClienteID", clienteID)
                        .append("ProductID", productID)
                        .append("Cantidad", cantidad)
                        .append("Total", total)
                        .append("FechaCompra", new java.util.Date());

                // Mostrar documento de compra antes de insertarlo
                System.out.println("Documento de compra: " + compra.toJson());

                comprasCollection.insertOne(compra);
            }

        } catch (Exception e) {
            handleError("Error al registrar la compra: " + e.getMessage(), e);
        }
    }
}
