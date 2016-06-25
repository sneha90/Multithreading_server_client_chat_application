package CS532HW5;

//import files
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

//start of class
public class ChatServer extends JFrame {
	  //text field to display data from client
	  private JTextArea textArea = new JTextArea();

	  private Hashtable outputStreams = new Hashtable();//for multiple clients
	  private ServerSocket serverSocket;

	  //main method
	  public static void main(String[] args) {
	    new ChatServer();//call to constructor
	  }
	  
	  //constructor
	  public ChatServer() {
	    setLayout(new BorderLayout());
	    add(new JScrollPane(textArea), BorderLayout.CENTER);
	    setTitle("Chat Server");
	    setSize(500, 300);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setLocationRelativeTo(null); 	    
	    setVisible(true); 

	    textArea.setEditable(false); 
	     listen();
	  }
	  private void listen() {
	      try {
	        serverSocket = new ServerSocket(8000);
	        textArea.append("MultiThreadServer started at " + new Date() + '\n');
	        
	      while (true) {
	        Socket socket = serverSocket.accept();
	        textArea.append("Connection from " + socket + " at " + new Date() + '\n');
	        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
	        outputStreams.put(socket, outputStream);
	        new ChatServerThread(this, socket);
	      }
	      
	    }
	    catch(IOException ex) {
	      System.err.println(ex);
	    }
	  }
	  
	//generic to  refer each client
	  Enumeration getOutputStreams(){
	      return outputStreams.elements();
	  }
	  
	//send message to all connected clients
	  void sendMessageToAll(String message){
	      for (Enumeration e = getOutputStreams(); e.hasMoreElements();){
	          DataOutputStream outputStream = (DataOutputStream)e.nextElement();
	          try {
	        	  outputStream.writeUTF(message);
	          } catch (IOException ex) {
	              System.err.println(ex);
	          }
	      }
	  }
	  class ChatServerThread extends Thread {
	      private ChatServer server;
	      private Socket socket;
	      public ChatServerThread(ChatServer server, Socket socket) {
	          this.socket = socket;
	          this.server = server;
	          start();
	      }
	      public void run() {
	          try {
	              DataInputStream din = new DataInputStream(socket.getInputStream());
	              while (true) {
	                  String string = din.readUTF();
	                  server.sendMessageToAll(string);
	                  textArea.append(string + '\n');
	              }
	          }
	          catch(IOException e) {
	              System.err.println(e);
	          }
	      }//end of run
	  }//end of ChatServerThread
}//end of class

