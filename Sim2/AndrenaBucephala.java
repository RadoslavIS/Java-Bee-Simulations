import java.time.LocalDateTime;
import java.util.NavigableMap;

public class AndrenaBucephala extends CommunalBee {

    public enum PollenSource {
        SWEETCHERRY,
        HAWTHORNS,
        SALIXCAPREA,
        MAPLE
    }

    private final ObservedNesting nesting = ObservedNesting.CAVITY;

    private final PollenSource[] pollenSources = PollenSource.values();

    /**
     * Creates a new individualized observation of a AndrenaBucephala, identified by a marker and a reference.
     * <p><b>Preconditions:</b>
     * <ul>
     *     <li>allObservations is the global non-null registry of observations</li>
     *     <li>time != null</li>
     *     <li>0 <= marker <= MaxMarker + 1</li>
     *     <li>marker must not be used by a different species</li>
     *     <li>reference != null</li>
     *     <li>if isBred is not known, default value is false</li>
     *     <li>behaviour == COMMUNAL || behaviour == SOLITARY</li>
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
     * @param behaviour       observed behaviour
     */
    public AndrenaBucephala(NavigableMap<LocalDateTime, Observation> allObservations, LocalDateTime time, String comment,
                            int marker, AndrenaBucephala reference, boolean isBred, ObservedBehaviour behaviour) {
        super(allObservations, time, comment, marker, reference, isBred, behaviour);
    }

    /**
     * Creates a new individualized observation of a AndrenaBucephala, identified by a reference.
     * <p><b>Preconditions:</b>
     * <ul>
     *     <li>allObservations is the global non-null registry of observations</li>
     *     <li>time != null</li>
     *     <li>reference != null</li>
     *     <li>if isBred is not known, default value is false</li>
     *     <li>behaviour == COMMUNAL || behaviour == SOLITARY</li>
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
     * @param behaviour       observed behaviour
     */
    public AndrenaBucephala(NavigableMap<LocalDateTime, Observation> allObservations, LocalDateTime time, String comment,
                       AndrenaBucephala reference, boolean isBred, ObservedBehaviour behaviour) {
        super(allObservations, time, comment, reference, isBred, behaviour);
    }

    /**
     * Creates a new individualized observation of a AndrenaBucephala, identified by a marker.
     * <p><b>Preconditions:</b>
     * <ul>
     *     <li>allObservations is the global non-null registry of observations</li>
     *     <li>time != null</li>
     *     <li>0 <= marker <= MaxMarker + 1</li>
     *     <li>if isBred is not known, default value is false</li>
     *     <li>behaviour == COMMUNAL || behaviour == SOLITARY</li>
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
     * @param behaviour       observed behaviour
     */
    public AndrenaBucephala(NavigableMap<LocalDateTime, Observation> allObservations, LocalDateTime time, String comment,
                       int marker, boolean isBred, ObservedBehaviour behaviour) {
        super(allObservations, time, comment, marker, isBred, behaviour);
    }

    /**
     * Creates a new non-individualized observation of a AndrenaBucephala.
     * <p><b>Preconditions:</b>
     * <ul>
     *     <li>allObservations is the global non-null registry of observations</li>
     *     <li>time != null</li>
     *     <li>if isBred is not known, default value is false</li>
     *     <li>behaviour == COMMUNAL || behaviour == SOLITARY</li>
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
     * @param behaviour       observed behaviour
     */
    public AndrenaBucephala(NavigableMap<LocalDateTime, Observation> allObservations, LocalDateTime time, String comment,
                            boolean isBred, ObservedBehaviour behaviour) {
        super(allObservations, time, comment, isBred, behaviour);
    }

    /**
     * Returns the observed nesting type.
     *
     * @return the nesting type
     */
    public ObservedNesting getNesting() {
        return nesting;
    }

    /**
     * Returns an array of pollen sources that this species utilizes.
     *
     * @return an array of pollen sources
     */
    public PollenSource[] getPollenSources() {
        return pollenSources;
    }
}