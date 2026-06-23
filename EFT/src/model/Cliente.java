package model;

public class Cliente extends Persona {
    private String codigoCliente;
    private String tipoCliente;
    private double limiteCredito;

    public Cliente (String nombre, String apellido, Rut rut, String telefono, String email, Direccion direccion, String codigoCliente, String tipoCliente, double limiteCredito) {
        super(nombre, apellido, rut, telefono, email, direccion);
        this.codigoCliente = codigoCliente;
        this.tipoCliente = tipoCliente;
        this.limiteCredito = limiteCredito;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public double getLimiteCredito() {
        return limiteCredito;
    }

    public void setLimiteCredito(double limiteCredito) {
        this.limiteCredito = limiteCredito;
    }

    @Override
    public void registrar() {
        System.out.println("Cliente registrado: " + getNombreCompleto() + " - Codigo: " + codigoCliente);
    }

    @Override
    public String mostrarDatos() {
        return " Cliente: " + "\n " + toString() +
                "\n - Codigo: " + codigoCliente +
                "\n - Tipo: " + tipoCliente +
                "\n - Limite Credito: $" + limiteCredito;
    }
    @Override
    public String toString(){
        return super.toString() + "\n\n - Codigo Cliente: " + codigoCliente;
    }
}
