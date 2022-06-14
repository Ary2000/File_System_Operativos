package Tree;

import java.util.HashMap;
import java.util.Map;

public class Node {
    private String directoryName;
    private HashMap<String,Node> directories;
    private HashMap<String,File> files;

    public Node(String pName){
        this.directoryName = pName;
        this.directories = new HashMap<>();
        this.files = new HashMap<>();
    }

    public boolean isEmpty(){
        return directories.isEmpty() && files.isEmpty();
    }

    public void addDirectory(Node directory){
        if(!directories.containsKey(directory.getDirectoryName()) && this != directory){
            directories.put(directory.getDirectoryName(),directory);
        }
    }

    public void deleteDirectory(Node directory){
        Node d = directories.get(directory.getDirectoryName());
        if(d == directory){
            if(d.isEmpty())
                this.directories.remove(directory.getDirectoryName());
            else{
                d.deleteAll();
                this.directories.remove(directory.getDirectoryName());
            }
        }
    }

    public void addFile(File file){
        if(!files.containsKey(file.getFileName())){
            files.put(file.getFileName(),file);
        }
    }

    public void deleteFile(File file){
        files.remove(file.getFileName());
    }

    private void deleteAll(){
        for(Map.Entry directoryEntry: directories.entrySet()){
            String dName = (String)directoryEntry.getKey();
            Node directory = (Node) directoryEntry.getValue();
            directory.deleteAll();
            directories.remove(dName);
        }
        files.clear();
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public boolean renameFile(String fileName, String newName){
        File file = files.get(fileName);
        if(file != null){
            files.remove(fileName);
            file.setFileName(newName);
            addFile(file);
            return true;
        }
        return false;
    }

    public boolean renameDirectory(String directoryName, String newName){
        Node directory = directories.get(directoryName);
        if(directory != null){
            files.remove(directoryName);
            directory.setDirectoryName(newName);
            addDirectory(directory);
            return true;
        }
        return false;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public HashMap<String,Node> getDirectories() {
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
