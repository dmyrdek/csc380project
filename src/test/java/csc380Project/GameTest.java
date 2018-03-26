package csc380Project;

import csc380Project.game.*;
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
        testGame.setThisGamesQuestions();
    }

    //tests that addPlayerToGame method adds player to the game object
    @Test
    public void addPlayerToGameTest(){
        assertEquals(testGame.getInGamePlayers().get(0).getName(), "Player One");
        assertEquals(testGame.getInGamePlayers().get(1).getName(), "Player Two");
    }

    //tests that setGameQuestionsTest method adds correct number of questions
    @Test
    public void setGameQuestionsTest(){
        testGame.setThisGamesQuestions();
        assertEquals(testGame.getGameQuestions().getQuestions().length, rounds*testGame.getInGamePlayers().size());
    }


    //try to get questions for valid round
    @Test
    public void getQuestionsForRoundTest(){

        int roundNum = 2;
        String [] qfr = testGame.getQuestionsForRound(roundNum);
        int location = (roundNum-1) * testGame.getInGamePlayers().size();
        for (int i = 0; i <testGame.getInGamePlayers().size(); i++){
            assertEquals(qfr[i], testGame.getGameQuestions().getQuestions()[location+i]);
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

    //tests that giveQuestionToPlayer method gives questions properly to each player
    @Test
    public void giveQuestionstoPlayersTestValid(){
        testGame.giveQuestionstoPlayers();
        int numPlayers = testGame.getInGamePlayers().size();

        //check players 1 up until the last players have the correct questions
        for (int playerNum= 0; playerNum<numPlayers-1; playerNum++) {
            for (int currentRound = 0; currentRound<testGame.getNumOfRounds(); currentRound++){
                int index = (currentRound * numPlayers) + playerNum;

                assertEquals(testGame.getInGamePlayers().get(playerNum).getQuestionAtIndex(index),
                        testGame.getGameQuestions().getQuestions()[index]);

                assertEquals(testGame.getInGamePlayers().get(playerNum).getQuestionAtIndex(index+1),
                        testGame.getGameQuestions().getQuestions()[index+1]);

            }
        }

        //check the last players questions are correct
        for (int currentRound=0; currentRound<testGame.getNumOfRounds(); currentRound++){
            int firstQuestionForRound = (currentRound * numPlayers);
            int secondQuestionForRound = currentRound*numPlayers + numPlayers-1;

            assertEquals(testGame.getInGamePlayers().get(numPlayers-1).getQuestionAtIndex(firstQuestionForRound),
                    testGame.getGameQuestions().getQuestions()[firstQuestionForRound]);
            assertEquals(testGame.getInGamePlayers().get(numPlayers-1).getQuestionAtIndex(secondQuestionForRound),
                    testGame.getGameQuestions().getQuestions()[secondQuestionForRound]);
        }

    }

    //tests that giveQuestionsToPlayer returns false with invalid num of rounds
    @Test
    public void giveQuestionstoPlayersTestInvalid(){

        Game badGame = new Game(-1, q);

        assertEquals(badGame.giveQuestionstoPlayers(), false);

    }

    //tests that giveQuestionsToPlayer returns false with empty question pack
    @Test
    public void giveQuestionstoPlayersTestQPInvalid() {
        q = new QuestionPack();
        Game badGame = new Game(2, q);

        assertEquals(badGame.giveQuestionstoPlayers(), false);

    }




}
