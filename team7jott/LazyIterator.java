package team7jott;

import java.util.Iterator;

public class LazyIterator implements Iterable<Character> {
    private int idx = 0;
    private final int lineNum;
    private final String text, filePath;

    public LazyIterator(String text, String filePath, int lineNum) {
        this.text = text;
        this.lineNum = lineNum;
        this.filePath = filePath;
    }

    public void back() {
        idx--;
    }

    public int getLineNum() {
        return lineNum;
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public Iterator<Character> iterator() {
        return new CustomIterator();
    }

    private class CustomIterator implements Iterator<Character> {
        @Override
        public boolean hasNext() {
            return idx < text.length();
        }

        @Override
        public Character next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            return text.charAt(idx++);
        }
    }
}
