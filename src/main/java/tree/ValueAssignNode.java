package tree;

import java.util.List;

public class ValueAssignNode extends NamedNode {
    public ValueAssignNode(String name, String typeName, EquationNode equation) {
        super(Type.VALUE_ASSIGN, name);
        this.equation = equation;
        if (equation != null) {
            equation.setParent(this);
        }
        this.typeName = typeName;
    }

    private EquationNode equation;
    private String typeName;

    public EquationNode getEquation() {
        return equation;
    }

    public String getTypeName() {
        return typeName;
    }
}
