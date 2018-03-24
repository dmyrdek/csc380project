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
}