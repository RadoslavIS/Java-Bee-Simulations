@Author("Boris")
@ProgramPart
public class U extends Bee{

	@Author("Boris")
	@Postcondition("number of remaining days being active is set to 9")
	public U() {
		super(9);
	}

	@Author("Boris")
	@Precondition("p != null")
	@Postcondition({"if p is X or if p is Y and there no plants of type X, returns true, else false",
			"if p is accepted, the number of visits to the type p is incremented and the number of visits by U in p is increased"})
	@Override
	public boolean visit(Plant p, boolean hasPreferred) {
		Compatibility compat = offer(p);
		if(compat == Compatibility.STRONG){
			incrementX();
			p.incrementU();
			return true;
		} else if (compat == Compatibility.WEAK && !hasPreferred) {
			incrementY();
			p.incrementU();
			return true;
		} else {
			return false;
		}
	}

	@Author("Boris")
	@Postcondition("returns the compatibility between U and the type of p")
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