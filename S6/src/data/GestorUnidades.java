package data;

import model.PlantaProceso;
import model.CentroCultivo;

/** Clase para crear y gestionar instancias de unidades operativas
 * genera datos de prueba para el sistema
 */

public class GestorUnidades {
    /** crea instancias de prueba de centros de cultivo y plantas de procesamiento
     */

    public void crearUnidadesPrueba(){
        //centros de cultivo
        CentroCultivo centro1 = new CentroCultivo(
                "Centro Cultivo Quellon",
                "Quellon",
                1250
        );
        CentroCultivo centro2 = new CentroCultivo(
                "Centro Cultivo Islote Conejos",
                "Calbuco",
                1000
        );
        //plantas de procesamiento
        PlantaProceso planta1 = new PlantaProceso(
                "Planta Puerto Montt",
                "Puerto Montt",
                200
        );
        PlantaProceso planta2 = new PlantaProceso(
                "Planta Castro",
                "Castro",
                300
        );

        //muestra informacion de las unidades
        System.out.println("\n --- Centros de Cultivo ---\n");
        System.out.println(centro1);
        System.out.println(centro2);

        System.out.println("\n --- Plantas de Procesamiento ---\n");
        System.out.println(planta1);
        System.out.println(planta2);
    }
}
