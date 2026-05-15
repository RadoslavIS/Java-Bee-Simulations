import java.time.LocalDateTime;
import java.util.Iterator;

public interface Observation {

	/**
	 * Exact time of the observation.
	 * @return the date and time of the observation
	 */
	LocalDateTime getTime();

	/**
	 * Some additional info about the observation.
	 * @return the comment about the observation
	 */
	String getComment();

	/**
	 * Whether the observation can be reached via iterators.
	 * @return validity of the observation
	 */
	boolean valid();

	/**
	 * Remove the observation from being seen from iterators.
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *     <li>isValid = false</li>
	 * </ul>
	 */
	void remove();

	/**
	 * All valid observation made strictly before this one.
	 * @return an iterator of all earlier observations
	 */
	Iterator<Observation> earlier();

	/**
	 * All valid observation made strictly after this one.
	 * @return an iterator of all later observations
	 */
	Iterator<Observation> later();
}