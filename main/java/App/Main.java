package App;


import model.Direccion;
import model.Empleado;
import model.Persona;

/** clase main donde ejecutar y probar el sistema
 */

public class Main {
    public static void main(String[] args) {
        System.out.println("\n--- Sistema de Gestion Salmontt ---");

        // ejemplo de persona con direccion completa, se tratara como eventual no como empleado contratado para instanciar la creacion simple de una persona.

        System.out.println("\nEventual encargada de limpieza");
        Direccion direccion1 = new Direccion(
                "Av. Pedro Aguirre Cerda",
                "245",
                "Puerto Montt",
                "Los Lagos"
        );
Persona persona1 = new Persona(
                "12.123.456-1",
                "Juanita",
                "Perez",
                "+569 1234 1234",
                "juanitabakan1986@gmail.com",
                 direccion1
);
        System.out.println(persona1);
        System.out.println();

        System.out.println("\nEmpleado Sector de Produccion");
        Direccion direccion2 = new Direccion(
                "5 Oriente",
                "325",
                "Puerto Montt",
                "Los Lagos"
        );
        Empleado empleado1 = new Empleado(
                "20.205.206-K",
                "Juan",
                "Perez",
                "+569 9876 3421",
                "juan.perez@salmontt.cl",
                direccion2,
                "Supervisor de Produccion",
                "Planta de Procesamiento",
                1650000
        );
        System.out.println(empleado1);
        System.out.println();

        System.out.println("\nEmpleado Administrativo");
        Direccion direccion3 = new Direccion(
                "Av. Ramon Barros Luco",
                "384",
                "Puerto Montt",
                "Los Lagos"
        );
        Empleado empleado2 = new Empleado(
                "17.472.254-3",
                "Roberto",
                "Diaz",
                "+56 9 7364 3857",
                "roberto.diaz@salmontt.cl",
                direccion3,
                "Jefe de Recursos Humanos",
                "Administracion",
                1500000
        );
        System.out.println(empleado2);
        System.out.println();

        //demostracion de funcionalidades ordenada

        System.out.println("Nombre Completo Eventual de Aseo: " + persona1.getNombreCompleto());
        System.out.println("Email de Supervisor de Produccion: " + empleado1.getEmail());
        System.out.println("Numero de Telefono Jefe de RRHH: "+ empleado2.getTelefono());
    }
        }
