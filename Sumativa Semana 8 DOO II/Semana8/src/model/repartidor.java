package model;

public class repartidor {

    private int id;
    private String nombre;

    public repartidor(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public repartidor(String nombre) {
        this.nombre = nombre;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    //para que se vea bonito en el jcombobox
    @Override
    public String toString() {
        return id + " - " + nombre;
    }
}
