/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoredes2;

import java.io.DataOutputStream;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class SuperNodo extends JFrame implements Runnable {

    int puertoRMI;
    String inicioT = "<html><head></head><body><table><tr><td>nombre</td><td>MDS</td><td>nodo</td></tr>";
    String inicioT2 = "<html><head></head><body><table>";
    String inicioT3 = "<html><head></head><body><table><tr><td>Puerto</td><td>restante</td></tr>";
    String finT = "</table></body></html>";
    SNServerMultidif server1;
    SNClientMultidif client1;
    Thread hilo;
    Registry registry;
    SNRMIinterface stub;
    SNRMIinterface inter = null;

    ArrayList<Cronometro> Nodos = new ArrayList<>();
    String ContenidosSN = "";
    String[] contenidos = new String[2];
    String[] contenidosHTML = new String[2];
    String[] puertos = new String[2];
    int[] tiempos = new int[2];
    Registry registryI = null;
    Remote remote = null;

    JEditorPane je;
    JScrollPane scrollPane;
    JEditorPane je2;
    JScrollPane scrollPane2;
    JEditorPane je3;
    JScrollPane scrollPane3;
    JLabel label1;
    JLabel label2;
    JLabel label3;
    JLabel label4;

    String caden = "";

    public SuperNodo() throws RemoteException, AlreadyBoundException {
        puertos[0] = "";
        puertos[1] = "";
        tiempos[0] = 0;
        tiempos[1] = 0;
        contenidos[0] = "";
        contenidos[1] = "";
        contenidosHTML[0] = "";
        contenidosHTML[1] = "";
        puertoRMI = Integer.parseInt(JOptionPane.showInputDialog("Ingresa el puerto a utilizar para RMI"));

        setLayout(null);//Layout absoluto
        setBounds(10, 10, 850, 400);//Tamaño de la ventana
        setTitle("Super-Nodo: " + puertoRMI);//Título
        setResizable(false);//No redimensionable
        setDefaultCloseOperation(EXIT_ON_CLOSE);//Cerrar proceso al cerrar la ventana

        label1 = new JLabel("Puerto RMI " + puertoRMI);//Etiqueta
        label2 = new JLabel("Archivos");//Etiqueta
        label3 = new JLabel("Otros Supernodos");//Etiqueta
        label4 = new JLabel("Nodos Conectados");//Etiqueta

        label1.setBounds(30, 10, 200, 30);
        label2.setBounds(30, 60, 200, 30);
        label3.setBounds(550, 60, 200, 30);
        label4.setBounds(700, 60, 200, 30);

        add(label1);
        add(label2);
        add(label3);
        add(label4);

        je = new JEditorPane();
        je.setContentType("text/html");
        je.setBounds(30, 100, 500, 100);
        je.setText(inicioT + contenidos[0] + contenidos[1] + finT);
        add(je);
        scrollPane = new JScrollPane(je);
        scrollPane.setBounds(30, 100, 500, 200);
        add(scrollPane);
        je2 = new JEditorPane();
        je2.setContentType("text/html");
        je2.setBounds(550, 100, 120, 200);
        je2.setText(inicioT2 + finT);
        add(je2);
        scrollPane2 = new JScrollPane(je2);
        scrollPane2.setBounds(550, 100, 120, 200);
        add(scrollPane2);

        je3 = new JEditorPane();
        je3.setContentType("text/html");
        je3.setBounds(700, 100, 120, 200);
        je3.setText(inicioT3 + finT);
        add(je3);
        scrollPane3 = new JScrollPane(je3);
        scrollPane3.setBounds(700, 100, 120, 200);
        add(scrollPane3);

        setVisible(true);

        initServer();

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                try {
                    if (!puertos[0].equals("")) {
                        String host = "localhost";
                        int pto = Integer.parseInt(puertos[0]);
                        Socket cl = new Socket(host, pto);
                        DataOutputStream salida = new DataOutputStream(cl.getOutputStream());
                        salida.writeUTF("Desconexion()r86d8qRTpNVGXPC");
                        salida.flush();
                        salida.close();
                        cl.close();
                    }
                    if (!puertos[1].equals("")) {
                        String host1 = "localhost";
                        int pto1 = Integer.parseInt(puertos[1]);
                        Socket cl1 = new Socket(host1, pto1);
                        DataOutputStream salida1 = new DataOutputStream(cl1.getOutputStream());
                        salida1.writeUTF("Desconexion()r86d8qRTpNVGXPC");
                        salida1.flush();
                        salida1.close();
                        cl1.close();
                    }
                } catch (IOException | NumberFormatException ex) {
                    ex.printStackTrace();
                } finally {
                    System.exit(0);
                }
            }
        });
    }

    private void initServer() throws RemoteException, AlreadyBoundException {

        server1 = new SNServerMultidif(puertoRMI);//anuncia cada 5 segundos que sigue activo
        client1 = new SNClientMultidif(puertoRMI);
        inter = new SNRMIinterface() {

            @Override
            public String[] Busqueda(String cad) throws RemoteException, AccessException {
                ArrayList<String> result = new ArrayList<String>();
                String[] cadena = ContenidosSN.split("\n");
                for (int i = 0; i < cadena.length; i++) {
                    System.out.println(cadena[i]);
                    if (cadena[i].contains(cad)) {
                        result.add(cadena[i]);
                    }
                }
                String nodosV = client1.nodosVivos();
                String[] cadenas2 = nodosV.split("/");
                for (int i = 0; i < cadenas2.length; i++) {//para cada super Nodo activo
                    String host = "localhost";
                    if (!cadenas2[i].equals("")) {
                        registry = LocateRegistry.getRegistry(host, Integer.parseInt(cadenas2[i]));
                        try {
                            stub = (SNRMIinterface) registry.lookup("Interf");
                            String CadenasAjenas = stub.SuperBusqueda();
                            String[] filas = CadenasAjenas.split("\n");
                            for (int j = 0; j < filas.length; j++) {
                                if (filas[j].contains(cad)) {
                                    result.add(filas[j]);
                                }
                            }
                        } catch (NotBoundException ex) {
                            Logger.getLogger(SuperNodo.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                String[] mensaje = result.toArray(new String[result.size()]);
                return mensaje;
            }

            @Override
            public String Actualizacion(String[] cad) throws RemoteException {
                String contenidoAux = "";
                String contenidoAuxNoHTML = "";
                for (int i = 0; i < cad.length; i++) {
                    System.out.println(cad[i]);
                    String[] filas = cad[i].split("/");
                    contenidoAuxNoHTML = contenidoAuxNoHTML + cad[i] + "\n";
                    contenidoAux = contenidoAux + "<tr><td>" + filas[0] + "</td><td>" + filas[1] + "</td><td>" + filas[2] + "</td></tr>";//nombre checksum nodename puerto
                }
                String[] filasp = cad[0].split("/");
                if (filasp[2].equals(puertos[0])) {
                    contenidosHTML[0] = contenidoAux;
                    contenidos[0] = contenidoAuxNoHTML;
                    tiempos[0] = 15;
                } else {
                    contenidosHTML[1] = contenidoAux;
                    contenidos[1] = contenidoAuxNoHTML;
                    tiempos[1] = 15;
                }
                ContenidosSN = contenidos[0] + "\n" + contenidos[1];
                je.setText(inicioT + contenidosHTML[0] + contenidosHTML[1] + caden + finT);
                return "actualizado";
            }

            @Override
            public String Registro(String cad) throws RemoteException {

                if (server1.disponibles > 0) {
                    server1.menos1();
                    if (puertos[0].equals("")) {
                        tiempos[0] = 15;
                        puertos[0] = cad;
                    } else {
                        tiempos[1] = 15;
                        puertos[1] = cad;
                    }
                    return "conectado a supernodo " + puertoRMI;
                } else {
                    return "no se pudo conectar";
                }
            }

            @Override
            public String SuperBusqueda() throws RemoteException {
                return ContenidosSN;
            }

            @Override
            public String Baja(String cad) throws RemoteException {
                return "dado de baja";
            }
        };
        remote = UnicastRemoteObject.exportObject(inter, 0);
        registryI = LocateRegistry.createRegistry(puertoRMI);
        System.out.println("Servidor escuchando en el puerto " + String.valueOf(puertoRMI));
        registryI.bind("Interf", remote); // Registrar 
        hilo = new Thread(this);
        hilo.start();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InterruptedException, RemoteException, AlreadyBoundException {
        new SuperNodo();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                if (tiempos[0] > 0) {
                    tiempos[0]--;
                } else {
                    darBaja(puertos[0]);
                }
                if (tiempos[1] > 0) {
                    tiempos[1]--;
                } else {
                    darBaja(puertos[1]);
                }
                String nodosT = client1.tabla();
                je2.setText(inicioT2 + nodosT + finT);
                je3.setText(inicioT3 + "<tr><td>" + puertos[0] + "</td><td>" + tiempos[0] + "</td></tr><tr><td>" + puertos[1] + "</td><td>" + tiempos[1] + "</td></tr>" + finT);
            } catch (InterruptedException ex) {
                Logger.getLogger(SuperNodo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void darBaja(String cad) {
        if (!cad.equals("")) {
            server1.mas1();
            System.out.println("Dado de baja: " + cad);
            if (puertos[0].equals(cad) && !puertos[0].equals("")) {
                puertos[0] = "";
                contenidosHTML[0] = "";
                contenidos[0] = "";
            } else {
                puertos[1] = "";
                contenidosHTML[1] = "";
                contenidos[1] = "";
            }
            ContenidosSN = contenidos[0] + "\n" + contenidos[1];
            je.setText(inicioT + contenidosHTML[0] + contenidosHTML[1] + finT);
        }
    }
}
