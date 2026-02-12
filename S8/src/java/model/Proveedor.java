package model;

// representa a un proveedor de la empresa

public class Proveedor implements Registrable {
    private String rut;
    private String razonSocial;
    private String tipoServicio;
    private String contacto;

    public Proveedor(String rut, String razonSocial, String tipoServicio, String contacto) {
        this.rut = rut;
        this.razonSocial = razonSocial;
        this.tipoServicio = tipoServicio;
        this.contacto = contacto;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    @Override
    public String mostrarResumen(){
        return String.format(
                "\n--- Proveedor ---" +
                        "Rut: %s\n" +
                        "Razon Social: %s\n" +
                        "Tipo de Servicio: %s\n" +
                        "Contacto: %s\n" +
                        "---------------------------",
                rut, razonSocial, tipoServicio, contacto
        );
    }
}
