/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tree;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author irsac
 */
public class File {
    private String fileName;
    private String extension;
    private String content;
    private Date creationDate;
    private Date modificationDate;
    private int size;
    private ArrayList<Integer> registrosBase;

    public File(String name, String extension, String content, int size){
        this.fileName = name;
        this.extension = extension;
        this.content = content;
        this.creationDate = new Date();
        this.modificationDate = new Date();
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

    /**
     * @return the extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * @param extension the extension to set
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    private void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return the modificationDate
     */
    public Date getModificationDate() {
        return modificationDate;
    }

    /**
     * @param modificationDate the modificationDate to set
     */
    public void setModificationDate() {
        this.modificationDate = new Date();
    }

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }
    
    public String toString(){
        String str = "";
        str += "File Name: " + this.fileName + "\n";
        str += "Extension: " + this.extension + "\n";
        str += "Creation Date: " + this.creationDate + "\n";
        str += "Last modification Date: " + this.modificationDate + "\n";
        str += "Size: " + String.valueOf(this.size) +"\n";
        return str;
    }
}
