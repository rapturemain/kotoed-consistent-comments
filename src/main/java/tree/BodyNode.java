package tree;

import javax.naming.OperationNotSupportedException;
import java.util.Iterator;
import java.util.List;

public class BodyNode extends AbstractNode {
    public BodyNode(List<Node> nodes) {
        super(Type.BODY);
        this.nodes = nodes;
    }

    private List<Node> nodes;

    public List<Node> getNodes() {
       return nodes;
    }
}
