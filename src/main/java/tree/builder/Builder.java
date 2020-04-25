package tree.builder;

import javafx.util.Pair;
import tree.Node;

import java.io.PipedOutputStream;
import java.util.*;

public class Builder {
    public static List<Node> build(String text) throws Exception {
        List<Pair<Integer, String>> elements = split(text);
        List<Node> list = new ArrayList<>();
        Pair<Integer, Node> pair = null;
        for (Pair<Integer, String> element : elements) {
            if (pair != null && element.getKey() < pair.getKey()) {
                continue;
            }
            Keyword keyword = Keyword.get(element.getValue());
            if (keyword == null) {
                continue;
            }
            pair = keyword.handle(element.getKey(), text);
            list.add(pair.getValue());

        }
        return list;
    }

    private static Set<Character> separators = new HashSet<>();

    static {
        separators.add(' ');
        separators.add('\n');
        separators.add('\r');
        separators.add(':');
        separators.add('(');
        separators.add(')');
        separators.add('{');
        separators.add('}');
     //   separators.add('.');
        separators.add('=');
    }

    private static List<Pair<Integer, String>> split(String str) {
        List<Pair<Integer, String>> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean isString = false;
        boolean isChar = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if ((!isString && !isChar) && separators.contains(c)) {
                addToList(sb, i, list);
                if (c == ' ' && !list.get(list.size() - 1).getValue().equals(" ")) {
                   // list.add(" ");
                    continue;
                }
                if (c == '\n') {
                    if (!list.get(list.size() - 1).equals("\n")) {
                        list.add(new Pair<>(i, "\n"));
                    }
                    continue;
                }
                if (c == '\r') {
                    continue;
                }
                list.add(new Pair<>(i, String.valueOf(c)));
                continue;
            }
            if (c == '\"' && !isChar) {
                if (isString) {
                    sb.append(c);
                    addToList(sb, i + 1, list);
                } else {
                    addToList(sb, i, list);
                    sb.append(c);
                }
                isString = !isString;
                continue;
            }
            if (c == '\'' && !isString) {
                if (isChar) {
                    sb.append(c);
                    addToList(sb, i + 1, list);
                } else {
                    addToList(sb, i, list);
                    sb.append(c);
                }
                isChar = !isChar;
                continue;
            }
            sb.append(c);
        }
        addToList(sb, str.length(), list);
        return list;
    }

    private static void addToList(StringBuilder sb, int index, List<Pair<Integer, String>> list) {
        String str = sb.toString();
        if (!str.equals("")) {
            list.add(new Pair<>(index - str.length(), str));
        }
        sb.delete(0, sb.length());
    }
}
