package model;

public class PedidoComida extends Pedido {
    public PedidoComida(String idPedido, String direccionEntrega) {
        super(idPedido, direccionEntrega, "Comida");
    }
    @Override
    public void asignarRepartidor() {
        System.out.println("Pedido de comida " + idPedido);
        System.out.println("Buscando repartidor con mochila termica...");
    }
    @Override
    public void asignarRepartidor(String nombreRepartidor) {
        System.out.println("Pedido de comida " + idPedido);
        System.out.println("Validando mochila de " + nombreRepartidor);
        System.out.println("Repartidor " + nombreRepartidor + " asignado al pedido");
    }
}
