import java.util.Random;

/**
 * Represents one instance of a hive<br>
 * --- Invariants ---
 * <ul>
 *     <li>0 <= <code>population</code></li>
 *     <li>0 <= <code>foodReserves</code></li>
 * </ul>
 */

public class Hive {
	private final PollinatorSpecies params;
	private double population;
	private double foodReserves;
	private final double hiveCapacity;

	private static final double STARVATION_SEVERITY = 0.2;

	/**
	 * Initializes a <code>Hive</code> object.
	 *
	 * <p><b>Preconditions</b>
	 * <ul>
	 *   <li>availableFood >= 0</li>
	 *   <li>Weather != null</li>
	 *   <li>0 <= day <= Simulation.DAYS_TO_SIMULATE</li>
	 *   <li>rng != null</li>
	 * </ul>
	 *
	 * <p><b>Postconditions</b>
	 * <ul>
	 *   <li>hiveCapacity is a function of initialPopulation</li>
	 * </ul>
	 *
	 * @param params            PollinatorSpecies of this Hive
	 * @param initialPopulation initial population of this Hive
	 * @param initialFood       initial foodReserves of this Hive
	 */
	//STYLE: OOP -> object initialization via constructor
	public Hive(PollinatorSpecies params, double initialPopulation, double initialFood) {
		this.params = params;
		this.population = initialPopulation;
		this.foodReserves = initialFood;
		hiveCapacity = initialPopulation * 500;
	}

	/**
	 * Applies daily updates to population and foodReserves.
	 * <p><b>Preconditions</b>
	 * <ul>
	 *   <li>availableFood >= 0</li>
	 *   <li>Weather != null</li>
	 *   <li>0 <= day <= Simulation.DAYS_TO_SIMULATE</li>
	 *   <li>rng != null</li>
	 * </ul>
	 *
	 * @param availableFood number of blooming compatible flowers
	 * @param weather       current Weather
	 * @param day           current day
	 * @param rng           given Random object
	 */
	//STYLE: OOP interface -> hides implementation from other classes, procedural implementation -> changes state directly
	public void updateDay(double availableFood, Weather weather, int day, Random rng) {
		if (population <= 0.0) return;

		//no collecting when raining or inactive or temp out of favourable range
		if (weather.isRaining() || !params.isActiveOn(day)
				|| weather.getTemperature() < params.getTemperatureMin()
				|| weather.getTemperature() > params.getTemperatureMax()) {
			availableFood = 0.0;
		}
		availableFood = Math.max(0.0, availableFood);

		// add fresh nectar to reserves (so reproduction uses reserves + fresh)
		foodReserves += availableFood;

		// protect tiny populations
		double births = getBirths(rng);

		population += births;

		// daily food consumption
		double maintenance = params.getFoodPerOffspring() * population;

		if (maintenance <= foodReserves) {
			// consume maintenance need from reserves
			foodReserves -= maintenance;
		} else {
			// reduce population proportionally to shortage fraction
			double shortageRatio = Math.min(1.0, maintenance - foodReserves / (maintenance + 1e-12)); //BAD: procedural -> unclear expression at first sight
			population *= Math.max(0.0, 1.0 - STARVATION_SEVERITY * shortageRatio);
			// retain small fraction of reserves
			foodReserves *= 0.1;
		}

		// natural daily mortality
		population *= Math.max(0.0, 1.0 - params.getMortalityRate());

		// sanity clamps
		if (!Double.isFinite(population) || population < 0.0) population = 0.0;
		if (!Double.isFinite(foodReserves) || foodReserves < 0.0) foodReserves = 0.0;
	}

	/**
	 * Calculates and returns number of newborn pollinators
	 *
	 * <p><b>Preconditions</b>:
	 * <ul>
	 *  <li>rng != null</li>
	 * </ul>
	 *
	 * <p><b>Postconditions</b>
	 * <ul>
	 *     <li>returned value is a function of foodReserves and population</li>
	 * </ul>
	 *
	 * @param rng given Random object
	 * @return number of newborn pollinators
	 */
	//STYLE: OOP interface -> hides implementation from other classes, procedural implementation -> changes state directly
	private double getBirths(Random rng) {

		double N = Math.max(population, 1e-12);

		double availablePerCapita = foodReserves / N;

		// per-capita births modulated by food and density-dependence
		double foodFactor = availablePerCapita / (availablePerCapita + params.getFoodPerOffspring() + 1e-12); //BAD: procedural -> unclear role of constant on first sight

		double densityFactor = 1.0 - (N / hiveCapacity);
		if (densityFactor < 0.0) densityFactor = 0.0;

		double perCapitaBirthRate = params.getBaseReproductionRate() * foodFactor * densityFactor;
		double noise = rng.nextGaussian(0, 0.4);
		perCapitaBirthRate *= Math.max(0.0, 1 + noise);
		if (perCapitaBirthRate < 0.0) perCapitaBirthRate = 0.0;

		return N * perCapitaBirthRate;
	}


	/**
	 * Starts the season for this Hive. <br>
	 * <p><b>Postconditions</b>
	 * <ul>rng != null</ul>
	 *
	 * <p><b>Postconditions</b>
	 * <ul>
	 *   <li>0 <= new population >= old population * 0.15</li>
	 *   <li>0 <= new foodReserves <= 0.33 * old foodReserves</li>
	 * </ul>
	 *
	 * @param rng given Random object
	 */
	//STYLE: OOP interface -> hides implementation from other classes, procedural implementation -> changes state directly
	public void startSeason(Random rng) {
		if (population <= 0) {
			return;
		}

		double winterSurvival = (foodReserves / population) * (population > 20 ? 1 : 0.5);
		winterSurvival = Math.max(winterSurvival, 0.2);
		population = population * winterSurvival * rng.nextDouble(0.75, 1.25);
		foodReserves = Math.max(foodReserves * 0.33, 0);
	}

	/**
	 * Generates a copy of this hive
	 *
	 * @return deep copy of this object
	 */
	//STYLE: OOP -> creates copy of object
	public Hive copy() {
		return new Hive(params, population, foodReserves);
	}

	/**
	 * @return current <code>population</code> of this Hive
	 */
	//STYLE: OOP -> getter methods
	public double getPopulation() {
		return population;
	}

	/**
	 * @return PollinatorSpecies of this Hive
	 */
	public PollinatorSpecies getSpecies() {
		return params;
	}
}
