import java.util.Random;
/**
 * Weather model<br>
 * --- Invariants ---
 * <ul>
 *     <li>0 <= <code>humidity</code> <= 1</li>
 *     <li>0 <= <code>sunlight</code> <= 24</li>
 *     <li>0 <= <code>cloudCover</code> <= 1</li>
 *     <li>0 <= <code>totalSunshine</code></li>
 * </ul>
 */
//GOOD: OOP -> high class cohesion - sole purpose of this class is to model weather, low coupling, encapsulated from other classes
public class Weather {
	private double humidity;
	private double sunlight;
	private double cloudCover;
	private double totalSunshine;
	private double temperature;
	private boolean isRaining;
	private final Random rng;

	private final double latitude = 48.2;//latitude of Vienna in degrees
	private final double coldest = 0.0;//coldest avg temp in Vienna
	private final double hottest = 20.0;//warmest avg temp in Vienna
	private final double shortest = calculateDayLength(297);//shortest day = Dec 22nd
	private final double longest = calculateDayLength(115);//longest day = Jun 23rd

	/**
	 * Creates a Weather object with non-deterministic Random.
	 */
	//STYLE: OOP -> object initialization via constructor
	public Weather() {
		this(new Random());
	}

	/**
	 * Creates a Weather object using the provided Random generator.
	 * Use this constructor for reproducible simulations (pass a seeded Random).
	 *
	 * <p><b>Preconditions</b>:
	 * <ul>
	 *  <li>rng != null</li>
	 * </ul>
	 *
	 * @param rng given Random object
	 */
	//STYLE: OOP -> object initialization via constructor
	public Weather(Random rng) {
		if (rng == null) throw new NullPointerException("Rng must not be null!");
		this.rng = rng;
	}

	/**
	 * Starts vegetative season starting on March 1st.
	 *
	 * <p><b>Postconditions</b>
	 * <ul>
	 *     <li>variables are initialized</li>
	 * </ul>
	 */
	//STYLE: procedural -> changes state directly
	public void startSeason() {
		totalSunshine = 0;
		cloudCover = 0.45;//avg cloudCover in Vienna on March 1st
		updateSunlight(0);
		isRaining = false;
		updateTemperature(0);
		humidity = Math.clamp(Math.pow(0.9, temperature / 3 + 2), 0, 1);//0.9 = avg humidity in Vienna on March 1st
	}

	/**
	 * Applies daily updates to all weather conditions.
	 *
	 * <p><b>Preconditions</b>:
	 * <ul>
	 *  <li>0 <= day <= Simulation.DAYS_TO_SIMULATE</li>
	 * </ul>
	 *
	 * <p><b>Postconditions</b>
	 * <ul>
	 *     <li>dT = old temperature - new temperature</li>
	 * </ul>
	 */
	//STYLE: procedural -> structured calls to other procedures
	public void updateDay(int day) {
		updateSunlight(day);
		double dT = temperature;
		updateTemperature(day);
		dT -= temperature;
		checkRainfall(dT);
		updateHumidity();
		updateCloudCover(dT);
		updateTotalSunshine();
	}

	/**
	 * Calculates and returns number of hours of sunlight on a given day
	 *
	 * <p><b>Preconditions</b>:
	 * <ul>
	 *  <li>0 <= day <= Simulation.DAYS_TO_SIMULATE</li>
	 * </ul>
	 *
	 * <p><b>Postconditions</b>
	 * <ul>
	 *     <li>0 <= returned value <= 24</li>
	 * </ul>
	 *
	 * @param day current day
	 * @return number of sunny hours on this day
	 */
	//STYLE: procedural -> calculates and returns value
	private double calculateDayLength(int day) {
		//GOOD: procedural -> minimal dependency on global variable, concrete purpose
		double degreeToRadian = Math.PI / 180;
		double latitude = this.latitude * degreeToRadian;
		double solarDeclination = 23.44 * degreeToRadian * Math.sin(2 * Math.PI * (day - 22) / 365.25);
		double dayLength = 24 / Math.PI * Math.acos(-1 * Math.tan(latitude) * Math.tan(solarDeclination));
		//48.2 = latitude of vienna, 23.44 = axial tilt, 22 = March 22nd(solar equinox), 365.25 = days in a year, 24 = hours in a day
		return dayLength;
	}

