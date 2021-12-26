/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoredes2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zorro
 */
public class NServFlujo implements Runnable {

    Thread hilo;
    int port;
    File carpeta;
    boolean conectado = false;

    NServFlujo(int puerto, File carpeta) {
        this.carpeta = carpeta;
        port = puerto;
        hilo = new Thread(this);
        hilo.start();
    }

    public boolean isConectado() {
        return conectado;
    }

    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }

    @Override
    public void run() {//envia
        try {
            ServerSocket s = new ServerSocket(port);
            System.out.println("Servidor preparadado.");
            for (;;) {
                Socket cl = s.accept();
                System.out.println("Conexión establecida desde" + cl.getInetAddress() + ":" + cl.getPort());
                DataInputStream entrada = new DataInputStream(cl.getInputStream());
                DataOutputStream salida = new DataOutputStream(cl.getOutputStream());
                DataInputStream fis;
                String nombre = entrada.readUTF();//leer nombre del archivo
                //String detalles=nombre.split("")
                if (nombre.equals("Desconexion()r86d8qRTpNVGXPC")) {
                    setConectado(false);
                } else {
                    File buscado = buscar(nombre, carpeta);
                    //String MD5= entrada.readUTF();
                    long recibidos, tam;
                    String archivodir;
                    archivodir = buscado.getAbsolutePath(); //Dirección
                    nombre = buscado.getName(); //Nombre
                    tam = buscado.length();  //Tamaño
                    fis = new DataInputStream(new FileInputStream(archivodir));
                    //salida.writeUTF(nombre); //se envia nombre del archivo
                    System.out.println("" + nombre);
                    salida.flush();
                    System.out.println("Enviando '" + nombre + "', de tamaño: " + tam + "Bytes");
                    salida.writeLong(tam);
                    salida.flush();
                    byte[] b = new byte[1024];
                    long enviados = 0;
                    int n;
                    while (enviados < tam) {
                        n = fis.read(b);
                        salida.write(b, 0, n);//enviamos bytes tamaño del buffer
                        salida.flush();
                        enviados = enviados + n;
                        System.out.print("Enviados: " + enviados + " de " + tam + "\r");
                    }
                    if (entrada.readBoolean()) {//todo bien
                        System.out.println(nombre + " enviado exitosamente\n");
                    } else {
                        System.out.println("Hubo un error al enviar " + nombre + "\n");
                    }
                    fis.close();//cerrar flujo para archivo individual
                    //}
                    entrada.close();//cerrar flujos
                    salida.close();
                    cl.close();
                    System.out.println("Conexión con el cliente terminada!");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Nodo.class.getName()).log(Level.SEVERE, null, ex);
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    File buscar(String archivoABuscar, File directorio) {
        System.out.println("" + archivoABuscar);
        System.out.println(directorio.getAbsolutePath() + "   " + directorio);
        File[] archivos = directorio.listFiles();
        for (File archivo : archivos) {
            if (archivo.getName().equals(archivoABuscar)) {
                return archivo;
            }
            if (archivo.isDirectory()) {
                File resultadoRecursion = buscar(archivoABuscar, archivo);
                if (resultadoRecursion != null) {
                    return resultadoRecursion;
                }
            }
        }
        return null;
    }

}
