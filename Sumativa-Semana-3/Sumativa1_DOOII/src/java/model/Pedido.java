package model;

public abstract class Pedido {
    protected String id;
    protected String cliente;
    protected String direccion;
    protected String repartidor;
    protected boolean despachado;
    protected boolean cancelado;

    public Pedido (String id, String cliente, String direccion){
        this.id = id;
        this.cliente = cliente;
        this.direccion = direccion;
        this.despachado = false;
        this.cancelado = false;
    }
    // asignacion automatica
    public void asignarRepartidor(){
        this.repartidor = "Repartidor Auto " + (int)(Math.random()*100);
        System.out.println("Repartidor asignado: " + this.repartidor);
    }
    // asignacion manual
    public void asignarRepartidor(String nombre){
        this.repartidor = nombre;
        System.out.println("Repartidor asignado: " + this.repartidor);
    }
    //metodo comun
    public void mostrarResumen() {
        System.out.println("\n --- Resumen del pedido ---");
        System.out.println("ID: " + id);
        System.out.println("Cliente: " + cliente);
        System.out.println("Direccion: " + direccion);
        System.out.println("Repartidor: " + repartidor != null ? repartidor : "No asignado"); //excepcion en caso de que no haya nadie asignado
        System.out.println("Tiempo estimado: " + calcularTiempoEntrega() + " minutos");
        System.out.println("Estado: " + (cancelado ? "Cancelado" : (despachado ? "Despachado" : "Pendiente")));
    }
    //metodo abstracto que todas las subclases implementaran de forma distinta
        public abstract int calcularTiempoEntrega();

    //getters y setters
    public String getId() { return id; }
    public String getCliente() { return cliente; }
    public String getRepartidor() { return repartidor; }
    public boolean isDespachado() { return despachado; }
    public boolean isCancelado() { return cancelado; }
    public void setCancelado(boolean cancelado) { this.cancelado = cancelado; }
    public void setDespachado(boolean cancelado) { this.despachado = despachado; }

    }