	/**
	 * Updates sunlight according to the current day.
	 *
	 * <p><b>Preconditions</b>:
	 * <ul>
	 *  <li>0 <= day <= Simulation.DAYS_TO_SIMULATE</li>
	 * </ul>
	 *
	 * <p><b>Postconditions</b>
	 * <ul>
	 *     <li>sunlight is a function of the day length and cloudCover</li>
	 * </ul>
	 */
	private void updateSunlight(int day) {
		this.sunlight = calculateDayLength(day) * Math.sqrt(cloudCover);
	}

	/**
	 * Updates temperature according to the current day.
	 *
	 * <p><b>Preconditions</b>:
	 * <ul>
	 *  <li>0 <= day <= Simulation.DAYS_TO_SIMULATE</li>
	 * </ul>
	 *
	 * <p><b>Postconditions</b>
	 * <ul>
	 *     <li>temperature is a function of the old temperature, day length, cloudCover and isRaining</li>
	 * </ul>
	 */
	//STYLE: procedural -> changes state directly
	private void updateTemperature(int day) {
		double prevTemp = temperature;
		double base = calculateDayLength(day - 31) * (hottest - coldest) / (longest - shortest) - 20;
		//-31 days to account for thermal inertia
		double deviation = (-3) * (cloudCover - 0.7) + (isRaining ? -3 : 0) + rng.nextGaussian(0, 6);
		double newTemp = base + deviation;
		this.temperature = 0.65 * newTemp + 0.35 * prevTemp;
	}

	/**
	 * Updates humidity.
	 *
	 * <p><b>Postconditions</b>
	 * <ul>
	 *     <li>humidity is a function of the old humidity and temperature</li>
	 * </ul>
	 */
	private void updateHumidity() {
		humidity = 1.2 / (1.2 * humidity + 0.6 * (Math.exp(0.066 * temperature)));
		humidity = Math.clamp(humidity, 0.0, 1.0);
	}

	/**
	 * Update cloudCover.
	 *
	 * <p><b>Preconditions</b>
	 * <ul>
	 *     <li>dT is a valid double</li>
	 * </ul>
	 *
	 * <p><b>Postconditions</b>
	 * <ul>
	 *     <li>cloudCover is a function of humidity and dT</li>
	 * </ul>
	 *
	 * @param dT change of temperature between last day current day
	 */
	private void updateCloudCover(double dT) {
		cloudCover = 1.55 / (1 + Math.exp(-2 * (humidity - 0.9) + dT / 5));
		cloudCover = Math.clamp(cloudCover, 0.0, 1.0);
	}

	/**
	 * Apply effect of rain and/or activate rain.
	 *
	 * <p><b>Preconditions</b>
	 * <ul>
	 *     <li>dT is a valid double</li>
	 * </ul>
	 *
	 * <p><b>Postconditions</b>
	 * <ul>
	 *     <li>if isRaining == true, humidity and cloudCover are decreased</li>
	 *     <li>isRaining is activated depending on cloudCover, humidity, dT and temperature</li>
	 * </ul>
	 *
	 * @param dT change of temperature between last day current day
	 */
	private void checkRainfall(double dT) {
		if (isRaining) {
			humidity -= 0.15;
			cloudCover -= 0.1;
		}
		isRaining = cloudCover > 0.45 && humidity > 0.55 && dT < 0 || rng.nextDouble(-5, 50) < temperature;
	}

	/**
	 * <p><b>Postconditions</b>
	 * <ul>
	 *     <li>new totalSunshine = old totalSunshine + sunlight</li>
	 * </ul>
	 */
	private void updateTotalSunshine() {
		this.totalSunshine += this.sunlight;
	}

	/**
	 * @return value of the <code>humidity</code>
	 */
	public double getHumidity() {
		return humidity;
	}

	/**
	 * Returns the sum of all effective sunlight hours during the simulated year
	 *
	 * @return <code>totalSunshine</code>
	 */
	public double getTotalSunshine() {
		return totalSunshine;
	}

	/**
	 * @return effective daily sunlight hours
	 */
	public double getSunlight() {
		return sunlight;
	}

	/**
	 * @return current <code>temperature</code>
	 */
	public double getTemperature() {
		return temperature;
	}

	/**
	 * @return whether it <code>isRaining</code>
	 */
	public boolean isRaining() {
		return isRaining;
	}

	/**
	 * @return current <code>cloudCover</code>
	 */
	public double getCloudCover() {
		return cloudCover;
	}
}
