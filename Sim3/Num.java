public class Num implements Modifiable<Num, Num> {

	private final Integer number;

	/**
	 * Creates a new Num with the given number.
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *     <li>number != null</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *     <li><code>getValue() == number</code></li>
	 *     <li>the created object is immutable</li>
	 * </ul>
	 *
	 * @param number the integer value to store
	 */
	public Num(Integer number) {
		this.number = number;
	}

	/**
	 * Returns a new <code>Num</code> with value equal to the sum of the values of <code>this</code> and <code>x</code>.
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *     <li>x != null</li>
	 *     <li>x.number != null</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 * 	   <li>a new <code>Num</code> will be returned with value equal to <code>this.number + x.number</code></li>
	 *     <li><code>this</code> remains unchanged</li>
	 *     <li><code>x</code> remains unchanged</li>
	 * </ul>
	 *
	 * @param x the value to add to <code>this</code>
	 * @return new <code>Num</code> containing the sum of the two numbers
	 */
	@Override
	public Num add(Num x) {
		return new Num(number + x.number);
	}

	/**
	 * Returns a new <code>Num</code> with value equal to the difference of the values of <code>this</code> and <code>x</code>.
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *     <li>x != null</li>
	 *     <li>x.number != null</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 * 	   <li>a new <code>Num</code> will be returned with value equal to <code>this.number - x.number</code></li>
	 *     <li><code>this</code> remains unchanged</li>
	 *     <li><code>x</code> remains unchanged</li>
	 * </ul>
	 *
	 * @param x the value to subtract from <code>this</code>
	 * @return new <code>Num</code> containing the sum of the two numbers
	 */
	@Override
	public Num subtract(Num x) {
		return new Num(number - x.number);
	}

	@Override
	public String toString() {
		return number.toString();
	}

	/**
	 * The value of <code>this</code>.
	 *
	 * @return the Integer number of <code>this</code>
	 */
	public Integer getValue() {
		return this.number;
	}
}