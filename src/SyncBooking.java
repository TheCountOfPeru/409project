/**
 * Interface for methods used for booking a trip
 * @author Jonathan
 *
 */
public interface SyncBooking{
	/**
	 * Books a trip on a flight. Should modify number of seats remaining. Needs to be synchronized.
	 * @param trip The flight
	 * @param customer The passenger flying
	 * @return a Ticket for the passenger
	 */
	public Ticket bookSeat(int FlightID, Passenger customer);
	/**
	 * Extracts all booked tickets from database for viewing
	 */
	public void allTickets();
	
}