package data;

import model.*;
import java.util.ArrayList;

/** gestor para daministrar las entidades registrables del sistema
 */

public class GestorEntidades {
    private ArrayList<Registrable> entidades;

    public GestorEntidades() {
        this.entidades = new ArrayList<>();
        inicializarDatosPrueba();
    }

    //agregamos una nueva entidad al sistema

    public void agregarEntidad(Registrable entidad) {
        entidades.add(entidad);
    }

    //se obtienen todas las entidades registradas

    public ArrayList<Registrable> getEntidades() {
        return entidades;
    }

    //se muestran las entidades con logica diferenciada segun el tipo

    public String mostrarTodasLasEntidades() {
        StringBuilder resultado = new StringBuilder();
        resultado.append("Total de Entidades Registradas: ").append(getEntidades().size()).append("\n\n");

        int centros = 0, plantas = 0, proveedores = 0, empleados = 0, transportes = 0;

        for (Registrable entidad : entidades) {
            resultado.append(entidad.mostrarResumen()).append("\n\n");

            //se usa el instanceof para identificar el tipo y aplicar logica diferenciada
            if (entidad instanceof CentroCultivo){
                centros++;
                CentroCultivo centro = (CentroCultivo) entidad;
                resultado.append("Tipo identificado: Centro de Cultivo (Produccion: ")
                        .append(centro.getProduccionAnual()).append(" ton\n\n");
            } else if (entidad instanceof PlantaProceso){
                plantas++;
                PlantaProceso planta = (PlantaProceso) entidad;
                resultado.append("Tipo identificado: Planta de Proceso (Producto: ")
                        .append(planta.getTipoProducto()).append(")\n\n");
            } else if (entidad instanceof Proveedor){
                proveedores++;
                Proveedor proveedor = (Proveedor) entidad;
                resultado.append("Tipo identificado: Proveedor (Servicio: ")
                        .append(proveedor.getTipoServicio()).append(")\n\n");
            } else if (entidad instanceof Empleado){
                empleados++;
                Empleado empleado = (Empleado) entidad;
                resultado.append("Tipo identificado: Empleado (Area: ")
                        .append(empleado.getArea()).append(")\n\n");
            } else if (entidad instanceof EquipoTransporte){
                transportes++;
                EquipoTransporte transporte = (EquipoTransporte) entidad;
                resultado.append("Tipo identificado: Equipo de Transporte (Tipo: ")
                        .append(transporte.getTipo()).append(")\n\n");
            }
        }

        resultado.append("---------- Resumen por Tipo ----------");
        resultado.append("Centros de Cultivo: ").append(centros).append("\n");
        resultado.append("Plantas de Proceso: ").append(plantas).append("\n");
        resultado.append("Proveedores: ").append(proveedores).append("\n");
        resultado.append("Empleados: ").append(empleados).append("\n");
        resultado.append("Equipos de Transporte: ").append(transportes).append("\n");
        resultado.append("---------------------------------");
        return resultado.toString();
    }

    /**inicializamos datos para probar
     */

    private void inicializarDatosPrueba() {
        entidades.add(new CentroCultivo("CC001", "Centro Chiloe Norte", "Chiloe", 24, 1500));
        entidades.add(new PlantaProceso("PP001","Planta Puerto Montt","Puerto Montt", 50,"Salmon Fresco"));
        entidades.add(new Proveedor("76.123.456-7","Alimentos del Mar S.A","Alimento para Peces","contacto@alimentosdelmar.cl"));
        entidades.add(new Empleado("12.345.678-9","Juan Perez","Supervisor de Cultivo","Operaciones",1000000));
    }
    //obtenemos el numero total de entidades
    public int getTotalEntidades() {
        return entidades.size();
    }
}
