package tree.builder;

import javafx.util.Pair;
import tree.*;

import java.util.*;

public class BuilderUtils {
    /**
     * Searches for the pair of input opening bracket
     * @param startIndex of search
     * @param opening bracket
     * @param closing bracket
     * @param entries - entry list
     * @return index of closing bracket or -1 if bracket isn't found
     */
    public static int findClosingBracket(int startIndex, String opening, String closing, EntryList entries) {
        int opened = 0;
        for (int i = startIndex; i < entries.size(); i++) {
            if (entries.get(i).getString().equals(opening)) {
                opened++;
                continue;
            }
            if (entries.get(i).getString().equals(closing)) {
                opened--;
                if (opened == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static BodyNode createBodyNode (EntryList entries) throws Exception {
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            Keyword keyword = Keyword.get(entries.get(i).getString());
            Pair<Integer, Node> pair;
            if (keyword != null) {
                pair = keyword.handle(i, entries);
                nodes.add(pair.getValue());
                i = pair.getKey();
            } else {
                nodes.add(BuilderUtils.createEquationNode(i, entries));
                i += ((EquationNode) nodes.get(nodes.size() - 1)).getComponents().size() - 1;
            }
        }
        return new BodyNode(nodes);
    }

    public static Pair<Integer, Node> createSimpleNodeOneComponent(int index, String name, EntryList entries) throws Exception {
        List<Node> list = new ArrayList<>(1);
        int size = 0;
        if (index + 1 < entries.size()) {
            EquationNode eq = BuilderUtils.createEquationNode(index + 1, entries);
            list.add(BuilderUtils.createEquationNode(index + 1, entries));
            size = eq.getComponents().size();
        }
        SimpleNode node = new SimpleNode(name, list);
        node.setLine(entries.get(index).getLine());
        return new Pair<>(size + index, node);
    }

    public static Pair<Integer, BodyNode> createBodyNode(int index, EntryList entries) throws Exception {
        int end;
        BodyNode body;
        if (!entries.getString(index).equals("{")) {
            Keyword kw = Keyword.get(entries.getString(index));
            if (kw != null) {
                Pair<Integer, Node> pair = kw.handle(index, entries);
                end = pair.getKey();
                List<Node> list = new ArrayList<>(1);
                list.add(pair.getValue());
                body = new BodyNode(list);
            } else {
                List<Node> list = new ArrayList<>(1);
                list.add(BuilderUtils.createEquationNode(index, entries));
                body = new BodyNode(list);
                end = index + ((EquationNode) list.get(0)).getComponents().size() - 1;
            }
        } else {
            end = BuilderUtils.findClosingBracket(index, "{", "}", entries);
            body = BuilderUtils.createBodyNode(entries.subList(index + 1, end - 1));
        }
        return new Pair<>(end, body);
    }

    public static EquationNode createEquationNode(int index, int endIndex, EntryList entries) throws Exception {
        boolean wasOperator = true;
        List<Node> nodes = new ArrayList<>();
        for (int i = index; i <= endIndex; i++) {
            if (entries.getString(i).equals("(")) {
                int iold = i + 1;
                int line = entries.get(i).getLine();
                String name = entries.getString(i - 1);
                i = findClosingBracket(i, "(", ")", entries);
                // Arguments
                List<EquationNode> arguments = new LinkedList<>();
                for (; iold < i; iold++) {
                    arguments.add(createEquationNode(iold, entries));
                    iold += arguments.get(arguments.size() - 1).getComponents().size();
                }
                nodes.add(new CallNode(name, arguments));
                nodes.get(nodes.size() - 1).setLine(line);
                wasOperator = false;
                continue;
            }
            if (entries.getString(i).equals("{")) {
                int iold = i;
                i = findClosingBracket(i, "{", "}", entries);
                nodes.add(BuilderUtils.createBodyNode(entries.subList(iold + 1, i - 1)));
                wasOperator = false;
                continue;
            }
            if (entries.getString(i).equals("[")) {
                nodes.add(createEquationNode(i + 1, entries));
                nodes.get(nodes.size() - 1).setLine(entries.get(i + 1).getLine());
                i = findClosingBracket(i, "[", "]", entries);
                wasOperator = false;
                continue;
            }
            if (operators.contains(entries.getString(i))) {
                wasOperator = true;
                continue;
            }
            if (!wasOperator) {
                EquationNode eq = new EquationNode(entries.stringList().subList(index, i), nodes);
                eq.setLine(entries.get(index).getLine());
                return eq;
            }
            wasOperator = false;
        }
        EquationNode eq = new EquationNode(entries.stringList().subList(index, endIndex + 1), nodes);
        eq.setLine(entries.get(index).getLine());
        return eq;
    }

    public static EquationNode createEquationNode(int index, EntryList entries) throws Exception {
        return createEquationNode(index, entries.size() - 1, entries);
    }

    private static List<String> operators = new ArrayList<>();

    static {
        operators.add("+");
        operators.add("-");
        operators.add("*");
        operators.add("/");
        operators.add("%");
        operators.add("as");
        operators.add("as?");
        operators.add("is");
        operators.add("!is");
        operators.add("==");
        operators.add("===");
        operators.add("!=");
        operators.add("!==");
        operators.add(">");
        operators.add(">=");
        operators.add("<");
        operators.add("<=");
        operators.add(".");
        operators.add("to");
        operators.add("");
        operators.add("=");
    }
}
