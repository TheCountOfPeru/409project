import java.io.Serializable;

/**
 * This hold class holds information for a single passenger
 * @author Jonathan Yee, Haoxian Zhang, Jacob Turnbull
 * @version 1.0
 * @since Apr 1, 2017
 */
class Passenger implements Serializable {
	/**
	 * First name of the passenger
	 */
	private String firstName;
	/**
	 * Last name of the passenger
	 */
	private String lastName;
	/**
	 * Date of birth of the passenger. YYYY MM DD
	 */
	private int[] dob;
	/**
	 * Class constructor
	 * @param firstName
	 * @param lastName
	 * @param dob
	 */
	public Passenger(String firstName, String lastName, int[] dob){
		this.firstName = firstName;
		this.lastName = lastName;
		int[] temp = {dob[0], dob[1], dob[2]};
		this.dob = temp;
	}
	/**
	 * Getter for the firstname of the passenger
	 * @return
	 */
	public String getFname(){
		return firstName;
	}
	/**
	 * Getter for the last name of the passenger
	 * @return
	 */
	public String getLname(){
		return lastName;
	}
	/**
	 * Getter for the date of birth of the passenger
	 * @return
	 */
	public int[] getDOB(){
		return dob;
	}
	/**
	 * The toString method for date of birth
	 * @return
	 */
	public String DOBtoString(){
		String s = dob[0]+"-"+dob[1]+"-"+dob[2];
		return s;
	}
}