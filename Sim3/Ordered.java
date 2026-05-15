/**
 * Represents an ordering relation on elements of type {@code E}.
 */
public interface Ordered<E, R> {
    /**
     * Query whether {@code x} is (in this ordering) before {@code y}
     * The method must not modify {@code this}, {@code x} or {@code y}.
     *
     * @param x first element
     * @param y second element
     * @return non-null of type {@code R} if {@code x} is before {@code y},
     *         otherwise null
     */
    R before(E x, E y);

    /**
     * Ensure that {@code x} is before {@code y} in the ordering represented by {@code this}.
     *
     * <p>After successful completion, {@code before(x,y)} must return a non-{@code null}
     * value. Implementations may insert {@code x} or {@code y} into the container if
     * necessary (and if doing so is allowed by the concrete type).
     *
     * {@code setBefore(x,y)} may throw {@link IllegalArgumentException} in the following
     * situations:
     * <ul>
     *     <li>{@code x} and {@code y} are identical </li>
     *     <li>preconditions about presence or ordering of {@code x} or {@code y} expected by
     *         the implementation are not fulfilled;</li>
     *     <li>adding the requested relation would violate internal invariants.</li>
     * </ul>
     *
     * @param x first element
     * @param y second element
     * @throws IllegalArgumentException if the requested relation cannot be established
     *                                  for reasons described above
     */
    void setBefore(E x, E y) throws IllegalArgumentException;
}
