package ui;

import data.GestorDatos;
import model.CentroCultivo;

import java.sql.SQLOutput;
import java.util.ArrayList;

/**
 * Clase Principal donde ejecutar el sistema
 */

public class Main  {
    public static void main(String[] args) {
        System.out.println("\n ---  Sistema de Gestion Salmontt ---");

        //creamos instancia del gestor de datos
        GestorDatos gestor = new GestorDatos();

        //Cargamos datos desde el archivo
        String rutaArchivo = "resources/centros_cultivo.txt";

        if (gestor.cargarDatos(rutaArchivo)) {
            System.out.println("\nDatos cargados\n");
        } else {
            System.out.println("Error al cargar datos\n");
            return;
        }
        //mostramos los elementos de la lista
        System.out.println(" - 1. Todos los centros de cultivo - ");
        ArrayList <CentroCultivo> todosCentros = gestor.getCentros();

        if (todosCentros.isEmpty()) {
            System.out.println("No hay centros de cultivo\n");
        } else {
            for (int i = 0; i < todosCentros.size(); i++) {
                CentroCultivo centroCultivo = todosCentros.get(i);
                System.out.println((i + 1) + ". " + centroCultivo.getNombre()
                + " - " + centroCultivo.getComuna()
                + " - " + centroCultivo.getToneladasProducidas() + " toneladas");

            }
        }
        System.out.println("\nTotal de centros: " + todosCentros.size());
        System.out.println();

        //filtro segun condiciones
        System.out.println(" - 2. Centros con produccion mayor a mil toneladas - ");
        ArrayList <CentroCultivo> centrosAlta = gestor.filtrarPorProduccion(1000);
        if (centrosAlta.isEmpty()) {
            System.out.println("No hay centros de cultivo con produccion sobre 1000 toneladas\n");
        } else {
            for (CentroCultivo centro : centrosAlta) {
                System.out.println(" - "+ centro.getNombre() +
                        " (" + centro.getComuna() + "): " +
                        centro.getToneladasProducidas() + " toneladas");
            }
        }
        System.out.println("\n Centros con alta produccion: " + centrosAlta.size());
        System.out.println();

        //identificamos el centro con mayor produccion

        System.out.println(" - 3. Centro de Mayor Produccion");
        CentroCultivo mayorProduccion = gestor.obtenerCentroMayorProduccion();
        if (mayorProduccion != null) {
            System.out.println(" Numero 1. " + mayorProduccion.getNombre());
            System.out.println(" Comuna: " + mayorProduccion.getComuna());
            System.out.println(" Produccion: " + mayorProduccion.getToneladasProducidas() + " toneladas");
        } else {
            System.out.println("No hay datos disponibles.");
        }
        System.out.println();
        System.out.println(" - Fin del Reporte - ");
    }
}