/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author irsac
 */
public class Arbol {
    private final Directory root;
    public Directory actualDirectory;
    //private ManejadorDD manejador;

    public File archivoTemp;
    public Directory directorioTemp;
    
    public Arbol(int varCantidadSectores,int varTamanoSectores){
        root = new Directory("FS");
        this.actualDirectory = root;
        ManejadorDD.crearDiscoDuro(varCantidadSectores,varTamanoSectores);
    }
    
    public boolean findDirectory(String dirName){
        return this.actualDirectory.directoryExist(dirName);
    }
    
    public boolean findFile(String fileName){
        return this.actualDirectory.fileExist(fileName);
    }
    
    //MKDIR
    public boolean createDirectory(String dirName){
        Directory dir = new Directory(dirName, actualDirectory);
        return actualDirectory.addDirectory(dir);
    }
    
    //FILE
    public boolean createFile(String fileName, String extension, String content){
        ArrayList<Integer> registrosBase = new ArrayList<>();
        ArrayList<Integer> registrosBases = ManejadorDD.insertarEspacio(content);
        File file = new File(fileName, extension, content, registrosBases);
        return actualDirectory.addFile(file);
    }
    
    //CambiarDIR
    public void changeDirectory(){}
    
    private int verificarTipoDato(String ruta) {
        String[] todosDirectorios = ruta.split("/"); 
        String pasoFinal = todosDirectorios[todosDirectorios.length - 1];
        String[] pasosFinales = pasoFinal.split("\\.");
        if(pasosFinales.length == 2)
            return 0;
        return 1;
    }
    
    //Revisa que la ruta virtual exista
    public int revisarRutaVirtual(String rutaVirtual) {
        String[] todosDirectorios = rutaVirtual.split("/"); 
        directorioTemp = actualDirectory;
        if(todosDirectorios[0].equals("FS")) 
            directorioTemp = root;
        for(int i = 1; i < todosDirectorios.length - 1; i++) {
            directorioTemp = directorioTemp.findDirectory(todosDirectorios[i]);
            if(directorioTemp == null)
                //Ruta virtual no es valida
                return -1;
        }
        if(todosDirectorios.length == 1)
            return 1;
        String pasoFinal = todosDirectorios[todosDirectorios.length-1];
        String[] pasosFinales = pasoFinal.split("\\.");
        if(pasosFinales.length == 1) {
            directorioTemp = directorioTemp.findDirectory(pasoFinal);
            //Ruta virtual apunta a un directorio
            return 1;
        }
        archivoTemp = directorioTemp.findFile(pasoFinal);
        //Ruta virtual apunta a un archivo
        return 0;
    }
    
    public void insertarArchivo() {
        
    }
    
    static String rutaRealActual = "";
    static Directory directorioVirtualActual = null;
    
    
    static Path direccionRevisar = null;
    //https://www.baeldung.com/java-folder-size
    private int getTamanoDirectorio(Path path) {
        direccionRevisar = path;
        try {
        long largo = Files.walk(direccionRevisar)
                .filter(p -> p.toFile().isFile())
                .mapToLong(p -> p.toFile().length())
                .sum();
        return (int)largo;
        } catch (Exception e) {
            return -1;
        }
    }
    
    
    public void insertarDirectorio(String rutaReal, Directory directorioVirtual) throws IOException{
        String nombreFolder = rutaReal.split("/")[rutaReal.split("/").length - 1];
        Directory directorioNuevo = new Directory(nombreFolder, directorioVirtual); 
        directorioVirtual.addDirectory(directorioNuevo);
        rutaRealActual = rutaReal.replace("/", "\\");
        directorioVirtualActual = directorioNuevo;
        try (Stream<Path> paths = Files.walk(Paths.get(rutaRealActual))){
                    paths
                            .filter(Files::isRegularFile)
                            .filter(p->p.getParent().toString().equals(rutaRealActual))
                            .forEach(p->{
                                try {
                                    String[] nombreyExtension = p.getFileName().toString().split("\\.");
                                    byte[] codificado = Files.readAllBytes(p.toAbsolutePath());
                                    String contenidoOrigen = new String(codificado, StandardCharsets.UTF_8);
                                    ArrayList<Integer> registrosBase = ManejadorDD.insertarEspacio(contenidoOrigen);
                                    File archivo = new File(nombreyExtension[0], nombreyExtension[1], contenidoOrigen, registrosBase);
                                    directorioVirtualActual.addFile(archivo);
                                } catch(Exception e) {}
                            });
        }
        try (Stream<Path> paths = Files.walk(Paths.get(rutaRealActual))){
                    paths
                            .filter(Files::isDirectory)
                            .filter(p-> p.getParent().toString().equals(rutaRealActual))
                            .forEach(p->{
                                try{
                                    insertarDirectorio(rutaRealActual + "/" + p.getFileName().toString(), directorioVirtualActual);
                                }
                                catch (Exception e) {}
                            });
        }
    }
    
