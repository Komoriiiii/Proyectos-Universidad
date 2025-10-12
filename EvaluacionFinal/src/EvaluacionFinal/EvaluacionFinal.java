package evaluacionFinal;
import java.util.ArrayList;
import java.util.Scanner;

public class EvaluacionFinal {
    //constantes para los precios segun su ubicacion
    private static final double PRECIO_PLATEA = 25000;
    private static final double PRECIO_PALCO = 35000;
    private static final double PRECIO_GALERIA = 15000;
    
    //arreglos paralelos para gestionar las ventas
    private static  ArrayList<String> nombresClientes = new ArrayList<>();
    private static  ArrayList<Integer> edadesClientes = new ArrayList<>();
    private static ArrayList<String> ubicaciones = new ArrayList<>();
    private static ArrayList<String> asientos = new ArrayList<>();
    private static ArrayList<Double> preciosFinales = new ArrayList<>();
    private static ArrayList<Integer> descuentos = new ArrayList<>();
    private static ArrayList<Integer> idsVenta = new ArrayList<>();
    
    //mapa de asientos
    // 0. platea 20 asientos, 1. palco 15 asientos, 2. galeria 30 asientos
    private static boolean[][] asientosOcupados = new boolean[3][30];
    
    private static int contadorVentas = 1;
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        inicializarAsientos();
        mostrarBienvenida();
        
