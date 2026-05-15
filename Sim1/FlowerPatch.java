import java.util.Random;

/**
 * Represents one instance of a flower patch<br>
 * --- Invariants ---
 * <ul>
 *     <li>0 <= <code>growthStrength</code></li>
 *     <li>0 <= <code>bloomRate</code> <= 1</li>
 *     <li>0 <= <code>seedQuality</code> <= 1</li>
 * </ul>
 */

public class FlowerPatch {
	/**
	 * Contains all parameters, affecting the flower patch's behaviour, saved as FlowerSpecies
	 */
	private final FlowerSpecies params;

	private double growthStrength;
	private double bloomRate;
	private double seedQuality;
	private int health;

	/**
	 * Initializes a <code>FlowerPatch</code> object.
	 *
	 * <p><b>Preconditions</b>
	 * <ul>
	 *   <li>initGrowthStrength >= 0</li>
	 *   <li>params != null</li>
	 * </ul>
	 *
	 * <p><b>Postconditions</b>
	 * <ul>
	 *     <li>health = 100</li>
	 * </ul>
	 *
	 * @param initGrowthStrength initial number of flowers in this FlowerPatch
	 * @param params             FlowerSpecies of this FlowerPatch
	 */
	//STYLE: OOP -> object initialization via constructor
	public FlowerPatch(double initGrowthStrength, FlowerSpecies params) {
		if (!Double.isFinite(initGrowthStrength) || initGrowthStrength < 0.0) {
			throw new IllegalArgumentException("initGrowthStrength must be finite and >= 0");
		}
		if (params == null) {
			throw new NullPointerException("Species object can't be null");
		}

		this.growthStrength = initGrowthStrength;
		this.params = params;
		this.health = 100;
	}

	/**
	 * Apply all daily updates to this flower: moisture, temperature, bloom change and seed-quality accumulation.
	 *
	 * <p><b>Preconditions</b>
	 * <ul>
	 *   <li>pollinators >= 0</li>
	 *   <li>weather != null</li>
	 *   <li>foodSupply >= 0</li>
	 *   <li>0 <= day <= Simulation.DAYS_TO_SIMULATE</li>
	 * </ul>
	 *
	 * @param pollinators population of compatible pollinators
	 * @param weather     current Weather
	 * @param foodSupply  total number of blooming flowers
	 * @param day         current day
	 */
	//STYLE: OOP interface -> hides implementation from other classes, procedural -> structured calls to other procedures
	public void updateDay(double pollinators, Weather weather, double foodSupply, int day) {
		if (health > 80) {
			growthStrength *= 1.002;
		}
		applyMoisture(weather.getHumidity());
		applyTemperature(weather.getTemperature());
		updateBloom(day, weather.getSunlight());
		updateSeedQuality(pollinators, foodSupply, weather.getSunlight());
	}

	/**
	 * Apply daily moisture effect to health and growthStrength.
	 *
	 * <p><b>Preconditions</b>
	 * <ul>
	 *   <li>0 <= moisture <= 1</li>
	 * </ul>
	 *
	 * <p><b>Postconditions</b>
	 * <ul>
	 *   <li>if moisture is in favorable range, health is increased</li>
	 *   <li>if moisture is out of favorable range, health is decreased stepwise depending on unfavorability of moisture</li>
	 *   <li>if health is 0, growthStrength is lowered</li>
	 *   <li>growthStrength >= 0.0</li>
	 *   <li>0 <= health <= 100</li>
	 * </ul>
	 *
	 * @param moisture current moisture
	 */
	//STYLE: OOP interface -> hides implementation from other classes, procedural implementation -> changes state directly
	private void applyMoisture(double moisture) {
		if (health == 0) {
			growthStrength *= 0.9;
			return;
		}

		if ((params.getMoistureMin() / 2.0 < moisture && moisture < params.getMoistureMin()) ||
				(params.getMoistureMax() < moisture && moisture < 2.0 * params.getMoistureMax())) {
			health -= 5;
		} else if (moisture <= params.getMoistureMin() / 2.0 || 2.0 * params.getMoistureMax() <= moisture) {
			health -= 15;
		} else {
			health += 10;
		}

		health = Math.clamp(health, 0, 100);
	}

