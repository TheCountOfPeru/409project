import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * This is the class for the GUI for a passenger to search flights and
 * book tickets, then print out the ticket to a file
 * @author Jonathan Yee, Haoxian Zhang, Jacob Turnbull
 * @version 1.0
 * @since Mar 31, 2017 
 */
class ClientP{
	/**
	 * Used for connecting to the server
	 */
	private Socket socket;
	/**
	 * Use for receiving serialized objects to the server
	 */
	static private ObjectInputStream inputO;
	/**
	 * Use for sending serialized objects to the server
	 */
	static ObjectOutputStream output;
	/**
	 * Used for sending commands to the server
	 */
	static private PrintWriter toSocket;
	/**
	 * Used to save Flight information. Should be serialized then sent to the server.
	 */
	private Flight myFlight;
	/**
	 * Used to save mulitple flight data
	 */
	private static Vector<Flight> manyFlights;
	/**
	 * Used to save Ticket information. Server should send ticket to client and client must print it.
	 */
	private Ticket myTkt;
	/**
	 * Print the ticket to a text file
	 * @wbp.parser.entryPoint
	 */
	void outputTicket(Ticket ticket){
		
	}
	/**
	 * Get the object from the server and deserialize it
	 * @return the object
	 * @wbp.parser.entryPoint
	 */
	ObjectInputStream deserialization(){
		return null;
	}
	/**
	 * Communicate with the server
	 * @wbp.parser.entryPoint
	 */
	public void communicate(){
		
	}
	/**
	 * Constructs ClientP
	 * @wbp.parser.entryPoint
	 */
	public ClientP(String serverName, int portNum){
		try {
			socket = new Socket(serverName,portNum);
			output = new ObjectOutputStream(socket.getOutputStream());
			inputO = new ObjectInputStream(socket.getInputStream());
			toSocket = new PrintWriter(socket.getOutputStream(),true);
			System.out.println("Connected to the server!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Create the Main Window.
	 */
	static class MainWindow {
		private DefaultListModel<String> listModel;
		private JList<String> list;
		private JFrame frmFlightBookingSystem;
		private JTextField TxtSearchKey;
		private JComboBox<Object> searchType;
		private boolean change = false;
		
		public MainWindow(){
			frmFlightBookingSystem = new JFrame();
			frmFlightBookingSystem.setTitle("Flight Booking System for Client");
			frmFlightBookingSystem.setBounds(100, 100, 466, 588);
			frmFlightBookingSystem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frmFlightBookingSystem.getContentPane().setLayout(null);
			frmFlightBookingSystem.addWindowListener(new java.awt.event.WindowAdapter() {
			    @Override
			    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
			    		toSocket.println("A;QUIT");
			    		System.exit(0);
			    		
			        }
			});
			JLabel lblWelcomeStartTo = new JLabel("Welcome! Start to search a flight");
			lblWelcomeStartTo.setBounds(42, 30, 248, 16);
			frmFlightBookingSystem.getContentPane().add(lblWelcomeStartTo);
			
			searchType = new JComboBox<Object>();
		    searchType.setModel(new DefaultComboBoxModel<Object>(new String[] {"", "Date of Departure", "Departure City", "Destination"}));
			searchType.setBounds(22, 116, 174, 27);
			frmFlightBookingSystem.getContentPane().add(searchType);
			
			JLabel lblSearchKey = new JLabel("Search Key");
			lblSearchKey.setBounds(273, 88, 76, 16);
			frmFlightBookingSystem.getContentPane().add(lblSearchKey);
			
			TxtSearchKey = new JTextField();
			TxtSearchKey.setBounds(233, 115, 163, 26);
			frmFlightBookingSystem.getContentPane().add(TxtSearchKey);
			TxtSearchKey.setColumns(10);
			
			// Search flight
			JButton btnDateSearch = new JButton("Search");
			btnDateSearch.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Search based on the date
					if(searchType.getSelectedItem().equals("Date of Departure")){
						if(TxtSearchKey.getText().equals("") || !TxtSearchKey.getText().matches("\\d{4}-\\d{2}-\\d{2}")){
							JOptionPane.showMessageDialog(null,"Please provide a format of YYYY-MM-DD for the date!");
							return;}
						toSocket.println("P;searchDate;"+TxtSearchKey.getText());
						try {
							manyFlights = (Vector<Flight>) inputO.readObject();
							if(manyFlights.size() == 0){
								JOptionPane.showMessageDialog(null,"No match records");
								return;
							}
							listModel.clear();
							for(Flight i: manyFlights){
								listModel.addElement("Flight Number: "+i.getFlightNum()+
										"; Departure City: "+i.getSource()+"; Destination: "
										+i.getDest()+"; Schedule: "+i.getDate()+" "+i.getTime());
							}
							
						} catch (ClassNotFoundException | IOException e1) {
							e1.printStackTrace();
						}
					}
					else if(searchType.getSelectedItem().equals("Departure City")){
						if(TxtSearchKey.getText().equals("")){
							JOptionPane.showMessageDialog(null,"Please provide the departure city!");
							return;}
						toSocket.println("P;searchDeparture;"+TxtSearchKey.getText());
						try {
							manyFlights = (Vector<Flight>) inputO.readObject();
							if(manyFlights.size() == 0){
								JOptionPane.showMessageDialog(null,"No match records");
								return;
							}
							listModel.clear();
							for(Flight i: manyFlights){
								listModel.addElement("Flight Number: "+i.getFlightNum()+
										"; Departure City: "+i.getSource()+"; Destination: "
										+i.getDest()+"; Schedule: "+i.getDate()+" "+i.getTime());
							}
							
						} catch (ClassNotFoundException | IOException e1) {
							e1.printStackTrace();
						}
					}
					else if(searchType.getSelectedItem().equals("Destination")){
						if(TxtSearchKey.getText().equals("")){
							JOptionPane.showMessageDialog(null,"Please provide the destination city!");
							return;}
						toSocket.println("P;searchDestination;"+TxtSearchKey.getText());
						try {
							manyFlights = (Vector<Flight>) inputO.readObject();
							if(manyFlights.size() == 0){
								JOptionPane.showMessageDialog(null,"No match records");
								return;
							}
							listModel.clear();
							for(Flight i: manyFlights){
								listModel.addElement("Flight Number: "+i.getFlightNum()+
										"; Departure City: "+i.getSource()+"; Destination: "
										+i.getDest()+"; Schedule: "+i.getDate()+" "+i.getTime());
							}
							
						} catch (ClassNotFoundException | IOException e1) {
							e1.printStackTrace();
						}
					}
					else{
						JOptionPane.showMessageDialog(null,"Please select a search type!");
						return;
					}
					
				}
			});
			btnDateSearch.setBounds(146, 178, 117, 29);
			frmFlightBookingSystem.getContentPane().add(btnDateSearch);
			
			JLabel lblSearchResult = new JLabel("Select a flight to see more information");
			lblSearchResult.setBounds(22, 230, 268, 16);
			frmFlightBookingSystem.getContentPane().add(lblSearchResult);
			
			// Clear search
			JButton btnClear = new JButton("Clear All");
			btnClear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int size = listModel.size();
					change = true;
					for(int i = 0; i < size; i++){
						listModel.remove(0);
					}
					change = false;
					//list.setModel(listModel);
					
					TxtSearchKey.setText("");
					searchType.setSelectedIndex(0);
					
				}
			});
			
			btnClear.setBounds(279, 178, 117, 29);
			frmFlightBookingSystem.getContentPane().add(btnClear);
			
			
			// ScrollPane for displaying results
			listModel = new DefaultListModel<String>();
			list = new JList<String>(listModel);
			list.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent arg) {
					
					int index = listModel.indexOf(list.getSelectedValue());
					if(index > -1){
					Flight selected = manyFlights.get(index);
					
					if (!arg.getValueIsAdjusting() && selected != null) {
						
						System.out.println("Selected "+selected.getFlightNum());
						// Call Flight Info Window
						if(!change){
						new FlightInfoWindow(selected);
						}
		            }
					}
				} 
				
			});
			JScrollPane scrollPane = new JScrollPane(list);
			scrollPane.setBounds(6, 253, 454, 307);
			frmFlightBookingSystem.getContentPane().add(scrollPane);
			
			JLabel lblSearchType = new JLabel("Search Type");
			lblSearchType.setBounds(79, 88, 89, 16);
			frmFlightBookingSystem.getContentPane().add(lblSearchType);
			frmFlightBookingSystem.setVisible(true);	
			
	}
	/**
	 * Flight Info Window
	 */
	static class FlightInfoWindow{
		private JFrame frmFlightInformation;
		private JTextField TxtFlightNo;
		private JTextField TxtSource;
		private JTextField TxtDest;
		private JTextField TxtDate;
		private JTextField TxtTime;
		private JTextField TxtRemaining;
		private JTextField TxtTotalSeats;
		private JTextField TxtPrice;
		private JButton btnBook;
		private JButton btnCancel;
		
		public FlightInfoWindow(Flight input){
			
			
			frmFlightInformation = new JFrame();
			frmFlightInformation.setTitle("Flight Information");
			frmFlightInformation.setBounds(100, 100, 353, 417);
			frmFlightInformation.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frmFlightInformation.getContentPane().setLayout(null);
			
			JLabel lblFlightNo = new JLabel("Flight No.");
			lblFlightNo.setBounds(30, 30, 118, 16);
			frmFlightInformation.getContentPane().add(lblFlightNo);
			
			TxtFlightNo = new JTextField();
			TxtFlightNo.setEditable(false);
			TxtFlightNo.setBounds(189, 25, 130, 26);
			frmFlightInformation.getContentPane().add(TxtFlightNo);
			TxtFlightNo.setColumns(10);
			
			JLabel lblDepartureCity = new JLabel("Departure City");
			lblDepartureCity.setBounds(30, 69, 100, 16);
			frmFlightInformation.getContentPane().add(lblDepartureCity);
			
			TxtSource = new JTextField();
			TxtSource.setEditable(false);
			TxtSource.setBounds(189, 64, 130, 26);
			frmFlightInformation.getContentPane().add(TxtSource);
			TxtSource.setColumns(10);
			
			JLabel lblDestination = new JLabel("Destination");
			lblDestination.setBounds(30, 112, 82, 16);
			frmFlightInformation.getContentPane().add(lblDestination);
			
			TxtDest = new JTextField();
			TxtDest.setEditable(false);
			TxtDest.setBounds(189, 107, 130, 26);
			frmFlightInformation.getContentPane().add(TxtDest);
			TxtDest.setColumns(10);
			
			JLabel lblDepartureDate = new JLabel("Departure Date");
			lblDepartureDate.setBounds(30, 150, 118, 16);
			frmFlightInformation.getContentPane().add(lblDepartureDate);
			
			TxtDate = new JTextField();
			TxtDate.setEditable(false);
			TxtDate.setBounds(189, 145, 130, 26);
			frmFlightInformation.getContentPane().add(TxtDate);
			TxtDate.setColumns(10);
			
			JLabel lblDepatureTime = new JLabel("Depature Time");
			lblDepatureTime.setBounds(30, 188, 103, 16);
			frmFlightInformation.getContentPane().add(lblDepatureTime);
			
			TxtTime = new JTextField();
			TxtTime.setEditable(false);
			TxtTime.setBounds(189, 183, 130, 26);
			frmFlightInformation.getContentPane().add(TxtTime);
			TxtTime.setColumns(10);
			
			JLabel lblRemainingSeats = new JLabel("Available Seats");
			lblRemainingSeats.setBounds(30, 226, 118, 16);
			frmFlightInformation.getContentPane().add(lblRemainingSeats);
			
			TxtRemaining = new JTextField();
			TxtRemaining.setEditable(false);
			TxtRemaining.setBounds(189, 221, 130, 26);
			frmFlightInformation.getContentPane().add(TxtRemaining);
			TxtRemaining.setColumns(10);
			
			JLabel lblTotalSeats = new JLabel("Total Seats");
			lblTotalSeats.setBounds(30, 264, 100, 16);
			frmFlightInformation.getContentPane().add(lblTotalSeats);
			
			TxtTotalSeats = new JTextField();
			TxtTotalSeats.setEditable(false);
			TxtTotalSeats.setBounds(189, 259, 130, 26);
			frmFlightInformation.getContentPane().add(TxtTotalSeats);
			TxtTotalSeats.setColumns(10);
			
			JLabel lblPriceTax = new JLabel("Price (7% tax not included)");
			lblPriceTax.setBounds(30, 300, 166, 16);
			frmFlightInformation.getContentPane().add(lblPriceTax);
			
			TxtPrice = new JTextField();
			TxtPrice.setEditable(false);
			TxtPrice.setBounds(225, 297, 94, 26);
			frmFlightInformation.getContentPane().add(TxtPrice);
			TxtPrice.setColumns(10);
			
			// Book button for Flight Info Window
			btnBook = new JButton("Book");
			btnBook.setBounds(64, 345, 117, 29);
			frmFlightInformation.getContentPane().add(btnBook);
			btnBook.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// check availability first
					if(input.getRemainingSeats() <= 0){
						JOptionPane.showMessageDialog(null, 
								"Sorry, the selected flight is full now!");
						return;
					}
					// open the passenger info window to get passenger info
					new PassengerInfoWindow(input);
					//close the menu window
					frmFlightInformation.dispose();
				}
			});
			
			// Cancel button for Flight Info Window
			btnCancel = new JButton("Cancel");
			btnCancel.setBounds(193, 345, 117, 29);
			frmFlightInformation.getContentPane().add(btnCancel);
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frmFlightInformation.dispose();
				}
			});
			
			initializeTextFields(input);
			frmFlightInformation.setVisible(true);
		}
		
		public void initializeTextFields(Flight input){
			this.TxtFlightNo.setText(Integer.toString(input.getFlightNum()));
			this.TxtSource.setText(input.getSource());
			this.TxtDest.setText(input.getDest());
			this.TxtDate.setText(input.getDate());
			this.TxtTime.setText(input.getTime());
			this.TxtRemaining.setText(Integer.toString(input.getRemainingSeats()));
			this.TxtTotalSeats.setText(Integer.toString(input.getTotalSeats()));			
			this.TxtPrice.setText(Double.toString(input.getPrice()));
		}
		
	}
	static class PassengerInfoWindow{
		private JFrame PassengerInfoWindow;
		private JTextField TxtfirstName;
		private JTextField TxtlastName;
		private JTextField TxtDOB;
		private Passenger customer;
		private Ticket aTicket;
				
		public PassengerInfoWindow(Flight input){
			PassengerInfoWindow = new JFrame();
			PassengerInfoWindow.setTitle("Passenger Information");
			PassengerInfoWindow.setBounds(100, 100, 450, 323);
			PassengerInfoWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			PassengerInfoWindow.getContentPane().setLayout(null);
			
			JLabel lblPleaseEnterInfo = new JLabel("Please enter the passenger information");
			lblPleaseEnterInfo.setBounds(38, 16, 308, 16);
			PassengerInfoWindow.getContentPane().add(lblPleaseEnterInfo);
			
			JLabel lblFirstName = new JLabel("First Name");
			lblFirstName.setBounds(53, 67, 91, 16);
			PassengerInfoWindow.getContentPane().add(lblFirstName);
			
			TxtfirstName = new JTextField();
			TxtfirstName.setBounds(193, 62, 130, 26);
			PassengerInfoWindow.getContentPane().add(TxtfirstName);
			TxtfirstName.setColumns(10);
			
			JLabel lblLastName = new JLabel("Last Name");
			lblLastName.setBounds(53, 112, 80, 16);
			PassengerInfoWindow.getContentPane().add(lblLastName);
			
			TxtlastName = new JTextField();
			TxtlastName.setBounds(193, 107, 130, 26);
			PassengerInfoWindow.getContentPane().add(TxtlastName);
			TxtlastName.setColumns(10);
			
			JLabel lblDateOfBirth = new JLabel("Date of Birth");
			lblDateOfBirth.setBounds(53, 163, 85, 16);
			PassengerInfoWindow.getContentPane().add(lblDateOfBirth);
			
			TxtDOB = new JTextField();
			TxtDOB.setBounds(193, 158, 130, 26);
			PassengerInfoWindow.getContentPane().add(TxtDOB);
			TxtDOB.setColumns(10);
			
			JLabel lblYyyymmdd = new JLabel("YYYY-MM-DD");
			lblYyyymmdd.setBounds(337, 163, 90, 16);
			PassengerInfoWindow.getContentPane().add(lblYyyymmdd);
			
			JButton btnOk = new JButton("Ok");
			btnOk.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("OK pressed");
					String[] strArray = TxtDOB.getText().split("-");
					if(!TxtDOB.getText().matches("\\d{4}-\\d{2}-\\d{2}")){
						JOptionPane.showMessageDialog(PassengerInfoWindow,
							    "Format for date of birth is incorrect. Needs to be YYYY-MM-DD",
							    "Error",
							    JOptionPane.WARNING_MESSAGE);
						PassengerInfoWindow.dispose();
					}
					//need to send data to server to build a ticket
					// get the date of birth. split it as a string array then populate a int array
					
					int[] dob = new int[3];
					for(int i = 0; i < 3; i++) {
					    dob[i] = Integer.parseInt(strArray[i]);
					    }
					// construct a temporary passenger
					try {
						customer = new Passenger(TxtfirstName.getText(), TxtlastName.getText(), dob);
					// send command to server
					toSocket.println("P;bookSeat;"+input.getFlightNum());
					// send passenger object
					output.writeObject(customer);
					} catch (IOException e2w) {
						e2w.printStackTrace();
					}
					try {
						
						// recieve the ticket object
						aTicket = (Ticket) inputO.readObject();
						// check if ticket is null. Ticket is null when no seats are available
						if(aTicket == null){
							JOptionPane.showMessageDialog(
								    btnOk, "Sorry, no more seats available.",
								    "Booking Error",
								    JOptionPane.WARNING_MESSAGE);
							//close the window
							PassengerInfoWindow.dispose();
							return;
							
						}
					} catch (ClassNotFoundException e1) {
						System.out.println("Error receiving customer data to server.");
						e1.printStackTrace();
					} catch (IOException e1) {
						System.out.println("Error receiving customer data to server.");
						e1.printStackTrace();
					}
					// pass ticket to ticketwindow
					new TicketWindow(aTicket, customer, input);
					//close the window
					PassengerInfoWindow.dispose();
					
				}
			});
			btnOk.setBounds(100, 221, 117, 29);
			PassengerInfoWindow.getContentPane().add(btnOk);
			
			JButton btnCancel = new JButton("Cancel");
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					PassengerInfoWindow.dispose();
				}
			});
			btnCancel.setBounds(256, 221, 117, 29);
			// Showing the flight that is booking
			JLabel lblBookingFligth = new JLabel("(Booking: "+input.getFlightNum()+" "+
			input.getSource()+" to "+input.getDest()+")");
			lblBookingFligth.setBounds(40, 262, 350, 16);
			PassengerInfoWindow.getContentPane().add(lblBookingFligth);
			
			PassengerInfoWindow.getContentPane().add(btnCancel);
			PassengerInfoWindow.setVisible(true);
		}
	}
	
	static class TicketWindow{
		private JFrame TicketWindow;
		private JTextField TxtFlightNum;
		private JTextField TxtSource;
		private JTextField TxtDest;
		private JTextField TxtDate;
		private JTextField TxtTime;
		private JTextField TxtDuration;
		private JTextField TxtFirstName;
		private JTextField TxtLastName;
		private JTextField TxtDOB;
		private JTextField TxtTicketID;
		private JTextField TxtTotalPrice;
		
		
		public TicketWindow(Ticket aTicket, Passenger customer, Flight theflight){
			
			TicketWindow = new JFrame();
			TicketWindow.setTitle("Ticket");
			TicketWindow.setBounds(100, 100, 450, 680);
			TicketWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			TicketWindow.getContentPane().setLayout(null);
			
			JLabel lblYourTicketHas = new JLabel("Your ticket has been successfully booked!");
			lblYourTicketHas.setBounds(97, 6, 285, 16);
			TicketWindow.getContentPane().add(lblYourTicketHas);
			
			JLabel lblFlightNo = new JLabel("Flight No.");
			lblFlightNo.setBounds(40, 101, 101, 16);
			TicketWindow.getContentPane().add(lblFlightNo);
			
			TxtFlightNum = new JTextField();
			TxtFlightNum.setBounds(228, 96, 130, 26);
			TxtFlightNum.setEditable(false);
			TicketWindow.getContentPane().add(TxtFlightNum);
			TxtFlightNum.setColumns(10);
			
			TxtSource = new JTextField();
			TxtSource.setBounds(228, 140, 130, 26);
			TxtSource.setEditable(false);
			TicketWindow.getContentPane().add(TxtSource);
			TxtSource.setColumns(10);
			
			JLabel lblDepartureCity = new JLabel("Departure City");
			lblDepartureCity.setBounds(40, 145, 101, 16);
			TicketWindow.getContentPane().add(lblDepartureCity);
			
			JLabel lblDestination = new JLabel("Destination");
			lblDestination.setBounds(40, 188, 101, 16);
			TicketWindow.getContentPane().add(lblDestination);
			
			TxtDest = new JTextField();
			TxtDest.setBounds(228, 183, 130, 26);
			TxtDest.setEditable(false);
			TicketWindow.getContentPane().add(TxtDest);
			TxtDest.setColumns(10);
			
			JLabel lblDate = new JLabel("Date of Departure");
			lblDate.setBounds(40, 232, 130, 16);
			TicketWindow.getContentPane().add(lblDate);
			
			TxtDate = new JTextField();
			TxtDate.setBounds(228, 227, 130, 26);
			TxtDate.setEditable(false);
			TicketWindow.getContentPane().add(TxtDate);
			TxtDate.setColumns(10);
			
			JLabel lblTime = new JLabel("Time");
			lblTime.setBounds(40, 273, 61, 16);
			TicketWindow.getContentPane().add(lblTime);
			
			TxtTime = new JTextField();
			TxtTime.setEditable(false);
			TxtTime.setBounds(228, 268, 130, 26);
			TicketWindow.getContentPane().add(TxtTime);
			TxtTime.setColumns(10);
			
			JLabel lblDuration = new JLabel("Duration (minutes)");
			lblDuration.setBounds(40, 313, 120, 16);
			TicketWindow.getContentPane().add(lblDuration);
			
			TxtDuration = new JTextField();
			TxtDuration.setBounds(228, 308, 130, 26);
			TxtDuration.setEditable(false);
			TicketWindow.getContentPane().add(TxtDuration);
			TxtDuration.setColumns(10);
			
			JLabel lblPassenger = new JLabel("Passenger Information:");
			lblPassenger.setBounds(6, 345, 182, 16);
			TicketWindow.getContentPane().add(lblPassenger);
			
			JLabel lblFirstName = new JLabel("First Name");
			lblFirstName.setBounds(40, 376, 87, 16);
			TicketWindow.getContentPane().add(lblFirstName);
			
			TxtFirstName = new JTextField();
			TxtFirstName.setBounds(228, 360, 130, 26);
			TxtFirstName.setEditable(false);
			TicketWindow.getContentPane().add(TxtFirstName);
			TxtFirstName.setColumns(10);
			
			JLabel lblLastName = new JLabel("Last Name");
			lblLastName.setBounds(40, 410, 73, 16);
			TicketWindow.getContentPane().add(lblLastName);
			
			TxtLastName = new JTextField();
			TxtLastName.setBounds(228, 398, 130, 26);
			TxtLastName.setEditable(false);
			TicketWindow.getContentPane().add(TxtLastName);
			TxtLastName.setColumns(10);
			
			JLabel lblDateOfBirth = new JLabel("Date of Birth");
			lblDateOfBirth.setBounds(40, 441, 87, 16);
			TicketWindow.getContentPane().add(lblDateOfBirth);
			
			TxtDOB = new JTextField();
			TxtDOB.setBounds(228, 436, 130, 26);
			TxtDOB.setEditable(false);
			TicketWindow.getContentPane().add(TxtDOB);
			TxtDOB.setColumns(10);
			
			JLabel lblFlightInformation = new JLabel("Flight Information:");
			lblFlightInformation.setBounds(6, 55, 130, 16);
			TicketWindow.getContentPane().add(lblFlightInformation);
			
			JLabel lblTicketInformation = new JLabel("Ticket Information:");
			lblTicketInformation.setBounds(6, 483, 145, 16);
			TicketWindow.getContentPane().add(lblTicketInformation);
			
			JLabel lblTicketNumber = new JLabel("Ticket Number");
			lblTicketNumber.setBounds(40, 526, 130, 16);
			TicketWindow.getContentPane().add(lblTicketNumber);
			
			TxtTicketID = new JTextField();
			TxtTicketID.setBounds(228, 521, 130, 26);
			TxtTicketID.setEditable(false);
			TicketWindow.getContentPane().add(TxtTicketID);
			TxtTicketID.setColumns(10);
			
			JLabel lblTotalPriceincluded = new JLabel("Total Price (7% tax included)");
			lblTotalPriceincluded.setBounds(40, 565, 182, 16);
			TicketWindow.getContentPane().add(lblTotalPriceincluded);
			
			TxtTotalPrice = new JTextField();
			TxtTotalPrice.setBounds(228, 559, 130, 26);
			TxtTotalPrice.setEditable(false);
			TicketWindow.getContentPane().add(TxtTotalPrice);
			TxtTotalPrice.setColumns(10);
			
			//Set text fields to ticket values
			TxtFlightNum.setText(Integer.toString(aTicket.getFlightNum()));
			TxtSource.setText(aTicket.getTsource());
			TxtDest.setText(aTicket.getTdest());
			TxtDate.setText(aTicket.getTdate());
			TxtTime.setText(aTicket.getTtime());
			TxtDuration.setText(Integer.toString(aTicket.getTdur()));
			TxtFirstName.setText(aTicket.getFirstName());
			TxtLastName.setText(aTicket.getLastName());
			TxtDOB.setText(customer.DOBtoString());
			TxtTicketID.setText(Integer.toString(aTicket.getTicketID()));
			TxtTotalPrice.setText(Double.toString(theflight.getPrice()));			
			
			JButton btnSave = new JButton("Print Ticket");
			btnSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {					
					System.out.println("Print Ticket");
					// print to file here
					try{
					    PrintWriter writer = new PrintWriter(aTicket.getFirstName()+aTicket.getFlightNum()+"-FLIGHT-TICKET.txt", "UTF-8");
					    writer.println("PASSENGER INFORMATION");
					    writer.println(("name: "+aTicket.getLastName()+", "+aTicket.getFirstName()).toUpperCase());
					    writer.println("DATE OF BIRTH: "+customer.DOBtoString());
					    writer.println("");
					    writer.println("TICKET INFORMATION");
					    writer.println("ID NUMBER: "+aTicket.getTicketID());
					    writer.println("SEAT PRICE: "+theflight.getPrice());
					    writer.println("");
					    writer.println("FLIGHT INFORMATION");
					    writer.println("CITY OF DEPARTURE: "+aTicket.getTsource().toUpperCase());
					    writer.println("CITY OF DESTINATION: "+aTicket.getTdest().toUpperCase());
					    writer.println("DATE OF TRAVEL: "+aTicket.getTdate());
					    writer.println("TIME OF DAY: "+aTicket.getTtime());
					    writer.println("DURATION OF FLIGHT (MINUTES): "+aTicket.getTdur());
					    writer.println("");
					    writer.println(".:THANK YOU FOR FLYING SCHULICH AIRLINES:.");   						    
					    writer.close();
					} catch (IOException e1) {
						System.out.println("Error printing the ticket.");
					   e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(TicketWindow,
						    "Your ticket has been saved to file!");
					// close the window
					TicketWindow.dispose();
					
				}
			});
			btnSave.setBounds(53, 609, 117, 29);
			TicketWindow.getContentPane().add(btnSave);
			
			
			
			TicketWindow.setVisible(true);
			}
		}
	}
	/**
	 * @wbp.parser.entryPoint
	 */
	public static void main (String[] args) throws IOException{
		// Connect to the server
		ClientP cp = new ClientP("localhost", 9898);
		// if this fails to connect it should not open the gui
		// Generate the Main Window
		MainWindow mainWindow = new MainWindow();
		
	}
}