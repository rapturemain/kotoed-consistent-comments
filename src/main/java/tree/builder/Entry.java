package tree.builder;

public class Entry {
    public Entry(int index, String string) {
        this.index = index;
        this.string = string;
    }

    private int index;
    private String string;

    public int getIndex() {
        return index;
    }

    public String getString() {
        return string;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "index=" + index +
                ", string='" + string + '\'' +
                '}';
    }
}
