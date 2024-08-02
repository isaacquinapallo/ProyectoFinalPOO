import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class CategoriasVendedor {
    public JPanel mainPanel;
    private JButton deportivoButton;
    private JButton casualButton;
    private JButton formalButton;
    private JLabel tituloArticulo1;
    private JLabel img1;
    private JLabel precio1;
    private JLabel categoria1;
    private JLabel marca1;
    private JButton comprarButton1;
    private JLabel tituloArticulo2;
    private JLabel img2;
    private JLabel precio2;
    private JLabel categoria2;
    private JLabel marca2;
    private JButton comprarButton2;
    private JLabel tituloArticulo3;
    private JLabel img3;
    private JLabel precio3;
    private JLabel categoria3;
    private JLabel marca3;
    private JButton comprarButton3;
    private static MongoDatabase database;
    private static MongoCollection<Document> collection;

    public CategoriasVendedor() {
        try {
            // Inicializar MongoDB
            var mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("proyectoIsaacQuinapallo");
            collection = database.getCollection("productos");

            // Configurar productos
            //Producto 1 (Nombre,Marca,Precio,Categoria,Tamano,Color,Inventario,URLImagen,Calificacion)
            handleCategoryButtonClick("1A", tituloArticulo1, precio1, categoria1, marca1, img1);
            //Producto 2 (Nombre,Marca,Precio,Categoria,Tamano,Color,Inventario,URLImagen,Calificacion)
            handleCategoryButtonClick("1B", tituloArticulo2, precio2, categoria2, marca2, img2);
            //Producto 3 (Nombre,Marca,Precio,Categoria,Tamano,Color,Inventario,URLImagen,Calificacion)
            handleCategoryButtonClick("1C", tituloArticulo3, precio3, categoria3, marca3, img3);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // DEPORTIVOS
        deportivoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Producto 1 (Nombre,Marca,Precio,Categoria,Tamano,Color,Inventario,URLImagen,Calificacion)
                handleCategoryButtonClick("1A", tituloArticulo1, precio1, categoria1, marca1, img1);
                //Producto 2 (Nombre,Marca,Precio,Categoria,Tamano,Color,Inventario,URLImagen,Calificacion)
                handleCategoryButtonClick("1B", tituloArticulo2, precio2, categoria2, marca2, img2);
                //Producto 3 (Nombre,Marca,Precio,Categoria,Tamano,Color,Inventario,URLImagen,Calificacion)
                handleCategoryButtonClick("1C", tituloArticulo3, precio3, categoria3, marca3, img3);

            }
        });

        // CASUAL
        casualButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Producto 1 (Nombre,Marca,Precio,Categoria,Tamano,Color,Inventario,URLImagen,Calificacion)
                handleCategoryButtonClick("1D", tituloArticulo1, precio1, categoria1, marca1, img1);
                //Producto 2 (Nombre,Marca,Precio,Categoria,Tamano,Color,Inventario,URLImagen,Calificacion)
                handleCategoryButtonClick("1E", tituloArticulo2, precio2, categoria2, marca2, img2);
                //Producto 3 (Nombre,Marca,Precio,Categoria,Tamano,Color,Inventario,URLImagen,Calificacion)
                handleCategoryButtonClick("1F", tituloArticulo3, precio3, categoria3, marca3, img3);
            }
        });

        // FORMAL
        formalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Producto 1 (Nombre,Marca,Precio,Categoria,Tamano,Color,Inventario,URLImagen,Calificacion)
                handleCategoryButtonClick("1G", tituloArticulo1, precio1, categoria1, marca1, img1);
                //Producto 2 (Nombre,Marca,Precio,Categoria,Tamano,Color,Inventario,URLImagen,Calificacion)
                handleCategoryButtonClick("1H", tituloArticulo2, precio2, categoria2, marca2, img2);
                //Producto 3 (Nombre,Marca,Precio,Categoria,Tamano,Color,Inventario,URLImagen,Calificacion)
                handleCategoryButtonClick("1I", tituloArticulo3, precio3, categoria3, marca3, img3);
            }
        });
    }

    //Cambiar esto de acuerdo a las tallas, Colores y Disponibilidad
    private void configureProduct(String productId, JLabel titulo, JLabel precio, JLabel categoria, JLabel marca, JLabel img) {
        try {
            Document product = collection.find(new Document("ProductID", productId)).first();
            if (product != null) {
                String nombre = product.getString("Nombre");
                String marcaValue = product.getString("Marca");
                String categoriaValue = product.getString("Categoria");
                String urlImagen = product.getString("URLImagen");
                double precioValue = product.getDouble("Precio");

                // Imprimir en la terminal para verificar
                System.out.println("ProductID " + productId + ":");
                System.out.println("Nombre: " + nombre);
                System.out.println("Marca: " + marcaValue);
                System.out.println("Categoria: " + categoriaValue);
                System.out.println("URL Imagen: " + urlImagen);
                System.out.println("Precio: " + precioValue);

                // Configurar JLabel
                titulo.setText(nombre);
                precio.setText("Precio: $" + String.format("%.2f", precioValue));
                categoria.setText("Categoria: " + categoriaValue); //
                marca.setText("Marca: " + marcaValue);

                // Mostrar imagen
                try {
                    URL url = new URL(urlImagen);
                    Image image = ImageIO.read(url);
                    ImageIcon icon = new ImageIcon(image.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                    img.setIcon(icon);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("No se encontró el producto con ProductID " + productId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleCategoryButtonClick(String productId, JLabel titulo, JLabel precio, JLabel categoria, JLabel marca, JLabel img) {
        try {
            configureProduct(productId, titulo, precio, categoria, marca, img);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
