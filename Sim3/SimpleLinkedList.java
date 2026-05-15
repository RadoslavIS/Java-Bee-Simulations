import java.util.Iterator;
import java.util.NoSuchElementException;

/** A simple linked list implementation */
public class SimpleLinkedList<T> implements Iterable<T> {
    private final static class LinkNode<T> {
        T value;
        LinkNode<T> next;

        LinkNode(T value) {
            this.value = value;
        }
    }

    private LinkNode<T> head;
    private LinkNode<T> tail;
    private int size;

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    /** Adds an element to the start of the list.
     *  @param v value of the added node */
    public void addFirst(T v) {
        LinkNode<T> n = new LinkNode<>(v);
        n.next = head;
        head = n;
        if (tail == null) tail = n;
        size++;
    }

    /** Adds an element to the end of the list.
     * @param v value of the added node */
    public void addLast(T v) {
        LinkNode<T> n = new LinkNode<>(v);
        if (tail == null) head = tail = n;
        else {
            tail.next = n;
            tail = n;
        }
        size++;
    }

    /** Adds an element to the end of the list.
     * @param v value of the added node */
    public void add(T v) {
        addLast(v);
    }

    /** Removes and returns the first element or null if the list is empty.
     *
     * @return value of the first node */
    public T removeFirst() {
        if (isEmpty()) return null;
        T removed = head.value;
        head = head.next;
        if (size == 1) tail = null;
        size--;
        return removed;
    }

    /** Removes and returns the last element or null if the list is empty.
     * @return value of the last node */
    public T removeLast() {
        if (isEmpty()) return null;
        T removed = tail.value;
        removeByValue(removed);
        return removed;
    }

    /** Searches for the given element and returns true if it removes it else false
     * @param target value to be removed
     * @return if successfully removed */
    public boolean removeByValue(T target) {
        LinkNode<T> prev = null;
        for (LinkNode<T> cur = head; cur != null; cur = cur.next) {
            if (cur.value == target) {
                if (prev == null) head = cur.next;
                else prev.next = cur.next;
                if (cur == tail) tail = prev;
                size--;
                return true;
            }
            prev = cur;
        }
        return false;
    }

    /** Checks if the given element is present inside the list
     * @param target compared value
     * @return if found */
    public boolean contains(T target) {
        for (LinkNode<T> cur = head; cur != null; cur = cur.next) {
            if (cur.value == target) return true;
        }
        return false;
    }

    /** Clears the list of all elements */
    public void clear() {
        head = tail = null;
        size = 0;
    }

    public SimpleLinkedList<T> copy() {
        SimpleLinkedList<T> copy = new SimpleLinkedList<>();
        for (T t : this) {
            copy.addFirst(t);
        }
        return copy;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            LinkNode<T> cur = head;
            @Override
            public boolean hasNext() {
                return cur != null;
            }
            @Override
            public T next() {
                if (cur == null) throw new NoSuchElementException();
                T v = cur.value;
                cur = cur.next;
                return v;
            }
        };
    }
}
