/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package Interfaz;

import Tree.Arbol;
import Tree.Directory;
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
    public String dirActual = "Ingrese su instruccion:";
    private boolean create = false;
    private Arbol arbolCreado;
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
    int,int; SI
    stringContenido,stringnombre.extension; SI
    nombreDirectorio; SI
    RECIBE 1 RUTA; 
    nada;
    1 NOMBRE.EXTENSION Y UN STRING;
    NOMBRE.EXTENSION;
    NOMBRE.EXTENSION;
    STRING.RUTA,STRING.RUTA;
    NOMBRE.TXT O SIN .TXT Y DIRECCION DONDE SE VA A MOVER;
    NOMBRE.TXT O SIN .TXT;
    STRING;
    NADA;*/
    

    public Hilo(int velocidad) {
        this.velocidad = velocidad;
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
    public void instructorCalls(String[] instruccions,int instrNumber){
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
                String nombre[] = instruccions[1].split("\\."); 
                arbolCreado.createFile(nombre[0], nombre[1], instruccions[2]);
                //System.out.println("n:"+nombre[0]+"e"+nombre[1]+"cont"+instruccions[2]);
              // code block
              break;
            case 3:
                // se debe validad que el directorio venga sin extension
              arbolCreado.createDirectory(instruccions[1]);
              // code block
              break;
            case 4:
              // code block
              break;
            case 5:
              // code block
              break;
            case 6:
              // code block
              break;
            case 7:
              // code block
              break;
            case 8:
              // code block
              break;
            case 9:
              // code block
              break;
            case 10:
              // code block
              break;
            case 11:
              // code block
              break;
            case 12:
              // code block
              break;
            case 13:
              // code block
              break;
            default:
              // code block
          }
    }
}
