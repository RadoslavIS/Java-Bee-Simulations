import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.NavigableMap;

public class LasioglossumCalceatum extends SolitaryBee implements SocialBee{

    public enum LasioglossumCalceatumCaste {
        MALE,
        FEMALE,
        FOUNDRESS,
        FUTURE_FOUNDRESS
    }
    private LasioglossumCalceatumCaste caste;

    private final ObservedNesting nesting;

    @Override
    public Iterator<SocialBee> social() {
        return allObservations.values().stream().
                filter(this::equals).
                map(SocialBee.class::cast).
                iterator();
    }
    /**
     * Creates a new individualized observation of a LasioglossumCalceatum, identified by a marker and a reference.
     * <p><b>Preconditions:</b>
     * <ul>
     *     <li>allObservations is the global non-null registry of observations</li>
     *     <li>time != null</li>
     *     <li>0 <= marker <= MaxMarker + 1</li>
     *     <li>marker must not be used by a different species</li>
     *     <li>reference != null</li>
     *     <li>if isBred is not known, default value is false</li>
     *     <li>behaviour == SOCIAL || behaviour == SOLITARY</li>
     *     <li>caste != null</li>
     *     <li>nesting != null</li>
     * </ul>
     * <p><b>Postconditions:</b>
     * <ul>
     *     <li>if reference.marker == marker, both observations remain with marker = marker</l>
     *     <li>if reference.marker == -1, <code>reference.marker = marker</code></l>
     *     <li>else, all observations with marker == reference.marker will get the new marker and
     *     all observations with MaxMarker will get the old reference.marker</l>
     *     <li><code>this</code> is added to <code>allObservations</code></l>
     * </ul>
     *
     * @param allObservations global non-null registry of observations
     * @param time            exact time of the observation
     * @param comment         additional information about the observation
     * @param marker          identifier of an individual bee
     * @param reference       reference to another WildBee observation
     * @param isBred          observed origin
     * @param behaviour       observed behaviour
     * @param caste           caste of the Lasioglossum Calceatum
     * @param nesting         preferred nesting type
     */
    public LasioglossumCalceatum(NavigableMap<LocalDateTime, Observation> allObservations, LocalDateTime time, String comment,
                                 int marker, LasioglossumCalceatum reference, boolean isBred, ObservedBehaviour behaviour,
                                 LasioglossumCalceatumCaste caste, ObservedNesting nesting) {
        super(allObservations, time, comment, marker, reference, isBred, behaviour);
        this.caste = caste;
        this.nesting = nesting;
    }

    /**
     * Creates a new individualized observation of a LasioglossumCalceatum, identified by a reference.
     * <p><b>Preconditions:</b>
     * <ul>
     *     <li>allObservations is the global non-null registry of observations</li>
     *     <li>time != null</li>
     *     <li>reference != null</li>
     *     <li>if isBred is not known, default value is false</li>
     *     <li>behaviour == SOCIAL || behaviour == SOLITARY</li>
     *     <li>caste != null</li>
     *     <li>nesting != null</li>
     * </ul>
     * <p><b>Postconditions:</b>
     * <ul>
     *     <li>if reference.marker == -1, both observations get marker = MaxMarker + 1</l>
     *     <li>else, <code>this.marker = reference.marker</code></l>
     *     <li><code>this</code> is added to <code>allObservations</code></l>
     * </ul>
     *
     * @param allObservations global non-null registry of observations
     * @param time            exact time of the observation
     * @param comment         additional information about the observation
     * @param reference       reference to another WildBee observation
     * @param isBred          observed origin
     * @param behaviour       observed behaviour
     * @param caste           caste of the Lasioglossum Calceatum
     * @param nesting         preferred nesting type
     */
    public LasioglossumCalceatum(NavigableMap<LocalDateTime, Observation> allObservations, LocalDateTime time, String comment,
                                 LasioglossumCalceatum reference, boolean isBred, ObservedBehaviour behaviour,
                                 LasioglossumCalceatumCaste caste, ObservedNesting nesting) {
        super(allObservations, time, comment, reference, isBred, behaviour);
        this.caste = caste;
        this.nesting = nesting;
    }
    /**
     * Creates a new individualized observation of a LasioglossumCalceatum, identified by a marker.
     * <p><b>Preconditions:</b>
     * <ul>
     *     <li>allObservations is the global non-null registry of observations</li>
     *     <li>time != null</li>
     *     <li>0 <= marker <= MaxMarker + 1</li>
     *     <li>marker must not be used by a different species</li>
     *     <li>if isBred is not known, default value is false</li>
     *     <li>behaviour == SOCIAL || behaviour == SOLITARY</li>
     *     <li>caste != null</li>
     *     <li>nesting != null</li>
     * </ul>
     * <p><b>Postconditions:</b>
     * <ul>
     *     <li><code>this.marker = marker</code></l>
     *     <li><code>this</code> is added to <code>allObservations</code></l>
     * </ul>
     *
     * @param allObservations global non-null registry of observations
     * @param time            exact time of the observation
     * @param comment         additional information about the observation
     * @param marker          identifier of an individual bee
     * @param isBred          observed origin
     * @param behaviour       observed behaviour
     * @param caste           caste of the Lasioglossum Calceatum
     * @param nesting         preferred nesting type
     */
    public LasioglossumCalceatum(NavigableMap<LocalDateTime, Observation> allObservations, LocalDateTime time, String comment,
                                 int marker, boolean isBred, ObservedBehaviour behaviour,
                                 LasioglossumCalceatumCaste caste, ObservedNesting nesting) {
        super(allObservations, time, comment, marker, isBred, behaviour);
        this.caste = caste;
        this.nesting = nesting;
    }

    /**
     * Creates a new non-individualized observation of a LasioglossumCalceatum.
     * <p><b>Preconditions:</b>
     * <ul>
     *     <li>allObservations is the global non-null registry of observations</li>
     *     <li>time != null</li>
     *     <li>if isBred is not known, default value is false</li>
     *     <li>behaviour == SOCIAL || behaviour == SOLITARY</li>
     *     <li>caste != null</li>
     *     <li>nesting != null</li>
     * </ul>
     * <p><b>Postconditions:</b>
     * <ul>
     *     <li><code>this.marker = -1</code></l>
     *     <li><code>this</code> is added to <code>allObservations</code></l>
     * </ul>
     *
     * @param allObservations global non-null registry of observations
     * @param time            exact time of the observation
     * @param comment         additional information about the observation
     * @param isBred          observed origin
     * @param behaviour       observed behaviour
     * @param caste           caste of the Lasioglossum Calceatum
     * @param nesting         preferred nesting type
     */
    public LasioglossumCalceatum(NavigableMap<LocalDateTime, Observation> allObservations, LocalDateTime time, String comment,
                                 boolean isBred, ObservedBehaviour behaviour,
                                 LasioglossumCalceatumCaste caste, ObservedNesting nesting) {
        super(allObservations, time, comment, isBred, behaviour);
        this.caste = caste;
        this.nesting = nesting;
    }
    /**
     * Returns the caste of this Lasioglossum Calceatum.
     *
     * @return the caste of the Lasioglossum Calceatum
     */
    public LasioglossumCalceatumCaste getCaste() {
        return caste;
    }
    /**
     * Returns the observed nesting type.
     *
     * @return the nesting type
     */
    public ObservedNesting getNesting() {
        return nesting;
    }
}