package csc380Project.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import csc380Project.server.ChatAccess;
import csc380Project.controllers.*;
import csc380Project.game.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.TextFlow;
import javafx.scene.control.ListView;
import java.io.IOException;
import java.util.Observer;
import java.util.ArrayList;
import java.util.Observable;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;

public class LeaderBoardController implements Observer {

    @FXML
    TextFlow chat_area;

    @FXML
    JFXTextField message_field;

    @FXML
    JFXButton submit_button;

    @FXML
    ListView leaderboard_names;

    @FXML
    ListView leaderboard_score;


    private static ChatAccess chatAccess;
    private static ArrayList<Text> texts = new ArrayList<>();
    private static Stage myStage;
    private static BooleanProperty isLeaderBoardLoaded = new SimpleBooleanProperty(false);
    private BooleanProperty allPlayersReady = new SimpleBooleanProperty(false);
    public ObservableList leaderboardNamesList = FXCollections.observableArrayList();
    public ObservableList leaderboardScoresList = FXCollections.observableArrayList();
    private int submittedPlayerSize = 1;
    private int numPlayers;
    private int playersInList = 0;

    public static ChatAccess getChatAccess() {
        return chatAccess;
    }

    public static void setStage(Stage stage) {
        myStage = stage;
    }

    public static void setIsLeaderBoardLoadedToTrue() {
        isLeaderBoardLoaded.setValue(true);
    }

    @FXML
    public void initialize() throws IOException {
        // add names to the list, and sort them
        //game = ClientThread
        numPlayers = WaitingLobbyController.getNumberOfLivePlayers();

        LeaderBoardController current = this;

        leaderboard_names.setItems(leaderboardNamesList);
        leaderboard_score.setItems(leaderboardScoresList);

        isLeaderBoardLoaded.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    chatAccess = VotingPromptController.getChatAccess();
                    chatAccess.deleteObservers();
                    chatAccess.addObserver(current);

                    leaderboardNamesList.clear();

                    for (Text t : VotingPromptController.getTexts()) {
                        if (!texts.contains(t)){
                            texts.add(t);
                        }
                    }
                    chat_area.getChildren().addAll(texts);
                    chatAccess.send("`inLeaderboard");
                }
            }
        });

        Parent homePageParent = FXMLLoader.load(getClass().getClassLoader().getResource("QuestionPrompt.fxml"));
        Scene homePage = new Scene(homePageParent);

        allPlayersReady.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    isLeaderBoardLoaded.setValue(false);
                    myStage.setScene(homePage);
                    myStage.show();
                    myStage.requestFocus();
                    QuestionPromptController.setIsQuestionPromptLoadedFromLeaderBoardToTrue();
                }
            }
        });
    }

    public void readyUp(ActionEvent event) {
        submit_button.setDisable(true);
        chatAccess.send("`submitted");
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
                        allPlayersReady.set(true);
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
                            allPlayersReady.set(true);
                            submittedPlayerSize = 1;
                        }
                    }
                } else if (finalArg.toString().startsWith("{")) {
                    if (playersInList < numPlayers-1){
                        String a = finalArg.toString().substring(1);
                        String [] stuff = a.split("]");
                        leaderboardNamesList.add(playersInList,stuff[0]);
                        leaderboardScoresList.add(playersInList, stuff[1]);
                        playersInList++;
                    }
                }

                else {
                    //Message history will store all chat history in a String we will locally cache to be read inbetween scenes to keep chat saved.
                    int index = finalArg.toString().indexOf(">");
                    String name = finalArg.toString().substring(1, index);
                    if (finalArg.toString().startsWith("<")) {

                    }
                    Text textName = new Text(name);
                    Text textMessage = new Text(": " + finalArg.toString().substring(index + 1) + "\n");
                    Color nameColor = Color.GOLD;
                    for(PlayerColor pc : WaitingLobbyController.colors){
                        if(pc.getName().equals(name)){
                            nameColor = pc.getColor();
                        }
                    }
                    textName.setFill(nameColor);
                    textMessage.setFill(Color.WHITE);
                    texts.add(textName);
                    texts.add(textMessage);
                    chat_area.getChildren().addAll(textName, textMessage);
                }
            }
        });
    }

    public static ArrayList<Text> getTexts() {
        return texts;
    }
}