    //https://www.geeksforgeeks.org/traverse-through-a-hashmap-in-java/
    public void directoriosVirualAReal(String rutaDestino, Directory directorioVirtual) {
        try{
            for(Map.Entry mapElement:directorioVirtual.getDirectories().entrySet()) {
                Directory directorioActual = (Directory)mapElement.getValue();
                String rutaNuevoDirectorio = rutaDestino + "/" + directorioActual.getDirectoryName();
                Files.createDirectories(Paths.get(rutaNuevoDirectorio));
                directoriosVirualAReal(rutaNuevoDirectorio, directorioActual);
            }
            for(Map.Entry mapElement:directorioVirtual.getFiles().entrySet()) {
                File fileActual = (File)mapElement.getValue();
                String rutaNuevoArchivo = rutaDestino + "/" + fileActual.getFileName() + "." + fileActual.getExtension();
                Files.createFile(Paths.get(rutaNuevoArchivo));
                byte[] contenidoArchivo = fileActual.getContent().getBytes();
                Files.write(Paths.get(rutaNuevoArchivo), contenidoArchivo);
            }
        } catch(Exception e) {}
    }
    
    Directory copiarDirectorio(Directory directoryACopiar) {
        Directory directorioCopia = new Directory(directoryACopiar.getDirectoryName());
        for (Directory directorioHijo: directoryACopiar.getDirectories().values()) {
            directorioCopia.addDirectory(copiarDirectorio(directorioHijo));
        }
        for (File archivoCopiar: directoryACopiar.getFiles().values()) {
            File archivoCopia = new File(archivoCopiar);
            archivoCopia.setRegistrosBase(ManejadorDD.insertarEspacio(archivoCopiar.getContent()));
            directorioCopia.addFile(archivoCopia);
        }
        return directorioCopia;
    }
    
    //MoVer
    public boolean move(String route, String name, String newName, boolean hasExtension){
        boolean result = false;
        int type = revisarRutaVirtual(route);
        if(type == 1){
            if(hasExtension && !directorioTemp.fileExist(newName)){
                File file = actualDirectory.removeFile(name);
                if(file != null){
                    file.setFileName(newName);
                    file.setModificationDate();
                    directorioTemp.addFile(file);
                    result = true;
                }
            }else if (!directorioTemp.directoryExist(newName)){
                Directory dir = actualDirectory.removeDirectory(name);
                if(dir != null){
                    dir.setFather(directorioTemp);
                    dir.setDirectoryName(newName);
                    directorioTemp.addDirectory(dir);
                    result = true;
                }
            }
        }
        return result;
    }
    
