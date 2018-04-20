package csc380Project.game;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;

public class Player implements Comparable {

    private String name;
    private int numVotesReceived;
    private int playerNumber;
    private boolean isHost;
    public ArrayList<Player> playersInGame;

    private String[] questionsToAnswer;
    private String[] answersToQuestions;

    public Player(String n){
        name =n;
        numVotesReceived =0;
        playerNumber=-1;
        isHost = false;

        answersToQuestions = new String [400]; //max at total questions (344)
        questionsToAnswer = new String[400];
        playersInGame = new ArrayList<Player>();
    }

    @Override
    public int compareTo(Object other){
        return ((Player) other).getNumVotesReceived()-this.numVotesReceived;
    }


    public String getName(){
        return this.name;
    }

    public int getNumVotesReceived(){
        return this.numVotesReceived;
    }

    public int getPlayerNumber(){
        return playerNumber;
    }

    public String[] getAnswers(){
        return this.answersToQuestions;
    }

    public String[] getQuestions() { return this.questionsToAnswer; }

    public boolean getHostStatus() { return this.isHost; }

    public void increaseVotesReceived(){
        this.numVotesReceived++;
    }

    //method works if addOtherPlayersToReference method in Game class run before this
    public void setPlayerNumber(){
        for (int x = 0; x < playersInGame.size(); x++) {
            if (playersInGame.get(x).getName().equals(this.getName())) {
                playerNumber = x;
            }
        }
        //look to see what other players exist in the game, and get smaller possible value left
        //need to implement when possible
    }

    //not using?
    public String[] addAnswerToIndex(String ans, int index){
        answersToQuestions[index] = ans;
        return answersToQuestions;
    }

    public String[] addQuestionToIndex(String q, int index){
        questionsToAnswer[index] = q;
        return questionsToAnswer;
    }

    public String getQuestionAtIndex(int index){
        return this.questionsToAnswer[index];
    }

    public String getAnswerAtIndex(int index){
        return this.answersToQuestions[index];
    }

    public void setHostStatus(boolean b){
        this.isHost = b;
    }


    //allow users to add answers at a specified index that matches the question number
    public boolean addAnswer(String ans, String question){
        //look for index of matching question, and assign answer to same index
        for (int i = 0; i < this.questionsToAnswer.length; i++) {
            if (this.questionsToAnswer[i] != null){
                if (this.questionsToAnswer[i].equals(question)){
                    this.answersToQuestions[i] = ans;
                    return true;
                }
            }
        }

        // no matching question was found, so return false
        return false;

    }

    public Player findPlayerInList(String playerName) {
        for (int i = 0; i<playersInGame.size(); i++) {
            if (playersInGame.get(i).name.equals(playerName)){
                return playersInGame.get(i);
            }
        }
        return null;
    }

    public boolean updateScore(String answer){
        for (Player p : playersInGame) {
            if(p != null) {
                for (String a : p.getAnswers()) {
                    if (a != null) {
                        if (a.equals(answer)) {
                            p.increaseVotesReceived();
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    public String toString() {
        return this.name + " votes received: " + this.getNumVotesReceived();
    }

    //
    public ArrayList<String> getQuestionsToAnswerForRound(int roundNum) {
        ArrayList<String> questionsForRound = new ArrayList<>();
        ArrayList<String> allQuestions = new ArrayList<>();
        int questionsAlreadyAnswered = (roundNum) *2;

        for (int i = 0; i <this.questionsToAnswer.length; i++) {
            if (questionsToAnswer[i] != null) {
                allQuestions.add(questionsToAnswer[i]);
            }
        }
        questionsForRound.add(allQuestions.get(questionsAlreadyAnswered));
        questionsForRound.add(allQuestions.get(questionsAlreadyAnswered+1));

        return questionsForRound;
    }


}