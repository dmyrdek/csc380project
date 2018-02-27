package sample;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    Socket threadSock;

    public ServerThread(Socket clientSock){
        threadSock = clientSock;
    }

    public void run(){
        BufferedReader reader = null;
        DataOutputStream out = null;

        try{
            InputStream in = threadSock.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            out = new DataOutputStream(threadSock.getOutputStream());
        } catch (IOException e){
            e.printStackTrace();
            return;
        }

        String line;
        while(true){
            try{
                line = reader.readLine();
                if(line != null){
                    out.writeBytes(line + "\n");
                    out.flush();
                }
                else{
                    threadSock.close();
                    return;
                }
            } catch (IOException e){
                e.printStackTrace();
                return;
            }
        }
    }
}
