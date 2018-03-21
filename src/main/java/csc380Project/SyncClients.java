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

    private static final int maxClientsCount = Game.getMaxPlayers();

    private static final GameClient[] threads = new GameClient[maxClientsCount];

    
}