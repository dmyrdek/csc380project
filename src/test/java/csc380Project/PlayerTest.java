package csc380Project;


import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class PlayerTest {

    Player testPlayer;

    @Before
    public void setUp(){
        System.out.println("current in setUp for PlayerTest");
        testPlayer = new Player("testPlayer");

    }


    @Test
    public void addAnswerTest() {
        testPlayer.addAnswer("test Answer");
        assertEquals(testPlayer.getAnswers().get(0).submitter, testPlayer);
        assertEquals(testPlayer.getAnswers().get(0).getAnswer(), "test Answer");
    }

    @Test
    public void increaseVotesReceivedTest() {
        testPlayer.increaseVotesReceived();
        assertEquals(testPlayer.getNumVotesReceived(), 1);
        testPlayer.increaseVotesReceived();
        assertEquals(testPlayer.getNumVotesReceived(), 2);
    }




}
