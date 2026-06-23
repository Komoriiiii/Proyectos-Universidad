package model;

import java.util.ArrayList;
import java.util.List;

public class OrdenDeCompra {
    private String numeroOrden;
    private Cliente cliente;
    private String fecha;
    private List<ItemOrden> items;
    private String estado;

    public OrdenDeCompra(String numeroOrden, Cliente cliente, String fecha) {
        this.numeroOrden = numeroOrden;
        this.cliente = cliente;
        this.fecha = fecha;
        this.items = new ArrayList<>();
        this.estado = "Pendiente";
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public String getFecha() {
        return fecha;
    }

    public List<ItemOrden> getItems() {
        return items;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    public void agregarItem(ItemOrden item){
        items.add(item);
    }

    public double calcularTotal(){
        double total = 0;
        for(ItemOrden item : items){
            total += item.calcularSubtotal();
        }
        return total;
    }
    public void procesarOrden(){
        boolean exito = true;
        for(ItemOrden item : items){
            if (!item.getProducto().reducirStock(item.getCantidad())){
                System.out.println("Stock insuficiente para: "+ item.getProducto().getNombre());
                exito = false;
            }
        }
        if(exito){
            this.estado = "Procesado";
            System.out.println("Orden "+ numeroOrden +" procesada exitosamente");
        } else {
            this.estado = "Error";
        }
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(" --- Orden de Compra --- \n");
        sb.append ("Numero: ").append(numeroOrden).append("\n");
        sb.append("Cliente: ").append(cliente.getNombreCompleto()).append("\n");
        sb.append("Fecha: ").append(fecha).append("\n");
        sb.append("Estado: ").append(estado).append("\n");
        sb.append("Items:\n");
        for(ItemOrden item : items){
            sb.append("  - ").append(item.toString()).append("\n");
        }
        sb.append("Total: $").append(calcularTotal()).append("\n");
        return sb.toString();
    }
}

