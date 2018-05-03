package csc380Project;

import csc380Project.game.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.Before;
import org.junit.Test;
import java.io.FileNotFoundException;

public class QuestionPackTest {

    QuestionPack qp = new QuestionPack();
    Game g = new Game(7, qp);
    Player a = new Player("Player 1");
    Player b = new Player("Player 2");
    Player c = new Player("Player 3");
    Player d = new Player("Player 4");


    @Before
    public void setUp() {
        g.addPlayerToGame(a);
        g.addPlayerToGame(b);
        g.addPlayerToGame(c);
        g.addPlayerToGame(d);
    }


    //tests getQuestionPack method with valid txt file
    @Test
    public void getQuestionPackTestValid(){
        assertEquals(qp.getQuestionPack("questions.txt"), true);

    }

    //tests getQuestionPack method with invalid txt file
    @Test
    public void getQuestionPackTestInValid(){

        String invalidFile = "InvalidFile.txt";
        assertEquals(qp.getQuestionPack(invalidFile), false);

    }

    //tests addAllQuestions method adds all 344 of our questions (useless atm)
    @Test
    public void addAllQuestionsTest(){
        QuestionPack q = new QuestionPack().addAllQuestions();
        assertEquals(q.getQArrayList().size(), 362);
    }


}
