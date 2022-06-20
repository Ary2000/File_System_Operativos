/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package Interfaz;

import Tree.Arbol;
import Tree.Directory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;


/**
 *
 * @author yeico
 */
//Esta clase debe llamarse Consola.java
public class Hilo extends Thread{
    private int velocidad;
    private boolean ejecutar = false; 
    public String dirActual = "/>";
    private boolean create = false;
    private Arbol arbolCreado;
    public String[] nombre;
    private String[] palabrasReservadas = {
        "CREATE",
        "FILE",
        "MKDIR",
        "CambiarDIR",
        "ListarDIR",
        "ModFILE",
        "VerPropiedades",
        "VerFile",
        "COPY",
        "MOVER",
        "REMOVE",
        "FIND",
        "TREE"
    };
    private int[] espacios = {3,3,2,2,1,3,2,2,3,3,2,2,1};
    /*
    1.int,int; SI
    2.stringContenido,stringnombre.extension; SI
    3.nombreDirectorio; SI
    4.RECIBE 1 RUTA; 
    5. nada;
    6.1 NOMBRE.EXTENSION Y UN STRING;
    7.NOMBRE.EXTENSION;
    8.NOMBRE.EXTENSION;
    9.STRING.RUTA,STRING.RUTA;
    10.NOMBRE.TXT O SIN .TXT Y DIRECCION DONDE SE VA A MOVER;
    11.NOMBRE.TXT O SIN .TXT;
    12.STRING;
    13.NADA;*/
    

    public Hilo(int velocidad) {
        this.velocidad = velocidad;
    }
    
    public boolean revisarSiRutaEsAbsoluta(String ruta) {
        String[] pasos = ruta.split("/");
        if(pasos[0].equals("FS"))
            return true;
        return false;
    }
    
    @Override
    public void run(){
        try{
            while(ejecutar){
                Scanner sc = new Scanner(System.in);
                System.out.print(dirActual);
                String inputText = sc.nextLine();
                //System.out.println(inputText);
                String input[] = inputText.split(" ");
                for (int i = 0; i < palabrasReservadas.length; i++) {
                    if(palabrasReservadas[i].equals(input[0])){
                        if(input.length == espacios[i]){
                            //Significa que la entrada es correcta
                            instructorCalls(input,i+1);
                        }
                        else if(i==1 && input.length>2){
                            instructorCalls(input,i+1);   
                        }
                        else if(i==5 && input.length>2){
                            instructorCalls(input,i+1);   
                        }
                        else{
                            System.out.println("[ERROR] Falta asignar algún parametro");
                        }
                    }
                }
                sleep(velocidad);
            }
            
        }
        catch(InterruptedException e){
            System.out.println(e.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(Hilo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void iniciar(){
        if (!ejecutar) {
            ejecutar = true;
            start(); //metodo del thread que ejecuta el run 
        }
    }
    public void terminar(){
        if(ejecutar){
            ejecutar = false;
        }
    }
    public void instructorCalls(String[] instruccions,int instrNumber) throws IOException{
        Arbol arbol;
        switch(instrNumber) {
            case 1:
                if(create==false){
                    //System.out.println("Se ha creado un disco!!!");
                    arbol = new Arbol(Integer.parseInt(instruccions[1]),Integer.parseInt(instruccions[2]));
                    create = true;
                    dirActual = arbol.actualDirectory.getDirectoryName() + "/>";
                    arbolCreado = arbol;
                }
                else{
                    System.out.println("ya existe un disco!!!");
                }
                break;
            case 2:
                //se debe poder ingresar el contenido de forma separada
                nombre = instruccions[1].split("\\.");
                String content = "";
                for (int i = 2; i < instruccions.length; i++) {
                    content += instruccions[i] + " ";
                }
                arbolCreado.createFile(nombre[0], nombre[1], content.substring(0, content.length()-1));
                //dirActual = arbolCreado.actualDirectory.getDirectoryName() + "/>";
                //System.out.println("n:"+nombre[0]+"e"+nombre[1]+"cont"+instruccions[2]);
              // code block
              break;
            case 3:
                // se debe validad que el directorio venga sin extension
              arbolCreado.createDirectory(instruccions[1]);
              //dirActual = arbolCreado.actualDirectory.getDirectoryName() + "/>";
              // code block
              break;
            case 4:
              // cambiarDir
                int resultado = arbolCreado.revisarRutaVirtual(instruccions[1]);
                if(resultado ==-1){
                    System.out.println("Ruta no existe");
                }
                else if(resultado==0){
                    System.out.println("Ruta apunta a un archivo");
                }
                else{
                    arbolCreado.actualDirectory = arbolCreado.directorioTemp;
                    if(revisarSiRutaEsAbsoluta(instruccions[1]))
                        dirActual = instruccions[1] + "/>";
                    else
                        dirActual = dirActual.substring(0, dirActual.length() - 1) + instruccions[1] + "/>";
                        //dirActual = arbolCreado.actualDirectory.getDirectoryName() + "/>";
                }
              break;
            case 5:
              //actual directorio,imprimir nombres  
              arbolCreado.listarDir();
              //dirActual = arbolCreado.actualDirectory.getDirectoryName() + "/>";
              break;
            case 6:
                String content2 = "";
                for (int i = 2; i < instruccions.length; i++) {
                    content2 += instruccions[i] + " ";
                }
              arbolCreado.modFile(instruccions[1], content2);
              //dirActual = arbolCreado.actualDirectory.getDirectoryName() + "/>";
              break;
            case 7:
              System.out.println(arbolCreado.fileProperties(instruccions[1]));
              //dirActual = arbolCreado.actualDirectory.getDirectoryName() + "/>";
              break;
            case 8:
              System.out.println(arbolCreado.fileContent(instruccions[1]));
              //dirActual = arbolCreado.actualDirectory.getDirectoryName() + "/>";
              break;
            case 9:
              arbolCreado.copy(instruccions[1],instruccions[2]);
              //dirActual = arbolCreado.actualDirectory.getDirectoryName() + "/>";
              break;
            case 10:
                nombre = instruccions[1].split("\\."); 
                if(nombre.length==2){
                    arbolCreado.move(instruccions[2], instruccions[1], nombre[0], true);
                }else{
                    arbolCreado.move(instruccions[2], instruccions[1], nombre[0], false);
                }
                //dirActual = arbolCreado.actualDirectory.getDirectoryName() + "/>";
              break;
            case 11:
                nombre = instruccions[1].split("\\."); 
                if(nombre.length==2){
                    arbolCreado.actualDirectory.deleteFile(instruccions[1]);
                }else{
                    arbolCreado.actualDirectory.deleteDirectory(instruccions[1]);
                }
                //dirActual = arbolCreado.actualDirectory.getDirectoryName() + "/>";
              break;
            case 12:
                ArrayList<String> found = arbolCreado.find(instruccions[1]);
                for (int i = 0; i < found.size(); i++) {
                    System.out.println(found.get(i));
                }
                //dirActual = arbolCreado.actualDirectory.getDirectoryName() + "/>";
              break;
            case 13:
                arbolCreado.imprimirArbol();
              break;
          }
    }
}
