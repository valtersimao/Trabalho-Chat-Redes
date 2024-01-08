/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author arthu
 */
public class Server {
    private ServerSocket soquete_servidor;
    
    public Server(int porta) throws Exception {
        super();
        this.soquete_servidor = new ServerSocket(porta);
    }
    
    public void finalizar() throws IOException {
        this.soquete_servidor.close();
    }

    public static void main(String[] args) throws Exception {
        Server servidor = new Server(15500);
        Socket soqueteCliente = null;
        while (true) {
            try {
                soqueteCliente = servidor.soquete_servidor.accept();
                System.out.println("\u001b[32m" + soqueteCliente + " - Conectou!");
                new Thread(new TrataCliente(soqueteCliente)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
