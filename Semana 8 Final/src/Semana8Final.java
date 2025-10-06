import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Semana8Final {
    // limites y estructuras
    static final int MAX_EVENTS = 10; //maximo posible de eventos registrados en el sistema
    static final int MAX_CLIENTS = 1000; //maximo posible de clientes para el sistema
    static final int MAX_VENTAS = 5000; //maximo posible de ventas con el sistema
    static final int DEFAULT_CAPACITY = 100; //capacidad de teatro
    static final long RESERVA_TIMEOUT_MS = 60000; // 1 minuto para que expire una reserva

    // eventos
    static int eventosCount = 0;
    static int[] eventoId = new int[MAX_EVENTS];
    static String[] eventoNombre = new String[MAX_EVENTS];
    static int[] eventoCapacidad = new int[MAX_EVENTS];
    static String[][] eventoAsientoEstado = new String[MAX_EVENTS][DEFAULT_CAPACITY]; // estados libres, vendidos o reservados de los asientos
    static long[][] eventoTiempoReserva = new long[MAX_EVENTS][DEFAULT_CAPACITY];
    static int[][] eventoVentaIdPorAsiento = new int[MAX_EVENTS][DEFAULT_CAPACITY]; // 0 = sin venta

    // arreglo para clientes
    static int clientesCount = 0;
    static int[] clienteId = new int[MAX_CLIENTS];
    static String[] clienteNombre = new String[MAX_CLIENTS];
    static String[] clienteTelefono = new String[MAX_CLIENTS];
    static String[] clienteEmail = new String[MAX_CLIENTS];
    static int nextClienteId = 1;

    // promociones
    static List<String> promoCode = new ArrayList<>();
    static List<String> promoDesc = new ArrayList<>();
    static List<Double> promoPct = new ArrayList<>();

    // registro de ventas globales
    static int ventasCount = 0;
    static int[] ventaId = new int[MAX_VENTAS];
    static int[] ventaEventoId = new int[MAX_VENTAS];
    static int[] ventaAsiento = new int[MAX_VENTAS];
    static int[] ventaCliente = new int[MAX_VENTAS];
    static double[] ventaPrecioBase = new double[MAX_VENTAS];
    static double[] ventaDescuento = new double[MAX_VENTAS];
    static double[] ventaPrecioFinal = new double[MAX_VENTAS];
    static long[] ventaTimestamp = new long[MAX_VENTAS];
    static int nextVentaId = 1;

    // precios por defecto 
    static int[] eventoPrecioVIP = new int[MAX_EVENTS];
    static int[] eventoPrecioPlatea = new int[MAX_EVENTS];
    static int[] eventoPrecioBalcon = new int[MAX_EVENTS];

    static Scanner kb = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            initSistema();
            loopMenu();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        } finally {
            kb.close();
        }
    }

    static void initSistema() {
        // promociones creadas para probar el sistema
        promoCode.add("PROMO5"); promoDesc.add("Descuento extra 5%"); promoPct.add(0.05);
        promoCode.add("PROMO10"); promoDesc.add("Descuento extra 10%"); promoPct.add(0.10);
        promoCode.add("FEST20"); promoDesc.add("Festival 20%"); promoPct.add(0.20);

        // 3 eventos para testear el sistema
        crearEventoAuto("Concierto Principal", 100, 20000, 15000, 10000); //capacidad primero y precios de cada asiento segun la ubicacion
        crearEventoAuto("Obra Teatro 1", 80, 18000, 12000, 8000);
        crearEventoAuto("Espectaculo Infantil", 60, 15000, 10000, 7000);
    }

    static void crearEventoAuto(String nombre, int cap, int vip, int platea, int balcon) { // crea un evento de forma automatica usando los parametros entregados dentro del codigo
        if (eventosCount >= MAX_EVENTS) return;
        int idx = eventosCount++; //idx abreviatura de indice (index en ingles, casi todos los videos que veo son en ingles, perdone lo gringo xd)
        eventoId[idx] = idx + 1; //se crea una nueva id para el evento
        eventoNombre[idx] = nombre; //se asigna un nombre al evento
        eventoCapacidad[idx] = cap; //se le asigna una capacidad maxima y en las siguientes lineas se asignan los precios para las distintas entradas
        eventoPrecioVIP[idx] = vip;
        eventoPrecioPlatea[idx] = platea;
        eventoPrecioBalcon[idx] = balcon;
        for (int s = 0; s < cap && s < DEFAULT_CAPACITY; s++) {
            eventoAsientoEstado[idx][s] = "Libre"; //cada asiento comieza en estado libre y a medida que se venden o reservan cambian de estado
            eventoTiempoReserva[idx][s] = 0;
            eventoVentaIdPorAsiento[idx][s] = 0; //se inicia el id de ventas en 0 ya que al ser un nuevo evento aun no hay ventas
        }
    }

    static void loopMenu() {
        boolean salir = false;
        int seleccionado = -1;
        while (!salir) {
            try {
                revisarTodasReservas();
                System.out.println("\n ===== Sistema Teatro Moro ===== ");
                System.out.println("Eventos: " + eventosCount + (seleccionado>=0?("  [Evento Seleccionado: "+eventoNombre[seleccionado]+"]"):"")); //muestra para cual de los eventos disponibles se esta comprando o reservando una entrada
                System.out.println("1. Crear evento");
                System.out.println("2. Seleccionar evento");
                System.out.println("3. Reservar asiento");
                System.out.println("4. Modificar reserva");
                System.out.println("5. Comprar entradas");
                System.out.println("6. Imprimir ultima boleta");
                System.out.println("7. Resumen ventas");
                System.out.println("8. Gestion de clientes");
                System.out.println("9. Gestion de promociones");
                System.out.println("0. Salir");
                System.out.print("Opcion: ");
                int op = Integer.parseInt(kb.nextLine().trim());
                switch (op) {
                    case 1: crearEventoInteractive(); break;
                    case 2: seleccionado = seleccionarEventoInteractive(); break;
                    case 3:
                        if (checkSeleccion(seleccionado)) reservarAsientoInteractive(seleccionado);
                        break;
                    case 4:
                        if (checkSeleccion(seleccionado)) modificarReservaInteractive(seleccionado);
                        break;
                    case 5:
                        if (checkSeleccion(seleccionado)) comprarEntradasInteractive(seleccionado);
                        break;
                    case 6:
                        if (checkSeleccion(seleccionado)) imprimirUltimaBoletaEvento(seleccionado);
                        break;
                    case 7:
                        if (checkSeleccion(seleccionado)) mostrarResumenEvento(seleccionado);
                        break;
                    case 8: gestionClientesMenu(); break;
                    case 9: gestionPromocionesMenu(); break;
                    case 0:
                        System.out.println("Muchas gracias por usar el sistema de ventas del Teatro Moro, que tenga un buen dia!");
                        salir = true; break;
                    default:
                        System.out.println("Opcion invalida");
                }
            } catch (NumberFormatException nfe) { // para pillar alguna entrada de numero invalida
                System.out.println("Entrada invalida");
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());  //catch en caso de cualquier null o error inesperado
            }
        }
    }

    static boolean checkSeleccion(int sel) {
        if (sel < 0 || sel >= eventosCount) {
            System.out.println("No hay evento seleccionado. Use la opcion 2."); //para asegurar que el usuario esta comprando las entradas del evento correcto se pide que se seleccione antes de hacer cualquier operacion
            return false;
        }
        return true;
    }

    static void revisarTodasReservas() {
        long ahora = System.currentTimeMillis();
        for (int e = 0; e < eventosCount; e++) {
            int cap = eventoCapacidad[e];
            for (int s = 0; s < cap; s++) {
                if ("Reservado".equals(eventoAsientoEstado[e][s]) && ahora - eventoTiempoReserva[e][s] > RESERVA_TIMEOUT_MS) {
                    eventoAsientoEstado[e][s] = "Libre";
                    eventoTiempoReserva[e][s] = 0;
                    System.out.println("Reserva expirada: Evento " + eventoNombre[e] + " asiento " + (s + 1)); 
                }
            }
        }
    }

    // eventos interactivos, o manualmente ingresados dentro del mismo programa
    static void crearEventoInteractive() {
        try {
            if (eventosCount >= MAX_EVENTS) { System.out.println("Maximo eventos alcanzado"); return; }
            System.out.print("Nombre evento: "); String nombre = kb.nextLine().trim();
            System.out.print("Capacidad (max " + DEFAULT_CAPACITY + "): "); int cap = Integer.parseInt(kb.nextLine().trim());
            System.out.print("Precio VIP: "); int vip = Integer.parseInt(kb.nextLine().trim());
            System.out.print("Precio Platea: "); int p = Integer.parseInt(kb.nextLine().trim());
            System.out.print("Precio Balcon: "); int b = Integer.parseInt(kb.nextLine().trim());
            crearEventoAuto(nombre, Math.min(cap, DEFAULT_CAPACITY), vip, p, b);
            System.out.println("Evento creado.");
        } catch (Exception ex) {
            System.out.println("Error creando evento: " + ex.getMessage()); //catch en caso de cualquier null o error inesperado
        }
    }

    // bloque para que el usuario pueda seleccionar un evento que exista dentro del sistema
    static int seleccionarEventoInteractive() { 
        try {
            if (eventosCount == 0) { System.out.println("No hay eventos"); return -1; }
            System.out.println("Eventos:");
            for (int i = 0; i < eventosCount; i++) System.out.println(eventoId[i] + ". " + eventoNombre[i] + " (cap " + eventoCapacidad[i] + ")");  //muestra la lista de eventos y la capacidad de cada uno
            System.out.print("Ingrese ID evento: ");
            int id = Integer.parseInt(kb.nextLine().trim()); 
            for (int i = 0; i < eventosCount; i++) if (eventoId[i] == id) return i;  //busca  el evento con el id que pone el usuario, en caso de q el id no coincida se envia un codigo de error
            System.out.println("Evento no encontrado");
        } catch (Exception ex) { System.out.println("Error seleccionando: " + ex.getMessage()); } //catch en caso de cualquier null o error inesperado
        return -1;
    }

    static void mostrarAsientosEvento(int e) {
        System.out.println("Asientos evento: " + eventoNombre[e]);
        int cap = eventoCapacidad[e];
        for (int i = 0; i < cap; i++) {
            System.out.printf("%3d:%-9s ", i + 1, eventoAsientoEstado[e][i]); //muestra lista con el estado actual de los asientos
            if ((i + 1) % 10 == 0) System.out.println();
        }
        System.out.println();
    }

    static void reservarAsientoInteractive(int e) {
        try {
            mostrarAsientosEvento(e);
            System.out.print("Ingrese asiento a reservar: ");
            int a = Integer.parseInt(kb.nextLine().trim());
            if (!validaAsiento(e, a)) { System.out.println("Asiento invalido"); return; }
            int idx = a - 1;
            if ("Libre".equals(eventoAsientoEstado[e][idx])) {
                eventoAsientoEstado[e][idx] = "Reservado";
                eventoTiempoReserva[e][idx] = System.currentTimeMillis();
                System.out.println("Reserva exitosa asiento " + a);
            } else {
                System.out.println("Asiento no disponible: " + eventoAsientoEstado[e][idx]);
            }
        } catch (Exception ex) { System.out.println("Error reservar: " + ex.getMessage()); } //catch en caso de cualquier null o error inesperado con mensaje explicando el error en la reserva
    }

    static void modificarReservaInteractive(int e) {
        try {
            System.out.print("Ingrese asiento reservado a modificar: ");
            int viejo = Integer.parseInt(kb.nextLine().trim());
            if (!validaAsiento(e, viejo)) { System.out.println("Asiento invalido"); return; } //valida que el asiento que se busca modificar efectivamente ya haya sido reservado antes de lo contrario muestra un mensaje de error
            int vi = viejo - 1;
            if (!"Reservado".equals(eventoAsientoEstado[e][vi])) { System.out.println("No existe reserva en ese asiento"); return; }
            eventoAsientoEstado[e][vi] = "Libre"; eventoTiempoReserva[e][vi] = 0;
            System.out.print("Ingrese nuevo asiento: ");
            int nuevo = Integer.parseInt(kb.nextLine().trim());
            if (!validaAsiento(e, nuevo) || !"Libre".equals(eventoAsientoEstado[e][nuevo-1])) { System.out.println("Nuevo asiento no disponible"); return; } //se valida el nuevo asiento en caso de que ese este libre, de lo contrario se envia un mensaje de error
            eventoAsientoEstado[e][nuevo-1] = "Reservado"; eventoTiempoReserva[e][nuevo-1] = System.currentTimeMillis();
            System.out.println("Reserva modificada a " + nuevo);
        } catch (Exception ex) { System.out.println("Error modificar reserva: " + ex.getMessage()); } //catch en caso de cualquier null o error inesperado con mensaje explicativo
    }

    // comprar entradas
    static void comprarEntradasInteractive(int e) {
        try {
            mostrarAsientosEvento(e);
            System.out.print("Ingrese asientos a comprar separados por coma (ej: 1,3): ");
            String line = kb.nextLine().trim();
            if (line.isEmpty()) { System.out.println("Entrada vacia"); return; } //en caso de que no se ingrese ningun dato
            System.out.print("Usar cliente existente? (s/n): ");
            String usar = kb.nextLine().trim().toLowerCase(); //se piden los datos para identificar al cliente
            int idCliente = -1;
            if ("s".equals(usar)) {
                listarClientesBreve();
                System.out.print("Ingrese ID de Cliente: ");
                idCliente = Integer.parseInt(kb.nextLine().trim());
                if (!existeClientePorId(idCliente)) { System.out.println("Cliente no encontrado, se creara nuevo"); idCliente = crearClienteInteractive(); } //en caso de ingresar una id invalida se crea un nuevo cliente
            } else {
                idCliente = crearClienteInteractive();
            }
            String[] partes = line.split(",");
            List<Integer> comprados = new ArrayList<>();
            for (String p : partes) {
                try {
                    int a = Integer.parseInt(p.trim());
                    if (!validaAsiento(e, a)) { System.out.println("Asiento fuera de rango: " + a); continue; } //en caso de que se seleccione un numero de asiento fuera de la ubicacion elegida
                    int idx = a - 1;
                    if ("Vendido".equals(eventoAsientoEstado[e][idx])) { System.out.println("Asiento ya vendido: " + a); continue; }
                    // seleccionar ubicacion y precio
                    System.out.println("Ubicacion para asiento " + a + ": 1. Vip 2. Platea 3. Balcon");
                    int ub = Integer.parseInt(kb.nextLine().trim());
                    double base = eventoPrecioPlatea[e];
                    String ubic = "Platea";
                    if (ub == 1) { base = eventoPrecioVIP[e]; ubic = "Vip"; }
                    else if (ub == 3) { base = eventoPrecioBalcon[e]; ubic = "Balcon"; }
                    // descuento automatico dependiendo del cliente
                    System.out.println("Es Estudiante (1) o Tercera edad (2)? Otro = Ninguno");
                    int tipo = Integer.parseInt(kb.nextLine().trim());
                    double descuento = 0;
                    if (tipo == 1) descuento = base * 0.10;
                    else if (tipo == 2) descuento = base * 0.15;
                    // codigos promocionales opcionales
                    System.out.print("Codigo promocion (o enter): ");
                    String cod = kb.nextLine().trim();
                    if (!cod.isEmpty()) {
                        double extra = aplicarPromocion(cod, base);
                        if (extra >= 0) { descuento += extra; System.out.println("Promocion aplicada"); } else System.out.println("Promo no encontrada");
                    }
                    double finalp = base - descuento;
                    // marcar asiento vendido
                    eventoAsientoEstado[e][idx] = "Vendido";
                    eventoVentaIdPorAsiento[e][idx] = registrarVenta(e, a, idCliente, base, descuento, finalp);
                    comprados.add(a);
                    imprimirBoletaPorVentaId(eventoVentaIdPorAsiento[e][idx]);
                } catch (NumberFormatException nfe) { //catch en caso de cualquier null o error inesperado
                    System.out.println("Asiento invalido en lista: " + p);
                }
            }
            if (comprados.isEmpty()) System.out.println("No se realizo ninguna compra");
            else System.out.println("Compra finalizada. Asientos: " + comprados);
            validarReferenciasEvento(e);
        } catch (Exception ex) { System.out.println("Error al comprar entradas: " + ex.getMessage()); } //catch en caso de cualquier null o error inesperado
    }

    static double aplicarPromocion(String code, double base) {
        try {
            for (int i = 0; i < promoCode.size(); i++) if (promoCode.get(i).equalsIgnoreCase(code)) return base * promoPct.get(i);
        } catch (Exception ex) { }
        return -1;
    }

    static int registrarVenta(int eventoIdx, int asiento, int cliente, double base, double desc, double finalp) { 
        if (ventasCount >= MAX_VENTAS) { System.out.println("Limite de ventas alcanzado"); return -1; } 
        int id = nextVentaId++;
        int idx = ventasCount++;
        ventaId[idx] = id;
        ventaEventoId[idx] = eventoId[eventoIdx];
        ventaAsiento[idx] = asiento;
        ventaCliente[idx] = cliente;
        ventaPrecioBase[idx] = base;
        ventaDescuento[idx] = desc;
        ventaPrecioFinal[idx] = finalp;
        ventaTimestamp[idx] = System.currentTimeMillis();  //se almacenan todos los datos de la venta en el indice, asegurando que el id, el evento, asiento cliente y precio esten correctos y relacionados entre si
        return id;
    }

    static void imprimirBoletaPorVentaId(int vId) { //bloque para imprimir la boleta especifica asociada al id de la venta que se busca
        try {
            for (int i = 0; i < ventasCount; i++) {
                if (ventaId[i] == vId) {
                    System.out.println("======================================");
                    System.out.println("            Teatro Moro");
                    System.out.println("======================================");
                    System.out.println("ID de Venta: " + ventaId[i]);
                    System.out.println("ID de Evento: " + ventaEventoId[i]);
                    System.out.println("Asiento: " + ventaAsiento[i]);
                    System.out.println("Precio base: $" + ventaPrecioBase[i]);
                    System.out.println("Descuento: $" + ventaDescuento[i]);
                    System.out.println("Total: $" + ventaPrecioFinal[i]);
                    int c = ventaCliente[i];
                    if (existeClientePorId(c)) System.out.println("Cliente: " + obtenerNombreCliente(c));
                    System.out.println("Gracias por su compra!");
                    System.out.println("======================================");
                    return;
                }
            }
            System.out.println("Venta no encontrada para imprimir boleta");
        } catch (Exception ex) { System.out.println("Error al imprimir boleta: " + ex.getMessage()); } //catch en caso de cualquier null o error inesperado
    }

    // resumen y validaciones
    static void mostrarResumenEvento(int e) {
        try {
            System.out.println("Resumen evento: " + eventoNombre[e]);
            double ingresos = 0;
            int vendidos = 0;
            for (int i = 0; i < ventasCount; i++) {
                if (ventaEventoId[i] == eventoId[e]) {
                    System.out.println("ID de Venta " + ventaId[i] + " Asiento " + ventaAsiento[i] + " Cliente " + ventaCliente[i] + " Total $" + ventaPrecioFinal[i]);
                    ingresos += ventaPrecioFinal[i];
                    vendidos++;
                }
            }
            int disponibles = contarEstado(e, "Libre");
            int reservados = contarEstado(e, "Reservado");
            System.out.println("Vendidos: " + vendidos + " Ingresos: $" + ingresos + " Disponibles: " + disponibles + " Reservados: " + reservados);
        } catch (Exception ex) { System.out.println("Error al mostrar resumen: " + ex.getMessage()); } //catch en caso de cualquier null o error inesperado
    }

    static int contarEstado(int e, String estado) { //para tener la cuenta del estado de los asientos del evento actual
        int c = 0;
        for (int i = 0; i < eventoCapacidad[e]; i++) if (estado.equals(eventoAsientoEstado[e][i])) c++;
        return c;
    }

    static void imprimirUltimaBoletaEvento(int e) {
        try {
            int lastIdx = -1;
            for (int i = ventasCount - 1; i >= 0; i--) if (ventaEventoId[i] == eventoId[e]) { lastIdx = i; break; } 
            if (lastIdx == -1) { System.out.println("No hay ventas para este evento"); return; }
            imprimirBoletaPorVentaId(ventaId[lastIdx]);
        } catch (Exception ex) { System.out.println("Error al imprimir la ultima boleta: " + ex.getMessage()); }  //catch en caso de cualquier null o error inesperado
    }

    static void validarReferenciasEvento(int e) {
        try {
            boolean ok = true;
            double calcIngresos = 0;
            int calcVendidos = 0;
            for (int s = 0; s < eventoCapacidad[e]; s++) {  //cuantos asientos tiene el evento
                int vid = eventoVentaIdPorAsiento[e][s];  //id de ventas de cada asiento vendido "vid" siendo el id de venta
                if (vid != 0) {  //si vid es distinto de 0 significa que esta vendido
                    // buscar venta global
                    int idx = buscarVentaGlobalPorId(vid); 
                    if (idx == -1) { System.out.println("Error: ID de Venta erroneo" + (s+1)); ok = false; } // si el idx == -1 significa que el asiento tiene un id de venta q no existe asi que se muestra un error de referencia
                    else {
                        if (ventaAsiento[idx] != s+1 || ventaEventoId[idx] != eventoId[e]) { System.out.println("Error referencia inconsistente venta " + vid); ok = false; } //verifica que el numero de asiento guardado en la venta coincida con el asiento actual, tambien revisa lo mismo con que sea el mismo evento, de lo contrario muestra un mensaje de error
                        calcIngresos += ventaPrecioFinal[idx]; calcVendidos++;
                        if (!existeClientePorId(ventaCliente[idx])) { System.out.println("Venta referencia a cliente inexistente: " + ventaCliente[idx]); ok = false; } //revisa que el cliente al que se le vendio el asiento realmente exista dentro de los datos del sistema
                    }
                }
            }
            // calcular ingresos reales
            double ingresos = 0; int vendidos = 0;
            for (int i = 0; i < ventasCount; i++) if (ventaEventoId[i] == eventoId[e]) { ingresos += ventaPrecioFinal[i]; vendidos++; } //bucle de verificacion para ver si la venta va asociada al evento correspondiente
            if (Math.abs(ingresos - calcIngresos) > 0.01) { System.out.println("Inconsistencia ingresos evento: calculado:" + calcIngresos + " global:" + ingresos); ok = false; } // se recalculan los ingresos y manda un mensaje de error en caso de que los calculos no sean iguales a los almacenados anteriormente
            if (vendidos != calcVendidos) { System.out.println("Inconsistencia entradas vendidas del evento: " + vendidos + " vs " + calcVendidos); ok = false; }  // muestra si los asientos vendidos coinciden con los calculados anteriormente por el sistema de lo contrario muestra un mensaje de error
            if (ok) System.out.println("Validacion de referencias correcta para el evento " + eventoNombre[e]); // se envia mensaje de aprobacion si todas las partes anteriores pasan exitosamente sin incongruencias
        } catch (Exception ex) { System.out.println("Error al validar referencias: " + ex.getMessage()); }  //catch en caso de cualquier null o error inesperado
    }

    static int buscarVentaGlobalPorId(int id) {
        for (int i = 0; i < ventasCount; i++) if (ventaId[i] == id) return i;
        return -1;
    }

    // clientes
    static int crearClienteInteractive() {
        try {
            if (clientesCount >= MAX_CLIENTS) { System.out.println("Maximo de clientes alcanzado"); return -1; }  //mensaje en caso de superar el maximo de clientes
            System.out.print("Nombre cliente: "); String nom = kb.nextLine().trim();
            System.out.print("Telefono: "); String tel = kb.nextLine().trim();
            System.out.print("Email: "); String em = kb.nextLine().trim();
            int id = nextClienteId++;
            clienteId[clientesCount] = id;
            clienteNombre[clientesCount] = nom; clienteTelefono[clientesCount] = tel; clienteEmail[clientesCount] = em; //guarda los datos del cliente en los arreglos y aumenta el contador en la siguiente linea
            clientesCount++;
            System.out.println("ID de cliente creada " + id);
            return id;
        } catch (Exception ex) { System.out.println("Error al crear cliente: " + ex.getMessage()); return -1; } //catch en caso de cualquier null o error inesperado
    }

    static boolean existeClientePorId(int id) {
        for (int i = 0; i < clientesCount; i++) if (clienteId[i] == id) return true;
        return false;
    }

    static String obtenerNombreCliente(int id) {
        for (int i = 0; i < clientesCount; i++) if (clienteId[i] == id) return clienteNombre[i];
        return "";
    }

    static void listarClientesBreve() {
        if (clientesCount == 0) { System.out.println("No hay clientes"); return; } //en caso de que no haya ningun cliente creado
        System.out.println("Clientes:");
        for (int i = 0; i < clientesCount; i++) System.out.println(clienteId[i] + ". " + clienteNombre[i]);
    }

    static void gestionClientesMenu() {
        boolean loop = true;
        while (loop) {
            try {
                System.out.println("Gestion de clientes");
                System.out.println("1. Crear");
                System.out.println("2. Editar");
                System.out.println("3. Eliminar");
                System.out.println("4. Listar");
                System.out.println("0. Volver");
                System.out.print("Opcion: "); int op = Integer.parseInt(kb.nextLine().trim());
                switch (op) {
                    case 1: crearClienteInteractive(); break;
                    case 2:
                        System.out.print("ID de cliente a editar: "); int id = Integer.parseInt(kb.nextLine().trim());
                        editarCliente(id); break;
                    case 3:
                        System.out.print("ID de cliente a eliminar: "); int del = Integer.parseInt(kb.nextLine().trim());
                        eliminarCliente(del); break;
                    case 4:
                        listarClientesCompleto(); break;
                    case 0: loop = false; break;
                    default: System.out.println("Opcion invalida");
                }
            } catch (Exception ex) { System.out.println("Error de gestion de clientes: " + ex.getMessage()); } //catch en caso de cualquier null o error inesperado
        }
    }

    static void editarCliente(int id) {
        try {
            for (int i = 0; i < clientesCount; i++) {
                if (clienteId[i] == id) {
                    System.out.print("Nuevo nombre (Presione enter para mantener informacion actual): "); String n = kb.nextLine().trim();
                    System.out.print("Nuevo telefono (Presione enter para mantener informacion actual): "); String t = kb.nextLine().trim();
                    System.out.print("Nuevo email (Presione enter para mantener informacion actual): "); String e = kb.nextLine().trim();
                    if (!n.isEmpty()) clienteNombre[i] = n;
                    if (!t.isEmpty()) clienteTelefono[i] = t;
                    if (!e.isEmpty()) clienteEmail[i] = e;
                    System.out.println("Cliente actualizado");
                    return;
                }
            }
            System.out.println("Cliente no encontrado");
        } catch (Exception ex) { System.out.println("Error al editar cliente: " + ex.getMessage()); }
    }

    static void eliminarCliente(int id) {
        try {
            // no eliminar si tiene ventas asociadas
            for (int i = 0; i < ventasCount; i++) if (ventaCliente[i] == id) { System.out.println("No se puede eliminar cliente con ventas asociadas"); return; }
            for (int i = 0; i < clientesCount; i++) {
                if (clienteId[i] == id) {
                    //desplazar
                    for (int j = i; j < clientesCount - 1; j++) {
                        clienteId[j] = clienteId[j+1];
                        clienteNombre[j] = clienteNombre[j+1];
                        clienteTelefono[j] = clienteTelefono[j+1];
                        clienteEmail[j] = clienteEmail[j+1];
                    }
                    clientesCount--;
                    System.out.println("Cliente eliminado");
                    return;
                }
            }
            System.out.println("Cliente no encontrado");
        } catch (Exception ex) { System.out.println("Error al eliminar cliente: " + ex.getMessage()); }
    }

    static void listarClientesCompleto() {
        if (clientesCount == 0) { System.out.println("No hay clientes"); return; } //en caos de que no haya clientes registrados
        System.out.println("Listado clientes:");
        for (int i = 0; i < clientesCount; i++) System.out.println(clienteId[i] + ". " + clienteNombre[i] + " Tel:" + clienteTelefono[i] + " Email:" + clienteEmail[i]);
    }

    //promociones
    static void gestionPromocionesMenu() {
        boolean loop = true;
        while (loop) {
            try {
                System.out.println("Promociones");
                System.out.println("1. Listar Promociones Actuales");
                System.out.println("2. Agregar Nuevo Codigo");
                System.out.println("3. Eliminar Codigo Promocional");
                System.out.println("0. Volver");
                System.out.print("Opcion: "); int op = Integer.parseInt(kb.nextLine().trim());
                switch (op) {
                    case 1: listarPromos(); break;
                    case 2: agregarPromoInteractive(); break;
                    case 3:
                        System.out.print("Codigo promo a eliminar: "); String c = kb.nextLine().trim(); 
                        eliminarPromo(c); break;
                    case 0: loop = false; break;
                    default: System.out.println("Opcion invalida");
                }
            } catch (Exception ex) { System.out.println("Error promociones: " + ex.getMessage()); }
        }
    }

    static void listarPromos() {
        if (promoCode.isEmpty()) { System.out.println("No hay promociones"); return; }
        System.out.println("Promociones:");
        for (int i = 0; i < promoCode.size(); i++) System.out.println(promoCode.get(i) + " - " + promoDesc.get(i) + " (" + (int)(promoPct.get(i)*100) + "%)");
    }

    static void agregarPromoInteractive() { //agregar nuevos codigos promocionales dentro del mismo sistema
        try {
            System.out.print("Codigo: "); String c = kb.nextLine().trim();
            System.out.print("Descripcion: "); String d = kb.nextLine().trim();
            System.out.print("Porcentaje (ej 10 para 10% de descuento): "); double p = Double.parseDouble(kb.nextLine().trim()) / 100.0;
            promoCode.add(c); promoDesc.add(d); promoPct.add(p);
            System.out.println("Promo agregada");
        } catch (Exception ex) { System.out.println("Error al agregar promo: " + ex.getMessage()); }
    }

    static void eliminarPromo(String code) {
        for (int i = 0; i < promoCode.size(); i++) {
            if (promoCode.get(i).equalsIgnoreCase(code)) { promoCode.remove(i); promoDesc.remove(i); promoPct.remove(i); System.out.println("Promo eliminada"); return; } //elimina promociones del indice
        }
        System.out.println("Promo no encontrada");
    }

    //utilidades
    static boolean validaAsiento(int e, int asiento) { 
        return asiento >= 1 && asiento <= eventoCapacidad[e]; //para revisar si el asiento esta dentro del rango correcto del evento (es decir para que no se vaya a vender el asiento 60 en un evento con capacidad para 50 personas)
      }

    static int contarEstadoGlobal(int e, String estado) {
        int c = 0;  
        for (int s = 0; s < eventoCapacidad[e]; s++) if (estado.equals(eventoAsientoEstado[e][s])) c++; //muestra un resumen del estado de los asientos de un evento, es decir dira cuantos asientos estan reservados vendidos y libres
        return c;
    }



}