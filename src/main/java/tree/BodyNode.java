package tree;

import javax.naming.OperationNotSupportedException;
import java.util.Iterator;
import java.util.List;

public class BodyNode extends AbstractNode {
    public BodyNode(List<Node> nodes) {
        super(Type.BODY);
        this.nodes = nodes;
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i) == null) {
                continue;
            }
            if (i > 0) {
                nodes.get(i).setPrev(nodes.get(i - 1));
            }
            if (i < nodes.size() - 1) {
                nodes.get(i).setNext(nodes.get(i + 1));
            }
            nodes.get(i).setParent(this);
        }
    }

    private List<Node> nodes;

    public List<Node> getNodes() {
       return nodes;
    }

    public double equalityRate(Node node) {
        if (node.getType() != Type.BODY) {
            return 0;
        }
        BodyNode other = (BodyNode) node;
        double rate = 0;
        for (Node n : this.nodes) {
            for (Node nO : other.nodes) {
                if (n.equalityRate(nO) > 0.5) {
                    rate += 1;
                }
            }
        }
        if (this.nodes.size() == 0) {
            return rate;
        } else {
            return rate / this.nodes.size();
        }
    }
}
