package checker.test;

import checker.Checker;
import javafx.util.Pair;
import tree.Tree;
import tree.builder.Builder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws Exception {
        test("funBuildTest1.txt", "funBuildTest2.txt", 25, 26, "Test funBuildTest");
        test("test2old.txt", "test2new.txt", 5, 4, "Test test2");
        test("test3old.txt", "test3new.txt", 10, 10, "Test test3");
        test("test4old.txt", "test4new.txt", 7, 11, "Test test4");

        test("test5old.txt", "test5new.txt", 31, 34, "Test test5 31");
        test("test5old.txt", "test5new.txt", 39, 25, "Test test5 29");
        test("test5old.txt", "test5new.txt", 52, 57, "Test test5 52");
        test("test5old.txt", "test5new.txt", 60, 65, "Test test5 60");

        test("test6old.txt", "test6new.txt", 3, 3, "Test test6 3");
        test("test6old.txt", "test6new.txt", 4, 4, "Test test6 4");
        test("test6old.txt", "test6new.txt", 8, 7, "Test test6 8");
        test("test6old.txt", "test6new.txt", 13, 16, "Test test6 16");

        testUnchanged("funBuildTest1.txt", "Test funBuildTest1");
        testUnchanged("funBuildTest2.txt", "Test funBuildTest2");
        testUnchanged("test2new.txt", "Test test2new");
        testUnchanged("test2old.txt", "Test test2old");
        testUnchanged("test3new.txt", "Test test3new");
        testUnchanged("test3old.txt", "Test test3old");
        testUnchanged("kotlinasfirst\\Simple.txt", "Test Simple");
        testUnchanged("kotlinasfirst\\IfElse.txt", "Test IfElse");
        testUnchanged("kotlinasfirst\\Logical.txt", "Test Logical");
        testUnchanged("kotlinasfirst\\Loop.txt", "Test Loop");
        testUnchanged("kotlinasfirst\\List.txt", "Test List");
        testUnchanged("kotlinasfirst\\Map.txt", "Test Map");
        testUnchanged("kotlinasfirst\\Files.txt", "Test Files");
        testUnchanged("kotlinasfirst\\Geometry.txt", "Test Geometry");
        testUnchanged("kotlinasfirst\\Chess.txt", "Test Chess");
        testUnchanged("kotlinasfirst\\Graph.txt", "Test Graph");
        testUnchanged("kotlinasfirst\\Matrix.txt", "Test Matrix");
        testUnchanged("kotlinasfirst\\Matrices.txt", "Test Matrices");
    }

    private static void test(String path1, String path2, int line, int expectedLine, String testName) throws Exception {
        String p1 = "src\\main\\java\\checker\\test\\" + path1;
        String p2 = "src\\main\\java\\checker\\test\\" + path2;
        Pair<Integer, String> result = Checker.check(fileToString(p1), fileToString(p2), line);
        System.out.println(testName + " result: " + result.getValue());
        System.out.println(testName + " result: " + (result.getKey().equals(expectedLine) ? "OK" :
                "FAIL <------------------------------\n /\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\"));
        System.out.println(" ============================= ");
    }

    private static void testUnchanged(String path1, String testName) throws Exception {
        String p1 = "src\\main\\java\\checker\\test\\" + path1;
        String file1 = fileToString(p1);
        int countLines = 0;
        for (int i = 0; i < file1.length(); i++) {
            if (file1.charAt(i) == '\n') {
                countLines++;
            }
        }
        Tree tree1 = Builder.build(file1);
        Tree tree2 = Builder.build(file1);
        for (int i = 1; i <= countLines; i++) {
            Pair<Integer, String> result = Checker.check(tree1, tree2, i);
            if (result.getKey() != i) {
                if (result.getKey() != -2) {
                    System.out.println(result.getValue());
                    System.out.println("Expected: " + i);
                    System.out.println(testName + " unchanged result: " +
                            "FAIL <------------------------------\n /\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\");
                    System.out.println(" ============================= ");
                    return;
                }
            }
        }
        System.out.println(testName + " unchanged result: " + "OK");
        System.out.println(" ============================= ");
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
