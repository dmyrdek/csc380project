package csc380Project.game;

import csc380Project.controllers.WaitingLobbyController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main extends Application {
    public static void testDisplayQuestionsForRound(String[] roundQuestions) {
        if (roundQuestions != null) {
            for (String s : roundQuestions) {
                System.out.println(s);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        
        launch(args);

        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("cedric"));
        players.add(new Player("doug"));
        players.add(new Player("brian"));
        players.add(new Player("dylan"));

        Game g = new Game(4, players);
        ArrayList<String> p1q = g.getInGamePlayers().get(0).getQuestionsToAnswerForRound(3);
        g.getInGamePlayers().get(2).increaseVotesReceived();
        g.getInGamePlayers().get(2).increaseVotesReceived();
        g.getInGamePlayers().get(1).increaseVotesReceived();
        Player[] leaders = g.getLeaderBoard();
        String q1 = g.getGameQuestions().getQuestions()[0];
        g.getInGamePlayers().get(3).addAnswer("test", q1);
        g.getInGamePlayers().get(2).addAnswer("test2", q1);
        g.getInGamePlayers().get(0).addAnswer("t", g.getInGamePlayers().get(0).getQuestionsToAnswerForRound(0).get(0));
        String[] ans = g.getAllAnswersForQuestion(g.getInGamePlayers().get(0).getQuestionsToAnswerForRound(0).get(0));
        String[] answersToq1 = g.getAllAnswersForQuestion(q1);
        int votes = g.voteForAnswer("test", q1);
        votes = g.voteForAnswer("test", q1);
        leaders = g.getLeaderBoard();
        ArrayList<String> leaderBoardString = g.getLeaderBoardStrings();

        System.out.println();

        Scanner kb = new Scanner(System.in);

        QuestionPack qp = (new QuestionPack().addAllQuestions());
        for (int i = 0; i < qp.currentQuestions().size(); i++) {
            System.out.println(qp.currentQuestions().get(i));
        }

        /*
         * this block of code below creates a new game, add 4 players, loads the
         * appropriate number of questions for that game (based on rounds and players)
         * and assures that the questions are random everytime the game is run.
         */
        Game testGame = new Game(4, qp);
        testGame.addPlayerToGame("doug");
        testGame.addPlayerToGame("cedric");
        testGame.addPlayerToGame("bryan");
        testGame.addPlayerToGame("dylan");
        testGame.setThisGamesQuestions();

        // print all questions to be used for this instance of the game
        System.out.println("\n \nList of all the questions to be used for this game \n ");
        for (String s : testGame.getGameQuestions().getQuestions()) {
            System.out.println(s);
        }

        // gets the correct questions to be used for a specific round of play unless
        // round specified is outside of acceptable bounds
        System.out.println("\n \nlist of questions for specified round (3 right now) \n \n");
        String[] questionsForRound = testGame.getQuestionsForRound(3);
        testDisplayQuestionsForRound(questionsForRound);

        testGame.giveQuestionstoPlayers();
        System.out.println("\n \nsuccessfully gave all players their questions for the game\n\n");

        Player p1 = new Player("Player1");
        Player p2 = new Player("P2");
        
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setResizable(false);
        primaryStage.setOpacity(0.0);
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("HomeScreen.fxml"));
        primaryStage.setTitle("Questionnaires");
        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.show();
        root.requestFocus();
        primaryStage.setOpacity(1.0);
        WaitingLobbyController.setStage(primaryStage);
    }
}
