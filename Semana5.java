import java.util.Scanner;

public class Semana5 {

    // variables estaticas
    static int totalEntradasVendidas = 0;
    static double totalIngresos = 0;
    static int totalEstudiantes = 0;
    static int totalTerceraEdad = 0;

    // metodo para agregar entradas al carrito
    public static void agregarEntrada(String tipoDesc, String lugar, int precioBase,
                                      String[] carrito, int[] numerosEntrada, String[] ubicaciones,
                                      String[] tiposDescuento, double[] preciosFinales, int cantidad) {
        double descuento = 0;
        if (tipoDesc.equals("Estudiante")) {
            descuento = 0.10;
        } else if (tipoDesc.equals("Tercera Edad")) {
            descuento = 0.15;
        }

        double precioFinal = precioBase - (precioBase * descuento);

        numerosEntrada[cantidad] = cantidad + 1;
        ubicaciones[cantidad] = lugar.toUpperCase();
        tiposDescuento[cantidad] = tipoDesc;
        preciosFinales[cantidad] = precioFinal;

        carrito[cantidad] = lugar.toUpperCase() + " $" + precioFinal + " " + tipoDesc;

        System.out.println(" Entrada anadida al carrito por $" + precioFinal);

        // actualizar estadisticas globales
        totalEntradasVendidas++;
        totalIngresos += precioFinal;
        if (tipoDesc.equals("Estudiante")) {
            totalEstudiantes++;
        } else if (tipoDesc.equals("Tercera Edad")) {
            totalTerceraEdad++;
        }
    }

    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);

        String opcionMenu;
        boolean continuar = true;

        int[] numerosEntrada = new int[300];
        String[] ubicaciones = new String[300];
        String[] tiposDescuento = new String[300];
        double[] preciosFinales = new double[300];
        int cantidad = 0;
        String[] carrito = new String[300];

        int palco = 20000;
        int platea = 15000;
        int general = 10000;

        System.out.println("Bienvenido al sistema de venta del teatro moro");

        while (continuar) {
            System.out.println("\n Menu Principal");
            System.out.println("\n 1.- Comprar entradas");
            System.out.println("\n 2.- Informacion Promociones");
            System.out.println("\n 3.- Ver o editar carrito");
            System.out.println("\n 4.- Busqueda de entradas por zona");
            System.out.println("\n 5.- Salir");
            System.out.println("\n Seleccione una opcion");
            opcionMenu = kb.nextLine();
            //1. comprar entradas
            if (opcionMenu.equals("1")) {
                boolean comprarMas = true;
                while (comprarMas) {
                    String lugar = "";
                    int precioBase = 0;
                    System.out.println("\n Localizacion entrada");
                    System.out.println("Palco: $" + palco);
                    System.out.println("Platea: $" + platea);
                    System.out.println("General: $" + general);
                    //1.1 definir en que zona se compran las entradas
                    System.out.println("Ingrese que tipo de entrada desea (Palco, Platea o General)");
                    lugar = kb.nextLine().toUpperCase();

                    if (lugar.equalsIgnoreCase("Palco")) {
                        precioBase = palco;
                    } else if (lugar.equalsIgnoreCase("Platea")) {
                        precioBase = platea;
                    } else if (lugar.equalsIgnoreCase("General")) {
                        precioBase = general;
                    } else {
                        System.out.println("Zona invalida, intente nuevamente");
                        continue;
                    }
                    //1.2 ver cuantas entradas seran con descuento del total solicitado
                    System.out.print("Cuantas entradas con descuento de estudiante?: ");
                    int est = kb.nextInt();
                    System.out.print("Cuantas entradas con descuento de tercera edad?: ");
                    int tercera = kb.nextInt();
                    System.out.print("Cuantas entradas sin descuento?: ");
                    int sinDescuento = kb.nextInt();
                    kb.nextLine();
                    //1.3 registro de entradas en el carrito aplicando descuento y sumandose a estadisticas totales.
                    for (int i = 0; i < est; i++)
                        agregarEntrada("Estudiante", lugar, precioBase, carrito, numerosEntrada, ubicaciones, tiposDescuento, preciosFinales, cantidad++);
                    for (int i = 0; i < tercera; i++)
                        agregarEntrada("Tercera Edad", lugar, precioBase, carrito, numerosEntrada, ubicaciones, tiposDescuento, preciosFinales, cantidad++);
                    for (int i = 0; i < sinDescuento; i++)
                        agregarEntrada("Ninguno", lugar, precioBase, carrito, numerosEntrada, ubicaciones, tiposDescuento, preciosFinales, cantidad++);

                    System.out.print("Desea comprar entradas en otra zona? (si/no): ");
                    String resp = kb.nextLine();
                    if (!resp.equalsIgnoreCase("si")) {
                        comprarMas = false;
                    }
                }
                //2. informacion de promociones disponibles
            } else if (opcionMenu.equals("2")) {
                System.out.println("\n Promociones Disponibles");
                System.out.println("Estudiantes: 10% descuento");
                System.out.println("Tercera Edad: 15% descuento");
                System.out.println("Compra de 3 entradas o mas: 5% adicional");
                System.out.println("- Presione enter para volver al menu principal");
                kb.nextLine();
                //3. ver y editar carrito
            } else if (opcionMenu.equals("3")) {
                if (cantidad == 0) {
                    System.out.println("El carrito esta vacio");
                    System.out.println("- Presione enter para volver al menu principal");
                    kb.nextLine();
                } else {
                    double subtotal = 0;
                    System.out.println("\nCarrito");
                    for (int i = 0; i < cantidad; i++) {
                        System.out.println((i + 1) + ". " + carrito[i]);
                        subtotal += preciosFinales[i];
                    }
                    System.out.println("Subtotal de la compra: $" + subtotal);
                    //3.1 eliminar entradas y elminar ingresos de estadisticas totales
                    System.out.print("Desea eliminar alguna entrada? (s/n/si/no): ");
                    String eliminar = kb.nextLine();
                    if (eliminar.equalsIgnoreCase("s") || eliminar.equalsIgnoreCase("si")) {
                        System.out.print("Ingrese el numero de la entrada a eliminar: ");
                        int num = kb.nextInt();
                        kb.nextLine();
                        if (num > 0 && num <= cantidad) {
                            totalEntradasVendidas--;
                            totalIngresos -= preciosFinales[num - 1];
                            if (tiposDescuento[num - 1].equalsIgnoreCase("Estudiante"))
                                totalEstudiantes--;
                            else if (tiposDescuento[num - 1].equalsIgnoreCase("Tercera Edad"))
                                totalTerceraEdad--; //entradas eliminadas exitosamente de las estadisticas globales

                            for (int i = num - 1; i < cantidad - 1; i++) {
                                carrito[i] = carrito[i + 1];
                                numerosEntrada[i] = numerosEntrada[i + 1];
                                ubicaciones[i] = ubicaciones[i + 1];
                                tiposDescuento[i] = tiposDescuento[i + 1];
                                preciosFinales[i] = preciosFinales[i + 1];
                            }
                            carrito[cantidad - 1] = null;
                            numerosEntrada[cantidad - 1] = 0;
                            ubicaciones[cantidad - 1] = null;
                            tiposDescuento[cantidad - 1] = null;
                            preciosFinales[cantidad - 1] = 0;
                            cantidad--;
                            System.out.println("Entrada eliminada correctamente");
                        } else {
                            System.out.println("Numero invalido");
                        }
                    }
                    System.out.println("- Presione Enter para volver al menu principal");
                    kb.nextLine();
                }
                //4. ver cuantas entradas quedan disponibles por sector
            } else if (opcionMenu.equals("4")) {
                int totalPalco = 100;
                int totalPlatea = 100;
                int totalGeneral = 100;

                int vendidasPalco = 0;
                int vendidasPlatea = 0;
                int vendidasGeneral = 0;

                for (int i = 0; i < cantidad; i++) {
                    if (ubicaciones[i].equalsIgnoreCase("Palco")) vendidasPalco++;
                    else if (ubicaciones[i].equalsIgnoreCase("Platea")) vendidasPlatea++;
                    else if (ubicaciones[i].equalsIgnoreCase("General")) vendidasGeneral++;
                }

                System.out.println("\nBuscador de entradas por lugar:");
                System.out.println("Palco: " + (totalPalco - vendidasPalco) + " disponibles de " + totalPalco);
                System.out.println("Platea: " + (totalPlatea - vendidasPlatea) + " disponibles de " + totalPlatea);
                System.out.println("General: " + (totalGeneral - vendidasGeneral) + " disponibles de " + totalGeneral);
                System.out.println("- Presione enter para volver al menu principal");
                kb.nextLine();
                //5. salir y mostrar estadisticas totales de la venta
            } else if (opcionMenu.equals("5")) {
                continuar = false;
                System.out.println("Gracias por usar el sistema del Teatro Moro");
            } else {
                System.out.println("Opcion invalida, intente nuevamente");
            }
        }
            //5.1 estadisticas totales
        System.out.println("\nEstadisticas Totales");
        System.out.println("Total Entradas Vendidas: " + totalEntradasVendidas);
        System.out.println("Total Ingresos: $" + totalIngresos);
        System.out.println("Total Estudiantes: " + totalEstudiantes);
        System.out.println("Total Tercera Edad: " + totalTerceraEdad);
    }
}

