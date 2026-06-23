package app;

import model.PedidoComida;
import model.PedidoExpress;
import model.PedidosEncomienda;

public class Main {
    public static void main(String[] args) {

    PedidoComida comida  = new PedidoComida("C001", "Calle 123");
    PedidosEncomienda encomienda = new PedidosEncomienda("E001", "Avenida 123");
    PedidoExpress express = new PedidoExpress("X001", "Plaza 123");

        System.out.println("--- Asignacion automatica ---");

        comida.asignarRepartidor();
        System.out.println();

        encomienda.asignarRepartidor();
        System.out.println();

        express.asignarRepartidor();
        System.out.println();

        System.out.println("--- Asignacion manual ---");
        comida.asignarRepartidor("Juan Perez");
        System.out.println();
        encomienda.asignarRepartidor("Roberto Diaz");
        System.out.println();
        express.asignarRepartidor("Sebastian Romero");
    }
}
