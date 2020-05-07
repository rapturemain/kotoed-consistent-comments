package tree.builder;

import javafx.util.Pair;
import tree.*;

import java.security.Key;
import java.util.*;

public final class Keyword {
    // Instance module
    private Keyword(String keyword, boolean withBody, Executor executor) {
        this.keyword = keyword;
        this.withBody = withBody;
        this.executor = executor;
    }

    private Keyword(String keyword) {
        this.keyword = keyword;
        this.withBody = false;
    }

    private boolean withBody;
    private String keyword;
    private Executor executor;

    @Override
    public String toString() {
        return keyword;
    }

    public boolean isWithBody() {
        return withBody;
    }

    public Pair<Integer, Node> handle(int startIndex, EntryList entries) throws Exception {
        return executor.execute(startIndex, entries);
    }

    // Static module
    private static Map<String, Keyword> map = new HashMap<>();

    public static Keyword get(String string) {
        return map.get(string);
    }

    public static Keyword funKeyword = new Keyword("fun", true, (index, entries) -> {
        String name = entries.get(index + 1).getString();
        EquationNode type = new EquationNode(Collections.emptyList(), Collections.emptyList());

        // Arguments
        List<ArgumentNode> arguments = new LinkedList<>();
        int i = entries.getFirst(index, "(");
        int bodyStart = BuilderUtils.findClosingBracket(i, "(", ")", entries);
        boolean isVararg = false;
        for (; i < bodyStart; i++) {
            if (entries.get(i).getString().equals("vararg")) {
                isVararg = true;
                continue;
            }
            if (entries.getString(i + 1).equals(":")) {
                arguments.add(new ArgumentNode(entries.getString(i),
                        BuilderUtils.createEquationNode(i + 2, entries), isVararg));
                arguments.get(arguments.size() - 1).setLine(entries.get(i).getLine());
                i += 1 + arguments.get(arguments.size() - 1).getTypeName().getComponents().size();
            }
        }

        // Type
        i = bodyStart + 1;
        if (entries.getString(i).equals(":")) {
            int b1 = entries.getFirst(i, "{");
            int b2 = entries.getFirst(i, "=");
            b1 = b1 == -1 ? Integer.MAX_VALUE : b1;
            b2 = b2 == -1 ? Integer.MAX_VALUE : b2;
            b1 = b1 == b2 ? entries.size() : Math.min(b1, b2);
            if (entries.getFirst(i, "{") == -1) {
                type = BuilderUtils.createEquationNode(i + 1, b1, entries);
            }
            i += type.getComponents().size() + 1;
        }
        if (entries.getString(i).equals("=")) {
            List<Node> list = new ArrayList<>(1);
            list.add(BuilderUtils.createEquationNode(entries.getFirst(bodyStart, "=") + 1, entries));
            CallableNode node = new CallableNode(
                    name,
                    type,
                    arguments,
                    new BodyNode(list)
            );
            node.setLine(entries.get(index).getLine());
            return new Pair<>(
                    entries.getFirst(bodyStart, "=") + ((EquationNode) list.get(0)).getComponents().size(),
                    node
            );
        }
        if (entries.getString(i).equals("{")) {
            Pair<Integer, BodyNode> pair = BuilderUtils.createBodyNode(i, entries);
            CallableNode node = new CallableNode(
                    name,
                    type,
                    arguments,
                    pair.getValue()
            );
            node.setLine(entries.get(index).getLine());
            return new Pair<>(pair.getKey(), node);
        }
        CallableNode node = new CallableNode(
                name,
                type,
                arguments,
                new BodyNode(Collections.emptyList())
        );
        node.setLine(entries.get(index).getLine());
        return new Pair<>(i - 1, node);
    });

    public static Keyword varKeyword = new Keyword("var", false, (index, entries) -> {
        String name = entries.getString(index + 1);
        String type = "";
        if (entries.getString(index + 2).equals(":")) {
            type = entries.getString(index + 3);
        }
        boolean wasAssign = type.equals("") ?
                entries.getString(index + 2).equals("=") : entries.getString(index + 4).equals("=");
        if (!wasAssign) {
            ValueAssignNode it = new ValueAssignNode(name, type, new EquationNode(Collections.emptyList(),
                    Collections.emptyList()));
            it.setLine(entries.get(index).getLine());
            return new Pair<>(index + 3, it);
        }
        int i = type.equals("") ? index + 3 : index + 5;
        ValueAssignNode it = new ValueAssignNode(name, type, BuilderUtils.createEquationNode(i, entries));
        it.setLine(entries.get(index).getLine());
        return new Pair<>(i + it.getEquation().getComponents().size() - 1, it);
    });
    public static Keyword valKeyword = new Keyword("val", false, varKeyword.executor);

    public static Keyword ifKeyword = new Keyword("if", true, (index, entries) -> {
        int opening = 0;
        int closing = 0;
        for (int i = index; i < entries.size(); i++) {
            if (entries.getString(i).equals("(")) {
                opening = i;
                closing = BuilderUtils.findClosingBracket(i, "(", ")", entries);
                break;
            }
        }
        EquationNode statement = BuilderUtils.createEquationNode(opening, entries);
        Pair<Integer, BodyNode> pair = BuilderUtils.createBodyNode(closing + 1, entries);
        BodyNode trueBody = pair.getValue();
        opening = pair.getKey();
        BodyNode elseBody = null;
        if (entries.getString(opening + 1).equals("else")) {
            pair = BuilderUtils.createBodyNode(opening + 2, entries);
            elseBody = pair.getValue();
            closing = pair.getKey();
        }
        if (elseBody == null) {
            elseBody = BuilderUtils.createBodyNode(entries.subList(opening + 1, opening - 1));
        }
        BrunchNode it = new BrunchNode(statement, trueBody, elseBody);
        if (closing < opening) {
            closing = opening;
        }
        it.setLine(entries.get(index).getLine());
        return new Pair<>(closing, it);
    });

