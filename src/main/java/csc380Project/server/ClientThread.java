package csc380Project.server;

import csc380Project.game.Player;
import csc380Project.game.Game;
import csc380Project.game.QuestionPack;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

// For every client's connection we call this class
public class ClientThread extends Thread {
  private String clientName = null;
  private DataInputStream is = null;
  private PrintStream os = null;
  private Socket clientSocket = null;
  private final ClientThread[] threads;
  private final int maxClientsCount;
  private int roundsNum = 16;
  private String name = "";
  private QuestionPack qp = new QuestionPack().addAllQuestions();
  private boolean[][] myVotes = new boolean[roundsNum][2];
  private int[][] totalVotes = new int[roundsNum][2];
  private Player player;
  private final int numOfPlayers = 16; //allways test for the max number of players
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
  private boolean inVotingPrompt = false;
  private boolean getQuestions = false;
  private boolean getVotes = false;
  private int clientVoteOne = 0;
  private int clientVoteTwo = 0;
  private boolean inVotingResults = false;
  private int inVotingResultsTime = 10;
  private int inVotingLobbyCounter = 0;
  private int votingPromptQuestionNumber = 0;
  private int votingResultQuestionNumber = 0;


  public String getUserName() {
    return name;
  }

  public Game getMyGame() {
    return myGame;
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

  /*
  public Player[] getLeaderBoard (){
    Player[] leaderboard = new Player [this.threads[0].myGame.getInGamePlayers().size()];
    ArrayList<Player> players = this.threads[0].myGame.getInGamePlayers();
    Collections.sort(players);
    for (int i = 0; i<leaderboard.length; i++) {
      leaderboard[i] = players.get(i);
    }
    return leaderboard;
  }
  */
  public void run() {
    int maxClientsCount = this.maxClientsCount;
    ClientThread[] threads = this.threads;

    try {
      /*
       * Create input and output streams for this client.
       */
      is = new DataInputStream(clientSocket.getInputStream());
      os = new PrintStream(clientSocket.getOutputStream());

      if (this.name.equals("")) {
        String str = is.readLine();
        if (str.startsWith("}")) {
          this.name = str.substring(1);
          player = new Player(name);
          if (this == this.threads[0]) {
            this.isHost = true;
          }
          if (isHost){
            String str2 = is.readLine();
            if(str2.startsWith("%")){
              System.out.println(str2.substring(1));
              roundsNum = Integer.parseInt(str2.substring(1));
            }
          }else{
            roundsNum = threads[0].roundsNum;
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
              } else if (inVotingPrompt) {
                countDownTime = inQuestionPromptTime;
                inVotingPrompt = false;
              } else if (inVotingResults) {
                countDownTime = inVotingResultsTime;
                inVotingResults = false;
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
            if (!threads[0].playerList.contains(threads[i].player)) {
              threads[0].playerList.add(threads[i].player);
            }
            /*I am one step close to the edge...and I'm about to*/break;
          }
        }
        for (int i = 0; i < maxClientsCount; i++) {
          if (threads[i] != null && threads[i] != this) {
            threads[i].os.println("~A new user " + name + " has joined!");
            threads[i].os.println("}" + name);
            if (!threads[0].playerList.contains(threads[i].player)) {
              threads[0].playerList.add(threads[i].player);
            }
          }
        }
      }

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
            getQuestions = true;
            for (int i = 0; i < maxClientsCount; i++) {
              if (threads[i] != null) {
                threads[i].os.println("%" + roundsNum);
              }
            }
          } else if (line.substring(1).equals("submitted")) {
            this.submittedAnswer = true;
            for (int i = 0; i < maxClientsCount; i++) {
              if (threads[i] != null) {
                threads[i].os.println("`submitted");
                //threads[i].os.println(threads[i].answers[currentround][questionNumber]);
              }
            }
          } else if (line.substring(1).equals("allPlayersSubmitted")) {
            this.allPlayersSubmitted = true;
            for (int i = 0; i < maxClientsCount; i++) {
              if (threads[i] != null) {
                this.os.println(threads[i].answers[currentround][questionNumber]);
              }
            }
            if (this.questionNumber == 0) {
              this.questionNumber = 1;
            } else if (this.questionNumber == 1) {
              this.currentround++;
              this.questionNumber = 0;
            }
          } else if (line.substring(1).equals("inVotingPrompt")) {
            this.inVotingPrompt = true;
            this.getVotes = true;
            this.os.println("{" + threads[0].myGame.getGameQuestions().getQuestions()[votingPromptQuestionNumber]);
            this.os.println("}" + threads[0].myGame.getAllAnswersForQuestion(
                threads[0].myGame.getGameQuestions().getQuestions()[votingPromptQuestionNumber])[0]);
            this.os.println("%" + threads[0].myGame.getAllAnswersForQuestion(
                threads[0].myGame.getGameQuestions().getQuestions()[votingPromptQuestionNumber])[1]);
            votingPromptQuestionNumber++;
          } else if (line.substring(1).equals("inVotingResults")) {
            inVotingResults = true;

            this.os.println("{" + threads[0].myGame.getGameQuestions().getQuestions()[votingResultQuestionNumber]);

            this.os.println("}" + threads[0].myGame.getAllAnswersForQuestion(
                threads[0].myGame.getGameQuestions().getQuestions()[votingResultQuestionNumber])[0]);

            this.os.println("%" + threads[0].myGame.getAllAnswersForQuestion(
                threads[0].myGame.getGameQuestions().getQuestions()[votingResultQuestionNumber])[1]);

            this.os.println("}" + "By: \""
                + threads[0].myGame.whoAnsweredQuestion(
                    threads[0].myGame.getAllAnswersForQuestion(
                        threads[0].myGame.getGameQuestions().getQuestions()[votingResultQuestionNumber])[0],
                    threads[0].myGame.getGameQuestions().getQuestions()[0])
                + "\"");

            this.os.println("%" + "By: \""
                + threads[0].myGame.whoAnsweredQuestion(
                    threads[0].myGame.getAllAnswersForQuestion(
                        threads[0].myGame.getGameQuestions().getQuestions()[votingResultQuestionNumber])[1],
                    threads[0].myGame.getGameQuestions().getQuestions()[0])
                + "\"");

            this.os.println("}Total votes: " + clientVoteOne);

            this.os.println("%Total votes: " + clientVoteTwo);

            votingResultQuestionNumber++;
          }
        }

        synchronized (this) {
          if (getQuestions) {
            if (this == threads[0]) {
              if (myGame == null) {
                myGame = new Game(10, threads[0].playerList);
                System.out.println(myGame.toString());
              }
            }

            for (int i = 0; i < maxClientsCount; i++) {
              if (threads[i] != null && threads[0].myGame != null
                  && threads[0].myGame.getInGamePlayers().get(i) != null) {
                threads[i].os.println("%" + threads[0].myGame.getNumOfRounds()*roundsNum);
                
                threads[i].os.println("{" + threads[0].myGame.getInGamePlayers().get(i)
                    .getQuestionsToAnswerForRound(currentround).get(questionNumber));
              }
            }
            getQuestions = false;
          }
        }

        synchronized (this) {
          if (getVotes) {
            if (line.startsWith("}")) {
              if (line.substring(1).equals("1")) {
                for (int i = 0; i < maxClientsCount; i++) {
                  if (threads[i] != null) {
                    threads[i].clientVoteOne++;

                    threads[0].myGame.voteForAnswer(
                        threads[0].myGame.getAllAnswersForQuestion(
                            threads[0].myGame.getGameQuestions().getQuestions()[votingPromptQuestionNumber-1])[0],
                        threads[0].myGame.getGameQuestions().getQuestions()[votingPromptQuestionNumber-1]);

                    threads[i].os
                        .println(
                            "1 vote for \""
                                + threads[0].myGame.getAllAnswersForQuestion(
                                    threads[0].myGame.getGameQuestions().getQuestions()[votingPromptQuestionNumber-1])[0]
                                + "\"");

                    threads[i].os.println("`submitted");
                  }
                }
              } else if (line.substring(1).equals("2")) {
                for (int i = 0; i < maxClientsCount; i++) {
                  if (threads[i] != null) {
                    threads[i].clientVoteTwo++;

                    threads[0].myGame.voteForAnswer(
                        threads[0].myGame.getAllAnswersForQuestion(
                            threads[0].myGame.getGameQuestions().getQuestions()[votingPromptQuestionNumber-1])[1],
                        threads[0].myGame.getGameQuestions().getQuestions()[votingPromptQuestionNumber-1]);

                    threads[i].os
                        .println(
                            "1 vote for \""
                                + threads[0].myGame.getAllAnswersForQuestion(
                                    threads[0].myGame.getGameQuestions().getQuestions()[votingPromptQuestionNumber-1])[1]
                                + "\"");

                    threads[i].os.println("`submitted");
                  }
                }
              }
            }
          }
        }

        synchronized (this) {
          if (line.startsWith("~")) {
            this.answers[currentround][questionNumber] = line.substring(1);
            if (this.submittedAnswer) {
              for (int i = 0; i < maxClientsCount; i++) {
                if (this == threads[i]) {
                  /*if (questionNumber == 1) {
                    threads[0].myGame.getInGamePlayers().get(i).addAnswer(this.answers[currentround][0],
                        threads[0].myGame.getInGamePlayers().get(i).getQuestionsToAnswerForRound(currentround).get(0));
                  } else {
                    threads[0].myGame.getInGamePlayers().get(i).addAnswer(this.answers[currentround - 1][1],
                        threads[0].myGame.getInGamePlayers().get(i).getQuestionsToAnswerForRound(currentround - 1)
                            .get(1));
                  }*/
                  threads[0].myGame.getInGamePlayers().get(i).addAnswer(this.answers[currentround][questionNumber],
                        threads[0].myGame.getInGamePlayers().get(i).getQuestionsToAnswerForRound(currentround).get(questionNumber));
                }
              }
              this.allPlayersSubmitted = false;
            }
          }
        }

        // Exiting chat and game
        if (line.startsWith("/quit")) {
          /*I am one step close to the edge...and I'm about to*/break;
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
                    /*I am one step close to the edge...and I'm about to*/break;
                  }
                }
              }
            }
          }
        } else {
          /* The message is public, broadcast it to all other clients. */
          synchronized (this) {
            if (!line.startsWith("}") && !line.startsWith("{") && !line.startsWith("|") && !line.startsWith("~")
                && !line.startsWith("`") && !line.startsWith("%")) {
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