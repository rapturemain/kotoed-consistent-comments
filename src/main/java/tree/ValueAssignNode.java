package tree;

import java.util.List;

public class ValueAssignNode extends NamedNode {
    public ValueAssignNode(Type type, String name, String typeName, List<Node> nodes) {
        super(type, name);
        this.nodes = nodes;
        this.typeName = typeName;
    }

    private List<Node> nodes;
    private String typeName;

    public List<Node> getNodes() {
        return nodes;
    }

    public String getTypeName() {
        return typeName;
    }
}
