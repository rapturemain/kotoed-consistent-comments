package tree.builder;

import javafx.util.Pair;
import tree.Node;

import java.io.PipedOutputStream;
import java.util.*;

public class Builder {
    public static List<Node> build(String text) throws Exception {
        EntryList entries = split(text);
        List<Node> list = new ArrayList<>();
        Pair<Integer, Node> pair = null;
        for (int i = 0; i < entries.size(); i++) {
            Entry entry = entries.get(i);
            if (pair != null && entry.getIndex() < pair.getKey()) {
                continue;
            }
            Keyword keyword = Keyword.get(entry.getString());
            if (keyword == null) {
                continue;
            }
            pair = keyword.handle(i, entries);
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

    private static EntryList split(String str) {
        EntryList list = new EntryList();
        StringBuilder sb = new StringBuilder();
        boolean isString = false;
        boolean isChar = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if ((!isString && !isChar) && separators.contains(c)) {
                addToList(sb, i, list);
                if (c == ' ' && !list.get(list.size() - 1).getString().equals(" ")) {
                   // list.add(" ");
                    continue;
                }
                if (c == '\n') {
                    if (!list.get(list.size() - 1).equals("\n")) {
                     //   list.add(i, "\n");
                    }
                    continue;
                }
                if (c == '\r') {
                    continue;
                }
                list.add(i, String.valueOf(c));
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

    private static void addToList(StringBuilder sb, int index, EntryList list) {
        String str = sb.toString();
        if (!str.equals("")) {
            list.add(index - str.length(), str);
        }
        sb.delete(0, sb.length());
    }
}
