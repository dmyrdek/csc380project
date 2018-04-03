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
        assertEquals(testPlayer.findPlayerInList("Cedric").getName(), "Cedric");
        assertEquals(testPlayer.findPlayerInList("Doug").getName(), "Doug");
        assertEquals(testPlayer.findPlayerInList("Brian").getName(), "Brian");
        assertEquals(testPlayer.findPlayerInList("Dylan").getName(), "Dylan");
    }

    @Test
    public void addQuestionParseTest(){
        //input looks like [questionsForGame|<questionIndex>|<question>|<receivingPlayer1>|<receivingPlayer2> ... (repeating)..]

        //assume players have already been added to the list of playersInGame (which is done by addPlayers method)
        Player testPlayer = new Player("Cedric");
        testPlayer.playersInGame.add(new Player("Doug"));
        testPlayer.playersInGame.add(new Player("Brian"));
        testPlayer.playersInGame.add(new Player("Dylan"));
        testPlayer.playersInGame.add(testPlayer);

        String questions = "questionsForGame|0|testQuestion0|Cedric|Doug|1|testQuestions1|Doug|Brian|2|testQuestion2|Brian|Dylan|" +
                "3|testQuestion3|Dylan|Cedric|4|testQuestion4|Cedric|Doug|5|testQuestion5|Doug|Brian|6|testQuestion6|Brian|Dylan|"+
                "7|testQuestion7|Dylan|Cedric";

        GameClient.parse(questions, testPlayer);
        assertEquals(testPlayer.findPlayerInList("Doug").getQuestions()[0], "testQuestion0");
        assertEquals(testPlayer.findPlayerInList("Doug").getQuestions()[2], null);
        assertEquals(testPlayer.findPlayerInList("Brian").getQuestions()[3], null);
        assertEquals(testPlayer.findPlayerInList("Doug").getQuestions()[5], "testQuestion5");
        assertEquals(testPlayer.findPlayerInList("Dylan").getQuestions()[7], "testQuestion7");
    }

    @Test
    public void addAnswerParseTest(){

        //assume players already exists, and have questions already added to their name (which is done by the addPlayer method)

        Player testPlayer = new Player("Cedric");
        Player testPlayer2 = new Player("Doug");
        Player testPlayer3 = new Player("Brian");
        Player testPlayer4 = new Player("Dylan");
        g.addPlayerToGame(testPlayer);
        g.addPlayerToGame(testPlayer2);
        g.addPlayerToGame(testPlayer3);
        g.addPlayerToGame(testPlayer4);
        g.addOtherPlayersReference();


        String questions = "questionsForGame|0|testQuestion0|Cedric|Doug|1|testQuestion1|Doug|Brian|2|testQuestion2|Brian|Dylan|" +
                "3|testQuestion3|Dylan|Cedric|4|testQuestion4|Cedric|Doug|5|testQuestion5|Doug|Brian|6|testQuestion6|Brian|Dylan|"+
                "7|testQuestion7|Dylan|Cedric";

        GameClient.parse(questions, testPlayer);

        //input looks like [answer|<question>|<answer>|<player>]

        String answer = "answer|testQuestion0|testAnswer0Cedric|Cedric";
        String answer2 = "answer|testQuestion1|testAnswer1Doug|Doug";
        String answer3 = "answer|testQuestion4|testAnswer4Brian|Brian"; //this result should NOT be recorded -- make sure it fails
        String answer7 = "answer|testQuestion7|testAnswer7Dylan|Dylan";

        GameClient.parse(answer, testPlayer);
        GameClient.parse(answer2, testPlayer);
        GameClient.parse(answer3, testPlayer);
        GameClient.parse(answer7, testPlayer);

        assertEquals(testPlayer.findPlayerInList("Cedric").getAnswerAtIndex(0), "testAnswer0Cedric");
        assertEquals(testPlayer.findPlayerInList("Doug").getAnswerAtIndex(1), "testAnswer1Doug");
        assertEquals(testPlayer.findPlayerInList("Brian").getAnswerAtIndex(4), null);
        assertEquals(testPlayer.findPlayerInList("Dylan").getAnswerAtIndex(7), "testAnswer7Dylan");
    }


    @Test
    public void voteParseTest(){

        Player testPlayer = new Player("Cedric");
        Player testPlayer2 = new Player("Doug");
        Player testPlayer3 = new Player("Brian");
        Player testPlayer4 = new Player("Dylan");
        g.addPlayerToGame(testPlayer);
        g.addPlayerToGame(testPlayer2);
        g.addPlayerToGame(testPlayer3);
        g.addPlayerToGame(testPlayer4);
        g.addOtherPlayersReference();

        String questions = "questionsForGame|0|testQuestion0|Cedric|Doug|1|testQuestion1|Doug|Brian|2|testQuestion2|Brian|Dylan|" +
                "3|testQuestion3|Dylan|Cedric|4|testQuestion4|Cedric|Doug|5|testQuestion5|Doug|Brian|6|testQuestion6|Brian|Dylan|"+
                "7|testQuestion7|Dylan|Cedric";

        GameClient.parse(questions, testPlayer);

        String answer = "answer|testQuestion0|testAnswer0Cedric|Cedric";
        String answer2 = "answer|testQuestion1|testAnswer1Doug|Doug";
        String answer4 = "answer|testQuestion4|testAnswer4Brian|Brian"; //this answer does NOT GET RECORDED on purpose
        String answer7 = "answer|testQuestion7|testAnswer7Dylan|Dylan";
        GameClient.parse(answer, testPlayer);
        GameClient.parse(answer2, testPlayer);
        GameClient.parse(answer4, testPlayer);
        GameClient.parse(answer7, testPlayer);


        // input will look like [vote|<question>|<answer>|<player>]
        String vote1 = "vote|testQuestion0|testAnswer0Cedric|Cedric";
        String vote2 = "vote|testQuestion1Doug|testAnswer1Doug|Doug";
        String vote3 = "vote|testQuestion7|testAnswer7Dylan|Dylan";


        GameClient.parse(vote1, testPlayer);
        GameClient.parse(vote2, testPlayer);
        GameClient.parse(vote3, testPlayer);

        assertEquals(testPlayer.getNumVotesReceived(), 1);
        assertEquals(testPlayer.findPlayerInList("Cedric").getNumVotesReceived(), 1);
        assertEquals(testPlayer.findPlayerInList("Dylan").getNumVotesReceived(), 1);
        assertEquals(testPlayer.findPlayerInList("Doug").getNumVotesReceived(), 1);
        assertEquals(testPlayer.findPlayerInList("Brian").getNumVotesReceived(), 0); //this player has not received any votes

        String answer3 = "answer|testQuestion3|testAnswer3Cedric|Cedric";
        String vote4 = "vote|testQuestion3|testAnswer3Cedric|Cedric";
        GameClient.parse(answer3, testPlayer);
        GameClient.parse(vote4, testPlayer);
        assertEquals(testPlayer.getNumVotesReceived(), 2);

    }

    //tests combination of parsing methods
    @Test
    public void totalParseTest(){

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
