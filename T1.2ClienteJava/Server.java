import java.net.*;
import java.io.*;

// 005 - Server

public class Server {
    public static void main(String[] args) {
        try {
            
            ServerSocket server = new ServerSocket(4000);
            
            while(true) {
                System.out.println("Waiting some client...");
                
                Socket conn = server.accept();
                System.out.println("Connection established from " + conn.getInetAddress() + ":" + conn.getPort());
                
                DataInputStream dis = new DataInputStream(conn.getInputStream());
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                
                long boleta = dis.readLong();
                String nombre = dis.readUTF();
                int edad = dis.readInt();
                
                System.out.println("Datos del cliente: [" + boleta + ", " + nombre + ", " + edad + "]");
                
                long tamano = 1000;
                byte[] arr = new byte[(int) tamano];
                double valor = 999;
                
                System.out.print("Enviando tamano... ");
                dos.writeLong(tamano);
                dos.flush();
                System.out.print("OK\n");
                
                System.out.print("Enviando array de bytes... ");
                dos.write(arr);
                dos.flush();
                System.out.print("OK\n");
                
                System.out.print("Enviando valor... ");
                dos.writeDouble(valor);
                dos.flush();
                System.out.print("OK\n");
                
                System.out.print("Recibiendo valor... ");
                double valorCliente = dis.readDouble();
                System.out.print("OK\n");
                
                System.out.println("Mi valor: " + valor +", cliente: " + valorCliente);
                
                
                System.out.print("Enviando resultado: " + (valor == valorCliente ? "true" : "false - "));
                dos.writeBoolean(valor == valorCliente);
                dos.flush();
                System.out.print("OK\n");
                
                dis.close();
                dos.close();
            }

            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}