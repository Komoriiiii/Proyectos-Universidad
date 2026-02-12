package ui;

import data.GestorEntidades;
import model.*;
import javax.swing.*;

public class Main {
    private GestorEntidades gestor;

    public Main() {
        gestor = new GestorEntidades();
        mostrarMenu();
    }

    public void mostrarMenu() {
        while (true) {
            String menu = "\n--- Sistema Salmontt ---\n\n" +
                    "Total de Entidades: " + gestor.getTotalEntidades() + "\n\n" +
                    "1. Agregar Proveedor\n" +
                    "2. Agregar Empleado\n" +
                    "3. Agregar Centro de Cultivo\n" +
                    "4. Agregar Planta de Procesamiento\n" +
                    "5. Mostrar todas las Entidades\n" +
                    "6. Salir\n\n" +
                    "Seleccione una opcion:";

            String opcion = JOptionPane.showInputDialog(null, menu);

            if (opcion == null) {
                break;
            }
            switch (opcion) {
                case "1":
                    agregarProveedor();
                    break;
                case "2":
                    agregarEmpelado();
                    break;
                case "3":
                    agregarCentro();
                    break;
                case "4":
                    agregarPlanta();
                    break;
                case "5":
                    mostrarTodas();
                    break;
                case "6":
                    JOptionPane.showMessageDialog(null, "Gracias por usar el sistema, hasta luego.");
                    return;
                default:
                    JOptionPane.showMessageDialog(null, "Opcion Invalida");
            }
        }
    }

    private void agregarProveedor() {
        String rut = JOptionPane.showInputDialog(null, "Ingrese Rut:");
        if (rut == null) return;
        String razonSocial = JOptionPane.showInputDialog(null, "Ingrese Razon social:");
        if (razonSocial == null) return;
        String tipoServicio = JOptionPane.showInputDialog(null, "Ingrese Tipo de Servicio:");
        if (tipoServicio == null) return;
        String contacto = JOptionPane.showInputDialog(null, "Ingrese informacion de Contacto:");
        if (contacto == null) return;

        Proveedor prov = new Proveedor(rut, razonSocial, tipoServicio, contacto);
        gestor.agregarEntidad(prov);

        //mensaje de confirmacion

        JOptionPane.showMessageDialog(null, "Proveedor agregado con exito\n\n" + prov.mostrarResumen());
    }

    private void agregarEmpelado() {
        String rut = JOptionPane.showInputDialog(null, "Ingrese Rut:");
        if (rut == null) return;
        String nombre = JOptionPane.showInputDialog(null, "Ingrese Nombre:");
        if (nombre == null) return;
        String cargo = JOptionPane.showInputDialog(null, "Ingrese Cargo:");
        if (cargo == null) return;
        String area = JOptionPane.showInputDialog(null, "Ingrese Area:");
        if (area == null) return;
        double salario = Double.parseDouble(JOptionPane.showInputDialog(null, "Ingrese Salario:"));
        if (salario <= 0) return;

        Empleado emp = new Empleado(rut, nombre, cargo, area, salario);
        gestor.agregarEntidad(emp);

        JOptionPane.showMessageDialog(null, "Empleado agregado con exito\n\n" + emp.mostrarResumen());
    }

    private void agregarCentro() {
        String id = JOptionPane.showInputDialog(null, "Ingrese ID:");
        String nombre = JOptionPane.showInputDialog(null, "Ingrese Nombre:");
        String ubicacion = JOptionPane.showInputDialog(null, "Ingrese Ubicacion:");
        int capacidadJaulas = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese Capacidad de Jaulas:"));
        double produccionAnual = Double.parseDouble(JOptionPane.showInputDialog(null, "Ingrese Produccion:"));

        CentroCultivo centro = new CentroCultivo(id, nombre, ubicacion, capacidadJaulas, produccionAnual);
        gestor.agregarEntidad(centro);

        JOptionPane.showMessageDialog(null, "Centro agregado con exito\n\n" + centro.mostrarResumen());
    }

    private void agregarPlanta() {
        String id = JOptionPane.showInputDialog(null, "Ingrese ID:");
        String nombre = JOptionPane.showInputDialog(null, "Ingrese Nombre:");
        String ubicacion = JOptionPane.showInputDialog(null, "Ingrese Ubicacion:");
        int capacidadProceso = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese Capacidad de Procesamiento:"));
        String tipoProducto = JOptionPane.showInputDialog(null, "Ingrese Tipo de Producto:");

        PlantaProceso planta = new PlantaProceso(id, nombre, ubicacion, capacidadProceso, tipoProducto);
        gestor.agregarEntidad(planta);

        JOptionPane.showMessageDialog(null, "Planta de Procesamiento agregada con exito\n\n" + planta.mostrarResumen());
    }

    private void mostrarTodas() {
        String lista = gestor.mostrarTodasLasEntidades();
        JTextArea area = new JTextArea(lista);
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new java.awt.Dimension(500, 400));
        JOptionPane.showMessageDialog(null, scroll, "Todas las Entidades", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        new Main();
    }
}