    public static Keyword importKeyword = new Keyword("import", false, (index, entries) ->
            BuilderUtils.createSimpleNodeOneComponent(index, "import", entries));

    public static Keyword packageKeyword = new Keyword("package", false, (index, entries) ->
            BuilderUtils.createSimpleNodeOneComponent(index, "package", entries));


    public static Keyword returnKeyword = new Keyword("return", false, (index, entries) ->
            BuilderUtils.createSimpleNodeOneComponent(index, "return", entries));


    public static Keyword forKeyword = new Keyword("for", true, (index, entries) -> {
        int opening = index + 1;
        int closing = BuilderUtils.findClosingBracket(opening, "(", ")", entries);
        EquationNode equation = BuilderUtils.createEquationNode(opening + 1, entries);
        opening = closing + 1;
        Pair<Integer, BodyNode> pair = BuilderUtils.createBodyNode(opening, entries);
        BodyNode body = pair.getValue();
        closing = pair.getKey();
        BrunchNode node = new BrunchNode(equation, body, new BodyNode(Collections.emptyList()));
        node.setLine(entries.get(index).getLine());
        return new Pair<>(closing, node);
    });
    public static Keyword whileKeyword = new Keyword("while", true, forKeyword.executor);

    public static Keyword classKeyword = new Keyword("class", true, (index, entries) -> {
        String name = entries.getString(index + 1);
        List<Node> supertypes = new ArrayList<>(0);
        List<Node> typeBounds = new ArrayList<>(0);
        BodyNode body = new BodyNode(new ArrayList<>());
        CallableNode call = null;
        int indx = index + 2;
        if (index + 2 >= entries.size()) {
            ClassNode node = new ClassNode(name, Collections.emptyList(), Collections.emptyList(),
                    new BodyNode(Collections.emptyList()));
            node.setLine(entries.get(index).getLine());
            return new Pair<>(index + 1, node);
        }
        if (entries.getString(index + 2).equals("<")) {
            int closing = BuilderUtils.findClosingBracket(index + 2, "<", ">", entries);
            for (int i = index + 3; i < closing; i++) {
                typeBounds.add(BuilderUtils.createEquationNode(i, closing - 1, entries));
                i += ((EquationNode) typeBounds.get(typeBounds.size() - 1)).getComponents().size();
            }
            indx = closing + 1;
        }
        if (entries.getString(indx).equals("constructor")) {
            indx += 1;
        }
        if (!entries.getString(indx).equals("fun") && entries.getString(indx + 1).equals("constructor")) {
            indx += 2;
        }
        if (indx < entries.size() && entries.getString(indx).equals("(")) {
            int closing = BuilderUtils.findClosingBracket(indx, "(", ")", entries);
            indx++;
            List<ArgumentNode> arguments = new ArrayList<>();
            boolean isVararg = false;
            for (; indx < closing; indx++) {
                if (entries.get(indx).getString().equals("vararg")) {
                    isVararg = true;
                    continue;
                }
                if (entries.getString(indx + 1).equals(":")) {
                    arguments.add(new ArgumentNode(entries.getString(indx),
                            BuilderUtils.createEquationNode(indx + 2, entries), isVararg));
                    arguments.get(arguments.size() - 1).setLine(entries.get(indx).getLine());
                    indx += 1 + arguments.get(arguments.size() - 1).getTypeName().getComponents().size();
                }
            }
            List<String> comps = new ArrayList<>(1);
            comps.add(name);
            call = new CallableNode(name, new EquationNode(comps, Collections.emptyList()), arguments,
                    new BodyNode(Collections.emptyList()));
            indx = closing + 1;
        }
        if (indx < entries.size() && entries.getString(indx).equals(":")) {
            for (int i = indx + 1; i < entries.size(); i++) {
                if (entries.getFirst(i, "{") != -1) {
                    supertypes.add(BuilderUtils.createEquationNode(i, entries.getFirst(i, "{") - 1, entries));
                } else {
                    supertypes.add(BuilderUtils.createEquationNode(i, entries));
                }
                i += ((EquationNode) supertypes.get(supertypes.size() - 1)).getComponents().size();
                if (!entries.getString(i).equals(",")) {
                    indx = i;
                    break;
                }
            }
        }
        if (indx < entries.size() && entries.getString(indx).equals("{")) {
            Pair<Integer, BodyNode> pair = BuilderUtils.createBodyNode(indx, entries);
            body = pair.getValue();
            indx = pair.getKey() + 1;
        }
        if (call != null) {
            body.getNodes().add(call);
        }
        ClassNode node = new ClassNode(name, typeBounds, supertypes, body);
        node.setLine(entries.get(index).getLine());
        return new Pair<>(indx - 1, node);
    });
    public static Keyword interfaceKeyword = new Keyword("interface", true, classKeyword.executor);

    static {
        map.put(funKeyword.toString(), funKeyword);
        map.put(varKeyword.toString(), varKeyword);
        map.put(valKeyword.toString(), valKeyword);
        map.put(ifKeyword.toString(), ifKeyword);
        map.put(importKeyword.toString(), importKeyword);
        map.put(packageKeyword.toString(), packageKeyword);
        map.put(returnKeyword.toString(), returnKeyword);
        map.put(forKeyword.toString(), forKeyword);
        map.put(whileKeyword.toString(), whileKeyword);
        map.put(classKeyword.toString(), classKeyword);
        map.put(interfaceKeyword.toString(), interfaceKeyword);
    }

    private interface Executor {
        Pair<Integer, Node> execute(int index, EntryList entries) throws Exception;
    }
}
