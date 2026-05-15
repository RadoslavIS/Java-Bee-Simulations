import java.util.Iterator;

public final class Ordering<E> implements Ordered<E, Boolean>, Modifiable<E, Ordering<E>>, Iterator<E> {

	private final Ordered<? super E, ?> c;
	private final Graph<E> dag;
	private final Iterator<E> elements;

	/**
	 * Constructor for creating a new Ordering object, meant to be used only by the OSet.before and MSet.before methods.
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *     <li>dag != null</li>
	 * </ul>
	 *
	 * @param c   checker object for a reference of possible ordering relations
	 * @param dag graph used to store elements as nodes and relations as edges
	 */
	protected Ordering(Ordered<? super E, ?> c, Graph<E> dag) {
		this.c = c;
		this.dag = dag;
		this.elements = dag.getAllElements().iterator();
	}

	/**
	 * Returns <code>this</code> or a copy of <code>this</code> with an added element <code>e</code>, depending on
	 * if <code>e</code> is already present
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *     <li>if <code>e</code> is <b>not</b> already contained in <code>this</code>, an exact copy of <code>this</code> will
	 *     be returned with <code>e</code> added</li>
	 *     <li>if <code>e</code> is already contained in <code>this</code>, <code>this</code> will be returned</li>
	 * </ul>
	 *
	 * @param e element to be added to the returned copy of <code>this</code>
	 * @return object of type Ordering with the same type parameter as <code>this</code>
	 */
	@Override
	public Ordering<E> add(E e) {
		if (dag.contains(e)) {
			return this;
		} else {
			Graph<E> copy = dag.copy();
			copy.add(e);
			return new Ordering<>(c, copy);
		}
	}

	/**
	 * Returns <code>this</code> or a copy of <code>this</code> with a removed element <code>e</code>, depending on
	 * if <code>e</code> is present
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *     <li>if <code>e</code> is contained in <code>this</code>, an exact copy of <code>this</code> will
	 *     be returned with <code>e</code> removed and all other relations left intact</li>
	 *     <li>if <code>e</code> is <b>not</b> contained in <code>this</code>, <code>this</code> will be returned</li>
	 * </ul>
	 *
	 * @param e element to be removed to the returned copy of <code>this</code>
	 * @return object of type Ordering with the same type parameter as <code>this</code>
	 */
	@Override
	public Ordering<E> subtract(E e) {
		if (!dag.contains(e)) {
			return this;
		} else {
			Graph<E> copy = dag.copy();
			copy.remove(e);
			return new Ordering<>(c, copy);
		}
	}

	/**
	 * Returns whether <code>x</code> comes before <code>y</code> in the ordering relation of <code>this</code>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *     <li>if <code>x</code> is before <code>y</code> in the ordering relation of <code>this</code> returns true</li>
	 *     <li>if <code>x</code> is <b>not</b> before <code>y</code> in the ordering relation of <code>this</code> returns null</li>
	 * </ul>
	 *
	 * @param x lower element in relation
	 * @param y upper element in relation
	 * @return Boolean object representing the result of the query
	 */
	@Override
	public Boolean before(E x, E y) {
		if (dag.reachable(x, y)) {
			return true;
		}
		return null;
	}

	/**
	 * Sets <code>x</code> before <code>y</code> in the ordering relation of <code>this</code>
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *     <li><code>x</code> != <code>y</code></li>
	 *     <li><code>x</code> is contained in <code>this</code></li>
	 *     <li><code>y</code> is contained in <code>this</code></li>
	 *     <li>the checker object of <code>this</code> is null or the relation is valid according to it</li>
	 *     <li>the backwards relation does not already exist</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *     <li><code>x</code> is set to be before <code>y</code> in the ordering relation of <code>this</code></li>
	 *     <li>if preconditions are not met, throws IllegalArgumentException</li>
	 * </ul>
	 *
	 * @param x lower element in relation
	 * @param y upper element in relation
	 * @throws IllegalArgumentException if preconditions are not met
	 */
	@Override
	public void setBefore(E x, E y) throws IllegalArgumentException {
		if (x == y || !dag.contains(x) || !dag.contains(y) || (c != null && c.before(x, y) == null) || before(y, x) != null) {
			throw new IllegalArgumentException("x and y must already be present, not identical and the relation must be valid!");
		} else {
			dag.addEdge(x, y);
		}
	}

	/**
	 * Returns whether iteration of <code>this</code> has more elements.
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *     <li>returns true if there are more elements in iteration</li>
	 *     <li>returns false if there are <b>no</b> more elements in iteration</li>
	 * </ul>
	 *
	 * @return availability of next
	 */
	@Override
	public boolean hasNext() {
		return elements.hasNext();
	}

	/**
	 * Returns next element in iteration of <code>this</code>.
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *     <li>hasNext == true</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *     <li>returns next element in iteration</li>
	 *     <li>if preconditions are not met, throws NoSuchElementException</li>
	 * </ul>
	 *
	 * @return next element
	 */
	@Override
	public E next() {
		return elements.next();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("{");
		boolean first = true;
		Iterator<E> it = dag.getAllElements().iterator();
		for (; it.hasNext(); ) {
			E e = it.next();
			if (!first) {
				sb.append(", ");
			}
			sb.append(e.toString());
			first = false;
		}
		sb.append("}");
		return sb.toString();
	}
}