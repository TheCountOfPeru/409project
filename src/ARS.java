import java.util.ArrayList;
/**
 * Interface for methods of interacting with a flight database
 * @author Jonathan
 *
 */
public interface ARS{
	/**
	 * Search a database by date. put results in a vector
	 * @param input
	 * @return
	 */
	public void searchDate(String input);
	/**
	 * Search a database by departure source. put results in a vector
	 * @param input
	 * @return
	 */
	public void searchDeparture (String input); 
	/**
	 * source a database by destination. put results in a vector
	 * @param input
	 * @return
	 */
	public void searchDestination (String input); 
	/**
	 * Cancel a booked ticket. Edit the database to reflect change
	 * @param id
	 */
	public void cancelBookedTicket(int Fid, int Tid); 
	/**
	 * Insert a flight into a database
	 * @param newFlight
	 */
	public void addFlight(Flight newFlight);
	/**
	 * Insert more than one flight into a database
	 */
	public void addManyFlights();
	
}