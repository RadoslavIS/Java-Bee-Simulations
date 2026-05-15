import java.time.LocalDateTime;

public class HoneyBee extends Bee implements Modifiable<String, HoneyBee> {

	private final String string;

	/**
	 * Creates a new honeybee observation with a given string.
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *     <li>time != null</li>
	 *     <li>description != null</li>
	 *     <li>marker >= 0</li>
	 *     <li>x != null</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *     <li><code>sort() == string</code></li>
	 *     <li>the created object is immutable</li>
	 * </ul>
	 *
	 * @param time        the time of the observation
	 * @param description short description about the observation
	 * @param marker      a numerical identifier
	 * @param string      String of type/breeding of an observed bee
	 */
	public HoneyBee(LocalDateTime time, String description, int marker, String string) {
		super(time, description, marker);
		this.string = string;
	}

	/**
	 * Returns <code>this</code> or a copy of <code>this</code> with an added String <code>s</code>, depending on
	 * if <code>s</code> is empty
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *     <li>s != null</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *     <li>if <code>s</code> is <b>not</b> empty, an exact copy of <code>this</code> will
	 *     be returned with <code>s</code> appended to the string</li>
	 *     <li>if <code>s</code> is empty, <code>this</code> will be returned</li>
	 * </ul>
	 *
	 * @param s String to be appended to the string of the returned copy of <code>this</code>
	 * @return object of type HoneyBee
	 */
	@Override
	public HoneyBee add(String s) {
		if (s.isEmpty()) {
			return this;
		}

		return new HoneyBee(super.getTime(), super.getDescription(), super.getMarker(), string.concat(s));
	}

	/**
	 * Returns <code>this</code> or a copy of <code>this</code> with a removed String <code>s</code>, depending on
	 * if <code>s</code> is present in the string of <code>this</code>
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *     <li>s != null</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *     <li>if <code>s</code> is present and <b>not</b> empty, an exact copy of <code>this</code> will
	 *     be returned with <code>s</code> all occurrences of s removed from the string</li>
	 *     <li>if <code>s</code> is <b>not</b> present or is empty, <code>this</code> will be returned</li>
	 * </ul>
	 *
	 * @param s String to be removed from the string of the returned copy of <code>this</code>
	 * @return object of type HoneyBee
	 */
	@Override
	public HoneyBee subtract(String s) {
		if (s.isEmpty()) {
			return this;
		}

		String removed = string.replaceAll(s, "");
		if (removed.equals(string)) {
			return this;
		}

		return new HoneyBee(super.getTime(), super.getDescription(), super.getMarker(), removed);
	}

	/**
	 * Returns a string representing the type of bee or breeding
	 *
	 * @return the string of this HoneyBee
	 */
	public String sort() {
		return string;
	}
}