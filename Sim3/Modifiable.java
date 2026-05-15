public interface Modifiable<X, T> {
    /**
     * Returns a T representing “this extended by x” (no mutation).
     * If extension impossible, return this.
     * <p><b>Postconditions:</b>
     * <ul>
     *     <li>x stays unchanged</li>
     *     <li>if extension is possible, return a modified instance (depending on implementation)</li>
     *     <li>if extension is impossible, return this</li>
     * </ul>
     *
     * @param x     value to add
     * @return extended instance of this, or this
     */
    T add(X x);

    /**
     * Return a T representing “this with x removed” (no mutation).
     * If impossible, return this.
     * <p><b>Postconditions:</b>
     * <ul>
     *     <li>x stays unchanged</li>
     *     <li>if possible, return a modified instance (depending on implementation)</li>
     *     <li>if impossible, return this</li>
     * </ul>
     *
     * @param x     value to subtract
     * @return modified instance of this, or this
     */
    T subtract(X x);
}
