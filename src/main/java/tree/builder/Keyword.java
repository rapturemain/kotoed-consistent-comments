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

    public Pair<Integer, Node> handle(int startIndex, String text) throws Exception {
        return executor.execute(startIndex, text);
    }

    // Static module
    private static Map<String, Keyword> map = new HashMap<String, Keyword>();

    public static Keyword get(String string) {
        return map.get(string);
    }

    public static Keyword fun = new Keyword("fun", true, new Executor() {
        public Pair<Integer, Node> execute(int startIndex, String str) throws Exception {
            int i = startIndex;
            StringBuilder sb = new StringBuilder();

            while (str.charAt(i) != ' ') {
                i++;
            }
            i++;

            while (str.charAt(i) != '(') {
                sb.append(str.charAt(i));
                i++;
            }
            String name = sb.toString().trim();
            sb.delete(0, sb.length());

            // Arguments
            String buffer;
            boolean isVararg = false;
            List<ArgumentNode> arguments = new LinkedList<ArgumentNode>();
            int bodyStart = BuilderUtils.findClosingBracket(i, '(', ')', str);
            for (++i; i < bodyStart; i++) {
                while (str.charAt(i) != ':' && i < bodyStart) {
                    if (str.charAt(i) == ' ') {
                        if (sb.toString().equals("vararg")) {
                            isVararg = true;
                        }
                        sb.delete(0, sb.length());
                        i++;
                        continue;
                    }
                    sb.append(str.charAt(i++));
                }
                buffer = sb.toString();
                sb = new StringBuilder();
                while (str.charAt(i) != ',' && i < bodyStart) {
                    sb.append(str.charAt(i++));
                }
                arguments.add(new ArgumentNode(buffer, sb.toString().replaceAll("[ :]", ""), isVararg));
                sb.delete(0, sb.length());
                isVararg = false;
            }

            for (int j = 0; j < arguments.size(); j++) {
                if (j != 0) {
                    arguments.get(j).setPrev(arguments.get(j - 1));
                }
                if (j != arguments.size() - 1) {
                    arguments.get(j).setNext(arguments.get((j + 1)));
                }
            }

            // Type
            bodyStart++;
            while (str.charAt(bodyStart) != '=' && str.charAt(bodyStart) != '{') {
                sb.append(str.charAt(bodyStart++));
            }

            String type = sb.toString().replaceAll("[ :]", "");

            //
            CallableNode node;
            int closing = 0;
            if (str.charAt(bodyStart) == '{') {
                closing = BuilderUtils.findClosingBracket(bodyStart, '{', '}', str);
                node = new CallableNode(
                        name,
                        type,
                        arguments,
                        BuilderUtils.createBodyNode(str.substring(bodyStart, closing)
                        ));
                node.getArguments().forEach(it -> it.setParent(node));
                node.getBody().setParent(node);
            } else if (str.charAt(i) == '=') {
                return null;
            } else {
                throw new Exception("Something went wrong while creating a fun");
            }
            node.getArguments().forEach(it -> it.setParent(node));
            node.getBody().setParent(node);
            return new Pair<>(closing, node);
        }
    });

    static {
        map.put(fun.toString(), fun);
    }

    private interface Executor {

        abstract Pair<Integer, Node> execute(int startIndex, String str) throws Exception;
    }
}
