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
			SyncClients sc = new SyncClients(15);
			sc.start();
			GameClient gc = new GameClient();
			gc.start();
			sc.main(arguments);
			String IPServer = JOptionPane.showInputDialog("Enter the port number");
			String[] arg = new String[] {IPServer};
			gc.main(arg);
		}else if(selection.equals("Client")){
			String IPServer = JOptionPane.showInputDialog("Enter the port number");
			String[] arguments = new String[] {IPServer};
			GameClient gc = new GameClient();
			gc.main(arguments);
			//new GameClient().main(arguments);
		}
		
	}
}