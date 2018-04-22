package csc380Project.server;

import csc380Project.controllers.*;
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

import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

// For every client's connection we call this class
public class ClientThread extends Thread {
  private String clientName = null;
  private DataInputStream is = null;
  private PrintStream os = null;
  private Socket clientSocket = null;
  private final ClientThread[] threads;
  private int maxClientsCount;
  private String name = "";
  //private ArrayList<String> names = new ArrayList<>();
  private int roundsNum = 15;
  private QuestionPack qp = new QuestionPack().addAllQuestions();
  private boolean[][] myVotes = new boolean[roundsNum][2];
  private int[][] totalVotes = new int[roundsNum][2];
  private Player player;
  private int numOfPlayers = 0;
  private boolean isHost = false;
  private boolean isReady = false;
  private boolean submittedAnswer = false;
  private boolean inWaitingLobby = true;
  private boolean inQuestionPrompt = false;
  private int countDownTime = 120;
  private int inQuestionPromptTime = 60;
  private ArrayList<Player> playerList = new ArrayList<>();
  private Game myGame;
  private boolean[][] myRounds = new boolean[roundsNum][2];
  private String[][] answers = new String[roundsNum][2];
  private int currentround = 0;
  private int questionNumber = 0;
  private boolean allPlayersSubmitted = false;

  public String getUserName() {
    return name;
  }

  public Game getMyGame(){return myGame;}

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

  public Player[] getLeaderBoard (){
    Player[] leaderboard = new Player [this.threads[0].myGame.getInGamePlayers().size()];
    ArrayList<Player> players = this.threads[0].myGame.getInGamePlayers();
    Collections.sort(players);
    for (int i = 0; i<leaderboard.length; i++) {
      leaderboard[i] = players.get(i);
    }
    return leaderboard;
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
      /*while (true) {
        synchronized (this) {
          for (int i = 0; i < maxClientsCount; i++) {
            if (threads[i] != null && threads[i].clientName != null && !names.contains(threads[i].getUserName())) {
              names.add(threads[i].getUserName());
              //this.os.println(names);
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
            //this.os.println(names);
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
      */
      if (this.name.equals("")) {
        String str = is.readLine();
        if (str.startsWith("}")) {
          this.name = str.substring(1);
          player = new Player(name);
          if (this == this.threads[0]) {
            this.isHost = true;
          }
        }
      }
      for (int i = 0; i < maxClientsCount; i++) {
        if (threads[i] != null) {
          if (i == 0) {
            this.os.println("}" + threads[i].name + " (host)");
          } else {
            this.os.println("}" + threads[i].name);
          }
        }
      }

      synchronized (this) {
        if (this == threads[0] && inWaitingLobby) {
          final Timer timer = new Timer();
          timer.schedule(new TimerTask() {
            @Override
            public void run() {
              if (inQuestionPrompt) {
                countDownTime = inQuestionPromptTime;
                inQuestionPrompt = false;
              }
              countDownTime--;
              for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] != null) {
                  threads[i].os.println("|" + countDownTime);
                }
              }
              if (countDownTime < 1) {
                for (int i = 0; i < maxClientsCount; i++) {
                  if (threads[i] != null) {
                    threads[i].os.println("`ready");
                    threads[i].inWaitingLobby = false;
                    threads[i].isReady = true;
                  }
                }
                //timer.cancel();
              }
            }
          }, 1000, 1000);
        }
      }

      /* Welcome the new the client. */
      os.println("~Welcome to Questionnaires " + name + "!");

      synchronized (this) {
        for (int i = 0; i < maxClientsCount; i++) {
          if (threads[i] != null && threads[i] == this) {
            clientName = "@" + name;
            break;
          }
        }
        for (int i = 0; i < maxClientsCount; i++) {
          if (threads[i] != null && threads[i] != this) {
            threads[i].os.println("~A new user " + name + " has joined!");
            threads[i].os.println("}" + name);
          }
        }
      }

      /*
      synchronized (this) {
        for (int i = 0; i < maxClientsCount; i++) {
          if (threads[i] != null && threads[i].clientName != null && !names.contains(threads[i].getUserName())) {
            names.add(threads[i].getUserName());
          }
        }
        this.os.println(names.toString());
      }*/

      /* Start the conversation. */
      while (true) {

        String line = is.readLine();

        if (line.startsWith("`")) {
          if (line.substring(1).equals("ready")) {
            this.isReady = true;
            for (int i = 0; i < maxClientsCount; i++) {
              if (threads[i] != null) {
                threads[i].os.println("`ready");
              }
            }
          } else if (line.substring(1).equals("inQuestionPrompt")) {
            inQuestionPrompt = true;
          } else if (line.substring(1).equals("submitted")) {
            this.submittedAnswer = true;
            for (int i = 0; i < maxClientsCount; i++) {
              if (threads[i] != null) {
                threads[i].os.println("`submitted");
                //threads[i].os.println(threads[i].answers[currentround][questionNumber]);
              }
            }
          } else if (line.substring(1).equals("allPlayersSubmitted")){
            allPlayersSubmitted = true;
              for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] != null) {
                  this.os.println(threads[i].answers[currentround][questionNumber]);
                  this.questionNumber++;
                }
            } 
          }
        }

        if (this == threads[0] && inQuestionPrompt) {
          for (int i = 0; i < maxClientsCount; i++) {
            if (threads[i] != null) {
              threads[i].myRounds[1][1] = true;
              if (!this.playerList.contains(threads[i].player)) {
                this.playerList.add(threads[i].player);
              }
            }
          }
          if (myGame == null) {
            myGame = new Game(10, this.playerList);
            System.out.println(myGame.toString());
          }
        }
        synchronized (this) {
          if (inQuestionPrompt) {
            for (int i = 0; i < maxClientsCount; i++) {
              if (threads[i] != null) {
                threads[i].os.println("{" + threads[0].myGame.getInGamePlayers().get(i)
                    .getQuestionsToAnswerForRound(currentround).get(questionNumber));
                //threads[i].os.println("}" + threads[0].myGame.getInGamePlayers().get(i).getQuestionsToAnswerForRound(0).get(1));
              }
            }
          }
        }

        if (line.startsWith("~")) {
          synchronized (this) {
            this.answers[currentround][questionNumber] = line.substring(1);
            if (allPlayersSubmitted){
              for (int i = 0; i < maxClientsCount; i++) {
                if (this == threads[i]) {
                  threads[0].myGame.getInGamePlayers().get(i).addAnswer(this.answers[currentround][questionNumber],
                      threads[0].myGame.getInGamePlayers().get(i).getQuestionsToAnswerForRound(currentround)
                              .get(questionNumber));
                }
              }
              allPlayersSubmitted = false;
            }
          }
        }

        // Exiting chat and game
        if (line.startsWith("/quit")) {
          break;
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
            if (!line.startsWith("}") && !line.startsWith("}") && !line.startsWith("|") && !line.startsWith("~")
                && !line.startsWith("`")) {
              for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] != null && threads[i].clientName != null) {
                  threads[i].os.println("<" + name + "> " + line);
                }
              }
            }
          }
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