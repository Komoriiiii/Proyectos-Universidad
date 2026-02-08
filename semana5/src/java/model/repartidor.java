package model;

public class repartidor implements Runnable {
    private String nombre;
    private zonaCarga zonaCarga;

    public repartidor(String nombre, zonaCarga zonaCarga) {
        this.nombre = nombre;
        this.zonaCarga = zonaCarga;
    }
    // se inicia el hilo representando el trabajo que hace el repartidor
    @Override
    public void run() {
        System.out.println("[" + nombre + "] Comenzando trabajo");

        //proceso completo de trabajo del repartidor
        while (true) {
            if (!zonaCarga.hayPedidos()) {
                System.out.println("[" + nombre + "] No hay pedidos.");
                break;
            }
            Pedido pedido = zonaCarga.retirarPedido();
            if (pedido == null) {
                System.out.println("[" + nombre + "] No hay mas pedidos disponibles."); //si es null significa que otro repartidor tomo el pedido
                break;
            }
            pedido.setEstado(estadoPedido.EN_REPARTO);
            System.out.println("[" + nombre + "] Retiro el " + pedido + " -> Iniciando entrega"); //se cambia el estado del reparto
            //simulacion del tiempo de entrega
            try {
                int tiempoEntrega = 2000;
                Thread.sleep(tiempoEntrega);
            } catch (InterruptedException e) {
                System.out.println("[" + nombre + "] fue interrumpido");
                break;
            }
            pedido.setEstado(estadoPedido.ENTREGADO); //se cambia el estado a entregado
            System.out.println("[" + nombre + "] Entrego el " + pedido + ", Entrega completada.");
        }
        System.out.println("[" + nombre + "] Finalizo su jornada.\n");
    }
    public String getNombre() {
    return nombre;
    }
    public zonaCarga getZonaCarga() {
        return zonaCarga;
    }
}
