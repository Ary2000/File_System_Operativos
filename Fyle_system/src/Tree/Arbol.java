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
    }
}
