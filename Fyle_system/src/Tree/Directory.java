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

    public boolean isEmpty(){
        return directories.isEmpty() && files.isEmpty();
    }
    
    public boolean directoryExist(String directoryName){
        return directories.containsKey(directoryName);
    }
    
    public boolean fileExist(String fileName){
        return files.containsKey(fileName);
    }

    public void addDirectory(Directory directory){
        if(!directoryExist(directory.getDirectoryName()) && this != directory){
            directories.put(directory.getDirectoryName(),directory);
        }
    }

    public void deleteDirectory(Directory directory){
        Directory d = directories.get(directory.getDirectoryName());
        if(d == directory){
            if(d.isEmpty())
                this.directories.remove(directory.getDirectoryName());
            else{
                d.deleteAll();
                this.directories.remove(directory.getDirectoryName());
            }
        }
    }

    private void deleteAll(){
        for(Map.Entry directoryEntry: directories.entrySet()){
            String dName = (String)directoryEntry.getKey();
            Directory directory = (Directory) directoryEntry.getValue();
            directory.deleteAll();
            directories.remove(dName);//corregir?
        }
        files.clear();
    }

    public void addFile(File file){
        if(!fileExist(file.getFileName()+file.getExtension())){
            files.put(file.getFileName()+file.getExtension(),file);
        }
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

    public boolean renameFile(String fileName, String newName){
        File file = files.get(fileName);
        if(file != null && !fileExist(newName)){
            files.remove(fileName);
            file.setFileName(newName);
            addFile(file);
            return true;
        }
        return false;
    }

    public boolean renameDirectory(String directoryName, String newName){
        Directory directory = directories.get(directoryName);
        if(directory != null && !directoryExist(newName)){
            files.remove(directoryName);
            directory.setDirectoryName(newName);
            addDirectory(directory);
            return true;
        }
        return false;
    }

    private void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public HashMap<String,Directory> getDirectories() {
        return directories;
    }

    public HashMap<String,File> getFiles() {
        return files;
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
