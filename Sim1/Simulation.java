import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Container managing the execution of a simulation run.
 * <p><b>--- Invariants ---</b>
 * <ul>
 *     <li>simulationYears >= 0</li>
 *     <li>weather != null</li>
 *     <li>pollinators != null</li>
 *     <li>flowers != null</li>
 *     <li>viewer != null</li>
 * </ul>
 */
public class Simulation {
	private final int simulationYears;
	private final Weather weather;
	private final Pollinators pollinators;
	private final Flowers flowers;
	private final Map<String, Set<String>> pollinatorToFlowerCompatibility;
	private final Map<String, Set<String>> flowerToPollinatorCompatibility;
	private final Viewer viewer;

	public static final int DAYS_TO_SIMULATE = 240;
	/**
	 * Creates a Simulation object, which simulates the interaction between pollinator and flower populations,
	 * and reacting to weather condition.
	 *
	 * <p><b>Preconditions</b>
	 * <ul>
	 *     <li>simulationYears >= 0</li>
	 *     <li>pollinators != null</li>
	 *     <li>flowers != null</li>
	 *     <li>weather != null</li>
	 *     <li>viewer != null</li>
	 *     <li>all FlowerSpecies contained in Flowers must also be present in flowerToPollinatorCompatibility.keySet()</li>
	 *     <li>all PollinatorSpecies contained in Pollinators must also be present in pollinatorToFlowerCompatibility.keySet()</li>
	 * </ul>
	 *
	 * @param simulationYears					desired simulation duration in years
	 * @param pollinators						initial pollinators
	 * @param flowers							initial flowerPatches
	 * @param weather							initial weather
	 * @param viewer							object extracting data
	 * @param pollinatorToFlowerCompatibility	map about which PollinatorSpecie pollinates which FlowerSpecies
	 * @param flowerToPollinatorCompatibility 	map about which FlowerSpecie gets pollinated by which PollinatorSpecies
	 */
	// ERROR: The constructor documents compatibility-key requirements but does not validate them.
	public Simulation(int simulationYears, Pollinators pollinators, Flowers flowers, Weather weather, Viewer viewer,
					  Map<String, Set<String>> pollinatorToFlowerCompatibility, Map<String, Set<String>> flowerToPollinatorCompatibility) {
		this.simulationYears = simulationYears;
		this.pollinators = pollinators;
		this.flowers = flowers;
		this.weather = weather;
		this.viewer = viewer;
		this.pollinatorToFlowerCompatibility = pollinatorToFlowerCompatibility;
		this.flowerToPollinatorCompatibility = flowerToPollinatorCompatibility;
	}

	/**
	 * Execute the simulation run
	 */
	public void simulateTotal() {
		for (int currentYear = 0; currentYear < simulationYears; currentYear++) {
			simulateYear(currentYear);
			viewer.computeYearlyStats();
		}
	}

	/**
	 * Simulates a year
	 *
	 * <p><b>Preconditions</b>
	 * <ul>
	 *     <li>0 <= currentYear < simulationYears</li>
	 * </ul>
	 *
	 * @param currentYear current year of the simulation
	 */

	//STYLE: procedural style -> problem is broken down into smaller ones (days). execution of those complete the whole simulation year
	private void simulateYear(int currentYear) {

		//initializes season
		weather.startSeason();
		if (currentYear > 0) {
			pollinators.startSeason();
			flowers.startSeason();
		}

		//simulating year
		for (int currentDay = 0; currentDay < DAYS_TO_SIMULATE; currentDay++) {
			simulateDay(currentDay);
			viewer.feed(pollinators.getPopulationBySpecies(), flowers.getPopulationBySpecies());
		}
	}

	/**
	 * Simulates a single day
	 *
	 * <p><b>Preconditions</b>
	 * <ul>
	 *     <li>0 <= day <= DAYS_TO_SIMULATE</li>
	 * </ul>
	 *
	 * @param day current day
	 */
	//GOOD: procedural -> structured code, no aliases
	private void simulateDay(int day) {

		//fetch current populations
		Map<String, Double> pollinatorAvailabilityPerFlowerSpecie = new HashMap<>();
		Map<String, Double> flowerAvailabilityPerPollinatorSpecie = new HashMap<>();

		pollinators.countAvailability(pollinatorAvailabilityPerFlowerSpecie, pollinatorToFlowerCompatibility);
		flowers.countAvailability(flowerAvailabilityPerPollinatorSpecie, flowerToPollinatorCompatibility);

		//daily update of data
		weather.updateDay(day);
		flowers.updateDay(pollinatorAvailabilityPerFlowerSpecie, weather, day);
		pollinators.updateDay(flowerAvailabilityPerPollinatorSpecie, weather, day);
	}

	/**
	 * @return Viewer object
	 */
	public Viewer getViewer(){
		return viewer;
	}
}
