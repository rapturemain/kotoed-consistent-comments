package tree;

import java.util.List;

public class CallNode extends NamedNode {
    public CallNode(String name, CallableNode called, List<NamedNode> arguments) {
        super(Type.CALL, name);
        this.called = called;
        this.arguments = arguments;
    }

    private CallableNode called;
    private List<NamedNode> arguments;

    public CallableNode getCalled() {
        return called;
    }

    public List<NamedNode> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return "Type: " + type.toString() + "Name: " + name;
    }
}