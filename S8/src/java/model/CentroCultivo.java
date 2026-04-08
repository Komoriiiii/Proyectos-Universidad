package model;

public class CentroCultivo extends Entidad {
    private int capacidadJaulas;
    private double produccionAnual;

    public CentroCultivo(String id, String nombre, String ubicacion, int capacidadJaulas, double produccionAnual) {
        super(id, nombre, ubicacion);
        this.capacidadJaulas = capacidadJaulas;
        this.produccionAnual = produccionAnual;
    }

    public int getCapacidadJaulas() {
        return capacidadJaulas;
    }

    public void setCapacidadJaulas(int capacidadJaulas) {
        this.capacidadJaulas = capacidadJaulas;
    }

    public double getProduccionAnual() {
        return produccionAnual;
    }

    public void setProduccionAnual(double produccionAnual) {
        this.produccionAnual = produccionAnual;
    }

    @Override
    public String mostrarResumen() {
        return String.format(
                "\n--- Centro de Cultivo--- \n" +
                "ID: %s\n" +
                "Nombre: %s\n" +
                "Ubicacion: %s\n" +
                "Capacidad de Jaulas: %d\n" +
                "Produccion Anual: %.2f toneladas\n" +
                "----------------------------------",
        id, nombre, ubicacion, capacidadJaulas, produccionAnual
        );
    }
}
