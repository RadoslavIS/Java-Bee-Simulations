import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Container for all pollinator Hives.
 *
 * <p><b>--- Invariants ---</b>
 * <ul>
 *     <li>hives != null</li>
 * </ul>
 */

// BAD: object coupling -> slightly tight object coupling, if we were to introduce another species with the ability to pollinate, we cannot directly introduce it to this class
public class Pollinators {

	private final Hive[] hives;
	private final Random rng;

	/**
	 * Generates a <code>Pollinators</code> object with given Hives.
	 *
	 * <p><b>Preconditions</b>
	 * <ul>
	 *     <li>hives > 0</li>
	 * </ul>
	 *
	 * @param hives initial hives of pollinators
	 */
	public Pollinators(Hive[] hives) {
		this(hives, new Random());
	}

	/**
	 * Generates a <code>Pollinators</code> object with given Hives and a Random object.
	 *
	 * <p><b>Preconditions</b>
	 * <ul>
	 *     <li>hives > 0</li>
	 *     <li>rng != null</li>
	 * </ul>
	 *
	 * @param hives initial hives of pollinators
	 * @param rng   given Random object
	 */
	public Pollinators(Hive[] hives, Random rng) {
		if (hives == null) throw new IllegalArgumentException("hives can't be null");
		if (rng == null) throw new NullPointerException("rng can't be null");
		this.hives = hives;
		this.rng = rng;
	}

	/**
	 * Starts the season for each Hive object.
	 */
	public void startSeason() {
		for (Hive h : hives) {
			h.startSeason(rng);
		}
	}

	/**
	 * Applies daily updates for each <code>Hive</code> object.
	 *
	 * <p><b>Preconditions</b>
	 * <ul>
	 *     <li>fAvail != null</li>
	 *     <li>weather != null</li>
	 *     <li>0 <= day <= Simulation.DAYS_TO_SIMULATE</li>
	 * </ul>
	 *
	 * @param weather current weather != null
	 * @param fAvail  available flowers per PollinatorSpecie
	 */
	public void updateDay(Map<String, Double> fAvail, Weather weather, int day) {
		for (Hive h : hives) {
			double collectedFood = 0.0;
			if (fAvail.containsKey(h.getSpecies().getID())) {
				collectedFood = fAvail.get(h.getSpecies().getID());
			}
			h.updateDay(collectedFood, weather, day, rng);
		}
	}

	/**
	 * For every FlowerSpecies.ID in p2fCompat counts sum of current population of Hives who's
	 * FLowerSpecies.ID is contained in the Set of p2fCompat, this data is then stored in pAvail
	 *
	 * <p><b>Preconditions</b>
	 * <ul>
	 *     <li>pAvail != null</li>
	 *     <li>every PollinatorSpecies.ID of a Hive contained in this Pollinators object is contained as a key in p2fCompat</li>
	 *     <li>p2fCompat != null</li>
	 * </ul>
	 *
	 * <p><b>Postconditions</b>
	 * <ul>
	 *     <li>every Double value in pAvail >= 0.0</li>
	 * </ul>
	 *
	 * @param pAvail    map, in which to store resulting data
	 * @param p2fCompat compatibility map containing info about which PollinatorSpecies pollinates which FlowerSpecies
	 */
	// ERROR: assumes p2fCompat contains a mapping for every PollinatorSpecies.ID.
	// If a key is missing, p2fCompat.get(...) will return null and this will throw NPE.
	public void countAvailability(Map<String, Double> pAvail, Map<String, Set<String>> p2fCompat) {
		for (Hive h : hives) {
			for (String flowerSpecies : p2fCompat.get(h.getSpecies().getID())) {
				double currValue = 0;
				if (pAvail.containsKey(flowerSpecies)) {
					currValue = pAvail.get(flowerSpecies);
				}
				pAvail.put(flowerSpecies, currValue + h.getPopulation());
			}
		}
	}

	/**
	 * Generates a deep copy of this Pollinators instance
	 *
	 * <p><b>Preconditions</b>
	 * <ul>
	 *     <li>rngFactory != null</li>
	 * </ul>
	 *
	 * @param rngFactory uses simulation rng for consistent behaviour
	 * @return deep copy of this object
	 **/
	//STYLE: OOP -> creates copy of object
	public Pollinators copy(Random rngFactory) {
		Hive[] hivesCopy = new Hive[hives.length];
		for (int i = 0; i < hives.length; i++) {
			hivesCopy[i] = hives[i].copy();
		}
		return new Pollinators(hivesCopy, new Random(rngFactory.nextLong()));
	}

	/**
	 * <p><b>Postconditions</b>
	 * <ul>
	 *     <li>returned value >= 0</li>
	 * </ul>
	 *
	 * @return total population from all hives
	 */
	public double getPopulation() {
		double population = 0;
		for (Hive h : hives) {
			population += h.getPopulation();
		}
		return population;
	}

	/**
	 * Tallies populations by specie.
	 *
	 * <p><b>Postconditions</b>
	 * <ul>
	 *     <li>returned map has all species that were present in initialization of this Pollinator object</li>
	 * </ul>
	 *
	 * @return map, that maps PollinatorSpecies.ID to Double, representing the current population
	 */
	public Map<String, Double> getPopulationBySpecies() {
		Map<String, Double> popBySpecies = new LinkedHashMap<>();
		for (Hive h : hives) {
			String pID = h.getSpecies().getID();
			popBySpecies.put(pID, popBySpecies.getOrDefault(pID, 0.0) + h.getPopulation());
		}
		return popBySpecies;
	}

}