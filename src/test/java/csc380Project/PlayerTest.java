package csc380Project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class PlayerTest {
    Player testPlayer = new Player("testPlayer");

    @Test
    void addAnswerTest() {
        System.out.println("In add Answer Test");
        Answer testAnswer = new Answer("test Answer", testPlayer);
        testPlayer.addAnswer("test Answer");
        assertEquals(testPlayer.getAnswers(), testAnswer);
    }

    @Test
    void increaseVotesReceivedTest() {
        System.out.println("In increase Votes Received Test");
        testPlayer.increaseVotesReceived();
        assertEquals(testPlayer.getNumVotesReceived(), 1);
    }
}
