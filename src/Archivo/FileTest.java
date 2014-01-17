/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ClaseFile;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Gotcha
 */
public class FileTest {
    public static void main(String[] args) {
        Scanner lea = new Scanner(System.in);
        System.out.println("Ingrese dir abstracta o absoluta: ");
        File file = new File(lea.next());
        char op;
        
        if( file.exists() ){
            System.out.println("YA EXISTE");
            printinfo(file);
            System.out.println("Lo quiere borrar?: (s/n) ");
            op = lea.next().charAt(0);
            
            if( Character.toUpperCase(op) == 'S'){
                if( file.delete() )
                    System.out.println("Removido exitosamente");
            }
        }
        else{
            System.out.println("NO EXISTE");
            
            System.out.println("Quiere archivo o directio (a/d): ");
            op = lea.next().charAt(0);
            
            if( Character.toUpperCase(op) == 'A'){
               try{
                  if( file.createNewFile() )
                       System.out.println("Creacion de archivo exitosa");
                  else
                       System.out.println("No se pudo crear archivo");
               }
               catch(IOException e){
                   System.out.println("Error io: " + e.getMessage());
               }
            }
            else{
                if( file.mkdirs() )
                    System.out.println("Creacion de directorio exitosa");
                else
                    System.out.println("No se pudo crear directorio");
            }
        }
        
    }

    private static void printinfo(File file) {
        System.out.println("DATOS DEL ARCHIVO");
        System.out.println("Nombre: " + file.getName());
        System.out.println("Path: " + file.getPath());
        System.out.println("Path Absoluto: " + file.getAbsolutePath());
        System.out.println("Directio pade: " + file.getAbsoluteFile().getParent());
        
        if( file.isAbsolute() )
            System.out.println("Esta instanciado con una dir. abosuluta");
        else
            System.out.println("Esta instanciado con una dir abstracta");
        
        
        if( file.isFile())
            System.out.println("ES UN ARCHIVO");
        else if(file.isDirectory())
            System.out.println("ES UN DIRECTORIO");
        
        if( file.isHidden())
            System.out.println("ESTA ESCONDIDO");
        
        System.out.println("Bytes: " + file.length());
        
    }
}
