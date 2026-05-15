@Author("Boris")
@ProgramPart
public class Y extends Plant{

	@Author("Boris")
	@Postcondition("number of remaining days being active is set to 8")
	public Y() {
		super(8);
	}

	@Author("Boris")
	@Postcondition("returns a weak compatibility")
	@Override
	public Compatibility offer(U u) {
		return Compatibility.WEAK;
	}

	@Author("Boris")
	@Postcondition("returns a strong compatibility")
	@Override
	public Compatibility offer(V v) {
		return Compatibility.STRONG;
	}

	@Author("Boris")
	@Postcondition("returns a non-existent compatibility")
	@Override
	public Compatibility offer(W w) {
		return Compatibility.NONE;
	}

	@Author("Rado")
	@Override
	public void accept(EntityVisitor v) {
		v.visit(this);
	}
}