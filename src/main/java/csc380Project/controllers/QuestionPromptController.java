package csc380Project.controllers;

import java.sql.SQLOutput;
import java.util.ArrayList;

import csc380Project.server.*;
import java.util.ArrayList;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.scene.text.Text;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyEvent;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import java.lang.Runnable;
import java.io.*;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class QuestionPromptController implements Observer{

    @FXML
    JFXTextArea question_prompt;

    @FXML
    TextFlow chat_area;

    @FXML
    JFXTextField message_field;

    @FXML
    JFXButton submit_button;

    @FXML
    JFXTextArea answer_prompt;

    private ChatAccess chatAccess;
    private static String port;
    private static BooleanProperty isQuestionPromptLoaded = new SimpleBooleanProperty(false);
    private String questionAnswer = "";
    //private boolean allPlayersReady = false;
    private static BooleanProperty allPlayersSubmitted = new SimpleBooleanProperty(false);
    private int submittedPlayerSize = 0;

    public static void setIsQuestionPromptLoadedToTrue(){
        isQuestionPromptLoaded.set(true);
    }

    @FXML
    public void initialize() {

        question_prompt.setMouseTransparent(true);

        QuestionPromptController current = this;
        isQuestionPromptLoaded.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    chatAccess = WaitingLobbyController.getChatAccess();
                    chatAccess.deleteObservers();
                    chatAccess.addObserver(current);
                    
                    for (Text t: WaitingLobbyController.getTexts()){
                        chat_area.getChildren().add(t);
                    }

                    chatAccess.send("`inQuestionPrompt");
                }
            }
        });

        allPlayersSubmitted.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    
                }
            }
        });
    }

    @FXML
    public void getAnswer(KeyEvent event){
        questionAnswer = answer_prompt.getText();
        //chatAccess.send("~" + questionAnswer);
    }

    public void submit(ActionEvent event){
        submit_button.setDisable(true);
        //submit_button = "Submitted!";
        chatAccess.send("`submitted");
        chatAccess.send("~" + questionAnswer);
    }

    public void sendMessage(ActionEvent event){
        String str = message_field.getText();
        if (str != null && str.trim().length() > 0 && !str.startsWith("{") && !str.startsWith("}") && !str.startsWith("|") && !str.startsWith("~") && !str.startsWith("`"))
            chatAccess.send(str);
        message_field.selectAll();
        message_field.requestFocus();
        message_field.setText("");
    }

    public void update(Observable o, Object arg) {
        final Object finalArg = arg;
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                //If income message starts with a "}" then it is a name, add it to the list
                if (finalArg.toString().startsWith("}")){
                    //names.add(finalArg.toString().substring(1));
                }else if(finalArg.toString().startsWith("|")){
                    if (finalArg.toString().substring(1).equals("1")){
                        allPlayersSubmitted.set(true);
                    }
                    submit_button.setText(finalArg.toString().substring(1) + " - " + "Submit");
                }else if (finalArg.toString().startsWith("~")){
                    Text text = new Text(finalArg.toString().substring(1)+"\n");
                    text.setFill(Color.SKYBLUE);
                    chat_area.getChildren().add(text);
                }else if(finalArg.toString().startsWith("`")){
                    String str = finalArg.toString().substring(1);
                    if (str.equals("submitted")){
                        submittedPlayerSize++;
                        if (submittedPlayerSize == WaitingLobbyController.names.size()){
                            allPlayersSubmitted.set(true);
                        }
                    }
                }else if(finalArg.toString().startsWith("{")){
                    question_prompt.selectAll();
                    question_prompt.setText(finalArg.toString().substring(1));
                }
                
                else{
                    //Message history will store all chat history in a String we will locally cache to be read inbetween scenes to keep chat saved.
                    Text text = new Text(finalArg.toString()+"\n");
                    text.setFill(Color.WHITE);
                    chat_area.getChildren().add(text);
                }
            }
        });
    }
}