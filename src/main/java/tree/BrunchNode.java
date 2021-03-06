package tree;

public class BrunchNode extends AbstractNode {
    public BrunchNode() {
        super(Type.BRUNCH);
    }

    public BrunchNode(EquationNode equation, BodyNode trueNode, BodyNode falseNode) {
        super(Type.BRUNCH);
        this.equation = equation;
        if (equation != null) {
            equation.setParent(this);
        }
        this.trueNode = trueNode;
        this.falseNode = falseNode;
        if (falseNode != null) {
            falseNode.setParent(this);
        }
        if (trueNode != null) {
            trueNode.setParent(this);
        }
    }

    private EquationNode equation = null;
    private BodyNode trueNode = null;
    private BodyNode falseNode = null;

    public void setEquation(EquationNode equation) {
        this.equation = equation;
    }

    public void setTrueNode(BodyNode trueNode) {
        this.trueNode = trueNode;
    }

    public void setFalseNode(BodyNode falseNode) {
        this.falseNode = falseNode;
    }

    public EquationNode getEquation() {
        return equation;
    }

    public BodyNode getTrueNode() {
        return trueNode;
    }

    public BodyNode getFalseNode() {
        return falseNode;
    }

    public double equalityRate(Node node) {
        if (node.getType() != Type.BRUNCH) {
            return 0;
        }
        BrunchNode other = (BrunchNode) node;
        double rate = 0;
        rate += other.getEquation().equalityRate(this.equation);
        rate += other.getTrueNode().equalityRate(this.trueNode);
        rate += other.getFalseNode().equalityRate(this.falseNode);
        return rate / 3;
    }
}
