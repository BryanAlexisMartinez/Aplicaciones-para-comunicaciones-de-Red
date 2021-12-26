/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoredes2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import static java.lang.Thread.sleep;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

public class Nodo extends JFrame implements ActionListener, Runnable {

    String archivosMD5Texto;
    JFileChooser fileChooser;
    /*Declaramos el objeto fileChooser*/

    String archivosMD5 = "";
    int count = 0;
    File carpeta;
    int puerto;
    Integer NodeName = (int) (Math.random() * 1000 + 10000);

    JEditorPane je;
    JScrollPane scrollPane;
    String inicioT = "<html><head></head><body><table>";
    String finT = "</table></body></html>";
    ArrayList<String> archivosSend = new ArrayList<>();
    String[] respuestaB;

    JLabel label1;
    JLabel label2;
    JLabel label3;
    JLabel label4;
    JLabel label5;

    JTextField TF = new JTextField();
    Registry registry;
    SNRMIinterface stub;
    Thread hilo;
    JButton boton1;
    JButton boton2;
    JButton boton3;
    JComboBox combo;
    private final String IP = "228.1.1.10";
    private MulticastSocket cl;
    private DatagramPacket packet;
    private byte b[];
    private InetAddress grupo = null;
    public final int TAM_BUFFER = 100;
    NServFlujo serverFlujo;

