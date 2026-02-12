package model;

public class Proveedor extends Persona {
    private String razonSocial;
    private String rubro;
    private String codigoProveedor;
    public Proveedor(String nombre, String apellido, Rut rut, String telefono, String email, Direccion direccion, String razonSocial, String rubro, String codigoProveedor) {
        super(nombre, apellido, rut, telefono, email, direccion);
        this.razonSocial = razonSocial;
        this.rubro = rubro;
        this.codigoProveedor = codigoProveedor;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getRubro() {
        return rubro;
    }

    public void setRubro(String rubro) {
        this.rubro = rubro;
    }

    public String getCodigoProveedor() {
        return codigoProveedor;
    }

    public void setCodigoProveedor(String codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }

    @Override
    public void registrar(){
        System.out.println("Proveedor registrado: " + razonSocial + " - Codigo: " + codigoProveedor);
    }
    @Override
    public String mostrarDatos() {
        return "Proveedor: " + toString() +
                ", Razon social: " + razonSocial +
                ", Rubro: " + rubro +
                ", Codigo: " +  codigoProveedor;
    }
    @Override
    public String toString(){
        return super.toString() + ", Razon social: " + razonSocial + ", Rubro: " + rubro;
    }
}
