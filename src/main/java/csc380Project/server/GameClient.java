package csc380Project.server;


import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import csc380Project.game.*;

public class GameClient {
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

  /** Chat client UI */
  static class ChatFrame extends JFrame implements Observer {

    private JTextArea textArea;
    private JTextField inputTextField;
    private JButton sendButton;
    private ChatAccess chatAccess;

    public ChatFrame(ChatAccess chatAccess) {
      this.chatAccess = chatAccess;
      chatAccess.addObserver(this);
      buildGUI();
    }

    /** Builds the user interface */
    private void buildGUI() {
      textArea = new JTextArea(20, 50);
      textArea.setEditable(false);
      textArea.setLineWrap(true);
      add(new JScrollPane(textArea), BorderLayout.CENTER);

      Box box = Box.createHorizontalBox();
      add(box, BorderLayout.SOUTH);
      inputTextField = new JTextField();
      sendButton = new JButton("Send");
      box.add(inputTextField);
      box.add(sendButton);

      // Action for the inputTextField and the goButton
      ActionListener sendListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          String str = inputTextField.getText();
          if (str != null && str.trim().length() > 0)
            chatAccess.send(str);
          inputTextField.selectAll();
          inputTextField.requestFocus();
          inputTextField.setText("");
        }
      };
      inputTextField.addActionListener(sendListener);
      sendButton.addActionListener(sendListener);

      this.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
          chatAccess.close();
        }
      });
    }

    /** Updates the UI depending on the Object argument */
    public void update(Observable o, Object arg) {
      final Object finalArg = arg;
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          textArea.append(finalArg.toString());
          textArea.append("\n");
        }
      });
    }
  }

  public static void main(String[] args) {

    int port = Integer.parseInt(args[0]);
    String server = "tcp://0.tcp.ngrok.io";

    ChatAccess access = new ChatAccess();

    JFrame frame = new ChatFrame(access);
    frame.setTitle("MyChatApp - connected to " + server + ":" + port);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);
    frame.setVisible(true);

    try {
      access.InitSocket(server, port);
    } catch (IOException ex) {
      System.out.println("Cannot connect to " + server + ":" + port);
      ex.printStackTrace();
      System.exit(0);
    }
  }

  public static void parse(String s, Player me) {

      String [] input = s.split("\\|");


      // input looks like [players|<player1>|<player2> ....]
      if (input[0].equals("players")){
          String [] playerList = new String [input.length-1];
          for (int i = 0; i<input.length-1; i++){
              playerList[i] = input[i+1];
          }
          addPlayers(playerList, me);

      //input looks like [questionsForGame|<questionIndex>|<question>|<receivingPlayer1>|<receivingPlayer2> ... (repeating)..]
      } else if (input[0].equals("questionsForGame")) {
          String [] questionsForGame = new String [input.length-1];
          for (int i = 0; i<input.length-1; i++) {
              questionsForGame[i] = input[i+1];

          }
          addQuestions(questionsForGame, me);

          // [answer|<question>|<answer>|<player>]
      } else if (input[0].equals("answer")){
          String [] answer = new String [input.length-1];
          for (int i = 0; i<input.length-1; i++) {
              answer[i] = input[i+1];
          }
          addAnswer(answer, me);

          // [vote|<question>|<answer>|<player>]
      } else if (input[0].equals("vote")){
          String [] vote = new String [input.length-1];
          for (int i = 0; i<input.length-1; i++) {
              vote[i] = input[i+1];
          }
          vote(vote, me);
      }
  }


    // add others players to "my player"'s references of other players
  static void addPlayers(String [] s, Player me) {
    for(int i=0; i<s.length; i++) {
      me.playersInGame.add(new Player(s[i]));
    }
  }

  // add the correct questions for the correct players
  static void addQuestions(String [] questions, Player me) {
    //[<questionIndex>|<question>|<player>|<player>|...]
    for(int i=0; i<questions.length; i+=4) {
      int index = Integer.parseInt(questions[i]);
      String question = questions[i+1];
      for(int j=0; j<me.playersInGame.size();j++){
        if(me.playersInGame.get(j).getName().equals(questions[i+2])){
          me.playersInGame.get(j).addQuestionToIndex(question, index);
        }
        else if(me.playersInGame.get(j).getName().equals(questions[i+3])){
          me.playersInGame.get(j).addQuestionToIndex(question, index);
        }
      }
    }
  }
  static void addAnswer(String [] answers, Player me) {
    //[<question>|<answer>|<player>]
    String question = answers[0];//used later for displaying stuff
    String answer = answers[1];
    for(int j=0; j<me.playersInGame.size();j++){
      if(me.playersInGame.get(j).getName().equals(answers[2])){
        me.playersInGame.get(j).addAnswer(answer, question);
      }
    }
  }
  static void vote(String [] vote, Player me){
    //[<question>|<answer>|<player>]
    String question = vote[0]; //used later for displaying stuff
    String answer = vote[1];
    for(int j=0; j<me.playersInGame.size();j++){
      if(me.playersInGame.get(j).getName().equals(vote[2])){
        me.playersInGame.get(j).increaseVotesReceived();
      }
    }
  }







}