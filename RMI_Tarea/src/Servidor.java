import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
//import Suma.*;
	
public class Servidor implements Calculadora{
	
    public Servidor() {}

    public int suma(float a, float b) {
        return a+b;
    }
    
    public int resta(float a,float b){
        return a-b;
    }
    
    public int multi(float a, float b){
        return a*b;
    }
    
    public double div(float a, float b){
        return a/b;
    }
    
    public static void main(String args[]) {
	try {
               //puerto default del rmiregistry
               java.rmi.registry.LocateRegistry.createRegistry(1099); 
               System.out.println("RMI registro listo.");
	} catch (Exception e) {
               System.out.println("Excepcion RMI del registry:");
                e.printStackTrace();
           }//catch
	try {
                System.setProperty("java.rmi.server.codebase","file:/c:/Temp/Calculadora/");
                Servidor obj = new Servidor();
                Calculadora stub = (Calculadora) UnicastRemoteObject.exportObject(obj, 0);
                // Ligamos el objeto remoto en el registro
                Registry registry = LocateRegistry.getRegistry();
                registry.bind("Suma", stub);
                registry.bind("Resta", stub);
                registry.bind("Multiplicacion", stub);
                registry.bind("División", stub);
             

                System.err.println("Servidor listo...");
	} catch (Exception e) {
	    System.err.println("Excepción del servidor: " + e.toString());
	    e.printStackTrace();
	}
    }
}