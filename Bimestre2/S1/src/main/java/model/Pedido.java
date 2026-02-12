package model;

class Pedido {
    protected String idPedido;
    protected String direccionEntrega;
    protected String tipoPedido;

    public Pedido(String idPedido, String direccionEntrega, String tipoPedido) {
        this.idPedido = idPedido;
        this.direccionEntrega = direccionEntrega;
        this.tipoPedido = tipoPedido;
    }
    public void asignarRepartidor(){
        System.out.println("asignando repartidor para pedido" + idPedido);
    }
    public void asignarRepartidor(String nombreRepartidor) {
        System.out.println("Repartidor " + nombreRepartidor + " asignado al pedido" + idPedido);
    }
}
