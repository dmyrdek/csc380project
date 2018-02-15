package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;


public class Main extends Application {

    public static void main(String[] args) {


        QuestionPack qp= new QuestionPack();
        qp.getQuestionPack("questions.txt");
	    qp.getQuestionPack("questions2.txt");
        for (int i = 0; i < qp.currentQuestions().size(); i++) {
            System.out.println(qp.currentQuestions().get(i));
        }

        Client c = new Client();
        launch(args);

    }


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("CSC380 Project");
       primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();

    }

}
