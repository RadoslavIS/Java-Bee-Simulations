import java.util.Iterator;

/**
 * An ordered set (container) of entries of type {@code E}.
 * Key properties:
 * <ul>
 *     <li>Entries are unique by identity (no duplicate references are stored).</li>
 *     <li>Ordering relations must be acyclic.</li>
 *     <li>An {@code OrdSet} is associated with an (optional) external checker
 *         {@code c}, which constrains which ordering relations are permitted,
 *         if {@code c} is {@code null} no extra constraints are imposed.</li>
 */
public interface OrdSet<E, R> extends Iterable<E>, Ordered<E, R> {

    @Override
    R before(E x, E y);

    @Override
    void setBefore(E x, E y) throws IllegalArgumentException;

    /**
     * Set {@code c} as the checker used to validate allowed ordering relations.
     *
     * <p>If {@code c} is {@code null} the checker is removed and future changes are
     * not restricted by an external checker. If {@code c} is non-{@code null}, every
     * currently stored ordering relation (including those implied transitively by
     * stored edges) is verified against {@code c} by calling {@code c.before(a,b)}.
     * If any stored relation is forbidden by {@code c}, this method throws {@link IllegalArgumentException}.
     *
     * @param c new checker or {@code null}
     * @throws IllegalArgumentException if {@code c} forbids any existing ordering relation
     */
    void check(Ordered<? super E, ?> c) throws IllegalArgumentException;

    /**
     * Set {@code c} as the checker used to validate allowed ordering relations,
     * and remove any currently stored ordering relations that {@code c} forbids.
     *
     * <p>If {@code c} is {@code null} the checker is removed and no relations are deleted.
     * If {@code c} is non-{@code null}, the set of stored relations is scanned and any
     * relation for which {@code c.before(a,b)} returns {@code null} (or throws a
     * {@link ClassCastException}) is removed from the container. After this call the
     * checker {@code c} is in effect.
     *
     * @param c new checker or {@code null}
     */
    void checkForced(Ordered<? super E, ?> c);

    /**
     * Number of entries in this container.
     *
     * @return number of stored entries
     */
    int size();

    @Override
    Iterator<E> iterator();
}