	/**
	 * Apply daily temperature effect to health and growthStrength.
	 *
	 * <p><b>Preconditions</b>
	 * <ul>
	 *   <li>temperature is a valid double</li>
	 * </ul>
	 *
	 * <p><b>Postconditions</b>
	 * <ul>
	 *   <li>if temperature is in favorable range, health is increased</li>
	 *   <li>if temperature is out of favorable range, health is decreased</li>
	 *   <li>if health is 0, growthStrength is lowered</li>
	 *   <li>growthStrength >= 0.0</li>
	 *   <li>0 <= health <= 100</li>
	 * </ul>
	 *
	 * @param temperature current temperature
	 */
	//STYLE: OOP interface -> hides implementation from other classes, procedural implementation -> changes state directly
	private void applyTemperature(double temperature) {
		if (health == 0) {
			growthStrength *= 0.9;
			return;
		}

		if (temperature < params.getTemperatureMin() || temperature > params.getTemperatureMax()) {
			health -= 10;
		} else {
			health += 10;
		}

		health = Math.clamp(health, 0, 100);
	}


	/**
	 * Update bloomRate according to current day and sunlight.
	 *
	 * <p><b>Preconditions</b>
	 * <ul>
	 *   <li>0 <= day <= Simulation.DAYS_TO_SIMULATE</li>
	 *   <li>0 <= effectiveSunlight <= 24</li>
	 * </ul>
	 *
	 * <p><b>Postconditions</b>
	 * <ul>
	 *   <li>if health is 0, bloomRate remains unchanged</li>
	 *   <li>if day is in range of bloom, bloomRate increases, according to health and sunlight</li>
	 *   <li>if day is out of range of bloom, bloomRate decreases, according to health and sunlight</li>
	 *   <li>0 <= bloomRate <= 1</li>
	 * </ul>
	 *
	 * @param day               simulation day
	 * @param effectiveSunlight effective sunlight hours
	 */
	//STYLE: OOP interface -> hides implementation from other classes, procedural implementation -> changes state directly
	private void updateBloom(int day, double effectiveSunlight) {
		if (health == 0) return;

		double healthFactor = health / 100.0;
		double effectiveRate = params.getBloomIntensity() * effectiveSunlight * healthFactor;

		// determine if we are inside the flowering window (handles wrap-around)
		boolean inWindow = (day >= params.getBloomingStartDay() && day <= params.getBloomingEndDay());
		double target = inWindow ? 1.0 : 0.0;

		// decrease is slower than increase
		if (!inWindow) effectiveRate *= FlowerSpecies.DECAY_FACTOR;

		// slowly move bloom towards target
		bloomRate += effectiveRate * (target - bloomRate);
		bloomRate = Math.clamp(bloomRate, 0.0, 1.0);
	}

	/**
	 * Update seedQuality based on pollination.
	 *
	 * <p><b>Preconditions</b>
	 * <ul>
	 *   <li>population >= 0</li>
	 *   <li>foodSupply >= 0</li>
	 *   <li>0 <= currentSunlight <= 24</li>
	 * </ul>
	 *
	 * <p><b>Postconditions</b>
	 * <ul>
	 *   <li>if bloomRate <= 0.0 or foodSupply < 1e-12 (avoids near-0 division), seedQuality remains unchanged </li>
	 *   <li>else seedQuality increases, according to available pollinators, competition and bloomRate</li>
	 *   <li>new seedQuality >= old seedQuality</li>
	 *   <li>0 <= seedQuality <= 1</li>
	 * </ul>
	 *
	 * @param population      population of compatible pollinators
	 * @param foodSupply      total number of blooming flowers
	 * @param currentSunlight effective sunlight hours
	 */
	//STYLE: OOP interface -> hides implementation from other classes, procedural implementation -> changes state directly
	private void updateSeedQuality(double population, double foodSupply, double currentSunlight) {
		if (health < 50 || bloomRate <= 0.0 || foodSupply <= 1e-12) { //BAD: procedural -> foodSupply being compared to 1e-12 may cause confusion
			return;
		}

		double incFactor = params.getPollinationProbability() * bloomRate * (currentSunlight + 1.0);
		if (population < foodSupply) {
			incFactor *= population / foodSupply;
		}
		seedQuality += incFactor * health / 100;
		seedQuality = Math.clamp(seedQuality, 0.0, 1.0);


	}

