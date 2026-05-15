/** Directed acyclic graph */
public class Graph<E> {
    static final class Node<E> {
        final E elem;
        // immediate successors
        SimpleLinkedList<Node<E>> succ = new SimpleLinkedList<>();
        // immediate predecessors
        SimpleLinkedList<Node<E>> pred = new SimpleLinkedList<>();
        // easy clone mapping trick
        private Node<E> cloneRef;
        public Node(E elem) {
            this.elem = elem;
        }
    }
    private final SimpleLinkedList<Node<E>> nodes = new SimpleLinkedList<>();

    public int size() {
        return nodes.size();
    }

    public boolean hasEdge(E from, E to) {
        Node<E> nfrom = findNode(from), nto = findNode(to);
        if (nfrom == null || nto == null) return false;
        return nfrom.succ.contains(nto);
    }

    public boolean addEdge(E from, E to) {
        if (from == null || to == null) throw new NullPointerException("Elements must not be null.");
        Node<E> nfrom = ensureNode(from), nto = ensureNode(to);
        // already have direct edge
        if (nfrom.succ.contains(nto)) return false;
        // if there already exists a path from 'from' to 'to', the direct edge would be redundant
        if (reachable(from, to)) return false;
        if (reachable(to, from)) throw new IllegalArgumentException("This would create a cycle.");
        nfrom.succ.addLast(nto);
        nto.pred.addLast(nfrom);
        return true;
    }

    /** Adds an element to the graph */
    public boolean add(E e) {
        if (e == null) throw new NullPointerException("Elements must not be null.");
        Node<E> added = findNode(e);
        if (added != null) return false;
        added = new Node<>(e);
        nodes.add(added);
        return true;
    }

    private Node<E> ensureNode(E e) {
        if (e == null) throw new NullPointerException("Elements must not be null.");
        Node<E> n = findNode(e);
        if (n != null) return n;
        n = new Node<>(e);
        nodes.add(n);
        return n;
    }

    /** Removes an element from the graph */
    public boolean remove(E e) {
        if (e == null) return false;
        Node<E> node = findNode(e);
        if (node == null) return false;

        // Snapshot predecessor and successor nodes
        SimpleLinkedList<Node<E>> preds = new SimpleLinkedList<>();
        for (Node<E> p : node.pred) preds.addLast(p);

        SimpleLinkedList<Node<E>> succs = new SimpleLinkedList<>();
        for (Node<E> s : node.succ) succs.addLast(s);

        // Detach this node from predecessors and successors
        for (Node<E> p : preds) {
            p.succ.removeByValue(node);
        }
        for (Node<E> s : succs) {
            s.pred.removeByValue(node);
        }
        node.pred.clear();
        node.succ.clear();

        // Remove node from node list
        nodes.removeByValue(node);

        for (Node<E> pNode : preds) {
            for (Node<E> sNode : succs) {
                if (pNode == sNode) continue;
                E pElem = pNode.elem;
                E sElem = sNode.elem;

                // skip if direct edge already exists
                if (hasEdge(pElem, sElem)) continue;

                // avoid creating a cycle
                if (reachable(sElem, pElem)) continue;

                addEdge(pElem, sElem);
            }
        }
        return true;
    }

    public void removeEdge(E from, E to) {
        Node<E> nfrom = findNode(from), nto = findNode(to);
        if (nfrom == null || nto == null) return;
        nfrom.succ.removeByValue(nto);
        nto.pred.removeByValue(nfrom);
    }

    public boolean contains(E e) {
        return findNode(e) != null;
    }

    /**
     * DFS algorithm that finds a path to the target
     *
     * @param from starting node
     * @param to   target node
     * @return if path to the target from the starting node exists
     **/
    public boolean reachable(E from, E to) {
        Node<E> nfrom = findNode(from), nto = findNode(to);
        if (nfrom == null || nto == null) return false;
        SimpleLinkedList<Node<E>> stack = new SimpleLinkedList<>();
        SimpleLinkedList<Node<E>> visited = new SimpleLinkedList<>();
        Node<E> cur;

        stack.addFirst(nfrom);
        while (!stack.isEmpty()) {
            cur = stack.removeFirst();
            if (visited.contains(cur)) continue;
            visited.addFirst(cur);
            if (cur == nto) return true;
            for (Node<E> n : cur.succ) {
                stack.addFirst(n);
            }
        }
        return false;
    }

    /** Computes a snapshot of all elements inside the graph
     *
     * @return snapshot of all the elements inside <code>nodes</code> */
    public SimpleLinkedList<E> getAllElements() {
        SimpleLinkedList<E> result = new SimpleLinkedList<>();
        for (Node<E> n : nodes) result.add(n.elem);
        return result;
    }

    /**
     * Find Node for element, or null if not present
     *
     * @param e element to search from
     * @return node corresponding to <code>e</code> or null
     */
    private Node<E> findNode(E e) {
        if (e == null) return null;
        for (Node<E> n : nodes) if (n.elem == e) return n;
        return null;
    }

