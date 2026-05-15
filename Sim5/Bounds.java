public record Bounds(double[] min, double[] max) {
	/** Record class containing the intervals of all function parameters of BA **/
	public Bounds(double[] min, double[] max) {
		if (min.length != max.length) throw new IllegalArgumentException("min and max must have same length");
		for (int i = 0; i < min.length; i++) {
			if (min[i] >= max[i]) throw new IllegalArgumentException("min < max required");
		}
		this.min = min.clone();
		this.max = max.clone();
	}

	public double[] midpoints(){
		double[] midpoints = new double[dim()];
		for (int i = 0; i < dim(); i++) {
			midpoints[i] = (max[i] + min[i])/2;
		}
		return midpoints;
	}

	public int dim() {
		return min.length;
	}
}