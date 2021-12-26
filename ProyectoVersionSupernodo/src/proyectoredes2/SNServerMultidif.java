/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoredes2;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SNServerMultidif implements Runnable{
    private final String IP = "228.1.1.10";
    int puerto=2828;
    int puertoRMI;
    int intervaloT=5000;
    Thread hilo;
    int disponibles=2;
    SNServerMultidif(int port){
        puertoRMI=port;
        hilo = new Thread( this );
        
        hilo.start();
        
    }
    @Override
    public void run() {
        boolean bandera=false;
        SocketAddress remoto = new InetSocketAddress("228.1.1.10",puerto);
        try{
            DatagramChannel canal = DatagramChannel.open(StandardProtocolFamily.INET);
            canal.configureBlocking(false);
            NetworkInterface ni = NetworkInterface.getByName("eth3");
            canal.setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);
            canal.connect(remoto);
            Selector selector = Selector.open();
            canal.register(selector,SelectionKey.OP_WRITE);
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
            
            while(true){
                selector.select(5000); //espera 5 segundos por la conexión
                Set sk = selector.selectedKeys();
                if(sk.isEmpty() && disponibles == 5110 || bandera){
                    canal.close();
                    break;
                }else{
                    Iterator it = sk.iterator();
                    while(it.hasNext()){
                        SelectionKey key = (SelectionKey)it.next();
                        it.remove();
                        if(key.isWritable()){
                            sendMessage(canal,disponibles+":"+puertoRMI,remoto);
                            
//                            buffer.clear();
//                            buffer.putInt(disponibles);
//                            buffer.flip();
                            Thread.sleep(5000);
                            System.out.println("imprimo "+disponibles+"   "+ puertoRMI);
                            canal.write(buffer);
                            
                        }//if
                    }//while
                }//else
            }//while
        }catch(Exception e){
            System.err.println(e);
        }//catch
        
    }
    public static void sendMessage(DatagramChannel client, String msg, SocketAddress serverAddress) throws IOException {
   // ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
     ByteBuffer buffer2 = ByteBuffer.wrap(msg.getBytes());
    client.send(buffer2, serverAddress);
}

    void menos1() {
        disponibles--;
    }

    void mas1() {
        disponibles++; 
    }
    
}
