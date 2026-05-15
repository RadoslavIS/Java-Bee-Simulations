@Author("Rado")
@ProgramPart
@Invariant({
        "size >= 0",
        "head == null when size == 0",
        "no element appears twice"
})
@ClientSideHistoryConstraint({
        "add/remove/contains/get may be called in any order by client code",
        "client code must not rely on stable iteration order"
})
public class Set{
    @Author("Rado")
    private final static class LinkNode {
        Object value;
        LinkNode next;

        @Author("Rado")
        LinkNode(Object value) {
            this.value = value;
        }
    }

    private LinkNode head;
    private int size;

    @Author("Rado")
    @Postcondition("returns the number of elements currently stored in the set (>= 0)")
    public int size() {
        return size;
    }

    @Author("Rado")
    @Precondition("o may be null but then its values are ignored")
    @Postcondition({
            "if o == null then set is unchanged",
            "if o is already present then set is unchanged",
            "else the set contains o and size is incremented by 1"
    })
    public void add(Object o) {
        if (o == null) return;
        if (contains(o)) return;
        LinkNode n = new LinkNode(o);
        n.next = head;
        head = n;
        size++;
    }

    @Author("Rado")
    @Precondition("idx may be any integer")
    @Postcondition({
            "if 0 <= idx < size then returned value is the element at that index (head == index 0)",
            "else return null"
    })
    public Object get(int idx) {
        if (idx < 0 || idx >= size) return null;
        LinkNode curr = head;
        for (int i = 0; i < idx; i++) {
            curr = curr.next;
        }
        return curr.value;
    }

    @Author("Rado")
    @Precondition("o may be null")
    @Postcondition("returns true iff a non-null element e exists in the set such that e == target")
    public boolean contains(Object o) {
        if (o == null) return false;
        for (LinkNode cur = head; cur != null; cur = cur.next) {
            if (cur.value == o) return true;
        }
        return false;
    }
}
