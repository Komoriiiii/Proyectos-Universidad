package model;

public class PedidoExpress extends Pedido {
    public PedidoExpress(String idPedido, String direccionEntrega) {
        super(idPedido, direccionEntrega, "Express");
    }

    @Override
    public void asignarRepartidor() {
        System.out.println("Pedido express " + idPedido);
        System.out.println("Buscando repartidor mas cercano disponible...");
    }
    @Override
    public void asignarRepartidor(String nombreRepartidor) {
        System.out.println("Pedido express " + idPedido);
        System.out.println("Verificando disponibilidad de " + nombreRepartidor);
        System.out.println("Repartidor " + nombreRepartidor + " asignado al pedido");
    }
}
