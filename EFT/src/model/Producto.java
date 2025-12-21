package model;

public class Producto {
    private String codigo;
    private String nombre;
    private String categoria;
    private double precio;
    private int stockDisponible;
    private String unidadMedida;

    public Producto(String codigo, String nombre, String categoria, double precio, int stockDisponible, String unidadMedida) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stockDisponible = stockDisponible;
        this.unidadMedida = unidadMedida;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStockDisponible() {
        return stockDisponible;
    }

    public void setStockDisponible(int stockDisponible) {
        this.stockDisponible = stockDisponible;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }
    public void agregarStock(int cantidad){
        this.stockDisponible+=cantidad;
    }
    public boolean reducirStock(int cantidad){
        if(cantidad <= stockDisponible){
            this.stockDisponible-=cantidad;
            return true;
        }
        return false;
    }
    @Override
    public String toString() {
        return "Producto ["+ codigo + "] "+ nombre +
                " - Categoria: " + categoria +
                ", Precio: $" + precio +
                ", Stock: " + stockDisponible + " " + unidadMedida;
    }
}
