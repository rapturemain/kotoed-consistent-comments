package tree;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquationNode extends AbstractNode {
    public EquationNode(List<String> components, List<Node> nodes) {
        super(Type.EQUATION);
        this.components = components;
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

    private List<String> components;

    private List<Node> nodes;

    public List<Node> getNodes() {
        return nodes;
    }

    public List<String> getComponents() {
        return components;
    }

    public double equalityRate(Node node) {
        if (node.getType() != Type.EQUATION) {
            return 0;
        }
        EquationNode other = (EquationNode) node;
        Map<String, Integer> otherMap = new HashMap<>();
        int thisSize = this.components.size();
        int otherSize = other.components.size();
        List<String> components = this.components;
        List<String> componentsO = other.components;
        if (thisSize == 0 && otherSize == 0) {
            return 1;
        }
        if (thisSize == 1 && otherSize == 1) {
            components = Arrays.asList(components.get(0).split(" "));
            componentsO = Arrays.asList(componentsO.get(0).split(" "));
        }
        for (String component : componentsO) {
            for (String c : component.split(" ")) {
                otherMap.merge(c, 1, Integer::sum);
            }
        }
        int same = 0;
        for (String component : components) {
            for (String c : component.split(" ")) {
                if (otherMap.containsKey(c)) {
                    int value = otherMap.get(c);
                    if (value > 0) {
                        otherMap.put(c, value - 1);
                        same++;
                    }
                }
            }
        }
        if (thisSize == 0) {
            thisSize = 1;
        }
        if (otherSize == 0) {
            otherSize = 1;
        }
        double rate = 1.0 * same / thisSize * Math.min(thisSize, otherSize) / Math.max(thisSize, otherSize);
        if (this.getLine() > 0 && other.getLine() > 0) {
            rate += (1.0 * Math.min(this.getLine(), other.getLine()) / Math.max(this.getLine(), other.getLine()) * 0.02);
        }

        thisSize = this.nodes.size();
        otherSize = other.nodes.size();
        double buffer = 0;
        for (Node n : this.nodes) {
            for (Node nO : other.nodes) {
                buffer += n.equalityRate(nO);
            }
        }
        if (thisSize > 0 && otherSize > 0) {
            rate += buffer / thisSize * Math.min(thisSize, otherSize) / Math.max(thisSize, otherSize);
            rate /= 2;
        }
        return rate;
    }

    public String collectComponents() {
        StringBuilder sb = new StringBuilder();
        for (String component : components) {
            sb.append(component);
        }
        return sb.toString();
    }
}
