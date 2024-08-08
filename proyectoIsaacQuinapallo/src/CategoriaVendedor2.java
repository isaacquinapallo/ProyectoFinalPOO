import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CategoriaVendedor2 {
    public JPanel mainPanel; // Panel principal de la interfaz
    private JLabel imagenProducto; // Etiqueta para mostrar la imagen del producto
    private JButton añadirCarritoButton; // Botón para añadir el producto al carrito
    private JComboBox<String> colorbox; // ComboBox para seleccionar el color del producto
    private JComboBox<String> tamanobox; // ComboBox para seleccionar el tamaño del producto
    private JLabel nombretxt; // Etiqueta para mostrar el nombre del producto
    private JLabel marcatxt; // Etiqueta para mostrar la marca del producto
    private JLabel categoriatxt; // Etiqueta para mostrar la categoría del producto
    private JLabel preciotxt; // Etiqueta para mostrar el precio del producto
    private JLabel inventariotxt; // Etiqueta para mostrar el inventario del producto
    private JLabel descripciontxt; // Etiqueta para mostrar la descripción del producto
    private JLabel reseñastxt; // Etiqueta para mostrar la calificación del producto
    private JSpinner cantidadSpinner; // Spinner para seleccionar la cantidad del producto

    private MongoDatabase database; // Base de datos MongoDB
    private MongoCollection<Document> collection; // Colección de productos en MongoDB

    public CategoriaVendedor2(String tituloProducto) {
        try {
            // Inicializa la conexión a MongoDB
            var mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("proyectoIsaacQuinapallo");
            collection = database.getCollection("productos");

            // Configura el producto basándose en el título proporcionado
            configureProductByTitle(tituloProducto);

            // Añade ActionListener a los JComboBox para actualizar detalles del producto al seleccionar un color o tamaño
            colorbox.addActionListener(e -> updateProductDetails(tituloProducto));
            tamanobox.addActionListener(e -> updateProductDetails(tituloProducto));

            // Añade ActionListener al botón de añadir al carrito
            añadirCarritoButton.addActionListener(e -> handleComprarButton(tituloProducto));

        } catch (Exception e) {
            // Captura y muestra errores al conectar con la base de datos
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configureProductByTitle(String titulo) {
        try {
            // Busca los productos con el nombre especificado en la base de datos
            List<Document> products = collection.find(new Document("Nombre", titulo)).into(new ArrayList<>());

            if (!products.isEmpty()) {
                // Listas para almacenar colores y tamaños únicos
                List<String> colores = new ArrayList<>();
                List<String> tamanos = new ArrayList<>();

                // Tomar el primer producto de la lista para los detalles generales
                Document firstProduct = products.get(0);

                // Establecer detalles generales del producto
                String nombre = getStringOrDefault(firstProduct, "Nombre");
                String marcaValue = getStringOrDefault(firstProduct, "Marca");
                String categoriaValue = getStringOrDefault(firstProduct, "Categoria");
                String urlImagen = getStringOrDefault(firstProduct, "URLImagen");
                Double precioValue = firstProduct.getDouble("Precio");
                String descripcion = getStringOrDefault(firstProduct, "Descripcion");
                Double calificacion = firstProduct.getDouble("Calificacion");

                // Obtener el inventario de manera segura
                Object inventarioObj = firstProduct.get("Inventario");
                String inventarioValue = "No disponible";
                if (inventarioObj != null) {
                    if (inventarioObj instanceof Integer) {
                        inventarioValue = "Inventario: " + inventarioObj.toString();
                    } else if (inventarioObj instanceof Long) {
                        inventarioValue = "Inventario: " + Long.toString((Long) inventarioObj);
                    }
                }

                // Actualizar las etiquetas con los detalles del producto
                nombretxt.setText(nombre != null ? nombre : "No disponible");
                preciotxt.setText(precioValue != null ? "Precio: $" + String.format("%.2f", precioValue) : "Precio: No disponible");
                categoriatxt.setText(categoriaValue != null ? "Categoria: " + categoriaValue : "Categoria: No disponible");
                marcatxt.setText(marcaValue != null ? "Marca: " + marcaValue : "Marca: No disponible");
                descripciontxt.setText(descripcion != null ? "Descripción: " + descripcion : "Descripción: No disponible");
                reseñastxt.setText(calificacion != null ? "Calificación: " + calificacion : "Calificación: No disponible");
                inventariotxt.setText(inventarioValue);

                // Obtener todos los colores y tamaños únicos de los productos encontrados
                for (Document product : products) {
                    String color = getStringOrDefault(product, "Color");
                    String tamano = getStringOrDefault(product, "Tamano");

                    if (color != null && !colores.contains(color)) {
                        colores.add(color);
                    }
                    if (tamano != null && !tamanos.contains(tamano)) {
                        tamanos.add(tamano);
                    }
                }

                // Configurar los JComboBox con los colores y tamaños obtenidos
                colorbox.setModel(new DefaultComboBoxModel<>(colores.toArray(new String[0])));
                tamanobox.setModel(new DefaultComboBoxModel<>(tamanos.toArray(new String[0])));

                // Mostrar la imagen del producto
                if (urlImagen != null && !urlImagen.isEmpty()) {
                    try {
                        URL url = new URL(urlImagen);
                        Image image = ImageIO.read(url);
                        if (image != null) { // Verificar si la imagen se cargó correctamente
                            ImageIcon icon = new ImageIcon(image.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                            imagenProducto.setIcon(icon);
                        } else {
                            imagenProducto.setText("Imagen no disponible");
                        }
                    } catch (IOException e) {
                        // Captura y muestra errores al cargar la imagen
                        e.printStackTrace();
                        imagenProducto.setText("Error al cargar la imagen");
                    }
                } else {
                    imagenProducto.setText("Imagen no disponible");
                }
            } else {
                System.out.println("No se encontró el producto con el título " + titulo);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Captura y muestra errores generales
        }
    }

    private void updateProductDetails(String titulo) {
        try {
            // Obtener el color y tamaño seleccionados en los JComboBox
            String selectedColor = (String) colorbox.getSelectedItem();
            String selectedSize = (String) tamanobox.getSelectedItem();

            if (selectedColor == null || selectedSize == null) {
                return; // Si no hay selección, no hacer nada
            }

            // Convertir el tamaño a entero
            int size = Integer.parseInt(selectedSize);

            // Buscar el producto con el nombre, color y tamaño seleccionados
            Document filter = new Document("Nombre", titulo)
                    .append("Color", selectedColor)
                    .append("Tamano", size); // Asegurar que Tamano es un entero

            Document product = collection.find(filter).first();

            if (product != null) {
                // Actualizar detalles del producto basado en la selección
                String productId = getStringOrDefault(product, "ProductID");
                String nombre = getStringOrDefault(product, "Nombre");
                String marcaValue = getStringOrDefault(product, "Marca");
                String categoriaValue = getStringOrDefault(product, "Categoria");
                String urlImagen = getStringOrDefault(product, "URLImagen");
                Double precioValue = product.getDouble("Precio");
                String descripcion = getStringOrDefault(product, "Descripcion");
                Double calificacion = product.getDouble("Calificacion");

                // Obtener el inventario de manera segura
                Object inventarioObj = product.get("Inventario");
                String inventarioValue = "No disponible";
                if (inventarioObj != null) {
                    if (inventarioObj instanceof Integer) {
                        inventarioValue = "Inventario: " + inventarioObj.toString();
                    } else if (inventarioObj instanceof Long) {
                        inventarioValue = "Inventario: " + Long.toString((Long) inventarioObj);
                    }
                }

                // Actualizar las etiquetas con los detalles del producto
                nombretxt.setText(nombre != null ? nombre : "No disponible");
                preciotxt.setText(precioValue != null ? "Precio: $" + String.format("%.2f", precioValue) : "Precio: No disponible");
                categoriatxt.setText(categoriaValue != null ? "Categoria: " + categoriaValue : "Categoria: No disponible");
                marcatxt.setText(marcaValue != null ? "Marca: " + marcaValue : "Marca: No disponible");
                descripciontxt.setText(descripcion != null ? "Descripción: " + descripcion : "Descripción: No disponible");
                reseñastxt.setText(calificacion != null ? "Calificación: " + calificacion : "Calificación: No disponible");
                inventariotxt.setText(inventarioValue);

                // Mostrar la imagen del producto
                if (urlImagen != null && !urlImagen.isEmpty()) {
                    try {
                        URL url = new URL(urlImagen);
                        Image image = ImageIO.read(url);
                        if (image != null) { // Verificar si la imagen se cargó correctamente
                            ImageIcon icon = new ImageIcon(image.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                            imagenProducto.setIcon(icon);
                        } else {
                            imagenProducto.setText("Imagen no disponible");
                        }
                    } catch (IOException e) {
                        // Captura y muestra errores al cargar la imagen
                        e.printStackTrace();
                        imagenProducto.setText("Error al cargar la imagen");
                    }
                } else {
                    imagenProducto.setText("Imagen no disponible");
                }
            } else {
                System.out.println("No se encontró el producto con los criterios especificados");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Captura y muestra errores generales
        }
    }

    private void handleComprarButton(String tituloProducto) {
        try {
            int cantidad = (Integer) cantidadSpinner.getValue();

            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(null, "Por favor, selecciona una cantidad válida.", "Cantidad inválida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String selectedColor = (String) colorbox.getSelectedItem();
            String selectedSize = (String) tamanobox.getSelectedItem();

            if (selectedColor == null || selectedSize == null) {
                JOptionPane.showMessageDialog(null, "Por favor, selecciona un color y un tamaño.", "Selección incompleta", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int size = Integer.parseInt(selectedSize);

            Document filter = new Document("Nombre", tituloProducto)
                    .append("Color", selectedColor)
                    .append("Tamano", size);

            Document product = collection.find(filter).first();

            if (product != null) {
                int inventarioActual = product.getInteger("Inventario", 0);
                if (inventarioActual < cantidad) {
                    JOptionPane.showMessageDialog(null, "No hay suficiente inventario para la cantidad seleccionada.", "Inventario insuficiente", JOptionPane.WARNING_MESSAGE);
                } else {
                    // Crear el documento para añadir al carrito
                    try {
                        // Obtener el ProductID
                        String productId = getStringOrDefault(product, "ProductID");

                        // Calcular el total
                        Double precio = product.getDouble("Precio");
                        double total = cantidad * precio;

                        // Obtener la colección carrito
                        MongoCollection<Document> carritoCollection = database.getCollection("carrito");

                        // Crear el documento para el carrito
                        Document carritoItem = new Document("ProductID", productId)
                                .append("Cantidad", cantidad)
                                .append("Total", total);

                        // Insertar el documento en la colección carrito
                        carritoCollection.insertOne(carritoItem);

                        // Cambiar el texto del botón a "Artículo Añadido!"
                        añadirCarritoButton.setText("Artículo Añadido!");

                    } catch (Exception e) {
                        // Captura y muestra errores al agregar al carrito
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al agregar al carrito: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Producto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            // Captura y muestra errores generales al procesar la compra
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al procesar la compra: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método auxiliar para obtener valores del documento, retornando un valor por defecto si es null
    private String getStringOrDefault(Document document, String key) {
        Object value = document.get(key);
        if (value == null) {
            return "No disponible"; // Ajustar para que no retorne null
        }
        if (value instanceof String) {
            return (String) value;
        }
        return value.toString(); // Convierte a String si es otro tipo
    }
}
