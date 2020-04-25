package tree.builder;

import javafx.util.Pair;
import tree.ArgumentNode;
import tree.BodyNode;
import tree.CallableNode;
import tree.Node;

import java.security.spec.ECField;
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
    private static Map<String, Keyword> map = new HashMap<String, Keyword>();

    public static Keyword get(String string) {
        return map.get(string);
    }

    public static Keyword fun = new Keyword("fun", true, new Executor() {
        public Pair<Integer, Node> execute(int index, EntryList entries) throws Exception {
            String name = entries.get(index + 1).getString();

            // Arguments
            List<ArgumentNode> arguments = new LinkedList<ArgumentNode>();
            int i = entries.findFirst(index, "(");
            int bodyStart = BuilderUtils.findClosingBracket(i, "(", ")", entries);
            boolean isVararg = false;
            for (; i < bodyStart; i++) {
                if (entries.get(i).getString().equals("vararg")) {
                    isVararg = true;
                    continue;
                }
                if (entries.getString(i + 1).equals(":")) {
                    arguments.add(new ArgumentNode(entries.getString(i), entries.getString(i + 2), isVararg));
                    i += 2;
                }
            }

            for (int j = 0; j < arguments.size(); j++) {
                if (j != 0) {
                    arguments.get(j).setPrev(arguments.get(j - 1));
                }
                if (j != arguments.size() - 1) {
                    arguments.get(j).setNext(arguments.get((j + 1)));
                }
            }

            if (entries.findFirst(bodyStart, "=") != -1 && entries.findFirst(bodyStart, "=") < entries.findFirst(bodyStart, "{")) {
                // TODO
            }

            // Type
            String type = entries.getString(entries.findFirst(bodyStart, ":") + 1);
            bodyStart = entries.findFirst(bodyStart, "{");

            int closing = BuilderUtils.findClosingBracket(bodyStart, "{", "}", entries);
            CallableNode node = new CallableNode(
                    name,
                    type,
                    arguments,
                    BuilderUtils.createBodyNode(entries.subList(bodyStart + 1, closing - 1))
            );
            node.getArguments().forEach(it -> it.setParent(node));
            node.getBody().setParent(node);
            return new Pair<>(closing, node);
        }
    });

  //  public static Keyword var = new Keyword("var", false, null);
  //  public static Keyword val = new Keyword("val", false, var.executor);

    static {
        map.put(fun.toString(), fun);
    }

    private interface Executor {
        Pair<Integer, Node> execute(int index, EntryList entries) throws Exception;
    }
}
