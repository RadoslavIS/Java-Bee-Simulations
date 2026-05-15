@Author("Boris")
@ProgramPart
public class Z extends Plant{

	@Author("Boris")
	@Postcondition("number of remaining days being active is set to 10")
	public Z() {
		super(10);
	}

	@Author("Boris")
	@Postcondition("returns a non-existent compatibility")
	@Override
	public Compatibility offer(U u) {
		return Compatibility.NONE;
	}

	@Author("Boris")
	@Postcondition("returns a weak compatibility")
	@Override
	public Compatibility offer(V v) {
		return Compatibility.WEAK;
	}

	@Author("Boris")
	@Postcondition("returns a strong compatibility")
	@Override
	public Compatibility offer(W w) {
		return Compatibility.STRONG;
	}

	@Author("Rado")
	@Override
	public void accept(EntityVisitor v) {
		v.visit(this);
	}
}