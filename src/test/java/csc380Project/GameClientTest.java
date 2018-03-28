package csc380Project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import csc380Project.game.Game;
import csc380Project.game.Player;
import csc380Project.game.QuestionPack;
import csc380Project.server.GameClient;
import csc380Project.server.GameServer;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import csc380Project.*;

public class GameClientTest {

    //GameClient.ChatAccess chatAccessTest;
    //GameClient.ChatFrame chatFrameTest;
    String serverTest;
    int validPort;
    int invalidPort;
    boolean test;
    Game g;


    @Before
    public void setUp(){
        String[] arguments = new String[] {};
        //new SyncClients().main(arguments);

        //chatAccessTest = new GameClient.ChatAccess();
        serverTest = "localhost";
        validPort = 4122;
        invalidPort = 1111;

        g = new Game(3, new QuestionPack().addAllQuestions());


    }


    @Test
    public void addPlayersParseTests(){
        // input looks like [players|<player1>|<player2> ....]
        String playerList = "players|Cedric|Doug|Brian|Dylan";
        Player testPlayer = new Player("Cedric");
        GameClient.parse(playerList, testPlayer);
        assertEquals(testPlayer.playersInGame.get(0).getName(), "Cedric");
        assertEquals(testPlayer.playersInGame.get(1).getName(), "Doug");
        assertEquals(testPlayer.playersInGame.get(2).getName(), "Brian");
        assertEquals(testPlayer.playersInGame.get(3).getName(), "Dylan");
    }

    @Test
    public void addQuestionParseTest(){
        //input looks like [questionsForGame|<questionIndex>|<question>|<receivingPlayer1>|<receivingPlayer2> ... (repeating)..]
        Player testPlayer = new Player("Cedric");
        testPlayer.playersInGame.add(new Player("Doug"));
        testPlayer.playersInGame.add(new Player("Brian"));
        testPlayer.playersInGame.add(new Player("Dylan"));
        testPlayer.playersInGame.add(testPlayer);

        String questions = "questionsForGame|0|testQuestion0|Cedric|Doug|1|testQuestions1|Doug|Brian|2|testQuestion2|Brian|Dylan|" +
                "3|testQuestion3|Dylan|Cedric|4|testQuestion4|Cedric|Doug|5|testQuestion5|Doug|Brian|6|testQuestion6|Brian|Dylan|"+
                "7|testQuestion7|Dylan|Cedric";
        
        GameClient.parse(questions, testPlayer);
        assertEquals(testPlayer.playersInGame.get(0).getQuestions()[0], "testQuestion0");
        assertEquals(testPlayer.playersInGame.get(0).getQuestions()[2], null);
        assertEquals(testPlayer.playersInGame.get(2).getQuestions()[3], "testQuestion3");
        assertEquals(testPlayer.playersInGame.get(3).getQuestions()[7], "testQuestion7");



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
