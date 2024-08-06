import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IngresoAdmin {
    public JPanel mainPanel;
    private JButton ingresarAlStockDeButton;
    private JButton revisarVentasDeVendedoresButton;
    private JButton agregarUsuariosCajerosButton;

    public IngresoAdmin() {
        ingresarAlStockDeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Crear instancia de AdminEditarProductos y mostrar en una nueva ventana
                AdminEditarProductos adminEditarProductos = new AdminEditarProductos();
                JFrame frame = new JFrame("Editar Productos de Stock");
                frame.setContentPane(adminEditarProductos.mainPanel);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });

        revisarVentasDeVendedoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Crear instancia de AdminVerVentasVendedores y mostrar en una nueva ventana
                AdminVerVentasVendedores adminVerVentasVendedores = new AdminVerVentasVendedores();
                JFrame frame = new JFrame("Revisar Ventas de Vendedores");
                frame.setContentPane(adminVerVentasVendedores.mainPanel);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });

        agregarUsuariosCajerosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Crear instancia de AdminAñadirVendedores y mostrar en una nueva ventana
                AdminAñadirVendedores adminAñadirVendedores = new AdminAñadirVendedores();
                JFrame frame = new JFrame("Añadir Usuarios/Cajeros");
                frame.setContentPane(adminAñadirVendedores.mainPanel);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}

