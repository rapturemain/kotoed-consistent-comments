package checker.test;

import checker.Checker;
import javafx.util.Pair;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws Exception {
        test("funBuildTest1.txt", "funBuildTest2.txt", 17, 26, "Test funBuildTest");
        test("test2old.txt", "test2new.txt", 5, 4, "Test test2");
        test("test3old.txt", "test3new.txt", 10, 10, "Test test3");
        test("test4old.txt", "test4new.txt", 7, 11, "Test test4");
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
        for (int i = 1; i <= countLines; i++) {
            Pair<Integer, String> result = Checker.check(file1, file1, i);
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
