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

public class NotaDeVenta {
    public JPanel mainPanel;
    private JButton generarNotaDeVentaButton;
    private JButton crearNuevoClienteButton;
    private JButton generarSinClienteRegistradoButton;
    private JTextField ClienteCedulaField1;
    private JLabel confirmadorClientetxt;

    private MongoDatabase database;
    private MongoCollection<org.bson.Document> clientesCollection;
    private MongoCollection<org.bson.Document> carritoCollection; // Añadir esta línea
    private Carrito carrito; // Referencia a la clase Carrito

    public NotaDeVenta(Carrito carrito) {
        this.carrito = carrito; // Inicializar la referencia

        try {
            // Inicializar MongoDB
            var mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("proyectoIsaacQuinapallo");
            clientesCollection = database.getCollection("clientes");
            carritoCollection = database.getCollection("carrito"); // Añadir esta línea

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
            carrito.reducirStock(); // Llamar al método para reducir el stock en la base de datos
            generarPDF(cedula); // Generar el PDF con la nota de venta
            frameBotones.dispose(); // Cerrar ventana de confirmación
            JOptionPane.showMessageDialog(null, "Compra confirmada y carrito vaciado.", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        });

        cancelarButton.addActionListener(e -> {
            // Lógica para cancelar la compra
            frameBotones.dispose(); // Cerrar ventana de confirmación
        });
    }

    private void generarPDF(String cedula) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("NotaDeVenta.pdf"));
            document.open();
            document.add(new Paragraph("Nota de Venta"));
            document.add(new Paragraph("Cédula del Cliente: " + cedula));

            // Traer datos del cliente desde la colección de clientes
            org.bson.Document filtro = new org.bson.Document("Cedula", Integer.parseInt(cedula));
            org.bson.Document cliente = clientesCollection.find(filtro).first();

            if (cliente != null) {
                document.add(new Paragraph("Nombre: " + cliente.getString("Nombre")));
                document.add(new Paragraph("Apellido: " + cliente.getString("Apellido")));
                document.add(new Paragraph("Dirección: " + cliente.getString("Direccion")));
            }

            // Agregar datos de los productos del carrito
            List<org.bson.Document> carritoItems = carritoCollection.find().into(new ArrayList<>());
            for (org.bson.Document item : carritoItems) {
                document.add(new Paragraph("Producto ID: " + item.getString("ProductID")));
                document.add(new Paragraph("Cantidad: " + item.getInteger("Cantidad")));
                document.add(new Paragraph("Total: $" + String.format("%.2f", item.getDouble("Total"))));
            }

            document.close();
            JOptionPane.showMessageDialog(null, "PDF generado exitosamente.", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al generar el PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearNuevoCliente() {
        // Lógica para crear un nuevo cliente
    }

    private void generarSinClienteRegistrado() {
        // Lógica para generar una nota de venta sin cliente registrado
    }
}
