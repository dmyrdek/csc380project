package csc380Project.game;

import java.util.ArrayList;
import java.util.Collections;
import java.time.format.DateTimeFormatter;

public class Game {

    private ArrayList<Player> inGamePlayers;
    private InGameQuestions gameQuestions;
    private ArrayList<String> allSubmittedAnswers;
    private QuestionPack qp;
    private int numRounds;
    private static int maxPlayers;

    public Game(int rounds, QuestionPack q) {
        inGamePlayers = new ArrayList<Player>();
        gameQuestions = null;
        allSubmittedAnswers = new ArrayList<String>();
        numRounds = rounds;
        qp = q;

    }


    /*
    *
    *
    *  USE THIS CONSTRUCTOR, THE ONE ABOVE WILL NOT WORK WITH THE WAY WE ARE MAKING THE GAME
    *
     */

    public Game(int rounds, ArrayList<Player> players){
        qp = new QuestionPack().addAllQuestions();
        inGamePlayers = players;
        numRounds = rounds;
        gameQuestions = this.setGameQuestions();
        allSubmittedAnswers = new ArrayList<String>();
        this.giveQuestionstoPlayers();
    }


    public Game(int rounds, ArrayList<Player> players, String questionPackFile){
        qp = new QuestionPack().addFromFile(questionPackFile);
        inGamePlayers = players;
        numRounds = rounds;
        gameQuestions = this.setGameQuestions();
        allSubmittedAnswers = new ArrayList<String>();
        this.giveQuestionstoPlayers();
    }

    public static int getMaxPlayers() {
        return maxPlayers;
    }

    public InGameQuestions getGameQuestions(){
        return gameQuestions;
    }

    public void setThisGamesQuestions(){
        this.gameQuestions = setGameQuestions();
    }

    public ArrayList<Player> getInGamePlayers(){
        return inGamePlayers;
    }

    public int getNumOfRounds(){
        return numRounds;
    }

    public ArrayList<String> getAllSubmittedAnswers() { return allSubmittedAnswers; }



    //add a player to the game using a string (not sure if we will use this method or the other one
    public Player addPlayerToGame(String name) {
        Player newPlayer = new Player(name);
        inGamePlayers.add(newPlayer);
        return newPlayer;
    }

    //add a player object to the game
    public Player addPlayerToGame(Player p) {
        inGamePlayers.add(p);
        return p;
    }


    //add a reference to every player in each player object (including themself)
    public void addOtherPlayersReference() {
        for (Player p : inGamePlayers) {
            for (Player q : inGamePlayers) {
                p.playersInGame.add(q);
            }
        }
    }


    public InGameQuestions setGameQuestions() {
        int totalQuestions = this.numRounds * this.inGamePlayers.size();
        int r = (int) Math.floor(Math.random() * qp.qArrayList.size());
        InGameQuestions questionsForTheGame = new InGameQuestions(totalQuestions);
        questionsForTheGame.getQuestions()[0] = qp.qArrayList.get(r);
        int size = 1;


        while (size < totalQuestions) {
            boolean match = false;
            int a = (int) Math.floor(Math.random() * qp.qArrayList.size());
            String question = this.qp.qArrayList.get(a);
            for (int i = 0; i < size; i++) {
                if (questionsForTheGame.getQuestions()[i].equals(question)) {
                    match = true;
                    break;
                }
            }
            if (!match){
                questionsForTheGame.getQuestions()[size] = question;
                size++;
            }
        }

        return questionsForTheGame;
    }


    public String[] getQuestionsForRound(int roundNum) {

        if (roundNum <= this.numRounds && 0 < roundNum) {
            String[] roundQuestions = new String[this.inGamePlayers.size()];
            System.arraycopy(this.gameQuestions.getQuestions(), (roundNum - 1) * this.inGamePlayers.size(), roundQuestions, 0, this.inGamePlayers.size());
            return roundQuestions;
        } else {
            System.out.println("tried to get questions for a round that does not exist in this game");
            return null;
        }

    }

    public String [] getAllAnswersForQuestion(String question){
        String [] answers = new String [2];
        int count=0;
        int numQuestions = this.inGamePlayers.size()*this.getNumOfRounds();
        for (Player p : inGamePlayers){
            for (int i = 0; i< numQuestions; i++){
                if (p.getQuestions()[i] != null){
                    if (p.getQuestions()[i].equals(question)){
                        if (p.getAnswerAtIndex(i) == null || p.getAnswerAtIndex(i).equals("")) {
                            answers[count] = funnyResponse(p.getName());
                            count++;
                        } else {
                            answers[count] = p.getAnswerAtIndex(i);
                            count++;
                        }
                    }
                }
            }
        }
        return answers;
    }



