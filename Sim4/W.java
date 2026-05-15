@Author("Boris")
@ProgramPart
public class W extends Bee{

	@Author("Boris")
	@Postcondition("number of remaining days being active is set to 10")
	public W() {
		super(10);
	}

	@Author("Boris")
	@Precondition("p != null")
	@Postcondition({"if p is Z or if p is X and there no plants of type Z, returns true, else false",
			"if p is accepted, the number of visits to the type p is incremented and the number of visits by W in p is increased"})
	@Override
	public boolean visit(Plant p, boolean hasPreferred) {
		Compatibility compat = offer(p);
		if(compat == Compatibility.STRONG){
			incrementZ();
			p.incrementW();
			return true;
		} else if (compat == Compatibility.WEAK && !hasPreferred) {
			incrementX();
			p.incrementW();
			return true;
		} else {
			return false;
		}
	}

	@Author("Boris")
	@Postcondition("returns the compatibility between W and the type of p")
	@Override
	public Compatibility offer(Plant p){
		return p.offer(this);
	}

	@Author("Rado")
	@Override
	public void accept(EntityVisitor v) {
		v.visit(this);
	}
}