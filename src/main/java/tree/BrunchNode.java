package tree;

public class BrunchNode extends AbstractNode {
    public BrunchNode() {
        super(Type.BRUNCH);
    }

    private EquationNode equation = null;
    private Node trueNode = null;
    private Node falseNode = null;

    public void setEquation(EquationNode equation) {
        this.equation = equation;
    }

    public void setTrueNode(Node trueNode) {
        this.trueNode = trueNode;
    }

    public void setFalseNode(Node falseNode) {
        this.falseNode = falseNode;
    }

    public EquationNode getEquation() {
        return equation;
    }

    public Node getTrueNode() {
        return trueNode;
    }

    public Node getFalseNode() {
        return falseNode;
    }
}
