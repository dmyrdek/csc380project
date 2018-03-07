package csc380Project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.jupiter.api.Test;

public class PlayerTest {

    Player testPlayer;

    @Before
    public void setUp(){
        Player testPlayer = new Player("testPlayer");

    }


    @Test
    void addAnswerTest() {
        Answer testAnswer = new Answer("test Answer", testPlayer);
        testPlayer.addAnswer("test Answer");
        assertEquals(testPlayer.getAnswers().get(0), testAnswer);
    }

    @Test
    void increaseVotesReceivedTest() {
        testPlayer.increaseVotesReceived();
        testPlayer.increaseVotesReceived();
        assertEquals(testPlayer.getNumVotesReceived(), 2);
    }



}
