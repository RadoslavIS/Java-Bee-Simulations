import java.util.*;

/*
    --- Contributions (Part 1) ---
    Boris - Pollinators, Simulation, Flowers
    Daniel - Test, Weather
    Radoslav - FlowerPatch, FlowerSpecies, Documentation

    --- Contributions (Part 2) ---
    Boris - Weather, Pollinators, Test
    Daniel - Flowers, FlowerPatch, Test
    Radoslav - Hive, PollinatorSpecies, Simulation

    --- Contributions (Part 3) ---
    Boris - Viewer and Statistics and their integration into other classes,
    Documentation overhaul of FlowerSpecies, FlowerPatch, Flowers, PollinatorSpecies, Hives, Pollinators, Weather, Simulation,
    Daniel - SimulationResult, comments judging aspects of paradigms
    Radoslav - BatchSimulationRunner, SimulationTask, SimulationResult, SimulationFactory, SimulationConfig
 */
public class Test {

    public static void main(String[] args) throws InterruptedException{
        long baseSeed = 12345L;
        Random random = new Random(12345L);
        String p1ID = "Honeybee";
        String p2ID = "Bumblebee";
        String p3ID = "Carpenter Bee";

        String fp1ID = "Narcissus";
        String fp2ID = "Lavender";
        String fp3ID = "Christmas Rose";

        PollinatorSpecies p1 = new PollinatorSpecies(
                p1ID,
                1.8,
                0.2,
                0.5,
                5,
                240,
                8,
                25
        );

        PollinatorSpecies p2 = new PollinatorSpecies(
                p2ID,
                3.0,
                0.25,
                1.2,
                60,
                220,
                8,
                28
        );

        PollinatorSpecies p3 = new PollinatorSpecies(
                p3ID,
                2.0,
                0.15,
                0.8,
                100,
                227,
                12,
                30
        );

        FlowerSpecies f1 = new FlowerSpecies(
                fp1ID,
                0.85,
                0.9,
                0.2,
                0.85,
                0,
                160,
                0.02,
                0.005,
                3,
                23
        );

        FlowerSpecies f2 =
                new FlowerSpecies(
                        fp2ID,
                        1.15,
                        1.20,
                        0.15,
                        0.9,
                        50,
                        190,
                        0.018,
                        0.001,
                        0,
                        25
                );

        FlowerSpecies f3 =
                new FlowerSpecies(
                        fp3ID,
                        1.20,
                        1.40,
                        0.15,
                        0.95,
                        110,
                        210,
                        0.025,
                        0.0013,
                        3,
                        28
                );

        //initialize flower patches
        FlowerPatch fp1 = new FlowerPatch(330, f1);
        FlowerPatch fp2 = new FlowerPatch(210, f2);
        FlowerPatch fp3 = new FlowerPatch(140, f3);

        //initialize hives
        Hive h1 = new Hive(p1, 30, 50);
        Hive h2 = new Hive(p2, 15, 10);
        Hive h3 = new Hive(p3, 40, 30);

        //initialize flowers and pollinators
        Flowers flowers = new Flowers(new FlowerPatch[]{fp1,fp2,fp3}, random);
        Pollinators pollinators = new Pollinators(new Hive[]{h1,h2,h3}, random);


        Set<String> p1Compatibility = new HashSet<>();
        p1Compatibility.add(fp1ID);
        p1Compatibility.add(fp2ID);
        p1Compatibility.add(fp3ID);

        //Set<String> p2Compatibility = new HashSet<>();
        //Set<String> p3Compatibility = new HashSet<>();

        Set<String> f1Compatibility = new HashSet<>();
        f1Compatibility.add(p1ID);
        f1Compatibility.add(p2ID);
        f1Compatibility.add(p3ID);

        //Set<String> f2Compatibility = new HashSet<>();
        //Set<String> f3Compatibility = new HashSet<>();

        // pollinator to flower compatibility
        Map<String, Set<String>> p2f = new HashMap<>();

        p2f.put(p1.getID(), p1Compatibility);
        p2f.put(p2.getID(), p1Compatibility);
        p2f.put(p3.getID(), p1Compatibility);

        // flower to pollinator compatibility
        Map<String, Set<String>> f2p = new HashMap<>();

        f2p.put(f1.getID(), f1Compatibility);
        f2p.put(f2.getID(), f1Compatibility);
        f2p.put(f3.getID(), f1Compatibility);

        // simulation config
        SimulationConfig cfg = new SimulationConfig("simA", 25, pollinators, flowers, p2f, f2p);

        // create factory
        SimulationFactory factory = cfg.toFactory();

        // run a batch
        BatchSimulationRunner runner = new BatchSimulationRunner(Runtime.getRuntime().availableProcessors());
        int numRuns = 8;
        List<SimulationResult> results = runner.runBatch(factory, cfg.getId(), numRuns, baseSeed);

        // print results
        System.out.println("Finished runs: " + results.size());
        for (SimulationResult r : results) {
            System.out.println(r);
        }

        runner.shutdown();
    }
}
