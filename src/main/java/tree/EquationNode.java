package tree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquationNode extends AbstractNode {
    public EquationNode(List<String> components) {
        super(Type.EQUATION);
        this.components = components;
    }

    List<String> components;

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
        for (String component : other.components) {
            otherMap.merge(component, 1, Integer::sum);
        }
        int same = 0;
        int i = 0;
        for (String component : this.components) {
            if (otherMap.containsKey(component)) {
                int value = otherMap.get(component);
                if (value > 0) {
                    otherMap.put(component, value);
                    same++;
                }
            }
        }
        if (thisSize == 0) {
            thisSize = 1;
        }
        if (otherSize == 0) {
            otherSize = 1;
        }
        return 1.0 * same / thisSize * Math.min(thisSize, otherSize) / Math.max(thisSize, otherSize);
    }
}
