package csc380Project;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Player {

    private String name;
    private int numVotesReceived;
    private int playerNumber;
    private ArrayList<String> answers;
    boolean isHost;
    public ArrayList<Player> playersInGame;

    String[] questionsToAnswer;
    String [] answersToQuestions;

    public Player(String n){
        name =n;
        numVotesReceived =0;
        playerNumber=0;
        isHost = false;

        answersToQuestions = new String [400];
        questionsToAnswer = new String[400];
        playersInGame = new ArrayList<Player>();
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


    public void increaseVotesReceived(){
        this.numVotesReceived++;
    }

    public void setPlayerNumber(){
        //look to see what other players exist in the game, and get smaller possible value left
        //need to implement when possible
    }


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



    public void setHostBool(boolean b){
        this.isHost = b;
    }





}