import java.util.*;

public class Viewer {
	private final List<Map<String, Double>> population4pollinator, population4flower;
	private final Map<String, Statistics> stats4pollinator, stats4flower;

	public Viewer(Set<String> pollinatorSpecies, Set<String> flowerSpecies) {
		population4pollinator = new ArrayList<>();
		population4flower = new ArrayList<>();

		stats4pollinator = new HashMap<>();
		for (String key : pollinatorSpecies) {
			stats4pollinator.put(key, new Statistics());
		}

		stats4flower = new HashMap<>();
		for (String key : flowerSpecies) {
			stats4flower.put(key, new Statistics());
		}
	}

	public void feed(Map<String, Double> pBySpecie, Map<String, Double> fBySpecie) {
		this.population4pollinator.add(new HashMap<>(pBySpecie));
		this.population4flower.add(new HashMap<>(fBySpecie));
	}

	public void computeYearlyStats() {

		for (String key : stats4pollinator.keySet()) {
			//STYLE: functional programming -> evaluate Statistics for each pollinator
			DoubleSummaryStatistics statStream = population4pollinator.stream()
					.map(map -> map.get(key))
					.mapToDouble(Double::doubleValue)
					.summaryStatistics();
			Statistics stats = stats4pollinator.get(key);
			stats.offerMin(statStream.getMin());
			stats.offerMax(statStream.getMax());
			stats.add2Sum(statStream.getSum());
			stats.add2Count(statStream.getCount());
			stats.add2SumSq(population4pollinator.stream()
					.map(map -> map.get(key))
					.mapToDouble(value -> Math.pow(value - stats.getAvg(), 2))
					.sum());
		}

		for (String key : stats4flower.keySet()) {
			//STYLE: functional programming -> evaluate Statistics for each flower
			DoubleSummaryStatistics statStream = population4flower.stream()
					.map(map -> map.get(key))
					.mapToDouble(Double::doubleValue)
					.summaryStatistics();
			Statistics stats = stats4flower.get(key);
			stats.offerMin(statStream.getMin());
			stats.offerMax(statStream.getMax());
			stats.add2Sum(statStream.getSum());
			stats.add2Count(statStream.getCount());
			stats.add2SumSq(population4flower.stream()
					.map(map -> map.get(key))
					.mapToDouble(value -> Math.pow(value - stats.getAvg(), 2))
					.sum());
		}

		population4pollinator.clear();
		population4flower.clear();
	}

	public Viewer copy() {
		return new Viewer(stats4pollinator.keySet(), stats4flower.keySet());
	}

	public Map<String, Statistics> getStats4pollinator() {
		return stats4pollinator;
	}

	public Map<String, Statistics> getStats4flower() {
		return stats4flower;
	}
}
