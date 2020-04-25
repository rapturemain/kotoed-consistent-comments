package tree.builder;

import tree.BodyNode;
import tree.Node;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BuilderUtils {
    /**
     * Searches for the pair of input opening bracket
     * @param startIndex of search
     * @param opening bracket
     * @param closing bracket
     * @param str to search in
     * @return index of closing bracket or -1 if bracket isn't found
     */
    public static int findClosingBracket(int startIndex, char opening, char closing, String str) {
        int opened = 0;
        for (int i = startIndex; i < str.length(); i++) {
            if (str.charAt(i) == opening) {
                opened++;
                continue;
            }
            if (str.charAt(i) == closing) {
                opened--;
                if (opened == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static BodyNode createBodyNode (String str) {
        List<Node> nodes = Collections.emptyList();
        // TODO
        return new BodyNode(nodes);
    }

}
