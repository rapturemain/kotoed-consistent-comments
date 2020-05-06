package tree.builder;

import javafx.util.Pair;
import tree.*;

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
                arguments.add(new ArgumentNode(entries.getString(i), entries.getString(i + 2), isVararg));
                arguments.get(arguments.size() - 1).setLine(entries.get(i).getLine());
                i += 2;
            }
        }

        if (entries.getFirst(bodyStart, "=") != -1 && entries.getFirst(bodyStart, "=") < entries.getFirst(bodyStart, "{")) {
            List<Node> list = new ArrayList<>(1);
            list.add(BuilderUtils.createEquationNode(entries.getFirst(bodyStart, "=") + 1, entries));
            CallableNode node = new CallableNode(
                    name,
                    "",
                    arguments,
                    new BodyNode(list)
            );
            node.setLine(entries.get(index).getLine());
            return new Pair<>(
                    entries.getFirst(bodyStart, "=") + ((EquationNode) list.get(0)).getComponents().size(),
                    node
            );
        }

        // Type
        String type = entries.getString(entries.getFirst(bodyStart, ":") + 1);
        bodyStart = entries.getFirst(bodyStart, "{");

        int closing = BuilderUtils.findClosingBracket(bodyStart, "{", "}", entries);
        CallableNode node = new CallableNode(
                name,
                type,
                arguments,
                BuilderUtils.createBodyNode(entries.subList(bodyStart + 1, closing - 1))
        );
        node.setLine(entries.get(index).getLine());
        return new Pair<>(closing, node);
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
        BodyNode trueBody;
        if (!entries.getString(closing + 1).equals("{")) {
            Keyword kw = map.get(entries.getString(closing + 1));
            if (kw != null) {
                Pair<Integer, Node> pair = kw.handle(closing + 1, entries);
                opening = pair.getKey();
                List<Node> list = new ArrayList<>(1);
                list.add(pair.getValue());
                trueBody = new BodyNode(list);
            } else {
                List<Node> list = new ArrayList<>(1);
                list.add(BuilderUtils.createEquationNode(closing + 1, entries));
                trueBody = new BodyNode(list);
                opening = closing + ((EquationNode) list.get(0)).getComponents().size();
            }
        } else {
            opening = BuilderUtils.findClosingBracket(closing + 1, "{", "}", entries);
            trueBody = BuilderUtils.createBodyNode(entries.subList(closing + 1, opening));
        }
        BodyNode elseBody = null;
        if (entries.getString(opening + 1).equals("else")) {
            if (!entries.getString(opening + 2).equals("{")) {
                Keyword kw = map.get(entries.getString(opening + 2));
                if (kw != null) {
                    Pair<Integer, Node> pair = kw.handle(opening + 2, entries);
                    closing = pair.getKey();
                    List<Node> list = new ArrayList<>(1);
                    list.add(pair.getValue());
                    elseBody = new BodyNode(list);
                } else {
                    List<Node> list = new ArrayList<>(1);
                    list.add(BuilderUtils.createEquationNode(opening + 2, entries));
                    closing = opening + 1 + ((EquationNode) list.get(0)).getComponents().size();
                    elseBody = new BodyNode(list);
                }
            } else {
                closing = BuilderUtils.findClosingBracket(opening + 2, "{", "}", entries);
                elseBody = BuilderUtils.createBodyNode(entries.subList(opening + 2, closing));
            }
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

    public static Keyword returnKeyword = new Keyword("return", false, (index, entries) -> {
        EquationNode eq = BuilderUtils.createEquationNode(index + 1, entries);
        List<Node> list = new ArrayList<>(1);
        list.add(BuilderUtils.createEquationNode(index + 1, entries));
        SimpleNode node = new SimpleNode("return", list);
        node.setLine(entries.get(index).getLine());
        return new Pair<>(eq.getComponents().size() + index, node);
    });

    public static Keyword forKeyword = new Keyword("for", true, (index, entries) -> {
        int opening = index + 1;
        int closing = BuilderUtils.findClosingBracket(opening, "(", ")", entries);
        EquationNode equation = BuilderUtils.createEquationNode(opening + 1, entries);
        opening = closing + 1;
        closing = BuilderUtils.findClosingBracket(opening, "{", "}", entries);
        BodyNode body = BuilderUtils.createBodyNode(entries.subList(opening, closing));
        BrunchNode node = new BrunchNode(equation, body, null);
        node.setLine(entries.get(index).getLine());
        return new Pair<>(closing, node);
    });
    public static Keyword whileKeyword = new Keyword("while", true, forKeyword.executor);



    static {
        map.put(funKeyword.toString(), funKeyword);
        map.put(varKeyword.toString(), varKeyword);
        map.put(valKeyword.toString(), valKeyword);
        map.put(ifKeyword.toString(), ifKeyword);
        map.put(returnKeyword.toString(), returnKeyword);
        map.put(forKeyword.toString(), forKeyword);
        map.put(whileKeyword.toString(), whileKeyword);
    }

    private interface Executor {
        Pair<Integer, Node> execute(int index, EntryList entries) throws Exception;
    }
}
