public class Statistics {
	private double min, max, sum, sumSq;
	private long count;

	public Statistics() {
		min = Double.MAX_VALUE;
		max = Double.MIN_VALUE;
		sum = 0;
		sumSq = 0;
		count = 0;
	}

	public double getMin() {
		return min;
	}

	public void offerMin(double n) {
		this.min = Math.min(n, min);
	}

	public double getMax() {
		return max;
	}

	public void offerMax(double n) {
		this.max = Math.max(n, max);
	}

	public double getAvg() {
		return sum / count;
	}

	public double getSum(){
		return sum;
	}

	public void add2Sum(double n) {
		this.sum += n;
	}

	public double getStdDev() {
		return Math.sqrt(sumSq / count);
	}

	public void add2SumSq(double n) {
		this.sumSq += n;
	}

	public long getCount() {
		return count;
	}

	public void add2Count(long n) {
		this.count += n;
	}
}
