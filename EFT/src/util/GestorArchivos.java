package util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorArchivos {

    public static List<String> leerArchivo(String nombreArchivo){
        List<String> lineas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))){
            String linea;
            while ((linea = br.readLine()) != null) {
                if(!linea.trim().isEmpty()){
                    lineas.add(linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer archivo " + e.getMessage());
        }
        return lineas;
    }
    public static void escribirArchivo(String nombreArchivo, String contenido) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo))){
            bw.write(contenido);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error al escribir archivo " + e.getMessage());
        }
    }
}
