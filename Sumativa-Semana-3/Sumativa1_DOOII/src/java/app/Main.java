package app;

import model.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("\n--- Sistema SpeedFast ---");

        ControladorEnvios controlador = new ControladorEnvios();

        System.out.println("\n--- Pedido 1: Comida ---");
        PedidoComida pedido1 = new PedidoComida("001", "Juan Perez", "Calle 123", "Restaurante Muy weno", true);
        pedido1.asignarRepartidor();
        pedido1.mostrarResumen();
        controlador.despachar(pedido1);

        System.out.println("\n--- Pedido 2: Express ---");
        PedidoExpress pedido2 = new PedidoExpress("002", "Juanita Parra","Avenida 123", 5, true);
        pedido2.asignarRepartidor("Claudio Parra"); //asignacion manual
        pedido2.mostrarResumen();
        controlador.despachar(pedido2);

        System.out.println("\n--- Pedido 3: Encomienda ---");
        PedidoEncomienda pedido3 = new PedidoEncomienda("003", "Rodrigo Rodriguez", "Calle 456", 10.5, "fragil");
        pedido3.asignarRepartidor();
        pedido3.mostrarResumen();
        controlador.despachar(pedido3);

        System.out.println("\n--- Ejemplo Cancelacion ---");
        PedidoComida pedido4 = new PedidoComida ("004", "Fernando Fernandez", "Avenida 456", "Completos Completos", false);
        pedido4.asignarRepartidor("Sofia Perez");
        controlador.cancelar(pedido4);

        System.out.println("\n--- Ejemplo Error: Sin repartidor ---");
        PedidoExpress pedido5 = new PedidoExpress("005", "Luis Garcia", "Calle 789", 2,false);
        controlador.despachar(pedido5);

        //ver historial
        controlador.verHistorial();

        //demostracion de polimorfismo
        System.out.println("\n --- Ejemplo de Polimorfismo ---");
        Pedido[] pedidos = {pedido1, pedido2, pedido3};
        for (Pedido p : pedidos) {
            System.out.println(p.getClass().getSimpleName() + " - Tiempo: " + p.calcularTiempoEntrega() + " min");
            }
        }
    }