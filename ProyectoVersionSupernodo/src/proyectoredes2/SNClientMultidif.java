/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoredes2;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;


public class SNClientMultidif implements Runnable{
ArrayList<Cronometro> SuperNodos =new ArrayList<Cronometro>();
Thread hilo;
private final String IP = "228.1.1.10";
int PUERTO=2828;
int PUERTOrmi;
    SNClientMultidif(int puerto){
        PUERTOrmi=puerto;
        
        hilo = new Thread( this );
        hilo.start();
        
    }
            
    @Override
    public void run() {
        //wait=new Cronometro();
        
        
        try{
            DatagramChannel canal = DatagramChannel.open(StandardProtocolFamily.INET);
            canal.configureBlocking(false);
            NetworkInterface ni = NetworkInterface.getByName("eth3");
            canal.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            canal.setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);
//            DatagramSocket socket = canal.socket();
            InetSocketAddress dirr =new InetSocketAddress(PUERTO);
            InetAddress Group= InetAddress.getByName("228.1.1.10");
            //canal.setReuseAddress(true);
            //SocketAddress dir = new InetSocketAddress(port);
            canal.join(Group, ni);
            
            canal.socket().bind(dirr);
            Selector selector = Selector.open();
            canal.register(selector,SelectionKey.OP_READ);
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
            while(true){
                selector.select(5000);
                Set sk = selector.selectedKeys();
                Iterator it = sk.iterator();
                while(it.hasNext()){
                    SelectionKey key = (SelectionKey)it.next();
                    it.remove();
                    if(key.isReadable()){
                        boolean NoEsta=true;
                        buffer.clear();
                        
//                        SocketAddress client = canal.receive(buffer);
//                        buffer.flip();
//                        int eco = buffer.getInt();
                       //0.disponible-1.puertoRMI
                        String ClienteS=receiveMessage(canal);
                        System.out.println(""+ClienteS);
                        ClienteS=ClienteS.replace("/", "");
                        String[] Datos=ClienteS.split(":");
                        if(Datos.length>1){
                            //System.out.println(""+Datos[0]+""+Datos[1]);
                            for(int i=0;i<SuperNodos.size();i++){
                                if(SuperNodos.get(i).contenido.equals(Datos[1])){
                                    NoEsta=false;
                                    System.out.println("superNodo "+Datos[1]+" Vivo");
                                    SuperNodos.get(i).Restablecer();
                                }

                            }

                            if (NoEsta){
                                System.out.println("superNodo "+Datos[1]+" registtrado");
                                SuperNodos.add(new Cronometro (Datos[1],30));
                            }
                        }
                    }
                }
            }//while
            }catch(IOException e){
                System.err.println(e);
            }//catch
    }
    String tabla() {
        String result="<tr>\n" +
"    <td>Puerto</td><td>restante</td>" +
"  </tr>";
        for(int i=0;i<SuperNodos.size();i++){
            if (SuperNodos.get(i)!=null&&SuperNodos.get(i).Segundos>0)//&&!SuperNodos.get(i).contenido.equals(PUERTOrmi+""))
         result=result+"<tr><td>"+SuperNodos.get(i).contenido +"</td>"+ "<td>"+SuperNodos.get(i).Segundos +"</td></tr>" ; 

        }
        
        return "<table>"+result+"</table>";
    }
    public static int aleatorio(int minimo, int maximo) {
        // nextInt regresa en rango pero con límite superior exclusivo, por eso sumamos 1
        return ThreadLocalRandom.current().nextInt(minimo, maximo + 1);
    }
    public static String receiveMessage(DatagramChannel server) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        SocketAddress remoteAdd = server.receive(buffer);
        String message = extractMessage(buffer);
        
        return message;
        
    }
    private static String extractMessage(ByteBuffer buffer) {
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        String msg = new String(bytes);
    
    return msg;
    }

    String nodosVivos() {
        String result="";
        for(int i=0;i<SuperNodos.size();i++){
            if (SuperNodos.get(i)!=null&&SuperNodos.get(i).Segundos>0&&!SuperNodos.get(i).contenido.equals(PUERTOrmi+""))
           result=result+SuperNodos.get(i).contenido +"/" ; 

        }
        return result;
    }
    
}
