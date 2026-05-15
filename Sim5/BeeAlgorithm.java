import java.util.*;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/** Algorithm that optimizes the parameters of an input function under a given interval
 * to produce the 'best' evaluation as defined by a given comparator */
public class BeeAlgorithm {
	/** Sampler that generates an infinite Stream<double[]> of random point coordinates
	 * inside a field defined by center and relative size s.
	 * For global sampling you may pass the midpoints of the bounds and s == 1 **/
	private static final RandomPointFunction BEE_RANDOM_POINT = (center, w, s) -> {
		int dimensions = w.dim();
		Random rng = new Random();
		return Stream.generate(() ->
				IntStream.range(0, dimensions).
						mapToDouble(d -> {
							double span = (w.max()[d] - w.min()[d]) * s;
							double min = Math.max(w.min()[d], center[d] - span / 2.0);
							double max = Math.min(w.max()[d], center[d] + span / 2.0);
							if(max <= min){
								return min;
							}
							return rng.nextDouble(min, max);
						}).toArray()
		);
	};

	/** One BA step - returns up to r best points found this iteration */
	private static final Function<BAParams, List<AbstractMap.SimpleEntry<double[], Double>>> BEE_STEP = (params) -> {
		int n = params.n(), r = params.r(), e = params.e(), p = params.p(), m = params.m(), q = params.q();
		ObjectiveFunction f = params.f();
		Bounds w = params.w();
		Comparator<AbstractMap.SimpleEntry<double[], Double>> c = (p1, p2) ->
				params.c().compare(p1.getValue(), p2.getValue());
		double s = params.s();

		// generate n scout points (global search)
		List<AbstractMap.SimpleEntry<double[], Double>> initialScouts = BEE_RANDOM_POINT.
				generate(w.midpoints(), w, 1).
				limit(n).
				map(point -> new AbstractMap.SimpleEntry<>(point, f.apply(point))).
				sorted(c).
				toList();

		// local search on top m fields
		List<AbstractMap.SimpleEntry<double[], Double>> local = IntStream.range(0, m).
				mapToObj(i -> {
					AbstractMap.SimpleEntry<double[], Double> center = initialScouts.get(i);
					// produce recruits p (elite) or q (non-elite) recruits around the field
					// pick the best among them and the center point
					int recruits = i < e ? p : q;

					return BEE_RANDOM_POINT.
							generate(center.getKey(), w, s).
							limit(recruits).
							map(point -> new AbstractMap.SimpleEntry<>(point, f.apply(point))).
							min(c).
							orElse(initialScouts.get(i));
				}).
				toList();

		// global search - for the last n-m scouts, compare their fresh global sample with the old one
		List<AbstractMap.SimpleEntry<double[], Double>> global = IntStream.range(0, n - m).
				mapToObj(i -> {
					// generate one fresh global sample and evaluate it
					double[] point = BEE_RANDOM_POINT.generate(w.midpoints(), w, 1).
							findFirst().
							orElse(w.midpoints());
					AbstractMap.SimpleEntry<double[], Double> found = new AbstractMap.SimpleEntry<>(point, f.apply(point));
					// keep the better of the two under comparator
					return c.compare(found, initialScouts.get(i)) < 0 ? found : initialScouts.get(i);
				})
				.toList();

		// combine field bests and globals, keep top r (best-first)
		return Stream.concat(local.stream(), global.stream()).
				sorted(c).
				limit(r).
				toList();
	};

	/** Run BA for t steps and keep the r best results under the given comparator
	 *  Merges and picks top r results of each iteration.
	 */
	public static final Function<BAParams, List<AbstractMap.SimpleEntry<double[], Double>>> APPLY = (params) -> {
		Comparator<AbstractMap.SimpleEntry<double[], Double>> c = (p1, p2) ->
				params.c().compare(p1.getValue(), p2.getValue());
		int r = params.r(), t = params.t();

		// produce a Stream<List<AbstractMap.SimpleEntry<double[], Double>>> of step results, reduce by merging to top-r each time
		return IntStream.range(0, t).
				mapToObj(i -> BEE_STEP.apply(params)).
				reduce((bestSoFar, newCandidates) ->
						Stream.concat(bestSoFar.stream(), newCandidates.stream()).
								sorted(c).
								limit(r).
								toList()
				).
				orElse(List.of());
	};
}