package csc380Project.server;

import csc380Project.game.Player;
import csc380Project.game.Game;
import csc380Project.game.QuestionPack;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import javax.lang.model.element.Name;
import org.omg.PortableServer.THREAD_POLICY_ID;

// For every client's connection we call this class
public class ClientThread extends Thread {
  private String clientName = null;
  private DataInputStream is = null;
  private PrintStream os = null;
  private Socket clientSocket = null;
  private final ClientThread[] threads;
  private int maxClientsCount;
  private String name;
  private ArrayList<String> names = new ArrayList<>();
  private int roundsNum = 15;
  private QuestionPack qp = new QuestionPack().addAllQuestions();
  private int currentround = 0;
  private boolean[][] myVotes = new boolean[roundsNum][2];
  private int[][] totalVotes = new int[roundsNum][2];
  private Player player;
  private int numOfPlayers = 0;

  private Game myGame;

  public String getUserName() {
    return name;
  }

  public ClientThread(Socket clientSocket, ClientThread[] threads) {
    this.clientSocket = clientSocket;
    this.threads = threads;
    maxClientsCount = threads.length;
  }

  public static void displayQuestionsForRound(String[] roundQuestions) {
    if (roundQuestions != null) {
      for (String s : roundQuestions) {
        System.out.println(s);
      }
    }
  }

