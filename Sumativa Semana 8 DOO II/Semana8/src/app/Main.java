package app;

import db.conexion;
import model.ventanaEntregas;
import model.ventanaPedidos;
import model.ventanaRepartidores;


import javax.swing.*;
import java.sql.SQLException;

//ventana principal del sistema
public class Main extends JFrame {

    public Main() {
        setTitle("SpeedFast - Sistema de Pedidos");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //verifica que la conexion funcione antes de abrir
        try {
            conexion.conectar();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "No se pudo conectar a la base de datos.\n" +
                            "Verifica que MySQL este corriendo y que los datos en la clase conexion sean correctos.\n\n" +
                            "Error: " + e.getMessage(),
                    "Error de conexion", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        //pestanas del sistema
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Repartidores", new ventanaRepartidores());
        tabs.addTab("Pedidos",      new ventanaPedidos());

        //la pestana de entregas necesita recargar combos cuando se abre
        ventanaEntregas ventanaEntregas = new ventanaEntregas();
        tabs.addTab("Entregas", ventanaEntregas);

        tabs.addChangeListener(e -> {
            if (tabs.getSelectedIndex() == 2) {
                ventanaEntregas.cargarCombos();
            }
        });

        add(tabs);
        setVisible(true);
    }

    public static void main(String[] args) {
        //para que se vea con el estilo del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("No se pudo aplicar el look and feel");
        }

        SwingUtilities.invokeLater(() -> new Main());
    }
}