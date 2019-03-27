/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managerSentAndReceive;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author Noumodong
 */
public class Serveur extends Thread {

    public static ServerSocket server_socket = null;
    public static String cheminDeSauvegarde = null;
    int PortDeReception = 0;

    public Serveur(String cheminDeSauvegarde, int PortDeReception) {
        Serveur.cheminDeSauvegarde = cheminDeSauvegarde;
        this.PortDeReception = PortDeReception;
    }

    @Override
    public void run() {

        try {
            server_socket = new ServerSocket(this.PortDeReception);
            //while (true) {
            System.out.println("En attente ...");
            new ClientWorker(server_socket.accept());

            // }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                server_socket.close();
                System.out.println("server socket want just to closed");
            } catch (IOException ex) {

            }
        }
    }

}
