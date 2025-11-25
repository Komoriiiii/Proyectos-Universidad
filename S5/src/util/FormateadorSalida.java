package util;

import model.CentroCultivo;

/**
 * clase para formatear la salida por consola
 * da los metodos para mostrar la informacion de manera organizada
 */

public class FormateadorSalida {
    private static final String lineaSeparadora = "=".repeat(100);
    private static final String lineaGuion = "-".repeat(100);

    //encabezado principal

    public static void imprimirEncabezado (String titulo) {
        System.out.println("\n" + lineaSeparadora);
        System.out.println(" " + titulo.toUpperCase());
        System.out.println(lineaSeparadora);
    }

    //encabezado secundario

    public static void imprimirSubtitulo (String subtitulo) {
        System.out.println("\n" + lineaGuion);
        System.out.println(" " + subtitulo.toUpperCase());
        System.out.println(lineaGuion);
    }

    // print de informacion de un centro de cultivo en formato tabla

    public static void imprimirCentroFormato(CentroCultivo centro, int numero) {
        System.out.printf("%-4d | %-25s | %-15s | %10.2f ton | %-18s | %3d emp%n",
                numero,
                centro.getNombre(),
                centro.getComuna(),
                centro.getToneladasProducidas(),
                centro.getEstado(),
                centro.getEmpleados());
    }

    //print del encabezado de la tabla de centros

    public static void imprimirEncabezadoTabla(){
        System.out.println(lineaGuion);
        System.out.printf("%-4s | %-25s | %-15s | %-12s | %-18s | -7s%n",
                "#", "Nombre", "Comuna", "Produccion", "Estado", "Empleados");
        System.out.println(lineaGuion);
    }

    //print mensaje exitoso
    public static void imprimirExito (String mensaje) {
        System.out.println("\n Proceso impreso con exito: " + mensaje);
    }

    //print mensaje error
    public static void imprimirError(String mensaje) {
        System.err.println("\n Error: " + mensaje);
    }

    //print mensaje de advertencia

    public static void imprimirAdvertencia(String mensaje) {
        System.out.println("\n Advertencia: " + mensaje);
    }

    //print informacion detallada de un centro

    public static void imprimirDetalleCompleto(CentroCultivo centro) {
        System.out.println("\n =========================================");
        System.out.println("           Centro de cultivo");
        System.out.println("========================================");
        System.out.printf("   Comuna:      %s%n", centro.getComuna());
        System.out.printf("   Produccion:      %.2f toneladas%n", centro.getToneladasProducidas());
        System.out.printf("   Categoria:      %s%n", centro.getCategoria());
        System.out.printf("   Estado:      %s%n", centro.getEstado());
        System.out.printf("   Empleados:      %d%n", centro.getEmpleados());
        System.out.printf("   Produccion/Empleado:      %.2f tons/empleado%n", centro.getProduccionPorEmpleado());
        System.out.println("========================================");
    }

    //print de estadisticas resumidas

    public static void imprimirEstadisticas(int total, double promedio, double max, double min){
        System.out.println("\n Estadisticas Generales:");
        System.out.printf(" - Total de centros:  %d%n", total);
        System.out.printf(" - Produccion promedio: %.2f toneladas%n", promedio);
        System.out.printf(" - Produccion maxima: %.2f toneladas%n", max);
        System.out.printf(" - Produccion minima: %.2f toneladas%n", min);
    }
}

