import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.NavigableMap;

public class Bumblebee extends WildBee implements SocialBee {

	public enum BumblebeeCaste {
		QUEEN,
		WORKER,
		MALE,
		YOUNG_QUEEN
	}

	private final BumblebeeCaste caste;

	@Override
	public Iterator<SocialBee> social() {
		return allObservations.values().stream().
				filter(this::equals).
				map(SocialBee.class::cast).
				iterator();
	}

	/**
	 * Creates a new individualized observation of a Bumblebee, identified by a marker and a reference.
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *     <li>allObservations is the global non-null registry of observations</li>
	 *     <li>time != null</li>
	 *     <li>0 <= marker <= MaxMarker + 1</li>
	 *     <li>marker must not be used by a different species</li>
	 *     <li>reference != null</li>
	 *     <li>if isBred is not known, default value is false</li>
	 *     <li>caste != null</li>
	 * </ul>
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *     <li>if reference.marker == marker, both observations remain with marker = marker</li>
	 *     <li>if reference.marker == -1, <code>reference.marker = marker</code></li>
	 *     <li>else, all observations with marker == reference.marker will get the new marker and
	 *     all observations with MaxMarker will get the old reference.marker</li>
	 *     <li><code>this</code> is added to <code>allObservations</code></li>
	 * </ul>
	 *
	 * @param allObservations global non-null registry of observations
	 * @param time            exact time of the observation
	 * @param comment         additional information about the observation
	 * @param marker          identifier of an individual bee
	 * @param reference       reference to another WildBee observation
	 * @param isBred          observed origin
	 * @param caste           caste of the Bumblebee
	 */
	public Bumblebee(NavigableMap<LocalDateTime, Observation> allObservations, LocalDateTime time, String comment,
					 int marker, Bumblebee reference, boolean isBred, BumblebeeCaste caste) {
		super(allObservations, time, comment, marker, reference, isBred);
		this.caste = caste;
	}

	/**
	 * Creates a new individualized observation of a Bumblebee, identified by a reference.
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *     <li>allObservations is the global non-null registry of observations</li>
	 *     <li>time != null</li>
	 *     <li>reference != null</li>
	 *     <li>if isBred is not known, default value is false</li>
	 *     <li>caste != null</li>
	 * </ul>
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *     <li>if reference.marker == -1, both observations get marker = MaxMarker + 1</li>
	 *     <li>else, <code>this.marker = reference.marker</code></li>
	 *     <li><code>this</code> is added to <code>allObservations</code></li>
	 * </ul>
	 *
	 * @param allObservations global non-null registry of observations
	 * @param time            exact time of the observation
	 * @param comment         additional information about the observation
	 * @param reference       reference to another WildBee observation
	 * @param isBred          observed origin
	 * @param caste           caste of the Bumblebee
	 */
	public Bumblebee(NavigableMap<LocalDateTime, Observation> allObservations, LocalDateTime time, String comment,
					 Bumblebee reference, boolean isBred, BumblebeeCaste caste) {
		super(allObservations, time, comment, reference, isBred);
		this.caste = caste;
	}

	/**
	 * Creates a new individualized observation of a Bumblebee, identified by a marker.
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *     <li>allObservations is the global non-null registry of observations</li>
	 *     <li>time != null</li>
	 *     <li>0 <= marker <= MaxMarker + 1</li>
	 *     <li>marker must not be used by a different species</li>
	 *     <li>if isBred is not known, default value is false</li>
	 *     <li>caste != null</li>
	 * </ul>
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *     <li><code>this.marker = marker</code></li>
	 *     <li><code>this</code> is added to <code>allObservations</code></li>
	 * </ul>
	 *
	 * @param allObservations global non-null registry of observations
	 * @param time            exact time of the observation
	 * @param comment         additional information about the observation
	 * @param marker          identifier of an individual bee
	 * @param isBred          observed origin
	 * @param caste           caste of the Bumblebee
	 */
	public Bumblebee(NavigableMap<LocalDateTime, Observation> allObservations, LocalDateTime time, String comment,
					 int marker, boolean isBred, BumblebeeCaste caste) {
		super(allObservations, time, comment, marker, isBred);
		this.caste = caste;
	}

	/**
	 * Creates a new non-individualized observation of a Bumblebee.
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *     <li>allObservations is the global non-null registry of observations</li>
	 *     <li>time != null</li>
	 *     <li>if isBred is not known, default value is false</li>
	 *     <li>caste != null</li>
	 * </ul>
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *     <li><code>this.marker = -1</code></li>
	 *     <li><code>this</code> is added to <code>allObservations</code></li>
	 * </ul>
	 *
	 * @param allObservations global non-null registry of observations
	 * @param time            exact time of the observation
	 * @param comment         additional information about the observation
	 * @param isBred          observed origin
	 * @param caste           caste of the Bumblebee
	 */
	public Bumblebee(NavigableMap<LocalDateTime, Observation> allObservations, LocalDateTime time, String comment,
					 boolean isBred, BumblebeeCaste caste) {
		super(allObservations, time, comment, -1, isBred);
		this.caste = caste;
	}

	/**
	 * Attempts to found a new bumblebee state at the given time.
	 * <p>
	 * Only bumblebees that can overwinter (young queens) can found a state. If successful,
	 * a new {@link Bumblebee} instance is created and recorded in {@code allObservations}.
	 *
	 * @param time the time at which the new state is founded; not null
	 * @return the newly created {@link Bumblebee} representing the founder
	 * @throws IllegalArgumentException if the bumblebee is not a young queen
	 */
	public Bumblebee foundState(LocalDateTime time) {
		if (isOverwintering()) {
			Bumblebee newBee = new Bumblebee(allObservations, time, "State founder", false, getCaste());
			allObservations.put(time, newBee);
		}
		throw new IllegalArgumentException("only young queens can found a state");
	}

	/**
	 * Returns the caste of this bumblebee.
	 *
	 * @return the caste of the bumblebee
	 */
	public BumblebeeCaste getCaste() {
		return caste;
	}

	/**
	 * Checks if this bumblebee can overwinter.
	 * <p>
	 * Only young queens overwinter.
	 *
	 * @return {@code true} if the bumblebee is a young queen (and can overwinter), {@code false} otherwise
	 */
	public boolean isOverwintering() {
		return getCaste() == BumblebeeCaste.YOUNG_QUEEN;
	}
}