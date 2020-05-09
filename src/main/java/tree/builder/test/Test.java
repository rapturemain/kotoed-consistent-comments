package tree.builder.test;

import tree.Tree;
import tree.builder.Builder;

import java.io.*;

public class Test {
    public static void main(String[] args) throws Exception {
        Tree list = Builder.build(fileToString("src\\main\\java\\tree\\builder\\test\\funBuildTest.txt"));
        System.out.println(list.getNodes().size());
    }

    private static void assertEquals(Object expected, Object actual) throws Exception {
        if (!expected.equals(actual)) {
            throw new Exception("Expected:\n" + expected.toString() + "\n But was:\n" + actual.toString());
        }
    }

    private static String fileToString(String path) throws IOException {
        File file = new File(path);
        FileReader fr = new FileReader(file);
        int val;
        StringBuilder sb = new StringBuilder();
        while ((val = fr.read()) != -1) {
            sb.append((char) val);
        }
        return sb.toString();
    }
}
