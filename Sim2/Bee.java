import java.time.LocalDateTime;
import java.util.Iterator;

public interface Bee extends Pollinator, Wasp {


	/**
	 * All valid observations of the same individual bee ordered from earliest to latest
	 * @return iterator of all observations, corresponding to the same individual bee, ordered chronologically
	 */
	default Iterator<Bee> sameBee() {
		return sameBee(true);
	}

	/**
	 * All valid observations of the same individual bee ordered in a specified direction
	 * @param direction chronological direction of the iterator, true = earlier first, false = later first
	 * @return iterator of all observations, corresponding to the same individual bee, with a specified direction
	 */
	default Iterator<Bee> sameBee(boolean direction) {
		return sameBee(direction, LocalDateTime.MIN, LocalDateTime.MAX);
	}

	/**
	 * All valid observations of the same individual bee ordered from earliest to latest and only inside a specified time interval
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *     <li>startTime != null</li>
	 *     <li>endTime != null</li>
	 * </ul>
	 * @param startTime lower bound of the time interval
	 * @param endTime upper bound of the time interval
	 * @return iterator of all observations, corresponding to the same individual bee, ordered chronologically with a specified time interval
	 */
	default Iterator<Bee> sameBee(LocalDateTime startTime, LocalDateTime endTime) {
		return sameBee(true, startTime, endTime);
	}

	/**
	 * All valid observations of the same individual bee ordered in a specified direction and only inside a specified time interval
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *     <li>startTime != null</li>
	 *     <li>endTime != null</li>
	 * </ul>
	 * @param direction chronological direction of the iterator, true = earlier first, false = later first
	 * @param startTime lower bound of the time interval
	 * @param endTime upper bound of the time interval
	 * @return iterator of all observations, corresponding to the same individual bee, with a specified direction and time interval
	 */
	Iterator<Bee> sameBee(boolean direction, LocalDateTime startTime, LocalDateTime endTime);

}