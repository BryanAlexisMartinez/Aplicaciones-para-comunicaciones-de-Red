/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoredes2;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


public interface SNRMIinterface extends Remote{
    //ArrayList<Archivo> suma(int a,int b) throws RemoteException;
    String [] Busqueda(String cad) throws RemoteException;
    String SuperBusqueda() throws RemoteException;
    String Actualizacion(String [] cad) throws RemoteException;
    String Registro(String cad) throws RemoteException;
    String Baja(String cad)throws RemoteException;
    //float Division(int a,int b) throws RemoteException;
}
