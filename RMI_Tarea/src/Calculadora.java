import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Calculadora extends Remote {
    int suma(int a,int b) throws RemoteException;
    int resta (int a,int b) throws RemoteException;
    int multi(int a,int b) throws RemoteException;
    double div (int a,int b) throws RemoteException;

}
