import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Record, containing final results of the simulation
 *
 * @param stats4pollinator
 * @param stats4flower
 **/
public record SimulationResult(String configID, int runIndex, Map<String, Statistics> stats4pollinator,
                               Map<String, Statistics> stats4flower) {


    /**
     * Gathers the results of the Simulation and combines it into a readable String form
     *
     * @return output of the Simulation, more specifically it returns the data of Flowers and Pollinators at the end of the
     * simulation
     */
    //GOOD: Dynamic Binding -> textbook example of dynamic binding. @Override tag determines the target method to call at runtime, not compile time
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.CEILING);

        //STYLE: functional programming -> formats the main variables of the Statistics object into an outputable form later used in mapFormatter
        Function<Statistics, String> statsFormatter = stats -> Optional.ofNullable(stats)
                .map(s -> String.format(
                        "min=%s, max=%s, avg=%s, stdDev=%s",
                        df.format(s.getMin()),
                        df.format(s.getMax()),
                        df.format(s.getAvg()),
                        df.format(s.getStdDev())
                ))
                .orElse(null);

        //STYLE: functional programming -> main formatting/output part
        Function<Map<String, Statistics>, String> mapFormatter = map -> Optional.ofNullable(map)
                .filter(m -> !m.isEmpty())
                .map(m -> m.entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByKey())
                        .map(e -> String.format("    %s -> [ %s ]", e.getKey(), statsFormatter.apply(e.getValue())))
                        .collect(Collectors.joining("\n"))
                )
                .orElse(null);

        String header = String.format("Result of Simulation with configID=%s, runIndex=%d:", configID, runIndex);

        String pollinatorStats = "\nPollinators Statistics:\n" + mapFormatter.apply(stats4pollinator);
        String flowerStats = "\nFlowers Statistics:\n" + mapFormatter.apply(stats4flower);

        //STYLE: functional programming -> output data
        String result = Stream.of(header, pollinatorStats, flowerStats)
                .collect(Collectors.joining("\n"));

        //STYLE: functional programming -> styling purposes
        String border = IntStream.range(0, 100)
                .mapToObj(i -> "#")
                .collect(Collectors.joining());

        return border + "\n" + result + "\n";
    }

}
