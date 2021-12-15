import java.net.*;

import javax.sound.midi.Receiver;

import java.io.*;
public class Server {
	public static void main(String [] args){
		try{			
			int PORT = 2000;
			DatagramSocket server = new DatagramSocket(PORT);
			System.out.println("Servidor iniciado en el puerto "+PORT);
			InetAddress LOCAL_HOST = InetAddress.getByName("127.0.0.1");
			int clientPort = PORT;
			
			

			while(true){
				StringBuffer msgBuff = new StringBuffer();	
				int msgLength = 20; //Inicializamos para entrar al while
				int offset = 0;
				
				byte[] msgBytes = new byte[20];
								
				//SE RECIBE EL MENSAJE
				while(msgLength >= 20){
					//SE RECIBEN PAQUETES DE 20 BYTES HASTA QUE LLEGUE EL ULTIMO TRAMO
					DatagramPacket receivePacket = new DatagramPacket(msgBytes,msgBytes.length);
					server.receive(receivePacket);
					String data = new String(receivePacket.getData(),0,receivePacket.getLength());
				    System.out.println("Recibo: "+data+", con offset="+offset);
					msgBuff.insert(offset,data);
					offset += msgLength;
					msgLength = receivePacket.getLength();
					clientPort = receivePacket.getPort();
				}				
				/*DatagramPacket receivePacket = new DatagramPacket(msgBytes,msgBytes.length);
				server.receive(receivePacket);*/				
				System.out.println("Mensaje recibido: "+msgBuff);
				//SE PREPARA PARA ENVIAR RESPUESTA AL CLIENTE, CON ECO
				String eco = "Eco " + msgBuff.toString();
				byte[] bytes = eco.getBytes();
				/*Thread.sleep(2000);
				DatagramPacket sendPacket = new DatagramPacket(bytes,0,bytes.length,LOCAL_HOST,clientPort);
				server.send(sendPacket);*/				
				System.out.println("Vamos a enviar: "+eco);
				msgBytes = eco.getBytes();
				offset = 0;
				int buffLength = msgBytes.length - offset;
				System.out.println("buffLength: "+buffLength);
				//con ayuda del msgBytes (total de bytes del msg) y del offset(que son los bytes recibidos)
				//Se sabe cuando terminan de llegar los paquetes, que es cuando msgBytes.lenght-offset nos de 0
				while(buffLength > 0){
					//MIENTRAS EL BUFFLENGTH SEA MAYOR A 0 Y MENOR A 20 SE 
					if(buffLength < 20){
						System.out.println("Entro aqui con un length="+buffLength); 
						DatagramPacket sendPacket = new DatagramPacket(msgBytes,offset,buffLength,LOCAL_HOST,clientPort);
						server.send(sendPacket);
						buffLength = 0; //Salimos
					}else{
						DatagramPacket sendPacket = new DatagramPacket(msgBytes,offset,buffLength,LOCAL_HOST,clientPort);
						server.send(sendPacket);
						offset += 20;
						buffLength = msgBytes.length - offset;	
						//System.out.println("El offset es: "+offset+", la length es: "+buffLength);
					}
				}
				System.out.println("Respuesta enviada...");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}