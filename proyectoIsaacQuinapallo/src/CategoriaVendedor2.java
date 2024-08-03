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
    public JPanel mainPanel;
    private JLabel imagenProducto;
    private JButton comprarButton;
    private JComboBox<String> colorbox;
    private JComboBox<String> tamanobox;
    private JLabel nombretxt;
    private JLabel marcatxt;
    private JLabel categoriatxt;
    private JLabel preciotxt;
    private JLabel inventariotxt;
    private JLabel descripciontxt;
    private JLabel reseñastxt;

    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public CategoriaVendedor2(String tituloProducto) {
        try {
            // Inicializar MongoDB
            var mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("proyectoIsaacQuinapallo");
            collection = database.getCollection("productos");

            // Buscar el producto por el título
            configureProductByTitle(tituloProducto);

            // Añadir ActionListener a los JComboBox
            colorbox.addActionListener(e -> updateProductDetails(tituloProducto));
            tamanobox.addActionListener(e -> updateProductDetails(tituloProducto));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configureProductByTitle(String titulo) {
        try {
            // Buscar los productos con el mismo nombre en la base de datos
            List<Document> products = collection.find(new Document("Nombre", titulo)).into(new ArrayList<>());

            if (!products.isEmpty()) {
                // Listas para colores y tamaños
                List<String> colores = new ArrayList<>();
                List<String> tamanos = new ArrayList<>();

                // Tomar el primer producto para detalles generales
                Document firstProduct = products.get(0);

                // Establecer detalles generales
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

                // Verificar que los valores no sean nulos antes de asignar a los JLabels
                nombretxt.setText(nombre != null ? nombre : "No disponible");
                preciotxt.setText(precioValue != null ? "Precio: $" + String.format("%.2f", precioValue) : "Precio: No disponible");
                categoriatxt.setText(categoriaValue != null ? "Categoria: " + categoriaValue : "Categoria: No disponible");
                marcatxt.setText(marcaValue != null ? "Marca: " + marcaValue : "Marca: No disponible");
                descripciontxt.setText(descripcion != null ? "Descripción: " + descripcion : "Descripción: No disponible");
                reseñastxt.setText(calificacion != null ? "Calificación: " + calificacion : "Calificación: No disponible");
                inventariotxt.setText(inventarioValue);

                // Obtener todos los colores y tamaños únicos
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

                // Configurar JComboBoxes
                colorbox.setModel(new DefaultComboBoxModel<>(colores.toArray(new String[0])));
                tamanobox.setModel(new DefaultComboBoxModel<>(tamanos.toArray(new String[0])));

                // Mostrar imagen
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
            e.printStackTrace();
        }
    }

    private void updateProductDetails(String titulo) {
        try {
            // Obtener el color y tamaño seleccionados
            String selectedColor = (String) colorbox.getSelectedItem();
            String selectedSize = (String) tamanobox.getSelectedItem();

            // Imprimir los valores seleccionados para depuración
            System.out.println("Color seleccionado: " + selectedColor);
            System.out.println("Tamaño seleccionado: " + selectedSize);

            // Convertir tamaño a entero
            int size = Integer.parseInt(selectedSize);

            // Buscar el producto con el nombre, color y tamaño seleccionados
            Document filter = new Document("Nombre", titulo)
                    .append("Color", selectedColor)
                    .append("Tamano", size); // Asegurar que Tamano es un entero

            // Imprimir el filtro para depuración
            System.out.println("Filtro de búsqueda: " + filter.toJson());

            Document product = collection.find(filter).first();

            if (product != null) {
                // Establecer detalles del producto
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

                // Verificar que los valores no sean nulos antes de asignar a los JLabels
                nombretxt.setText(nombre != null ? nombre : "No disponible");
                preciotxt.setText(precioValue != null ? "Precio: $" + String.format("%.2f", precioValue) : "Precio: No disponible");
                categoriatxt.setText(categoriaValue != null ? "Categoria: " + categoriaValue : "Categoria: No disponible");
                marcatxt.setText(marcaValue != null ? "Marca: " + marcaValue : "Marca: No disponible");
                descripciontxt.setText(descripcion != null ? "Descripción: " + descripcion : "Descripción: No disponible");
                reseñastxt.setText(calificacion != null ? "Calificación: " + calificacion : "Calificación: No disponible");
                inventariotxt.setText(inventarioValue);

                // Mostrar imagen
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
                        e.printStackTrace();
                        imagenProducto.setText("Error al cargar la imagen");
                    }
                } else {
                    imagenProducto.setText("Imagen no disponible");
                }
            } else {
                System.out.println("No se encontró el producto con los filtros seleccionados.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private String getStringOrDefault(Document document, String key) {
        Object value = document.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        }
        return value.toString(); // Convierte a String si es otro tipo
    }
}
