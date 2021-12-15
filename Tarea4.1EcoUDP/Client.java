import java.net.*;
import java.io.*;
public class Client {
	public static void main(String [] args){
		try {
			//SE ASOCIA UN BUFFER DE LECTURA DE CARACTERES A LA ENTRADA DE DATOS ESTANDAR
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			InetAddress LOCAL_HOST = InetAddress.getByName("127.0.0.1");
			int PORT = 2000;
			byte[] buffer = new byte[20];

			DatagramSocket client = new DatagramSocket();
			//Inserte el mensaje que el cliente quiere enviar
			System.out.println("Escriba un mensaje para enviar al servidor:");
			//SE LEE LO QUE ESCRIBE EL USUARIO
			String msg = reader.readLine();
			//String mensaje= reader.readLine();
			//CONVIERTE LA CADENA EN UN ARREGLO DE BYTES
			byte[] msgBytes = msg.getBytes();
			//byte[] mensajeBytes = mensaje.getBytes();

			//System.out.println("El tamanio es"+ mensajeBytes);
			/*int buffmenLenght = mensajeBytes.length;
			while(buffmenLenght > 0){
				DatagramPacket tamanioenviado = new DatagramPacket(mensajeBytes, buffmenLenght,LOCAL_HOST,PORT);
				client.send(tamanioenviado);
			}
*/
			int offset = 0;
			int buffLength = msgBytes.length - offset;
			//PROBAR ESTO???????????
				//DatagramPacket tamanio = new DatagramPacket(buffer, buffLength);
				//client.send(tamanio);
				//System.out.println("El tamanio es"+ msgBytes);
				
			while(buffLength > 0){
				
				//SE VALIDA SI LA ENTRADA ES MENOR A 20, EN ESE CASO SE ENVIA SOLO UN PAQUETE
				if(buffLength < 20){
					//ENTRA LA PARTE QUE ENVIA EL ULTIMO TRAMO
					//System.out.println("Entro aqui con un length="+buffLength); 
					DatagramPacket sendPacket = new DatagramPacket(msgBytes,offset,buffLength,LOCAL_HOST,PORT);
					client.send(sendPacket);
					buffLength = 0; //Salimos
				//EN CASO DE SER MAYOR A 20 SE PROCEDE A REALIZAR LA DIVISION EN 20
				}else{
					//CADA PARTE GENERADA SE MANDA POR ESTE DATAGRAMA
					DatagramPacket sendPacket = new DatagramPacket(msgBytes,offset,buffLength,LOCAL_HOST,PORT);
					client.send(sendPacket);
					//SE SUMA AL OFFSET LOS 20 BYTES ENVIADOS POR CADA DATAGRAMA
					offset += 20;
					//SE LE RESTA A LA LONGITUD INCIAL EL OFFSET
					buffLength = msgBytes.length - offset;	
					//SE IMPRIME EL OFFSET Y EL LENGHT QUE FALTA
					System.out.println("El offset es: "+offset+", la length es: "+buffLength);
				}
			}
			
			//Se crea un StringBuffer,, luego se van leyendo los paquetes hasta que sean de una longitud menor a 20
			//Se van insertando los paquetes en el stringbuffer

			//Creamos y enviamos el packet 		
			/*DatagramPacket sendPacket = new DatagramPacket(msgBytes,5,msgBytes.length-5,LOCAL_HOST,PORT);
			client.send(sendPacket);*/
			System.out.println("Esperando respuesta por parte del servidor.");
			//SE RECIBE UN PAQUETE
			StringBuffer msgBuff = new StringBuffer();	
			int msgLength = 20;
			//SE INICIALIZA PARA ENTRAR AL WHILE
			offset = 0;
			//Recibimos el mensaje
			while(msgLength >= 20){
				//SE RECIBEN PAQUETES DE 20 BYTES HASTA QUE LLEGUE EL ULTIMO
				DatagramPacket receivePacket = new DatagramPacket(buffer,buffer.length);
				client.receive(receivePacket);
				String data = new String(receivePacket.getData(),0,receivePacket.getLength());
				msgBuff.insert(offset,data);
				offset += msgLength;
				msgLength = receivePacket.getLength();
			}
			System.out.println("Recibo este mensaje: "+msgBuff);

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}