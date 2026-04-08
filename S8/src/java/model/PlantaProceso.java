package model;

//representa una planta de procesamiento

public class PlantaProceso extends Entidad {
    private int capacidadProceso;
    private String tipoProducto;

    public PlantaProceso(String id, String nombre, String ubicacion, int capacidadProceso, String tipoProducto) {
        super(id, nombre, ubicacion);
        this.capacidadProceso = capacidadProceso;
        this.tipoProducto = tipoProducto;
    }

    public int getCapacidadProceso() {
        return capacidadProceso;
    }

    public void setCapacidadProceso(int capacidadProceso) {
        this.capacidadProceso = capacidadProceso;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    @Override
    public String mostrarResumen(){
        return String.format(
                "\n--- Planta de Procesamiento ---\n" +
                "ID: %s\n" +
                        "Nombre: %s\n" +
                        "Ubicacion: %s\n" +
                        "Capacidad de Procesamiento: %d tons/dia\n" +
                "Tipo de Producto: %s\n" +
                        "----------------------",
                id,nombre,ubicacion,capacidadProceso,tipoProducto
        );
    }
}
