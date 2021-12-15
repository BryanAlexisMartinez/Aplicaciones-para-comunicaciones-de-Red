import java.net.*;
import java.io.*;
public class SEcoTCPB {
   public static void main(String[] args){
        try{
            // Se crea el server socket
            ServerSocket s = new ServerSocket(4321);
            System.out.println("Esperando cliente ...");
            // Iniciamos el ciclo infinito donde constantemente se recibiran conecciones 
            for(;;){
                // Tenemos un bloqueo, en el momento que llegue una conexión continua el programa
                Socket cl = s.accept();
                //getInetAddress Obtiene la direccion          getPort obtiene el puerto desde donde se conecta el cliente
                System.out.println("Conexión establecida desde "+  cl.getInetAddress()+":" + cl.getPort());
                String mensaje ="Hola mundo";
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
                // Se envía el mensaje
                pw.println(mensaje);
                // Se limpia le flujo
                pw.flush();
                //cerramos el socket
                pw.close();
                cl.close();
            }//for
}catch(Exception e){ // Manejo de excepciones
            e.printStackTrace();
        }//catch
    }//main
}

