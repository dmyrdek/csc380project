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
        testPlayer = new Player("Player four");
    }

    //tests that addPlayerToGame method adds player to the game object
    @Test
    public void addPlayerToGameTest(){

        assertEquals(testGame.getInGamePlayers().get(0).getName(), "Player One");
        assertEquals(testGame.getInGamePlayers().get(1).getName(), "Player Two");

    }

    //tests that addPlayerToGame (Player Object) method adds player to the game object
    @Test
    public void addPlayerToGameTestPlayer(){

        testGame.addPlayerToGame(testPlayer);
        assertEquals(testGame.getInGamePlayers().get(3).getName(), "Player four");

    }

    //tests that addOtherPlayerToReference works as it should
    @Test
    public void addOtherPlayersToReferenceTest(){
        testGame.addOtherPlayersReference();
        String name1 = testGame.getInGamePlayers().get(0).playersInGame.get(0).getName();
        String name2 = testGame.getInGamePlayers().get(1).playersInGame.get(0).getName();
        String name3 = testGame.getInGamePlayers().get(0).playersInGame.get(2).getName();
        assertEquals(name1, "Player One");
        assertEquals(name2, "Player One");
        assertEquals(name3, "Player three");
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

    //tests that setPlayerNumber method in Player class works properly
    @Test
    public void setTestPlayerNumber(){

        testGame.addPlayerToGame(testPlayer);
        testGame.addOtherPlayersReference();
        testPlayer.setPlayerNumber();
        assertEquals(3, testPlayer.getPlayerNumber());

    }

    //tests that setPlayerNumber method doesn't work if addOtherPlayersReference method not called
    @Test
    public void badSetTestPlayerNumber(){

        testGame.addPlayerToGame(testPlayer);
        testPlayer.setPlayerNumber();
        assertEquals(-1, testPlayer.getPlayerNumber());

    }

    //tests updateScore method
    @Test
    public void updateScoreTest(){

        testGame.addPlayerToGame(testPlayer);
        testGame.addOtherPlayersReference();
        testPlayer.addQuestionToIndex("test question at index 3", 3);
        testPlayer.addAnswer("this is the answer to question at index 3", "test question at index 3");
        testPlayer.updateScore("this is the answer to question at index 3");

        int newScore = testPlayer.getNumVotesReceived();
        assertEquals(1, newScore);

        newScore = testGame.getInGamePlayers().get(0).playersInGame.get(3).getNumVotesReceived();
        assertEquals(1, newScore);

        boolean test = testGame.getInGamePlayers().get(1).updateScore("this is the answer to question at index 3");
        assertEquals(true, test);

    }

    //tests updateScore method returns false if answer requested does not exist
    @Test
    public void updateScoreInvalidAnswer(){

        testGame.addPlayerToGame(testPlayer);
        testGame.addOtherPlayersReference();
        testPlayer.addQuestionToIndex("A real question", 0);
        testPlayer.addAnswer("A real answer", "A real question");

        boolean test = testPlayer.updateScore("Not a real answer");
        assertEquals(false, test);

    }


}
