package csc380Project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class AnswerTest {
    QuestionPack q;
    Game testGame;
    Player testPlayer ;
    int rounds;


    @Before
    public void setUp(){
        testGame = new Game(rounds, new QuestionPack());

    }

    @Test
    public void getAnswerTest() {
        testGame.addPlayerToGame("Player One");
        Player p = testGame.inGamePlayers.get(0);
        Answer a = new Answer ("test answer", p);
        testGame.inGamePlayers.get(0).addAnswer(a);
        assertEquals(p.getAnswers().get(0), testGame.inGamePlayers.get(0).getAnswers().get(0) );

    }





}
