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

        return null;
    }
}
