/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tree;

import java.util.ArrayList;

/**
 *
 * @author irsac
 */
public class File {
    private String fileName;
    private String extencion;
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
