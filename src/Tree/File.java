package Tree;

import java.util.ArrayList;

public class File {
    private String fileName;
    private String content;
    private ArrayList<Integer> registrosBase;

    public File(String name){
        this.fileName = name;
        this.registrosBase = new ArrayList<>();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<Integer> getRegistrosBase() {
        return registrosBase;
    }

    public void setRegistrosBase(ArrayList<Integer> registrosBase) {
        this.registrosBase = registrosBase;
    }
}
