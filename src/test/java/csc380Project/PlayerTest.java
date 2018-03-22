package csc380Project;


import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class PlayerTest {

    Player testPlayer;

    @Before
    public void setUp(){
        testPlayer = new Player("testPlayer");

    }

    @Test
    public void addQuestionTest(){
        testPlayer.addQuestionToIndex("test question", 42);
        assertEquals("test question", testPlayer.getQuestionAtIndex(42));
        assertEquals(null, testPlayer.getQuestionAtIndex(0));
    }


    @Test
    public void addAnswerTest(){
        testPlayer.addAnswerToIndex("test answer", 42);
        assertEquals("test answer", testPlayer.getAnswerAtIndex(42));
    }


    @Test
    public void increaseVotesReceivedTest() {
        testPlayer.increaseVotesReceived();
        assertEquals(testPlayer.getNumVotesReceived(), 1);
        testPlayer.increaseVotesReceived();
        assertEquals(testPlayer.getNumVotesReceived(), 2);
    }



}
