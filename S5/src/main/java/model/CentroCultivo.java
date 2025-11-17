package model;


    /** Clase que representa el centro de cultivo del salmon
     * contiene informacion sobre nombre, comuna y cuantas toneladas han sido producidas
     */

        //parametros

    public class CentroCultivo {
        private String nombre;
        private String comuna;
        private double toneladasProducidas;

        // constructor vacio

        public CentroCultivo() {
        }

        //constructor con parametros

        public CentroCultivo(String nombre, String comuna, double toneladasProducidas) {
            this.nombre = nombre;
            this.comuna = comuna;
            this.toneladasProducidas = toneladasProducidas;
        }

        //getters y setters

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getComuna() {
            return comuna;
        }

        public void setComuna(String comuna) {
            this.comuna = comuna;
        }

        public double getToneladasProducidas() {
            return toneladasProducidas;
        }

        public void setToneladasProducidas(double toneladasProducidas) {
            this.toneladasProducidas = toneladasProducidas;
        }

        //toString

        @Override
        public String toString() {
            return "CentroCultivo{" +
                    "nombre='" + nombre + '\'' +
                    ", comuna='" + comuna + '\'' +
                    ", toneladasProducidas=" + toneladasProducidas +
                    '}';
        }
    }
