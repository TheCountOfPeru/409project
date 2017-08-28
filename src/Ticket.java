
import java.io.Serializable;

class Ticket implements Serializable{
	/**
	 * serialVerionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The ticket ID number - same as the flight num
	 */
	private int TicketID;
	/**
	 * The price of a ticket
	 */
	private double Tprices; 
	/**
	 * The passenger of the ticket
	 * Only use the first name last name parts
	 */
	private Passenger customer;
	/**
	 * First name of the passenger
	 */
	private String firstName;
	/**
	 * Last name of the passenger
	 */
	private String lastName;
	/**
	 * Flight number of the ticket
	 */
	private int FlightNum;
	/**
	 * Destnation of the ticket
	 */
	private String Tdest;
	/**
	 * Source of the flight of the ticket
	 */
	private String Tsource;
	/**
	 * Departure time
	 */
	private String Ttime;
	/**
	 * Trip traveling tme
	 */
	private int Tdur;
	/**
	 * Date of the flight
	 */
	private String Tdate;
	/**
	 * Constructor for Ticket
	 * @param ID ID number
	 * @param price Price of ticket
	 */
	public Ticket(int ID, String firstName, String lastName, int FlightNum, 
			String Tdest, String Tsource, String Ttime, int Tdur, String tdate){
		this.TicketID = ID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.FlightNum = FlightNum;
		this.Tdest = Tdest;
		this.Tsource = Tsource;
		this.Ttime = Ttime;
		this.Tdur = Tdur;
		this.Tdate = tdate;
	}
	/**
	 * Getter for ticket id
	 * @return ticket id
	 */
	public int getTicketID(){
		return TicketID;
	}
	/**
	 * Setter for TicketID
	 * @param a new id
	 */
	public void setTicketID(int a){
		TicketID = a;
	}
	/**
	 * Getter for customer passenger
	 * @return the passenger
	 */
	public Passenger getCustomer(){
		return customer;
	}
	/**
	 * What will be printed to a ticket
	 */
	public String toString(){
		String print_this = null;
		print_this = "";
		return print_this;
		
	}
	/**
	 * Getter for flightID
	 * @return
	 */
	public int getFlightNum() {
		return FlightNum;
	}
	/**
	 * Setter for flightID
	 */
	public void setFlightNum(int flightNum) {
		FlightNum = flightNum;
	}
	/**
	 * Getter for destination
	 * @return
	 */
	public String getTdest() {
		return Tdest;
	}
	/**
	 * Setter for destination
	 * @param tdest
	 */
	public void setTdest(String tdest) {
		Tdest = tdest;
	}
	/**
	 * Getter for source
	 * @return
	 */
	public String getTsource() {
		return Tsource;
	}
	/**
	 * Setter for source
	 * @param tsource
	 */
	public void setTsource(String tsource) {
		Tsource = tsource;
	}
	/**
	 * Getter for time
	 * @return
	 */
	public String getTtime() {
		return Ttime;
	}
	/**
	 * Setter for time
	 * @param ttime
	 */
	public void setTtime(String ttime) {
		Ttime = ttime;
	}
	/**
	 * Getter for duration
	 * @return
	 */
	public int getTdur() {
		return Tdur;
	}
	/**
	 * Setter for duration
	 * @param tdur
	 */
	public void setTdur(int tdur) {
		Tdur = tdur;
	}
	/**
	 * Getter for date
	 * @return
	 */
	public String getTdate() {
		return Tdate;
	}
	/**
	 * Setter for date
	 * @param tdate
	 */
	public void setTdate(String tdate) {
		Tdate = tdate;
	}
	/**
	 * Getter for first name
	 * @return
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * Setter for first name
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * Getter for last name
	 * @return
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * Setter for last name
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * Getter for price
	 * @return
	 */
	public double getTprices() {
		return Tprices;
	}
	/**
	 * Setter for price
	 * @param tprices
	 */
	public void setTprices(double tprices) {
		Tprices = tprices;
	}
	
}