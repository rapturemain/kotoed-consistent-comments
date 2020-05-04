package tree.builder;

import javafx.util.Pair;
import tree.BodyNode;
import tree.EquationNode;
import tree.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
            }
        }
        return new BodyNode(nodes);
    }

    public static EquationNode createEquationNode(int index, EntryList entries) {
        boolean wasOperator = true;
        for (int i = index; i < entries.size(); i++) {
            if (entries.getString(i).equals("(")) {
                i = findClosingBracket(i, "(", ")", entries);
                continue;
            }
            if (entries.getString(i).equals("{")) {
                i = findClosingBracket(i, "{", "}", entries);
                continue;
            }
            if (operators.contains(entries.getString(i))) {
                wasOperator = true;
                continue;
            }
            if (!wasOperator) {
                return new EquationNode(entries.stringList().subList(index, i));
            }
            wasOperator = false;
        }
        EquationNode eq = new EquationNode(entries.stringList().subList(index, entries.size()));
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

    public static EquationNode createEquationNode(int index, int endIndex, EntryList entries) {
        EquationNode eq = new EquationNode(entries.stringList().subList(index, endIndex + 1));
        eq.setLine(entries.get(index).getLine());
        return eq;
    }

}
