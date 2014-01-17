/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Archivos;

import java.io.File;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author Gotcha
 */
public class DirMethod {
    public static void main(String[] args) {
        Scanner lea = new Scanner(System.in);
        
        do{
            System.out.println("Direccion del dir: ");
            String dirpath = lea.next();
            
            File dir = new File(dirpath);
            
            if(dir.isDirectory()){
                File elements[] = dir.listFiles();
                int files=0, dirs=0, suma=0;
                
                for(File alpaca : elements ){
                    if(!alpaca.isHidden()){
                        //ultima fecha
                        Date date = new Date(alpaca.lastModified());
                        System.out.print(date);
                        //DIR O BYTES
                        if(alpaca.isDirectory()){
                            System.out.print("\t<DIR>\t");
                            dirs++;
                        }
                        else if(alpaca.isFile()){
                            System.out.println("\t     " +
                                    alpaca.length() + " ");
                            files++;
                            suma += alpaca.length();
                        }
                        //nombre
                        System.out.println(alpaca.getName());
                    }
                }
                System.out.println(files + " File(s)");
                System.out.println(dirs + " Dir(s)");
                System.out.println(suma + " bytes");
                System.out.println(dir.getFreeSpace() +
                        " bytes free");
                
                
                printdir(dir,"");
            }
            
            
        }while(true);
    }

    private static void printdir(File dir, String tab) {
        System.out.println(tab + dir.getName());
        if( dir.isDirectory() ){
            File files[] = dir.listFiles();
            for(File fi : files){
                printdir(fi,tab+"\t");
            }
        }
    }
}