  public void run() {
    int maxClientsCount = this.maxClientsCount;
    ClientThread[] threads = this.threads;

    try {
      /*
       * Create input and output streams for this client.
       */
      is = new DataInputStream(clientSocket.getInputStream());
      os = new PrintStream(clientSocket.getOutputStream());
      while (true) {
        synchronized (this) {
          for (int i = 0; i < maxClientsCount; i++) {
            if (threads[i] != null && threads[i].clientName != null && !names.contains(threads[i].getUserName())) {
              names.add(threads[i].getUserName());
              this.os.println(names);
            }
          }
        }
        os.println("Enter your name.");
        name = is.readLine().trim();
        int end = 0;
        while (true) {
          if (!names.isEmpty()) {
            for (int i = 0; i < names.size(); i++) {
              if (!names.get(i).isEmpty() && names.get(i).equals(name)) {
                os.println("Sorry that name already exists, please enter in a different one.");
                end++;
              }
            }
          }
          if (end == 0) {
            player = new Player(this.name);
            names.add(name);
            this.os.println(names);
            break;
          }
          name = is.readLine().trim();
          end = 0;
        }
        if (name.indexOf('@') == -1) {
          break;
        } else {
          os.println("The name should not contain '@' character.");
        }
      }

      /* Welcome the new the client. */
      os.println("Welcome " + name + " to our chat room.\nTo leave, enter /quit in a new line.");

      synchronized (this) {
        for (int i = 0; i < maxClientsCount; i++) {
          if (threads[i] != null && threads[i] == this) {
            clientName = "@" + name;
            break;
          }
        }
        for (int i = 0; i < maxClientsCount; i++) {
          if (threads[i] != null && threads[i] != this) {
            threads[i].os.println("*** A new user " + name + " entered the chat room !!! ***");
          }
        }
      }

      synchronized (this) {
        for (int i = 0; i < maxClientsCount; i++) {
          if (threads[i] != null && threads[i].clientName != null && !names.contains(threads[i].getUserName())) {
            names.add(threads[i].getUserName());
          }
        }
        this.os.println(names.toString());
      }

      synchronized (this) {
        for (int i = 0; i < maxClientsCount; i++) {
          if (threads[i] != null) {

          }
        }
      }

      String line = is.readLine();

      /* Start the conversation. */
      while (true) {

        // Exiting chat and game
        if (line.startsWith("/quit")) {
          break;
        }

        // Start game with Host pressing button that says "StartGame"
        if(player.getHostStatus()){
          if(line.startsWith("StartGame")){
            break;
          }
        }

        /* If the message is private send it to the given client. */
        if (line.startsWith("@")) {
          String[] words = line.split("\\s", 2);
          if (words.length > 1 && words[1] != null) {
            words[1] = words[1].trim();
            if (!words[1].isEmpty()) {
              synchronized (this) {
                for (int i = 0; i < maxClientsCount; i++) {
                  if (threads[i] != null && threads[i] != this && threads[i].clientName != null
                      && threads[i].clientName.equals(words[0])) {
                    threads[i].os.println("<" + name + "> " + words[1]);
                    /*
                     * Echo this message to let the client know the private
                     * message was sent.
                     */
                    this.os.println(">" + name + "> " + words[1]);
                    break;
                  }
                }
              }
            }
          }
        } else {
          /* The message is public, broadcast it to all other clients. */
          synchronized (this) {
            for (int i = 0; i < maxClientsCount; i++) {
              if (threads[i] != null && threads[i].clientName != null) {
                threads[i].os.println("<" + name + "> " + line);
              }
            }
          }
        }
      }

      /* Game Code */
      while (true) {
        if(!line.startsWith("StartGame")){
          break;
        }
        //leaving game
        if(line.startsWith("/quit")){
          break;
        }

        //start game for each player
        synchronized (this){
          //assign player numbers (not sure if this will cause error)
          for(int i = 0; i < maxClientsCount; i++){
            if(threads[i] != null && threads[i].clientName != null){
              threads[i].player.setPlayerNumber();
            }
          }
          //create game for each player & calculate num of players
          for(int i = 0; i < maxClientsCount; i++){
            if(threads[i] != null && threads[i].clientName != null){
              threads[i].myGame = new Game(roundsNum, qp);
              numOfPlayers ++;
            }
          }
          //set questions for the game
          for(int i = 0; i < maxClientsCount; i++){
            if(threads[i] != null && threads[i].clientName != null){
              threads[i].myGame.setThisGamesQuestions();
            }
          }
          //give questions to each player
          for(int i = 0; i < maxClientsCount; i++){
            if(threads[i] != null && threads[i].clientName != null){
              threads[i].myGame.giveQuestionstoPlayers();
            }
          }
        }

        /* Start playing */
        currentround ++;

        //for each round (needs to be synchronized some how)
        while(currentround <= roundsNum){
          /* Display Questions and Accept Answers */
          int indexAdjuster = (roundsNum - currentround) * numOfPlayers;
          for(int index = (currentround - 1) * numOfPlayers; index < player.getQuestions().length - indexAdjuster; index ++){
            if(player.getQuestionAtIndex(index) != null){
              //display question
              //take answer
              String answer = "";
              player.addAnswerToIndex(answer, index);
            }
          }

          /* Display Each Answer and Vote */
          for(int index = (currentround - 1) * numOfPlayers; index < player.getQuestions().length - indexAdjuster; index ++){
            String[] currentQuestionAns = new String[2];
            for(int i = 0; i < maxClientsCount; i++){
              if (threads[i] != null && threads[i].clientName != null) {
                if(threads[i].player.getAnswerAtIndex(index) != null) {
                  if(currentQuestionAns[0] == null){
                    currentQuestionAns[0] = threads[i].player.getAnswerAtIndex(index);
                  } else{
                    currentQuestionAns[1] = threads[i].player.getAnswerAtIndex(index);
                  }
                }
              }
            }
            //display answers
            //vote for answers
          }

          /* Display Leaderboard */
        }
      }

      synchronized (this) {
        for (int i = 0; i < maxClientsCount; i++) {
          if (threads[i] != null && threads[i] != this && threads[i].clientName != null) {
            threads[i].os.println("*** The user " + name + " is leaving the chat room !!! ***");
          }
        }
      }
      os.println("*** Bye " + name + " ***");

      /*
       * Clean up. Set the current thread variable to null so that a new client
       * could be accepted by the server.
       */
      synchronized (this) {
        for (int i = 0; i < maxClientsCount; i++) {
          if (threads[i] == this) {
            threads[i] = null;
          }
        }
      }
      /*
       * Close the output stream, close the input stream, close the socket.
       */
      is.close();
      os.close();
      clientSocket.close();
    } catch (IOException e) {
    }
  }
}