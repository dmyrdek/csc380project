package csc380Project.controllers;

import java.io.File;
import java.util.ArrayList;
import csc380Project.server.*;
import csc380Project.game.*;
import csc380Project.controllers.*;
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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class VotingPromptController implements Observer {

    @FXML
    Pane main_pane;

    @FXML
    JFXTextArea voting_question_prompt;

    @FXML
    JFXTextArea results_question_prompt;

    @FXML
    JFXTextArea voting_answer_prompt_one;

    @FXML
    JFXTextArea voting_answer_prompt_two;

    @FXML
    JFXTextArea results_answer_prompt_one;

    @FXML
    JFXTextArea results_answer_prompt_two;

    @FXML
    Label voting_results_one_name;

    @FXML
    Label voting_results_one_votes;

    @FXML
    Label voting_results_two_name;

    @FXML
    Label voting_results_two_votes;

    @FXML
    VBox voting_vbox;

    @FXML
    VBox results_vbox;

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
    private static BooleanProperty isVotingPromptLoadedFromQuestionPrompt = new SimpleBooleanProperty(false);
    private BooleanProperty allPlayersSubmitted = new SimpleBooleanProperty(false);
    private int submittedPlayerSize = 1;
    private static Stage myStage;
    private String voteOption = "";
    private static int numPlayers;
    private int votingPromptCount = 0;
    private boolean inVotingPrompt = true;
    private int answerOneCounter = 0;
    private int answerTwoCounter = 0;

    public static ChatAccess getChatAccess() {
        return chatAccess;
    }

    public static void setStage(Stage stage) {
        myStage = stage;
    }

    @FXML
    public void initialize() {

        voting_question_prompt.setMouseTransparent(true);
        results_question_prompt.setMouseTransparent(true);

        LeaderBoardController.setStage(myStage);

        VotingPromptController current = this;

        chat_scroll_pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chat_scroll_pane.vvalueProperty().bind((chat_area.heightProperty()));
        //JFXScrollPane.smoothScrolling(chat_scroll_pane);

        isVotingPromptLoadedFromQuestionPrompt.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    numPlayers = WaitingLobbyController.getNumberOfLivePlayers();
                    numPlayers = numPlayers - 1;
                    chatAccess = QuestionPromptController.getChatAccess();
                    chatAccess.deleteObservers();
                    chatAccess.addObserver(current);

                    for (Text t : QuestionPromptController.getTexts()) {
                        if (!texts.contains(t)){
                            texts.add(t);
                        }
                    }
                    chat_area.getChildren().addAll(texts);
                    chatAccess.send("`inVotingPrompt");

                }
                //isVotingPromptLoadedFromQuestionPrompt.set(false);
            }
        });

        allPlayersSubmitted.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    isVotingPromptLoadedFromQuestionPrompt.setValue(false);
                    vote_option_one.setDisable(false);
                    vote_option_two.setDisable(false);
                    System.out.println(votingPromptCount + " " + numPlayers);
                    //chatAccess.send("`allPlayersSubmitted");
                    if (votingPromptCount < numPlayers) {
                        if (inVotingPrompt) {
                            submittedPlayerSize = 1;
                            inVotingPrompt = false;
                            submit_button.setDisable(false);
                            voting_vbox.setVisible(false);
                            results_vbox.setVisible(true);
                            allPlayersSubmitted.set(false);
                            votingPromptCount++;
                            chatAccess.send("`inVotingResults");
                        } else {
                            submittedPlayerSize = 1;
                            inVotingPrompt = true;
                            answerOneCounter = 0;
                            answerTwoCounter = 0;
                            submit_button.setDisable(false);
                            vote_option_one.setSelected(false);
                            vote_option_two.setSelected(false);
                            voting_vbox.setVisible(true);
                            results_vbox.setVisible(false);
                            allPlayersSubmitted.set(false);
                            chatAccess.send("`inVotingPrompt");
                        }
                    } else {
                        try {
                            inVotingPrompt = true;
                            Parent leaderBoardParent = FXMLLoader
                                    .load(getClass().getClassLoader().getResource("LeaderBoard.fxml"));
                            Scene LeaderBoardScene = new Scene(leaderBoardParent);
                            myStage.setScene(LeaderBoardScene);
                            myStage.show();
                            myStage.requestFocus();
                            LeaderBoardController.setIsLeaderBoardLoadedToTrue();
                            allPlayersSubmitted.set(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                //allPlayersSubmitted.set(false);
            }
        });
    }

    public static void setIsVotingPromptLoadedFromQuestionPromptToTrue() {
        isVotingPromptLoadedFromQuestionPrompt.setValue(true);
    }

    public void sendMessage(ActionEvent event) {
        String str = message_field.getText();
        if (str != null && str.trim().length() > 0 && !str.startsWith("{") && !str.startsWith("}")
                && !str.startsWith("|") && !str.startsWith("~") && !str.startsWith("`"))
            chatAccess.send(str);
        message_field.selectAll();
        message_field.requestFocus();
        message_field.setText("");
    }

    public void voteOptionOne(ActionEvent event) {
        if (vote_option_two.isSelected()) {
            vote_option_two.setSelected(false);
        }
        vote_option_one.setSelected(true);
        voteOption = "1";
    }

    public void voteOptionTwo(ActionEvent event) {
        if (vote_option_one.isSelected()) {
            vote_option_one.setSelected(false);
        }
        vote_option_two.setSelected(true);
        voteOption = "2";
    }

    public void submitVote(ActionEvent event) {
        submit_button.setDisable(true);
        if (inVotingPrompt) {
            chatAccess.send("}" + voteOption);
        } else {
            chatAccess.send("`submitted");
        }
    }

    public void update(Observable o, Object arg) {
        final Object finalArg = arg;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //If income message starts with a "}" then it is a name, add it to the list
                if (inVotingPrompt) {
                    if (finalArg.toString().startsWith("}")) {
                        if (finalArg.toString().charAt(1) == '~') {
                            voting_answer_prompt_one.selectAll();
                            voting_answer_prompt_one.setText(finalArg.toString().substring(2));
                            vote_option_one.setDisable(true);
                        } else {
                            voting_answer_prompt_one.selectAll();
                            voting_answer_prompt_one.setText(finalArg.toString().substring(1));
                        }
                    } else if (finalArg.toString().startsWith("%")) {
                        if (finalArg.toString().charAt(1) == '~') {
                            voting_answer_prompt_two.selectAll();
                            voting_answer_prompt_two.setText(finalArg.toString().substring(2));
                            vote_option_two.setDisable(true);
                        } else {
                            voting_answer_prompt_two.selectAll();
                            voting_answer_prompt_two.setText(finalArg.toString().substring(1));
                        }
                    } else if (finalArg.toString().startsWith("|")) {
                        if (finalArg.toString().substring(1).equals("1")) {
                            allPlayersSubmitted.set(true);
                        }
                        submit_button.setText(finalArg.toString().substring(1) + " - " + "Submit");
                    } else if (finalArg.toString().startsWith("~")) {
                        Text text = new Text(finalArg.toString().substring(1) + "\n");
                        text.setFill(Color.SKYBLUE);
                        texts.add(text);
                        chat_area.getChildren().add(text);
                    } else if (finalArg.toString().startsWith("`")) {
                        String str = finalArg.toString().substring(1);
                        if (str.equals("submitted")) {
                            submittedPlayerSize++;
                            if (submittedPlayerSize == WaitingLobbyController.names.size()) {
                                allPlayersSubmitted.set(true);
                            }
                        }
                    } else if (finalArg.toString().startsWith("{")) {
                        voting_question_prompt.selectAll();
                        voting_question_prompt.setText(finalArg.toString().substring(1));
                    } else {
                        //Message history will store all chat history in a String we will locally cache to be read inbetween scenes to keep chat saved.
                        int index = finalArg.toString().indexOf(">");
                        String name = finalArg.toString().substring(1, index);
                        if (finalArg.toString().startsWith("<")) {

                        }
                        Text textName = new Text(name);
                        Text textMessage = new Text(": " + finalArg.toString().substring(index + 1) + "\n");
                        Color nameColor = Color.GOLD;
                        for(PlayerColor pc : WaitingLobbyController.getColors()){
                            if(pc.getName().equals(name)){
                                nameColor = pc.getColor();
                            }
                        }
                        textName.setFill(nameColor);
                        textMessage.setFill(Color.WHITE);
                        texts.add(textName);
                        texts.add(textMessage);
                        chat_area.getChildren().addAll(textName, textMessage);
                        playSound();
                    }
                } else {
                    if (finalArg.toString().startsWith("}")) {
                        if (answerOneCounter == 0) {
                            results_answer_prompt_one.selectAll();
                            results_answer_prompt_one.setText(finalArg.toString().substring(1));
                            answerOneCounter++;
                        } else if (answerOneCounter == 1) {
                            voting_results_one_name.setText(finalArg.toString().substring(1));
                            answerOneCounter++;
                        } else {
                            voting_results_one_votes.setText(finalArg.toString().substring(1));
                        }
                    } else if (finalArg.toString().startsWith("%")) {
                        if (answerTwoCounter == 0) {
                            results_answer_prompt_two.selectAll();
                            results_answer_prompt_two.setText(finalArg.toString().substring(1));
                            answerTwoCounter++;
                        } else if (answerTwoCounter == 1) {
                            voting_results_two_name.setText(finalArg.toString().substring(1));
                            answerTwoCounter++;
                        } else {
                            voting_results_two_votes.setText(finalArg.toString().substring(1));
                        }
                    } else if (finalArg.toString().startsWith("|")) {
                        if (finalArg.toString().substring(1).equals("1")) {
                            allPlayersSubmitted.set(true);
                        }
                        submit_button.setText(finalArg.toString().substring(1) + " - " + "Ready");
                    } else if (finalArg.toString().startsWith("~")) {
                        Text text = new Text(finalArg.toString().substring(1) + "\n");
                        text.setFill(Color.SKYBLUE);
                        texts.add(text);
                        chat_area.getChildren().add(text);
                    } else if (finalArg.toString().startsWith("`")) {
                        String str = finalArg.toString().substring(1);
                        if (str.equals("submitted")) {
                            submittedPlayerSize++;
                            if (submittedPlayerSize == WaitingLobbyController.names.size()) {
                                allPlayersSubmitted.set(true);
                            }
                        }
                    } else if (finalArg.toString().startsWith("{")) {
                        results_question_prompt.selectAll();
                        results_question_prompt.setText(finalArg.toString().substring(1));
                    } else {
                        //Message history will store all chat history in a String we will locally cache to be read inbetween scenes to keep chat saved.
                        int index = finalArg.toString().indexOf(">");
                        String name = finalArg.toString().substring(1, index);
                        if (finalArg.toString().startsWith("<")) {

                        }
                        Text textName = new Text(name);
                        Text textMessage = new Text(": " + finalArg.toString().substring(index + 1) + "\n");
                        Color nameColor = Color.GOLD;
                        for(PlayerColor pc : WaitingLobbyController.getColors()){
                            if(pc.getName().equals(name)){
                                nameColor = pc.getColor();
                            }
                        }
                        textName.setFill(nameColor);
                        textMessage.setFill(Color.WHITE);
                        texts.add(textName);
                        texts.add(textMessage);
                        chat_area.getChildren().addAll(textName, textMessage);
                        playSound();
                    }
                }
            }
        });
    }

    public void playSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("D:/MusicPlayer/fml.mp3").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch(Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    public static ArrayList<Text> getTexts() {
        return texts;
    }
}