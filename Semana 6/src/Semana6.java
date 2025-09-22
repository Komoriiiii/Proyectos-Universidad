import java.util.*;

public class Semana6 {

    //variables estaticas
    static String nombreTeatro = "Teatro Moro";
    static int capacidad = 100;
    static String[] asientos = new String[capacidad]; //estados libre, reservado o vendido
    static long[] tiemposReserva = new long[capacidad]; //tiempo de inicio de reserva
    static int totalDisponibles = capacidad;
    static int totalReservados = 0;
    static int totalVendidos = 0;
    static int precioEntrada = 10000;

    //variable para guardar la ultima compra
    static List<Integer> ultimaCompra = new ArrayList<>();

    //reserva de asiento con tiempo limite
    public static void reservarAsiento(Scanner kb) {
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

    //comprar entradas
    public static void comprarEntradas(Scanner kb) {
        System.out.println("Ingrese asientos a comprar separados por coma (ejemplo: 1,3,5)");
        String linea = kb.nextLine();
        String[] partes = linea.split(",");
        List<Integer> asientosComprados = new ArrayList<>();
        for (String p : partes) {
            int asiento = Integer.parseInt(p.trim());
            if (asiento < 1 || asiento > capacidad) {
                System.out.println("Asiento invalido: " + asiento);
                continue;
            }
            if (asientos[asiento - 1].equals("Libre") || asientos[asiento - 1].equals("Reservado")) {
                if (asientos[asiento - 1].equals("Reservado")) {
                    totalReservados--;
                } else {
                    totalDisponibles--;
                }
                asientos[asiento - 1] = "Vendido";
                totalVendidos++;
                asientosComprados.add(asiento);
                System.out.println("Asiento " + asiento + " comprado");
            } else {
                System.out.println("Asiento " + asiento + " no disponible");
            }
        }
        if (!asientosComprados.isEmpty()) {
            ultimaCompra = new ArrayList<>(asientosComprados); //guardamos la ultima compra
            imprimirBoleta(ultimaCompra);
        }
    }

    //imprimir boleta con 3 puntos de depuracion
    public static void imprimirBoleta(List<Integer> asientosComprados) {
        System.out.println("Boleta teatro " + nombreTeatro);
        System.out.println("Entradas compradas: " + asientosComprados.size());
        System.out.println("Asientos: " + asientosComprados);
        int total = asientosComprados.size() * precioEntrada;
        System.out.println("Total a pagar: $" + total);
        // depuracion
        System.out.println("debug: total vendidos = " + totalVendidos);
        System.out.println("debug: total reservados = " + totalReservados);
        System.out.println("debug: total disponibles = " + totalDisponibles);
    }

    //imprimir la ultima boleta desde el menu
    public static void imprimirUltimaBoleta() {
        if (ultimaCompra.isEmpty()) {
            System.out.println("No hay compras registradas para imprimir boleta");
        } else {
            System.out.println("Mostrando ultima boleta realizada:");
            imprimirBoleta(ultimaCompra);
        }
    }

    //revisar expiracion de reservas
    public static void revisarReservas() {
        long ahora = System.currentTimeMillis();
        for (int i = 0; i < capacidad; i++) {
            if (asientos[i].equals("Reservado")) {
                if (ahora - tiemposReserva[i] > 60000) { //1 minuto de tiempo limite
                    asientos[i] = "Libre";
                    totalReservados--;
                    totalDisponibles++;
                    System.out.println("Reserva en asiento " + (i + 1) + " expirada");
                }
            }
        }
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
            System.out.println("4. Imprimir boleta");
            System.out.println("5. Salir");
            int opcion = kb.nextInt();
            kb.nextLine();
            switch (opcion) {
                case 1: reservarAsiento(kb); break;
                case 2: modificarReserva(kb); break;
                case 3: comprarEntradas(kb); break;
                case 4: imprimirUltimaBoleta(); break;
                case 5: continuar = false; break;
                default: System.out.println("Opcion invalida");
            }
        }
        kb.close();
    }
}
