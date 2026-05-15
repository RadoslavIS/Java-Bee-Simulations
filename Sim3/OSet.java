import java.util.Iterator;

public class OSet<E> extends ISet<E> {

	/**
	 *Creates a new <code>OSet</code> object with a checker object <code>c</code>.
	 *
	 * @param c checker Object
	 */
	public OSet(Ordered<? super E, ?> c){
		super(c);
	}

	@Override
	public void check(Ordered<? super E, ?> c) throws IllegalArgumentException {
		super.check(c);
	}

	@Override
	public void checkForced(Ordered<? super E, ?> c) {
		super.checkForced(c);
	}

	/**
	 * Returns a new <code>Ordering</code> object with the same checker object and a subgraph of the storing the
	 * elements between <code>x</code> and <code>y</code> in the ordering relation of <code>this</code>.
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *     <li>returned Ordering has same checker object and a deep copy of the subgraph storing the elements
	 *     between <code>x</code> and <code>y</code> in the ordering relation of <code>this</code></li>
	 * </ul>
	 *
	 * @param x lower elements in relation
	 * @param y upper elements in relation
	 * @return Ordering object with same checker and sub-copy of ordering relation of <code>this</code>
	 */
	@Override
	public Ordering<E> before(E x, E y) {
		if(super.before(x, y) == null){
			return null;
		}
		return new Ordering<>(c, getGraphIntervalSnapshot(x, y));
	}

	@Override
	public void setBefore(E x, E y) throws IllegalArgumentException {
		super.setBefore(x, y);
	}

	@Override
	public int size() {
		return super.size();
	}

	@Override
	public Iterator<E> iterator() {
		return super.iterator();
	}
}