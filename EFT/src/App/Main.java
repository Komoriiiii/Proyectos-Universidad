package App;

import model.*;
import java.util.*;

public class Main {
    private List<Registrable> registros;
    private Map<String, Producto> productos;
    private Stack<OrdenDeCompra> ordenesPendientes;

    public Main(){
        this.registros = new ArrayList<>();
        this.productos = new HashMap<>();
        this.ordenesPendientes = new Stack<>();
    }
    public void agregarRegistrable(Registrable registro){
        registros.add(registro);
        registro.registrar();
    }
    public void agregarProducto(Producto producto){
        productos.put(producto.getCodigo(), producto);
        System.out.println("Producto agregado: "+producto.getNombre());
    }
    public Producto buscarProducto(String codigo){
        return productos.get(codigo);
    }
    public void agregarOrden(OrdenDeCompra orden){
        ordenesPendientes.push(orden);
        System.out.println("Orden agregada: " + orden.getNumeroOrden());
    }
    public void procesarUltimaOrden(){
        if (!ordenesPendientes.isEmpty()){
            OrdenDeCompra orden = ordenesPendientes.pop();
            orden.procesarOrden();
        } else {
            System.out.println("No hay ordenes pendientes");
        }
    }
    public void listarTodosLosRegistros(){
        System.out.println(" === Listado de Registros ===");
        for (Registrable reg : registros) {
            System.out.println(reg.mostrarDatos());
            System.out.println(" --- ");
        }
    }
    public void listarEmpleados(){
        System.out.println("\n === Empleados ===");
        for (Registrable reg : registros) {
            if (reg instanceof Empleado){
                System.out.println(reg.mostrarDatos());
            }
        }
    }
    public void listarClientes(){
        System.out.println("\n === Clientes ===");
        for (Registrable reg : registros) {
            if (reg instanceof Cliente){
                System.out.println(reg.mostrarDatos());
            }
        }
    }
    public void listarProductos(){
        System.out.println("\n === Productos === ");
        for  (Producto prod : productos.values()){
            System.out.println(prod);
        }
    }
    public static void main(String[] args) {
        System.out.println(" \n\n----- Sistema Salmontt -----");
        Main sistema = new Main();

        try {
            //creamos direcciones
            Direccion dir1 = new Direccion("Av. Angelmo", "1234", "Puerto Montt", "Puerto Montt", "Los Lagos");
            Direccion dir2 = new Direccion("Calle Quellon", "4567", "Puerto Montt", "Puerto Montt", "Los Lagos");
            Direccion dir3 = new Direccion("Ruta 5 sur", "Km 1020", "Puerto Varas", "Puerto Varas", "Los Lagos");
            //se crean empleados
            Rut rutEmp1 = new Rut("12345678-5");
            Empleado emp1 = new Empleado("Juan", "Perez", rutEmp1, "+569 1234 1223", "juanperez@salmontt.cl", dir1, "Gerente de Produccion", 2500000, " 2020-01-10", "Produccion");
            sistema.agregarRegistrable(emp1);

            Rut rutEmp2 = new Rut("11111111-1");
            Empleado emp2 = new Empleado("Maria", "Gonzalez", rutEmp2, "+56 9 1235 6423", "mariagonzalez@salmontt.cl", dir2, "Jefa de Control de Calidad", 2000000, "2018-03-11", "Control de Calidad");
            sistema.agregarRegistrable(emp2);

            //creamos clientes

            Rut rutCli1 = new Rut("10138037-8");
            Cliente cli1 = new Cliente("Roberto", "Diaz", rutCli1, "+56 9 1237 7349", "roberto.diaz@pecessanantonio.cl", dir3, "CLI-001", "Mayorista", 5000000);
            sistema.agregarRegistrable(cli1);

            //se crea proveedor

            Rut rutProv1 = new Rut("10569043-6");
            Proveedor prov1 = new Proveedor("Carlos", "Rodriguez", rutProv1, "+56 9 2456 2743", "contacto@alimentos-pacifico.cl", dir1, "Alimentos Pacifico Ltda.", "Alimentos para Peces", "PROV-001");
            sistema.agregarRegistrable(prov1);

            //Se crean productos

            Producto prod1 = new Producto("SAL-001", "Salmon Atlantico", "Salmon", 7500, 1000, "Kg");
            sistema.agregarProducto(prod1);

            Producto prod2 = new Producto("SAL-002", "Salmon Ahumado", "Salmon", 10000, 500, "Kg");
            sistema.agregarProducto(prod2);

            Producto prod3 = new Producto("SAL-003", "Salmon Coho", "Salmon", 6000, 1250, "Kg");
            sistema.agregarProducto(prod3);

            //se crea orden de compra
            OrdenDeCompra orden1 = new OrdenDeCompra("ORD-2025-991", cli1, "2025-01-01");
            orden1.agregarItem(new ItemOrden(prod1, 50));
            orden1.agregarItem(new ItemOrden(prod2, 100));
            sistema.agregarOrden(orden1);

            //mostrar informacion
            System.out.println("\n");
            sistema.listarEmpleados();
            sistema.listarClientes();
            sistema.listarProductos();

            System.out.println("\n" + orden1);
            System.out.println("\n===== Procesando Orden =====");
            sistema.procesarUltimaOrden();

            System.out.println("\n===== Stock Actualizado =====");
            sistema.listarProductos();

        } catch (RutInvalidoException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}