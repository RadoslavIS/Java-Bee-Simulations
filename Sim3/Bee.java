import java.time.LocalDateTime;

/** Observation of a bee **/
public class Bee {
    private LocalDateTime time;
    private String description;
    private int marker;

    /**
     * Creates a new bee observation.
     * <p><b>Preconditions:</b>
     * <ul>
     *     <li>time != null</li>
     *     <li>description != null</li>
     *     <li>marker >= 0</li>
     * </ul>
     * @param time        the time of the observation
     * @param description short description about the observation
     * @param marker      a numerical identifier
     */
    public Bee(LocalDateTime time, String description, int marker) {
        this.time = time;
        this.description = description;
        this.marker = marker;
    }

    /**
     * Returns the time of the observation
     *
     * @return the time of the observation
     */
    public LocalDateTime getTime() {
        return time;
    }

    /**
     * Returns the numerical identifier of the observation
     *
     * @return the numerical identifier of the observation
     */
    public int getMarker() {
        return marker;
    }

    /**
     * Returns the description of the observation
     *
     * @return the description of the observation
     */
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Time of observation: ").append(time.toString()).append(" ");
        sb.append("Description: ").append(description).append(" ");
        sb.append("Marker: ").append(marker).append(" ");

        return sb.toString();
    }
}
