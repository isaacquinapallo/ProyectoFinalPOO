import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Carrito {
    public JPanel mainPanel;
    private JTable carritoTable;
    private JButton pagarButton;
    private JSpinner cantidadSpinner;
    private JLabel TotalPagartxt;
    private JTextField productIDtxt;
    private JButton editarButton;

    private MongoDatabase database;
    private MongoCollection<Document> carritoCollection;

    public Carrito() {
        try {
            // Inicializar MongoDB
            var mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("proyectoIsaacQuinapallo");
            carritoCollection = database.getCollection("carrito");

            // Cargar datos del carrito
            loadCarritoData();

            // Configurar botón de edición
            editarButton.addActionListener(e -> {
                editarProducto();
                calcularTotalPagar(); // Calcular el total después de editar un producto
            });

            // Configurar botón de pagar
            pagarButton.addActionListener(e -> Confirmacion());

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCarritoData() {
        try {
            List<Document> carritoItems = carritoCollection.find().into(new ArrayList<>());

            // Crear las columnas para la tabla
            String[] columnNames = {"ProductID", "Cantidad", "Total"};
            Object[][] data = new Object[carritoItems.size()][3];

            // Rellenar los datos de la tabla
            for (int i = 0; i < carritoItems.size(); i++) {
                Document item = carritoItems.get(i);
                data[i][0] = item.getString("ProductID");
                data[i][1] = item.getInteger("Cantidad");
                data[i][2] = item.getDouble("Total");
            }

            // Actualizar la tabla con los datos
            carritoTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar los datos del carrito: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarProducto() {
        try {
            String productID = productIDtxt.getText();
            int nuevaCantidad = (Integer) cantidadSpinner.getValue();

            if (productID.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un ProductID.", "ProductID vacío", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Document filtro = new Document("ProductID", productID);
            Document producto = carritoCollection.find(filtro).first();

            if (producto != null) {
                if (nuevaCantidad == 0) {
                    carritoCollection.deleteOne(filtro);
                } else {
                    double precioUnitario = producto.getDouble("Total") / producto.getInteger("Cantidad");
                    double nuevoTotal = nuevaCantidad * precioUnitario;

                    Document update = new Document("$set", new Document("Cantidad", nuevaCantidad).append("Total", nuevoTotal));
                    carritoCollection.updateOne(filtro, update);
                }

                loadCarritoData();
            } else {
                JOptionPane.showMessageDialog(null, "Producto no encontrado en el carrito.", "Producto no encontrado", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al editar el producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calcularTotalPagar() {
        try {
            List<Document> carritoItems = carritoCollection.find().into(new ArrayList<>());

            double total = 0;
            for (Document item : carritoItems) {
                total += item.getDouble("Total");
            }

            TotalPagartxt.setText("Total a Pagar: $" + String.format("%.2f", total));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al calcular el total a pagar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void Confirmacion() {
        // Crear y mostrar la ventana para ConfirmacionCompra
        JFrame frameForm3 = new JFrame();
        ConfirmacionCompra formInstance = new ConfirmacionCompra();
        frameForm3.setContentPane(formInstance.mainPanel);
        frameForm3.pack();
        frameForm3.setVisible(true);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Carrito");
        Carrito carrito = new Carrito();
        frame.setContentPane(carrito.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
