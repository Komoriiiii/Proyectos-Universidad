package data;

import model.CentroCultivo;
import util.ValidadorDatos;
import util.FormateadorSalida;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collector;
import java.util.stream.Collectors;


/**
 * Clase para gestionar la lectura de datos desde archivo y usada para crear objetos CentroCultivo
 */

public class GestorDatos {
    private ArrayList<CentroCultivo> centros;
    private int contadorErrores;

    public GestorDatos() {
        this.centros = new ArrayList<>();
        this.contadorErrores = 0;
    }
    /**
     * lee el archivo y carga los centros de cultivo en la lista
     * @param rutaArchivo es la ruta del archivo a leer
     * @return true si la lectura salio bien, false de lo contrario
     */

    public boolean cargarDatos(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            int lineaNumero = 0;

            while ((linea = br.readLine()) != null) {
                lineaNumero++;

                try {
                    //validar formato de las lineas
                    if (!ValidadorDatos.validarFormatoLinea(linea, 5)) {
                        FormateadorSalida.imprimirAdvertencia(
                                "Linea" + lineaNumero + " tiene formato incorrecto: " + linea);
                        contadorErrores++;
                        continue;
                    }
                    //separacion de los datos por punto y coma
                    String[] datos = linea.split(";");

                    //validar y limpiar datos
                    String nombre = datos[0].trim();
                    String comuna = datos[1].trim();
                    String estado = datos[3].trim();

                    if (!ValidadorDatos.validarTextoNoVacio(nombre)) {
                        throw new IllegalArgumentException("Nombre no puede estar vacio");
                    }

                    if (!ValidadorDatos.validarTextoNoVacio(comuna)) {
                        throw new IllegalArgumentException("Comuna no puede estar vacio");
                    }

                    double toneladas = Double.parseDouble(datos[2].trim());
                    if (!ValidadorDatos.validarNumeroNoNegativo(toneladas)) {
                        throw new IllegalArgumentException("Toneladas no puede estar vacio");
                    }

                    int empleados = Integer.parseInt(datos[4].trim());
                    if (!ValidadorDatos.validarNumeroNoNegativo(empleados)) {
                        throw new IllegalArgumentException("Empleadas no puede estar vacio");
                    }
                    if (!ValidadorDatos.validarEstado(estado)) {
                        FormateadorSalida.imprimirAdvertencia(
                                "Linea " + lineaNumero + ": Estado invalido '" + estado + "', se usara 'Activo'");
                        estado = "Activo";
                    }

                    CentroCultivo centro = new CentroCultivo(nombre, comuna, toneladas, estado, empleados);
                    centros.add(centro);

                } catch (NumberFormatException e) {
                    FormateadorSalida.imprimirError(
                            "Linea " + lineaNumero + ": Error al convertir numeros - " + e.getMessage());
                    contadorErrores++;
                } catch (IllegalArgumentException e) {
                    FormateadorSalida.imprimirError(
                            "Linea " + lineaNumero + ": " + e.getMessage());
                    contadorErrores++;
                } catch (Exception e) {
                    FormateadorSalida.imprimirError(
                            "Linea " + lineaNumero + ": Error inesperado - " + e.getMessage());
                    contadorErrores++;
                }
            }

            if (centros.isEmpty()) {
                FormateadorSalida.imprimirError("No se cargo ningun centro valido");
                return false;
            }

            if (contadorErrores > 0) {
                FormateadorSalida.imprimirAdvertencia(
                        "Se cargaron " + centros.size() + " centros con " + contadorErrores + " errores");
            } else {
                FormateadorSalida.imprimirExito(
                        "Se cargaron " + centros.size() + " centros exitosamente");
            }
            return true;
        } catch (IOException e) {
            FormateadorSalida.imprimirError("No se pudo leer el archivo: " + e.getMessage());
            return false;
        }
    }

    // agrega un nuevo centro a la coleccion

public boolean agregarCentro(CentroCultivo centro){
    try {
        if (centro == null){
            throw new IllegalArgumentException("El centro no puede ser nulo");
        }
        if (buscarPorNombre(centro.getNombre()) != null) {
            FormateadorSalida.imprimirAdvertencia("Ya existe un centro con ese nombre");
            return false;
        }
        centros.add(centro);
        FormateadorSalida.imprimirExito("Centro agregado correctamente");
        return true;
    } catch (Exception e){
        FormateadorSalida.imprimirError(e.getMessage());
        return false;
    }
}

//Buscador de centro por nombre exacto

public CentroCultivo buscarPorNombre(String nombre) {
    for (CentroCultivo centro : centros){
        if (centro.getNombre().equalsIgnoreCase(nombre)){
            return centro;
        }
    }
    return null;
}

//busca centros por nombre parcial

public ArrayList<CentroCultivo> buscarPorNombreParcial(String textoBusqueda) {
    ArrayList<CentroCultivo> resultados = new ArrayList<>();
    String busqueda = textoBusqueda.toLowerCase();

    for (CentroCultivo centro : centros) {
        if (centro.getNombre().toLowerCase().contains(busqueda)) {
            resultados.add(centro);
        }
    }
    return resultados;
    }

    // lista completa de centros
    public ArrayList<CentroCultivo> getCentros() {
        return new ArrayList<>(centros);
    }

    // filtra centros con produccion mayor a un valor dado
    public ArrayList<CentroCultivo> filtrarPorProduccion(double toneladasMinimas){
        return centros.stream()
                .filter(c-> c.getToneladasProducidas()>toneladasMinimas)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    //filtra centros por estado
    public ArrayList<CentroCultivo> filtrarPorEstado(String estado) {
        return centros.stream()
                .filter (c-> c.getEstado().equalsIgnoreCase(estado))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    //filtra centros por comuna
    public ArrayList<CentroCultivo> filtrarPorComuna(String comuna) {
        return centros.stream()
                .filter(c -> c.getComuna().equalsIgnoreCase(comuna))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    //Obtiene el centro con mayor produccion
    public CentroCultivo obtenerCentroMayorProduccion(){
        return centros.stream()
                .max(Comparator.comparingDouble(CentroCultivo::getToneladasProducidas))
                .orElse(null);
    }

    //obtiene el centro con menor produccion
    public CentroCultivo obtenerCentroMenorProduccion(){
        return centros.stream()
                .min(Comparator.comparingDouble(CentroCultivo::getToneladasProducidas))
                .orElse(null);
    }
    //calcula el promedio de produccion

    public double calcularPromedioProduccion(){
        if (centros.isEmpty()) return 0;

        return centros.stream()
                .mapToDouble(CentroCultivo::getToneladasProducidas)
                .average()
                .orElse(0);
    }

    //calcula la produccion total
    public double calcularProduccionTotal(){
        return centros.stream()
                .mapToDouble(CentroCultivo::getToneladasProducidas)
                .sum();
    }

    //cuenta centros por estado

    public int contarPorEstado(String estado){
        return (int) centros.stream()
                .filter(c -> c.getEstado().equalsIgnoreCase(estado))
                .count();
    }

    //obtiene lista comunas unicas

    public ArrayList<String> obtenerComunasUnicas(){
        return centros.stream()
                .map(CentroCultivo::getComuna)
                .distinct()
                .sorted()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    //ordena centros por produccion descendente

    public ArrayList<CentroCultivo> ordenarPorProduccion(){
        return centros.stream()
                .sorted(Comparator.comparingDouble(CentroCultivo::getToneladasProducidas).reversed())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public int getContadorErrores(){
        return contadorErrores;
    }

    public int getCantidadCentros(){
        return centros.size();
    }
}