    public Nodo() throws NoSuchAlgorithmException, InterruptedException, Exception//constructor
    {
        NodeName = Integer.parseInt(JOptionPane.showInputDialog("Ingresa el puerto a utilizar"));

        fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.showOpenDialog(null);
        carpeta = fileChooser.getSelectedFile();
        FilesForFolder(carpeta);

        System.out.println("" + archivosSend);
        setLayout(null);//Layout absoluto
        setBounds(10, 10, 700, 500);//Tamaño de la ventana
        setTitle("Nodo: " + NodeName);//Título
        setResizable(false);//No redimensionable
        setDefaultCloseOperation(EXIT_ON_CLOSE);//Cerrar proceso al cerrar la ventana

        label1 = new JLabel("conectando...");//Etiqueta
        label2 = new JLabel("Archivos");//Etiqueta
        label4 = new JLabel("Nombre del nodo: " + NodeName);//Etiqueta
        label5 = new JLabel("");//Etiqueta

        label1.setBounds(190, 10, 200, 30);
        label2.setBounds(30, 50, 200, 30);
        label4.setBounds(30, 10, 200, 30);
        label5.setBounds(30, 380, 200, 30);

        add(label1);
        add(label2);
        add(label4);
        add(label5);

        TF.setBounds(30, 290, 400, 30);
        add(TF);

        combo = new JComboBox();
        combo.setBounds(30, 330, 400, 30);
        add(combo);

        je = new JEditorPane();
        je.setContentType("text/html");
        je.setBounds(30, 70, 500, 200);
        je.setText(inicioT + archivosMD5 + finT);
        add(je);
        scrollPane = new JScrollPane(je);
        scrollPane.setBounds(30, 70, 500, 200);
        add(scrollPane);

        boton1 = new JButton("buscar");
        boton1.setBounds(450, 290, 100, 30);
        add(boton1);
        boton2 = new JButton("descargar");
        boton2.setBounds(450, 330, 100, 30);
        add(boton2);
        boton3 = new JButton("desconectar");
        boton3.setBounds(450, 380, 120, 30);
        add(boton3);

        boton1.addActionListener(this);
        boton2.addActionListener(this);
        boton3.addActionListener(this);

        setVisible(true);
        serverFlujo = new NServFlujo(NodeName, carpeta);
        initConectSuperNodo();

        Runnable r = () -> {
            while (true) {
                try {
                    FilesForFolder(carpeta);
                    je.setText(inicioT + archivosMD5 + finT);
                    sleep(1000);
                } catch (Exception ex) {
                    Logger.getLogger(Nodo.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        };
        Thread t = new Thread(r);
        t.start();
        //descSN();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == boton1) {//buscar
            try {
                combo.removeAllItems();
                label5.setText("");
                respuestaB = stub.Busqueda(TF.getText());
                for (int i = 0; i < respuestaB.length; i++) {
                    combo.addItem(respuestaB[i]);
                }
                if (respuestaB.length == 0) {
                    label5.setText("No existe el archivo buscado");
                }
            } catch (RemoteException ex) {
                Logger.getLogger(Nodo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (e.getSource() == boton2) {//descargar
            String detalles = (String) combo.getSelectedItem();
            String[] datos = detalles.split("/");
            ClientFlujo("localhost", Integer.parseInt(datos[2]), datos[0]);
        }
        if (e.getSource() == boton3) {
            try { //desconnectar
                String aux = stub.Baja("" + NodeName);
                System.out.println("" + aux);
                System.exit(0);
            } catch (RemoteException ex) {
                Logger.getLogger(Nodo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void initConectSuperNodo() throws InterruptedException {
        try {
            NClientMutidif Cmulti = new NClientMutidif();//recibe los paquetes durante 30 segundos de todos los supernodo
            Thread.sleep(35000);
            label1.setText("conectado a " + Cmulti.puertoRMI);
            String host = "localhost";
            System.out.println("" + Cmulti.puertoRMI);
            registry = LocateRegistry.getRegistry(host, Cmulti.puertoRMI);
            stub = (SNRMIinterface) registry.lookup("Interf");
            System.out.println("despues del metodo RMI  " + Cmulti.puertoRMI);

            String respuesta = stub.Registro(NodeName.toString());
            serverFlujo.setConectado(true);
            System.out.println("" + respuesta);
            String[] mensaje = archivosSend.toArray(new String[archivosSend.size()]);
            respuesta = stub.Actualizacion(mensaje);
            hilo = new Thread(this);
            hilo.start();
        } catch (RemoteException ex) {
            Logger.getLogger(Nodo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(Nodo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FilesForFolder(File folder) throws NoSuchAlgorithmException, Exception {
        archivosSend.clear();
        archivosMD5 = "";
        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                archivosSend.add(fileEntry.getName() + "/" + getMD5Checksum(fileEntry) + "/" + NodeName);
                archivosMD5 = archivosMD5 + "<tr><td>" + fileEntry.getName() + "</td><td>" + getMD5Checksum(fileEntry) + "</td></tr>";
            }
        }
    }

    public static byte[] createChecksum(File filename) throws Exception {
        InputStream fis = new FileInputStream(filename);
        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;
        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);
        fis.close();
        return complete.digest();
    }

    // see this How-to for a faster way to convert
    // a byte array to a HEX string
    public static String getMD5Checksum(File filename) throws Exception {
        byte[] b = createChecksum(filename);
        String result = "";

        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    public void ClientFlujo(String hostA, int puertoA, String select) {//recibe
        try {
            String host = hostA;
            int pto = puertoA;
            Socket cl = new Socket(host, pto);
            DataInputStream fis;
            DataOutputStream salida = new DataOutputStream(cl.getOutputStream());
            DataInputStream entrada = new DataInputStream(cl.getInputStream());
            String archivo, nombre;
            DataOutputStream fileOut;

            salida.writeUTF(select); //se envia nombre del archivo
            salida.flush();

            long recibidos, tam;
            int n = 0;
            byte[] b = new byte[1024];
            tam = entrada.readLong();//tamaño
            System.out.println("Recibimos el archivo: '" + select + "', de tamaño: " + tam + " bytes.");
            fileOut = new DataOutputStream(new FileOutputStream(carpeta + "\\" + select));//creamos flujo para la salida del file
            recibidos = 0L;
            while (recibidos < tam) {
                n = entrada.read(b);
                fileOut.write(b, 0, n);//en la cadena de bytes agregamos el contenido de b, sin offset al flujo de datos que da a l archivo de longitud del buffer
                fileOut.flush();
                recibidos = recibidos + n;
                System.out.println("Recibido: " + recibidos + " de " + tam);
            }//While
            System.out.println("Archivo recibido.\n");
            salida.writeBoolean(true);
            salida.flush();
            fileOut.close();
            salida.close();
            entrada.close();
            cl.close();
            System.out.println("Conexión con el servidor terminada!");
        } catch (IOException ex) {
            Logger.getLogger(Nodo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InterruptedException, Exception {
        new Nodo();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(5000);
                String[] mensaje = archivosSend.toArray(new String[archivosSend.size()]);

                if (serverFlujo.isConectado()) {
                    stub.Actualizacion(mensaje);
                } else {
                    label1.setText("conectando...");
                    initConectSuperNodo();
                }

            } catch (RemoteException ex) {
                Logger.getLogger(Nodo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Nodo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(Nodo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /*public void descSN() {
        try {
            ServerSocket s = new ServerSocket(NodeName);
            Runnable r = () -> {
                while (true) {
                    try {
                        Socket cl = s.accept();
                        BufferedReader br = new BufferedReader(new InputStreamReader(cl.getInputStream()));
                        String mensaje = br.readLine();
                        if (mensaje.equals("Desconexion")) {
                            conectado = false;
                        }
                        br.close();
                        cl.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Nodo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            Thread t = new Thread(r);
            t.start();
        } catch (IOException ex) {
            Logger.getLogger(Nodo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
}
