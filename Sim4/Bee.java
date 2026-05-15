@Author("Boris")
@ProgramPart
@Invariant({"0 <= daysActive <= totalDays",
		"numX >= 0",
		"numY >= 0",
		"numZ >= 0"})
@ServerSideHistoryConstraint("if advanceDay() was called \"totalDays\" many times or more, isActive() will return false, else true")
@ClientSideHistoryConstraint("each day, isActive() should be the first method called")
public abstract class Bee {
	private final int totalDays;
	private int daysActive = 0;

	private int numX = 0;
	private int numY = 0;
	private int numZ = 0;

	@Author("Boris")
	@Precondition("totalDays > 0")
	@Postcondition("this.totalDays = totalDays")
	public Bee(int totalDays) {
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
	@Postcondition("returns whether this bee is still active")
	public boolean isActive() {
		return daysActive < totalDays;
	}

	@Author("Boris")
	@Precondition("p != null")
	@Postcondition({"if p is accepted returns true, else false",
			"if p is accepted, the number of visits to the type p is incremented and the number of visits by the type of \"this\" is incremented in p"})
	public abstract boolean visit(Plant p, boolean hasPreferred);

	@Author("Boris")
	@Postcondition("returns the compatibility between the type of the bee and the type of p")
	public abstract Compatibility offer(Plant p);

	@Author("Rado")
	public abstract void accept(EntityVisitor v);

	@Author("Boris")
	@Postcondition("collectedFromX() will return a greater value")
	protected void incrementX() {
		numX++;
	}

	@Author("Boris")
	@Postcondition("collectedFromY() will return a greater value")
	protected void incrementY() {
		numY++;
	}

	@Author("Boris")
	@Postcondition("collectedFromZ() will return a greater value")
	protected void incrementZ() {
		numZ++;
	}

	@Author("Boris")
	@Postcondition("return number of visits to plants of type X")
	public int collectedFromX() {
		return numX;
	}

	@Author("Boris")
	@Postcondition("return number of visits to plants of type Y")
	public int collectedFromY() {
		return numY;
	}

	@Author("Boris")
	@Postcondition("return number of visits to plants of type Z")
	public int collectedFromZ() {
		return numZ;
	}
}