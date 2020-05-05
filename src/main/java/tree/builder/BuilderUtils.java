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

    public static int findClosingBracket(int startIndex, List<String> opening, List<String> closing, EntryList entries) {
        int opened = 0;
        for (int i = startIndex; i < entries.size(); i++) {
            if (opening.contains(entries.get(i).getString())) {
                opened++;
                continue;
            }
            if (closing.contains(entries.get(i).getString())) {
                opened--;
                if (opened == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static List<String> createStringList(String... strings) {
        List<String> list = new ArrayList<>(strings.length);
        for (String str : strings) {
            list.add(str);
        }
        return list;
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
                i += ((EquationNode) nodes.get(nodes.size() - 1)).getComponents().size();
            }
        }
        return new BodyNode(nodes);
    }

    public static EquationNode createEquationNode(int index, EntryList entries) throws Exception {
        boolean wasOperator = true;
        List<Node> nodes = new ArrayList<>();
        for (int i = index; i < entries.size(); i++) {
            if (entries.getString(i).equals("(")) {
                int iold = i;
                String name = entries.getString(i - 1);
                i = findClosingBracket(i, "(", ")", entries);
                // Arguments
                List<ArgumentNode> arguments = new LinkedList<>();
                boolean isVararg = false;
                for (; iold < i; iold++) {
                    if (entries.get(iold).getString().equals("vararg")) {
                        isVararg = true;
                        continue;
                    }
                    if (entries.getString(iold + 1).equals(":")) {
                        arguments.add(new ArgumentNode(entries.getString(iold), entries.getString(iold + 2), isVararg));
                        arguments.get(arguments.size() - 1).setLine(entries.get(iold).getLine());
                        iold += 2;
                    }
                }
                nodes.add(new CallNode(name, arguments));
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
        EquationNode eq = new EquationNode(entries.stringList().subList(index, entries.size()), nodes);
        eq.setLine(entries.get(index).getLine());
        return eq;
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
    }
}
