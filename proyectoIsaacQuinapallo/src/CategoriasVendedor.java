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
    private JLabel color1;
    private JLabel marca1;
    private JButton comprarButton1;
    private JLabel tituloArticulo2;
    private JLabel img2;
    private JLabel precio2;
    private JLabel color2;
    private JLabel marca2;
    private JButton comprarButton2;
    private JLabel tituloArticulo3;
    private JLabel img3;
    private JLabel precio3;
    private JLabel color3;
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

            // Recuperar producto con ProductID 1A
            Document product1 = collection.find(new Document("ProductID", "1A")).first();
            if (product1 != null) {
                String nombre1 = product1.getString("Nombre");
                String marca1Value = product1.getString("Marca");
                String color1Value = product1.getString("Color");
                String urlImagen1 = product1.getString("URLImagen");
                double precio1Value = product1.getDouble("Precio");

                // Imprimir en la terminal para verificar
                System.out.println("ProductID 1A:");
                System.out.println("Nombre: " + nombre1);
                System.out.println("Marca: " + marca1Value);
                System.out.println("Color: " + color1Value);
                System.out.println("URL Imagen: " + urlImagen1);
                System.out.println("Precio: " + precio1Value);

                // Configurar JLabel
                tituloArticulo1.setText(nombre1);
                precio1.setText("Precio: $" + String.format("%.2f", precio1Value));
                color1.setText("Color: " + color1Value);
                marca1.setText("Marca: " + marca1Value);

                // Mostrar imagen
                try {
                    URL url1 = new URL(urlImagen1);
                    Image image1 = ImageIO.read(url1);
                    ImageIcon icon1 = new ImageIcon(image1.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                    img1.setIcon(icon1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("No se encontró el producto con ProductID 1A");
            }

            // Recuperar producto con ProductID 2A
            Document product2 = collection.find(new Document("ProductID", "2A")).first();
            if (product2 != null) {
                String nombre2 = product2.getString("Nombre");
                String marca2Value = product2.getString("Marca");
                String color2Value = product2.getString("Color");
                String urlImagen2 = product2.getString("URLImagen");
                double precio2Value = product2.getDouble("Precio");

                // Imprimir en la terminal para verificar
                System.out.println("ProductID 2A:");
                System.out.println("Nombre: " + nombre2);
                System.out.println("Marca: " + marca2Value);
                System.out.println("Color: " + color2Value);
                System.out.println("URL Imagen: " + urlImagen2);
                System.out.println("Precio: " + precio2Value);

                // Configurar JLabel
                tituloArticulo2.setText(nombre2);
                precio2.setText("Precio: $" + String.format("%.2f", precio2Value));
                color2.setText("Color: " + color2Value);
                marca2.setText("Marca: " + marca2Value);

                // Mostrar imagen
                try {
                    URL url2 = new URL(urlImagen2);
                    Image image2 = ImageIO.read(url2);
                    ImageIcon icon2 = new ImageIcon(image2.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                    img2.setIcon(icon2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("No se encontró el producto con ProductID 2A");
            }

            // Recuperar producto con ProductID 3A
            Document product3 = collection.find(new Document("ProductID", "3A")).first();
            if (product3 != null) {
                String nombre3 = product3.getString("Nombre");
                String marca3Value = product3.getString("Marca");
                String color3Value = product3.getString("Color");
                String urlImagen3 = product3.getString("URLImagen");
                double precio3Value = product3.getDouble("Precio");

                // Imprimir en la terminal para verificar
                System.out.println("ProductID 3A:");
                System.out.println("Nombre: " + nombre3);
                System.out.println("Marca: " + marca3Value);
                System.out.println("Color: " + color3Value);
                System.out.println("URL Imagen: " + urlImagen3);
                System.out.println("Precio: " + precio3Value);

                // Configurar JLabel
                tituloArticulo3.setText(nombre3);
                precio3.setText("Precio: $" + String.format("%.2f", precio3Value));
                color3.setText("Color: " + color3Value);
                marca3.setText("Marca: " + marca3Value);

                // Mostrar imagen
                try {
                    URL url3 = new URL(urlImagen3);
                    Image image3 = ImageIO.read(url3);
                    ImageIcon icon3 = new ImageIcon(image3.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                    img3.setIcon(icon3);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("No se encontró el producto con ProductID 3A");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        //DEPORTIVOS
        deportivoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Inicializar MongoDB
                    var mongoClient = MongoClients.create("mongodb://localhost:27017");
                    database = mongoClient.getDatabase("proyectoIsaacQuinapallo");
                    collection = database.getCollection("productos");

                    // Recuperar producto con ProductID 1A
                    Document product1 = collection.find(new Document("ProductID", "1A")).first();
                    if (product1 != null) {
                        String nombre1 = product1.getString("Nombre");
                        String marca1Value = product1.getString("Marca");
                        String color1Value = product1.getString("Color");
                        String urlImagen1 = product1.getString("URLImagen");
                        double precio1Value = product1.getDouble("Precio");

                        // Imprimir en la terminal para verificar
                        System.out.println("ProductID 1A:");
                        System.out.println("Nombre: " + nombre1);
                        System.out.println("Marca: " + marca1Value);
                        System.out.println("Color: " + color1Value);
                        System.out.println("URL Imagen: " + urlImagen1);
                        System.out.println("Precio: " + precio1Value);

                        // Configurar JLabel
                        tituloArticulo1.setText(nombre1);
                        precio1.setText("Precio: $" + String.format("%.2f", precio1Value));
                        color1.setText("Color: " + color1Value);
                        marca1.setText("Marca: " + marca1Value);

                        // Mostrar imagen
                        try {
                            URL url1 = new URL(urlImagen1);
                            Image image1 = ImageIO.read(url1);
                            ImageIcon icon1 = new ImageIcon(image1.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                            img1.setIcon(icon1);
                        } catch (IOException u) {
                            u.printStackTrace();
                        }
                    } else {
                        System.out.println("No se encontró el producto con ProductID 1A");
                    }

                    // Recuperar producto con ProductID 2A
                    Document product2 = collection.find(new Document("ProductID", "2A")).first();
                    if (product2 != null) {
                        String nombre2 = product2.getString("Nombre");
                        String marca2Value = product2.getString("Marca");
                        String color2Value = product2.getString("Color");
                        String urlImagen2 = product2.getString("URLImagen");
                        double precio2Value = product2.getDouble("Precio");

                        // Imprimir en la terminal para verificar
                        System.out.println("ProductID 2A:");
                        System.out.println("Nombre: " + nombre2);
                        System.out.println("Marca: " + marca2Value);
                        System.out.println("Color: " + color2Value);
                        System.out.println("URL Imagen: " + urlImagen2);
                        System.out.println("Precio: " + precio2Value);

                        // Configurar JLabel
                        tituloArticulo2.setText(nombre2);
                        precio2.setText("Precio: $" + String.format("%.2f", precio2Value));
                        color2.setText("Color: " + color2Value);
                        marca2.setText("Marca: " + marca2Value);

                        // Mostrar imagen
                        try {
                            URL url2 = new URL(urlImagen2);
                            Image image2 = ImageIO.read(url2);
                            ImageIcon icon2 = new ImageIcon(image2.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                            img2.setIcon(icon2);
                        } catch (IOException u) {
                            u.printStackTrace();
                        }
                    } else {
                        System.out.println("No se encontró el producto con ProductID 2A");
                    }

                    // Recuperar producto con ProductID 3A
                    Document product3 = collection.find(new Document("ProductID", "3A")).first();
                    if (product3 != null) {
                        String nombre3 = product3.getString("Nombre");
                        String marca3Value = product3.getString("Marca");
                        String color3Value = product3.getString("Color");
                        String urlImagen3 = product3.getString("URLImagen");
                        double precio3Value = product3.getDouble("Precio");

                        // Imprimir en la terminal para verificar
                        System.out.println("ProductID 3A:");
                        System.out.println("Nombre: " + nombre3);
                        System.out.println("Marca: " + marca3Value);
                        System.out.println("Color: " + color3Value);
                        System.out.println("URL Imagen: " + urlImagen3);
                        System.out.println("Precio: " + precio3Value);

                        // Configurar JLabel
                        tituloArticulo3.setText(nombre3);
                        precio3.setText("Precio: $" + String.format("%.2f", precio3Value));
                        color3.setText("Color: " + color3Value);
                        marca3.setText("Marca: " + marca3Value);

                        // Mostrar imagen
                        try {
                            URL url3 = new URL(urlImagen3);
                            Image image3 = ImageIO.read(url3);
                            ImageIcon icon3 = new ImageIcon(image3.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                            img3.setIcon(icon3);
                        } catch (IOException u) {
                            u.printStackTrace();
                        }
                    } else {
                        System.out.println("No se encontró el producto con ProductID 3A");
                    }

                } catch (Exception u) {
                    u.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + u.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //CASUAL
        casualButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Inicializar MongoDB
                    var mongoClient = MongoClients.create("mongodb://localhost:27017");
                    database = mongoClient.getDatabase("proyectoIsaacQuinapallo");
                    collection = database.getCollection("productos");

                    // Recuperar producto con ProductID 1B
                    Document product1 = collection.find(new Document("ProductID", "1B")).first();
                    if (product1 != null) {
                        String nombre1 = product1.getString("Nombre");
                        String marca1Value = product1.getString("Marca");
                        String color1Value = product1.getString("Color");
                        String urlImagen1 = product1.getString("URLImagen");
                        double precio1Value = product1.getDouble("Precio");

                        // Imprimir en la terminal para verificar
                        System.out.println("ProductID 1B:");
                        System.out.println("Nombre: " + nombre1);
                        System.out.println("Marca: " + marca1Value);
                        System.out.println("Color: " + color1Value);
                        System.out.println("URL Imagen: " + urlImagen1);
                        System.out.println("Precio: " + precio1Value);

                        // Configurar JLabel
                        tituloArticulo1.setText(nombre1);
                        precio1.setText("Precio: $" + String.format("%.2f", precio1Value));
                        color1.setText("Color: " + color1Value);
                        marca1.setText("Marca: " + marca1Value);

                        // Mostrar imagen
                        try {
                            URL url1 = new URL(urlImagen1);
                            Image image1 = ImageIO.read(url1);
                            ImageIcon icon1 = new ImageIcon(image1.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                            img1.setIcon(icon1);
                        } catch (IOException u) {
                            u.printStackTrace();
                        }
                    } else {
                        System.out.println("No se encontró el producto con ProductID 1B");
                    }

                    // Recuperar producto con ProductID 2B
                    Document product2 = collection.find(new Document("ProductID", "2B")).first();
                    if (product2 != null) {
                        String nombre2 = product2.getString("Nombre");
                        String marca2Value = product2.getString("Marca");
                        String color2Value = product2.getString("Color");
                        String urlImagen2 = product2.getString("URLImagen");
                        double precio2Value = product2.getDouble("Precio");

                        // Imprimir en la terminal para verificar
                        System.out.println("ProductID 2B:");
                        System.out.println("Nombre: " + nombre2);
                        System.out.println("Marca: " + marca2Value);
                        System.out.println("Color: " + color2Value);
                        System.out.println("URL Imagen: " + urlImagen2);
                        System.out.println("Precio: " + precio2Value);

                        // Configurar JLabel
                        tituloArticulo2.setText(nombre2);
                        precio2.setText("Precio: $" + String.format("%.2f", precio2Value));
                        color2.setText("Color: " + color2Value);
                        marca2.setText("Marca: " + marca2Value);

                        // Mostrar imagen
                        try {
                            URL url2 = new URL(urlImagen2);
                            Image image2 = ImageIO.read(url2);
                            ImageIcon icon2 = new ImageIcon(image2.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                            img2.setIcon(icon2);
                        } catch (IOException u) {
                            u.printStackTrace();
                        }
                    } else {
                        System.out.println("No se encontró el producto con ProductID 2B");
                    }

                    // Recuperar producto con ProductID 3B
                    Document product3 = collection.find(new Document("ProductID", "3B")).first();
                    if (product3 != null) {
                        String nombre3 = product3.getString("Nombre");
                        String marca3Value = product3.getString("Marca");
                        String color3Value = product3.getString("Color");
                        String urlImagen3 = product3.getString("URLImagen");
                        double precio3Value = product3.getDouble("Precio");

                        // Imprimir en la terminal para verificar
                        System.out.println("ProductID 3B:");
                        System.out.println("Nombre: " + nombre3);
                        System.out.println("Marca: " + marca3Value);
                        System.out.println("Color: " + color3Value);
                        System.out.println("URL Imagen: " + urlImagen3);
                        System.out.println("Precio: " + precio3Value);

                        // Configurar JLabel
                        tituloArticulo3.setText(nombre3);
                        precio3.setText("Precio: $" + String.format("%.2f", precio3Value));
                        color3.setText("Color: " + color3Value);
                        marca3.setText("Marca: " + marca3Value);

                        // Mostrar imagen
                        try {
                            URL url3 = new URL(urlImagen3);
                            Image image3 = ImageIO.read(url3);
                            ImageIcon icon3 = new ImageIcon(image3.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                            img3.setIcon(icon3);
                        } catch (IOException u) {
                            u.printStackTrace();
                        }
                    } else {
                        System.out.println("No se encontró el producto con ProductID 3B");
                    }

                } catch (Exception u) {
                    u.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + u.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        //FORMAL
        formalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Inicializar MongoDB
                    var mongoClient = MongoClients.create("mongodb://localhost:27017");
                    database = mongoClient.getDatabase("proyectoIsaacQuinapallo");
                    collection = database.getCollection("productos");

                    // Recuperar producto con ProductID 1C
                    Document product1 = collection.find(new Document("ProductID", "1C")).first();
                    if (product1 != null) {
                        String nombre1 = product1.getString("Nombre");
                        String marca1Value = product1.getString("Marca");
                        String color1Value = product1.getString("Color");
                        String urlImagen1 = product1.getString("URLImagen");
                        double precio1Value = product1.getDouble("Precio");

                        // Imprimir en la terminal para verificar
                        System.out.println("ProductID 1C:");
                        System.out.println("Nombre: " + nombre1);
                        System.out.println("Marca: " + marca1Value);
                        System.out.println("Color: " + color1Value);
                        System.out.println("URL Imagen: " + urlImagen1);
                        System.out.println("Precio: " + precio1Value);

                        // Configurar JLabel
                        tituloArticulo1.setText(nombre1);
                        precio1.setText("Precio: $" + String.format("%.2f", precio1Value));
                        color1.setText("Color: " + color1Value);
                        marca1.setText("Marca: " + marca1Value);

                        // Mostrar imagen
                        try {
                            URL url1 = new URL(urlImagen1);
                            Image image1 = ImageIO.read(url1);
                            ImageIcon icon1 = new ImageIcon(image1.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                            img1.setIcon(icon1);
                        } catch (IOException u) {
                            u.printStackTrace();
                        }
                    } else {
                        System.out.println("No se encontró el producto con ProductID 1C");
                    }

                    // Recuperar producto con ProductID 2C
                    Document product2 = collection.find(new Document("ProductID", "2C")).first();
                    if (product2 != null) {
                        String nombre2 = product2.getString("Nombre");
                        String marca2Value = product2.getString("Marca");
                        String color2Value = product2.getString("Color");
                        String urlImagen2 = product2.getString("URLImagen");
                        double precio2Value = product2.getDouble("Precio");

                        // Imprimir en la terminal para verificar
                        System.out.println("ProductID 2C:");
                        System.out.println("Nombre: " + nombre2);
                        System.out.println("Marca: " + marca2Value);
                        System.out.println("Color: " + color2Value);
                        System.out.println("URL Imagen: " + urlImagen2);
                        System.out.println("Precio: " + precio2Value);

                        // Configurar JLabel
                        tituloArticulo2.setText(nombre2);
                        precio2.setText("Precio: $" + String.format("%.2f", precio2Value));
                        color2.setText("Color: " + color2Value);
                        marca2.setText("Marca: " + marca2Value);

                        // Mostrar imagen
                        try {
                            URL url2 = new URL(urlImagen2);
                            Image image2 = ImageIO.read(url2);
                            ImageIcon icon2 = new ImageIcon(image2.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                            img2.setIcon(icon2);
                        } catch (IOException u) {
                            u.printStackTrace();
                        }
                    } else {
                        System.out.println("No se encontró el producto con ProductID 2C");
                    }

                    // Recuperar producto con ProductID 3C
                    Document product3 = collection.find(new Document("ProductID", "3C")).first();
                    if (product3 != null) {
                        String nombre3 = product3.getString("Nombre");
                        String marca3Value = product3.getString("Marca");
                        String color3Value = product3.getString("Color");
                        String urlImagen3 = product3.getString("URLImagen");
                        double precio3Value = product3.getDouble("Precio");

                        // Imprimir en la terminal para verificar
                        System.out.println("ProductID 3C:");
                        System.out.println("Nombre: " + nombre3);
                        System.out.println("Marca: " + marca3Value);
                        System.out.println("Color: " + color3Value);
                        System.out.println("URL Imagen: " + urlImagen3);
                        System.out.println("Precio: " + precio3Value);

                        // Configurar JLabel
                        tituloArticulo3.setText(nombre3);
                        precio3.setText("Precio: $" + String.format("%.2f", precio3Value));
                        color3.setText("Color: " + color3Value);
                        marca3.setText("Marca: " + marca3Value);

                        // Mostrar imagen
                        try {
                            URL url3 = new URL(urlImagen3);
                            Image image3 = ImageIO.read(url3);
                            ImageIcon icon3 = new ImageIcon(image3.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                            img3.setIcon(icon3);
                        } catch (IOException u) {
                            u.printStackTrace();
                        }
                    } else {
                        System.out.println("No se encontró el producto con ProductID 3C");
                    }

                } catch (Exception u) {
                    u.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + u.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
