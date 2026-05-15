import java.util.Comparator;

public record BAParams(int a, ObjectiveFunction f, Bounds w, Comparator<Double> c,
                             int t, int n, int m,
                             int e, int p, int q,
                             double s, int r) {

	/** Record class used to initialize the parameters of BA **/
	public BAParams {
		if (a <= 0) {
			throw new IllegalArgumentException("a must be > 0");
		}
		if (w.dim() != a) {
			throw new IllegalArgumentException("w dimensions must be = a");
		}
		if (t <= 0) {
			throw new IllegalArgumentException("t must be > 0");
		}
		if (n <= 0) {
			throw new IllegalArgumentException("n must be > 0");
		}
		if (m <= 0 || m >= n) {
			throw new IllegalArgumentException("m must be > 0 and < n");
		}
		if (e <= 0 || e >= m) {
			throw new IllegalArgumentException("e must be > 0 and < m");
		}
		if (p <= 0) {
			throw new IllegalArgumentException("p must be > 0");
		}
		if (q <= 0 || q >= p) {
			throw new IllegalArgumentException("q must be > 0 and < p");
		}
		if (s <= 0 || s > 1) {
			throw new IllegalArgumentException("s must be < 0 and >= 1");
		}
		if (r <= 0) {
			throw new IllegalArgumentException("r must be > 0");
		}
	}
}