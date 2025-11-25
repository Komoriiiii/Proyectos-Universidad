package util;

/** clase para validar datos de entrada
 * utiliza metodos estaticos para validaciones comunes
 */

public class ValidadorDatos {

    //valida que las strings no sean nulas ni esten vacias

    public static boolean validarTextoNoVacio(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    //validador de numeros positivos

    public static boolean validarNumeroPositivo(double numero){
        return numero > 0;
    }

    //valida que el numero no sea negativo

    public static boolean validarNumeroNoNegativo(double numero){
        return numero >= 0;
    }

    //valida numeros enteors entre ciertos rangos

    public static boolean validarRango(int valor, int min, int max){
        return valor >= min && valor <= max;
    }

    //valida formato de toneladas

    public static boolean validarFormatoToneladas(String toneladas) {
        try {
            double valor = Double.parseDouble(toneladas);
            return validarNumeroNoNegativo(valor);
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    //valida que el estado sea uno de los ya determinados

    public static boolean validarEstado(String estado) {
        return estado.equals("Activo") ||
                estado.equals("Inactivo") ||
                estado.equals("En Mantenimiento");
    }

    //valida formato de linea del archivo, los campos deben estar separados con ";"

    public static boolean validarFormatoLinea(String linea, int camposEsperados) {
        if (!validarTextoNoVacio(linea)) {
            return false;
        }
        String[] campos = linea.split(";");
        return campos.length == camposEsperados;
    }

    //ordena y limpia el texto, usando trim y poniendo primera letra mayuscula

    public static String normalizarTexto(String texto) {
        if (!validarTextoNoVacio(texto)) {
            return "";
        }
        texto = texto.trim();
        return texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase();
    }
}
