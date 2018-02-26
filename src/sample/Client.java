package sample;

import java.net.*;
import java.io.*;

public class Client {

    public static void main(String [] args) {

        //for command line running use
        // String serverName = args[0];
        // int port = Integer.parseInt(args[1]);

        String serverName = "";
        int port = 4122;

        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            System.out.println("Server says " + in.readUTF());
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
