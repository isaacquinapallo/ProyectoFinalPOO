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
    private JButton gregarProductosNuevosButton; // Botón para añadir nuevos productos
    private JButton EliminarProductosbutton;

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

            // Verificar y copiar datos a la colección respaldo si está vacía
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

            // Configurar botón de añadir nuevos productos
            gregarProductosNuevosButton.addActionListener(e -> agregarNuevoProducto());

            // Configurar botón de eliminar productos
            EliminarProductosbutton.addActionListener(e -> eliminarProducto());


        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verificarYCopiarRespaldo() {
        try {
            // Verificar si la colección respaldo está vacía
            long count = respaldoCollection.countDocuments();

            if (count == 0) {
                // La colección respaldo está vacía, copiar datos de productos y añadir fecha actual
                copiarDatosConFechaActual();
            } else {
                // La colección respaldo no está vacía, verificar la fecha
                Document últimoDocumento = respaldoCollection.find().sort(new Document("fecha", -1)).first();
                if (últimoDocumento != null) {
                    Date fechaÚltimaCopia = últimoDocumento.getDate("fecha");
                    long diferenciaHoras = (new Date().getTime() - fechaÚltimaCopia.getTime()) / (1000 * 60 * 60);

                    if (diferenciaHoras >= 24) {
                        // Han pasado más de 24 horas, eliminar la colección y copiar datos de productos
                        respaldoCollection.drop();
                        copiarDatosConFechaActual();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al verificar o copiar datos a la colección respaldo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void copiarDatosConFechaActual() {
        try {
            // Obtener datos de productos
            List<Document> productosItems = productosCollection.find().into(new ArrayList<>());

            if (!productosItems.isEmpty()) {
                // Añadir la fecha actual a cada documento
                Date fechaActual = new Date();
                for (Document producto : productosItems) {
                    producto.append("fecha", fechaActual);
                }

                // Insertar documentos en la colección respaldo
                respaldoCollection.insertMany(productosItems);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al copiar datos con fecha actual a la colección respaldo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadProductosData() {
        try {
            List<Document> productosItems = productosCollection.find().into(new ArrayList<>());

            // Crear las columnas para la tabla
            String[] columnNames = {"ProductID", "Nombre", "Marca", "Categoria", "Tamano", "Color", "Precio", "Inventario", "Descripcion", "URLImagen", "Calificacion"};
            Object[][] data = new Object[productosItems.size()][11];

            // Rellenar los datos de la tabla
            for (int i = 0; i < productosItems.size(); i++) {
                Document item = productosItems.get(i);
                data[i][0] = item.getString("ProductID");
                data[i][1] = item.getString("Nombre");
                data[i][2] = item.getString("Marca");
                data[i][3] = item.getString("Categoria");
                data[i][4] = item.getInteger("Tamano");
                data[i][5] = item.getString("Color");
                data[i][6] = item.getDouble("Precio");
                data[i][7] = item.getInteger("Inventario");
                data[i][8] = item.getString("Descripcion");
                data[i][9] = item.getString("URLImagen");
                data[i][10] = item.getDouble("Calificacion");
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

    private void agregarNuevoProducto() {
        try {
            String productID = obtenerProductID();
            String nombre = obtenerValorNoVacio("Ingrese el Nombre:");
            String marca = obtenerValorNoVacio("Ingrese la Marca:");
            String categoria = obtenerValorNoVacio("Ingrese la Categoria:");

            int tamano = obtenerValorEntero("Ingrese el Tamano:");
            String color = obtenerValorNoVacio("Ingrese el Color:");
            double precio = obtenerValorDecimal("Ingrese el Precio:");
            int inventario = obtenerValorEntero("Ingrese el Inventario:");
            String descripcion = obtenerValorNoVacio("Ingrese la Descripcion:");
            String urlImagen = obtenerValorNoVacio("Ingrese la URL de la Imagen:");
            double calificacion = obtenerValorDecimal("Ingrese la Calificacion:");

            // Crear un nuevo documento para el producto
            Document nuevoProducto = new Document("ProductID", productID)
                    .append("Nombre", nombre)
                    .append("Marca", marca)
                    .append("Categoria", categoria)
                    .append("Tamano", tamano)
                    .append("Color", color)
                    .append("Precio", precio)
                    .append("Inventario", inventario)
                    .append("Descripcion", descripcion)
                    .append("URLImagen", urlImagen)
                    .append("Calificacion", calificacion);

            // Insertar el nuevo producto en la colección
            productosCollection.insertOne(nuevoProducto);

            JOptionPane.showMessageDialog(null, "Producto añadido correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            loadProductosData(); // Recargar los datos de la tabla

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al añadir el producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String obtenerProductID() {
        while (true) {
            String input = JOptionPane.showInputDialog("Ingrese el ProductID:");
            if (input == null || input.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Error: El ProductID no puede estar vacío.", "Error de formato", JOptionPane.ERROR_MESSAGE);
            } else if (!input.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$")) {
                JOptionPane.showMessageDialog(null, "Error: El ProductID debe contener al menos una letra y un número.", "Error de formato", JOptionPane.ERROR_MESSAGE);
            } else if (productIDExists(input)) {
                JOptionPane.showMessageDialog(null, "Error: El ProductID ya existe. Por favor ingrese uno diferente.", "Error de formato", JOptionPane.ERROR_MESSAGE);
            } else {
                return input;
            }
        }
    }

    private boolean productIDExists(String productID) {
        Document query = new Document("ProductID", productID);
        return productosCollection.find(query).iterator().hasNext();
    }

    private String obtenerValorNoVacio(String mensaje) {
        while (true) {
            String input = JOptionPane.showInputDialog(mensaje);
            if (input == null || input.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Error: Este campo no puede estar vacío.", "Error de formato", JOptionPane.ERROR_MESSAGE);
            } else {
                return input;
            }
        }
    }

    private int obtenerValorEntero(String mensaje) {
        while (true) {
            try {
                String input = JOptionPane.showInputDialog(mensaje);
                if (input == null || input.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Error: Este campo no puede estar vacío.", "Error de formato", JOptionPane.ERROR_MESSAGE);
                } else {
                    return Integer.parseInt(input);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: Debe ingresar un valor entero.", "Error de formato", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private double obtenerValorDecimal(String mensaje) {
        while (true) {
            try {
                String input = JOptionPane.showInputDialog(mensaje);
                if (input == null || input.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Error: Este campo no puede estar vacío.", "Error de formato", JOptionPane.ERROR_MESSAGE);
                } else {
                    return Double.parseDouble(input);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: Debe ingresar un valor decimal.", "Error de formato", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarProducto() {
        // Mostrar el cuadro de diálogo para ingresar el ProductID
        String productID = JOptionPane.showInputDialog("Ingrese el ProductID que desea eliminar:");

        if (productID == null || productID.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error: El ProductID no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Mostrar cuadro de confirmación
        int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea eliminar el producto con ProductID: " + productID + "?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (respuesta == JOptionPane.YES_OPTION) {
            try {
                // Eliminar el producto de la colección
                Document filtro = new Document("ProductID", productID);
                long resultado = productosCollection.deleteOne(filtro).getDeletedCount();

                if (resultado > 0) {
                    JOptionPane.showMessageDialog(null, "Producto eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    loadProductosData(); // Recargar los datos de la tabla
                } else {
                    JOptionPane.showMessageDialog(null, "Producto no encontrado.", "Producto no encontrado", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al eliminar el producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


}
