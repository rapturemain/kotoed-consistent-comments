package tree.builder;

import java.util.ArrayList;
import java.util.List;

public class EntryList extends ArrayList<Entry> {
    public EntryList() {
        super();
    }

    public EntryList(int size) {
        super(size);
    }

    public boolean add(int index, String string) {
        return super.add(new Entry(index, string));
    }

    public boolean add(int index, String string, int line) {
        boolean result = super.add(new Entry(index, string));
        if (result) {
            this.get(this.size() - 1).setLine(line);
        }
        return result;
    }

    public String getString(int i) {
        if (i < 0 || i >= this.size()) {
            return "";
        }
        return this.get(i).getString();
    }

    public int getIndex(int i) {
        return this.get(i).getIndex();
    }

    public int getFirst(int start, String string) {
        for (int i = start; i < this.size(); i++) {
            if (this.getString(i).equals(string)) {
                return i;
            }
        }
        return -1;
    }

    public Entry getEntryWithIndex(int index) {
        for (Entry en : this) {
            if (en.getIndex() == index) {
                return en;
            }
        }
        return null;
    }

    public EntryList subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex >= this.size()) {
            throw new IndexOutOfBoundsException("Entry list size: " + this.size() +
                    " inputs: from " + fromIndex + " to " + toIndex);
        }
        if (fromIndex > toIndex) {
            return new EntryList();
        }
        EntryList sub = new EntryList(toIndex - fromIndex + 1);
        for (int i = fromIndex; i <= toIndex; i++) {
            sub.add(this.get(i));
        }
        return sub;
    }

    public List<String> stringList() {
        List<String> strings = new ArrayList<>(this.size());
        for (Entry en : this) {
            strings.add(en.getString());
        }
        return strings;
    }
}
