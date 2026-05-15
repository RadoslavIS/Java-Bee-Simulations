import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.NavigableMap;
import java.util.Objects;

public abstract class WildBee implements Bee {
	protected final NavigableMap<LocalDateTime, Observation> allObservations;
	private final LocalDateTime time;
	private final String comment;
	private boolean isValid;
	private int marker;
	private final boolean isBred;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof WildBee wildBee)) return false;
		return marker != -1 && marker == wildBee.marker && wildBee.isValid;
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
		this.isValid = false;
	}

	@Override
	public Iterator<Observation> earlier() {
		return allObservations.
				headMap(time, false).
				descendingMap().
				values().
				stream().
				filter(Observation::valid).
				iterator();
	}

	@Override
	public Iterator<Observation> later() {
		return allObservations.
				tailMap(time, false).
				values().
				stream().
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

	/**
	 * All valid observations of the same individual wild bee, where their observed origin is the same.
	 *
	 * @param isBred whether the bee lives wild or is bred by humans
	 * @return iterator of all observations, corresponding to the same individual wild bee, where their isBred is the same as this
	 */
	public Iterator<WildBee> wild(boolean isBred) {
		return allObservations.values().stream().
				filter(this::equals).
				map(WildBee.class::cast).
				filter(wildBee -> wildBee.isBred == isBred).
				iterator();
	}

	/**
	 * Creates a new individualized observation of a WildBee, identified by a marker and a reference.
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *     <li>allObservations is the global non-null registry of observations</li>
	 *     <li>time != null</li>
	 *     <li>0 <= marker <= MaxMarker + 1</li>
	 *     <li>marker must not be used by a different species</li>
	 *     <li>reference != null</li>
	 *     <li>if isBred is not known, default value is false</li>
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
	 */
	public WildBee(NavigableMap<LocalDateTime, Observation> allObservations, LocalDateTime time, String comment,
				   int marker, WildBee reference, boolean isBred) {
		this(allObservations, time, comment, marker, isBred);
		Objects.requireNonNull(reference, "Reference must not be null").equalize(marker);
	}

	/**
	 * Creates a new individualized observation of a WildBee, identified by a reference.
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *     <li>allObservations is the global non-null registry of observations</li>
	 *     <li>time != null</li>
	 *     <li>reference != null</li>
	 *     <li>if isBred is not known, default value is false</li>
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
	 */
	public WildBee(NavigableMap<LocalDateTime, Observation> allObservations, LocalDateTime time, String comment,
				   WildBee reference, boolean isBred) {
		this(allObservations, time, comment,
				ensureIndividualizedReference(Objects.requireNonNull(reference, "Reference must not be null")), isBred);
	}

	/**
	 * Creates a new individualized observation of a WildBee, identified by a marker.
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *     <li>allObservations is the global non-null registry of observations</li>
	 *     <li>time != null</li>
	 *     <li>-1 <= marker <= MaxMarker + 1</li>
	 *     <li>marker must not be used by a different species</li>
	 *     <li>if isBred is not known, default value is false</li>
	 * </ul>
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *     <li><code>this.marker = marker</code></li>
	 *     <li>if marker == -1, bee is not individualized, otherwise it is</li>
	 *     <li><code>this</code> is added to <code>allObservations</code></li>
	 * </ul>
	 *
	 * @param allObservations global non-null registry of observations
	 * @param time            exact time of the observation
	 * @param comment         additional information about the observation
	 * @param marker          identifier of an individual bee
	 * @param isBred          observed origin
	 */
	public WildBee(NavigableMap<LocalDateTime, Observation> allObservations, LocalDateTime time, String comment,
				   int marker, boolean isBred) {
		this.allObservations = Objects.requireNonNull(allObservations, "allObservations must not be null.");
		if (marker > getMaxMarker() + 1 || marker < -1) {
			throw new IllegalArgumentException("Marker must be already in use by a bee or increment the last issued marker for a new one.");
		}
		if (marker != -1 && markerUsedByDifferentSpecies(marker)) {
			throw new IllegalArgumentException("Marker already used for a different species.");
		}
		this.time = time;
		this.comment = comment;
		this.isValid = true;
		this.marker = marker;
		this.isBred = isBred;
		allObservations.put(time, this);
	}

	/**
	 * Creates a new non-individualized observation of a WildBee.
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *     <li>allObservations is the global non-null registry of observations</li>
	 *     <li>time != null</li>
	 *     <li>if isBred is not known, default value is false</li>
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
	 */
	public WildBee(NavigableMap<LocalDateTime, Observation> allObservations, LocalDateTime time, String comment, boolean isBred) {
		this(allObservations, time, comment, -1, isBred);
	}

	private int getMaxMarker() {
		return allObservations.values().stream().
				filter(Observation::valid).
				filter(Bee.class::isInstance).
				map(Bee.class::cast).
				mapToInt(Bee::hashCode).
				max().orElse(-1);
	}

	private static int ensureIndividualizedReference(WildBee reference) {
		if (reference.marker == -1) {
			reference.individualize();
		}
		return reference.marker;
	}

	private void individualize() {
		this.marker = getMaxMarker() + 1;
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
			((WildBee) it.next()).marker = newMarker;
		}
		Iterator<WildBee> it1 = allObservations.values().stream().
				filter(Observation::valid).
				filter(WildBee.class::isInstance).
				map(WildBee.class::cast).
				filter(wb -> wb.marker == getMaxMarker()).
				iterator();
		while (it1.hasNext()) {
			it1.next().marker = oldMarker;
		}
	}

	private boolean markerUsedByDifferentSpecies(int marker) {
		boolean usedByWildBees = allObservations.values().stream().
				filter(WildBee.class::isInstance).
				map(WildBee.class::cast).
				anyMatch(wildBee -> wildBee.hashCode() == marker && getClass() != wildBee.getClass());
		boolean usedByHoneybees = allObservations.values().stream().
				filter(Honeybee.class::isInstance).
				map(Honeybee.class::cast).
				anyMatch(honeybee -> honeybee.hashCode() == marker);
		return usedByHoneybees || usedByWildBees;
	}
}