    public String funnyResponse(String name) {
        int a = (int) Math.floor(Math.random() * 7);

        if (a == 0) {
            return "~That's awkward... Looks like " + name + " doesn't know how to type on their computer";
        } else if (a == 1) {
            return "~" + name + " didn't submit an answer. DEFINITELY make fun of them for being a sore loser. They didn't answer their question";
        } else if (a == 2) {
            return "~" + name +  " didn't submit an answer this round. They might be dropping a couple of kids off at the pool...";
        } else if (a == 3) {
            return "~Looks like " + name + "'s mom was yelling at them for playing video games past their " +
                    java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("hh:00")) + " curfew. They didn't submit an answer";
        } else if (a == 4){
            return "~" + name + " is out mowing their driveway. They didn't answer the question";
        } else if (a == 5) {
            return "~" + name + " is out painting their grass green. They didn't answer the question";
        } else if (a ==6) {
            return "~" + name + " fell off their chair and died of laughter. They didn't answer the question";
        } else {
            return "~" + name + " didn't submit an answer this round. You should probably make fun of them";
        }

    }




    //assign the correct questions to each player for the ENTIRE GAME. Question is placed in
    //the corresponding spot to where the question is in the inGameQuestions list.
    //ex, for players who have been assigned question 4, their own list of questions will have the
    //correct question in slot 4, and no other players will have a question in that spot
    public boolean giveQuestionstoPlayers() {

        //just a check to see if game is valid
        if (numRounds!= 0 && this.inGamePlayers.size() != 0 &&
                this.gameQuestions.getQuestions().length != 0 ){


            int numPlayers = this.inGamePlayers.size();
            for (int playerNum = 0; playerNum < numPlayers; playerNum++) {
                Player temp = this.inGamePlayers.get(playerNum);

                for (int roundNum = 0; roundNum < numRounds; roundNum++) {
                    //if its not the last player, assign questions normally
                    if (playerNum < numPlayers -1 ) {

                        int index = (roundNum * numPlayers) + playerNum;
                        temp.addQuestionToIndex(this.gameQuestions.getQuestions()[index], index);
                        temp.addQuestionToIndex(this.gameQuestions.getQuestions()[index + 1], index + 1);

                        //if its the last player, add questions differently so that the math works out
                    } else {
                        int q1 = (roundNum * numPlayers) + playerNum;
                        int q2 = (roundNum * numPlayers);

                        temp.addQuestionToIndex(this.gameQuestions.getQuestions()[q1], q1);
                        temp.addQuestionToIndex(this.gameQuestions.getQuestions()[q2], q2);

                    }
                }
            }
            return true;
        } else {
            System.out.println("Game is not valid");
            return false;
        }
    }

    public int voteForAnswer(String answer, String question){
        int numQuestions = this.numRounds*this.inGamePlayers.size();
        for (Player p: inGamePlayers){
            for (int i = 0; i<numQuestions; i++){
                if (p.getAnswerAtIndex(i) != null){
                    if (p.getAnswerAtIndex(i).equals(answer) && p.getQuestionAtIndex(i).equals(question)){
                        p.increaseVotesReceived();
                        return p.getNumVotesReceived();
                    }
                }
            }
        }

        // no answer is found, no question found, or question and answer don't match, return -1
        return -1;
    }

    //
    public Player[] getLeaderBoard (){
        Player[] leaderboard = new Player [this.getInGamePlayers().size()];
        ArrayList<Player> players = this.inGamePlayers;
        Collections.sort(players);
        for (int i = 0; i<leaderboard.length; i++) {
            leaderboard[i] = players.get(i);
        }
        return leaderboard;
    }

    public String toString(){
        return "total Rounds: " + numRounds + " numPlayers: " + this.inGamePlayers.size() + " Player 1 is: " + this.inGamePlayers.get(0).toString() +" question 1 for player 1" + this.inGamePlayers.get(0).getQuestionAtIndex(0);
    }


    public String whoAnsweredQuestion(String ans, String question){
        for (Player p : inGamePlayers){
            for (int i = 0; i<p.getQuestions().length; i++){
                if (p.getQuestions()[i]!=null){
                    if(p.getAnswerAtIndex(i) != null) {
                        if (p.getQuestionAtIndex(i).equals(question) && p.getAnswerAtIndex(i).equals(ans)) {
                            return p.getName();
                        }
                    }
                }
            }
        }
        return null;
    }

    public ArrayList<String> getLeaderBoardStrings(){
        ArrayList<String> playerLeaderBoard = new ArrayList<>();
        ArrayList<Player> players = this.inGamePlayers;
        Collections.sort(players);
        for (Player p: players){
            playerLeaderBoard.add(p.displayForLeaderBoard(this.inGamePlayers.size()));
        }
        return playerLeaderBoard;
    }
    



}
