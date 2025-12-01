package model;

/** clase para representar centro de cultivo de salmones, hereda de UnidadOperativa y agrega atributos.
 */

public class CentroCultivo extends UnidadOperativa {
    private double toneladasProduccion;

    /** Constructor clase CentroCultivo
     */

    public CentroCultivo(String nombre, String comuna, double toneladasProduccion) {
        super(nombre, comuna);
        this.toneladasProduccion = toneladasProduccion;
    }

    public double getToneladasProduccion() {
        return toneladasProduccion;
    }

    public void setToneladasProduccion(double toneladasProduccion) {
        this.toneladasProduccion = toneladasProduccion;
    }

    @Override
    public String toString() {
        return  "- " + nombre + "- \n" +
                "\n Comuna: " + comuna +
                "\n Toneladas Producidas:  " + toneladasProduccion +
                ".\n";
    }
}
