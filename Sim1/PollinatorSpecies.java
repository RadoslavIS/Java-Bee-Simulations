public class PollinatorSpecies {
    // --- Immutable specie fields ---
    private final String ID;

    private final double baseReproductionRate;
    private final double mortalityRate;
    private final double foodPerOffspring;
    private final int activeStartDay, activeEndDay;
    private final double temperatureMin, temperatureMax;

    /**
     * Initializes a <code>PollinatorSpecies</code> object.
     *
     * <p><b>Preconditions</b>
     * <ul>
     *   <li>ID != null</li>
     *   <li>ID.isEmpty() = false</li>
     *   <li>baseReproductionRate >= 0</li>
     *   <li>0 <= mortalityRate <= 1</li>
     *   <li>foodPerOffspring > 0</li>
     *   <li>0 <= activeStartDay <= activeEndDay <= Simulation.DAYS_TO_SIMULATE</li>
     *   <li>temperatureMin <= temperatureMax</li>
     * </ul>
     *
     * @param ID                        identifier
     * @param baseReproductionRate      offspring per adult per suitable season-day (baseline)
     * @param mortalityRate             probability per time-step (day) for an adult to die
     * @param foodPerOffspring          how much food one population unit consumes
     * @param activeStartDay            day of year when specie starts activity
     * @param activeEndDay              day of year when species ends activity
     * @param temperatureMin            minimum favourable temperature
     * @param temperatureMax            maximum favourable temperature
     */
    //STYLE: OOP -> creates an immutable object via constructor
    public PollinatorSpecies(String ID, double baseReproductionRate, double mortalityRate, double foodPerOffspring,
                             int activeStartDay, int activeEndDay, double temperatureMin, double temperatureMax) {
        if (ID == null) throw new IllegalArgumentException("ID can't be null or empty");

        if (!Double.isFinite(baseReproductionRate) || baseReproductionRate < 0.0) {
            throw new IllegalArgumentException("baseReproductionRate must be finite and >= 0");
        }
        if (!Double.isFinite(mortalityRate) || mortalityRate < 0.0 || mortalityRate > 1.0) {
            throw new IllegalArgumentException("mortalityRate must be finite and in [0,1]");
        }
        if (!Double.isFinite(foodPerOffspring) || foodPerOffspring <= 0.0) {
            throw new IllegalArgumentException("foodPerOffspring must be finite and > 0");
        }

        if (activeStartDay < 0 || activeStartDay > Simulation.DAYS_TO_SIMULATE) {
            throw new IllegalArgumentException("activeStartDay must be in 0..DAYS_TO_SIMULATE");
        }
        if (activeEndDay < 0 || activeEndDay > Simulation.DAYS_TO_SIMULATE) {
            throw new IllegalArgumentException("activeEndDay must be in 0..DAYS_TO_SIMULATE");
        }

        // temperature checks
        if (!Double.isFinite(temperatureMin) || !Double.isFinite(temperatureMax)) {
            throw new IllegalArgumentException("temperatureMin/Max must be finite");
        }
        if (temperatureMax < temperatureMin) {
            throw new IllegalArgumentException("temperatureMax must be >= temperatureMin");
        }

        this.ID = ID;
        this.baseReproductionRate = baseReproductionRate;
        this.mortalityRate = mortalityRate;
        this.foodPerOffspring = foodPerOffspring;
        this.activeStartDay = activeStartDay;
        this.activeEndDay = activeEndDay;
        this.temperatureMin = temperatureMin;
        this.temperatureMax = temperatureMax;
    }

    //STYLE: OOP interface -> hides implementation from other classes
    // checks if species is active on this day
    public boolean isActiveOn(int day) {
        return activeStartDay <= day && day <= activeEndDay;
    }

    //STYLE: OOP -> getter methods
    public String getID() {
        return ID;
    }

    public double getBaseReproductionRate() {
        return baseReproductionRate;
    }

    public double getMortalityRate() {
        return mortalityRate;
    }

    public double getFoodPerOffspring() {
        return foodPerOffspring;
    }

    public double getTemperatureMin() {
        return temperatureMin;
    }

    public double getTemperatureMax() {
        return temperatureMax;
    }
}
