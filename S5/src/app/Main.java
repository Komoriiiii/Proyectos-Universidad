package ui;

import data.GestorDatos;
import model.CentroCultivo;
import util.FormateadorSalida;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Clase Principal donde ejecutar el sistema
 */

public class Main {
    private static GestorDatos gestor;
    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        gestor = new GestorDatos();

        cargarDatosIniciales();

        if (gestor.getCantidadCentros() > 0) {
            menuPrincipal();
        } else {
            FormateadorSalida.imprimirError("No hay datos para trabajar. Finalizando programa.");
        }

        scanner.close();
        System.out.println("\n Gracias por usar el sistema de Gestion de Salmontt.");
    }

    /**
     * Cargamos los datos iniciales desde el archivo
     */
    private static void cargarDatosIniciales() {
        FormateadorSalida.imprimirSubtitulo("Cargando datos desde archivo...");

        // intentar multiples rutas
        String[] rutasPosibles = {
                "resources/centros_cultivo.txt",
                "src/resources/centros_cultivo.txt",
                "centros_cultivo.txt"
        };

        boolean cargado = false;
        for (String ruta : rutasPosibles) {
            System.out.println("Intentando cargar desde: " + ruta);
            if (gestor.cargarDatos(ruta)) {
                cargado = true;
                break;
            }
        }
        if (!cargado) {
            FormateadorSalida.imprimirError("No se pudo cargar el archivo");
        }
    }

    /**
     * menu principal del sistema
     */

    private static void menuPrincipal() {
        int opcion;

        do {
            mostrarMenuPrincicpal();
            opcion = leerOpcion();

            switch (opcion) {
                case 1 -> listarTodosCentros();
                case 2 -> buscarCentroPorNombre();
                case 3 -> filtrarPorProduccion();
                case 4 -> filtrarPorComuna();
                case 5 -> filtrarPorEstado();
                case 6 -> mostrarEstadisticas();
                case 0 -> System.out.println("\n Saliendo del Sistema...");
                default -> FormateadorSalida.imprimirError("Opcion no valida");
            }

            if (opcion != 0) {
                pausar();
            }

        } while (opcion != 0);
    }

    private static void mostrarMenuPrincicpal() {
        FormateadorSalida.imprimirEncabezado("                                            Menu Principal");
        System.out.println(" 1. Listar todos los centros de cultivo");
        System.out.println(" 2. Buscar centro por nombre");
        System.out.println(" 3. Filtrar por produccion");
        System.out.println(" 4. Filtrar por comuna");
        System.out.println(" 5. Filtrar por estado");
        System.out.println(" 6. Ver estadisticas generales");
        System.out.println(" 0. Salir");;
        System.out.println("=".repeat(100));
        System.out.println("\n Seleccione una opcion: ");
    }

    /**
     * opcion 1. listar todos los centros
     */
    private static void listarTodosCentros() {
        FormateadorSalida.imprimirEncabezado("                             Listado completo de centros de cultivo: ");

        ArrayList<CentroCultivo> centros = gestor.getCentros();

        if (centros.isEmpty()) {
            System.out.println("\n No hay centros registrados.");
            return;
        }

        FormateadorSalida.imprimirEncabezadoTabla();
        for (int i = 0; i < centros.size(); i++) {
            FormateadorSalida.imprimirCentroFormato(centros.get(i), i + 1);
        }

        System.out.println("-".repeat(100));
        System.out.printf("\nTotal de centros: %d%n", centros.size());
        System.out.printf("Produccion total: %.2f toneladas%n", gestor.calcularProduccionTotal());
    }

    /**
     * opcion 2. buscar centro por nombre
     */
    private static void buscarCentroPorNombre() {
        FormateadorSalida.imprimirEncabezado("                             Busqueda de centro por nombre");

        System.out.println("\n Ingrese el nombre del centro a buscar: ");
        String texto = scanner.nextLine();

        ArrayList<CentroCultivo> resultados = gestor.buscarPorNombreParcial(texto);
        if (resultados.isEmpty()) {
            FormateadorSalida.imprimirAdvertencia("No se encontraron centros con ese nombre");
        } else {
            System.out.println("\nSe encontraron " + resultados.size() + " resultado(s):");
            FormateadorSalida.imprimirEncabezadoTabla();
            for (int i = 0; i < resultados.size(); i++) {
                FormateadorSalida.imprimirCentroFormato(resultados.get(i), i + 1);
            }
        }
    }

    /**
     * opcion 3. filtrar por produccion minima
     */
    private static void filtrarPorProduccion() {
        FormateadorSalida.imprimirEncabezado("                             Filtrar por produccion minima");

        System.out.println("\nIngrese las toneladas minimas: ");
        try {
            double minimo = Double.parseDouble(scanner.nextLine());

            ArrayList<CentroCultivo> filtrados = gestor.filtrarPorProduccion(minimo);

            if (filtrados.isEmpty()) {
                FormateadorSalida.imprimirAdvertencia("No hay centros con produccion mayor a " + minimo + " toneladas:");
            } else {
                System.out.println("\nCentros con produccion mayor a " + minimo + " toneladas:");
                FormateadorSalida.imprimirEncabezadoTabla();
                for (int i = 0; i < filtrados.size(); i++) {
                    FormateadorSalida.imprimirCentroFormato(filtrados.get(i), i + 1);
                }
                System.out.printf(" \nTotal filtrados: %d de %d centros%n",
                        filtrados.size(), gestor.getCantidadCentros());
                ;
            }
        } catch (NumberFormatException e) {
            FormateadorSalida.imprimirError("Ingrese un numero valido");
        }
    }

    /**
     * opcion 4. filtrar por comuna
     */
    private static void filtrarPorComuna() {
        FormateadorSalida.imprimirEncabezado("                             Filtrar por comuna");

        ArrayList<String> comunas = gestor.obtenerComunasUnicas();
        System.out.println("\nComunas disponibles: ");
        for (int i = 0; i < comunas.size(); i++) {
            System.out.printf("    %d. %s%n", i + 1, comunas.get(i));
        }
        System.out.println("\nIngrese el nombre de la comuna: ");
        String comuna = scanner.nextLine();
        ArrayList<CentroCultivo> filtrados = gestor.filtrarPorComuna(comuna);
        if (filtrados.isEmpty()) {
            FormateadorSalida.imprimirAdvertencia("No hay centros en la comuna: " + comuna);
        } else {
            System.out.println("\nCentros en " + comuna + ":");
            FormateadorSalida.imprimirEncabezadoTabla();
            for (int i = 0; i < filtrados.size(); i++) {
                FormateadorSalida.imprimirCentroFormato(filtrados.get(i), i + 1);
            }
        }
    }

    /**
     * opcion 5. filtra por estado
     */
    private static void filtrarPorEstado() {
        FormateadorSalida.imprimirEncabezado("                             Filtrar por estado");

        System.out.println("\nEstados disponibles: ");
        System.out.println("    1. Activo");
        System.out.println("    2. Inactivo");
        System.out.println("    3. En Mantenimiento");

        System.out.println("\nIngrese el estado a filtrar: ");
        String estado = scanner.nextLine();

        ArrayList<CentroCultivo> filtrados = gestor.filtrarPorEstado(estado);
        if (filtrados.isEmpty()) {
            FormateadorSalida.imprimirAdvertencia("No se encontraron centros con ese estado: " + estado);
        } else {
            System.out.println("\nCentros con estado '" + estado + "':");
            FormateadorSalida.imprimirEncabezadoTabla();
            for (int i = 0; i < filtrados.size(); i++) {
                FormateadorSalida.imprimirCentroFormato(filtrados.get(i), i + 1);
            }
        }
    }

    /**
     * opcion 6. mostrar estadisticas generales
     */
    private static void mostrarEstadisticas() {
        FormateadorSalida.imprimirEncabezado("                             Estadisticas generales del sistema");
        int total = gestor.getCantidadCentros();
        double promedio = gestor.calcularPromedioProduccion();
        CentroCultivo mayor = gestor.obtenerCentroMayorProduccion();
        CentroCultivo menor = gestor.obtenerCentroMenorProduccion();

        System.out.println("\nProduccion: ");
        System.out.printf("    - Total de centros:       %d%n", total);
        System.out.printf("    - Produccion total:       %.2f toneladas%n", gestor.calcularProduccionTotal());
        System.out.printf("    - Produccion promedio:    %.2f toneladas%n", promedio);
        System.out.printf("    - Produccion maxima:      %.2f toneladas(%s)%n",
                mayor.getToneladasProducidas(), mayor.getNombre());
        System.out.printf("    - Produccion minima:      %.2f toneladas (%s)%n",
                menor.getToneladasProducidas(), menor.getNombre());
        System.out.println("\nEstados: ");
        System.out.printf("    - Activos:             %d%n", gestor.contarPorEstado("Activo"));
        System.out.printf("    - Inactivos:           %d%n", gestor.contarPorEstado("Inactivo"));
        System.out.printf("    - En Mantenimiento:    %d%n", gestor.contarPorEstado("En Mantenimiento"));

        System.out.println("\nDistribucion: ");
        System.out.printf("    - Comunas con centros:          %d%n", gestor.obtenerComunasUnicas().size());

        long altaProduccion = gestor.getCentros().stream()
                .filter(CentroCultivo::esAltaProduccion).count();
        System.out.printf("    - Centros de alta produccion:   %d (>1000 ton)%n", altaProduccion);
    }
    /**Lee una opcion del menu
     */
    private static int leerOpcion(){
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    /** pausa la ejecucion hasta que el usuario aprete enter
     */
    private static void pausar(){
        System.out.println("\nPresione enter para continuar...");
        scanner.nextLine();
    }
}
