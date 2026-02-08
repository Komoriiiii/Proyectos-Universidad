package app;

import model.Pedido;
import model.repartidor;
import model.zonaCarga;

public class Main {
    public static void main(String[] args) {
        System.out.println("\n=======================================");
        System.out.println("    Sistema de entregas SpeedFast   ");
        System.out.println("=======================================\n");

        // se cre una zona de carga compartida

        zonaCarga zonaCarga = new zonaCarga();
        System.out.println("    Zona de carga creada\n");

        //se agregan pedidos para ejemplificar el funcionamiento del sistema

        System.out.println("--- Agregando Pedidos ---");
        zonaCarga.agregarPedido(new Pedido(1, "Calle Concepcion 123"));
        zonaCarga.agregarPedido(new Pedido(2, "Av. Chile 563"));
        zonaCarga.agregarPedido(new Pedido(3, "Av. Tarapaca 432"));
        zonaCarga.agregarPedido(new Pedido(4, "Calle Romeral 682" ));
        zonaCarga.agregarPedido(new Pedido(5, "Calle El Trigal 1234"));

        System.out.println("Total de pedidos: " + zonaCarga.cantidadPedidos());

        //se crean repartidores

        System.out.println("\n --- Iniciando Repartidores --- \n");

        repartidor repartidor1 = new repartidor("Juan", zonaCarga);
        repartidor repartidor2 = new repartidor("Pedro", zonaCarga);
        repartidor repartidor3 = new repartidor("Maria", zonaCarga);

        //se crean hilos para cada repartidor

        Thread thread1 = new Thread(repartidor1);
        Thread thread2 = new Thread(repartidor2);
        Thread thread3 = new Thread(repartidor3);

        //se inician los hilos con start

        thread1.start();
        thread2.start();
        thread3.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            System.out.println("Error: El programa fue interrumpido");
        }

        System.out.println("=======================================");
        System.out.println(" Todos los pedidos han sido entregados");
        System.out.println("=======================================");

        }
    }