import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.sun.javafx.event.EventQueue;

/**
 * This is the class GUI for the Admin to view a list of booked tickets and cancel any booked tickets.
 * @author Jonathan Yee, Jacob Turnbull, Haoxian Zhang
 * @version 1.0
 * @since Mar 31, 2017
 */
class ClientA{
	/**
	 * Used for receiving serialized objects to the server
	 */
	private static ObjectInputStream input;
	/**
	 * Used for sending serialized objects to the server
	 */
	private static ObjectOutputStream output;
	/**
	 * Used for storing multiple tickets
	 */
	private static Vector<Ticket> myTkts;
	/**
	 * For sending messages to the server
	 */
	private static PrintWriter socketOut;
	/**
	 * The socket for the client
	 */
	private Socket palinSocket;
	private BufferedReader stdIn;
	private BufferedReader socketIn;
	private static DataOutputStream IntsToSever;
	/**
	 * The constructor for the clientA. Sets up the connection between sever and client
	 * @param serverName
	 * @param portNumber
	 */
	public ClientA(String serverName, int portNumber) {
		try {
			palinSocket = new Socket(serverName, portNumber);
			stdIn = new BufferedReader(new InputStreamReader(System.in));
			socketIn = new BufferedReader(new InputStreamReader(
					palinSocket.getInputStream()));
			socketOut = new PrintWriter((palinSocket.getOutputStream()), true);
		} catch (IOException e) {
			System.err.println(e.getStackTrace());
		}
	}
	/**
	 * Main window
	 */
static class AdminGUI {

