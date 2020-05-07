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
                rate += n.equalityRate(nO);
            }
        }
        if (this.nodes.size() == 0 && other.nodes.size() == 0) {
            return 1;
        } else {
            int s1 = this.nodes.size() == 0 ? 1 : this.nodes.size();
            int s2 = other.nodes.size() == 0 ? 1 : other.nodes.size();
            return rate / s1 * 1.0 * Math.min(s1, s2) / Math.max(s1, s2);
        }
    }
}
