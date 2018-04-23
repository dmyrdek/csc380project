package csc380Project.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import csc380Project.game.Player;
import csc380Project.server.ChatAccess;
import csc380Project.server.ClientThread;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.text.TextFlow;

import javax.swing.table.TableColumn;
import javax.swing.text.TableView;
import java.io.IOException;
import csc380Project.game.*;

public class LeaderBoardController {

    @FXML
    TextFlow chat_area;

    @FXML
    JFXTextField message_field;

    @FXML
    JFXButton submit_button;

    @FXML
    JFXListView leaderBoard;

    public static final ObservableList names = FXCollections.observableArrayList();

    Game game;


    private ChatAccess chatAccess;


    @FXML
    public void initialize() throws IOException {
        // add names to the list, and sort them
        //game = ClientThread



    }


    public void sendMessage(ActionEvent event){
        String str = message_field.getText();
        if (str != null && str.trim().length() > 0 && !str.startsWith("{") && !str.startsWith("}") && !str.startsWith("|") && !str.startsWith("~") && !str.startsWith("`"))
            chatAccess.send(str);
        message_field.selectAll();
        message_field.requestFocus();
        message_field.setText("");
    }




}
