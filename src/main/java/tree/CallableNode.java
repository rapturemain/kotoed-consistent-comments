package tree;

import java.util.List;

public class CallableNode extends NamedNode {
    public CallableNode(String name, String returnTypeName, List<ArgumentNode> arguments, BodyNode body) {
        super(Type.CALLABLE, name);
        this.returnTypeName = returnTypeName;
        this.arguments = arguments;
        this.body = body;
    }

    private String returnTypeName;
    private List<ArgumentNode> arguments;
    private BodyNode body;

    public BodyNode getBody() {
        return body;
    }

    public List<ArgumentNode> getArguments() {
        return arguments;
    }

    public String getReturnType() {
        return returnTypeName;
    }
}
