package Tree;

import java.util.ArrayList;

public class Node {
    private String fileName;
    private ArrayList<Node> directories;
    private ArrayList<String> files;

    public Node(String pName){
        this.fileName = pName;
        this.directories = new ArrayList<>();
        this.files = new ArrayList<>();
    }

    public boolean isEmpty(){
        return directories.isEmpty() && files.isEmpty();
    }

    public void addDirectory(Node directory){
        if(!directories.contains(directory) && this != directory){
            directories.add(directory);
        }
    }

    public void deleteDirectory(Node directory){
        for (Node d:directories) {
            if(d == directory){
                if(d.isEmpty()){
                    directories.remove(d);
                }else{
                    d.deleteAll();
                }
            }
        }
    }

    public void addFile(String file){
        if(!files.contains(file)){
            files.add(file);
        }
    }

    public void deleteFile(String file){
        files.remove(file);
    }

    private void deleteAll(){
        Node directory;
        while(!directories.isEmpty()){
            directory = directories.get(0);
            if(directory.isEmpty()){
                directories.remove(0);
            }else{
                directory.deleteAll();
                directories.remove(0);
            }
        }
        files.clear();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ArrayList<Node> getDirectories() {
        return directories;
    }

    public ArrayList<String> getFiles() {
        return files;
    }

    @Override
    public String toString() {
        return "Node{" +
                "fileName='" + fileName + '\'' +
                ", directories=" + directories +
                ", files=" + files +
                '}';
    }
}
