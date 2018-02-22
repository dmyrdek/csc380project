package sample;

import java.util.ArrayList;

public class Player {

    private String name;
    private int numVotesReceived;
    private int playerNumber;
    private ArrayList<Answer> answers;
    boolean isHost;

    public Player(String n){
        name =n;
        numVotesReceived =0;
        playerNumber=0;
        answers = new ArrayList<>();
        isHost = false;
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

    public ArrayList<Answer> getAnswers(){
        return this.answers;
    }


    public void increaseVotesReceived(){
        this.numVotesReceived++;
    }

    public void setPlayerNumber(){
        //look to see what other players exist in the game, and get smaller possible value left
        //need to implement when possible
    }

    public ArrayList<Answer> addAnswer(Answer a){
        answers.add(a);
        return answers;
    }

    public void setHostBool(boolean b){
        this.isHost = b;
    }





}
