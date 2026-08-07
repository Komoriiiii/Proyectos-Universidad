package model;

public class PedidoExpress extends Pedido{
    private int prioridad;
    private boolean pagoExtra;

    public PedidoExpress(String id, String cliente, String direccion, int prioridad, boolean pagoExtra) {
        super(id, cliente, direccion);
        this.prioridad = prioridad;
        this.pagoExtra = pagoExtra;
    }

    @Override
    public int calcularTiempoEntrega(){
        int tiempo = 20;
        tiempo = tiempo - (prioridad * 2);
        if (pagoExtra){
            tiempo = tiempo - (prioridad * 2);
        }
        if (tiempo < 10){
            tiempo = 10;
        }
        return tiempo;
    }
    @Override
    public void asignarRepartidor(){
        this.repartidor = "Repartidor Express " + (int)(Math.random()*100);
        System.out.println("Repartidor express asignado: " + this.repartidor + " (Prioridad: " + prioridad + ")");
    }
}