        boolean continuar = true;
        while (continuar) {
            try {
                mostrarMenuPrincipal();
                int opcion = leerEntero("Seleccione una opcion: ");
                
                switch (opcion) {
                    case 1:
                        registrarVenta();
                        break;
                    case 2:
                        mostrarVentas();
                        break;
                    case 3:
                        buscarVenta();
                        break;
                    case 4:
                        modificarVenta();
                        break;
                    case 5:
                        eliminarVenta();
                        break;
                    case 6:
                        mostrarEstadisticas();
                        break;
                    case 7:
                        continuar = false;
                        System.out.println("\nGracias por usar el sistema del Teatro Moro!");
                        break;
                    default:
                        System.out.println("\n Opcion invalida, intente nuevamente.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine(); 
            }
        }
        scanner.close();
    }
    
    //funciones de inicializacion
    
    private static void inicializarAsientos() {
        for (int i = 0; i < asientosOcupados.length; i++) {
            for (int j = 0; j < asientosOcupados[i].length; j++) {
                asientosOcupados[i][j] = false;
            }
        }
    }
    
    private static void mostrarBienvenida() {
        System.out.println("\nBienvenido al sistema de ventas del Teatro Moro");
    }
    
    //menu principal
    
    private static void mostrarMenuPrincipal() {
        System.out.println("===============================");
        System.out.println("         Menu Principal                    ");
        System.out.println("===============================");
        System.out.println(" 1. Registrar nueva venta");
        System.out.println(" 2. Ver todas las ventas");
        System.out.println(" 3. Buscar venta especifica");
        System.out.println(" 4. Modificar venta");
        System.out.println(" 5. Cancelar venta");
        System.out.println(" 6. Ver estadisticas");
        System.out.println(" 7. Salir");

    }
    
    //validacion de datos
    
    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un numero entero.");
            }
        }
    }
    
    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        String texto = scanner.nextLine().trim();
        while (texto.isEmpty()) {
            System.out.print("El campo no puede estar vacio. " + mensaje);
            texto = scanner.nextLine().trim();
        }
        return texto;
    }
    
    private static boolean validarEdad(int edad) {
        return edad > 0 && edad <= 120;
    }
    
    //funciones para ordenar asientos
    
    private static void mostrarAsientosDisponibles(int ubicacion) {
        String nombreUbicacion = obtenerNombreUbicacion(ubicacion);
        int maxAsientos = obtenerMaxAsientos(ubicacion);
        
        System.out.println("=================================");
        System.out.println("  Mapa de Asientos - " + nombreUbicacion.toUpperCase());
        System.out.println("=================================");
        
        for (int i = 0; i < maxAsientos; i++) {
            if (i % 5 == 0 && i != 0) {
                System.out.println();
            }
            
            String estado = asientosOcupados[ubicacion][i] ? "[X]" : "[" + (i + 1) + "]";
            System.out.print(String.format("%-6s", estado));
        }
        
        System.out.println("\n[X] = Ocupado  |  [Numero] = Disponible");
    }
    
    private static int obtenerMaxAsientos(int ubicacion) {
        switch (ubicacion) {
            case 0: return 20; //platea
            case 1: return 15; //palco
            case 2: return 30; //galeria
            default: return 0;
        }
    }
    
    private static String obtenerNombreUbicacion(int ubicacion) {
        switch (ubicacion) {
            case 0: return "Platea";
            case 1: return "Palco";
            case 2: return "Galeria";
            default: return "Desconocida";
        }
    }
    
    private static double obtenerPrecioBase(int ubicacion) {
        switch (ubicacion) {
            case 0: return PRECIO_PLATEA;
            case 1: return PRECIO_PALCO;
            case 2: return PRECIO_GALERIA;
            default: return 0;
        }
    }
    
    private static boolean verificarAsientoDisponible(int ubicacion, int numeroAsiento) {
        int maxAsientos = obtenerMaxAsientos(ubicacion);
        if (numeroAsiento < 1 || numeroAsiento > maxAsientos) {
            return false;
        }
        return !asientosOcupados[ubicacion][numeroAsiento - 1];
    }
    
    private static void ocuparAsiento(int ubicacion, int numeroAsiento) {
        asientosOcupados[ubicacion][numeroAsiento - 1] = true;
    }
    
    private static void liberarAsiento(int ubicacion, int numeroAsiento) {
        asientosOcupados[ubicacion][numeroAsiento - 1] = false;
    }
    
    //funciones para descuentos
    
    private static int calcularDescuento(int edad) {
        if (edad < 5) {
            return 100; //entrada gratuita para bebes y ninos de hasta 5 
        } else if (edad >= 5 && edad <= 12) {
            return 50; //entrada con 50% de descuento para ninos hasta 12
        } else if (edad >= 65) {
            return 30; // 30% de descuento tercera edad
        } else {
            return 0; // Sin descuento
        }
    }
    
    private static double calcularPrecioFinal(double precioBase, int descuento) {
        return precioBase * (1 - descuento / 100.0);
    }
    
    //funciones crud
    
    private static void registrarVenta() { //crear
        System.out.println("=================================");
        System.out.println("       Registrar Nueva Venta            ");
        System.out.println("=================================");
        
        try {
            //solicitar datos del cliente
            String nombre = leerTexto("\nNombre del cliente: ");
            int edad = leerEntero("Edad del cliente: ");
            
            if (!validarEdad(edad)) {
                System.out.println("Edad invalida. Debe estar entre 1 y 120 años.");
                return;
            }
            
            //mostrar ubicaciones disponibles
            System.out.println("=================================");
            System.out.println("      Ubicaciones y Precios                ");
            System.out.println("=================================");
            System.out.println("      1. Platea   - $" + String.format("%.0f", PRECIO_PLATEA));
            System.out.println("      2. Palco    - $" + String.format("%.0f", PRECIO_PALCO));
            System.out.println("      3. Galeria  - $" + String.format("%.0f", PRECIO_GALERIA));
            System.out.println("=================================");
            
            int ubicacionOpcion = leerEntero("\nSeleccione ubicacion (1-3): ");
            
            if (ubicacionOpcion < 1 || ubicacionOpcion > 3) {
                System.out.println("Ubicacion invalida.");
                return;
            }
            
            int ubicacion = ubicacionOpcion - 1;
            String nombreUbicacion = obtenerNombreUbicacion(ubicacion);
            
            //mostrar asientos disponibles
            mostrarAsientosDisponibles(ubicacion);
            
            int numeroAsiento = leerEntero("\nSeleccione numero de asiento: ");
            
            if (!verificarAsientoDisponible(ubicacion, numeroAsiento)) {
                System.out.println("El asiento no esta disponible o no existe.");
                return;
            }
            
            //calcular precio y descuento
            double precioBase = obtenerPrecioBase(ubicacion);
            int descuento = calcularDescuento(edad);
            double precioFinal = calcularPrecioFinal(precioBase, descuento);
            
            //registrar la venta
            idsVenta.add(contadorVentas);
            nombresClientes.add(nombre);
            edadesClientes.add(edad);
            ubicaciones.add(nombreUbicacion);
            asientos.add(nombreUbicacion.substring(0, 1) + numeroAsiento);
            preciosFinales.add(precioFinal);
            descuentos.add(descuento);
            
            ocuparAsiento(ubicacion, numeroAsiento);
            
            //mostrar boleta
           imprimirBoleta(idsVenta.size() - 1); 
            
            contadorVentas++;
            
        } catch (Exception e) {
            System.out.println("Error al registrar la venta: " + e.getMessage());
        }
    }
    
    private static void mostrarVentas() { //leer
        if (idsVenta.isEmpty()) {
            System.out.println("\n===============================");
            System.out.println("   No hay ventas registradas.");
            return;
        }
        
        System.out.println("============================================");
        System.out.println("                     Lista de Ventas                                ");
        System.out.println("============================================");
        
        for (int i = 0; i < idsVenta.size(); i++) {
            System.out.printf("ID: %-3d | Cliente: %-30s - %n", idsVenta.get(i), nombresClientes.get(i));
            System.out.printf("Asiento: %-6s | Precio: $%.0f | Descuento: %d%% - %n", 
                asientos.get(i), preciosFinales.get(i), descuentos.get(i));
            System.out.println("============================================");
        }
        
        System.out.println("Total de ventas: " + idsVenta.size());
    }
    
    private static void buscarVenta() {
        if (idsVenta.isEmpty()) {
            System.out.println("\n===============================");
            System.out.println("   No hay ventas registradas.");
            return;
        }
        
        int id = leerEntero("Ingrese el ID de la venta a buscar: ");
        int indice = buscarIndicePorId(id);
        
        if (indice == -1) {
            System.out.println("\n===============================");
            System.out.println("No se encontro una venta con ese ID.");
            return;
        }
        
        imprimirBoleta(indice);
    }
    
    private static void modificarVenta() { //actualizar
        if (idsVenta.isEmpty()) {
            System.out.println("\n===============================");
            System.out.println("   No hay ventas registradas.");
            return;
        }
        
        int id = leerEntero("Ingrese el ID de la venta a modificar: ");
        int indice = buscarIndicePorId(id);
        
        if (indice == -1) {
            System.out.println("\n===============================");
            System.out.println("No se encontro una venta con ese ID.");
            return;
        }
        
        System.out.println("\nQue desea modificar?");
        System.out.println("1. Nombre del cliente");
        System.out.println("2. Edad del cliente");
        
        int opcion = leerEntero("Seleccione opcion: ");
        
        switch (opcion) {
            case 1:
                String nuevoNombre = leerTexto("Ingrese el nuevo nombre: ");
                nombresClientes.set(indice, nuevoNombre);
                System.out.println("Nombre actualizado correctamente.");
                break;
            case 2:
                int nuevaEdad = leerEntero("Ingrese la nueva edad: ");
                if (!validarEdad(nuevaEdad)) {
                    System.out.println("Edad invalida.");
                    return;
                }
                edadesClientes.set(indice, nuevaEdad);
                
                //recalcular descuento y precio
                String ubicacionStr = ubicaciones.get(indice);
                int ubicacion = obtenerUbicacionPorNombre(ubicacionStr);
                double precioBase = obtenerPrecioBase(ubicacion);
                int nuevoDescuento = calcularDescuento(nuevaEdad);
                double nuevoPrecio = calcularPrecioFinal(precioBase, nuevoDescuento);
                
                descuentos.set(indice, nuevoDescuento);
                preciosFinales.set(indice, nuevoPrecio);
                
                System.out.println("Edad actualizada y precio recalculado correctamente.");
                break;
            default:
                System.out.println("Opcion invalida.");
        }
    }
    
    private static void eliminarVenta() { //cancelar
        if (idsVenta.isEmpty()) {
            System.out.println("\n===============================");
            System.out.println("   No hay ventas registradas.");
            return;
        }
        
        int id = leerEntero("Ingrese el ID de la venta a cancelar: ");
        int indice = buscarIndicePorId(id);
        
        if (indice == -1) {
            System.out.println("\n===============================");
            System.out.println("   No se encontro una venta con ese ID.");
            return;
        }
        
        //liberar el asiento
        String ubicacionStr = ubicaciones.get(indice);
        int ubicacion = obtenerUbicacionPorNombre(ubicacionStr);
        String asiento = asientos.get(indice);
        int numeroAsiento = Integer.parseInt(asiento.substring(1));
        liberarAsiento(ubicacion, numeroAsiento);
        
        //eliminar de todos los arreglos paralelos
        idsVenta.remove(indice);
        nombresClientes.remove(indice);
        edadesClientes.remove(indice);
        ubicaciones.remove(indice);
        asientos.remove(indice);
        preciosFinales.remove(indice);
        descuentos.remove(indice);
        System.out.println("\n===============================");
        System.out.println("Venta cancelada correctamente.");
    }
    
    //funciones extra como busqueda de ventas, imprimir boletas y otros
    
    private static int buscarIndicePorId(int id) {
        for (int i = 0; i < idsVenta.size(); i++) {
            if (idsVenta.get(i) == id) {
                return i;
            }
        }
        return -1;
    }
    
    private static int obtenerUbicacionPorNombre(String nombre) {
        switch (nombre) {
            case "Platea": return 0;
            case "Palco": return 1;
            case "Galeria": return 2;
            default: return -1;
        }
    }
    
    private static void imprimirBoleta(int indice) {
        System.out.println("\n============================================");
        System.out.println("           Teatro Moro - Boleta                     ");
        System.out.println("============================================");
        System.out.printf("- ID de Venta: %-45d  %n", idsVenta.get(indice));
        System.out.printf("- Cliente: %-45s  %n", nombresClientes.get(indice));
        System.out.printf("- Edad: %-45d  %n", edadesClientes.get(indice));
        System.out.printf("- Ubicacion: %-45s  %n", ubicaciones.get(indice));
        System.out.printf("- Asiento: %-45s  %n", asientos.get(indice));
        System.out.println("============================================");
        
        String ubicacionStr = ubicaciones.get(indice);
        int ubicacion = obtenerUbicacionPorNombre(ubicacionStr);
        double precioBase = obtenerPrecioBase(ubicacion);
        
        System.out.printf("- Precio Base: $%.0f%n", precioBase);
        System.out.printf("- Descuento: %d%% %-40s  %n", descuentos.get(indice), "");
        System.out.printf("- Total a Pagar: $%.0f%n", preciosFinales.get(indice));
        System.out.println("============================================");
        
        if (descuentos.get(indice) > 0) {
            System.out.println("\n Descuento aplicado por edad");
        }
    }
     
    //estadisticas
    private static void mostrarEstadisticas() {
        if (idsVenta.isEmpty()) {
            System.out.println("\n===============================");
            System.out.println("   No hay ventas registradas."); 
            return;
        }
        
        double totalRecaudado = 0;
        int totalDescuentos = 0;
        int plateaVendidos = 0, palcoVendidos = 0, galeriaVendidos = 0;
        
        for (int i = 0; i < idsVenta.size(); i++) {
            totalRecaudado += preciosFinales.get(i);
            if (descuentos.get(i) > 0) {
                totalDescuentos++;
            }
            
            String ubicacion = ubicaciones.get(i);
            switch (ubicacion) {
                case "Platea": plateaVendidos++; break;
                case "Palco": palcoVendidos++; break;
                case "Galeria": galeriaVendidos++; break;
            }
        }
        
        System.out.println("\n============================================");
        System.out.println("         Estadisticas Del Sistema                  ");
        System.out.println("============================================");
        System.out.printf("- Total de ventas: %-35d%n", idsVenta.size());
        System.out.printf("- Total recaudado: $%-35.0f%n", totalRecaudado);  //.0f para que no aparezcan decimales innecesarios
        System.out.printf("- Ventas con descuento: %-35d%n", totalDescuentos);
        System.out.println("============================================");
        System.out.printf("- Asientos vendidos en Platea: %-25d%n", plateaVendidos);
        System.out.printf("- Asientos vendidos en Palco: %-25d%n", palcoVendidos);
        System.out.printf("- Asientos vendidos en Galeria: %-25d%n", galeriaVendidos);
        System.out.println("============================================");
    }
}