    //COPY
    //No usar rutas con espacios
    public boolean copy(String rutaOrigen, String rutaDestino) throws IOException{
        boolean esPrimeraRutaReal = false;
        boolean esSegundaRutaReal = false;
        String revisarPrimeros = rutaOrigen.substring(0, 2);
        String revisarSegundos = rutaDestino.substring(0, 2);
        if(revisarPrimeros.equals("C:"))
            esPrimeraRutaReal = true;
        if(revisarSegundos.equals("C:"))
            esSegundaRutaReal = true;
        if(esPrimeraRutaReal && esSegundaRutaReal) {
            System.out.println("No se puede usar dos rutas reales");
            return false;
        }
        String contenidoOrigen = "";
        int resultado1 = -1;
        int resultado2 = -1;
        File archivo = null;
        Directory directorio = null;
        if(esPrimeraRutaReal) {
            try{
                if(verificarTipoDato(rutaOrigen) == 0) {
                    byte[] codificado = Files.readAllBytes(Paths.get(rutaOrigen));
                    contenidoOrigen = new String(codificado, StandardCharsets.UTF_8);
                    resultado1 = 0;
                    String[] seccionesRuta = rutaOrigen.split("/");
                    String nombreArchivo = seccionesRuta[seccionesRuta.length - 1];
                    String[] nombreYextencion = nombreArchivo.split("\\.");
                    ArrayList<Integer> temp = new ArrayList<Integer>();
                    archivo = new File(nombreYextencion[0] ,nombreYextencion[1], contenidoOrigen, temp );
                }
                else {
                    resultado1 = 1;
                };
                    
                }
             catch (Exception e) {
                System.out.println("El primer archivo no se puede abrir");
                return false;
            }
        }
        else {
            resultado1 = revisarRutaVirtual(rutaOrigen);
            archivo = archivoTemp;
            directorio = directorioTemp;
            if(resultado1 == -1){
                System.out.println("Primera ruta no valida");
                return false;
            }
            else if (resultado1 == 0) {
                contenidoOrigen = archivo.getContent();
            }
        }
        if(esSegundaRutaReal) {
            if(resultado1 == 0) {
                String rutaNuevo = rutaDestino + "/" + archivo.getFileName() + "." + archivo.getExtension();
                Files.createFile(Paths.get(rutaNuevo));
                byte[] contenidoArchivo = archivo.getContent().getBytes();
                Files.write(Paths.get(rutaNuevo), contenidoArchivo);
            }
            else if(resultado1 == 1) {
                directoriosVirualAReal(rutaDestino, directorio);
            }
        }
        else {
            resultado2 = revisarRutaVirtual(rutaDestino);
            File archivoDestino = archivoTemp;
            Directory directorioDestino = directorioTemp;
            if(resultado1==0){
                if(resultado2 == -1){
                    System.out.println("Segunda ruta no valida");
                    return false;
                } 
                if(resultado2 == 0) {
                    System.out.println("Segunda ruta apunta a un archivo");
                    return false;
                }
                if(resultado1 == 0) {
                    archivoDestino = new File(archivo);
                    if(archivoDestino.getSize() > ManejadorDD.cantidadSectoresVacios * ManejadorDD.tamanoSectores) {
                        System.out.println("El archivo es muy grande para insertar en el disco duro");
                        return false;
                    }
                    ArrayList<Integer> registrosBases = ManejadorDD.insertarEspacio(contenidoOrigen);
                    archivoDestino.setRegistrosBase(registrosBases);
                    directorioDestino.addFile(archivoDestino);
                }
            } else if(resultado1==1) {
                if(esPrimeraRutaReal){
                    if(getTamanoDirectorio(Paths.get(rutaOrigen)) > ManejadorDD.cantidadSectoresVacios * ManejadorDD.tamanoSectores) {
                        System.out.println("No hay suficiente espacio en el disco duro");
                        return false;
                    }
                    insertarDirectorio(rutaOrigen, directorioDestino);
                }
                else {
                    if(directorio.conseguirTamanoDirectorio() > ManejadorDD.cantidadSectoresVacios * ManejadorDD.tamanoSectores) {
                        System.out.println("No hay suficiente espacio en el disco duro");
                        return false;
                    }
                    directorioDestino.addDirectory(copiarDirectorio(directorio));
                }
            }
        }
        return true;
    }
    
