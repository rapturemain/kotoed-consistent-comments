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
    }

    private static void test(String path1, String path2, int line, int expectedLine, String testName) throws Exception {
        String p1 = "src\\main\\java\\checker\\test\\" + path1;
        String p2 = "src\\main\\java\\checker\\test\\" + path2;
        Pair<Integer, String> result = Checker.check(fileToString(p1), fileToString(p2), line);
        System.out.println(testName + " result: " + result.getValue());
        System.out.println(testName + " result: " + (result.getKey().equals(expectedLine) ? "OK" : "FAIL"));
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
