package model;

public abstract class Persona implements Registrable {
    private String nombre;
    private String apellido;
    private Rut rut;
    private String telefono;
    private String email;
    private Direccion direccion;

    public Persona(String nombre, String apellido, Rut rut, String telefono, String email, Direccion direccion) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.rut = rut;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Rut getRut() {
        return rut;
    }

    public void setRut(Rut rut) {
        this.rut = rut;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    @Override
    public String obtenerIdentificador() {
        return rut.getRutCompleto();
    }


    @Override
    public String toString() {
        return "Nombre: " + getNombreCompleto() +
                "\n Rut: " + rut +
                "\n Email: " + email +
                "\n Telefono: " + telefono +
                "\n\n --- Direccion --- " + direccion;
    }
}

