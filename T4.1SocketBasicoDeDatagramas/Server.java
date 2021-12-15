import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
public class Server {
 public static void main(String[] args) throws InterruptedException {
 try {
 
 DatagramSocket socket = new DatagramSocket(3500);
 socket.setReceiveBufferSize(20);
 socket.setSendBufferSize(20);
 System.out.println("Server listen at port: 3500");
 
 while(true) {
 System.out.println("Waiting for a client...");
 
 DatagramPacket packet = new DatagramPacket(new byte[20], 20);
 socket.receive(packet);
 
 DataInputStream dis = new DataInputStream(new
ByteArrayInputStream(packet.getData()));
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 DataOutputStream dos = new DataOutputStream(baos);
 
 int counter = dis.readInt();
 System.out.println("packets: " + counter);
 
 String message = "";
 
 for(int i = 0; i < counter; ++i) {
 packet = new DatagramPacket(new byte[20], 20);
 socket.receive(packet);
 //dis = new DataInputStream(new ByteArrayInputStream(packet.getData());
 String partial = new String(packet.getData());
 message += partial;
 System.out.println(""+partial);
 
 }
 message = message.substring(2);
 System.out.println("message: " + message);
 message = "ECO " + message;
 dos.writeUTF(message);
 dos.flush();
 
 byte[] message_buffer = baos.toByteArray();
 ArrayList<byte[]> buffers = new ArrayList<>();
 
 int i = 0;
 while(i < message_buffer.length && message_buffer[i] != 0x0A) { 
 byte[] buffer = new byte[20];
 for(int j = 0; j < buffer.length; ++j) {
 if(i < message_buffer.length) {
 buffer[j] = message_buffer[i++];
 } else break;
 }
 buffers.add(buffer);
 }
 
 baos.reset();
 dos.writeInt(buffers.size());
 dos.flush();
 Thread.sleep(10);
 byte[] counter_buffer = baos.toByteArray();
 DatagramPacket packet1 = new DatagramPacket(counter_buffer,
counter_buffer.length, packet.getAddress(), packet.getPort());
 socket.send(packet1);
 for(byte[] buffer: buffers) {
 DatagramPacket packet2 = new DatagramPacket(buffer, buffer.length,
packet.getAddress(), packet.getPort());
 socket.send(packet2);
 Thread.sleep(10);
 }
 
 dos.close();
 baos.close();
 dis.close();
 }
 } catch(IOException e) {
 e.printStackTrace();
 }
 }
}
