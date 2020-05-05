package tree;

import java.util.List;

public class Tree {
    public Tree(List<Node> nodes) {
        this.nodes = nodes;
    }

    private List<Node> nodes;

    public List<Node> getNodes() {
        return nodes;
    }
}
