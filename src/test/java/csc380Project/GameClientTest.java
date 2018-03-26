package csc380Project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

public class GameClientTest {

    //GameClient.ChatAccess chatAccessTest;
    //GameClient.ChatFrame chatFrameTest;
    String serverTest;
    int validPort;
    int invalidPort;
    boolean test;


    @Before
    public void setUp(){
        String[] arguments = new String[] {};
        //new SyncClients().main(arguments);

        //chatAccessTest = new GameClient.ChatAccess();
        serverTest = "localhost";
        validPort = 4122;
        invalidPort = 1111;
    }

    /*
    //test with a good server and port
    @Test
    public void GameClientPortValid(){
        try {
            chatAccessTest.InitSocket(serverTest, validPort);
            test = true;
        }
        catch(IOException e){
            test = false;
        }
        assertEquals(true, test);
    }

    //test with an invalid port
    @Test
    public void GameClientPortInvalid(){
        try {
            chatAccessTest.InitSocket(serverTest, invalidPort);
            test = true;
        }
        catch(IOException e){
            test = false;
        }
        assertEquals(false, test);
    }*/



}
