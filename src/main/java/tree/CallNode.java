package tree;

import java.util.List;

public class CallNode extends NamedNode {
    public CallNode(String name, CallableNode called, List<ValueAssignNode> arguments) {
        super(Type.CALL, name);
        this.called = called;
        this.arguments = arguments;
    }

    private CallableNode called;
    private List<ValueAssignNode> arguments;

    public CallableNode getCalled() {
        return called;
    }

    public List<ValueAssignNode> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return "Type: " + type.toString() + "Name: " + name;
    }
}
