/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoredes2;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Cronometro implements Runnable {

    int Segundos = 0;
    Thread hilo;
    String contenido;

    Cronometro(String cont, int time) {
        contenido = cont;
        Segundos = time;
        hilo = new Thread(this);
        hilo.start();
    }

    Cronometro(int time) {
        contenido = "";
        Segundos = time;
        hilo = new Thread(this);
        hilo.start();
    }

    void Restablecer() {
        Segundos = 30;
    }

    String getDireccion() {
        return contenido;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println(contenido + "   " + Segundos);
                Thread.sleep(1000);
                Segundos = Segundos - 1;
                if (Segundos < 1) {
                    break;
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Cronometro.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
