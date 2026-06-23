package model;

public class PedidoComida extends Pedido {
    private String restaurante;
    private boolean requiereRefrigeracion;
    public PedidoComida(String id, String cliente, String direccion, String restaurante, boolean requiereRefrigeracion) {
        super(id, cliente, direccion);
        this.restaurante = restaurante;
        this.requiereRefrigeracion = requiereRefrigeracion;
    }
    @Override
    public int calcularTiempoEntrega() {
        int tiempo = 30;
        if (requiereRefrigeracion) {
            tiempo = tiempo + 10;
        }
        return tiempo;
    }
    @Override
    public void asignarRepartidor(){
        this.repartidor = "Repartidor " + (int)(Math.random()*100);
        System.out.println("Repartidor asignado: " + this.repartidor);
    }
}

