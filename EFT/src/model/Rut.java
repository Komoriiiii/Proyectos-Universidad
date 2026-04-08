package model;

public class Rut {
    private String numero;
    private char digitoVerificador;

    public Rut (String rutCompleto) throws RutInvalidoException {
        validarYAsignar(rutCompleto);
    }

    //validacion con modulo 11
    private void validarYAsignar(String rutCompleto) throws RutInvalidoException {
        if (rutCompleto == null || rutCompleto.isEmpty()) {
            throw new RutInvalidoException("El Rut no puede estar vacio");
        }

        String rutLimpio = rutCompleto.replaceAll("[^0-9kK-]","");
        String [] partes =  rutLimpio.split("-");

        if (partes.length != 2) {
            throw new RutInvalidoException("Formato de rut invalido. Use 12345678-9");
        }
        this.numero = partes [0];
        this.digitoVerificador = partes[1].toUpperCase().charAt(0);

        if (!validarDigitoVerificador()){
            throw new RutInvalidoException("Digito verificador invalido");
    }
}
 private boolean validarDigitoVerificador() {
     try {
         int rutNum = Integer.parseInt(numero);
         int m = 0, s = 1;
         for (; rutNum != 0; rutNum /= 10) {
             s = (s + rutNum % 10 * (9 - m++ % 6)) % 11;
         }
         char dvCalculado = (char) (s != 0 ? s + 47 : 75);
         return dvCalculado == digitoVerificador;
        } catch (NumberFormatException e) {
         return false;
        }
    }
    public String getRutCompleto() {
    return numero + "-" + digitoVerificador;
    }

    @Override
    public String toString() {
    return getRutCompleto();
        }
    }