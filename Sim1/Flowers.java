import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Container of FlowerPatch objects.
 * <p><b>--- Invariants ---</b>
 * <ul>
 *     <li>flowers != null</li>
 *     <li>each flowers[i] satisfies FlowerPatch invariants</li>
 * </ul>
 */
//BAD: object coupling -> slightly tight object coupling, if we were to introduce a flower field rather than a patch, we cannot directly introduce it to this class
public class Flowers {
	private final FlowerPatch[] flowers;
	private final Random rng;

	/**
	 * Create a Flowers container able to store {@code length} FlowerPatch entries with a given Random object.
	 *
	 * <p><b>Preconditions</b>
	 * <ul>
	 *     <li>length > 0</li>
	 *     <li>rng != null</li>
	 * </ul>
	 *
	 * <p><b>Postconditions</b>
	 * <ul>
	 *     <li>size() = length</li>
	 *     <li>all slots initially null</li>
	 * </ul>
	 *
	 * @param length number of FlowerPatch objects to be stored
	 * @param rng 	 given Random object
	 */
	public Flowers(int length, Random rng) {
		flowers = new FlowerPatch[length];
		this.rng = rng;
	}

	/**
	 * Create a Flowers object initialized with FlowerPatch objects and a given Random object.
	 *
	 * <p><b>Preconditions</b>
	 * <ul>
	 *     <li>flowerPatches != null</li>
	 *     <li>rng != null</li>
	 * </ul>
	 *
	 * <p><b>Postconditions</b>
	 * <ul>
	 *     <li>size() = length</li>
	 *     <li>flower[i] is a copy of flowerPatches[i]</li>
	 * </ul>
	 *
	 * @param flowerPatches array of FlowerPatch objects to be stored
	 * @param rng 	 		given Random object
	 */
	public Flowers(FlowerPatch[] flowerPatches, Random rng) {
		flowers = new FlowerPatch[flowerPatches.length];

		int index = 0;

		for (FlowerPatch fp : flowerPatches) {
			flowers[index] = fp.copy();
			index++;
		}
		this.rng = rng;
	}

	/**
	 * Set a FlowerPatch instance into the container at given index.
	 *
	 * <p><b>Preconditions</b>
	 * <ul>
	 *     <li>0 <= index < size()</li>
	 *     <li>flowerPatch != null</li>
	 * </ul>
	 *
	 * <p><b>Postconditions</b>
	 * <ul>
	 *     <li>flowers[index] = flowerPatch</li>
	 *     <li>other slots unchanged</li>
	 * </ul>
	 *
	 * @param index       position
	 * @param flowerPatch non-null FlowerPatch instance
	 */
	public void set(int index, FlowerPatch flowerPatch) {
		flowers[index] = flowerPatch;
	}

	/**
	 * Starts the season for each FlowerPatch object.
	 */
	public void startSeason() {
		for (FlowerPatch f : flowers) {
			f.startSeason(rng);
		}
	}

	/**
	 * Applies daily updates for each <code>FlowerPatch</code> object.
	 *
	 * <p><b>Preconditions</b>
	 * <ul>
	 *     <li>pAvail != null</li>
	 *     <li>weather != null</li>
	 *     <li>0 <= day <= Simulation.DAYS_TO_SIMULATE</li>
	 * </ul>
	 *
	 * @param weather current weather != null
	 * @param pAvail  available pollinators per FlowerSpecie
	 */
	public void updateDay(Map<String, Double> pAvail, Weather weather, int day) {
		double competition = calculateCompetition();
		for (FlowerPatch f : flowers) {
			double pollinators = 0.0;
			if (pAvail.containsKey(f.getSpecies().getID())) {
				pollinators = pAvail.get(f.getSpecies().getID());
			}
			f.updateDay(pollinators, weather, competition, day);
		}
	}

