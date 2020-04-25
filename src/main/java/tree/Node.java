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
        CALL;

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
                default: return "MISSING ENUM";
            }
        }
    }

    Type getType();

    Node getNext();

    Node getPrev();

    Node getParent();
}
