import java.util.*;

/**
 * Immutable configuration class for one simulation setup.
 */
public class SimulationConfig {
    private final String id;
    private final int years;

    private final Pollinators pollinators;
    private final Flowers flowers;

    private final Viewer viewer;

    // compatibility maps: pollinator ID -> set of flower IDs and vice-versa
    private final Map<String, Set<String>> pollinatorToFlowerCompatibility;
    private final Map<String, Set<String>> flowerToPollinatorCompatibility;

    public SimulationConfig(String id, int years,
                            Pollinators pollinators,
                            Flowers flowers,
                            Map<String, Set<String>> p2f,
                            Map<String, Set<String>> f2p) {
        this.id = id;
        this.years = years;
        this.pollinators = pollinators;
        this.flowers = flowers;
        this.viewer = new Viewer(p2f.keySet(), f2p.keySet());
        this.pollinatorToFlowerCompatibility = new LinkedHashMap<>(p2f);
        this.flowerToPollinatorCompatibility = new LinkedHashMap<>(f2p);
    }

    /** Generates an anonymous implementation of SimulationFactory that can create runnable simulations */
    // GOOD: OOP -> factory closure captures immutable config;
    public SimulationFactory toFactory() {
        return rng -> new Simulation(years, pollinators.copy(new Random(rng.nextLong())),
				flowers.copy(new Random(rng.nextLong())), new Weather(new Random(rng.nextLong())), viewer.copy(),
				new HashMap<>(pollinatorToFlowerCompatibility), new HashMap<>(flowerToPollinatorCompatibility));
    }

    public String getId(){
        return id;
    }
}
