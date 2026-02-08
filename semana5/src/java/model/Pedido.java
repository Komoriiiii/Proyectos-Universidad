package model;

public class Pedido {
    private int id;
    private String direccionEntrega;
    private estadoPedido estado;

    //constructor

    public Pedido(int id, String direccionEntrega){
        this.id = id;
        this.direccionEntrega = direccionEntrega;
        this.estado = estadoPedido.PENDIENTE; //siempre empieza pendiente
    }
    public int getId() {
        return id;
    }
    public estadoPedido getEstado() {
        return estado;
    }
    public String getDireccionEntrega() {
        return direccionEntrega;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }
    public void setEstado(estadoPedido nuevoEstado) {
        this.estado = nuevoEstado;
    }
    @Override
    public String toString() {
        return "Pedido #" + id + " - " + direccionEntrega + " [" + estado + "]";
    }
}
