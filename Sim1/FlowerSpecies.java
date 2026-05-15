public class FlowerSpecies {
    // --- Immutable specie fields ---
    private final String ID;

    private final double reproductionBoundMin, reproductionBoundMax;
    private final double moistureMin, moistureMax;
    private final int bloomingStartDay, bloomingEndDay;
    private final double bloomIntensity;
    private final double pollinationProbability;
    private final double temperatureMin,  temperatureMax;

    //speed of ending bloom over speed of setting bloom: 0 < DECAY_FACTOR < 1
    public static final double DECAY_FACTOR = 0.3;

    /**
     * Initializes a <code>FlowerSpecies</code> object
     *
     * <p><b>Preconditions</b>
     * <ul>
     *   <li>ID != null</li>
     *   <li>ID.isEmpty() = false</li>
     *   <li>0 < reproductionBoundMin < reproductionBoundMax</li>
     *   <li>0 < moistureMin < moistureMax < 1</li>
     *   <li>0 <= bloomingStartDay <= bloomingEndDay <= Simulation.DAYS_TO_SIMULATE</li>
     *   <li>0 < bloomIntensity < 1/15</li>
     *   <li>0 < pollinationProbability < 1 / (bloomingEndDay - bloomingStartDay)</li>
     *   <li>temperatureMin <= temperatureMax</li>
     * </ul>
     *
     * @param ID                     identifier
     * @param reproductionBoundMin   lower rest-phase reproduction bound
     * @param reproductionBoundMax   upper rest-phase reproduction bound
     * @param moistureMin            lower moisture bound
     * @param moistureMax            upper moisture bound
     * @param bloomingStartDay       lower bloom bound in days
     * @param bloomingEndDay         upper bloom bound in days
     * @param bloomIntensity         speed of the bloom setting in
     * @param pollinationProbability probability of seed development increasing for blooming flowers every sun hour
     * @param temperatureMin         lower favourable temperature
     * @param temperatureMax         upper favorable temperature
     */

    //STYLE: OOP -> creates an immutable object via constructor
    public FlowerSpecies(String ID, double reproductionBoundMin, double reproductionBoundMax, double moistureMin, double moistureMax,
                         int bloomingStartDay, int bloomingEndDay, double bloomIntensity, double pollinationProbability, double temperatureMin, double temperatureMax) {
        if (ID == null || ID.isEmpty()) throw new IllegalArgumentException("ID can't be null or empty");
        if (!Double.isFinite(reproductionBoundMin) || !Double.isFinite(reproductionBoundMax) ||
                !(0.0 < reproductionBoundMin && reproductionBoundMin <= reproductionBoundMax)) {
            throw new IllegalArgumentException("0 < reproductionBoundMin <= reproductionBoundMax");
        }
        if (!Double.isFinite(moistureMin) || !Double.isFinite(moistureMax) ||
                !(0.0 < moistureMin && moistureMin < moistureMax && moistureMax < 1.0)) {
            throw new IllegalArgumentException("0 < moistureMin < moistureMax < 1 (moisture bounds)");
        }
        if (!(0 <= bloomingStartDay && bloomingStartDay < bloomingEndDay && bloomingEndDay <= Simulation.DAYS_TO_SIMULATE)) {
            throw new IllegalArgumentException("0 <= bloomingStartDay < bloomingEndDay <= Simulation.DAYS_TO_SIMULATE (bloom thresholds)");
        }
        if (!Double.isFinite(bloomIntensity) ||
                !(0.0 < bloomIntensity && bloomIntensity < (1.0 / 15.0))) {
            throw new IllegalArgumentException("0 < bloomIntensity < 1/15 (bloom intensity)");
        }
        if (!Double.isFinite(pollinationProbability) ||
                !(0.0 < pollinationProbability && pollinationProbability < 1.0 / (bloomingEndDay - bloomingStartDay))) {
            throw new IllegalArgumentException("0 < pollinationProbability < 1/(bloomingEndDay - bloomingStartDay)");
        }

        this.ID = ID;
        this.reproductionBoundMin = reproductionBoundMin;
        this.reproductionBoundMax = reproductionBoundMax;
        this.moistureMin = moistureMin;
        this.moistureMax = moistureMax;
        this.bloomingStartDay = bloomingStartDay;
        this.bloomingEndDay = bloomingEndDay;
        this.bloomIntensity = bloomIntensity;
        this.pollinationProbability = pollinationProbability;
        this.temperatureMin = temperatureMin;
        this.temperatureMax = temperatureMax;
    }

    //STYLE: OOP -> creates an immutable object via constructor
    public FlowerSpecies(FlowerSpecies params) {
        this.ID = params.ID;
        this.reproductionBoundMin = params.reproductionBoundMin;
        this.reproductionBoundMax = params.reproductionBoundMax;
        this.moistureMin = params.moistureMin;
        this.moistureMax = params.moistureMax;
        this.bloomingStartDay = params.bloomingStartDay;
        this.bloomingEndDay = params.bloomingEndDay;
        this.bloomIntensity = params.bloomIntensity;
        this.pollinationProbability = params.pollinationProbability;
        this.temperatureMin = params.temperatureMin;
        this.temperatureMax = params.temperatureMax;
    }

    //STYLE: OOP -> getter methods
    public String getID() { return ID; }

    public double getReproductionBoundMin() { return reproductionBoundMin; }

    public double getReproductionBoundMax() { return reproductionBoundMax; }

    public double getMoistureMin() { return moistureMin; }

    public double getMoistureMax() { return moistureMax; }

    public int getBloomingStartDay() { return bloomingStartDay; }

    public int getBloomingEndDay() { return bloomingEndDay; }

    public double getBloomIntensity() { return bloomIntensity; }

    public double getPollinationProbability() { return pollinationProbability; }

    public double getTemperatureMin() { return temperatureMin; }

    public double getTemperatureMax() { return temperatureMax; }
}
