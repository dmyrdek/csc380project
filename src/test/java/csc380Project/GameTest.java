package csc380Project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class GameTest {

    QuestionPack q = new QuestionPack();
    Game testGame = new Game(1, q);
    Player testPlayer = new Player("testPlayer");
    int numRounds = 3;

    @Test
    void addPlayerToGameTest() {
        testGame.addPlayerToGame("testPlayer");
        assertEquals(testGame.inGamePlayers.get(0), testPlayer);
    }

    @Test
    void setGameQuestionsTest() {

    }


}
