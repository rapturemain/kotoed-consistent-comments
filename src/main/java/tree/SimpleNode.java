package tree;

import java.util.List;

public class SimpleNode extends NamedNode {
    public SimpleNode(String name, List<Node> nodes) {
        super(Type.SIMPLE, name);
        this.nodes = nodes;
        for (int i = 0; i < nodes.size(); i++) {
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
        if (node.getType() != Type.SIMPLE) {
            return 0;
        }
        double rate = 0;
        for (Node n : this.nodes) {
            for (Node nO : ((SimpleNode) node).getNodes()) {
                if (n.equalityRate(nO) > 0.5) {
                    rate += 1;
                }
            }
        }
        rate += this.name.equals(((SimpleNode) node).name) ? 2 : 0;
        return rate / (this.nodes.size() + 2);
    }
}
