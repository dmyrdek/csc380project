package csc380Project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class GameTest {

    QuestionPack q;
    Game testGame;
    Player testPlayer ;
    int rounds;
    InGameQuestions igq;


    @Before
    public void setUp(){
        q = new QuestionPack().addAllQuestions();
        rounds = 10;
        testGame = new Game(rounds, q);
        testGame.addPlayerToGame("Player One");
        testGame.addPlayerToGame("Player Two");
        testGame.addPlayerToGame("Player three");
        testGame.gameQuestions = testGame.setGameQuestions();
    }

    @Test
    public void addPlayerToGameTest() {
        assertEquals(testGame.inGamePlayers.get(0).getName(), "Player One");
        assertEquals(testGame.inGamePlayers.get(1).getName(), "Player Two");
    }


    @Test
    public void setGameQuestionsTest(){
        testGame.gameQuestions = testGame.setGameQuestions();
        assertEquals(testGame.gameQuestions.questions.length, rounds*testGame.inGamePlayers.size());
    }


    //try to get questions for valid round
    @Test
    public void getQuestionsForRoundTest(){

        int roundNum = 2;
        String [] qfr = testGame.getQuestionsForRound(roundNum);
        int location = (roundNum-1) * testGame.inGamePlayers.size();
        for (int i = 0; i <testGame.inGamePlayers.size(); i++){
            assertEquals(qfr[i], testGame.gameQuestions.questions[location+i]);
        }
    }

    //try to get questions for invalid round
    @Test
    public void getQuestionsForRoundTestInavlid(){

        int roundNum = -12344;
        assertEquals(null, testGame.getQuestionsForRound(roundNum));
        roundNum = Integer.MAX_VALUE;
        assertEquals(null, testGame.getQuestionsForRound(roundNum));

    }






}
