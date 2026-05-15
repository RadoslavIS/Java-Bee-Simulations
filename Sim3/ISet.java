import java.util.Iterator;

/**
 * A concrete {@link OrdSet} implementation whose {@link #before(Object,Object)}
 * returns an {@link Iterator} over the entries that lie strictly between the
 * two given elements in the container's order.
 * {@code ISet} internally uses a {@code Graph<E>} structure to store
 *         nodes and immediate edges and produces iterator snapshots (copies)
 *         for methods that must return reusable enumerations.</li>
 */
public class ISet<E> implements OrdSet<E, Iterator<E>> {

    protected Ordered<? super E, ?> c;
    private final Graph<E> graph = new Graph<>();

    /**
     * Create an {@code ISet} with the given initial checker.
     *
     * @param c initial checker (may be {@code null} to indicate no checker)
     */
    public ISet(Ordered<? super E, ?> c) {
        this.c = c;
    }

    @Override
    public void check(Ordered<? super E, ?> c) throws IllegalArgumentException {
        if (c == null) {
            this.c = null;
            return;
        }
        for (E from : graph.getAllElements()) {
            // also checks non-direct successors
            for (E to : graph.getDescendants(from)) {
                if (from == to) continue;
                boolean isValid;
                try { isValid = c.before(from, to) != null; }
                // fallback if c.before() tries to cast
                catch (ClassCastException ex) {
                    isValid = false;
                }
                if (!isValid) throw new IllegalArgumentException("New checker forbids existing relation.");
            }
        }
        this.c = c;
    }

    @Override
    public void checkForced(Ordered<? super E, ?> c) {
        this.c = c;
        if (c == null) return;
        SimpleLinkedList<AncCacheEntry<E>> ancCache = new SimpleLinkedList<>();
        SimpleLinkedList<EdgeEntry<E>> removals = new SimpleLinkedList<>();

        for (E from : graph.getAllElements()) {
            // also checks non-direct successors
            SimpleLinkedList<E> descFrom = graph.getDescendants(from);
            for (E to : descFrom) {
                boolean isValid;
                try { isValid = c.before(from, to) != null; }
                // fallback if c.before() tries to cast
                catch (ClassCastException ex) { isValid = false; }
                if (isValid) continue;
                //--- additional checks in case c is not transitive ---

                // cache ancestors of to, so you don't have to recompute them
                // in case multiple elements point to it
                SimpleLinkedList<E> ancTo = ancCacheLookup(ancCache, to);
                if (ancTo == null) {
                    ancTo = graph.getAncestors(to);
                    ancCache.add(new AncCacheEntry<>(to, ancTo));
                }

                for (E u : descFrom) {
                    for (E v : ancTo) {
                        // remove later to avoid Concurrency error
                        removals.add(new EdgeEntry<>(u, v));
                    }
                }
            }
            // no need to check reverse relations because of anti-symmetry
        }
        for (EdgeEntry<E> ent : removals) {
            graph.removeEdge(ent.key, ent.value);
        }
    }

    /**
     * Return an iterator over all elements that lie strictly between {@code x} and {@code y}
     * in the ordering of this container, or {@code null} if {@code x} is not before {@code y}.
     * @param x lower bound (exclusive)
     * @param y upper bound (exclusive)
     * @return iterator over elements in the interval, or {@code null} if {@code x} is not before {@code y}
     */
    @Override
    public Iterator<E> before(E x, E y) {
        if (!graph.reachable(x, y)) return null;
        SimpleLinkedList<E> snapshot = graph.getInterval(x, y);
        return snapshot.iterator();
    }

    /**
     * Attempt to make {@code x} come before {@code y} in this container's order.
     *
     * @param x first element
     * @param y second element
     * @throws IllegalArgumentException if {@code x == y}, if the installed checker
     * forbids the relation, or if the relation would
     * introduce a cycle
     */
    @Override
    public void setBefore(E x, E y) throws IllegalArgumentException{
        if (x == y) throw new IllegalArgumentException("x and y are identical.");
        if (c != null) {
            boolean isValid;
            try { isValid = c.before(x, y) != null; }
            // fallback if c.before() tries to cast
            catch (ClassCastException ex) { isValid = false; }
            if (!isValid) throw new IllegalArgumentException("Checker forbids relation.");
        }
        if (graph.reachable(y, x)) throw new IllegalArgumentException("Would create a cycle.");
        graph.addEdge(x, y);
    }

    /**
     * Number of elements contained in this {@code ISet}.
     *
     * @return number of entries
     */
    @Override
    public int size() {
        return graph.size();
    }

    @Override
    public Iterator<E> iterator() {
        SimpleLinkedList<E> snapshot = graph.getAllElements();
        final Iterator<E> it = snapshot.iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() { return it.hasNext(); }
            @Override
            public E next() { return it.next(); }
        };
    }

    /**
     * Return a deep copy of the internal graph restricted to the elements strictly
     * between {@code x} and {@code y}.
     *
     * @param x exclusive lower bound
     * @param y exclusive upper bound
     * @return deep copy of the graph restricted to the interval
     */
    protected Graph<E> getGraphIntervalSnapshot(E x, E y) {
        return graph.copyInterval(x, y);
    }

    /** Helper lookup method to save computation time through the cache
     * @param ancCache used cache
     * @param to element that is searched from
     * @return ancestors of the given element */
    private SimpleLinkedList<E> ancCacheLookup(SimpleLinkedList<AncCacheEntry<E>> ancCache, E to) {
        for (AncCacheEntry<E> ent : ancCache) {
            if (ent.key == to) return ent.ancestors;
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (E e : this) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(e.toString());
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }

    /** Record entry class for the ancestor cache
     * @param key the 'to' element
     */
    private record AncCacheEntry<T>(T key, SimpleLinkedList<T> ancestors) {}

    private record EdgeEntry<T>(T key, T value) {}
}
