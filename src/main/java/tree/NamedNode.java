package tree;

public abstract class NamedNode extends AbstractNode {
    protected NamedNode(Type type, String name) {
        super(type);
        this.name = name;
    }

    protected String name;

    public String getName() {
        return name;
    }

    public String toString() {
        return "Type: " + type.toString() + " Name: " + name + " Line: " + line;
    }
}
