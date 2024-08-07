import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdminEditarProductos {
    public JPanel mainPanel;
    private JTable productosTable;
    private JTextField productIDtxt;
    private JSpinner cantidadSpinner;
    private JButton editarButton;
    private JLabel imagenProducto;
    private JLabel descripcionImagen;
    private JButton añadirButton;
    private JLabel descripcionAñadidoModificadoTxt;
    private JButton gregarProductosButton;

    private MongoDatabase database;
    private MongoCollection<Document> productosCollection;
    private MongoCollection<Document> respaldoCollection;

    public AdminEditarProductos() {
        try {
            // Inicializar MongoDB
            var mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("proyectoIsaacQuinapallo");
            productosCollection = database.getCollection("productos");
            respaldoCollection = database.getCollection("respaldo");

            // Verificar y copiar datos a la colección respaldo si es necesario
            verificarYCopiarRespaldo();

            // Cargar datos de productos
            loadProductosData();

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
            editarButton.addActionListener(e -> editarProducto());

            // Configurar botón de añadir
            añadirButton.addActionListener(e -> añadirProducto());

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verificarYCopiarRespaldo() {
        try {
            long count = respaldoCollection.countDocuments();

            if (count == 0) {
                // La colección respaldo está vacía, copiar datos de productos
                List<Document> productosItems = productosCollection.find().into(new ArrayList<>());

                if (!productosItems.isEmpty()) {
                    // Añadir la fecha actual a cada documento
                    for (Document producto : productosItems) {
                        producto.append("fecha", new Date());
                    }
                    respaldoCollection.insertMany(productosItems);
                }
            } else {
                // Eliminar documentos que hayan pasado más de 24 horas
                long ahora = System.currentTimeMillis();
                long veinticuatroHoras = 86400000L; // 24 horas en milisegundos
                respaldoCollection.deleteMany(new Document("fecha", new Document("$lt", new Date(ahora - veinticuatroHoras))));

                // Copiar los datos actuales de productos a la colección respaldo
                List<Document> productosItems = productosCollection.find().into(new ArrayList<>());

                if (!productosItems.isEmpty()) {
                    // Añadir la fecha actual a cada documento
                    for (Document producto : productosItems) {
                        producto.append("fecha", new Date());
                    }
                    respaldoCollection.insertMany(productosItems);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al verificar o copiar datos a la colección respaldo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadProductosData() {
        try {
            List<Document> productosItems = productosCollection.find().into(new ArrayList<>());

            // Crear las columnas para la tabla
            String[] columnNames = {"ProductID", "Nombre", "Marca", "Color", "Inventario"};
            Object[][] data = new Object[productosItems.size()][5];

            // Rellenar los datos de la tabla
            for (int i = 0; i < productosItems.size(); i++) {
                Document item = productosItems.get(i);
                data[i][0] = item.getString("ProductID");
                data[i][1] = item.getString("Nombre");
                data[i][2] = item.getString("Marca");
                data[i][3] = item.getString("Color");
                data[i][4] = item.getInteger("Inventario");
            }

            // Actualizar la tabla con los datos
            productosTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar los datos de productos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
            Document producto = productosCollection.find(filtro).first();

            if (producto != null) {
                if (nuevaCantidad >= 0) {
                    // Actualizar la cantidad en inventario
                    Document update = new Document("$set", new Document("Inventario", nuevaCantidad));
                    productosCollection.updateOne(filtro, update);
                    descripcionAñadidoModificadoTxt.setText("Producto actualizado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor o igual a 0.", "Cantidad no válida", JOptionPane.WARNING_MESSAGE);
                }

                loadProductosData();
            } else {
                JOptionPane.showMessageDialog(null, "Producto no encontrado.", "Producto no encontrado", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al editar el producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void añadirProducto() {
        try {
            String productID = productIDtxt.getText();
            int cantidadAñadir = (Integer) cantidadSpinner.getValue();

            if (productID.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un ProductID.", "ProductID vacío", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Document filtro = new Document("ProductID", productID);
            Document producto = productosCollection.find(filtro).first();

            if (producto != null) {
                int cantidadActual = producto.getInteger("Inventario");
                int nuevaCantidad = cantidadActual + cantidadAñadir;

                // Actualizar la cantidad en inventario
                Document update = new Document("$set", new Document("Inventario", nuevaCantidad));
                productosCollection.updateOne(filtro, update);
                descripcionAñadidoModificadoTxt.setText("Producto añadido correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "Producto no encontrado.", "Producto no encontrado", JOptionPane.ERROR_MESSAGE);
            }

            loadProductosData();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al añadir el producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
