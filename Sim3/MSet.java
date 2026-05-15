import java.util.Iterator;

public class MSet<E extends Modifiable<X, E>, X> extends OSet<E> {

	/**
	 *Creates a new <code>MSet</code> object with a checker object <code>c</code>.
	 *
	 * @param c checker Object
	 */
	public MSet(Ordered<? super E, ?> c) {
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
		return super.before(x, y);
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

	/**
	 * Iterates through all elements stored in <code>this</code>, attempts to create a copy of each element with
	 * added <code>x</code> and for successful copies adds a new ordering relation between the added-to copy and the original.
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *     <li>for each element in <code>this</code>, if <code>x</code> is successfully added to the element a
	 *     new ordering relation is added to <code>this</code></li>
	 * </ul>
	 *
	 * @param x object to be added to each element
	 */
	public void plus(X x) {
		Iterator<E> it = iterator();
		try{
			while(it.hasNext()){
				E e = it.next();
				setBefore(e.add(x), e);
			}
		} catch (IllegalArgumentException ignored) {}
	}

	/**
	 * Iterates through all elements stored in <code>this</code>, attempts to create a copy of each element with
	 * subtracted <code>x</code> and for successful copies adds a new ordering relation between the subtracted-from copy and the original.
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *     <li>for each element in <code>this</code>, if <code>x</code> is successfully subtracted from the element a
	 *     new ordering relation is added to <code>this</code></li>
	 * </ul>
	 *
	 * @param x object to be subtracted from each element
	 */
	public void minus(X x) {
		Iterator<E> it = iterator();
		try{
			while(it.hasNext()){
				E e = it.next();
				setBefore(e.subtract(x), e);
			}
		} catch (IllegalArgumentException ignored) {}
	}
}