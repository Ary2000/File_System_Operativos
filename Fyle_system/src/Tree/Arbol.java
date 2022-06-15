/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tree;

/**
 *
 * @author irsac
 */
public class Arbol {
    private final Directory root;
    private Directory actualDirectory;

    
    public Arbol(){
        root = new Directory("File System");
        this.actualDirectory = root;
    }
    
    public boolean findDirectory(String dirName){
        return this.actualDirectory.directoryExist(dirName);
    }
    
    public boolean findFile(String fileName){
        return this.actualDirectory.fileExist(fileName);
    }
    
    public boolean createDirectory(String dirName, Directory actualDirectory){
        return false;
    }
    
    public boolean createFile(String fileName, String extension, String content){
        return false;
    }
    
    public void goToRoot(){
        this.actualDirectory = root;
    }
}
