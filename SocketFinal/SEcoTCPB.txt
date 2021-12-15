import java.net.*;
import java.io.*;
public class SEcoTCPB {
   public static void main(String[] args){
        try{
            // Se crea el server socket
            ServerSocket s = new ServerSocket(2345);
            System.out.println("Esperando cliente ...");
            // Iniciamos el ciclo infinito donde constantemente se recibiran conecciones 
            for(;;){
                // Tenemos un bloqueo, en el momento que llegue una conexión continua el programa
                Socket cl = s.accept();
                //getInetAddress Obtiene la direccion          getPort obtiene el puerto desde donde se conecta el cliente
                System.out.println("Conexión establecida desde "+  cl.getInetAddress()+":" + cl.getPort());
		BufferedReader br2 = new BufferedReader (new InputStreamReader(cl.getInputStream()));
		String frase = (br2.readLine());
		System.out.println("Mensaje recibido del cliente: " + frase);
		PrintWriter pw = new PrintWriter (new OutputStreamWriter(cl.getOutputStream()));

                pw.println("ECO:" + frase);
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
