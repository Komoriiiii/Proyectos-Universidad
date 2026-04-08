package model;

public class EquipoTransporte implements Registrable {
    private String patente;
    private String tipo;
    private double capacidadCarga;
    private String conductor;

    public EquipoTransporte(String patente, String tipo, double capacidadCarga, String conductor) {
        this.patente = patente;
        this.tipo = tipo;
        this.capacidadCarga = capacidadCarga;
        this.conductor = conductor;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getCapacidadCarga() {
        return capacidadCarga;
    }

    public void setCapacidadCarga(double capacidadCarga) {
        this.capacidadCarga = capacidadCarga;
    }

    public String getConductor() {
        return conductor;
    }

    public void setConductor(String conductor) {
        this.conductor = conductor;
    }

    @Override
    public String mostrarResumen(){
        return String.format(
                "\n--- Equipo de Transporte ---\n" +
                        "Patente: %s\n" +
                        "Tipo: %s\n" +
                        "Capacidad de Carga: %.2f toneladas\n" +
                        "Conductor: %s\n" +
                        "-------------------------------",
                patente,tipo,capacidadCarga,conductor
        );
    }
}
