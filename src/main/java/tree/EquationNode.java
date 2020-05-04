package tree;

import java.util.List;

public class EquationNode extends AbstractNode {
    public EquationNode(List<String> components) {
        super(Type.EQUATION);
        this.components = components;
    }

    List<String> components;

    public List<String> getComponents() {
        return components;
    }
}
