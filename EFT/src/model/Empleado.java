package model;

public class Empleado extends Persona {
    private String cargo;
    private double salario;
    private String fechaContratacion;
    private String departamento;

    public Empleado(String nombre, String apellido, Rut rut, String telefono, String email, Direccion direccion, String cargo, double salario, String fechaContratacion, String departamento) {
        super(nombre, apellido, rut, telefono, email, direccion);
        this.cargo = cargo;
        this.salario = salario;
        this.fechaContratacion = fechaContratacion;
        this.departamento = departamento;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public String getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(String fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    @Override
    public void registrar() {
        System.out.println("Empleado registrado: " + getNombreCompleto() + "\n - Cargo: " + cargo);
    }
    @Override
    public String mostrarDatos() {
        return "\n --- Empleado: \n" + " " + toString() +
                "\n - Salario: $" + salario +
                "\n - Fecha de Contratacion: " + fechaContratacion;
    }
    @Override
    public String toString() {
        return super.toString() + "\n\n - Cargo: " + cargo + "\n - Departamento: " + departamento;
    }
}

