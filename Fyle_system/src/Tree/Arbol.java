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
    
    public boolean createDirectory(String dirName){
        Directory dir = new Directory(dirName, actualDirectory);
        return actualDirectory.addDirectory(dir);
    }
    
    public boolean createFile(String fileName, String extension, String content){
        File file = new File(fileName, extension, content, content.length());
        return actualDirectory.addFile(file);
    }
    
    public void changeDirectory(){}
    
    public void modFile(String fileName, String content){
        File file = actualDirectory.findFile(fileName);
        file.setContent(content);
        file.setModificationDate();
        file.setSize(content.length());
    }
    
    public String fileProperties(String fileName){
        File file = actualDirectory.findFile(fileName);
        if(file != null){
            return file.toString();
        }
        return "File not fount";
    }
    
    public String fileContent(String fileName){
        File file = actualDirectory.findFile(fileName);
        if(file != null){
            return file.getContent();
        }
        return "File not fount";
    }
    
    public String find(String name){
        return "";
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
}
