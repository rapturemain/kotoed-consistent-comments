package tree;

import java.util.List;

public class ClassNode extends NamedNode {
    public ClassNode(String name, List<Node> typeBounds, List<Node> supertypes, BodyNode body) {
        super(Type.CLASS, name);
        this.supertypes = supertypes;
        this.body = body;
        this.typeBounds = typeBounds;
        this.body.setParent(this);
        for (int i = 0; i < this.supertypes.size(); i++) {
            if (this.supertypes.get(i) == null) {
                continue;
            }
            if (i > 0) {
                this.supertypes.get(i).setPrev(this.supertypes.get(i - 1));
            }
            if (i < supertypes.size() - 1) {
                this.supertypes.get(i).setNext(this.supertypes.get(i + 1));
            }
            this.supertypes.get(i).setParent(this);
        }
        for (int i = 0; i < this.typeBounds.size(); i++) {
            if (this.typeBounds.get(i) == null) {
                continue;
            }
            if (i > 0) {
                this.typeBounds.get(i).setPrev(this.typeBounds.get(i - 1));
            }
            if (i < supertypes.size() - 1) {
                this.typeBounds.get(i).setNext(this.typeBounds.get(i + 1));
            }
            this.typeBounds.get(i).setParent(this);
        }
    }

    public BodyNode getBody() {
        return body;
    }

    public List<Node> getTypeBounds() {
        return typeBounds;
    }

    public List<Node> getSupertypes() {
        return supertypes;
    }

    private List<Node> typeBounds;
    private List<Node> supertypes;
    private BodyNode body;

    public double equalityRate(Node node) {
        if (node.getType() != Type.CLASS) {
            return 0;
        }
        ClassNode other = (ClassNode) node;
        double rate = 0;
        double buffer = 0;
        for (Node n : typeBounds) {
            for (Node nO : other.typeBounds) {
                buffer += n.equalityRate(nO);
            }
        }
        if (typeBounds.size() > 0 && other.typeBounds.size() > 0) {
            rate = buffer;
            rate *= 1.0 / this.typeBounds.size() * Math.min(this.typeBounds.size(), other.typeBounds.size()) /
                    Math.max(this.typeBounds.size(), other.typeBounds.size());
        }
        buffer = 0;
        for (Node n : supertypes) {
            for (Node nO : other.supertypes) {
                buffer += n.equalityRate(nO);
            }
        }
        if (supertypes.size() > 0 && other.supertypes.size() > 0) {
            rate += buffer;
            rate *= 1.0 / this.supertypes.size() * Math.min(this.supertypes.size(), other.supertypes.size()) /
                    Math.max(this.supertypes.size(), other.supertypes.size());

        }
        rate += body.equalityRate(other.body) * 1.25;
        rate += this.name.equals(other.name) ? 1 : 0;
        return rate / 4.1;
    }
}
