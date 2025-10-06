import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Venta {
    String ubicacion;
    int asiento;
    double precioBase;
    double descuento;
    double precioFinal;

    public Venta(String ubicacion, int asiento, double precioBase, double descuento, double precioFinal) {
        this.ubicacion = ubicacion;
        this.asiento = asiento;
        this.precioBase = precioBase;
        this.descuento = descuento;
        this.precioFinal = precioFinal;
    }

    @Override  //uso de override para que solo salga la informacion que el usuario quiere ver y no un codigo ilegible xd
    public String toString() {
        return "Asiento: " + asiento + " | Ubicacion: " + ubicacion +
               " | Base: $" + precioBase + " | Desc: $" + descuento +
               " | Final: $" + precioFinal;
    }
}
public class Semana7 {

    //variables estaticas
    static String nombreTeatro = "Teatro moro";
    static int capacidad = 100;
    static String[] asientos = new String[capacidad]; //estados: libre, reservado, vendido
    static long[] tiemposReserva = new long[capacidad]; //tiempo de inicio de reserva
    static int totalDisponibles = capacidad;
    static int totalReservados = 0;
    static int totalVendidos = 0;
    static double totalIngresos = 0;

    //precios por ubicacion
    static int precioVIP = 20000;
    static int precioPlatea = 15000;
    static int precioBalcon = 10000;

    //variable para guardar la ultima compra
    static List<Integer> ultimaCompra = new ArrayList<>();

    //lista de ventas
    static List<Venta> ventas = new ArrayList<>();

    //arreglo para ventas
    static Venta[] ventasArray = new Venta[capacidad]; //guarda cada venta en la posicion del asiento

    //modo debug oculto
    static boolean modoDebug = false;

    //muestra el estado actual de los asientos
    public static void mostrarAsientos() {
        System.out.println("Estado actual de los asientos:");
        for (int i = 0; i < capacidad; i++) {
            System.out.printf("%3d:%-9s ", i + 1, asientos[i]);  //%d para numero entero, 3 para el ancho minimo de los caracteres, despues %s para string, 9 para 9 caracteres y el - para que quede a la izquierda luego el i+1 para el numero del asiento y finalmente el estado del asiento en asientos [i]
            if ((i + 1) % 10 == 0) System.out.println();  //salto de linea cada 10 asientos para tener ordenado
        }
        System.out.println();
    }

    //reserva de asiento con tiempo limite
    public static void reservarAsiento(Scanner kb) {
        mostrarAsientos(); 
        System.out.println("Ingrese numero de asiento a reservar (1-" + capacidad + ")");
        int asiento = kb.nextInt();
        kb.nextLine();
        if (asiento < 1 || asiento > capacidad) {
            System.out.println("Asiento invalido");
            return;
        }
        if (asientos[asiento - 1].equals("Libre")) {
            asientos[asiento - 1] = "Reservado";
            tiemposReserva[asiento - 1] = System.currentTimeMillis();
            totalDisponibles--;
            totalReservados++;
            System.out.println("Reserva exitosa en asiento " + asiento);
        } else {
            System.out.println("Asiento no disponible");
        }
    }

    //modificar una reserva
    public static void modificarReserva(Scanner kb) {
        System.out.println("Ingrese numero de asiento reservado a modificar");
        int asientoViejo = kb.nextInt();
        kb.nextLine();
        if (asientoViejo < 1 || asientoViejo > capacidad) {
            System.out.println("Asiento invalido");
            return;
        }
        if (asientos[asientoViejo - 1].equals("Reservado")) {
            asientos[asientoViejo - 1] = "Libre";
            totalReservados--;
            totalDisponibles++;
            System.out.println("Ingrese nuevo numero de asiento");
            int asientoNuevo = kb.nextInt();
            kb.nextLine();
            if (asientoNuevo >= 1 && asientoNuevo <= capacidad && asientos[asientoNuevo - 1].equals("Libre")) {
                asientos[asientoNuevo - 1] = "Reservado";
                tiemposReserva[asientoNuevo - 1] = System.currentTimeMillis();
                totalReservados++;
                totalDisponibles--;
                System.out.println("Reserva modificada a asiento " + asientoNuevo);
            } else {
                System.out.println("Nuevo asiento no disponible");
            }
        } else {
            System.out.println("No existe reserva en ese asiento");
        }
    }

