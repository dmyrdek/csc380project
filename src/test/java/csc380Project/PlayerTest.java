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

    //tests that we can add questions to correct question array indexes
    @Test
    public void addQuestionTest(){
        testPlayer.addQuestionToIndex("test question", 42);
        assertEquals("test question", testPlayer.getQuestionAtIndex(42));
        assertEquals(null, testPlayer.getQuestionAtIndex(0));
        assertEquals("test question at index 3", testPlayer.getQuestionAtIndex(3));
    }

    //tests that we can add answers to correct answer array indexes (not going to use this later)
    @Test
    public void addAnswerTest(){
        testPlayer.addAnswerToIndex("test answer", 42);
        assertEquals("test answer", testPlayer.getAnswerAtIndex(42));
        assertEquals("test question at index 3", testPlayer.getQuestionAtIndex(3));
    }

    //tests that we can increase the score of a player
    @Test
    public void increaseVotesReceivedTest() {
        testPlayer.increaseVotesReceived();
        assertEquals(testPlayer.getNumVotesReceived(), 1);
        testPlayer.increaseVotesReceived();
        assertEquals(testPlayer.getNumVotesReceived(), 2);
    }

    //tests that we can get an answer at a certain index
    @Test
    public void getIndexOfAnswerToQuestionTest() {
        String testAnswer = "this is the answer to question at index 3";
        testPlayer.addAnswer(testAnswer, "test question at index 3");
        int answerIndex = -1;
        for(int x = 0; x < testPlayer.getAnswers().length; x ++){
            if(testAnswer.equals(testPlayer.getAnswerAtIndex(x))){
                answerIndex = x;
            }
        }
        assertEquals(3, answerIndex);
    }

    //tests that we can add answer based on the question it's answering
    @Test
    public void addAnswerBasedOnQuestion() {
        boolean test1 = testPlayer.addAnswer("this is the answer to question at index 3", "test question at index 3");
        assertEquals(true, test1);
        boolean test2 = testPlayer.addAnswer("this is the answer for a question that does not exist", "no existant question");
        assertEquals(false, test2);

    }

    //tests that we answers are put into correct place
    @Test
    public void getAnswersAfterAddingTest() {
        testPlayer.addAnswer("this is the answer to question at index 3", "test question at index 3");
        String testAnswer = testPlayer.getAnswerAtIndex(3);
        assertEquals("this is the answer to question at index 3", testAnswer);
    }

    //tests that player is not host by default
    @Test
    public void isPlayerNotHost() {
        assertEquals(false, testPlayer.getHostStatus());
    }

    //tests that we can set player as host
    @Test
    public void setTestPlayerHost() {
        testPlayer.setHostStatus(true);
        assertEquals(true, testPlayer.getHostStatus());
        testPlayer.setHostStatus(false);
        assertEquals(false, testPlayer.getHostStatus());
    }


}
