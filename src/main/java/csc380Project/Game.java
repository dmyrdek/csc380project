package csc380Project;

import java.util.ArrayList;

public class Game {

    ArrayList<Player> inGamePlayers;
    InGameQuestions gameQuestions;
    ArrayList<String> allSubmittedAnswers;
    QuestionPack qp;
    int numRounds;
    static int maxPlayers;

    public Game(int rounds, QuestionPack q) {
        inGamePlayers = new ArrayList<Player>();
        gameQuestions = null;
        allSubmittedAnswers = new ArrayList<String>();
        numRounds = rounds;
        qp = q;

    }

    public static int getMaxPlayers() {
        return maxPlayers;
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
        questionsForTheGame.questions[0] = qp.qArrayList.get(r);
        int size = 1;


        while (size < totalQuestions) {
            int a = (int) Math.floor(Math.random() * qp.qArrayList.size());
            String question = this.qp.qArrayList.get(a);
            for (int i = 0; i < size; i++) {
                if (questionsForTheGame.questions[i].equals(question)) {
                    break;
                }
            }
            questionsForTheGame.questions[size] = question;
            size++;
        }

        return questionsForTheGame;
    }


    public String[] getQuestionsForRound(int roundNum) {

        if (roundNum <= this.numRounds && 0 < roundNum) {
            String[] roundQuestions = new String[this.inGamePlayers.size()];
            System.arraycopy(this.gameQuestions.questions, (roundNum - 1) * this.inGamePlayers.size(), roundQuestions, 0, this.inGamePlayers.size());
            return roundQuestions;
        } else {
            System.out.println("tried to get questions for a round that does not exist in this game");
            return null;
        }

    }

    public void displayQuestionsForRound(String[] roundQuestions) {
        if (roundQuestions != null) {
            for (String s : roundQuestions) {
                System.out.println(s);
            }
        }
    }


    //assign the correct questions to each player for the ENTIRE GAME. Question is placed in
    //the corresponding spot to where the question is in the inGameQuestions list.
    //ex, for players who have been assigned question 4, their own list of questions will have the
    //correct question in slot 4, and no other players will have a question in that spot
    public boolean giveQuestionstoPlayers() {

        //just a check to see if game is valid
        if (numRounds!= 0 && this.inGamePlayers.size() != 0 &&
                this.gameQuestions.questions.length != 0 ){


            int numPlayers = this.inGamePlayers.size();
            for (int playerNum = 0; playerNum < numPlayers; playerNum++) {
                Player temp = this.inGamePlayers.get(playerNum);

                for (int roundNum = 0; roundNum < numRounds; roundNum++) {
                    //if its not the last player, assign questions normally
                    if (playerNum < numPlayers -1 ) {

                        int index = (roundNum * numPlayers) + playerNum;
                        temp.addQuestionToIndex(this.gameQuestions.questions[index], index);
                        temp.addQuestionToIndex(this.gameQuestions.questions[index + 1], index + 1);

                        //if its the last player, add questions differently so that the math works out
                    } else {
                        int q1 = (roundNum * numPlayers) + playerNum;
                        int q2 = (roundNum * numPlayers);

                        temp.addQuestionToIndex(this.gameQuestions.questions[q1], q1);
                        temp.addQuestionToIndex(this.gameQuestions.questions[q2], q2);

                    }
                }
            }
            return true;
        } else {
            System.out.println("Game is not valid");
            return false;
        }


    }


        /*

        send a current state of game round to everyone, and then once people vote then send
        back to server for the server to validate the moves/votes

         */


    ///all the networking stuff needs to be added


}
