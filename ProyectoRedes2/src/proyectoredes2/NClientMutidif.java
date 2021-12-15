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

public class NClientMutidif implements Runnable {

    ArrayList<String> Serverdisp = new ArrayList<>();
    ArrayList<String> disponibles = new ArrayList<>();
    ArrayList<String> ServerPortRMI = new ArrayList<>();
    Thread hilo;
    int PUERTO = 2828;
    int puertoRMI = 0;
    Cronometro wait;
    int indiceAleatorio;

    NClientMutidif() {
        hilo = new Thread(this);
        hilo.start();
    }

    @Override
    public void run() {
        wait = new Cronometro(29);

        try {
            DatagramChannel canal = DatagramChannel.open(StandardProtocolFamily.INET);
            canal.configureBlocking(false);
            NetworkInterface ni = NetworkInterface.getByName("eth3");
            canal.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            canal.setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);
            InetSocketAddress dirr = new InetSocketAddress(PUERTO);
            InetAddress Group = InetAddress.getByName("228.1.1.10");
            canal.join(Group, ni);

            canal.socket().bind(dirr);
            Selector selector = Selector.open();
            canal.register(selector, SelectionKey.OP_READ);
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
            while (true) {
                if (wait.Segundos > 0) {
                    selector.select(5000);
                    Set sk = selector.selectedKeys();
                    Iterator it = sk.iterator();
                    while (it.hasNext()) {
                        SelectionKey key = (SelectionKey) it.next();
                        it.remove();
                        if (key.isReadable()) {
                            boolean NoEsta = true;
                            buffer.clear();
                            String ClienteS = receiveMessage(canal);
                            System.out.println("" + ClienteS);
                            ClienteS = ClienteS.replace("/", "");
                            String[] Datos = ClienteS.split(":");
                            if (Datos.length > 1) {
                                for (int i = 0; i < ServerPortRMI.size(); i++) {
                                    if (ServerPortRMI.get(i).equals(Datos[1])) {
                                        NoEsta = false;
                                        Serverdisp.set(i, Datos[0]);
                                    }
                                }
                                if (NoEsta) {
                                    System.out.println("agregado");
                                    ServerPortRMI.add(Datos[1]);
                                    Serverdisp.add(Datos[0]);
                                }
                            }
                        }
                    }
                } else {
                    if (puertoRMI == 0) {
                        for (int i = 0; i < Serverdisp.size(); i++) {
                            if (Integer.parseInt(Serverdisp.get(i)) > 0) {
                                disponibles.add(ServerPortRMI.get(i));
                            }
                        }
                        indiceAleatorio = aleatorio(0, disponibles.size() - 1);
                        System.out.println(" indice   " + indiceAleatorio);
                        puertoRMI = Integer.parseInt(disponibles.get(indiceAleatorio));
                        System.out.println("" + puertoRMI);
                    }
                }
            }//while
        } catch (IOException e) {
            System.err.println(e);
        }//catch
    }

    public static int aleatorio(int minimo, int maximo) {
        // nextInt regresa en rango pero con límite superior exclusivo, por eso sumamos 1
        return ThreadLocalRandom.current().nextInt(minimo, maximo + 1);
    }

    public static String receiveMessage(DatagramChannel server) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        SocketAddress remoteAdd = server.receive(buffer);
        String message = extractMessage(buffer);
        message = message;
        return message;
    }

    private static String extractMessage(ByteBuffer buffer) {
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        String msg = new String(bytes);
        return msg;
    }
}