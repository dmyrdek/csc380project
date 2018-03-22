package csc380Project;

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
    String server = args[0];
    int port = 10694;
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

  public static void connectClient(int port) throws Exception {

    Socket sock = new Socket("tcp://0.tcp.ngrok.io", port);
    // reading from keyboard (keyRead object)
    BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
    // sending to client (pwrite object)
    OutputStream ostream = sock.getOutputStream();
    PrintWriter pwrite = new PrintWriter(ostream, true);

    // receiving from server ( receiveRead  object)
    InputStream istream = sock.getInputStream();
    BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));

    System.out.println("Start the chitchat, type and press Enter key");

    String receiveMessage, sendMessage;
    while (true) {
      sendMessage = keyRead.readLine(); // keyboard reading
      pwrite.println(sendMessage); // sending to server
      pwrite.flush(); // flush the data
      if ((receiveMessage = receiveRead.readLine()) != null) //receive from server
      {
        System.out.println(receiveMessage); // displaying at DOS prompt
      }
    }
  }

}