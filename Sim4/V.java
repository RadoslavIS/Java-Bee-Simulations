@Author("Boris")
@ProgramPart
public class V extends Bee{

	@Author("Boris")
	@Postcondition("number of remaining days being active is set to 8")
	public V() {
		super(8);
	}

	@Author("Boris")
	@Precondition("p != null")
	@Postcondition({"if p is Y or if p is Z and there no plants of type Y, returns true, else false",
			"if p is accepted, the number of visits to the type p is incremented and the number of visits by V in p is increased"})
	@Override
	public boolean visit(Plant p, boolean hasPreferred) {
		Compatibility compat = offer(p);
		if(compat == Compatibility.STRONG){
			incrementY();
			p.incrementV();
			return true;
		} else if (compat == Compatibility.WEAK && !hasPreferred) {
			incrementZ();
			p.incrementV();
			return true;
		} else {
			return false;
		}
	}

	@Author("Boris")
	@Postcondition("returns the compatibility between V and the type of p")
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