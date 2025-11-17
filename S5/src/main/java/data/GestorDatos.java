package data;

import model.CentroCultivo;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Clase para gestionar la lectura de datos desde archivo y usada para crear objetos CentroCultivo
 */

public class GestorDatos {
    private ArrayList<CentroCultivo> centros;

    public GestorDatos() {
        this.centros = new ArrayList<>();
    }
    /**
     * lee el archivo y carga los centros de cultivo en la lista
     * @param rutaArchivo es la ruta del archivo a leer
     * @return true si la lectura salio bien, false de lo contrario
     */
    public boolean cargarDatos(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader( new FileReader(rutaArchivo))) {
            String linea;

            while ((linea = br.readLine()) != null){
                //separacion de los datos por punto y coma
                String[] datos = linea.split(";");

                //verificacion  de que la linea tenga 3 campos
                if (datos.length == 3) {
                    String nombre = datos[0].trim();
                    String comuna = datos[1].trim();
                    double toneladas = Double.parseDouble(datos[2].trim());

                    //creamos el objeto CentroCultivo y se agrega a la lista
                    CentroCultivo centro = new CentroCultivo (nombre, comuna, toneladas);
                    centros.add(centro);
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            return false;
        } catch (NumberFormatException e) {
            System.err.println("Error al convertir las toneladas: " + e.getMessage());
            return false;
        }
    }
    /**
     * se obtiene la lista completa de los centros de cultivo
     * @return ArrayList con todos los centros
     */
    public ArrayList<CentroCultivo> getCentros() {
        return centros;
    }

    /** Filtra centros con produccion mayor a un valor dado
     * @param toneladasMinimas toneladas minimas para filtrar
     * @return ArrayList con los centros filtrados
     */
    public ArrayList<CentroCultivo> filtrarPorProduccion(double toneladasMinimas) {
        ArrayList<CentroCultivo> centrosFiltrados = new ArrayList<>();

        for (CentroCultivo centro : centros) {
            if (centro.getToneladasProducidas() > toneladasMinimas) {
                centrosFiltrados.add(centro);
            }
        }
        return centrosFiltrados;
    }
    /** se busca el centro con mayor produccion
     * @return CentroCultivo con mayor produccion o null si no hay centros
     */
    public CentroCultivo obtenerCentroMayorProduccion(){
        if (centros.isEmpty()) {
            return null;
        }

        CentroCultivo mayorProduccion = centros.get(0);

        for (CentroCultivo centro : centros) {
            if (centro.getToneladasProducidas() > mayorProduccion.getToneladasProducidas()) {
                mayorProduccion = centro;
            }
        }
        return mayorProduccion;
    }
    /**
     * Filtra centros por comuna
     * @param comuna nombre de la comuna a buscar
     * @return ArrayList con los centros de la comuna
     */
    public ArrayList<CentroCultivo> filtrarPorComuna(String comuna) {
        ArrayList<CentroCultivo> centrosFiltrados = new ArrayList<>();

        for (CentroCultivo centro : centros) {
            if (centro.getComuna().equalsIgnoreCase(comuna)) {
                centrosFiltrados.add(centro);
            }
        }
        return centrosFiltrados;
    }
}
