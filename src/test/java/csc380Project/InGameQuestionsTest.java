package csc380Project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class InGameQuestionsTest {

    Game g;
    Player a = new Player("Player 1") ;
    Player b = new Player("Player 2");
    InGameQuestions iqs;

    @Before
    public void setUp(){
        g = new Game(2, new QuestionPack().addAllQuestions());
        g.addPlayerToGame(a);
        g.addPlayerToGame(b);

        iqs = g.setGameQuestions();
    }


    @Test
    public void getIGQSize(){
        assertEquals(iqs.questions.length, g.inGamePlayers.size() * g.numRounds);
    }


}
