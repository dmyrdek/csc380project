package sample;

import java.util.ArrayList;
import java.util.Random;

public class Game {

    ArrayList<Player> inGamePlayers;
    InGameQuestions gameQuestions;
    ArrayList<Answer> allSubmittedAnswers;
    QuestionPack qp;
    int numRounds;

    public Game (int rounds, QuestionPack q) {
        inGamePlayers = new ArrayList<>();
        gameQuestions = null;
        allSubmittedAnswers = new ArrayList<>();
        numRounds = rounds;
        qp = q;

    }


    public Player addPlayerToGame(String name) {
        Player newPlayer = new Player (name);
        inGamePlayers.add(newPlayer);
        return newPlayer;
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

            for (int i = 0; i< this.inGamePlayers.size(); i++){
                int selection = i+ ((roundNum-1)*this.inGamePlayers.size());
                roundQuestions[i] = this.gameQuestions.questions[selection];
            }

            //System.arraycopy(this.gameQuestions.questions,roundNum*this.inGamePlayers.size(), roundQuestions, 0, roundQuestions.length );


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











    ///all the networking stuff needs to be added


}