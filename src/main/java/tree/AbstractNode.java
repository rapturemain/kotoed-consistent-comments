package tree;

public abstract class AbstractNode implements Node {
    protected AbstractNode(Type type) {
        this.type = type;
    }

    protected Node next = null;
    protected Node prev = null;
    protected Node parent = null;
    protected final Type type;
    protected int line = 0;

    @Override
    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public Type getType() {
        return type;
    }

    public Node getNext() {
        return next;
    }

    public Node getPrev() {
        return prev;
    }

    public Node getParent() {
        return parent;
    }

    public String toString() {
        return "Type: " + type.toString() + " Line: " + line;
    }
}
