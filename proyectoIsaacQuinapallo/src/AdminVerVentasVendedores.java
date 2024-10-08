import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;

public class AdminVerVentasVendedores {
    // Elementos de la interfaz gráfica
    public JPanel mainPanel;
    private JTable tablaGeneral; // Tabla para mostrar datos generales de compras
    private JTextField busquedaField; // Campo de texto para buscar por CompraID
    private JButton buscarCompraButton; // Botón para buscar por CompraID
    private JTable tablaEspecifica; // Tabla para mostrar datos específicos de una compra
    private JLabel confirmacionCompra; // Etiqueta para mostrar mensajes de confirmación o error
    private JTextField busquedafield2; // Campo de texto para buscar por UserID
    private JButton buscarButton2; // Botón para buscar por UserID

    private MongoCollection<Document> comprasCollection; // Colección de compras en MongoDB

    public AdminVerVentasVendedores() {
        // Conectar a la base de datos MongoDB
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("proyectoIsaacQuinapallo");
        comprasCollection = database.getCollection("compras");

        // Configurar las tablas
        configurarTablas();

        // Cargar los datos en la tabla general
        cargarDatosTablaGeneral();

        // Agregar ActionListener al botón de búsqueda de CompraID
        buscarCompraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarYMostrarCompra();
            }
        });

        // Agregar ActionListener al botón de búsqueda de UserID
        buscarButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarYMostrarPorUserID();
            }
        });
    }

    private void configurarTablas() {
        // Configurar los encabezados de la tabla general
        String[] columnNamesGeneral = {"CompraID", "UserID", "ClienteID", "Cantidad Total", "Total Precio", "Fecha Compra"};
        DefaultTableModel modelGeneral = new DefaultTableModel(columnNamesGeneral, 0);
        tablaGeneral.setModel(modelGeneral);

        // Configurar los encabezados de la tabla específica
        String[] columnNamesEspecifica = {"CompraID", "UserID", "ClienteID", "ProductID", "Cantidad", "Total", "FechaCompra"};
        DefaultTableModel modelEspecifica = new DefaultTableModel(columnNamesEspecifica, 0);
        tablaEspecifica.setModel(modelEspecifica);
    }

    private void cargarDatosTablaGeneral() {
        // Obtener los datos de la colección compras
        FindIterable<Document> documentos = comprasCollection.find();

        // Mapas para almacenar los datos procesados
        Map<Integer, Map<String, Object>> datosMap = new HashMap<>();

        // Formateador de fecha
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Procesar los datos
        for (Document doc : documentos) {
            int compraID = doc.getInteger("CompraID");
            String userID = doc.getString("UserID");
            String clienteID = doc.getString("ClienteID");
            int cantidad = doc.getInteger("Cantidad", 0);
            Object totalObj = doc.get("Total");
            double total = totalObj instanceof Double ? (Double) totalObj : ((Integer) totalObj).doubleValue();
            Date fechaCompra = doc.getDate("FechaCompra");
            String fechaCompraStr = dateFormat.format(fechaCompra);

            // Acumulación de datos para cada CompraID
            if (datosMap.containsKey(compraID)) {
                Map<String, Object> datos = datosMap.get(compraID);
                datos.put("Cantidad", (int) datos.get("Cantidad") + cantidad);
                datos.put("Total", (double) datos.get("Total") + total);
            } else {
                Map<String, Object> datos = new HashMap<>();
                datos.put("UserID", userID);
                datos.put("ClienteID", clienteID);
                datos.put("Cantidad", cantidad);
                datos.put("Total", total);
                datos.put("FechaCompra", fechaCompraStr);
                datosMap.put(compraID, datos);
            }
        }

        // Obtener el modelo de la tabla y limpiar los datos existentes
        DefaultTableModel model = (DefaultTableModel) tablaGeneral.getModel();
        model.setRowCount(0); // Limpia las filas existentes

        // Agregar los datos al modelo
        for (Map.Entry<Integer, Map<String, Object>> entry : datosMap.entrySet()) {
            Integer compraID = entry.getKey();
            Map<String, Object> datos = entry.getValue();
            Object[] rowData = {
                    compraID,
                    datos.get("UserID"),
                    datos.get("ClienteID"),
                    datos.get("Cantidad"),
                    datos.get("Total"),
                    datos.get("FechaCompra")
            };
            model.addRow(rowData);
        }
    }

    private void buscarYMostrarCompra() {
        // Obtener el CompraID ingresado por el usuario
        String compraIDBuscadaStr = busquedaField.getText().trim();
        if (compraIDBuscadaStr.isEmpty()) {
            confirmacionCompra.setText("Por favor, ingrese un CompraID.");
            return;
        }

        int compraIDBuscada;
        try {
            compraIDBuscada = Integer.parseInt(compraIDBuscadaStr);
        } catch (NumberFormatException e) {
            confirmacionCompra.setText("CompraID debe ser un número entero.");
            return;
        }

        // Obtener los datos de la colección compras donde CompraID coincide
        FindIterable<Document> documentos = comprasCollection.find(new Document("CompraID", compraIDBuscada));

        // Obtener el modelo de la tabla específica y limpiar los datos existentes
        DefaultTableModel model = (DefaultTableModel) tablaEspecifica.getModel();
        model.setRowCount(0); // Limpia las filas existentes

        // Formateador de fecha
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        boolean encontrado = false;
        for (Document doc : documentos) {
            encontrado = true;
            Date fechaCompra = doc.getDate("FechaCompra");
            String fechaCompraStr = dateFormat.format(fechaCompra);

            // Agregar datos a la tabla específica
            Object[] rowData = {
                    doc.getInteger("CompraID"),
                    doc.getString("UserID"),
                    doc.getString("ClienteID"),
                    doc.getString("ProductID"),
                    doc.getInteger("Cantidad", 0),
                    doc.get("Total"),
                    fechaCompraStr
            };
            model.addRow(rowData);
        }

        if (encontrado) {
            confirmacionCompra.setText("Compra(s) encontrada(s).");
        } else {
            confirmacionCompra.setText("Compra no encontrada.");
        }
    }

    private void buscarYMostrarPorUserID() {
        // Obtener el UserID ingresado por el usuario
        String userIDBuscado = busquedafield2.getText().trim();
        if (userIDBuscado.isEmpty()) {
            confirmacionCompra.setText("Por favor, ingrese un UserID.");
            return;
        }

        // Obtener los datos de la colección compras donde UserID coincide
        FindIterable<Document> documentos = comprasCollection.find(new Document("UserID", userIDBuscado));

        // Obtener el modelo de la tabla específica y limpiar los datos existentes
        DefaultTableModel model = (DefaultTableModel) tablaEspecifica.getModel();
        model.setRowCount(0); // Limpia las filas existentes

        // Formateador de fecha
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        boolean encontrado = false;
        for (Document doc : documentos) {
            encontrado = true;
            Date fechaCompra = doc.getDate("FechaCompra");
            String fechaCompraStr = dateFormat.format(fechaCompra);

            // Agregar datos a la tabla específica
            Object[] rowData = {
                    doc.getInteger("CompraID"),
                    doc.getString("UserID"),
                    doc.getString("ClienteID"),
                    doc.getString("ProductID"),
                    doc.getInteger("Cantidad", 0),
                    doc.get("Total"),
                    fechaCompraStr
            };
            model.addRow(rowData);
        }

        if (encontrado) {
            confirmacionCompra.setText("Compra(s) encontrada(s) para el UserID especificado.");
        } else {
            confirmacionCompra.setText("No se encontraron compras para el UserID especificado.");
        }
    }
}
