package checker;

import javafx.util.Pair;
import tree.Node;
import tree.Tree;
import tree.builder.Builder;

import java.util.List;

public class Checker {
    public static Pair<Integer, String> check(String file1, String file2, int line) throws Exception {
        Tree tree1 = Builder.build(file1);
        Tree tree2 = Builder.build(file2);
        Node oldNode = CheckerUtils.getNodeByLine(tree1, line);
        if (oldNode == null) {
            return new Pair<>(-2, "Cannot find node on the line: " + line);
        }
        Node newNode = findClosest(tree2, oldNode);
        if (newNode == null) {
            return new Pair<>(-1, "Cannot find pair node for node on the line: " + line);
        }
        return new Pair<>(newNode.getLine(), "Found: " + newNode.toString());
    }

    private static Node findClosest(Tree tree2, Node node) {
        double bestRate = 0.5;
        Node bestNode = null;
        List<Node> nodes = CheckerUtils.getAllNodes(tree2);
        for (Node n2 : nodes) {
            double rate = 0;
            int steps = 0;
            if (node.equalityRate(n2) < 0.001) {
                continue;
            }
            Node parent2 = n2.getParent();
            Node parent1 = node.getParent();
            while (parent1 != null || parent2 != null) {
                if (parent1 != null && parent2 != null) {
                    rate += parent1.equalityRate(parent2);
                }
                if (parent1 != null) {
                    parent1 = parent1.getParent();
                }
                if (parent2 != null) {
                    parent2 = parent2.getParent();
                }
                steps++;
            }
            if (steps > 0) {
                rate /= steps;
            }
            rate += node.equalityRate(n2);
            if (steps > 0) {
                rate /= 2;
            }
            if (rate > bestRate) {
                bestRate = rate;
                bestNode = n2;
            }
        }
        return bestNode;
    }
}
