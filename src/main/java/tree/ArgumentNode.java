package tree;

public class ArgumentNode extends NamedNode {
    public ArgumentNode(String name, String typeName, boolean isVararg) {
        super(Type.ARGUMENT, name);
        this.typeName = typeName;
        this.isVararg = isVararg;
    }

    private String typeName;
    private boolean isVararg;

    public double equalityRate(Node node) {
        if (node.getType() != Type.ARGUMENT) {
            return 0;
        }
        ArgumentNode other = (ArgumentNode) node;
        double rate = 0;
        if (name.equals(other.getName())) {
            rate += 1.25;
        }
        if (this.isVararg == other.isVararg) {
            rate += 0.5;
        }
        if (this.typeName.equals(other.typeName)) {
            rate += 1.25;
        }
        return rate / 3;
    }
}
