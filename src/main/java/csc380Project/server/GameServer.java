package csc380Project.server;

import javax.swing.*;
import java.io.*;
import java.net.*;

public class GameServer {

    //use port 4122 with ngrok
    
    public static void main(String [] args){
		
		Object[] selectioValues = { "Server","Client"};
		String initialSection = "Server";
		
		Object selection = JOptionPane.showInputDialog(null, "Login as : ", "MyChatApp", JOptionPane.QUESTION_MESSAGE, null, selectioValues, initialSection);
		if(selection.equals("Server")){
                   String[] arguments = new String[] {};
			new SyncClients().main(arguments);
		}else if(selection.equals("Client")){
			String IPServer = JOptionPane.showInputDialog("Enter the port number");
                        String[] arguments = new String[] {IPServer};
			new GameClient().main(arguments);
		}
		
	}


    public static void createServer() throws IOException {
        ServerSocket sersock = new ServerSocket(4122);
        System.out.println("Server ready for chatting");
        Socket sock = sersock.accept( );
        // reading from keyboard (keyRead object)
        BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
        // sending to client (pwrite object)
        OutputStream ostream = sock.getOutputStream();
        PrintWriter pwrite = new PrintWriter(ostream, true);

        // receiving from server ( receiveRead  object)
        InputStream istream = sock.getInputStream();
        BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));

        String receiveMessage, sendMessage;
        while(true)
        {
            if((receiveMessage = receiveRead.readLine()) != null)
            {
                System.out.println(receiveMessage);
            }
            //
            pwrite.flush();
            //

            sendMessage = keyRead.readLine();
            pwrite.println(sendMessage);
            pwrite.flush();
        }
    }

}