import java.util.Random;

@Author("Boris")
@ProgramPart
@Invariant({"all objects stored in plants are a subtype of Plant",
		"all objects stored in bees are a subtype of Bee"})
@ClientSideHistoryConstraint({"aggregate(EntityVisitor) is called after simulate()",
		"simulate() is called only once"})
public class Simulation {
	private final Set plants, bees;
	private final Random rng;
	private final int maxVisitsPerDay;

	@Author("Boris")
	@Precondition({"maxVisitsPerDay >= 2",
			"rng != null"})
	@Postcondition({"plants is an empty set",
			"bees is an empty set",
			"this.rng = rng",
			"this.maxVisitsPerDay = maxVisitsPerDay"})
	public Simulation(int maxVisitsPerDay, Random rng) {
		this.plants = new Set();
		this.bees = new Set();
		this.rng = rng;
		this.maxVisitsPerDay = maxVisitsPerDay;
	}

	@Author("Boris")
	@Postcondition({"there are no active plants or bees",
			"there are at least 7 Bee and Plant objects each",
			"there are at most (maxVisitsPerDay-1)*7 Bee and Plant objects each"})
	public void simulate() {
		int currentDay = 0;
		do {
			if (currentDay < 7) {
				addNewMembers();
			}
			dailyVisits();
			advanceDay4all();
			currentDay++;
		} while (!hasEnded());
	}

	@Author("Boris")
	@Precondition("agg != null")
	@Postcondition("every object in plants and bees is visited exactly once by the visitor")
	public void aggregate(EntityVisitor agg) {
		for (int i = 0; i < plants.size(); i++) {
			Plant p = (Plant) plants.get(i);
			p.accept(agg);
		}

		for (int i = 0; i < bees.size(); i++) {
			Bee b = (Bee) bees.get(i);
			b.accept(agg);
		}
	}

	@Author("Boris")
	private void addNewMembers() {
		int count = rng.nextInt(1, maxVisitsPerDay), type = rng.nextInt(1, 4);
		for (int i = 0; i < count; i++) {
			Bee b1;
			Bee b2;
			Plant p1;
			Plant p2;
			switch (type) {
				case 1 -> {
					b1 = new V();
					b2 = new W();
					p1 = new Y();
					p2 = new Z();
				}
				case 2 -> {
					b1 = new U();
					b2 = new W();
					p1 = new X();
					p2 = new Z();
				}
				case 3 -> {
					b1 = new U();
					b2 = new V();
					p1 = new X();
					p2 = new Y();
				}
				default -> {
					b1 = null;
					b2 = null;
					p1 = null;
					p2 = null;
				}
			}
			bees.add(b1);
			bees.add(b2);
			plants.add(p1);
			plants.add(p2);
		}
	}

	@Author("Boris")
	private void dailyVisits() {
		for (int i = 0; i < bees.size(); i++) {
			Bee b = (Bee) bees.get(i);
			if (!b.isActive()) {
				continue;
			}
			int visitsLeft = rng.nextInt(1, maxVisitsPerDay);
			boolean hasPreferred = true;
			while (visitsLeft > 0) {
				if (pollinate(b, plants, hasPreferred)) {
					visitsLeft--;
				} else if (pollinate(b, plants, false)) {
					hasPreferred = false;
					visitsLeft--;
				} else {
					break;
				}
			}
		}
	}

	@Author("Boris")
	private void advanceDay4all() {
		for (int i = 0; i < plants.size(); i++) {
			((Plant) plants.get(i)).advanceDay();
		}
		for (int i = 0; i < bees.size(); i++) {
			((Bee) bees.get(i)).advanceDay();
		}
	}

	@Author("Boris")
	private boolean pollinate(Bee b, Set plants, boolean hasPreferred) {
		int index = rng.nextInt(plants.size());
		for (int j = (index + 1) % plants.size(); j != index; j = (j + 1) % plants.size()) {
			Plant p = (Plant) plants.get(j);
			if (p.isActive() && b.visit(p, hasPreferred)) {
				return true;
			}
		}
		return false;
	}

	@Author("Boris")
	private boolean hasEnded() {
		for (int i = 0; i < bees.size(); i++) {
			Bee b = (Bee) bees.get(i);
			if (b.isActive()) {
				for (int j = 0; j < plants.size(); j++) {
					Plant p = (Plant) plants.get(j);
					if (b.offer(p) != Compatibility.NONE) {
						return false;
					}
				}
			}
		}
		return true;
	}
}