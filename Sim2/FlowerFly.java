import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.NavigableMap;
import java.util.Objects;

/** Observation for a Flower fly **/
public class FlowerFly implements Pollinator {
    protected final NavigableMap<LocalDateTime, Observation> allObservations;
    private final LocalDateTime time;
    private final String comment;
    private boolean isValid;

    @Override
    public Iterator<Observation> earlier() {
        return allObservations.
                headMap(time,false).
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

    /**
     * Creates a new non-individualized observation of a WildBee.
     * <p><b>Preconditions:</b>
     * <ul>
     *     <li>allObservations is the global non-null registry of observations</li>
     *     <li>time != null</li>
     *     <li>marker must not be used by a different species</li>
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
     */
    public FlowerFly(NavigableMap<LocalDateTime, Observation> allObservations, LocalDateTime time, String comment) {
        this.allObservations = Objects.requireNonNull(allObservations, "allObservations must not be null.");
        this.time = time;
        this.comment = comment;
        this.isValid = true;
        allObservations.put(time, this);
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

}
