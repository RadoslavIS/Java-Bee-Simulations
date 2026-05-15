import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.NavigableMap;
import java.util.Objects;

/**
 * Observation for a Honey bee
 **/
public class Honeybee implements SocialBee {
	protected final NavigableMap<LocalDateTime, Observation> allObservations;
	private final LocalDateTime time;
	private final String comment;
	private boolean isValid;
	private int marker;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Honeybee honeybee)) return false;
		return marker != -1 && marker == honeybee.marker && honeybee.isValid;
	}

	@Override
	public int hashCode() {
		return marker;
	}

	@Override
	public LocalDateTime getTime() {
		return time;
	}

	@Override
	public String getComment() {
		return comment;
	}

	@Override
	public boolean valid() {
		return isValid;
	}

	@Override
	public void remove() {
		isValid = false;
	}

	@Override
	public Iterator<Observation> earlier() {
		return allObservations.
				headMap(time, false).
				descendingMap().
				values().stream().
				filter(Observation::valid).
				iterator();
	}

	@Override
	public Iterator<Observation> later() {
		return allObservations.
				tailMap(time, false).
				values().stream().
				filter(Observation::valid).
				iterator();
	}

	@Override
	public Iterator<Bee> sameBee(boolean direction, LocalDateTime startTime, LocalDateTime endTime) {
		NavigableMap<LocalDateTime, Observation> nm = allObservations.subMap(startTime, false, endTime, false);
		if (!direction) {
			nm = nm.descendingMap();
		}
		return nm.values().stream().
				filter(this::equals).
				map(Bee.class::cast).
				iterator();
	}

	@Override
	public Iterator<SocialBee> social() {
		return allObservations.values().stream().
				filter(this::equals).
				map(SocialBee.class::cast).
				iterator();
	}

	/**
	 * Creates a new individualized observation of a Honeybee, identified by a marker and a reference.
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *     <li>allObservations is the global non-null registry of observations</li>
	 *     <li>time != null</li>
	 *     <li>-1 <= marker <= MaxMarker + 1</li>
	 *     <li>marker must not be used by a different species</li>
	 *     <li>reference != null</li>
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
	 * @param reference       reference to another Honeybee observation
	 */
	public Honeybee(NavigableMap<LocalDateTime, Observation> allObservations, LocalDateTime time, String comment,
					int marker, Honeybee reference) {
		this(allObservations, time, comment, marker);
		Objects.requireNonNull(reference, "Reference must not be null").equalize(marker);
	}

	/**
	 * Creates a new individualized observation of a Honeybee, identified by a reference.
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *     <li>allObservations is the global non-null registry of observations</li>
	 *     <li>time != null</li>
	 *     <li>reference != null</li>
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
	 * @param reference       reference to another Honeybee observation
	 */
	public Honeybee(NavigableMap<LocalDateTime, Observation> allObservations, LocalDateTime time, String comment, Honeybee reference) {
		this(allObservations, time, comment, ensureIndividualizedReference(Objects.requireNonNull(reference, "Reference must not be null")));
	}

	/**
	 * Creates a new individualized observation of a Honeybee, identified by a marker.
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *     <li>allObservations is the global non-null registry of observations</li>
	 *     <li>time != null</li>
	 *     <li>-1 <= marker <= MaxMarker + 1</li>
	 *     <li>marker must not be used by a different species</li>
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
	 */
	public Honeybee(NavigableMap<LocalDateTime, Observation> allObservations, LocalDateTime time, String comment, int marker) {
		this.allObservations = Objects.requireNonNull(allObservations, "allObservations must not be null.");
		if (marker > getMaxMarker() + 1 || marker < -1) {
			throw new IllegalArgumentException("Marker must be already in use by a bee or increment the last issued for a new one.");
		}
		if (marker != -1 && markerUsedByDifferentSpecies(marker)) {
			throw new IllegalArgumentException("Marker already used for a different species.");
		}
		this.time = time;
		this.comment = comment;
		this.isValid = true;
		this.marker = marker;
		allObservations.put(time, this);
	}

	/**
	 * Creates a new non-individualized observation of a CommunalBee.
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *     <li>allObservations is the global non-null registry of observations</li>
	 *     <li>time != null</li>
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
	 */
	public Honeybee(NavigableMap<LocalDateTime, Observation> allObservations, LocalDateTime time, String comment) {
		this(allObservations, time, comment, -1);
	}

	private void equalize(int newMarker) {
		if (marker == newMarker) {
			return;
		}
		if (marker == -1) {
			marker = newMarker;
			return;
		}
		int oldMarker = marker;
		Iterator<Bee> it = sameBee();
		while (it.hasNext()) {
			((Honeybee) it.next()).marker = newMarker;
		}
		Iterator<Honeybee> it1 = allObservations.values().stream().
				filter(Observation::valid).
				filter(Honeybee.class::isInstance).
				map(Honeybee.class::cast).
				filter(hb -> hb.marker == getMaxMarker()).
				iterator();
		while (it1.hasNext()) {
			it1.next().marker = oldMarker;
		}
	}

	private int getMaxMarker() {
		return allObservations.values().stream().
				filter(Observation::valid).
				filter(Bee.class::isInstance).
				map(Bee.class::cast).
				mapToInt(Bee::hashCode).
				max().orElse(-1);
	}

	private static int ensureIndividualizedReference(Honeybee reference) {
		if (reference.marker == -1) {
			reference.individualize();
		}
		return reference.marker;
	}

	private void individualize() {
		this.marker = getMaxMarker() + 1;
	}

	private boolean markerUsedByDifferentSpecies(int marker) {
		boolean usedByWildBees = allObservations.values().stream().
				filter(WildBee.class::isInstance).
				map(WildBee.class::cast).
				anyMatch(wildBee -> wildBee.hashCode() == marker);
		boolean usedByHoneybees = allObservations.values().stream().
				filter(Honeybee.class::isInstance).
				map(Honeybee.class::cast).
				anyMatch(honeybee -> honeybee.hashCode() == marker && getClass() != honeybee.getClass());
		return usedByHoneybees || usedByWildBees;
	}
}