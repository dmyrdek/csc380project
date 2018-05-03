package csc380Project;

import csc380Project.game.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

        //check player 3 is tied for last
        boolean check = false;
        if(p3 == leaderboard[2] || p3 == leaderboard[3]){
            check = true;
        }
        assertEquals(true, check);

        //check player 4 is tied for last
        check = false;
        if(p4 == leaderboard[2] || p4 == leaderboard[3]){
            check = true;
        }
        assertEquals(true, check);

    }

    //tests getQuestionsToAnswerForRound method for players
    @Test
    public void getQuestionToAnswerForRoundTest() {

        testGame2.giveQuestionstoPlayers();
        int roundNum = 0;

        boolean test1 = false;
        boolean test2 = false;
        boolean test3 = false;
        boolean test4 = false;
        boolean test5 = false;
        boolean test6 = false;
        boolean test7 = false;
        boolean test8 = false;

        //questions
        ArrayList<String> questionsForP1 = new ArrayList<String>();
        ArrayList<String> questionsForP2 = new ArrayList<String>();
        ArrayList<String> questionsForP3 = new ArrayList<String>();
        ArrayList<String> questionsForP4 = new ArrayList<String>();

        ArrayList<String> roundQuestionsForP1 = new ArrayList<String>();
        ArrayList<String> roundQuestionsForP2 = new ArrayList<String>();
        ArrayList<String> roundQuestionsForP3 = new ArrayList<String>();
        ArrayList<String> roundQuestionsForP4 = new ArrayList<String>();

        String q1ForP1 = "";
        String q2ForP1 = "";
        String q1ForP2 = "";
        String q2ForP2 = "";
        String q1ForP3 = "";
        String q2ForP3 = "";
        String q1ForP4 = "";
        String q2ForP4 = "";

        //get the questions for all players
        for(int x = 0; x < testGame2.getInGamePlayers().get(0).getQuestions().length; x++){
            if(testGame2.getInGamePlayers().get(0).getQuestions()[x] != null) {
                questionsForP1.add(testGame2.getInGamePlayers().get(0).getQuestions()[x]);
            }
        }
        for(int x = 0; x < testGame2.getInGamePlayers().get(1).getQuestions().length; x++){
            if(testGame2.getInGamePlayers().get(1).getQuestions()[x] != null) {
                questionsForP2.add(testGame2.getInGamePlayers().get(1).getQuestions()[x]);
            }
        }
        for(int x = 0; x < testGame2.getInGamePlayers().get(2).getQuestions().length; x++){
            if(testGame2.getInGamePlayers().get(2).getQuestions()[x] != null) {
                questionsForP3.add(testGame2.getInGamePlayers().get(2).getQuestions()[x]);
            }        }
        for(int x = 0; x < testGame2.getInGamePlayers().get(3).getQuestions().length; x++){
            if(testGame2.getInGamePlayers().get(3).getQuestions()[x] != null) {
                questionsForP4.add(testGame2.getInGamePlayers().get(3).getQuestions()[x]);
            }
        }

        //get questions for 1st round for all players
        q1ForP1 = questionsForP1.get(0);
        q2ForP1 = questionsForP1.get(1);
        q1ForP2 = questionsForP2.get(0);
        q2ForP2 = questionsForP2.get(1);
        q1ForP3 = questionsForP3.get(0);
        q2ForP3 = questionsForP3.get(1);
        q1ForP4 = questionsForP4.get(0);
        q2ForP4 = questionsForP4.get(1);

        roundQuestionsForP1 = testGame2.getInGamePlayers().get(0).getQuestionsToAnswerForRound(roundNum);
        roundQuestionsForP2 = testGame2.getInGamePlayers().get(1).getQuestionsToAnswerForRound(roundNum);
        roundQuestionsForP3 = testGame2.getInGamePlayers().get(2).getQuestionsToAnswerForRound(roundNum);
        roundQuestionsForP4 = testGame2.getInGamePlayers().get(3).getQuestionsToAnswerForRound(roundNum);

        //check questions
        if(q1ForP1.equals(roundQuestionsForP1.get(0)) || q1ForP1.equals(roundQuestionsForP1.get(1))){
            test1 = true;
        }
        if(q2ForP1.equals(roundQuestionsForP1.get(0)) || q2ForP1.equals(roundQuestionsForP1.get(1))){
            test2 = true;
        }
        if(q1ForP2.equals(roundQuestionsForP2.get(0)) || q1ForP2.equals(roundQuestionsForP2.get(1))){
            test3 = true;
        }
        if(q2ForP2.equals(roundQuestionsForP2.get(0)) || q2ForP2.equals(roundQuestionsForP2.get(1))){
            test4 = true;
        }
        if(q1ForP3.equals(roundQuestionsForP3.get(0)) || q1ForP3.equals(roundQuestionsForP3.get(1))){
            test5 = true;
        }
        if(q2ForP3.equals(roundQuestionsForP3.get(0)) || q2ForP3.equals(roundQuestionsForP3.get(1))){
            test6 = true;
        }
        if(q1ForP4.equals(roundQuestionsForP4.get(0)) || q1ForP3.equals(roundQuestionsForP4.get(1))){
            test7 = true;
        }
        if(q2ForP4.equals(roundQuestionsForP4.get(0)) || q2ForP3.equals(roundQuestionsForP4.get(1))){
            test8 = true;
        }

        assertEquals(true, test1);
        assertEquals(true, test2);
        assertEquals(true, test3);
        assertEquals(true, test4);
        assertEquals(true, test5);
        assertEquals(true, test6);
        assertEquals(true, test7);
        assertEquals(true, test8);

        //round 2
        roundNum++;

        //reset checks
        test1 = false;
        test2 = false;
        test3 = false;
        test4 = false;
        test5 = false;
        test6 = false;
        test7 = false;
        test8 = false;

        //get questions for 1st round for all players
        q1ForP1 = questionsForP1.get(2);
        q2ForP1 = questionsForP1.get(3);
        q1ForP2 = questionsForP2.get(2);
        q2ForP2 = questionsForP2.get(3);
        q1ForP3 = questionsForP3.get(2);
        q2ForP3 = questionsForP3.get(3);
        q1ForP4 = questionsForP4.get(2);
        q2ForP4 = questionsForP4.get(3);

        roundQuestionsForP1 = testGame2.getInGamePlayers().get(0).getQuestionsToAnswerForRound(roundNum);
        roundQuestionsForP2 = testGame2.getInGamePlayers().get(1).getQuestionsToAnswerForRound(roundNum);
        roundQuestionsForP3 = testGame2.getInGamePlayers().get(2).getQuestionsToAnswerForRound(roundNum);
        roundQuestionsForP4 = testGame2.getInGamePlayers().get(3).getQuestionsToAnswerForRound(roundNum);

        //check questions
        if(q1ForP1.equals(roundQuestionsForP1.get(0)) || q1ForP1.equals(roundQuestionsForP1.get(1))){
            test1 = true;
        }
        if(q2ForP1.equals(roundQuestionsForP1.get(0)) || q2ForP1.equals(roundQuestionsForP1.get(1))){
            test2 = true;
        }
        if(q1ForP2.equals(roundQuestionsForP2.get(0)) || q1ForP2.equals(roundQuestionsForP2.get(1))){
            test3 = true;
        }
        if(q2ForP2.equals(roundQuestionsForP2.get(0)) || q2ForP2.equals(roundQuestionsForP2.get(1))){
            test4 = true;
        }
        if(q1ForP3.equals(roundQuestionsForP3.get(0)) || q1ForP3.equals(roundQuestionsForP3.get(1))){
            test5 = true;
        }
        if(q2ForP3.equals(roundQuestionsForP3.get(0)) || q2ForP3.equals(roundQuestionsForP3.get(1))){
            test6 = true;
        }
        if(q1ForP4.equals(roundQuestionsForP4.get(0)) || q1ForP3.equals(roundQuestionsForP4.get(1))){
            test7 = true;
        }
        if(q2ForP4.equals(roundQuestionsForP4.get(0)) || q2ForP3.equals(roundQuestionsForP4.get(1))){
            test8 = true;
        }

        assertEquals(true, test1);
        assertEquals(true, test2);
        assertEquals(true, test3);
        assertEquals(true, test4);
        assertEquals(true, test5);
        assertEquals(true, test6);
        assertEquals(true, test7);
        assertEquals(true, test8);

    }

    //tests whoAnsweredQuestion method
    @Test
    public void whoAnsweredQuestionTest() {

        //testing question 2 (for players 1 and 2)
        String questionTest = testGame2.getGameQuestions().getQuestions()[1];

        //add answers to test question
        String p1answer = "P1 answer";
        String p2answer = "P2 answer";
        testGame2.getInGamePlayers().get(0).addAnswer(p1answer, questionTest);
        testGame2.getInGamePlayers().get(1).addAnswer(p2answer, questionTest);

        //find out who answered the question
        //test p1
        String p1NameTest = testGame2.whoAnsweredQuestion(p1answer, questionTest);
        String p1NameActual = testGame2.getInGamePlayers().get(0).getName();
        assertEquals(p1NameActual, p1NameTest);

        //test p2
        String p2NameTest = testGame2.whoAnsweredQuestion(p2answer, questionTest);
        String p2NameActual = testGame2.getInGamePlayers().get(1).getName();

    }

    //tests whoAnsweredQuestion method with invalid input
    @Test
    public void whoAnsweredQuestionInvalidTest(){

        //testing question 2
        String questionTest = testGame2.getGameQuestions().getQuestions()[1];

        //invalid answer
        String nonAnswer = "not a real answer";

        //test invalid answer
        String test = testGame2.whoAnsweredQuestion(nonAnswer, questionTest);
        assertEquals(null, test);

        //invalid question
        String nonQuestion = "not a real questsion";

        //test invalid question
        test = testGame2.whoAnsweredQuestion(nonAnswer, nonQuestion);
        assertEquals(null, test);

    }


    // bryan this test was incredibly poorly done please redo it so we don't have to type in extra spaces
    // when we wanna change stuff
    /*

    //tests getLeaderboardStrings method
    @Test
    public void getLeaderBoardStringsTest() {

        //get question2 for players 1 and 2
        String question2 = testGame2.getGameQuestions().getQuestions()[1];

        //submit answers for players 1 and 2
        String p1answer = "player 1 answer";
        String p2answer = "player 2 answer";
        testGame2.getInGamePlayers().get(0).addAnswer(p1answer, question2);
        testGame2.getInGamePlayers().get(1).addAnswer(p2answer, question2);

        //vote twice for player 1
        testGame2.voteForAnswer(p1answer, question2);
        testGame2.voteForAnswer(p1answer, question2);

        //vote once for player 2
        testGame2.voteForAnswer(p2answer, question2);

        //output for players
        String p1output = "Player 1          2";
        String p2output = "Player 2          1";
        String p3output = "Player 3          0";
        String p4output = "Player 4          0";

        //get leaderboard arraylist of Strings
        ArrayList<String> leaderboard = testGame2.getLeaderBoardStrings();

        //check 1st place
        assertEquals(p1output, leaderboard.get(0));

        //check 2nd place
        assertEquals(p2output, leaderboard.get(1));

        //check that player 3 has the correct output in either 3rd or 4th place
        boolean test = false;
        if(leaderboard.get(2).equals(p3output) || leaderboard.get(3).equals(p3output)){
            test = true;
        }
        assertEquals(true, test);

        //check that player 4 has the correct output in either 3rd or 4th place
        test = false;
        if(leaderboard.get(2).equals(p4output) || leaderboard.get(3).equals(p4output)){
            test = true;
        }
        assertEquals(true, test);

    }
    */

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
        g.giveQuestionstoPlayers();

    }

    @Test
    public void FunnyResponseTest() {

        testGame.giveQuestionstoPlayers();
        String q = testGame.getInGamePlayers().get(0).getQuestionsToAnswerForRound(0).get(0);
        testGame.getInGamePlayers().get(0).addAnswer("test", q);
        String [] ans = testGame.getAllAnswersForQuestion(q);

        assertEquals(ans[0], "test");
        assertNotNull(ans[0]);

    }

}
