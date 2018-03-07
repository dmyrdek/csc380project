package csc380Project;


import javafx.application.Application;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Parent;
        import javafx.scene.Scene;
        import javafx.stage.Stage;
        import javafx.scene.layout.BorderPane;
        import javafx.geometry.Insets;

        import java.util.Scanner;


public class Main extends Application {

    public static void main(String[] args) throws Exception {
        launch(args);

        Scanner kb = new Scanner(System.in);

        QuestionPack qp= (new QuestionPack().addAllQuestions());
        for (int i = 0; i < qp.currentQuestions().size(); i++) {
            System.out.println(qp.currentQuestions().get(i));
        }

        

        /*
        this block of code below creates a new game, add 4 players, loads the appropriate
        number of questions for that game (based on rounds and players) and assures that the
        questions are random everytime the game is run.
        */
        Game testGame = new Game(4, qp);
        testGame.addPlayerToGame("doug");
        testGame.addPlayerToGame("cedric");
        testGame.addPlayerToGame("bryan");
        testGame.addPlayerToGame("dylan");
        testGame.gameQuestions = testGame.setGameQuestions();

        //print all questions to be used for this instance of the game
        System.out.println("\n \nList of all the questions to be used for this game \n ");
        for (String s : testGame.gameQuestions.questions) {
            System.out.println(s);
        }

        //gets the correct questions to be used for a specific round of play unless round specified is outside of acceptable bounds
        System.out.println("\n \nlist of questions for specified round \n");
        testGame.displayQuestionsForRound(testGame.getQuestionsForRound(3));




        //////---------testing stuff-----------------------










        ////-------------end test----------------------------



        String command = "";
        System.out.println("type 1 for server, 2 for client");
        command = kb.nextLine();

        boolean proper = false;

        while(!proper) {
            if (command.equals("1")) {
                GameServer.createServer();
                proper = true;
            } else if (command.equals("2")) {
                GameClient.connectClient();
                proper = true;
            }
        }



    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane bp = new BorderPane();
        bp.setPadding(new Insets(10, 50, 50, 50));
        Scene scene = new Scene(bp);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));
        primaryStage.setTitle("CSC380 Project");
        primaryStage.setScene(new Scene(root, 900, 500));
        //Button buttons = new Button();
        //buttons.setStyle("-fx-font: 22 Helvetica Neue");
        primaryStage.show();
    }
}
