package csc380Project.game;

import java.util.ArrayList;

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
    public Game(int rounds){
        inGamePlayers = new ArrayList<Player>();
        gameQuestions = null;
        allSubmittedAnswers = new ArrayList<String>();
        numRounds = rounds;
        qp = new QuestionPack().addAllQuestions();
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
            int a = (int) Math.floor(Math.random() * qp.qArrayList.size());
            String question = this.qp.qArrayList.get(a);
            for (int i = 0; i < size; i++) {
                if (questionsForTheGame.getQuestions()[i].equals(question)) {
                    break;
                }
            }
            questionsForTheGame.getQuestions()[size] = question;
            size++;
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

    //methods to send out properly formatted strings to the chat

        /*

        send a current state of game round to everyone, and then once people vote then send
        back to server for the server to validate the moves/votes

         */


    ///all the networking stuff needs to be added


}
