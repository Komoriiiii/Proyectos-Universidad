/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package semana.pkg4;

import java.util.Scanner;

/**
 *
 * @author Gustavo Gana
 */
public class Semana4 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        Scanner kb = new Scanner(System.in); //profe uso kb porque tengo todo el sistema operativo en ingles y me acostumbre a poner kb por keyboard en vez de tc, perdon

        // variables
        String opcionMenu;
        boolean continuar = true;

        // zonas del teatro con precios base
        int precioZonaA = 20000;
        int precioZonaB = 15000;
        int precioZonaC = 10000;

        System.out.println("Bienvenido al sistema de venta de entradas del Teatro Moro!");

        // ciclo principal del menu usando for 
        for (; continuar; ) {
            System.out.println("\n Menu Principal"); //  \n para saltar una linea y hacerlo mas ordenado
            System.out.println("1. Comprar entradas");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opcion: ");
            opcionMenu = kb.nextLine();

            if (opcionMenu.equals("1")) {
                //comprar entrada
                String zona = "";
                int precioBase = 0;

                System.out.println("\n Zonas del Teatro");
                System.out.println("Zona A: $" + precioZonaA);
                System.out.println("Zona B: $" + precioZonaB);
                System.out.println("Zona C: $" + precioZonaC);

                System.out.print("Ingrese la zona deseada (A, B, C): ");
                zona = kb.nextLine().toUpperCase();

                if (zona.equals("A")) {
                    precioBase = precioZonaA;
                } else if (zona.equals("B")) {
                    precioBase = precioZonaB;
                } else if (zona.equals("C")) {
                    precioBase = precioZonaC;
                } else {
                    System.out.println("Zona invalida, intente nuevamente.");
                    continue; // vuelta al menu
                }

                // validacion de edad y descuentos
                int edad = -1;
                while (edad < 0) {
                    try {
                        System.out.print("Ingrese su edad: ");
                        edad = Integer.parseInt(kb.nextLine());
                        if (edad < 0) {
                            System.out.println("Edad invalida.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Ingrese un numero valido.");
                    }
                }

                double descuento = 0.0;
                String tipoDescuento = "Ninguno";

                if (edad < 25) { // estudiantes
                    descuento = 0.10;
                    tipoDescuento = "Estudiante (10%)";
                } else if (edad >= 60) { // tercera edad
                    descuento = 0.15;
                    tipoDescuento = "Tercera edad (15%)";
                }

                // Calculo del precio final con  do-while
                double precioFinal;
                int confirmar = 0;
                do {
                    precioFinal = precioBase - (precioBase * descuento);
                    confirmar = 1; // solo calculamos una vez
                } while (confirmar == 0);

                // Resumen de compra
                System.out.println("\n Resumen de la compra");
                System.out.println("Ubicación del asiento: Zona " + zona);
                System.out.println("Precio base: $" + precioBase);
                System.out.println("Descuento aplicado: " + tipoDescuento);
                System.out.println("Precio final a pagar: $" + precioFinal);

                // preguntar si desea seguir comprando
                System.out.print("\n ¿Desea realizar otra compra? (S/N): ");
                String respuesta = kb.nextLine().toUpperCase();
                if (!respuesta.equals("S")) {
                    continuar = false;
                }

            } else if (opcionMenu.equals("2")) {
                System.out.println("Gracias por usar el sistema, hasta pronto!");
                continuar = false;
            } else {
                System.out.println(" Opcion invalida, intente nuevamente.");
                }
            }
        }
    }

   





