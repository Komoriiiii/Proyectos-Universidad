package model;

public class Empleado implements Registrable {
    private String rut;
    private String nombre;
    private String cargo;
    private String area;
    private double salario;

    public Empleado(String rut, String nombre, String cargo, String area, double salario) {
        this.rut = rut;
        this.nombre = nombre;
        this.cargo = cargo;
        this.area = area;
        this.salario = salario;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    @Override
    public String mostrarResumen(){
        return String.format(
                "\n--- Empleado ---" +
                        "Rut: %s\n" +
                        "Nombre: %s\n" +
                        "Cargo: %s\n" +
                        "Area: %s\n" +
                        "Salario: $%.2f\n" +
                        "----------------",
                rut,nombre,cargo,area,salario
        );
    }
}
