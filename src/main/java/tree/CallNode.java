package tree;

import java.util.List;

public class CallNode extends NamedNode {
    public CallNode(String name, List<EquationNode> arguments) {
        super(Type.CALL, name);
        this.arguments = arguments;
        for (int i = 0; i < arguments.size(); i++) {
            if (arguments.get(i) == null) {
                continue;
            }
            if (i > 0) {
                arguments.get(i).setPrev(arguments.get(i - 1));
            }
            if (i < arguments.size() - 1) {
                arguments.get(i).setNext(arguments.get(i + 1));
            }
            arguments.get(i).setParent(this);
        }
    }

    private List<EquationNode> arguments;

    public List<EquationNode> getArguments() {
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
        for (EquationNode ar : this.arguments) {
            for (EquationNode arO : other.arguments) {
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