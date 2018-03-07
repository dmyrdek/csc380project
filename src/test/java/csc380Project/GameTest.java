package csc380Project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class GameTest {

    QuestionPack q;
    Game testGame;
    Player testPlayer ;
    int rounds;


    @Before
    public void setUp(){
        q = new QuestionPack();
        rounds = 10;
        testGame = new Game(rounds, q);
        testGame.addPlayerToGame("Player One");
        testGame.addPlayerToGame("Player Two");
    }

    @Test
    public void addPlayerToGameTest() {
        assertEquals(testGame.inGamePlayers.get(0).getName(), "Player One");
        assertEquals(testGame.inGamePlayers.get(1).getName(), "Player Two");
    }


}
