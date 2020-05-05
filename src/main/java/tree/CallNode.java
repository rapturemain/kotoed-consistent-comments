package tree;

import java.util.List;

public class CallNode extends NamedNode {
    public CallNode(String name, List<ArgumentNode> arguments) {
        super(Type.CALL, name);
        this.arguments = arguments;
    }

    private List<ArgumentNode> arguments;

    public List<ArgumentNode> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return "Type: " + type.toString() + "Name: " + name;
    }

    @Override
    public double equalityRate(Node node) {
        if (node.getType() != Type.CALL) {
            return 0;
        }
        CallNode other = (CallNode) node;
        double rate = 0;
        for (ArgumentNode ar : this.arguments) {
            for (ArgumentNode arO : other.arguments) {
                if (ar.equalityRate(arO) > 0.5) {
                    rate += 1;
                }
            }
        }
        if (arguments.size() > 0) {
            rate /= this.arguments.size();
        }
        rate += this.name.equals(other.name) ? 2 : 0;
        return rate / 2;
    }
}