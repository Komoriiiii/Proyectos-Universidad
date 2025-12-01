package ui;
import data.GestorUnidades;

/** clase principal donde ejecutar el programa, muesta las unidades creadas por el gestor.
 */

public class Main {

    public static void main(String[] args){
        System.out.println("\n--- Unidades Operativas - Sistema Salmontt ---\n");
        GestorUnidades gestor = new GestorUnidades();
        gestor.crearUnidadesPrueba();

        System.out.println("\n" +"=".repeat(50));
        System.out.println("         Proceso completado con exito");
        System.out.println("=".repeat(50));

    }
}