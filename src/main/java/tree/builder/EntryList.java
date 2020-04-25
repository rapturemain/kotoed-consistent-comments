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

    public String getString(int i) {
        return this.get(i).getString();
    }

    public int getIndex(int i) {
        return this.get(i).getIndex();
    }

    public int findFirst(int start, String string) {
        for (int i = start; i < this.size(); i++) {
            if (this.getString(i).equals(string)) {
                return i;
            }
        }
        return -1;
    }

    public EntryList subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || fromIndex > toIndex || toIndex >= this.size()) {
            throw new IndexOutOfBoundsException("Entry list size: " + this.size() +
                    " inputs: from " + fromIndex + " to " + toIndex);
        }
        EntryList sub = new EntryList(toIndex - fromIndex + 1);
        for (int i = fromIndex; i <= toIndex; i++) {
            sub.add(this.get(i));
        }
        return sub;
    }
}
