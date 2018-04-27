package csc380Project.controllers;

import java.sql.SQLOutput;
import java.util.ArrayList;

import csc380Project.server.*;
import java.util.ArrayList;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import com.jfoenix.controls.JFXScrollPane;

public class VotingResultsController implements Observer{

    @FXML
    JFXTextArea question_prompt;

    @FXML
    JFXTextArea answer_prompt_one;

    @FXML
    JFXTextArea answer_prompt_two;

    @FXML
    TextFlow chat_area;

    @FXML
    JFXTextField message_field;

    @FXML
    JFXButton submit_button;

    @FXML
    Label voting_results_one_name;

    @FXML
    Label voting_results_one_votes;

    @FXML
    Label voting_results_two_name;

    @FXML
    Label voting_results_two_votes;

    @FXML
    ScrollPane chat_scroll_pane;


    private ChatAccess chatAccess;
    private static ArrayList<Text> texts = new ArrayList<>();
    private static BooleanProperty isVotingResultsLoaded = new SimpleBooleanProperty(false);
    private static BooleanProperty allPlayersSubmitted = new SimpleBooleanProperty(false);
    private int submittedPlayerSize = 0;
    private static Stage myStage;
    private String voteOption = "";
    private int answerOneCounter = 0;
    private int answerTwoCounter = 0;
    
    @FXML
    public void initialize() throws IOException {
        question_prompt.setMouseTransparent(true);

        VotingResultsController current = this;

        chat_scroll_pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chat_scroll_pane.vvalueProperty().bind((chat_area.heightProperty()));
        JFXScrollPane.smoothScrolling(chat_scroll_pane);

        isVotingResultsLoaded.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    chatAccess = VotingPromptController.getChatAccess();
                    chatAccess.deleteObservers();
                    chatAccess.addObserver(current);
                    
                    for (Text t: VotingPromptController.getTexts()){
                        texts.add(t);
                        chat_area.getChildren().add(t);
                    }

                    chatAccess.send("`inVotingResults");
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

    public static void setIsVotingResultsLoadedToTrue(){
        isVotingResultsLoaded.set(true);
    }

    public void sendMessage(ActionEvent event){
        String str = message_field.getText();
        if (str != null && str.trim().length() > 0 && !str.startsWith("{") && !str.startsWith("}") && !str.startsWith("|") && !str.startsWith("~") && !str.startsWith("`"))
            chatAccess.send(str);
        message_field.selectAll();
        message_field.requestFocus();
        message_field.setText("");
    }

    public void submitVote(ActionEvent event){
        submit_button.setDisable(true);
        chatAccess.send("}" + voteOption);
    }


    public void update(Observable o, Object arg) {
        final Object finalArg = arg;
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                //If income message starts with a "}" then it is a name, add it to the list
                if (finalArg.toString().startsWith("}")){
                    if (answerOneCounter == 0){
                        answer_prompt_one.selectAll();
                        answer_prompt_one.setText(finalArg.toString().substring(1));
                        answerOneCounter++;
                    } else if (answerOneCounter == 1){
                        voting_results_one_name.setText(finalArg.toString().substring(1));
                        answerOneCounter++;
                    } else{
                        voting_results_one_votes.setText(finalArg.toString().substring(1));
                    }
                } else if (finalArg.toString().startsWith("%")){
                    if (answerTwoCounter == 0){
                        answer_prompt_two.selectAll();
                        answer_prompt_two.setText(finalArg.toString().substring(1));
                        answerTwoCounter++;
                    } else if (answerTwoCounter == 1){
                        voting_results_two_name.setText(finalArg.toString().substring(1));
                        answerTwoCounter++;
                    } else {
                        voting_results_two_votes.setText(finalArg.toString().substring(1));
                    }
                }
                
                else if(finalArg.toString().startsWith("|")){
                    if (finalArg.toString().substring(1).equals("1")){
                        allPlayersSubmitted.set(true);
                    }
                    submit_button.setText(finalArg.toString().substring(1) + " - " + "Submit");
                }else if (finalArg.toString().startsWith("~")){
                    Text text = new Text(finalArg.toString().substring(1)+"\n");
                    text.setFill(Color.SKYBLUE);
                    texts.add(text);
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
                    texts.add(text);
                    chat_area.getChildren().add(text);
                }
            }
        });
    }

    public static ArrayList<Text> getTexts(){
        return texts;
    }
}