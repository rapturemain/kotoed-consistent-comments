package tree.builder;

import javafx.util.Pair;
import tree.EquationNode;
import tree.Node;
import tree.Tree;

import java.util.*;

public class Builder {
    public static Tree build(String text) throws Exception {
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
                list.add(BuilderUtils.createEquationNode(i, entries));
                i += ((EquationNode) list.get(list.size() - 1)).getComponents().size() - 1;
                continue;
            }
            pair = keyword.handle(i, entries);
            list.add(pair.getValue());
            i = pair.getKey();
        }
        return new Tree(list);
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
        separators.add('.');
        separators.add('=');
        separators.add('+');
        separators.add('-');
        separators.add('*');
        separators.add('%');
        separators.add('&');
        separators.add('|');
        separators.add('!');
        separators.add('<');
        separators.add('>');
        separators.add('[');
        separators.add(']');
    }

    private static EntryList split(String str) {
        int line = 1;
        EntryList list = new EntryList();
        StringBuilder sb = new StringBuilder();
        boolean isString = false;
        boolean isChar = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if ((!isString && !isChar) && separators.contains(c)) {
                addToList(sb, i, line, list);
                if (c == ' ' && !list.get(list.size() - 1).getString().equals(" ")) {
                   // list.add(" ");
                    continue;
                }
                if (c == '\n') {
                    line++;
                    continue;
                }
                if (c == '\r') {
                    continue;
                }
                list.add(i, String.valueOf(c), line);
                continue;
            }
            if (c == '\"' && !isChar) {
                if (isString) {
                    sb.append(c);
                    addToList(sb, i + 1, line, list);
                } else {
                    addToList(sb, i, line, list);
                    sb.append(c);
                }
                isString = !isString;
                continue;
            }
            if (c == '\'' && !isString) {
                if (isChar) {
                    sb.append(c);
                    addToList(sb, i + 1, line, list);
                } else {
                    addToList(sb, i, line, list);
                    sb.append(c);
                }
                isChar = !isChar;
                continue;
            }
            sb.append(c);
        }
        addToList(sb, str.length(), line, list);
        return list;
    }

    private static void addToList(StringBuilder sb, int index, int line, EntryList list) {
        String str = sb.toString();
        if (!str.equals("")) {
            list.add(index - str.length(), str, line);
        }
        sb.delete(0, sb.length());
    }
}
