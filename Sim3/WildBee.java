import java.time.LocalDateTime;

/** Observation of wild bees **/
public class WildBee extends Bee implements Modifiable<Integer, WildBee> {

    private final int length;


    /**
     * Constructs a WildBee observation.
     *
     * <p><b>Precondition:</b>
     * <ul>
     *   <li>time != null</li>
     *   <li>description != null</li>
     *   <li>marker >= 0</li>
     *   <li>length > 0</li>
     * </ul>
     *
     * <b>Postcondition:</b> a new WildBee is created with the given properties.
     *
     * @param time        the time of the observation
     * @param description short description about the observation
     * @param marker      a numerical identifier
     * @param length      estimated length in millimeters
     */
    public WildBee(LocalDateTime time, String description, int marker, int length) {
        super(time,description,marker);
        this.length = length;
    }

    /**
     * Returns a new WildBee object with an increased length.
     * If increase impossible, return this.
     * <p><b>Preconditions:</b>
     * <ul>
     *     <li>integer > 0</li>
     * </ul>
     * <p><b>Postconditions:</b>
     * <ul>
     *     <li>x stays unchanged</li>
     *     <li>if increase is possible, return a new WildBee object with length = this.length + integer (other parameters stay the same)</li>
     *     <li>if increase is impossible, return this</li>
     * </ul>
     *
     * @param integer     value to add
     * @return a new WildBee with increased length, or this
     */
    @Override
    public WildBee add(Integer integer) {
        if(integer > 0) {
            return new WildBee(super.getTime(), super.getDescription(), super.getMarker(), this.length + integer);
        }
        return this;
    }
    /**
     * Returns a new WildBee object with a decreased length.
     * If decrease impossible, return this.
     * <p><b>Preconditions:</b>
     * <ul>
     *     <li>integer > 0</li>
     * </ul>
     * <p><b>Postconditions:</b>
     * <ul>
     *     <li>x stays unchanged</li>
     *     <li>if decrease is possible, return a new WildBee object with length = this.length - integer (other parameters stay the same)</li>
     *     <li>if decrease is impossible, return this</li>
     * </ul>
     *
     * @param integer     value to subtract
     * @return a new WildBee with decreased length, or this
     */
    @Override
    public WildBee subtract(Integer integer) {
        if(integer > 0 && integer < length) {
            return new WildBee(super.getTime(), super.getDescription(), super.getMarker(), this.length - integer);
        }

        return this;
    }
    /**
     * Returns the estimated length in millimeters
     *
     * @return the estimated length in millimeters
     */
    public int length() {
        return this.length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("Length: ").append(length).append(" ");
        return sb.toString();
    }
}
