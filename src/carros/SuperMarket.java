/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package binario;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author Gotcha
 */
public class SuperMarket {
    private RandomAccessFile productos;
    private RandomAccessFile codigos;
    private String dirfactura = "supermarket/facturas";
    private Scanner lea = new Scanner(System.in);
    
    public SuperMarket(){
        creardirs();
        try{
            productos = new RandomAccessFile("supermarket/productos.smk", "rw");
            codigos = new RandomAccessFile("supermarket/codigos.smk", "rw");
            initcodsfile();
        }
        catch(IOException io){
            System.out.println("ERROR: " + io.getMessage());
        }
    }

    private void creardirs() {
        File f = new File(dirfactura);
        f.mkdirs();
    }

    private void initcodsfile() throws IOException{
        if(codigos.length() == 0){
            codigos.writeInt(1);
            codigos.writeInt(1);
        }
    }
    
    private int getCodigo(int offset)throws IOException{
        codigos.seek(offset);
        int codigo = codigos.readInt();
        codigos.seek(offset);
        codigos.writeInt(codigo+1);
        return codigo;
    }
    
    private int getNewCodProducto()throws IOException{
        return getCodigo(0);
    }
    
    private int getNewCodFactura()throws IOException{
        return getCodigo(4);
    }
   
    public void addProducto()throws IOException{
        productos.seek( productos.length() );
        
        //nombre
        System.out.println("Nombre: ");
        String nom = lea.next();
        //precio venta
        System.out.println("Precio Venta: ");
        double precv = lea.nextDouble();
        //precio compra
        System.out.println("Precio Compra: ");
        double precc = lea.nextDouble();
        //cantidad
        System.out.println("Cantidad: ");
        int cant = lea.nextInt();
        //tipo
        System.out.println("Tipo: ");
        TipoProducto tp = TipoProducto.valueOf(lea.next().toUpperCase());
        
        
        //a escribir
        productos.writeInt(getNewCodProducto());
        productos.writeUTF(nom);
        productos.writeDouble(precv);
        productos.writeDouble(precc);
        productos.writeInt(cant);
        productos.writeUTF(tp.name());
    }
    
    public void inventario()throws IOException{
        productos.seek(0);
        System.out.println("\n\nINVENTARIO\n----------");
        while(productos.getFilePointer() < productos.length()){
            int cod = productos.readInt();
            String n = productos.readUTF();
            double pv = productos.readDouble();
            double pc = productos.readDouble();
            int cant = productos.readInt();
            String tipo = productos.readUTF();
            
            System.out.printf("%d- %s Venta: Lps. %.2f Compra %.2f Cantidad: %d - %s\n",
                    cod,n,pv,pc,cant,tipo);
        }
    }
    
    public boolean search(int cod)throws IOException{
        productos.seek(0);
        
        while(productos.getFilePointer() < productos.length()){
            int code = productos.readInt();
            long pos = productos.getFilePointer();
            productos.readUTF();
            productos.seek(productos.getFilePointer()+16);
            int cant = productos.readInt();
            productos.readUTF();
            
            if(code==cod && cant > 0){
                productos.seek(pos);
                return true;
            }
        }
        
        return false;
    }
    
    public void generarCompra()throws IOException{
        ArrayList<LineItem> items = new ArrayList<>();
        char resp;
        System.out.println("Cliente: ");
        String cli = lea.next();
        double st = 0;
        
        do{
            System.out.println("Codigo producto: ");
            int cp = lea.nextInt();
            
            if(search(cp)){
                System.out.println(productos.readUTF());
                double pv = productos.readDouble();
                productos.readDouble();
                System.out.println("Cantidad: ");
                int cant = lea.nextInt();
                int cap = productos.readInt();
                if(cant <= cap && noEstaEnItems(items,cp)){
                    //continuar
                    st = st + (cant * pv);
                    items.add(new LineItem(cp, cant, pv));
                }
                else
                    System.out.println("SOLO TENEMOS " + cap);
            }
            else{
                System.out.println("Producto no existe o agotado");
            }
            
            System.out.print("Quiere otro? (s/n): ");
            resp = lea.next().charAt(0);
        }while(resp == 's');
        
        if( items.size() > 0)
            crearFactura(cli, st, items);
    }

    private void crearFactura(String cli, double st, ArrayList<LineItem> items)throws IOException {
        int codfact = getNewCodFactura();
        String filename = dirfactura + "/Factura#" + codfact + ".smk";
        RandomAccessFile rFa = new RandomAccessFile(filename,"rw");
        
        //codigo
        rFa.writeInt(codfact);
        //fecha
        rFa.writeLong(new Date().getTime());
        //st
        System.out.println("Subtotal: " + st);
        rFa.writeDouble(st);
        //impuesto
        double imp = st * 0.12;
        System.out.println("Impuesto: " + imp);
        rFa.writeDouble(imp);
        //descuento
        double desc = st > 10000 ? st * 0.04 : 0;
        System.out.println("Descuento: " + desc);
        rFa.writeDouble(desc);
        //line items
        for(LineItem li : items){
            rFa.writeInt(li.codprod);
            rFa.writeInt(li.cantprod);
            rFa.writeDouble(li.precprod);
            rebajarInventario(li.codprod, li.cantprod);
        }
        
        double tp = st + imp - desc;
        System.out.println("Total Pagar: " + tp);
        
    }
    
    

    private void rebajarInventario(int codprod, int cantprod)throws IOException {
        if( search(codprod)){
            productos.readUTF();
            productos.seek(productos.getFilePointer()+16);
            int cant = productos.readInt();
            productos.seek(productos.getFilePointer()-4);
            productos.writeInt(cant - cantprod);
        }
        
    }

    private boolean noEstaEnItems(ArrayList<LineItem> items, int cp) {
        for(LineItem li : items){
            if( li.codprod == cp ){
                System.out.println("Producto ya esta agregado");
                return false;
            }
        }
        return true;
    }
    
    public void comprar(int cp, int cant)throws IOException{
        productos.seek(0);
   
        
        while(productos.getFilePointer() < productos.length()){
            int cod = productos.readInt();
            productos.readUTF();
            productos.seek(productos.getFilePointer()+16);
            long pos = productos.getFilePointer();
            int cantp = productos.readInt();
            productos.readUTF();
            
            if(cod == cp){
                productos.seek(pos);
                productos.writeInt(cant+cantp);
                break;
            }
        }
    }
}
