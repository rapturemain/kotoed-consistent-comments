package tree.builder;

public class Entry {
    public Entry(int index, String string) {
        this.index = index;
        this.string = string;
    }

    private int index;
    private String string;
    private int line = 0;


    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String getString() {
        return string;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return "Entry:" +
                "index= " + index +
                ", string= '" + string + '\'' +
                ", line= " + line;
    }
}
