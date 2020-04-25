package tree.builder;

import javafx.util.Pair;
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

    public static BodyNode createBodyNode (EntryList entries) {
        List<Node> nodes = Collections.emptyList();
//        for (int i = 0; i < entries.size(); i++) {
//            Keyword keyword = Keyword.get(entries.get(i).getString());
//            if (keyword != null) {
//                keyword.handle(i, entries);
//            }
//        }
        return new BodyNode(nodes);
    }

}
