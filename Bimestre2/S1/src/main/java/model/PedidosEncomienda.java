package model;

public class PedidosEncomienda extends Pedido {
    public PedidosEncomienda(String idPedido, String direccionEntrega) {
        super(idPedido, direccionEntrega, "Encomienda");
    }
    @Override
    public void asignarRepartidor(){
        System.out.println("Pedido de encomienda " + idPedido);
        System.out.println("Validando peso y embalaje...");
    }
    @Override
    public void asignarRepartidor(String nombreRepartidor) {
        System.out.println("Pedido de encomienda " + idPedido);
        System.out.println("Validando capacidad de carga de " + nombreRepartidor);
        System.out.println("Repartidor " + nombreRepartidor + " asignado al pedido");
    }
}
