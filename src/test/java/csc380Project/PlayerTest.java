package csc380Project;

import csc380Project.game.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class PlayerTest {

    Player testPlayer;

    @Before
    public void setUp(){
        testPlayer = new Player("testPlayer");
        testPlayer.addQuestionToIndex("test question at index 3", 3);

    }

    @Test
    public void addQuestionTest(){
        testPlayer.addQuestionToIndex("test question", 42);
        assertEquals("test question", testPlayer.getQuestionAtIndex(42));
        assertEquals(null, testPlayer.getQuestionAtIndex(0));
        assertEquals("test question at index 3", testPlayer.getQuestionAtIndex(3));
    }


    @Test
    public void addAnswerTest(){
        testPlayer.addAnswerToIndex("test answer", 42);
        assertEquals("test answer", testPlayer.getAnswerAtIndex(42));
        assertEquals("test question at index 3", testPlayer.getQuestionAtIndex(3));
    }


    @Test
    public void increaseVotesReceivedTest() {
        testPlayer.increaseVotesReceived();
        assertEquals(testPlayer.getNumVotesReceived(), 1);
        testPlayer.increaseVotesReceived();
        assertEquals(testPlayer.getNumVotesReceived(), 2);
    }

    @Test
    public void addAnswerToIndex() {
        assertEquals(true, testPlayer.addAnswer("this is the answer to question at index 3", "test question at index 3"));
        assertEquals(false, testPlayer.addAnswer("this is the answer for a question that does not exist", "no existant question"));

    }



}
