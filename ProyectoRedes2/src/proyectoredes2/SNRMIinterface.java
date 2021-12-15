/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoredes2;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SNRMIinterface extends Remote {

    String[] Busqueda(String cad) throws RemoteException;

    String SuperBusqueda() throws RemoteException;

    String Actualizacion(String[] cad) throws RemoteException;

    String Registro(String cad) throws RemoteException;

    String Baja(String cad) throws RemoteException;
}