    //comprar entradas con ubicacion y descuentos
    public static void comprarEntradas(Scanner kb) {
        mostrarAsientos(); 
        System.out.println("Ingrese asientos a comprar separados por coma (Ejemplo: 1,3,5)");
        String linea = kb.nextLine();
        String[] partes = linea.split(",");
        List<Integer> asientosComprados = new ArrayList<>();
        for (String p : partes) {
            int asiento = Integer.parseInt(p.trim());
            if (asiento < 1 || asiento > capacidad) {
                System.out.println("Asiento invalido: " + asiento);
                continue;
            }

            //validacion del asiento vendido o reservado
            if (asientos[asiento - 1].equals("Vendido") || asientos[asiento - 1].equals("Reservado")) {
            System.out.println("Asiento " + asiento + " ya esta reservado o vendido.");
            continue; 
            }

            //seleccion de ubicacion
            System.out.println("Seleccione ubicacion para asiento " +  asiento + ": 1. Vip " + "  2. Platea "  + "  3. Balcon");
            int opcionUbicacion = kb.nextInt();
            kb.nextLine();

            String ubicacion = "";
            double precioBase = 0;
            switch (opcionUbicacion) {
                case 1: ubicacion = "Vip"; precioBase = precioVIP; break;
                case 2: ubicacion = "Platea"; precioBase = precioPlatea; break;
                case 3: ubicacion = "Balcon"; precioBase = precioBalcon; break;
                default: System.out.println("Opcion invalida"); continue;
            }

            //aplicar descuento
            System.out.println("Es Estudiante (1) o Tercera edad (2)? (Otro numero = Ninguno)");
            int tipoCliente = kb.nextInt();
            kb.nextLine();

            double descuento = 0;
            if (tipoCliente == 1) descuento = precioBase * 0.10; //descuento estudiante
            else if (tipoCliente == 2) descuento = precioBase * 0.15; //descuento tercera edad

            double precioFinal = precioBase - descuento;

            if (asientos[asiento - 1].equals("Reservado")) {
                totalReservados--;
            } else {
                totalDisponibles--;
            }
            asientos[asiento - 1] = "Vendido";
            totalVendidos++;
            totalIngresos += precioFinal;
            asientosComprados.add(asiento); 

            Venta v = new Venta(ubicacion, asiento, precioBase, descuento, precioFinal);
            ventas.add(v);

            //guardar en el arreglo
            ventasArray[asiento - 1] = v;

            System.out.println("Asiento " + asiento + " comprado");
            imprimirBoleta(v);
        }
        if (!asientosComprados.isEmpty()) {
            ultimaCompra = new ArrayList<>(asientosComprados); 
        }
    }

    //imprimir boleta 
    public static void imprimirBoleta(Venta v) {
        System.out.println("==========================================");
        System.out.println("               Teatro Moro");
        System.out.println("==========================================");
        System.out.println("Asiento: " + v.asiento);
        System.out.println("Ubicacion: " + v.ubicacion);
        System.out.println("Precio base: $" + v.precioBase);
        System.out.println("Descuento aplicado: $" + v.descuento);
        System.out.println("Total a pagar: $" + v.precioFinal);
        System.out.println("Gracias por su compra y disfrute la funcion!");
        System.out.println("==========================================");

        if (modoDebug) debugEstado();
    }

    //resumen de ventas
    public static void mostrarResumen() {
        if (ventas.isEmpty()) {
            System.out.println("No hay ventas registradas");
            return;
        }
        System.out.println("Resumen de ventas:");
        for (Venta v : ventas) {
            System.out.println(v);
        }
        System.out.println("Total entradas vendidas: " + totalVendidos);
        System.out.println("Total ingresos: $" + totalIngresos);
    }

    //imprimir la ultima boleta desde el menu
    public static void imprimirUltimaBoleta() {
        if (ventas.isEmpty()) {
            System.out.println("No hay compras registradas para imprimir boleta");
        } else {
            Venta ultima = ventas.get(ventas.size() - 1);
            System.out.println("Mostrando ultima boleta realizada:");
            imprimirBoleta(ultima);
        }
    }

    //revisar expiracion de reservas
    public static void revisarReservas() {
        long ahora = System.currentTimeMillis();
        for (int i = 0; i < capacidad; i++) {
            if (asientos[i].equals("Reservado")) {
                if (ahora - tiemposReserva[i] > 60000) {  //1 minuto de tiempo limite
                    asientos[i] = "Libre";
                    totalReservados--;
                    totalDisponibles++;
                    System.out.println("Reserva en asiento " + (i + 1) + " expirada");
                }
            }
        }
    }

    //metodo de debug
    private static void debugEstado() {
        System.out.println("Debug: Total vendidos = " + totalVendidos +
                           ", Reservados = " + totalReservados +
                           ", Disponibles = " + totalDisponibles);
    }

    //menu principal
    public static void main(String[] args) {
        for (int i = 0; i < capacidad; i++) asientos[i] = "Libre";
        Scanner kb = new Scanner(System.in);
        boolean continuar = true;
        while (continuar) {
            revisarReservas();
            System.out.println("Menu principal");
            System.out.println("1. Reservar asiento");
            System.out.println("2. Modificar reserva");
            System.out.println("3. Comprar entradas");
            System.out.println("4. Imprimir ultima boleta");
            System.out.println("5. Resumen de ventas");
            System.out.println("6. Salir");
            int opcion = kb.nextInt();
            kb.nextLine();
            //opcion oculta de debug, se activa al usar 999 en el menu principal y luego de activarse, al usar la opcion 4 para imprimir boleta mostrara el total de entradas vendidas, reservadas y disponibles
            if (opcion == 999) {
                modoDebug = !modoDebug;
                System.out.println("Modo debug " + (modoDebug ? "Activado" : "Desactivado"));
                continue;
            }

            switch (opcion) {
                case 1: reservarAsiento(kb); break;
                case 2: modificarReserva(kb); break;
                case 3: comprarEntradas(kb); break;
                case 4: imprimirUltimaBoleta(); break;
                case 5: mostrarResumen(); break;
                case 6: System.out.println("Muchas gracias por usar el sistema de ventas del Teatro Moro, que tenga un buen dia.");
                    continuar = false; break;
                default: System.out.println("Opcion invalida");
            }
        }
        kb.close();
    }
}
