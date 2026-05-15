import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.NavigableMap;

public abstract class CommunalBee extends SolitaryBee{

    /** Returns an iterator of all valid observations of the same individual with a communal lifestyle<br>
     * @return <code>CommunalBee</code> Iterator with behaviour == communal of the same individual **/
    public Iterator<CommunalBee> communal(){
        return allObservations.values().stream().
                filter(this::equals).
                map(CommunalBee.class::cast).
                filter(communalBee -> communalBee.behaviour == ObservedBehaviour.COMMUNAL).
                iterator();
    }

    /**
     * Creates a new individualized observation of a CommunalBee, identified by a marker and a reference.
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
     * @param reference       reference to another CommunalBee observation
     * @param isBred          observed origin
     * @param behaviour 	  observed lifestyle
     */
    public CommunalBee(NavigableMap<LocalDateTime, Observation> allObservations, LocalDateTime time, String comment,
                       int marker, CommunalBee reference, boolean isBred, ObservedBehaviour behaviour) {
        super(allObservations, time, comment, marker, reference, isBred, behaviour);
    }

    /**
     * Creates a new individualized observation of a CommunalBee, identified by a reference.
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
     * @param reference       reference to another CommunalBee observation
     * @param isBred          observed origin
     * @param behaviour 	  observed lifestyle
     */
    public CommunalBee(NavigableMap<LocalDateTime, Observation> allObservations, LocalDateTime time, String comment,
                       CommunalBee reference, boolean isBred, ObservedBehaviour behaviour) {
        super(allObservations, time, comment, reference, isBred, behaviour);
    }

    /**
     * Creates a new individualized observation of a CommunalBee, identified by a marker.
     * <p><b>Preconditions:</b>
     * <ul>
     *     <li>allObservations is the global non-null registry of observations</li>
     *     <li>time != null</li>
     *     <li>0 <= marker <= MaxMarker + 1</li>
     *     <li>marker must not be used by a different species</li>
     *     <li>if isBred is not known, default value is false</li>
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
     * @param behaviour 	  observed lifestyle
     */
    public CommunalBee(NavigableMap<LocalDateTime, Observation> allObservations, LocalDateTime time, String comment,
                       int marker, boolean isBred, ObservedBehaviour behaviour) {
        super(allObservations, time, comment, marker, isBred, behaviour);
    }

    /**
     * Creates a new non-individualized observation of a CommunalBee.
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
     * @param behaviour 	  observed lifestyle
     */
    public CommunalBee(NavigableMap<LocalDateTime, Observation> allObservations, LocalDateTime time, String comment,
                       boolean isBred, ObservedBehaviour behaviour) {
        super(allObservations, time, comment, isBred, behaviour);
    }
}
