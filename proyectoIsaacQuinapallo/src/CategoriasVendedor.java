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
    private JMenuItem Carrito;
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
        //Funcion Boton 1
        comprarButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtener el título del producto desde el JLabel
                String titulo = tituloArticulo1.getText();

                // Crear la instancia de CategoriaVendedor2
                CategoriaVendedor2 formInstance = new CategoriaVendedor2(titulo);

                // Configurar el JFrame
                JFrame frameForm3 = new JFrame("Detalles del Producto");
                frameForm3.setContentPane(formInstance.mainPanel);
                frameForm3.pack();
                frameForm3.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frameForm3.setVisible(true);
            }
        });
        // Función para el Botón 2
        comprarButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtener el texto del título del segundo producto
                String tituloProducto = tituloArticulo2.getText();
                // Crear y mostrar la ventana para CategoriaVendedor2 con el título del producto
                JFrame frameForm3 = new JFrame();
                CategoriaVendedor2 formInstance = new CategoriaVendedor2(tituloProducto);
                frameForm3.setContentPane(formInstance.mainPanel);
                frameForm3.pack();
                frameForm3.setVisible(true);
            }
        });
        // Función para el Botón 3
        comprarButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtener el texto del título del tercer producto
                String tituloProducto = tituloArticulo3.getText();
                // Crear y mostrar la ventana para CategoriaVendedor2 con el título del producto
                JFrame frameForm3 = new JFrame();
                CategoriaVendedor2 formInstance = new CategoriaVendedor2(tituloProducto);
                frameForm3.setContentPane(formInstance.mainPanel);
                frameForm3.pack();
                frameForm3.setVisible(true);
            }
        });
        Carrito.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Crear y mostrar la ventana para Carrito
                JFrame frameForm4 = new JFrame("Carrito");
                Carrito carrito = new Carrito();
                frameForm4.setContentPane(carrito.mainPanel);
                frameForm4.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frameForm4.pack();
                frameForm4.setVisible(true);
            }
        });
    }

    //Titulo,Precio,Cateoria,Marca,Img
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

    // Método para manejar el clic en el botón de categoría
    private void handleCategoryButtonClick(String productId, JLabel titulo, JLabel precio, JLabel categoria, JLabel marca, JLabel img) {
        try {
            // Llama al método configureProduct para configurar los detalles del producto
            configureProduct(productId, titulo, precio, categoria, marca, img);
        } catch (Exception e) {
            // Imprime el stack trace del error en la consola para depuración
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
