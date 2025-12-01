package model;

/** clase que representa una plata de procesamiento, hereda de unidad operativa y agrega atributos
 */

public class PlantaProceso extends UnidadOperativa {
    private int capacidadProceso;

    /** constructor de la clase
     */

    public PlantaProceso(String nombre, String comuna, int capacidadProceso) {
        super (nombre,comuna);
        this.capacidadProceso = capacidadProceso;
    }

    public int getCapacidadProceso() {
        return capacidadProceso;
    }

    public void setCapacidadProceso(int capacidadProceso) {
        this.capacidadProceso = capacidadProceso;
    }

    @Override
    public String toString() {
        return  "- " + nombre + " - \n" +
                "\n Comuna: " + comuna +
                "\n Capacidad de Procesamiento: " + capacidadProceso +
                ".\n";
    }
}