package tree;

public class ArgumentNode extends NamedNode {
    public ArgumentNode(String name, String typeName, boolean isVararg) {
        super(Type.ARGUMENT, name);
        this.typeName = typeName;
        this.isVararg = isVararg;
    }

    private String typeName;
    private boolean isVararg;
}
