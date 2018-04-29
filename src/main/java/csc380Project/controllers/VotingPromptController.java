package csc380Project.controllers;

import java.util.ArrayList;
import csc380Project.server.*;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.lang.Runnable;
import java.util.Observable;
import java.util.Observer;
import javafx.application.Platform;
import java.io.IOException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;


public class VotingPromptController implements Observer{

    @FXML
    Pane main_pane;

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
    JFXToggleButton vote_option_one;

    @FXML
    JFXToggleButton vote_option_two;

    @FXML
    ScrollPane chat_scroll_pane;


    private static ChatAccess chatAccess;
    private static ArrayList<Text> texts = new ArrayList<>();
    private static BooleanProperty isVotingPromptLoaded = new SimpleBooleanProperty(false);
    private BooleanProperty allPlayersSubmitted = new SimpleBooleanProperty(false);
    private int submittedPlayerSize = 1;
    private static Stage myStage;
    private String voteOption = "";
    private static int numPlayers;
    public static int votingPromptCount = 0;

    public static ChatAccess getChatAccess() {
        return chatAccess;
    }

    public static void setStage(Stage stage) {
        myStage = stage;
     }


    @FXML
    public void initialize() throws IOException {
        numPlayers = WaitingLobbyController.getNumberOfLivePlayers();

        question_prompt.setMouseTransparent(true);

        //VotingPromptController.setStage(myStage);


        VotingPromptController current = this;

        chat_scroll_pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chat_scroll_pane.vvalueProperty().bind((chat_area.heightProperty()));
        //JFXScrollPane.smoothScrolling(chat_scroll_pane);

        isVotingPromptLoaded.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    chatAccess = QuestionPromptController.getChatAccess();
                    chatAccess.deleteObservers();
                    chatAccess.addObserver(current);
                    
                    for (Text t: QuestionPromptController.getTexts()){
                        texts.add(t);
                        chat_area.getChildren().add(t);
                    }

                    chatAccess.send("`inVotingPrompt");
                }
                isVotingPromptLoaded.set(false);
            }
        });

        Parent votingResultParent = FXMLLoader.load(getClass().getClassLoader().getResource("VotingResults.fxml"));
        Scene votingResult = new Scene(votingResultParent);

        allPlayersSubmitted.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    vote_option_one.setDisable(false);
                    vote_option_two.setDisable(false);
                    //chatAccess.send("`allPlayersSubmitted");
                    if (votingPromptCount <= numPlayers){
                        System.out.println("numPlayers: " + numPlayers);
                        votingPromptCount++;
                        chatAccess.send("`inVotingPrompt");
                        submit_button.setDisable(false);
                    }else {
                        myStage.setScene(votingResult);
                        myStage.show();
                        myStage.requestFocus();
                        VotingResultsController.setIsVotingResultsLoadedToTrue();
                    }
                }
                allPlayersSubmitted.set(false);
            }
        });
    }

    public static void setIsVotingPromptLoadedToTrue(){
        isVotingPromptLoaded.set(true);
    }

    public void sendMessage(ActionEvent event){
        String str = message_field.getText();
        if (str != null && str.trim().length() > 0 && !str.startsWith("{") && !str.startsWith("}") && !str.startsWith("|") && !str.startsWith("~") && !str.startsWith("`"))
            chatAccess.send(str);
        message_field.selectAll();
        message_field.requestFocus();
        message_field.setText("");
    }

    public void voteOptionOne(ActionEvent event){
        if (vote_option_two.isSelected()){
            vote_option_two.setSelected(false);
        }
        vote_option_one.setSelected(true);
        voteOption = "1";
    }

    public void voteOptionTwo(ActionEvent event){
        if (vote_option_one.isSelected()){
            vote_option_one.setSelected(false);
        }
        vote_option_two.setSelected(true);
        voteOption = "2";
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
                    if (finalArg.toString().charAt(1) == '~'){
                        answer_prompt_one.selectAll();
                        answer_prompt_one.setText(finalArg.toString().substring(2));
                        vote_option_one.setDisable(true);
                    }else {
                        answer_prompt_one.selectAll();
                        answer_prompt_one.setText(finalArg.toString().substring(1));
                    }
                } else if (finalArg.toString().startsWith("%")){
                    if (finalArg.toString().charAt(1) == '~'){
                        answer_prompt_two.selectAll();
                        answer_prompt_two.setText(finalArg.toString().substring(2));
                        vote_option_two.setDisable(true);
                    }else {
                        answer_prompt_two.selectAll();
                        answer_prompt_two.setText(finalArg.toString().substring(1));
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