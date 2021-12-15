package Server;
import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Server {
 @SuppressWarnings("SleepWhileInLoop")
 public static void main(String[] args) {
 InetAddress group;
 try {
 MulticastSocket socket = new MulticastSocket(4000);
 socket.setReuseAddress(true);
 socket.setTimeToLive(1);
 group = InetAddress.getByName("228.1.1.1");
 socket.joinGroup(group);

 Runnable r1 = () -> {
 while(true) {
 String message = "hello, im a server 1";
byte[] b = message.getBytes();
 DatagramPacket packet = new DatagramPacket(b, b.length, group, 4000);
 try {
 socket.send(packet);
System.out.println("Sending message: '" + message + "' with time to live: " + socket.getTimeToLive());
 } catch (IOException ex) {

Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
 }
try {
 Thread.sleep(3000);
 } catch (InterruptedException ex) {

Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
 }
 }
 };

 Thread t1 = new Thread(r1);
 t1.start();

 Runnable r2 = () -> {
 while (true) {
 DatagramPacket packet = new DatagramPacket(new byte[512], 512);
 try {
 socket.receive(packet);
String message_received = new String(packet.getData());
 System.out.println("'" + message_received + "', from: " + packet.getAddress() + ":" + packet.getPort());
 } catch (IOException ex) {

Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
 }
 }
 };

 Thread t2 = new Thread(r2);
 t2.start();

 } catch (IOException e) {}
 }
}