    //ModFILE
    public void modFile(String fileName, String content){
        File file = actualDirectory.findFile(fileName);
        file.setContent(content);
        file.setModificationDate();
        file.setSize(content.length());
    }
    
    //VerPropiedades
    public String fileProperties(String fileName){
        File file = actualDirectory.findFile(fileName);
        if(file != null){
            return file.toString();
        }
        return "File not fount";
    }
    
    //VerFile
    public String fileContent(String fileName){
        File file = actualDirectory.findFile(fileName);
        if(file != null){
            return file.getContent();
        }
        return "File not fount";
    }
    
    //FIND
    public ArrayList<String> find(String name){
        ArrayList<String> found = findAux(name, "", root);
        return found;
    }
    
    private ArrayList<String> findAux(String name, String route, Directory dir){
        ArrayList<String> match = new ArrayList<>();
        if(dir.getDirectoryName().equals(name)){
            match.add(route + name);
        }
        for(Map.Entry directoryEntry: dir.getDirectories().entrySet()){
            Directory directory = (Directory) directoryEntry.getValue();
            match.addAll(findAux(name,route + dir.getDirectoryName() + "/", directory));
        }
        for(Map.Entry fileEntry: dir.getFiles().entrySet()){
            File file = (File) fileEntry.getValue();
            if(file.getFileName().equals(name) || file.getExtension().equals(name) || (file.getFileName() + file.getExtension()).equals(name)){
                match.add(route + (file.getFileName() +"."+file.getExtension()));
            }
        }
        return match;
    }
    
    public boolean renameFile(String fileName, String newName){
        File file = actualDirectory.findFile(fileName);
        if(file != null && !actualDirectory.fileExist(newName)){
            actualDirectory.getFiles().remove(fileName);
            file.setFileName(newName);
            actualDirectory.addFile(file);
            return true;
        }
        return false;
    }

    public boolean renameDirectory(String directoryName, String newName){
        Directory directory = actualDirectory.findDirectory(directoryName);
        if(directory != null && !actualDirectory.directoryExist(newName)){
            actualDirectory.getDirectories().remove(directoryName);
            directory.setDirectoryName(newName);
            actualDirectory.addDirectory(directory);
            return true;
        }
        return false;
    }
    
    public boolean goToRoot(){
        this.actualDirectory = root;
        return true;
    }
    
    public boolean goToFather(){
        if(this.actualDirectory == this.root){
            return false;
        }
        this.actualDirectory = this.actualDirectory.getFather();
        return true;
    }
    
    public boolean goToChildren(String dirName){
        Directory dir = actualDirectory.findDirectory(dirName);
        if(dir != null){
            this.actualDirectory = dir;
            return true;
        }
        return false;
    }
 
    public void imprimirArbol(){
        imprimirArbolAux(root,1);
    }
    
    private void imprimirArbolAux(Directory dir,int cont){
        char[] charArray = new char[cont];
        Arrays.fill(charArray, ' ');
        System.out.println(new String(charArray)+dir.getDirectoryName());
        for(Directory directoryEntry: dir.getDirectories().values()){
            imprimirArbolAux(directoryEntry,cont+1);
        }
        for(File fileEntry: dir.getFiles().values()){
            System.out.println(new String(charArray)+" "+ fileEntry.getFileName() + "." + fileEntry.getExtension());
        }
    }
    
    /*

    public void imprimirArbol(Directory directorioRaiz){
        /*"├── "
        "│   "
        "└── "
        "    "*/
        /*HashMap<String,Directory> varDirectories = root.getDirectories();
        HashMap<String,File> varFiles = root.getFiles();
        
        for (Integer key: map.keySet()){  
                System.out.println(key+ " = " + map.get(key));
        } */
        /*
        for (Iterator<TreeNode> it = children.iterator(); it.hasNext();) {
            TreeNode next = it.next();
            if (it.hasNext()) {
                next.print(buffer, childrenPrefix + "├── ", childrenPrefix + "│   ");
            } else {
                next.print(buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
            }
        }
    }*/
    
}
