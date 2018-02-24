package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.lang.reflect.InvocationTargetException;


public class Main extends Application {

    public static void main(String[] args) {


        QuestionPack qp= (new QuestionPack().addAllQuestions());
        for (int i = 0; i < qp.currentQuestions().size(); i++) {
            System.out.println(qp.currentQuestions().get(i));
        }


        /*
        this block of code below creates a new game, add 4 players, loads the appropriate
        number of questions for that game (based on rounds and players) and assures that the
        questions are random everytime the game is run.
        */
        Game testGame = new Game(2, qp);
        testGame.addPlayerToGame("doug");
        testGame.addPlayerToGame("cedric");
        testGame.addPlayerToGame("bryan");
        testGame.addPlayerToGame("dylan");
        testGame.gameQuestions = testGame.setGameQuestions();

        System.out.println();
        for (String s : testGame.gameQuestions.questions) {
            System.out.println(s);
        }



        //////---------testing stuff-----------------------










        ////-------------end test----------------------------



        Client c = new Client();
        launch(args);



    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane bp = new BorderPane();
        bp.setPadding(new Insets(10, 50, 50, 50));
        Scene scene = new Scene(bp);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("CSC380 Project");
        primaryStage.setScene(new Scene(root, 900, 500));
        //Button buttons = new Button();
        //buttons.setStyle("-fx-font: 22 Helvetica Neue");
        primaryStage.show();
    }
}
