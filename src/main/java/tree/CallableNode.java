package tree;

import java.util.List;

public class CallableNode extends NamedNode {
    public CallableNode(String name, EquationNode returnTypeName, List<ArgumentNode> arguments, BodyNode body) {
        super(Type.CALLABLE, name);
        this.returnTypeName = returnTypeName;
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
        this.body = body;
        if (body != null) {
            body.setParent(this);
        }
    }

    private EquationNode returnTypeName;
    private List<ArgumentNode> arguments;
    private BodyNode body;

    public BodyNode getBody() {
        return body;
    }

    public List<ArgumentNode> getArguments() {
        return arguments;
    }

    public EquationNode getReturnType() {
        return returnTypeName;
    }

    public double equalityRate(Node node) {
        if (node.getType() != Type.CALLABLE) {
            return 0;
        }
        CallableNode other = (CallableNode) node;
        double rate = 0;
        for (ArgumentNode ar : this.arguments) {
            for (ArgumentNode arO : other.arguments) {
                rate += ar.equalityRate(arO);
            }
        }
        if (this.arguments.size() > 0 && other.arguments.size() > 0) {
            rate *= 1.0 / this.arguments.size() * Math.min(this.arguments.size(), other.arguments.size()) /
                    Math.max(this.arguments.size(), other.arguments.size());
        }
        if (other.arguments.size() == 0 && this.arguments.size() == 0) {
            rate += 0.5;
        }
        rate += this.body.equalityRate(other.getBody()) * 3;
        rate += this.name.equals(other.name) ? 2 : 0;
        rate += this.returnTypeName.equalityRate(other.getReturnType());
        return rate / 4;
    }
}
