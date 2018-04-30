package csc380Project.controllers;

import java.util.ArrayList;
import csc380Project.server.*;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.paint.Color;
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
import java.lang.Runnable;
import java.sql.SQLOutput;
import java.util.Observable;
import java.util.Observer;
import javafx.application.Platform;
import java.io.IOException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import com.jfoenix.controls.JFXScrollPane;

public class VotingResultsFromQuestionController implements Observer{

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


    private static ChatAccess chatAccess;
    private static ArrayList<Text> texts = new ArrayList<>();
    private static BooleanProperty isVotingResultsLoaded = new SimpleBooleanProperty(false);
    private BooleanProperty allPlayersSubmitted = new SimpleBooleanProperty(false);
    private int submittedPlayerSize = 1;
    private static Stage myStage;
    private int answerOneCounter = 0;
    private int answerTwoCounter = 0;
    private static int numPlayers;
    
    @FXML
    public void initialize() {
        VotingPromptFromResultsController.setStage(myStage);

        numPlayers = WaitingLobbyController.getNumberOfLivePlayers();

        question_prompt.setMouseTransparent(true);

        VotingResultsFromQuestionController current = this;

        chat_scroll_pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chat_scroll_pane.vvalueProperty().bind((chat_area.heightProperty()));
        //JFXScrollPane.smoothScrolling(chat_scroll_pane);

        isVotingResultsLoaded.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    chatAccess = VotingPromptFromQuestionController.getChatAccess();
                    chatAccess.deleteObservers();
                    chatAccess.addObserver(current);
                    
                    for (Text t: VotingPromptFromQuestionController.getTexts()){
                        texts.add(t);
                        chat_area.getChildren().add(t);
                    }

                    chatAccess.send("`inVotingResults");
                }
            }
        });

        allPlayersSubmitted.addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    isVotingResultsLoaded.set(false);
                    if (VotingPromptFromQuestionController.getVotingPromptCount() <= numPlayers){
                        try{
                            System.out.print("Loading Voting Prompt");
                            Parent votingPromptParent = FXMLLoader.load(getClass().getClassLoader().getResource("VotingPromptFromResults.fxml"));
                            Scene votingPrompt = new Scene(votingPromptParent);
                            myStage.setScene(votingPrompt);
                            myStage.show();
                            myStage.requestFocus();
                            VotingPromptFromResultsController.setIsVotingPromptLoadedFromVotingPromptToTrue();
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                        //System.out.println("numPlayers: " + numPlayers);
                        //votingPromptCount++;
                        //chatAccess.send("`inVotingPrompt");
                        //submit_button.setDisable(false);
                    }else {
                        try{
                            Parent questionPromptParent = FXMLLoader.load(getClass().getClassLoader().getResource("QuestionPrompt.fxml"));
                            Scene questionPrompt = new Scene(questionPromptParent);
                            myStage.setScene(questionPrompt);
                            myStage.show();
                            myStage.requestFocus();
                            QuestionPromptController.setIsQuestionPromptLoadedToTrue();
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                allPlayersSubmitted.set(false);
            }
        });

    }

    public static ChatAccess getChatAccess() {
        return chatAccess;
    }

    public static void setStage(Stage stage) {
        myStage = stage;
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
        //hatAccess.send("}" + voteOption);
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