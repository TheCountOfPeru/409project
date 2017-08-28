import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * This is the server which creates threads to communicate with clients.
 * @author Jonathan
 *
 */
class Server{
	/**
	 * The socket for the server
	 */
	Socket aSocket;
	/**
	 * the serversocket
	 */
	ServerSocket serverSocket;
	/**
	 * For receiving messages from clients
	 */
	BufferedReader in;
	/**
	 * For sending messages to clients
	 */
	PrintWriter out;
	/**
	 * For sending objects to clients
	 */
	ObjectOutputStream objout;
	/**
	 * For receiving objects from clients
	 */
	ObjectInputStream objin;
	DataInputStream Intsin;
	/**
	 * The constructor for a server
	 */
	public Server() { // throws IOException {
		try {
			serverSocket = new ServerSocket(9898);
			
		} catch (IOException e) {
			System.out.println("Error creating the socket...");
		}
		System.out.println("Server is running");
	}
	
	public static void main(String[] args) throws IOException{
		try{
			Server myserver = new Server();			
			myserver.aSocket = myserver.serverSocket.accept();			
			myserver.in = new BufferedReader(new InputStreamReader(myserver.aSocket.getInputStream()));			
			myserver.out = new PrintWriter((myserver.aSocket.getOutputStream()), true);			
			myserver.objin = new ObjectInputStream(myserver.aSocket.getInputStream());			
			myserver.objout = new ObjectOutputStream(myserver.aSocket.getOutputStream());
			myserver.Intsin = new DataInputStream(myserver.aSocket.getInputStream());
			SubServer s1 = new SubServer(myserver.in, myserver.out, myserver.objout, myserver.objin, myserver.Intsin); 
			Thread t1 = new Thread (s1);
			t1.start();
			System.out.println("A client has connected...");
			
			myserver.aSocket = myserver.serverSocket.accept();
			myserver.in = new BufferedReader(new InputStreamReader(myserver.aSocket.getInputStream()));
			myserver.out = new PrintWriter((myserver.aSocket.getOutputStream()), true);
			myserver.objin = new ObjectInputStream(myserver.aSocket.getInputStream());
			myserver.objout = new ObjectOutputStream(myserver.aSocket.getOutputStream());
			myserver.Intsin = new DataInputStream(myserver.aSocket.getInputStream());
			SubServer s2 = new SubServer(myserver.in, myserver.out, myserver.objout, myserver.objin, myserver.Intsin); 
			Thread t2 = new Thread (s2);
			t2.start();
			System.out.println("A client has connected...");
			
			myserver.aSocket = myserver.serverSocket.accept();
			myserver.in = new BufferedReader(new InputStreamReader(myserver.aSocket.getInputStream()));
			myserver.out = new PrintWriter((myserver.aSocket.getOutputStream()), true);
			myserver.objin = new ObjectInputStream(myserver.aSocket.getInputStream());
			myserver.objout = new ObjectOutputStream(myserver.aSocket.getOutputStream());
			myserver.Intsin = new DataInputStream(myserver.aSocket.getInputStream());
			SubServer s3 = new SubServer(myserver.in, myserver.out, myserver.objout, myserver.objin, myserver.Intsin); 
			Thread t3 = new Thread (s3);
			t3.start();
			System.out.println("A client has connected...");
			
			myserver.aSocket = myserver.serverSocket.accept();
			myserver.in = new BufferedReader(new InputStreamReader(myserver.aSocket.getInputStream()));
			myserver.out = new PrintWriter((myserver.aSocket.getOutputStream()), true);
			myserver.objin = new ObjectInputStream(myserver.aSocket.getInputStream());
			myserver.objout = new ObjectOutputStream(myserver.aSocket.getOutputStream());
			myserver.Intsin = new DataInputStream(myserver.aSocket.getInputStream());
			SubServer s4 = new SubServer(myserver.in, myserver.out, myserver.objout, myserver.objin, myserver.Intsin); 
			Thread t4 = new Thread (s4);
			t4.start();
			System.out.println("A client has connected...");
			
			myserver.aSocket = myserver.serverSocket.accept();
			myserver.in = new BufferedReader(new InputStreamReader(myserver.aSocket.getInputStream()));
			myserver.out = new PrintWriter((myserver.aSocket.getOutputStream()), true);
			myserver.objin = new ObjectInputStream(myserver.aSocket.getInputStream());
			myserver.objout = new ObjectOutputStream(myserver.aSocket.getOutputStream());
			myserver.Intsin = new DataInputStream(myserver.aSocket.getInputStream());
			SubServer s5 = new SubServer(myserver.in, myserver.out, myserver.objout, myserver.objin, myserver.Intsin); 
			Thread t5 = new Thread (s5);
			t3.start();
			System.out.println("A client has connected...");
			
			myserver.aSocket = myserver.serverSocket.accept();
			myserver.in = new BufferedReader(new InputStreamReader(myserver.aSocket.getInputStream()));
			myserver.out = new PrintWriter((myserver.aSocket.getOutputStream()), true);
			myserver.objin = new ObjectInputStream(myserver.aSocket.getInputStream());
			myserver.objout = new ObjectOutputStream(myserver.aSocket.getOutputStream());
			myserver.Intsin = new DataInputStream(myserver.aSocket.getInputStream());
			SubServer s6 = new SubServer(myserver.in, myserver.out, myserver.objout, myserver.objin, myserver.Intsin); 
			Thread t6 = new Thread (s6);
			t3.start();
			System.out.println("A client has connected...");
			
//			try {
//			t1.join();
//			t2.join();
//		} catch (InterruptedException ee) {
//			System.out.println("Error occured");
//		}
//		myserver.in.close();
//		myserver.out.close();
			
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	
	
}