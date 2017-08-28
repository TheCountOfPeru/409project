import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
/**
 * This is for the threads that server creates and must run
 * @author Jonathan Yee, Jacob Turnbull, Haoxian Zhang
 * @version 1.0
 * @since Mar 31, 2017
 */
class SubServer implements Runnable, ARS, SyncBooking{
	/**
	 * The prepared statement
	 */
	PreparedStatement preparedStmt;
	/**
	 * Query string
	 */
	String query;
	/**
	 * Connection to database
	 */
	Connection myConn;
	/**
	 * Statement for database queries
	 */
	Statement myStmt;
	/**
	 * ResultSet for database
	 */
	ResultSet myRs;
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
	 * For storing multiple flights
	 */
	Vector<Flight> manyFlights;
	/**
	 * Information on a single flight
	 */
	Flight tempFlight;
	/**
	 * Information on a single ticket
	 */
	Ticket aTicket;
	/**
	 * For storing many tickets
	 */
	Vector<Ticket> manyTickets;
	/**
	 * For storing the information on a single passenger
	 */
	Passenger customer;
	/**
	 * Class constructor
	 * @param in Message input
	 * @param out Message output
	 * @param objout Object output
	 * @param objin Object output
	 */
	public SubServer(BufferedReader in, PrintWriter out, ObjectOutputStream objout, ObjectInputStream objin,DataInputStream Intsin){
		int[] t = {0,0,0};
		this.in = in;
		this.out = out;
		this.objout = objout;
		this.objin = objin;
		this.manyFlights = new Vector<Flight>();
		this.tempFlight = new Flight(0, null, null, null, null, 0, 0, 0, 0);
		this.manyTickets = new Vector<Ticket>();
		this.aTicket = new Ticket(0,null,null,0,null,null,null,0,null);
		this.Intsin = Intsin;
		this.customer = new Passenger(null, null, t);
	}
	/**
	 * The run method is for communicating and dealing with input from clients and also sending data to clients. Also edits a database.
	 */
	@Override
	public void run() {
		String message = null;
		// Setup JDBC stuff first
		try {
			//String URL = "jdbc:mysql://localhost:3306/schulichairline?autoReconnect=true&useSSL=false"; //for Hosman
			//String URL = "jdbc:mysql://localhost:3306/schulichairline?autoReconnect=true&useSSL=false"; //for Jonathan
			String URL = "jdbc:mysql://localhost:3306/schulichairline?autoReconnect=true&useSSL=false";//for Jacob
			//myConn = DriverManager.getConnection(URL, "root", "5shifanshuA"); //for Hosman
			myConn = DriverManager.getConnection(URL, "root", "zIOnZFVpmoapeupbRH74"); // for Jonathan
			//myConn = DriverManager.getConnection(URL, "student" , "student"); //for Jacob
			myStmt = myConn.createStatement();
			//myRs = myStmt.executeQuery("SELECT * FROM ensf409lab7.flights;"); // for Hosman
			myRs = myStmt.executeQuery("SELECT * FROM schulichairline.flights"); // for Jonathan
			//myRs = myStmt.executeQuery("SELECT * FROM finalproject.flights");//for Jacob
			/*
			while(myRs.next()){
				System.out.println("Flight :"+myRs.getString("flightNum"));
			}*/
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Connected to the database....");
		
		while(true){
			//should keep looping as long as a client is open and connected
			// Message format "Client type;Operations;Arguments", e.g."P;SEARCH DATE;2017/03/10"
			// String array to store commands and arguments
			String[] commands = null;
			try{
				message = in.readLine();//receive message from client. Should be what function needs to be called.
				
				// Extract the command string
				commands = message.split(";");
				
			}catch(IOException e){
				e.printStackTrace();
			}
			// use if and else statements to determine which function to use
			if(commands[1].equals("QUIT")){
				System.out.println(Thread.currentThread());
				break;// when client is done and closes
			}
			//
			else if(commands[1].equals("searchDate")){
				System.out.println("Calling search date method with parameter: "+commands[2]);
				manyFlights.removeAllElements(); // clear it first
				manyFlights = new Vector<Flight>();
				this.searchDate(commands[2]); // send parameter
				try{
					objout.writeObject(manyFlights); // manyFlights should have data and is sent to the passenger Client for viewing
				}catch(IOException e3){
					System.out.println("Error sending data to ClientP.");
					e3.printStackTrace();
				}catch(NullPointerException as){
					System.out.println("NullPointerException here.");
				}
			}
			//
			else if(commands[1].equals("searchDeparture")){
				System.out.println("Calling search departure method with parameter: "+ commands[2]);
				manyFlights.removeAllElements(); // clear it first
				manyFlights = new Vector<Flight>();
				this.searchDeparture(commands[2]); // send parameter
				try{
					objout.writeObject(manyFlights); // manyFlights should have data and is sent to the passenger Client for viewing
				}catch(IOException e3){
					System.out.println("Error sending data to ClientP.");
					e3.printStackTrace();
				}
				
			}
			//
			else if(commands[1].equals("searchDestination")){
				System.out.println("Calling search destination method with parameter: "+ commands[2]);
				manyFlights.removeAllElements(); // clear it first
				manyFlights = new Vector<Flight>();
				this.searchDestination(commands[2]); // send parameter
				try{
					objout.writeObject(manyFlights); // manyFlights should have data and is sent to the passenger Client for viewing
				}catch(IOException e3){
					System.out.println("Error sending data to ClientP.");
					e3.printStackTrace();
				}
			}
			//
			else if(commands[1].equals("addFlight")){
				System.out.println("addFlight");
				// I need a flight
				try {
					tempFlight = (Flight) objin.readObject(); // clientA should send a flight object. server reads it into the database
				} catch (ClassNotFoundException | IOException e) {
					System.out.println("Error receiving data from ClientP.");
					e.printStackTrace();
				}
				this.addFlight(tempFlight);
			}
			//
			else if(commands[1].equals("addManyFlights")){
				System.out.println("addManyFlights");
				// this needs a vector of flights to parse
				this.addManyFlights();
				
			}
			//
			else if(commands[1].equals("bookSeat")){
				System.out.println("bookSeat");
				try {
					
					customer = (Passenger)objin.readObject(); // client sends passenger information to server				
				} catch (IOException | ClassNotFoundException e) {
					System.out.println("Error receiving data from ClientP.");
					e.printStackTrace();
				}
				aTicket = bookSeat(Integer.parseInt(commands[2]), customer); // book the ticket, commands[2] has the flightID
				try {
					objout.writeObject(aTicket); // send ticket back to client
				} catch (IOException e) {
					System.out.println("Error sending ticket to ClientP.");
					e.printStackTrace();
				}				
			}
			else if(commands[1].equals("cancelBookedTicket")){
				System.out.println("cancelBookedTicket");
				// I need the flightID and ticketID to when I call the method to delete a ticket
				int flightID, ticketID;
				flightID= ticketID =0;
				flightID = Integer.parseInt(commands[2]);
				ticketID = Integer.parseInt(commands[3]);
				System.out.println(flightID +" "+ ticketID);
				cancelBookedTicket(flightID,ticketID);
				
			}
			else if(commands[1].equals("allTickets")){
				System.out.println("allTickets");
				this.allTickets();
			}
		}// end of while
		// close connections
		/*
		try{
			this.in.close();
			this.out.close();
			this.objin.close();
			this.objout.close();
		}catch(IOException e){
			System.out.println("Unexpected exception: " + e.getMessage());
		}*/
				
	}
	/**
	 * For searching the database with a date
	 */
	@Override
	public void searchDate(String input) {
		query ="SELECT * FROM schulichairline.flights WHERE date LIKE " 
				+ "'%"+input+"%'"; // search for date
		try{
			preparedStmt = myConn.prepareStatement(query);			
			ResultSet myRs1 = preparedStmt.executeQuery(query);			
			while(myRs1.next()){
				// set tempFlight with result set
				tempFlight = new Flight(myRs1.getInt("flightNum"), myRs1.getString("source"), myRs1.getString("dest"), myRs1.getString("date")
						, myRs1.getString("time"), myRs1.getInt("duration"), myRs1.getInt("totalSeats"), myRs1.getInt("remainingSeats"), 
						myRs1.getDouble("price"));				
				//add it to manyFlights				
				manyFlights.add(tempFlight);				
			}
		}catch (SQLException t) {			
			t.printStackTrace();			
		}catch(NullPointerException n){
			System.out.println("Null!");
		}
	}
	/**
	 * For searching the database with a departure
	 */
	@Override
	public void searchDeparture(String input) {
		query ="SELECT * FROM schulichairline.flights WHERE source LIKE " 
				+ "'%"+input+"%'"; // search for departure
		//query ="SELECT * FROM finalproject.flights WHERE source LIKE " 
		//		+ "'%"+input+"%'"; // search for departure
		try{
			preparedStmt = this.myConn.prepareStatement(query);
			ResultSet myRs1 = preparedStmt.executeQuery(query);
			while(myRs1.next()){
				//Use the constructor instead?
				// set tempFlight with result set
				tempFlight = new Flight(myRs1.getInt("flightNum"), myRs1.getString("source"), myRs1.getString("dest"), myRs1.getString("date")
						, myRs1.getString("time"), myRs1.getInt("duration"), myRs1.getInt("totalSeats"), myRs1.getInt("remainingSeats"),
						myRs1.getDouble("price"));
				
				//add it to manyFlights
				
				manyFlights.add(tempFlight);
				
			}
		}catch (SQLException t) {
			t.printStackTrace();
		}
	}
	/**
	 * For searching the database with a destination
	 */
	@Override
	public void searchDestination(String input) {
		query ="SELECT * FROM schulichairline.flights WHERE dest LIKE " 
				+ "'%"+input+"%'"; // search for 
		try{
			preparedStmt = this.myConn.prepareStatement(query);
			ResultSet myRs1 = preparedStmt.executeQuery(query);
			while(myRs1.next()){
				//Use the constructor instead?
				// set tempFlight with result set
				tempFlight = new Flight(myRs1.getInt("flightNum"), myRs1.getString("source"), myRs1.getString("dest"), myRs1.getString("date")
						, myRs1.getString("time"), myRs1.getInt("duration"), myRs1.getInt("totalSeats"), myRs1.getInt("remainingSeats"),
						myRs1.getDouble("price"));
				
				//add it to manyFlights
				
				manyFlights.add(tempFlight);
				
			}
		}catch (SQLException t) {
			t.printStackTrace();
		}
	}
	/**
	 * Receive flight from client as object then put it into a database
	 */
	@Override
	public void addFlight(Flight newFlight) {
		query = "INSERT INTO schulichairline.flights (flightNum, source, dest, date, time, duration, totalSeats, remainingSeats, price)"
		    + " values (?, ?, ?, ?, ?, ?, ?, ?,?)";//Jonathan
		//query = "INSERT INTO finalproject.flights (flightNum, source, dest, date, time, duration, totalSeats, remainingSeats, price)"
		//        + " values (?, ?, ?, ?, ?, ?, ?, ?,?)";//Jacob
		try {
			preparedStmt = this.myConn.prepareStatement(query);
			preparedStmt.setInt (1, newFlight.getFlightNum());
			preparedStmt.setString (2, newFlight.getSource());
			preparedStmt.setString (3, newFlight.getDest());
			preparedStmt.setString (4, newFlight.getDate());
			preparedStmt.setString (5, newFlight.getTime());
	      	preparedStmt.setInt (6, newFlight.getDuration());
	      	preparedStmt.setInt (7, newFlight.getTotalSeats());
	      	preparedStmt.setInt(8, newFlight.getRemainingSeats());
	      	preparedStmt.setDouble(9, newFlight.getPrice());
	      	preparedStmt.execute();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
	}
	/**
	 * Recive vector with many flights from a client to add to a database. 
	 */
	@Override
	public void addManyFlights() {
		try {
			manyFlights = (Vector<Flight>) objin.readObject();
		}catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		query = "INSERT INTO schulichairline.flights (flightNum, source, dest, date, time, duration, totalSeats, remainingSeats, price)"
		      + " values (?, ?, ?, ?, ?, ?, ?, ?,?)";//Jonathan
		//query = "INSERT INTO finalproject.flights (flightNum, source, dest, date, time, duration, totalSeats, remainingSeats, price)"
		//       + " values (?, ?, ?, ?, ?, ?, ?, ?,?)";//Jacob
		// loop through indexes
		for(int i = 0; i < manyFlights.size(); i++){
		try {
			preparedStmt = this.myConn.prepareStatement(query);
			
			preparedStmt.setInt (1, manyFlights.get(i).getFlightNum());
			preparedStmt.setString (2, manyFlights.get(i).getSource());
			preparedStmt.setString (3, manyFlights.get(i).getDest());
			preparedStmt.setString (4, manyFlights.get(i).getDate());
			preparedStmt.setString (5, manyFlights.get(i).getTime());
	      	preparedStmt.setInt (6, manyFlights.get(i).getDuration());
	      	preparedStmt.setInt (7, manyFlights.get(i).getTotalSeats());
	      	preparedStmt.setInt(8, manyFlights.get(i).getRemainingSeats());
	      	preparedStmt.setDouble(9, manyFlights.get(i).getPrice());
	      	preparedStmt.execute();
		} catch (SQLException e1) {
			e1.printStackTrace();
			}
		
		}
		
	}
	/**
	 * Receives a flightID and passenger to build a ticket which is passed back to the client
	 */
	@Override
	synchronized public Ticket bookSeat(int flightID, Passenger customer) {
		int seats = 0;
		Ticket tempTicket = null;
		// Check if seats are available
		query = "SELECT * FROM schulichairline.flights WHERE flightNum = "
			+ "'"+flightID+"'";
		try {
			//preparedStmt = this.myConn.prepareStatement(query);
			myRs = myStmt.executeQuery(query);
			while(myRs.next()){
				seats = myRs.getInt("remainingSeats");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			}
		if(seats == 0){
			return null;
		}
		// if seats are available update the seats remaining
		seats--;
		query ="UPDATE schulichairline.flights SET remainingSeats = ? WHERE flightNum = ?";
		try {
			preparedStmt = this.myConn.prepareStatement(query);
			preparedStmt.setInt(1, seats);
			preparedStmt.setInt(2, flightID);
			preparedStmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			}
		// build the ticket
		query = "SELECT * FROM schulichairline.flights WHERE flightNum = "
				+ "'"+flightID+"'";
		try {
			preparedStmt = this.myConn.prepareStatement(query);
			myRs = preparedStmt.executeQuery(query);
			while(myRs.next()){
				tempTicket = new Ticket(0, customer.getFname(), customer.getLname(), myRs.getInt("flightNum"), myRs.getString("dest"), myRs.getString("source"),
						myRs.getString("time"), myRs.getInt("duration"), myRs.getString("date"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			}
		// add the ticket to the database
		query = "INSERT INTO schulichairline.bookedtickets (flightID, firstName, lastName, dest, depart, date, time, duration) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			preparedStmt = this.myConn.prepareStatement(query);
			
			preparedStmt.setInt (1, tempTicket.getFlightNum());
			preparedStmt.setString (2, tempTicket.getFirstName());
			preparedStmt.setString (3, tempTicket.getLastName());
			preparedStmt.setString (4, tempTicket.getTdest());
			preparedStmt.setString (5, tempTicket.getTsource());
	      	preparedStmt.setString (6, tempTicket.getTdate());
	      	preparedStmt.setString (7, tempTicket.getTtime());
	      	preparedStmt.setInt(8, tempTicket.getTdur());			
			preparedStmt.execute();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		// get ticket id from database or not
		
		query = "SELECT id FROM schulichairline.bookedtickets ORDER BY id DESC LIMIT 1";
		try {
			preparedStmt = this.myConn.prepareStatement(query);
			myRs = preparedStmt.executeQuery(query);
			while(myRs.next()){
				tempTicket.setTicketID(myRs.getInt("id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			}
			
		System.out.println("A Ticket was added to the database.");
		System.out.println(tempTicket.getTicketID());
		return tempTicket;
	}
	/**
	 * Allows Admin Client to cancel a booked ticket. Needs flight id and ticket id from client
	 */
	@Override
	public void cancelBookedTicket(int flightID, int ticketID) {
				// edit flight database first
		int seats = 0;
		query = "SELECT * FROM schulichairline.flights WHERE flightNum = " //Jonathan
				+ "'"+flightID+"'";
		//query = "SELECT * FROM finalproject.flights WHERE flightNum = " //Jacob
		//		+ "'"+flightID+"'";
		System.out.println(" The server is updating "+ flightID +" "+ticketID);
			try {
				//myStmt = this.myConn.prepareStatement(query);
				myRs = myStmt.executeQuery(query);
				while(myRs.next()){
				seats = myRs.getInt("remainingSeats");
				}
			} catch (SQLException e) {
				System.out.println("Error getting  seats");
				e.printStackTrace();
				}
			// increase seats available
			seats++;
			query ="UPDATE schulichairline.flights SET remainingSeats = ? WHERE flightNum = ?";//Jonathan
			//query ="UPDATE finalproject.flights SET remainingSeats = ? WHERE flightNum = ?";//Jacob
			try {
				preparedStmt = this.myConn.prepareStatement(query);
				preparedStmt.setInt(1, seats);
				preparedStmt.setInt(2, flightID);
				preparedStmt.executeUpdate();
				
			} catch (SQLException e) {
				System.out.println("Error Updating flights set");
				e.printStackTrace();
				}
			//Delete ticket in tickets database
			query ="DELETE FROM schulichairline.bookedtickets WHERE id = ? ";//Jonathan
			//query ="DELETE FROM finalproject.bookedtickets WHERE id = ? ";//Jacob
			//
			try {
				preparedStmt = this.myConn.prepareStatement(query);
				preparedStmt.setInt(1, ticketID);
	            preparedStmt.execute();
			} catch (SQLException e) {
				System.out.println("Error deleting from database.");
				e.printStackTrace();
			}
		    
		
	}
	/**
	 * Gathers all the booked tickets to be sent to admin client
	 */
	@Override
	public void allTickets() {
	//	manyTickets.removeAllElements();
		try { // no query needed since it is too simple
			myRs = myStmt.executeQuery("SELECT * FROM schulichairline.bookedtickets");//For Jonathan
			//myRs = myStmt.executeQuery("SELECT * FROM finalproject.bookedtickets");//For Jacob
			while(myRs.next()){
				// build a single ticket
				aTicket = new Ticket(myRs.getInt("id"), myRs.getString("firstName"), myRs.getString("lastName"), 
						myRs.getInt("flightID"), myRs.getString("dest"), myRs.getString("depart"),myRs.getString("time"), 
						myRs.getInt("duration"), myRs.getString("date"));
				//Add it to the ticket vector
				manyTickets.addElement(aTicket);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			objout.writeObject(manyTickets);
		} catch (IOException e) {
			System.out.println("Error sending data to client");
			e.printStackTrace();
		}
		
		
	}
	
	
}