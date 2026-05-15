@Author("Boris")
@ProgramPart
@Invariant({"0 <= daysActive <= totalDays",
		"numU >= 0",
		"numV >= 0",
		"numW >= 0"})
@ServerSideHistoryConstraint("if advanceDay() was called \"totalDays\" many times or more, isActive() will return false, else true")
@ClientSideHistoryConstraint("each day, isActive() should be the first method called")
public abstract class Plant {
	private final int totalDays;
	private int daysActive = 0;

	private int numU = 0;
	private int numV = 0;
	private int numW = 0;

	@Author("Boris")
	@Precondition("totalDays > 0")
	@Postcondition("this.totalDays = totalDays")
	public Plant(int totalDays) {
		this.totalDays = totalDays;
	}

	@Author("Boris")
	@Postcondition("if isActive() == true, number of remaining days to be active is decremented")
	public void advanceDay() {
		if (isActive()) {
			daysActive++;
		}
	}

	@Author("Boris")
	@Postcondition("returns whether this plant is still active")
	public boolean isActive() {
		return daysActive < totalDays;
	}

	@Author("Boris")
	@Postcondition("visitedByU() will return a greater value")
	protected void incrementU() {
		numU++;
	}

	@Author("Boris")
	@Postcondition("visitedByV() will return a greater value")
	protected void incrementV() {
		numV++;
	}

	@Author("Boris")
	@Postcondition("visitedByW() will return a greater value")
	protected void incrementW() {
		numW++;
	}

	@Author("Boris")
	@Postcondition("returns the compatibility between the type of the plant and bees of type U")
	public abstract Compatibility offer(U u);

	@Author("Boris")
	@Postcondition("returns the compatibility between the type of the plant and bees of type U")
	public abstract Compatibility offer(V v);

	@Author("Boris")
	@Postcondition("returns the compatibility between the type of the plant and bees of type U")
	public abstract Compatibility offer(W w);

	@Author("Rado")
	public abstract void accept(EntityVisitor v);

	@Author("Boris")
	@Postcondition("return number of visits from bees of type U")
	public int visitedByU() {
		return numU;
	}

	@Author("Boris")
	@Postcondition("return number of visits from bees of type V")
	public int visitedByV() {
		return numV;
	}

	@Author("Boris")
	@Postcondition("return number of visits from bees of type W")
	public int visitedByW() {
		return numW;
	}
}