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

public class QuestionPromptController extends Thread implements Observer{
    static class ChatAccess extends Observable {
        private Socket socket;
        private OutputStream outputStream;

        @Override
        public void notifyObservers(Object arg) {
            super.setChanged();
            super.notifyObservers(arg);
        }

        /** Create socket, and receiving thread */
        public void InitSocket(String server, int port) throws IOException {
            socket = new Socket(server, port);
            outputStream = socket.getOutputStream();

            Thread receivingThread = new Thread() {
                @Override
                public void run() {
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null)
                            notifyObservers(line);
                    } catch (IOException ex) {
                        notifyObservers(ex);
                    }
                }
            };
            receivingThread.start();
        }

        private static final String CRLF = "\r\n"; // newline

        /** Send a line of text */
        public void send(String text) {
            try {
                outputStream.write((text + CRLF).getBytes());
                outputStream.flush();
            } catch (IOException ex) {
                notifyObservers(ex);
            }
        }

        /** Close the socket */
        public void close() {
            try {
                socket.close();
            } catch (IOException ex) {
                notifyObservers(ex);
            }
        }
    }



    @FXML
    JFXTextArea question_prompt;

    @FXML
    TextFlow chat_area;

    @FXML
    JFXTextField message_field;

    private ChatAccess chatAccess;
    private static String port;
    private static BooleanProperty isQuestionPromptLoaded = new SimpleBooleanProperty(false);

    public static void setIsQuestionPromptLoadedToTrue(){
        isQuestionPromptLoaded.set(true);
    }

    @FXML
    public void initialize() {
        if (JoinGameController.getPortNumber() != ""){
            port = JoinGameController.getPortNumber();
        }
        else if (CreateLobbyController.getPortNumber() != ""){
            port = CreateLobbyController.getPortNumber();
        }

        String server = "tcp://0.tcp.ngrok.io";


        chatAccess = new ChatAccess();
        chatAccess.addObserver(this);

        try {
            chatAccess.InitSocket(server, Integer.parseInt(port));

        } catch (IOException ex) {
            System.out.println("Cannot connect to " + server + ":" + port);
            ex.printStackTrace();
            System.exit(0);
        }
        question_prompt.setMouseTransparent(true);

        isQuestionPromptLoaded.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    for (Text t: WaitingLobbyController.getTexts()){
                        chat_area.getChildren().add(t);
                    }
                }
            }
        });
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
                    //ready_button.setText(finalArg.toString().substring(1) + " - " + readyStatus);
                }else if (finalArg.toString().startsWith("~")){
                    Text text = new Text(finalArg.toString().substring(1)+"\n");
                    text.setFill(Color.SKYBLUE);
                    chat_area.getChildren().add(text);
                }else if(finalArg.toString().startsWith("`")){
                    String str = finalArg.toString().substring(1);
                    if (str.equals("ready")){
                        //readyPlayerSize++;
                        //if (readyPlayerSize == names.size()){
                        //    allPlayersReady.set(true);
                        //}
                    }
                }else{
                    //Message history will store all chat history in a String we will locally cache to be read inbetween scenes to keep chat saved.
                    Text text = new Text(finalArg.toString()+"\n");
                    text.setFill(Color.WHITE);
                    chat_area.getChildren().add(text);
                }
            }
        });
    }
}