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









    ///all the networking stuff needs to be added


}
