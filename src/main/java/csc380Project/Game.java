package csc380Project;

import java.util.ArrayList;

public class Game {

    ArrayList<Player> inGamePlayers;
    InGameQuestions gameQuestions;
    ArrayList<Answer> allSubmittedAnswers;
    QuestionPack qp;
    int numRounds;

    public Game (int rounds, QuestionPack q) {
        inGamePlayers = new ArrayList<Player>();
        gameQuestions = null;
        allSubmittedAnswers = new ArrayList<Answer>();
        numRounds = rounds;
        qp = q;

    }


    public Player addPlayerToGame(String name) {
        Player newPlayer = new Player (name);
        inGamePlayers.add(newPlayer);
        return newPlayer;
    }

    public Player addPlayerToGame(Player p) {
     inGamePlayers.add(p);
     return p;
    }

    public void addOtherPlayersReference(){
        for (Player p : inGamePlayers){
            for (Player q : inGamePlayers){
                if (p != q){
                    p.otherPlayers.add(p);
                }
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
            for (int i =0; i<size; i++) {
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

        if (roundNum <= this.numRounds && 0<roundNum) {
            String [] roundQuestions = new String [this.inGamePlayers.size()];
            System.arraycopy(this.gameQuestions.questions, (roundNum-1)*this.inGamePlayers.size(), roundQuestions, 0, this.inGamePlayers.size());
            return roundQuestions;
        } else {
            System.out.println("tried to get questions for a round that does not exist in this game");
            return null;
        }

    }

    public void displayQuestionsForRound(String [] roundQuestions) {
        if (roundQuestions != null) {
            for (String s : roundQuestions) {
                System.out.println(s);
            }
        }
    }


        /*

        send a current state of game round to everyone, and then once people vote then send
        back to server for the server to validate the moves/votes

         */








    ///all the networking stuff needs to be added


}
