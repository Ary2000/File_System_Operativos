/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tree;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.io.RandomAccessFile;

/**
 *
 * @author aryel
 */
public class ManejadorDD {
    
    private static int[] sectores;
    int cantidadSectores;
    int tamanoSectores;
    
    public static int inputInteger(String mensaje){
        Scanner sc = new Scanner(System.in);
        boolean inputCorrecto = false;
        while(!inputCorrecto) {
        try {
            System.out.print(mensaje);
            int tamanoTxt = sc.nextInt();
            return tamanoTxt;
        }
        catch(Exception e) {
              System.out.print("");
        }
        System.out.println("Por favor usar un numero natural");
        }
        return -1;
    }
    
    public boolean revisarSiHayEspacio(int cantidadBytes) { return cantidadBytes/tamanoSectores == 0 ? false: true ; }
    
    public List<Integer> insertarEspacio(String contenidoProceso) {
        int cantidadBytes = contenidoProceso.length();
        List<Integer> sectoresUsados = new ArrayList();
        int cantidadSectoresProceso = cantidadBytes / tamanoSectores;
        if(cantidadBytes % tamanoSectores == 0)
            cantidadSectoresProceso++;
        if(cantidadSectoresProceso <= cantidadSectores) {
            try{
                RandomAccessFile discoDuro = new RandomAccessFile("discoDuro.txt", "rw");
                int indiceContenidoProceso = 0;
                for(int i = 0; cantidadSectoresProceso < 0; i++) {
                    if(sectores[i] == 0){
                        sectores[i] = 1;
                        cantidadSectoresProceso--;
                        byte[] data = {(byte)contenidoProceso.charAt(i)};
                        discoDuro.write(data, i, 1);
                        sectoresUsados.add(i);
                    }
                }
                discoDuro.close();
            } catch(Exception e) {
                        
                    }
        }
        return sectoresUsados;
    }
    
    public void eliminarEspacios(List<Integer> sectoresUsados) {
        sectoresUsados.forEach((sector) -> {
            sector = 0;
        });
    }
    
    public void crearDiscoDuro() {
        cantidadSectores = inputInteger("Inserte el tamaño del disco: ");
        tamanoSectores = inputInteger("Inserte el tamaño de un sector: ");
        //String contenidoDisco = "0".repeat(cantidadSectores*tamanoSectores);
        char[] contenidoDisco = new char[cantidadSectores*tamanoSectores];
        Arrays.fill(contenidoDisco, '0');
        try {
            File discoDuro = new File("discoDuro.txt");
            FileWriter fw = new FileWriter(discoDuro.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(contenidoDisco);
            bw.close();
            int[] sectores = IntStream.range(0, cantidadSectores).toArray();
            //sectores = Arrays.stream(arregloEspacios).boxed().collect( Collectors.toList() );
            System.out.println("Done");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
}
