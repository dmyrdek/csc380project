package csc380Project.server;

import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class SyncClients extends Thread{

    public SyncClients(int max){
        maxClientsCount = max;
        threads = new ClientThread[maxClientsCount];
    }

    // The server socket.
    private static ServerSocket serverSocket = null;
    // The client socket.
    private static Socket clientSocket = null;

    private static int maxClientsCount;

    private static ClientThread[] threads;

    public static void main(String args[]) {

        int portNumber = 4122;
    
        /*
         * Open a server socket on the portNumber (default 4122). Note that we can
         * not choose a port less than 1023 if we are not privileged users (root).
         */
        try {
          serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
          System.out.println(e);
        }
    
        /*
         * Create a client socket for each connection and pass it to a new client
         * thread.
         */
        while (true) {
          try {
            clientSocket = serverSocket.accept();
            int i = 0;
            for (i = 0; i < maxClientsCount; i++) {
              if (threads[i] == null) {
                (threads[i] = new ClientThread(clientSocket, threads)).start();
                break;
              }
            }
            if (i == maxClientsCount) {
              PrintStream os = new PrintStream(clientSocket.getOutputStream());
              os.println("Server too busy. Try later.");
              os.close();
              clientSocket.close();
            }
          } catch (IOException e) {
            System.out.println(e);
          }
        }
      }  
}