package checker;

import tree.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CheckerUtils {
    public static Node getClosestNode(Tree tree, int line) {
        for (int i = line; i >= 0; i--) {
            Node node = getNodeByLine(tree, line);
            if (node != null) {
                return node;
            }
        }
        return null;
    }

    public static Node getNodeByLine(Tree tree, int line) {
        for (Node n : tree.getNodes()) {
            if (n == null) {
                continue;
            }
            if (n.getLine() == line) {
                return n;
            }
            Node node = getNodeByLine(getAllNodes(n), line);
            if (node != null) {
                return node;
            }
        }
        return null;
    }

    public static Node getNodeByLine(List<Node> nodes, int line) {
        for (Node n : nodes) {
            if (n == null) {
                continue;
            }
            if (n.getLine() == line) {
                return n;
            }
        }
        return null;
    }

    public static List<Node> getAllNodes(Tree tree) {
        List<Node> list = new ArrayList<>();
        for (Node n : tree.getNodes()) {
            list.add(n);
            list.addAll(getAllNodes(n));
        }
        return list;
    }

    public static List<Node> getAllNodes(Node node) {
        if (node == null) {
            return Collections.emptyList();
        }
        List<Node> nodes = new ArrayList<>();
        switch (node.getType()) {
            case CALLABLE: {
                CallableNode n = (CallableNode) node;
                nodes.addAll(n.getArguments());
                nodes.addAll(getAllNodes(n.getBody()));
                return nodes;
            }
            case BODY: {
                BodyNode n = (BodyNode) node;
                for (Node n1 : n.getNodes()) {
                    nodes.add(n1);
                    nodes.addAll(getAllNodes(n1));
                }
                return nodes;
            }
            case BRUNCH: {
                BrunchNode n = (BrunchNode) node;
                nodes.add(n.getEquation());
                nodes.addAll(getAllNodes(n.getTrueNode()));
                nodes.addAll(getAllNodes(n.getFalseNode()));
                return nodes;
            }
            case SIMPLE: {
                SimpleNode n = (SimpleNode) node;
                for (Node n1 : n.getNodes()) {
                    nodes.add(n1);
                    nodes.addAll(getAllNodes(n1));
                }
                return nodes;
            }
            case VALUE_ASSIGN: {
                ValueAssignNode n = (ValueAssignNode) node;
                nodes.add(n.getEquation());
                return nodes;
            }
            case CALL: {
                CallNode n = (CallNode) node;
                nodes.addAll(n.getArguments());
                return nodes;
            }
            case EQUATION: {
                EquationNode n = (EquationNode) node;
                for (Node n1 : n.getNodes()) {
                    nodes.add(n1);
                    nodes.addAll(getAllNodes(n1));
                }
            }
            default: {
                nodes.add(node);
                return nodes;
            }
        }
    }

    public static Node getContainingFunction(Node node) {
        if (node.getType() == Node.Type.CALLABLE) {
            return node;
        }
        Node parent = node.getParent();
        while (parent != null && parent.getType() != Node.Type.CALLABLE) {
            parent = parent.getParent();
        }
        return parent;
    }
}