    /**
     * Computes elements between <code>x</code> and <code>y</code>
     *
     * @param x interval start (exclusive)
     * @param y interval end (exclusive)
     * @return snapshot of the interval
     */
    public SimpleLinkedList<E> getInterval(E x, E y) {
        SimpleLinkedList<E> result = new SimpleLinkedList<>();
        if (x == null || y == null) return result;
        if (!reachable(x, y)) return result;

        SimpleLinkedList<E> desc = getDescendants(x);
        SimpleLinkedList<E> anc = getAncestors(y);
        for (Node<E> n : nodes) {
            if (n.elem != x && n.elem != y && desc.contains(n.elem) && anc.contains(n.elem)) {
                result.addLast(n.elem);
            }
        }
        return result;
    }

    /**
     * Computes all descendants of the given node using DFS
     *
     * @param from starting node element
     * @return snapshot of all nodes that are ordered after <code>from</code>
     */
    public SimpleLinkedList<E> getDescendants(E from) {
        Node<E> nfrom = findNode(from);
        SimpleLinkedList<E> result = new SimpleLinkedList<>();
        if (nfrom == null) return result;
        SimpleLinkedList<Node<E>> stack = new SimpleLinkedList<>();
        SimpleLinkedList<Node<E>> visited = new SimpleLinkedList<>();
        Node<E> cur;

        stack.addFirst(nfrom);
        while (!stack.isEmpty()) {
            cur = stack.removeFirst();
            if (visited.contains(cur)) continue;
            visited.addFirst(cur);
            result.add(cur.elem);
            for (Node<E> n : cur.succ) {
                stack.addFirst(n);
            }
        }
        return result;
    }

    /**
     * Computes all descendants of the given node using DFS
     *
     * @param to starting node element
     * @return snapshot of all nodes that are ordered before <code>to</code>
     */
    public SimpleLinkedList<E> getAncestors(E to) {
        Node<E> nto = findNode(to);
        SimpleLinkedList<E> result = new SimpleLinkedList<>();
        if (nto == null) return result;
        SimpleLinkedList<Node<E>> stack = new SimpleLinkedList<>();
        SimpleLinkedList<Node<E>> visited = new SimpleLinkedList<>();
        Node<E> cur;

        stack.addFirst(nto);
        while (!stack.isEmpty()) {
            cur = stack.removeFirst();
            if (visited.contains(cur)) continue;
            visited.addFirst(cur);
            result.add(cur.elem);
            for (Node<E> n : cur.pred) {
                stack.addFirst(n);
            }
        }
        return result;
    }

    /** Return a deep copy of the graph structure.
     *  @return deep copy of the graph */
    public Graph<E> copy() {
        Graph<E> copy = new Graph<>();

        // create new node objects for every original node and attach to copy.nodes,
        for (Node<E> orig : nodes) {
            Node<E> clone = new Node<>(orig.elem);
            // maps original->clone
            orig.cloneRef = clone;
            copy.nodes.addLast(clone);
        }

        // recreate succ/pred in the clone graph using cloneRef mapping
        for (Node<E> orig : nodes) {
            Node<E> clone = orig.cloneRef;
            for (Node<E> s : orig.succ) {
                if (s.cloneRef != null) {
                    clone.succ.addLast(s.cloneRef);
                    s.cloneRef.pred.addLast(clone);
                }
            }
        }

        // clear temporary cloneRef mappings
        for (Node<E> orig : nodes) orig.cloneRef = null;
        return copy;
    }

    /** Return a deep copy of the graph that contains only elements strictly between x and y.
     *  @param x exclusive lower bound
     *  @param y exclusive upper bound
     *  @return interval copy of the graph */
    public Graph<E> copyInterval(E x, E y) {
        SimpleLinkedList<E> intervalElems = getInterval(x, y);
        if (intervalElems.isEmpty()) return new Graph<>(); // empty snapshot
        Graph<E> copy = new Graph<>();

        // for every node inside the interval,
        // create new node objects for every original node and attach to copy.nodes.
        for (Node<E> orig : nodes) {
            if (intervalElems.contains(orig.elem)) {
                Node<E> clone = new Node<>(orig.elem);
                orig.cloneRef = clone;
                copy.nodes.addLast(clone);
            }
        }

        // recreate succ/pred in the clone graph using cloneRef mapping
        for (Node<E> orig : this.nodes) {
            Node<E> cloneU = orig.cloneRef;
            if (cloneU == null) continue;
            for (Node<E> succOrig : orig.succ) {
                Node<E> cloneV = succOrig.cloneRef;
                if (cloneV != null) {
                    cloneU.succ.addLast(cloneV);
                    cloneV.pred.addLast(cloneU);
                }
            }
        }

        // clear temporary cloneRef mappings
        for (Node<E> orig : this.nodes) orig.cloneRef = null;
        return copy;
    }
}
