package csc380Project;

import csc380Project.game.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class GameTest {

    QuestionPack q;
    Game testGame;
    Player testPlayer ;
    int rounds;
    InGameQuestions igq;
    Game testGame2;
    Player p1;
    Player p2;
    Player p3;
    Player p4;
    ArrayList<Player> playerList;


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

        p1 = new Player("Player 1");
        p2 = new Player("Player 2");
        p3 = new Player("Player 3");
        p4 = new Player("Player 4");
        playerList = new ArrayList<Player>();
        playerList.add(p1);
        playerList.add(p2);
        playerList.add(p3);
        playerList.add(p4);
        testGame2 = new Game(rounds, playerList);
    }

    //tests the new Game constructor
    @Test
    public void checkGameTest(){
        assertEquals(testGame2.getInGamePlayers().get(0).getName(), "Player 1");
        assertEquals(testGame2.getInGamePlayers().get(3).getName(), "Player 4");
        assertEquals(testGame2.getNumOfRounds(), rounds);
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
        Player testPlayer2 = new Player("Player2");
        testGame.addPlayerToGame(testPlayer2);
        testGame.addOtherPlayersReference();
        testPlayer.addQuestionToIndex("test question at index 3", 3);
        testPlayer.addAnswer("this is the answer to question at index 3", "test question at index 3");
        testPlayer.updateScore("this is the answer to question at index 3");

        testPlayer2.addQuestionToIndex("test question at index 2", 2);
        testPlayer2.addAnswer("Answer2","test question at index 2");
        testPlayer.updateScore("Answer2");

        assertEquals(testPlayer.findPlayerInList("Player2").getNumVotesReceived(),1 );

        testPlayer.updateScore("Answer2");
        assertEquals(testPlayer.findPlayerInList("Player2").getNumVotesReceived(),2 );


        int newScore = testPlayer.getNumVotesReceived();
        assertEquals(1, newScore);

        newScore = testGame.getInGamePlayers().get(0).playersInGame.get(3).getNumVotesReceived();
        assertEquals(1, newScore);

        boolean test = testGame.getInGamePlayers().get(1).updateScore("this is the answer to question at index 3");
        assertEquals(true, test);

    }

    //tests updateScore method returns false if answer requested does not exist
    @Test
    public void updateScoreInvalidAnswer() {

        testGame.addPlayerToGame(testPlayer);
        testGame.addOtherPlayersReference();
        testPlayer.addQuestionToIndex("A real question", 0);
        testPlayer.addAnswer("A real answer", "A real question");

        boolean test = testPlayer.updateScore("Not a real answer");
        assertEquals(false, test);

    }

    //tests voteForAnswer method
    @Test
    public void voteForAnswerTest() {
        //get question AND answers
        String question2 = testGame2.getGameQuestions().getQuestions()[1];
        String p1answer = "P1 answer";
        String p2answer = "P2 answer";
        testGame2.getInGamePlayers().get(0).addAnswer(p1answer, question2);
        testGame2.getInGamePlayers().get(1).addAnswer(p2answer, question2);

        //vote for answers
        int p1Score;
        int p2Score;
        p1Score = testGame2.voteForAnswer(p1answer, question2);
        p1Score = testGame2.voteForAnswer(p1answer, question2);
        p2Score = testGame2.voteForAnswer(p2answer, question2);

        //check updated scores
        assertEquals(2, p1Score);
        assertEquals(1, p2Score);
    }

    //tests voteForAnswer with invalid answer AND answer
    @Test
    public void voteForNonExistantAnswer() {
        //get question AND answer
        String question3 = testGame2.getGameQuestions().getQuestions()[2];
        String nonquestion = "Not a real question";
        String p1answer = "P1 answer";
        String nonAnswer = "Not a real answer";
        testGame2.getInGamePlayers().get(0).addAnswer(p1answer, question3);

        //vote for wrong answer
        int p1Score;
        p1Score = testGame2.voteForAnswer(nonAnswer, question3);
        assertEquals(-1, p1Score);

        //vote for non existant question
        p1Score = testGame2.voteForAnswer(p1answer, nonquestion);
        assertEquals(-1, p1Score);

        //vote for incorrect question
        p1Score = testGame2.voteForAnswer(p1answer, question3);
        assertEquals(-1, p1Score);
    }

    //tests getLeaderboard method
    @Test
    public void leaderboardTest() {
        //get question AND answers
        String question2 = testGame2.getGameQuestions().getQuestions()[1];
        String p1answer = "P1 answer";
        String p2answer = "P2 answer";
        testGame2.getInGamePlayers().get(0).addAnswer(p1answer, question2);
        testGame2.getInGamePlayers().get(1).addAnswer(p2answer, question2);

        //vote for answers
        testGame2.voteForAnswer(p1answer, question2);
        testGame2.voteForAnswer(p1answer, question2);
        testGame2.voteForAnswer(p2answer, question2);

        //check leaderboard
        Player[] leaderboard = testGame2.getLeaderBoard();
        assertEquals(p1, leaderboard[0]);
        assertEquals(p2, leaderboard[1]);
        //check player 3 is tied for last with 4
        boolean check = true;
        if(p3 == leaderboard[2] || p3 == leaderboard[3]){
        } else if(p4 == leaderboard[2] || p4 == leaderboard[3]){
        } else{
            check = false;
        }
        assertEquals(true, check);

    }

    //tests total functionality

    //what is this even testing???
    @Test
    public void MakeGame() {
        ArrayList<Player> playerList = new ArrayList<>();

        playerList.add(new Player("cedric"));
        playerList.add(new Player("doug"));
        playerList.add(new Player("brian"));
        playerList.add(new Player("dylan"));

        Game g = new Game(4, playerList);
    }

}
