/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author irsac
 */
public class Directory {
    private String directoryName;
    private Directory father;
    private HashMap<String,Directory> directories;
    private HashMap<String,File> files;

    public Directory(String pName){
        this.directoryName = pName;
        this.directories = new HashMap<>();
        this.files = new HashMap<>();
        this.father = null;
    }
    
    public Directory(String pName, Directory father){
        this.directoryName = pName;
        this.directories = new HashMap<>();
        this.files = new HashMap<>();
        this.father = father;
    }
    
    public int conseguirTamanoDirectorio() {
        int tamanoDirectorio = 0;
        for(File archivo: files.values()) {
            tamanoDirectorio += archivo.getSize();
        }
        for(Directory directorio: directories.values()) {
            tamanoDirectorio += directorio.conseguirTamanoDirectorio();
        }
        return tamanoDirectorio;
    }

    public boolean isEmpty(){
        return directories.isEmpty() && files.isEmpty();
    }
    
    public boolean directoryExist(String directoryName){
        return directories.containsKey(directoryName);
    }
    
    public boolean fileExist(String fileName){
        return files.containsKey(fileName);
    }

    public boolean addDirectory(Directory directory){
        if(!directoryExist(directory.getDirectoryName()) && this != directory){
            directories.put(directory.getDirectoryName(),directory);
            return true;
        }
        return false;
    }

    public ArrayList<Integer> deleteDirectory(String dName){
        Directory d = directories.get(dName);
        ArrayList<Integer> temp = new ArrayList<>();
        if(d != null){
            if(d.isEmpty()){
                this.directories.remove(dName);
            }
            else{
                temp = d.deleteAll();
                for(Map.Entry fileEntry: files.entrySet()){
                    File file = (File) fileEntry.getValue();
                    temp.addAll(file.getRegistrosBase());
                }
                this.directories.remove(dName);
            }
        }
        files.clear();
        return temp;
    }

    private ArrayList<Integer> deleteAll(){
        ArrayList<Integer> temp = new ArrayList<>();
        for(Map.Entry directoryEntry: directories.entrySet()){
            String dName = (String)directoryEntry.getKey();
            Directory directory = (Directory) directoryEntry.getValue();
            temp.addAll(directory.deleteAll());
            directories.remove(dName);//corregir?
        }
        for(Map.Entry fileEntry: files.entrySet()){
            File file = (File) fileEntry.getValue();
            temp.addAll(file.getRegistrosBase());
        }
        files.clear();
        return temp;
    }

    public boolean addFile(File file){
        if(!fileExist(file.getFileName()+file.getExtension())){
            files.put(file.getFileName()+ "." +file.getExtension(),file);
            return true;
        }
        return false;
    }

    public ArrayList<Integer> deleteFile(String fileName){
        if(fileExist(fileName)){
            File file = files.get(fileName);
            files.remove(fileName);
            return file.getRegistrosBase();
        }
        return null;
    }

    public String getDirectoryName() {
        return directoryName;
    }
    
    public File findFile(String fileName){
        return this.files.get(fileName);
    }
    
    public Directory findDirectory(String dirName){
        return this.directories.get(dirName);
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public HashMap<String,Directory> getDirectories() {
        return directories;
    }

    public HashMap<String,File> getFiles() {
        return files;
    }
    
    public void setFather(Directory father){
        this.father = father;
    }
    
    public Directory getFather(){
        return this.father;
    }

    @Override
    public String toString() {
        return "Node{" +
                "directoryName='" + directoryName + '\'' +
                ", directories=" + directories +
                ", files=" + files +
                '}';
    }
}
