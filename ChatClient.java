package CS532HW5;
//import files
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//start of class
public class ChatClient extends JFrame implements Runnable {
	
	  //text fields for receiving name and text
	  private JTextField textField = new JTextField();
	  private JTextField nameJtf = new JTextField("Enter a name");
	  private JTextArea textArea = new JTextArea();
	  //create socket
	  private Socket socket;
	  //IO streams
	  private DataOutputStream outputStream;
	  private DataInputStream inputStream;
	  
	  //main method
	  public static void main(String[] args) {
	    new ChatClient();//call to constructor
	  }
	  
	  //constructor
	  public ChatClient() {
		// Panel p1 to hold  text field
	    JPanel p1 = new JPanel();
	    p1.setLayout(new BorderLayout());
	    p1.add(new JLabel("Enter text"), BorderLayout.WEST);
	    p1.add(textField, BorderLayout.CENTER);
	    
	 // Panel p2 to hold the name 
	    JPanel p2 = new JPanel();
	    p2.setLayout(new BorderLayout());
	    p2.add(new JLabel("Name"), BorderLayout.WEST);
	    p2.add(nameJtf, BorderLayout.CENTER);
	    
	    JPanel p = new JPanel();
	    p.setLayout(new BorderLayout());
	    p.add(p1, BorderLayout.SOUTH);
	    p.add(p2, BorderLayout.NORTH);
	    setLayout(new BorderLayout());
	    add(p, BorderLayout.NORTH);
	    add(new JScrollPane(textArea), BorderLayout.CENTER);

	    textField.addActionListener(new ButtonListener()); 

	    textArea.setEditable(false); 

	    setTitle("Multiple Chat Client");
	    setSize(500, 300);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setLocationRelativeTo(null); // Center the frame
	    setVisible(true); //  show the frame

	    try {
	        socket = new Socket("localhost", 8000);
	        inputStream = new DataInputStream(socket.getInputStream());
	        outputStream = new DataOutputStream(socket.getOutputStream());
	        new Thread(this).start();
	    }
	    catch (IOException ex) {
	        textArea.append(ex.toString() + '\n');
	    }
	  }//end of constructor

	  //action listener to perform chat
	  private class ButtonListener implements ActionListener {
	    @Override 
	    public void actionPerformed(ActionEvent e) {
	      try {
	        String string = nameJtf.getText().trim() + ": " + textField.getText().trim();//concat name and text entered by clients
	        outputStream.writeUTF(string);
	        textField.setText("");
	      }
	      catch (IOException ex) {
	        System.err.println(ex);
	      }
	    }
	  }//end of listener
	  public void run(){
	      try{
	          while(true){
	              String text = inputStream.readUTF();
	              textArea.append(text + '\n');
	          }
	      } catch (IOException ex) {
	          System.err.println(ex);
	      }
	  }//end of run
}//end of class