	/**
	 * Calculates and returns the total food supply of all stored <code>FlowerPatch</code> objects.
	 *
	 * <p><b>Postconditions</b>
	 * <ul>
	 *     <li>returned value >= 0</li>
	 * </ul>
	 *
	 * @return total food supply of all FlowerPatches
	 */
	public double calculateCompetition() {
		double competition = 0;
		for (FlowerPatch f : flowers) {
			competition += f.calculateCompetition();
		}
		return competition;
	}

	/**
	 * For every PollinatorSpecies.ID in f2pCompat counts sum of current blooming population of FlowerPatches who's
	 * FLowerSpecies.ID is contained in the Set of f2pCompat, this data is then stored in fAvail
	 *
	 * <p><b>Preconditions</b>
	 * <ul>
	 *     <li>fAvail != null</li>
	 *     <li>every FlowerSpecies.ID of a FlowerPatch contained in this Flowers object is contained as a key in f2pCompat</li>
	 *     <li>f2pCompat != null</li>
	 * </ul>
	 *
	 * <p><b>Postconditions</b>
	 * <ul>
	 *     <li>every Double value in fAvail >= 0.0</li>
	 * </ul>
	 *
	 * @param fAvail    map, in which to store resulting data
	 * @param f2pCompat compatibility map containing info about which FlowerSpecies gets pollinated by which PollinatorSpecies
	 */
	// ERROR: assumes f2pCompat contains a mapping for every FlowerSpecies.ID.
	// If a key is missing, f2pCompat.get(...) will return null and this will throw NPE.
	public void countAvailability(Map<String, Double> fAvail, Map<String, Set<String>> f2pCompat) {
		for (FlowerPatch f : flowers) {
			for (String pSpecies : f2pCompat.get(f.getSpecies().getID())) {
				double currValue = 0.0;
				if (fAvail.containsKey(pSpecies)) {
					currValue = fAvail.get(pSpecies);
				}
				fAvail.put(pSpecies, currValue + f.calculateCompetition());
			}
		}
	}

	/**
	 * Collects and outputs the growth strength, bloom rate and seed quality of each stored <code>FlowerPatch</code> object.
	 *
	 * <p><b>Postconditions</b>
	 * <ul>
	 *     <li>returned array has length = size()</li>
	 * </ul>
	 *
	 * @return array of [growth strength, bloom rate, seed quality] arrays, representing all FlowerPatch objects
	 */
	public double[][] outputData() {
		double[][] flowerData = new double[size()][];
		for (int i = 0; i < size(); i++) {
			flowerData[i] = flowers[i].outputData();
		}
		return flowerData;
	}

	/**
	 * Tallies populations by specie.
	 *
	 * <p><b>Postconditions</b>
	 * <ul>
	 *     <li>returned map has all species that were present in initialization of this Flower object</li>
	 * </ul>
	 *
	 * @return map, that maps FlowerSpecies.ID to Double, representing the current population
	 */
	public Map<String, Double> getPopulationBySpecies() {
		Map<String, Double> popBySpecies = new LinkedHashMap<>();
		for (FlowerPatch f : flowers) {
			String fID = f.getSpecies().getID();
			popBySpecies.put(fID, popBySpecies.getOrDefault(fID, 0.0) + f.getGrowthStrength());
		}
		return popBySpecies;
	}

	/**
	 * @return number of stored <code>FlowerPatch</code> objects
	 */
	public int size() {
		return flowers.length;
	}

	/**
	 * Generates a deep copy of this Flowers instance
	 *
	 * <p><b>Preconditions</b>
	 * <ul>
	 *     <li>rngFactory != null</li>
	 * </ul>
	 *
	 * @param rngFactory uses simulation rng for consistent behaviour
	 * @return deep copy of this object
	 */
	//STYLE: OOP -> creates copy of object
	public Flowers copy(Random rngFactory) {
		Flowers copy = new Flowers(size(), new Random(rngFactory.nextLong()));
		for (int i = 0; i < flowers.length; i++) {
			copy.set(i, flowers[i].copy());
		}
		return copy;
	}
}
