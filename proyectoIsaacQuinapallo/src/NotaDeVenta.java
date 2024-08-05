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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class NotaDeVenta {

    public JPanel mainPanel;
    private JButton generarNotaDeVentaButton;
    private JButton crearNuevoClienteButton;
    private JButton generarSinClienteRegistradoButton;
    private JTextField ClienteCedulaField1;
    private JLabel confirmadorClientetxt;

    private String email;
    private MongoDatabase database;
    private MongoCollection<org.bson.Document> clientesCollection;
    private MongoCollection<org.bson.Document> carritoCollection; // Añadir esta línea
    private MongoCollection<org.bson.Document> productosCollection; // Añadir esta línea
    private Carrito carrito; // Referencia a la clase Carrito


    public NotaDeVenta(Carrito carrito) {
        this.carrito = carrito;

        try {
            // Inicializar MongoDB
            var mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("proyectoIsaacQuinapallo");
            clientesCollection = database.getCollection("clientes");
            carritoCollection = database.getCollection("carrito"); // Añadir esta línea
            productosCollection = database.getCollection("productos"); // Añadir esta línea

            // Leer el correo del archivo
            try (BufferedReader reader = new BufferedReader(new FileReader("usuarioActual.txt"))) {
                email = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al leer el archivo de usuario actual.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            // Limpiar el archivo después de leer el correo
            try (FileWriter writer = new FileWriter("usuarioActual.txt")) {
                writer.write(""); // Escribir contenido vacío
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al vaciar el archivo de usuario actual.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Agregar DocumentListener a ClienteCedulaField1
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

            // Configurar botones
            generarNotaDeVentaButton.addActionListener(e -> generarNotaDeVenta());
            crearNuevoClienteButton.addActionListener(e -> crearNuevoCliente());
            generarSinClienteRegistradoButton.addActionListener(e -> generarSinClienteRegistrado());

            // Inicialmente deshabilitar el botón de generar nota de venta
            generarNotaDeVentaButton.setEnabled(false);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verificarCliente() {
        String cedulaStr = ClienteCedulaField1.getText().trim();

        if (cedulaStr.isEmpty()) {
            confirmadorClientetxt.setText("Ingrese una cédula.");
            return;
        }

        try {
            int cedula = Integer.parseInt(cedulaStr); // Convertir a entero
            org.bson.Document filtro = new org.bson.Document("Cedula", cedula);
            org.bson.Document cliente = clientesCollection.find(filtro).first();

            if (cliente != null) {
                confirmadorClientetxt.setText("Cliente registrado.");
                confirmadorClientetxt.setForeground(Color.GREEN);
                generarNotaDeVentaButton.setEnabled(true); // Habilitar botón
            } else {
                confirmadorClientetxt.setText("Cliente no registrado.");
                confirmadorClientetxt.setForeground(Color.RED);
                generarNotaDeVentaButton.setEnabled(false); // Deshabilitar botón
            }
        } catch (NumberFormatException e) {
            confirmadorClientetxt.setText("Cédula inválida.");
            confirmadorClientetxt.setForeground(Color.RED);
            generarNotaDeVentaButton.setEnabled(false); // Deshabilitar botón
        }
    }

    private void generarNotaDeVenta() {
        // Verificar si el cliente está registrado antes de continuar
        String cedulaStr = ClienteCedulaField1.getText().trim();
        if (cedulaStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese una cédula válida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int cedula = Integer.parseInt(cedulaStr); // Convertir a entero
            org.bson.Document filtro = new org.bson.Document("Cedula", cedula);
            org.bson.Document cliente = clientesCollection.find(filtro).first();

            if (cliente != null) {
                // Mostrar los botones de confirmación de compra
                mostrarBotonesConfirmacionCompra(cedulaStr);
            } else {
                JOptionPane.showMessageDialog(null, "Cliente no registrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Cédula inválida.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarBotonesConfirmacionCompra(String cedula) {
        // Crear un panel con los botones de confirmación
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
            // Lógica para confirmar la compra
            generarPDF(); // Generar el PDF con la nota de venta primero
            frameBotones.dispose(); // Cerrar ventana de confirmación
            JOptionPane.showMessageDialog(null, "Compra confirmada.", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        });

        cancelarButton.addActionListener(e -> {
            // Lógica para cancelar la compra
            frameBotones.dispose(); // Cerrar ventana de confirmación
        });
    }

    private void generarPDF() {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("NotaDeVenta.pdf"));
            document.open();
            document.add(new Paragraph("NOTA DE VENTA"));
            document.add(new Paragraph("------------------------"));
            document.add(new Paragraph("Datos del Vendedor:"));
            document.add(new Paragraph("------------------------"));
            // Usar el correo leído
            try {
                org.bson.Document userLogin = database.getCollection("userslogin")
                        .find(new org.bson.Document("Correo", email))
                        .first();
                if (userLogin != null) {
                    String nombre = userLogin.getString("Nombre");
                    String apellido = userLogin.getString("Apellido");
                    String telefono = userLogin.getString("Telefono");
                    document.add(new Paragraph("Correo: " + email));
                    document.add(new Paragraph("Nombre: " + nombre));
                    document.add(new Paragraph("Apellido: " + apellido));
                    document.add(new Paragraph("Teléfono: " + telefono));

                    // Actualizar la fecha de la última venta
                    database.getCollection("userslogin").updateOne(
                            new org.bson.Document("Correo", email),
                            new org.bson.Document("$set", new org.bson.Document("UltimaVenta", new java.util.Date()))
                    );
                } else {
                    document.add(new Paragraph("No se encontraron datos para el correo: " + email));
                }
            } catch (Exception e) {
                e.printStackTrace();
                document.add(new Paragraph("Error al recuperar datos de userslogin: " + e.getMessage()));
            }

            // Traer datos del cliente desde la colección de clientes
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
                e.printStackTrace();
                document.add(new Paragraph("Error al recuperar datos del cliente: " + e.getMessage()));
            }

            // Agregar datos de los productos del carrito
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
                            String nombre = producto.getString("Nombre");
                            String marca = producto.getString("Marca");
                            String color = producto.getString("Color");
                            document.add(new Paragraph("Producto ID: " + productID));
                            document.add(new Paragraph("Nombre: " + nombre));
                            document.add(new Paragraph("Marca: " + marca));
                            document.add(new Paragraph("Color: " + color));
                            document.add(new Paragraph("Cantidad: " + cantidad));
                            document.add(new Paragraph("Total: $" + String.format("%.2f", total)));
                            document.add(new Paragraph("---------------------------"));
                        } else {
                            document.add(new Paragraph("No se encontraron datos para el producto ID: " + productID));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                document.add(new Paragraph("Error al recuperar datos del carrito o productos: " + e.getMessage()));
            }

            document.close();
            JOptionPane.showMessageDialog(null, "PDF generado exitosamente.", "Confirmación", JOptionPane.INFORMATION_MESSAGE);

            notificarStockReducido();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al generar el PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void notificarStockReducido() {
        // Método que se llamará después de que se haya generado el PDF
        if (carrito != null) {
            carrito.reducirStock(); // Llamar al método para reducir el stock en la base de datos
        }
    }

    private void crearNuevoCliente() {
        // Lógica para crear un nuevo cliente
    }

    private void generarSinClienteRegistrado() {
        // Lógica para generar una nota sin cliente registrado
    }
}

