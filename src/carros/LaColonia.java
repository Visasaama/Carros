/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package binario;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Gotcha
 */
public class LaColonia {
    public static void main(String[] args) {
        SuperMarket sm = new SuperMarket();
        int op=0;
        Scanner lea = new Scanner(System.in);
        
        do{
            System.out.println("\n\n1- Agregar Producto");
            System.out.println("2- Inventario");
            System.out.println("3- Facturar");
            System.out.println("4- Comprar Inventario");
            System.out.println("5- Imprimir factura");
            System.out.println("6- Total Vendido");
            System.out.println("7- Producto MAS vendido");
            System.out.println("8- SALIR");
            System.out.println("\nEscoja opcion: ");
            
            try{
                op = lea.nextInt();

                switch(op){
                    case 1:
                        sm.addProducto();
                        break;
                    case 2:
                        sm.inventario();
                        break;
                    case 3:
                        sm.generarCompra();
                    case 4:
                        int cod = lea.nextInt();
                        int cant = lea.nextInt();
                        sm.comprar(cod, cant);
                        break;
                    case 5:
                        /*
                         * Pedir el codigo de la factura
                         * SI EXISTE LA FACTURA ustedes imprimen
                         * sus datos con el formato:
                         * CODIGO - FECHA
                         * NOMBRE CLIENTE
                         * *nombre de prod(+) - cant prod - precio unit - st(+)
                         * Subtotal
                         * impuesto
                         * descuento
                         * total a pagar(+)
                         */
                        break;
                    case 6:
                        /*
                         * Imprime el total de ventas generadas en el 
                         * sistema historicamente.
                         */
                    case 7:
                        /*
                         * Imprime los datos del productos que MAS SE HA
                         * vendido
                         */
                }
            }
            catch(IOException e){
                System.out.println("Error de Archivo: " + e.getMessage());
            }
            catch(InputMismatchException e){
                System.out.println("Dato Incorrecto");
                lea.next();
            }
            catch(Exception e){
                System.out.println("ERROR: " + e.getMessage());
            }
        }while(op !=7);
        
    }
}
