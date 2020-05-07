package tree;

public interface Node {
    enum Type {
        SIMPLE,
        CALLABLE,
        BODY,
        BRUNCH,
        VALUE_ASSIGN,
        EQUATION,
        ARGUMENT,
        CALL,
        CLASS;

        @Override
        public String toString() {
            switch (this) {
                case SIMPLE: return "Simple";
                case CALLABLE: return "Callable";
                case BODY: return "Body";
                case BRUNCH: return "Brunch";
                case VALUE_ASSIGN: return "Value assign";
                case EQUATION: return "Equation";
                case ARGUMENT: return "Argument";
                case CALL: return "Call";
                case CLASS: return "Class";
                default: return "MISSING ENUM";
            }
        }
    }

    int getLine();

    void setLine(int Line);

    void setParent(Node parent);

    void setNext(Node next);

    void setPrev(Node prev);

    Type getType();

    Node getNext();

    Node getPrev();

    Node getParent();

    double equalityRate(Node node);
}