	/**
	 * Return this flower's contribution to daily food supply.<br>
	 *
	 * <p><b>Postconditions</b>
	 * <ul>
	 *   <li>returned value equals growthStrength * bloomRate</li>
	 *   <li>returned value >= 0.0</li>
	 * </ul>
	 *
	 * @return food supply from this FlowerPatch
	 */
	//STYLE: OOP -> hides implementation from other classes
	public double calculateCompetition() {
		return growthStrength * bloomRate;
	}

	/**
	 * Starts the season for this FlowerPatch. <br>
	 * <p><b>Postconditions</b>
	 * <ul>
	 *   <li>growthStrength = growthStrength * seedQuality * r, where r from [cMinus, cPlus]</li>
	 *   <li>growthStrength >= 0</li>
	 *   <li>bloomRate = 0</li>
	 *   <li>seedQuality = 0</li>
	 * </ul>
	 *
	 * @param rng given Random object
	 */
	//STYLE: OOP interface -> hides implementation from other classes, procedural implementation -> changes state directly
	public void startSeason(Random rng) {
		//GOOD: procedural -> straightforward and readable calculation, intent and control flow is clear
		growthStrength = Math.max(0.0, growthStrength * seedQuality * rng.nextDouble(params.getReproductionBoundMin(), params.getReproductionBoundMax()));
		bloomRate = 0;
		seedQuality = 0;
	}

	/**
	 * Return [growthStrength, bloomRate, seedQuality]
	 *
	 * <p><b>Postconditions</b> returns a double array of length 3:
	 * <ul>
	 *   <li>arr[0] = growthStrength (>= 0)</li>
	 *   <li>arr[1] = bloomRate (0 <= bloomRate <= 1)</li>
	 *   <li>arr[2] = seedQuality (0 <= seedQuality <= 1)</li>
	 * </ul>
	 *
	 * @return array, containing <code>growth strength, bloom rate, seed quality</code>
	 */
	//STYLE: OOP -> getter methods
	public double[] outputData() {
		return new double[]{growthStrength, bloomRate, seedQuality};
	}

	/**
	 * @return current <code>growthStrength</code> of this FlowerPatch
	 */
	public double getGrowthStrength() {
		return growthStrength;
	}

	/**
	 * @return FlowerSpecies of this FlowerPatch
	 */
	public FlowerSpecies getSpecies() {
		return params;
	}

	/**
	 * Generates a deep copy of the flower
	 *
	 * @return deep copy of this object
	 */
	//STYLE: OOP -> creates copy of object
	public FlowerPatch copy() {
		FlowerPatch copy = new FlowerPatch(growthStrength, params);
		copy.bloomRate = bloomRate;
		copy.seedQuality = seedQuality;
		return copy;
	}

	/**
	 * Implements a custom <code>toString</code> method
	 */
	//STYLE: OOP -> toString implementation
	@Override
	public String toString() {
		return String.format("Flower[y=%.4f,b=%.4f,s=%.4f, f=[%.3f,%.3f], days active=[%d,%d]]",
				growthStrength, bloomRate, seedQuality, params.getMoistureMin(),
				params.getMoistureMax(), params.getBloomingStartDay(), params.getBloomingEndDay());
	}
}