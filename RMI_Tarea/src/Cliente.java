import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Cliente {
    private Cliente() {}
    public static void main(String[] args) {
	String host = (args.length < 1) ? null : args[0];
	try {
	    Registry registry = LocateRegistry.getRegistry(host);	
            //también puedes usar getRegistry(String host, int port)
            //Calculadora stub = (Calculadora) registry.lookup("Suma");
            Calculadora stub = (Calculadora) registry.lookup("Resta");
            //Calculadora stub= (Calculadora) registry.lookup("Multiplicacion");
            //Calculadora stub = (Calculadora) registry.lookup("Division");*/
            
	   int x=15,y=4;
	    int response = stub.suma(x,y);
	    System.out.println("respuesta de sumar "+x+" y "+y+" : " + response);
            int response2 = stub.resta(x,y);
	    System.out.println("respuesta de restar "+x+" y "+y+" : " + response2);
            int response3 = stub.multi(x,y);
	    System.out.println("respuesta de multiplicar  "+x+" y "+y+" : " + response3);
            double response4 = stub.div(x,y);
	    System.out.println("respuesta de dividir "+x+" y "+y+" : " + response4);            
            
	} catch (Exception e) {
	    System.err.println("Excepción del cliente: " +e.toString());
	    e.printStackTrace();
	}
      }
}
