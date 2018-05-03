package csc380Project.controllers;

import java.util.ArrayList;
import csc380Project.server.*;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.text.Text;
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

public class QuestionPromptController implements Observer {

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

    @FXML
    ScrollPane chat_scroll_pane;

    private static ChatAccess chatAccess;
    private static String port;
    private static BooleanProperty isQuestionPromptLoadedFromWaitingLobby = new SimpleBooleanProperty(false);
    private static BooleanProperty isQuestionPromptLoadedFromLeaderBoard = new SimpleBooleanProperty(false);
    private BooleanProperty allPlayersSubmitted = new SimpleBooleanProperty(false);
    private static BooleanProperty changeScene = new SimpleBooleanProperty(false);
    private static ArrayList<Text> texts = new ArrayList<>();
    private static int questionNumber = 0;
    private String questionAnswer = "";
    private int submittedPlayerSize = 1;
    private static Stage myStage;
    private static int maxPlayers;
    private static int numRounds;
    private static int numOfTotalQuestions;
    private static int currentNumOfRounds = 0;
    private static int currentNumOfQuestions = 0;
    private static boolean isHost = false;

    public static void setStage(Stage stage) {
        myStage = stage;
    }

    public static void setIsQuestionPromptLoadedFromWaitingLobbyToTrue() {
        isQuestionPromptLoadedFromWaitingLobby.setValue(true);
    }

    public static void setIsQuestionPromptLoadedFromLeaderBoardToTrue() {
        isQuestionPromptLoadedFromLeaderBoard.setValue(true);
    }

    public static ChatAccess getChatAccess() {
        return chatAccess;
    }

    @FXML
    public void initialize() throws IOException {
        currentNumOfQuestions++;
        currentNumOfRounds++;

        VotingPromptController.setStage(myStage);

        question_prompt.setMouseTransparent(true);

        QuestionPromptController current = this;

        chat_scroll_pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chat_scroll_pane.vvalueProperty().bind((chat_area.heightProperty()));
        //JFXScrollPane.smoothScrolling(chat_scroll_pane);
        isQuestionPromptLoadedFromWaitingLobby.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    chat_area.getChildren().remove(0, chat_area.getChildren().size());
                    isHost = WaitingLobbyController.getIsHost();
                    if (isHost) {
                        maxPlayers = WaitingLobbyController.getMaxPlayers();
                        numRounds = WaitingLobbyController.getNumRounds();
                    }

                    chatAccess = WaitingLobbyController.getChatAccess();
                    chatAccess.deleteObservers();
                    chatAccess.addObserver(current);

                    for (Text t : WaitingLobbyController.getTexts()) {
                        if (!texts.contains(t)){
                            texts.add(t);
                        }
                    }
                    chat_area.getChildren().addAll(texts);

                    chatAccess.send("`inQuestionPrompt");
                }
                //isQuestionPromptLoadedFromWaitingLobby.set(false);
            }
        });

        isQuestionPromptLoadedFromLeaderBoard.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    chat_area.getChildren().remove(0, chat_area.getChildren().size());
                    isHost = WaitingLobbyController.getIsHost();
                    if (isHost) {
                        maxPlayers = WaitingLobbyController.getMaxPlayers();
                        numRounds = WaitingLobbyController.getNumRounds();
                    }

                    chatAccess = LeaderBoardController.getChatAccess();
                    chatAccess.deleteObservers();
                    chatAccess.addObserver(current);

                    for (Text t : LeaderBoardController.getTexts()) {
                        if (!texts.contains(t)){
                            texts.add(t);
                        }
                    }
                    chat_area.getChildren().addAll(texts);

                    chatAccess.send("`inQuestionPrompt");
                }
                //isQuestionPromptLoadedFromVotingPrompt.set(false);
            }
        });

        Parent homePageParent = FXMLLoader.load(getClass().getClassLoader().getResource("VotingPrompt.fxml"));
        Scene homePage = new Scene(homePageParent);

        allPlayersSubmitted.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    isQuestionPromptLoadedFromWaitingLobby.setValue(false);
                    isQuestionPromptLoadedFromLeaderBoard.setValue(false);
                    chatAccess.send("`allPlayersSubmitted");
                    if (questionNumber == 0) {
                        questionNumber = 1;
                        chatAccess.send("`inQuestionPrompt");
                        submit_button.setDisable(false);
                        //myStage.getScene().setRoot(homePageParent);
                        //myStage.setScene(homePage);
                        //myStage.show();
                        //myStage.requestFocus();
                        //QuestionPromptControllerTwo.setIsQuestionPromptLoadedToTrue();
                    } else if (questionNumber == 1) {
                        questionNumber = 0;
                        myStage.setScene(homePage);
                        myStage.show();
                        myStage.requestFocus();
                        VotingPromptController.setIsVotingPromptLoadedFromQuestionPromptToTrue();
                    }
                }
                allPlayersSubmitted.set(false);
            }
        });

        changeScene.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    if (questionNumber == 0) {
                        questionNumber++;

                    }
                }
            }
        });
    }

    @FXML
    public void getAnswer(KeyEvent event) {
        questionAnswer = answer_prompt.getText();
        System.out.println(questionAnswer);
        //chatAccess.send("~" + questionAnswer);
    }

    public void submit(ActionEvent event) {
        submit_button.setDisable(true);
        //submit_button = "Submitted!";
        chatAccess.send("`submitted");
        chatAccess.send("~" + questionAnswer);
        answer_prompt.selectAll();
        answer_prompt.setText("");
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

    public void update(Observable o, Object arg) {
        final Object finalArg = arg;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //If income message starts with a "}" then it is a name, add it to the list
                if (finalArg.toString().startsWith("}")) {
                    //names.add(finalArg.toString().substring(1));
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
                            submittedPlayerSize = 1;
                        }
                    }
                } else if (finalArg.toString().startsWith("{")) {
                    question_prompt.selectAll();
                    question_prompt.setText(finalArg.toString().substring(1));
                } else if (finalArg.toString().startsWith("%")) {
                    numRounds = Integer.parseInt(finalArg.toString().substring(1));
                    numOfTotalQuestions = numRounds * 2;
                }

                else {
                    //Message history will store all chat history in a String we will locally cache to be read inbetween scenes to keep chat saved.
                    Text text = new Text(finalArg.toString() + "\n");
                    text.setFill(Color.WHITE);
                    texts.add(text);
                    chat_area.getChildren().add(text);
                }
            }
        });
    }

    public static ArrayList<Text> getTexts() {
        return texts;
    }
}