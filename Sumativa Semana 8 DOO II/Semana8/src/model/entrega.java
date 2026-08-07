package model;

public class entrega {

    private int id;
    private pedido pedido;
    private repartidor repartidor;
    private String fechaHora; //se guarda como string para simplificar

    public entrega(int id, pedido pedido, repartidor repartidor, String fechaHora) {
        this.id = id;
        this.pedido = pedido;
        this.repartidor = repartidor;
        this.fechaHora = fechaHora;
    }

    public entrega(pedido pedido, repartidor repartidor, String fechaHora) {
        this.pedido = pedido;
        this.repartidor = repartidor;
        this.fechaHora = fechaHora;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public pedido getPedido() { return pedido; }
    public void setPedido(pedido pedido) { this.pedido = pedido; }

    public repartidor getRepartidor() { return repartidor; }
    public void setRepartidor(repartidor repartidor) { this.repartidor = repartidor; }

    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }
}