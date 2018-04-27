package csc380Project.controllers;

import csc380Project.server.*;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Label;

import java.io.IOException;

public class CreateLobbyController {
    @FXML
    JFXTextField port_number_field;

    @FXML
    JFXTextField username_field;

    @FXML
    Label port_error;
    
    @FXML
    Label username_error;

    @FXML
    JFXSlider max_players;

    @FXML
    JFXSlider num_rounds;
    

    private static String portNumber = "";
    private static String username = "";
    private static int maxPlayers = 10;
    private static int numRounds = 10;

    @FXML
    public void submitPortNumber(KeyEvent event){
        portNumber = port_number_field.getText();
    }

    @FXML
    public void submitUsername(KeyEvent event){
        username = username_field.getText();
    }

    public static String getPortNumber(){
        return portNumber;
    }

    public static String getUsername(){
        return username;
    }

    public static int getMaxPlayers(){
        return maxPlayers;
    }

    public static int getNumRounds(){
        return numRounds;
    }

    public void backButtonPress(ActionEvent event) throws IOException {
        Parent homePageParent = FXMLLoader.load(getClass().getClassLoader().getResource("HomeScreen.fxml"));
        Scene homePage = new Scene(homePageParent);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(homePage);
        appStage.show();
        appStage.requestFocus();
    }

    public void createGameLaunchServer(ActionEvent event) throws IOException {

        //GameServer.createServer();
        //GameServer.main(null);
        //String[] arguments = new String[] {};
        //new SyncClients().main(arguments);


        if (!portNumber.equals("") && !username.equals("")){
            if (isInteger(portNumber)){
                new StartGameSever().start();
                
                Parent homePageParent = FXMLLoader.load(getClass().getClassLoader().getResource("WaitingLobby.fxml"));
                Scene homePage = new Scene(homePageParent);
                Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                appStage.setScene(homePage);

                Platform.runLater(
                    () -> {
                    appStage.show();
                    appStage.requestFocus();
                }
                );
            }
        } else {
            if (portNumber.equals("") || !isInteger(portNumber)){
                port_error.setVisible(true);
            } else {
                port_error.setVisible(false);
            }
            if (username.equals("")){
                username_error.setVisible(true);;
            } else{
                username_error.setVisible(false);;
            }
        }
    
    }

    @FXML
    public void initialize() {
        max_players.setValue(10);
        num_rounds.setValue(10);

        //maxNumberOfPlayers.getItems().addAll("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20");
        //numberOfRounds.getItems().addAll("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15");
        //maxNumberOfPlayers.setVisibleRowCount(10);
        //numberOfRounds.setVisibleRowCount(10);
    }

    private class StartGameSever extends Thread{
        public void run(){
            maxPlayers = (int) Math.round(max_players.getValue());
            numRounds = (int) Math.round(num_rounds.getValue());
            String[] arguments = new String[] {};
            new SyncClients(maxPlayers).main(arguments);
        }
    }

    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }
    
    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }
    
}