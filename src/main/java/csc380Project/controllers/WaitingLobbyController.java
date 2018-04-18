package csc380Project.controllers;

import csc380Project.server.*;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.scene.text.Text;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;
import javafx.scene.paint.Color;
import com.jfoenix.controls.JFXButton;
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

public class WaitingLobbyController extends Thread implements Observer {
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
    JFXButton ready_button;

    @FXML
    TextFlow chat_area;

    @FXML
    JFXTextField message_field;

    @FXML
    Pane main_pane;

    @FXML
    ListView player_list;


    private static Integer startTime = 80;
    //private Integer countDownTime;
    private Timeline timeline;
    private String readyStatus = "Ready Up";
    private Timer timer = new Timer();
    private static String port;
    private String server;
    private ChatAccess chatAccess;
    private static String messageHistory = "";
    private String name = "";
    public static final ObservableList names = FXCollections.observableArrayList();
    private BooleanProperty allPlayersReady = new SimpleBooleanProperty(false);
    private int readyPlayerSize = 0;

    private static Stage myStage;

    public static void setStage(Stage stage) {
       myStage = stage;
    }





    @FXML
    public void initialize() throws IOException {

        if (JoinGameController.getPortNumber() != ""){
            port = JoinGameController.getPortNumber();
        }
        else if (CreateLobbyController.getPortNumber() != ""){
            port = CreateLobbyController.getPortNumber();
        }
        server = "tcp://0.tcp.ngrok.io";


        chatAccess = new ChatAccess();
        chatAccess.addObserver(this);

        try {
            chatAccess.InitSocket(server, Integer.parseInt(port));

        } catch (IOException ex) {
            System.out.println("Cannot connect to " + server + ":" + port);
            ex.printStackTrace();
            System.exit(0);
        }


        if (name.equals("")){
            if (!JoinGameController.getUsername().equals("")){
                name = JoinGameController.getUsername();
                chatAccess.send("}" + name);
            }
            else if (!CreateLobbyController.getUsername().equals("")){
                name = CreateLobbyController.getUsername();
                chatAccess.send("}" + name);
            }
        }

        player_list.setItems(names);

        /*if (allPlayersReady){
            Parent homePageParent = FXMLLoader.load(getClass().getClassLoader().getResource("QuestionPrompt.fxml"));
            Scene homePage = new Scene(homePageParent);
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            appStage.setScene(homePage);
            appStage.show();
            appStage.requestFocus();
        }
*/
        Parent homePageParent = FXMLLoader.load(getClass().getClassLoader().getResource("QuestionPrompt.fxml"));
        Scene homePage = new Scene(homePageParent);
        allPlayersReady.addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    //System.out.println("scene change");
                    myStage.setScene(homePage);
                    myStage.show();
                    myStage.requestFocus();
                }
            }
        });

        /*
        Stage primaryStage = (Stage) main_pane.getScene().getWindow();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                chatAccess.close();
            }
        });*/


        //String[] arguments = new String[] {port};
        //new GameClient().main(arguments);


        //countDownTime = startTime;
        //ready_button.setText(countDownTime.toString() + " - Ready Up");
        /*timer.schedule(new TimerTask() {
            @Override
                public void run() {
                Platform.runLater(new Runnable() {
                   public void run() {
                        countDownTime--;
                        //ready_button.setText(countDownTime.toString() + " - Ready Up");

                        if (countDownTime < 0)
                            timer.cancel();
                  }
                });
            }
            }, 1000, 1000);*/
    }


    public void backToCreateLobby(ActionEvent event) throws IOException {
        Parent homePageParent = FXMLLoader.load(getClass().getClassLoader().getResource("CreateLobby.fxml"));
        Scene homePage = new Scene(homePageParent);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(homePage);
        appStage.show();
    }

    public void sendMessage(ActionEvent event){
        String str = message_field.getText();
        if (str != null && str.trim().length() > 0 && !str.startsWith("{") && !str.startsWith("}") && !str.startsWith("|") && !str.startsWith("~") && !str.startsWith("`"))
            chatAccess.send(str);
        message_field.selectAll();
        message_field.requestFocus();
        message_field.setText("");
    }

    public void readyUp(ActionEvent event){
        ready_button.setDisable(true);
        readyStatus = "Ready!";
        chatAccess.send("`ready");
    }


    public void update(Observable o, Object arg) {
        final Object finalArg = arg;
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                //If income message starts with a "}" then it is a name, add it to the list
                if (finalArg.toString().startsWith("}")){
                    names.add(finalArg.toString().substring(1));
                }else if(finalArg.toString().startsWith("|")){
                    ready_button.setText(finalArg.toString().substring(1) + " - " + readyStatus);
                }else if (finalArg.toString().startsWith("~")){
                    Text text = new Text(finalArg.toString().substring(1)+"\n");
                    text.setFill(Color.SKYBLUE);
                    chat_area.getChildren().add(text);
                }else if(finalArg.toString().startsWith("`")){
                    String str = finalArg.toString().substring(1);
                    if (str.equals("ready")){
                        readyPlayerSize++;
                        if (readyPlayerSize == names.size()){
                            allPlayersReady.set(true);
                        }
                    }
                }else{
                    //Message history will store all chat history in a String we will locally cache to be read inbetween scenes to keep chat saved.
                    messageHistory = messageHistory + finalArg.toString() + "\n";
                    Text text = new Text(finalArg.toString()+"\n");
                    text.setFill(Color.WHITE);
                    chat_area.getChildren().add(text);
                }
            }
        });
    }

    public static String getMessageHistory(){
        return messageHistory;
    }
}