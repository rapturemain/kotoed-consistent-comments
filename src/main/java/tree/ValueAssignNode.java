package tree;

import javax.xml.bind.ValidationEvent;
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

    public double equalityRate(Node node) {
        if (node.getType() != Type.VALUE_ASSIGN) {
            return 0;
        }
        ValueAssignNode other = (ValueAssignNode) node;
        double rate = 0;
        rate += (this.name.equals(other.getName()) ? 2 : 0) + (this.typeName.equals(other.getTypeName()) ? 1 : 0);
        rate += this.equation.equalityRate(other.getEquation());
        return rate / 4;
    }
}
