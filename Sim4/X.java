@Author("Boris")
@ProgramPart
public class X extends Plant{

	@Author("Boris")
	@Postcondition("number of remaining days being active is set to 9")
	public X() {
		super(9);
	}

	@Author("Boris")
	@Postcondition("returns a strong compatibility")
	@Override
	public Compatibility offer(U u) {
		return Compatibility.STRONG;
	}

	@Author("Boris")
	@Postcondition("returns a non-existent compatibility")
	@Override
	public Compatibility offer(V v) {
		return Compatibility.NONE;
	}

	@Author("Boris")
	@Postcondition("returns a weak compatibility")
	@Override
	public Compatibility offer(W w) {
		return Compatibility.WEAK;
	}

	@Author("Rado")
	@Override
	public void accept(EntityVisitor v) {
		v.visit(this);
	}
}