package model;

public class ItemOrden {
    private Producto producto;
    private int cantidad;
    private double precioUnitario;

    public ItemOrden(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecio();
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public double getPrecioUnitario() {
        return precioUnitario;
    }
    public double calcularSubtotal(){
        return cantidad * precioUnitario;
    }
    @Override
    public String toString(){
        return producto.getNombre()+
                " - Cantidad: " + cantidad +
                " - Precio Unidad: $" + precioUnitario +
                " - Subtotal: $" + calcularSubtotal();
    }
}
