
import java.io.Serializable;

/**
 * This class is supposed to hold the information about a single flight
 * @author Jonathan Yee, Haoxian Zhang, Jacob Turnbull
 * @version 1.0
 * @since Mar 31, 2017
 *
 */
class Flight implements Serializable{
	/**
	 * The Flight ID number
	 */
	private int flightNum;
	/**
	 * Where the flight starts from
	 */
	private String source;
	/**
	 * Where the flight ends at
	 */
	private String dest;
	/**
	 * Date of the flight
	 */
	private String date;
	/**
	 * Time of day of the flight
	 */
	private String time;
	/**
	 * How long the flight takes
	 */
	private int duration;
	/**
	 * Total seats on the flight
	 */
	private int totalSeats;
	/**
	 * Total remaining seats on the flight
	 */
	private int remainingSeats;
	/**
	 * The price of a seat on the flight
	 */
	private double price;
	/**
	 * Constructor for the Flight class
	 * @param flightNum
	 * @param source
	 * @param dest
	 * @param date
	 * @param time
	 * @param duration
	 * @param totalSeats
	 * @param remainingSeats
	 * @param price
	 */
	public Flight(int flightNum, String source, String dest, String date, 
			String time, int duration, int totalSeats, int remainingSeats, double price){
		
		this.flightNum = flightNum;
		this.source = source;
		this.dest = dest;
		this.date = date;
		this.time = time;
		this.duration =duration;
		this.totalSeats = totalSeats;
		this.remainingSeats = remainingSeats;
		this.price = price;
	}
	/**
	 * Getter for flightNum
	 * @return flightNum
	 */
	public int getFlightNum() {
		return flightNum;
	}
	/**
	 * Setter for flightNum
	 * @param flightNum
	 */
	public void setFlightNum(int flightNum) {
		this.flightNum = flightNum;
	}
	/**
	 * Getter for source
	 * @return source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * Setter for source
	 * @param source
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * Getter for dest
	 * @return dest
	 */
	public String getDest() {
		return dest;
	}
	/**
	 * Setter for dest
	 * @param dest
	 */
	public void setDest(String dest) {
		this.dest = dest;
	}
	/**
	 * Getter for date
	 * @return date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * Setter for date
	 * @param date
	 */
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * Getter for time
	 * @return time
	 */
	public String getTime() {
		return time;
	}
	/**
	 * Setter for time  
	 * @param time
	 */
	public void setTime(String time) {
		this.time = time;
	}
	/**
	 * Getter for duration
	 * @return duration
	 */
	public int getDuration() {
		return duration;
	}
	/**
	 * Setter for duration
	 * @param duration
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
	/** 
	 * Getter for totalSeats
	 * @return totalSeats
	 */
	public int getTotalSeats() {
		return totalSeats;
	}
	/** 
	 * Setter for totalSeats
	 * @param totalSeats
	 */
	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}
	/**
	 * Getter for remainingSeats
	 * @return remainingSeats
	 */
	public int getRemainingSeats() {
		return remainingSeats;
	}
	/**
	 * Setter for remainingSeats
	 * @param remainingSeats
	 */
	public void setRemainingSeats(int remainingSeats) {
		this.remainingSeats = remainingSeats;
	}
	/**
	 * Getter for price
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}
	/**
	 * Setter for price
	 * @param price
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	
	
}