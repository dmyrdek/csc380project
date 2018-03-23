package csc380Project.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import java.io.IOException;



public class waitingLobbyController{

    @FXML
    JFXButton ready_button;

    private static Integer startTime = 80;
    private Integer countDownTime;
    private Timeline timeline;
    private String readyStatus = "Ready Up";
    private Timer timer = new Timer();


    @FXML
    public void initialize() {
        countDownTime = startTime;        
        ready_button.setText(countDownTime.toString() + " - Ready Up");
        timer.schedule(new TimerTask() {
            @Override
                public void run() {
                Platform.runLater(new Runnable() {
                   public void run() {
                        countDownTime--;
                        ready_button.setText(countDownTime.toString() + " - Ready Up");
        
                        if (countDownTime < 0)
                            timer.cancel();
                  }
                });
            }
            }, 1000, 1000);
    }


    public void backToCreateLobby(ActionEvent event) throws IOException {
        Parent homePageParent = FXMLLoader.load(getClass().getClassLoader().getResource("CreateLobby.fxml"));
        Scene homePage = new Scene(homePageParent);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(homePage);
        appStage.show();
    }

}