	private DefaultListModel<String> listModel;   
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private static JFrame	MainFrm;
	private JList list;
	private AddFlightGUI myAddFlights;
	/**
	 * The constructor for the main menu of the Admin GUI
	 */
	public AdminGUI() {
		MainFrm = new JFrame();
		MainFrm.setTitle("Admin Page");
		MainFrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MainFrm.setBounds(100, 100, 1000, 600);
		MainFrm.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    		socketOut.println("A;QUIT");
		    		
		        }
		});
		myTkts = new Vector<Ticket>();
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		MainFrm.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(395, 40, 475, 211);
		contentPane.add(panel);
	
		JLabel label_1 = new JLabel("Flight Number");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setBounds(0, 0, 140, 46);
		panel.add(label_1);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(152, 2, 239, 46);
		panel.add(textField_1);
		
		JLabel label_2 = new JLabel("Passenger");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setBounds(0, 120, 140, 46);
		panel.add(label_2);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(152, 120, 239, 46);
		panel.add(textField_2);
		/**
		 * list that shows all Tickets in database
		 * 
		 */
		listModel = new DefaultListModel<String>();
		list = new JList<String>(listModel);
		list.setBounds(12, 40, 371, 506);
		contentPane.add(list);
		/**
		 * when user clicked on list it populates txt fields
		 */
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				//index number
				int i = list.getSelectedIndex();
				//populate the Txt fields
				if(i>-1){
				textField_1 .setText(Integer.toString(myTkts.get(i).getFlightNum()));
				textField_2.setText(myTkts.get(i).getLastName());
				}
			}
			
		});
		//Get all tickets from data base
		socketOut.println("A;allTickets");
		try {
			//read the object send in
			myTkts =(Vector<Ticket>) input.readObject();
		//for every Ticket add to the listModel
		for(int i =0;i< myTkts.size();i++){                                          
			listModel.addElement("Flight Number: "+myTkts.get(i).getFlightNum()+"Ticket ID"+ myTkts.get(i).getTicketID());
		}    
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/**
		 * Button to delete selected 
		 */
		JButton button = new JButton("Delete");
		button.setBounds(395, 264, 165, 118);
		/**
		 * This will delete the selected Ticket from database and update GUI
		 */
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//If nothing is selected
				if(list.getSelectedValue().toString().isEmpty())
					return;
				//send command to sever
				String toSever="A;cancelBookedTicket";
				//get index
				int i = list.getSelectedIndex();
				//get TicketID and FlightID
				int TicketID = (int) myTkts.get(i).getTicketID();
				int FlightID = (int) myTkts.get(i).getFlightNum();
				System.out.println(TicketID+" "+FlightID);
				//send message to sever
				socketOut.println(toSever+";"+FlightID+";"+TicketID); 
				// message to user
				JOptionPane.showMessageDialog(null,"The ticket has been deleted");
				//Update the list
				myTkts.remove(i);
				listModel.remove(i);
				
				
			}
		});
		contentPane.add(button);
		
		/**
		 * Open the AddFlights window
		 */
		JButton btnAddfights = new JButton("AddFights");
		btnAddfights.setBounds(705, 264, 165, 118);
		//When button is clicked
		btnAddfights.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//opening the otherGUI
				myAddFlights = new AddFlightGUI();
				MainFrm.setVisible(false);
			}
		});
		contentPane.add(btnAddfights);
		
		JLabel lblTickets = new JLabel("Tickets");
		lblTickets.setBounds(12, 11, 56, 16);
		contentPane.add(lblTickets);

		MainFrm.setVisible(true);
		// populate the Ticket list
		
                                                                 
}

	/**
	 * 
	 * @author Jacob
	 * This is GUI for add flights to data base
	 */
 static class AddFlightGUI{

	 	private JFrame AddFlights;
		private JPanel contentPane;
		private JTextField TFFlightID;
		private JLabel lbFlightSource;
		private JTextField TFFlightSource;
		private JLabel lbDest;
		private JTextField TFDest;
		private JLabel lbDate;
		private JTextField TFDate;
		private JLabel lbTime;
		private JTextField TFTime;
		private JLabel lbDuritation;
		private JTextField TFDuritation;
		private JLabel lbSeats;
		private JTextField TFNumSeats;
		private JLabel lbRemaningSeats;
		private JTextField TFRemSeats;
		private JLabel lbPrice;
		private JTextField TFPrice;
		private JTextField TFfilepath;
		private  InsertButtonListener linstener;
		
		private String result;

		/**
		 * Launch the application.
		 */
		
		public AddFlightGUI() {
			initialize();
		}

		/**
		 * Create the frame.
		 */
		public void initialize() {
			AddFlights = new JFrame();
			AddFlights.setTitle("Adding Flights");
			AddFlights.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			AddFlights.setBounds(100, 100, 900, 500);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			AddFlights.setContentPane(contentPane);
			contentPane.setLayout(null);
			
			JLabel lbflightID = new JLabel("Flight ID");
			lbflightID.setHorizontalAlignment(SwingConstants.CENTER);
			lbflightID.setBounds(12, 13, 56, 16);
			contentPane.add(lbflightID);
			
			TFFlightID = new JTextField();
			TFFlightID.setBounds(110, 10, 301, 22);
			contentPane.add(TFFlightID);
			TFFlightID.setColumns(10);
			
			lbFlightSource = new JLabel("Source");
			lbFlightSource.setHorizontalAlignment(SwingConstants.CENTER);
			lbFlightSource.setBounds(12, 65, 79, 45);
			contentPane.add(lbFlightSource);
			
			TFFlightSource = new JTextField();
			TFFlightSource.setColumns(10);
			TFFlightSource.setBounds(110, 62, 301, 48);
			contentPane.add(TFFlightSource);
			
			lbDest = new JLabel("Destination");
			lbDest.setHorizontalAlignment(SwingConstants.CENTER);
			lbDest.setBounds(12, 126, 79, 45);
			contentPane.add(lbDest);
			
			TFDest = new JTextField();
			TFDest.setColumns(10);
			TFDest.setBounds(110, 123, 301, 48);
			contentPane.add(TFDest);
			
			lbDate = new JLabel("Date");
			lbDate.setHorizontalAlignment(SwingConstants.CENTER);
			lbDate.setBounds(12, 187, 79, 45);
			contentPane.add(lbDate);
			
			TFDate = new JTextField();
			TFDate.setColumns(10);
			TFDate.setBounds(110, 184, 301, 48);
			contentPane.add(TFDate);
			
			lbTime = new JLabel("Time");
			lbTime.setHorizontalAlignment(SwingConstants.CENTER);
			lbTime.setBounds(12, 248, 79, 45);
			contentPane.add(lbTime);
			
			TFTime = new JTextField();
			TFTime.setColumns(10);
			TFTime.setBounds(110, 245, 301, 48);
			contentPane.add(TFTime);
			
			lbDuritation = new JLabel("Duritation");
			lbDuritation.setHorizontalAlignment(SwingConstants.CENTER);
			lbDuritation.setBounds(422, 65, 79, 45);
			contentPane.add(lbDuritation);
			
			TFDuritation = new JTextField();
			TFDuritation.setColumns(10);
			TFDuritation.setBounds(520, 62, 301, 48);
			contentPane.add(TFDuritation);
			
			lbSeats = new JLabel("Number of seats");
			lbSeats.setHorizontalAlignment(SwingConstants.CENTER);
			lbSeats.setBounds(410, 126, 106, 45);
			contentPane.add(lbSeats);
			
			TFNumSeats = new JTextField();
			TFNumSeats.setColumns(10);
			TFNumSeats.setBounds(521, 123, 301, 48);
			contentPane.add(TFNumSeats);
			
			lbRemaningSeats = new JLabel("Remaning seats");
			lbRemaningSeats.setHorizontalAlignment(SwingConstants.CENTER);
			lbRemaningSeats.setBounds(410, 187, 106, 45);
			contentPane.add(lbRemaningSeats);
			
			TFRemSeats = new JTextField();
			TFRemSeats.setColumns(10);
			TFRemSeats.setBounds(520, 184, 301, 48);
			contentPane.add(TFRemSeats);
			
			lbPrice = new JLabel("Price");
			lbPrice.setHorizontalAlignment(SwingConstants.CENTER);
			lbPrice.setBounds(422, 248, 79, 45);
			contentPane.add(lbPrice);
			
			TFPrice = new JTextField();
			TFPrice.setColumns(10);
			TFPrice.setBounds(520, 245, 301, 48);
			contentPane.add(TFPrice);
			
			JButton btnNewButton = new JButton("INSERT");
			btnNewButton.setBounds(12, 359, 186, 81);
			contentPane.add(btnNewButton);
			linstener = new InsertButtonListener (this);
			//link to Insert Button Listner
			btnNewButton.addActionListener(linstener);
			
			JLabel lblNewLabel = new JLabel("");
			lblNewLabel.setBounds(310, 13, 206, 16);
			contentPane.add(lblNewLabel);
			
			JLabel lblfiles = new JLabel("If adding fights through file enter the file path here");
			lblfiles.setBounds(210, 359, 513, 38);
			contentPane.add(lblfiles);
			
			TFfilepath = new JTextField();
			TFfilepath.setBounds(210, 388, 611, 52);
			contentPane.add(TFfilepath);
			TFfilepath.setColumns(10);
			AddFlights.setVisible(true);
			
		}
		 class InsertButtonListener implements ActionListener{
			private AddFlightGUI frame;

			// constructor
			public  InsertButtonListener(AddFlightGUI jf) {
				frame = jf;
			}

			/**
			 * checks to see if adding flights through a txt file or just one
			 * Then sends the files
			 * 
			 */
			public void actionPerformed(ActionEvent e) {
				
				//Check to see if adding one or many flights
				if(TFfilepath.getText().isEmpty()){
					String toSever ="A;addFlight";
					socketOut.println(toSever);
					//create tmp flight to be sent out
					Flight toBeAdded = new Flight(Integer.parseInt(TFFlightID.getText()),TFFlightSource.getText(),
							TFDest.getText(),TFDate.getText(), TFTime.getText(),Integer.parseInt(TFDuritation.getText()),
							Integer.parseInt(TFNumSeats.getText()),Integer.parseInt(TFRemSeats.getText()),Double.parseDouble(TFPrice.getText()));
					try {
						//send to the sever
						output.writeObject(toBeAdded);
					} catch (IOException e1) {
						System.out.println("failed to add one Flight");
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(null,"Your flight has been added");
					MainFrm.setVisible(true);
					AddFlights.dispose();
					return;
				}
				//if it is not empty
				//call function
				String toSever="A;addManyFlights";
				socketOut.println(toSever);
				
				try {
					//try to output object to sever
					output.writeObject(createManyFlights(TFfilepath.getText()));
				} catch (IOException e1) {
					System.out.println("failed to add many Flights");
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null,"Your flights have been added");
				//Change frames
				MainFrm.setVisible(true);
				AddFlights.dispose();
			}
			/**
			 * 
			 * @param inputfilepath
			 * @return Vector<Flight>
			 * createsManyFlights from a txt file
			 */
			private Vector<Flight> createManyFlights(String inputfilepath) {
				//Vector of flights to be sent to sever
				Vector<Flight> toBeSent = new Vector();
					 File file = new File(inputfilepath);
			         Scanner in;
					try {
						in = new Scanner(file);
						//while their is still a flight in the txt file
						while(in.hasNextLine()){
							//line has the line of txt file
							String line = in.nextLine();
							//txt file must split information by ;
							String[] information = line.split(";");
							//making a new Flight
							Flight tmp=new Flight(Integer.parseInt(information[0]),information[1],information[2],
									information[3],information[4],Integer.parseInt(information[5]),
									Integer.parseInt(information[6]),Integer.parseInt(information[7]),Double.parseDouble(information[8]));
							//adding to the vector
							toBeSent.add(tmp);
						}
					}catch(Exception e){
						System.out.println("e");
					}
				return toBeSent;
			}
		}
	}


}
 public static void main (String[] args) throws IOException{
	//connect to sever
	ClientA aClient = new ClientA("Localhost", 9898);
	//initializing Object in and out
	aClient.output = new ObjectOutputStream(aClient.palinSocket.getOutputStream());
	aClient.input = new ObjectInputStream(aClient.palinSocket.getInputStream());
	aClient.IntsToSever = new DataOutputStream(aClient.palinSocket.getOutputStream());
	//Open Main Window
	AdminGUI myFrame = new AdminGUI();
	
}

}
 	