package model;

/**
 * clase para representar los empleados de la empresa
 * hereda desde la clase persona y anade atributos para especificar que rol cumple en la empresa
 */
public class Empleado extends Persona {
    private String cargo;
    private String sectorLaboral;
    private Double sueldo;

    /**
     * constructor completo de empleado
     */
    public Empleado(String rut, String nombre, String apellido, String telefono, String email,
                    Direccion direccion, String cargo,
                    String sectorLaboral, double sueldo) {
        super(rut, nombre, apellido, telefono, email, direccion);
        this.cargo = cargo;
        this.sectorLaboral = sectorLaboral;
        this.sueldo = sueldo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getSectorLaboral() {
        return sectorLaboral;
    }

    public void setSectorLaboral(String sectorLaboral) {
        this.sectorLaboral = sectorLaboral;
    }

    public Double getSueldo() {
        return sueldo;
    }

    public void setSueldo(Double sueldo) {
        this.sueldo = sueldo;
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "RUT='" + getRut() + '\'' +
                ", Nombre='" + getNombreCompleto() + '\'' +
                ", cargo='" + cargo + '\'' +
                ", sectorLaboral='" + sectorLaboral + '\'' +
                ", Sueldo=$" + String.format("%,.0f", sueldo) +
                ", Teléfono='" + getTelefono() + '\'' +
                ", Email='" + getEmail() + '\'' +
                ", Dirección=" + (getDireccion() != null ? getDireccion().toString() : "No registrada") +
                '}';
    }
}
