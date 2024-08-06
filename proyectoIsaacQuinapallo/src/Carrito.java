import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
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
    private JLabel descripcionImagen;
    private JLabel imagenProducto;

    private MongoDatabase database;
    private MongoCollection<Document> carritoCollection;
    private MongoCollection<Document> productosCollection;

    public Carrito() {
        try {
            // Inicializar MongoDB
            var mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("proyectoIsaacQuinapallo");
            carritoCollection = database.getCollection("carrito");
            productosCollection = database.getCollection("productos");

            // Cargar datos del carrito
            loadCarritoData();
            calcularTotalPagar();

            // Agregar DocumentListener a productIDtxt
            productIDtxt.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                @Override
                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    actualizarInformacionProducto();
                }

                @Override
                public void removeUpdate(javax.swing.event.DocumentEvent e) {
                    actualizarInformacionProducto();
                }

                @Override
                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                    actualizarInformacionProducto();
                }
            });

            // Configurar botón de edición
            editarButton.addActionListener(e -> {
                editarProducto();
                calcularTotalPagar(); // Calcular el total después de editar un producto
            });

            // Configurar botón de pagar
            pagarButton.addActionListener(e -> confirmarYProceder());

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
                // Redondear el total a dos decimales
                data[i][2] = String.format("%.2f", item.getDouble("Total"));
            }

            // Actualizar la tabla con los datos
            carritoTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar los datos del carrito: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarInformacionProducto() {
        String productID = productIDtxt.getText();

        if (productID.isEmpty()) {
            return;
        }

        Document filtro = new Document("ProductID", productID);
        Document producto = productosCollection.find(filtro).first();

        if (producto != null) {
            // Actualizar la imagen
            try {
                String urlImagen = producto.getString("URLImagen");
                URL url = new URL(urlImagen);
                ImageIcon icon = new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
                imagenProducto.setIcon(icon);
            } catch (Exception e) {
                e.printStackTrace();
                imagenProducto.setIcon(null); // Clear the image if there's an error
            }

            // Actualizar la descripción
            String nombre = producto.getString("Nombre");
            String marca = producto.getString("Marca");
            String color = producto.getString("Color");
            descripcionImagen.setText(nombre + " " + marca + " de color " + color);
        } else {
            imagenProducto.setIcon(null);
            descripcionImagen.setText("");
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
            Document productoEnCarrito = carritoCollection.find(filtro).first();
            Document producto = productosCollection.find(filtro).first();

            if (producto != null) {
                if (productoEnCarrito != null) {
                    // El producto ya está en el carrito, actualizar la cantidad
                    if (nuevaCantidad == 0) {
                        carritoCollection.deleteOne(filtro);
                    } else {
                        double precioUnitario = productoEnCarrito.getDouble("Total") / productoEnCarrito.getInteger("Cantidad");
                        double nuevoTotal = nuevaCantidad * precioUnitario;

                        Document update = new Document("$set", new Document("Cantidad", nuevaCantidad).append("Total", nuevoTotal));
                        carritoCollection.updateOne(filtro, update);
                    }
                } else {
                    // El producto no está en el carrito, añadirlo
                    if (nuevaCantidad > 0) {
                        double precioUnitario = producto.getDouble("Precio"); // Suponiendo que tienes un campo "Precio" en productos
                        double total = nuevaCantidad * precioUnitario;

                        Document nuevoItem = new Document("ProductID", productID)
                                .append("Cantidad", nuevaCantidad)
                                .append("Total", total);
                        carritoCollection.insertOne(nuevoItem);
                    } else {
                        JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor que 0 para añadir un nuevo producto al carrito.", "Cantidad inválida", JOptionPane.WARNING_MESSAGE);
                    }
                }

                loadCarritoData();
                calcularTotalPagar(); // Calcular el total después de añadir o editar un producto

            } else {
                JOptionPane.showMessageDialog(null, "Producto no encontrado en la colección de productos.", "Producto no encontrado", JOptionPane.ERROR_MESSAGE);
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

    private void confirmarYProceder() {
        mostrarNotaDeVenta(); // Mostrar la nota de venta
    }

    public void reducirStock() {
        try {
            List<Document> carritoItems = carritoCollection.find().into(new ArrayList<>());

            for (Document item : carritoItems) {
                String productID = item.getString("ProductID");
                int cantidadCarrito = item.getInteger("Cantidad");

                // Buscar el producto en la colección de productos
                Document filtroProducto = new Document("ProductID", productID);
                Document producto = productosCollection.find(filtroProducto).first();

                if (producto != null) {
                    int cantidadActual = producto.getInteger("Inventario");
                    int nuevaCantidad = cantidadActual - cantidadCarrito;

                    // Actualizar la cantidad en inventario
                    Document update = new Document("$set", new Document("Inventario", nuevaCantidad));
                    productosCollection.updateOne(filtroProducto, update);
                }
            }

            // Vaciar el carrito después de la compra
            carritoCollection.deleteMany(new Document());

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al reducir el stock: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarNotaDeVenta() {
        // Crear y mostrar la ventana para NotaDeVenta
        JFrame frameNotaDeVenta = new JFrame("Nota de Venta");
        NotaDeVenta notaDeVenta = new NotaDeVenta(this); // Inicialmente, no pasamos el email
        frameNotaDeVenta.setContentPane(notaDeVenta.mainPanel);
        frameNotaDeVenta.pack();
        frameNotaDeVenta.setVisible(true);
    }
}