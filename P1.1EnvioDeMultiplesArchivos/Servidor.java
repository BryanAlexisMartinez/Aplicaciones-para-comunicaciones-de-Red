import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor {

    public static void main(String[] args) {
        try {
            // Creamos el socket
            ServerSocket s = new ServerSocket(1234);
            System.out.println("Se inicio el servidor");
            // Iniciamos el ciclo infinito del servidor
            for (;;) {
                // Esperamos una conexión
                Socket cl = s.accept();
                cl.setKeepAlive(true);
                System.out.println("Conexión establecida desde" + cl.getInetAddress() + ":" + cl.getPort());
                DataInputStream dis = new DataInputStream(cl.getInputStream());
                int buffer = dis.readInt();
                System.out.println(buffer);
                cl.setSendBufferSize(buffer);
                cl.setReceiveBufferSize(buffer);
                cl.setTcpNoDelay(dis.readBoolean());

                System.out.println("El tamaño del buffer de envio de paquetes es  :" + cl.getSendBufferSize());
                System.out.println("El tamaño del buffer para recibir paquetes es :" + cl.getReceiveBufferSize());

                int archivos = dis.readInt();
                System.out.println(archivos);
                String[] nombres = new String[archivos]; //Nombres
                long[] tam = new long[archivos];  //Tamaño

                for (int i = 0; i < archivos; i++) {
                    nombres[i] = dis.readUTF();
                    System.out.println(nombres[i]);
                    tam[i] = dis.readLong();
                    System.out.println(tam[i]);
                }

                int i = 0;
                while (i < archivos) {
                    byte[] b = new byte[buffer];
                    DataOutputStream dos = new DataOutputStream(new FileOutputStream(nombres[i]));
                    long recibidos = 0;
                    int n, porcentaje;
                    while (recibidos < tam[i]) {
                        n = dis.readInt();
                        n = dis.read(b,0,n);
                        dos.write(b, 0, n);
                        recibidos = recibidos + n;
                        dos.flush();
                        porcentaje = (int) (recibidos * 100 / tam[i]);
                      //  System.out.print("\nRecibido: " + porcentaje + "%\r");
                    }//While
                    System.out.println("\nRecibimos el archivo:" + nombres[i] +" recibido");
                    dos.close();
                    i++;
                    Thread.sleep(100);
                }
                dis.close();
                cl.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }//catch
    }
}