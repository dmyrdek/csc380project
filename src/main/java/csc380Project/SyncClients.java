package csc380Project;

import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class SyncClients{
    // The server socket.
    private static ServerSocket serverSocket = null;
    // The client socket.
    private static Socket clientSocket = null;

    private static final int maxClientsCount = 10;

    private static final ClientThread[] threads = new ClientThread[maxClientsCount];

    public static void main(String args[]) {

        // The default port number.
        int portNumber = 10694;
        if (args.length < 1) {
          System.out.println("Usage: java MultiThreadChatServerSync <portNumber>\n"
              + "Now using port number=" + portNumber);
        } else {
          portNumber = Integer.valueOf(args[0]).intValue();
        }
    
        /*
         * Open a server socket on the portNumber (default 2222). Note that we can
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