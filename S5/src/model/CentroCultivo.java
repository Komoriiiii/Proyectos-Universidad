package model;


import java.util.Objects;

/** Clase que representa el centro de cultivo del salmon
     * contiene informacion sobre nombre, comuna y cuantas toneladas han sido producidas
     */

        //parametros

    public class CentroCultivo {
        private String nombre;
        private String comuna;
        private double toneladasProducidas;
        private String estado; // Activo, Inactivo, En Mantenimiento
        private int empleados;

        // constructor vacio

        public CentroCultivo() {
            this.estado = "Activo";
            this.empleados = 0;
        }

        //constructor con parametros basico

        public CentroCultivo(String nombre, String comuna, double toneladasProducidas) {
            this.nombre = nombre;
            this.comuna = comuna;
            this.toneladasProducidas = toneladasProducidas;
            this.estado = "Activo";
            this.empleados = 0;
        }

        //Constructor Completo

        public CentroCultivo(String nombre, String comuna, double toneladasProducidas, String estado, int empleados) {
            this.nombre = nombre;
            this.comuna = comuna;
            this.toneladasProducidas = toneladasProducidas;
            this.estado = estado;
            this.empleados = empleados;
        }

        //getters

        public String getNombre() {
            return nombre;
        }

        public String getComuna() {
            return comuna;
        }

        public double getToneladasProducidas() {
            return toneladasProducidas;
        }

        public String getEstado() {
            return estado;
        }

        public int getEmpleados() {
            return empleados;
        }

        // setters con validaciones
        public void setNombre(String nombre) {
            if (nombre == null || nombre.trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacio");
            }
            this.nombre = nombre;
        }
        public void setComuna(String comuna) {
            if (comuna == null || comuna.trim().isEmpty()) {
                throw new IllegalArgumentException("La comuna no puede estar vacia");
            }
            this.comuna = comuna;
        }

        public void setToneladasProducidas(double toneladasProducidas) {
            if (toneladasProducidas < 0) {
                throw new IllegalArgumentException("Las toneladas producidas no pueden ser negativas");
            }
            this.toneladasProducidas = toneladasProducidas;
        }

        public void setEstado(String estado) {
            if (!estado.equals("Activo") && !estado.equals("Inactivo") && !estado.equals("En Mantenimiento")) {
                throw new IllegalArgumentException("Estado invalido. Usar: Activo, Inactivo o En Mantenimiento");
            }
            this.estado = estado;
        }

        public void setEmpleados(int empleados) {
            if (empleados < 0) {
                throw new IllegalArgumentException("El numero de empleados no puede ser negativo");
            }
            this.empleados = empleados;
        }

        public boolean esAltaProduccion(){
            return toneladasProducidas > 1000;
        }

        public String getCategoria() {
            if (toneladasProducidas >= 2000) {
                return "Excelente";
            } else if (toneladasProducidas >= 1500) {
                return "Muy Buena";
            } else if (toneladasProducidas >= 1000) {
                return "Buena";
            } else if (toneladasProducidas >= 500) {
                return "Regular";
            } else {
                return "Baja";
            }
        }

        public double getProduccionPorEmpleado(){
            if (empleados == 0) {
                return 0;
            }
            return toneladasProducidas / empleados;
        }

        //toString

        @Override
        public String toString() {
            return String.format("CentroCultivo{nombre='%s', comuna='%s', toneladas=%.2f, estado='%s', empleados=%d, categoria='%s'}",
            nombre, comuna, toneladasProducidas, estado, empleados, getCategoria());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CentroCultivo that = (CentroCultivo) o;
            return Objects.equals(nombre, that.nombre) && Objects.equals(comuna, that.comuna);
        }
        @Override
        public int hashCode() {
            return Objects.hash(nombre, comuna);
        }
    }
