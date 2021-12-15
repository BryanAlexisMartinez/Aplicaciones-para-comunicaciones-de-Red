import java.net.*;
import java.io.*;
public class Client {
    public static void main(String[] args) {
        while(true) {
            try {           
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));              
                String host = "201.145.113.145";
                System.out.println("Host: " + host);               
                int port = 1234;
                System.out.println("Port: " + port);                
                System.out.println("Connecting...");
                Socket conn = new Socket(host, port);
                System.out.println("Connected to " + conn.getInetAddress() + ":" + conn.getPort());              
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                DataInputStream dis = new DataInputStream(conn.getInputStream());           
                long boleta = 2019630497;
                String nombre = "Bryan Alexis Martínez Alvarado";
                int edad = 21;                
                System.out.println("Enviando boleta...");
                dos.writeLong(boleta);
                dos.flush();               
                System.out.println("Enviando nombre...");
                dos.writeUTF(nombre);
                dos.flush();                
                System.out.println("Enviando edad...");
                dos.writeInt(edad);
                dos.flush();                
                System.out.println("Recibiendo tamano...");
                long tamano = dis.readLong();                
                System.out.println("Recibiendo arr...");
                byte[] arr = dis.readNBytes((int) tamano);                
                System.out.println("Recibiendo valor...");
                double valor = dis.readDouble();                
                System.out.println("Datos: [" + tamano + ", arr_len " + arr.length + ", " + valor + "]");               
                dos.writeDouble(valor);
                dos.flush();               
                boolean resultado = dis.readBoolean();                
                System.out.println("Resultado: " + resultado);                
                dis.close();
                dos.close();
                br.close();
                conn.close();              
                break;
            } catch(Exception e) {
                System.out.println("Error in process\n");
            }
        }
    }
}