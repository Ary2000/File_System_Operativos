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
    static int cantidadSectores;
    static int tamanoSectores;
    static RandomAccessFile discoDuro;
    static int cantidadSectoresVacios;
    
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
    
    public static boolean revisarSiHayEspacio(String contenidoProceso) { return cantidadSectoresVacios > contenidoProceso.length() ? true : false; }
    
    public static  ArrayList<Integer> insertarEspacio(String contenidoProceso) {
        int cantidadBytes = contenidoProceso.length();
        ArrayList<Integer> sectoresUsados = new ArrayList();
        int cantidadSectoresProceso = cantidadBytes / tamanoSectores;
        int cantidadSectoresResta = cantidadSectoresProceso;
        if(cantidadBytes % tamanoSectores != 0)
            cantidadSectoresProceso++;
        if(cantidadSectoresProceso <= cantidadSectores) {
            try{
                int indiceContenidoProceso = 0;
                for(int i = 0; 0 < cantidadSectoresProceso; i++) {
                    if(sectores[i] == 0){
                        sectores[i] = 1;
                        cantidadSectoresProceso--;
                        //byte[] data = {(byte)contenidoProceso.charAt(indiceContenidoProceso)};
                        String datosInsertar;
                        if(indiceContenidoProceso+tamanoSectores < contenidoProceso.length()) {
                            datosInsertar = contenidoProceso.substring(indiceContenidoProceso, indiceContenidoProceso+tamanoSectores);
                        }
                        else{
                            datosInsertar = contenidoProceso.substring(indiceContenidoProceso);
                        }
                        indiceContenidoProceso += tamanoSectores;
                        discoDuro.seek(i*tamanoSectores);
                        discoDuro.write(datosInsertar.getBytes());
                        sectoresUsados.add(i);
                    }
                }
                //discoDuro.close();
            } catch(Exception e) {
                System.out.println(e); 
            }
        }
        cantidadSectoresVacios -= cantidadSectoresResta;
        return sectoresUsados;
    }
    
    public static void eliminarEspacios(List<Integer> sectoresUsados) {
        byte[] espaciosLimpios = new byte[tamanoSectores];
        Arrays.fill(espaciosLimpios, (byte)'0');
        sectoresUsados.forEach((sector) -> {
            try{
            sectores[sector] = 0;
            discoDuro.seek(sector);
            discoDuro.write(espaciosLimpios);
            } catch (Exception e) {
            }
        });
        cantidadSectoresVacios += sectoresUsados.size();
    }
    
    public static void crearDiscoDuro() {
        cantidadSectores = inputInteger("Inserte el tamaño del disco: ");
        tamanoSectores = inputInteger("Inserte el tamaño de un sector: ");
        //String contenidoDisco = "0".repeat(cantidadSectores*tamanoSectores);
        char[] contenidoDisco = new char[cantidadSectores*tamanoSectores];
        Arrays.fill(contenidoDisco, '0');
        try {
            File discoDuroAr = new File("discoDuro.txt");
            FileWriter fw = new FileWriter(discoDuroAr.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(contenidoDisco);
            bw.close();
            discoDuro = new RandomAccessFile("discoDuro.txt", "rw");
            sectores = new int[cantidadSectores];
            cantidadSectoresVacios = cantidadSectores;
            Arrays.fill(sectores, 0);
            //sectores = IntStream.range(0, cantidadSectores).toArray();
            //sectores = Arrays.stream(arregloEspacios).boxed().collect( Collectors.toList() );
            System.out.println("Done");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
}
