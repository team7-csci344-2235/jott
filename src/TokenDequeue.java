package src;

import java.util.ArrayDeque;
import java.util.List;

/**
 * Dequeue which stores tokens and keeps track of the last removed token.
 * Removes head in O(1) time.
 */
public class TokenDequeue {
    private final ArrayDeque<Token> tokens;
    private Token lastRemoved;

    public TokenDequeue(List<Token> tokens) {
        this.tokens = new ArrayDeque<>(tokens);
    }

    /**
     * @return the first element in the TokenList
     * @throws java.util.NoSuchElementException if the TokenList is empty
     */
    public Token getFirst() {
        return tokens.getFirst();
    }

    /**
     * Checks if the first token type equals the other
     * @param type the TokenType to compare to
     * @return the first element's TokenType in the TokenList
     * @throws java.util.NoSuchElementException if the TokenList is empty
     */
    public boolean isFirstOfType(TokenType type) {
        return tokens.getFirst().getTokenType() == type;
    }

    /**
     * Removes and returns the first element from the TokenList in O(1) time.
     * @return the first element in the TokenList
     * @throws java.util.NoSuchElementException if the TokenList is empty
     */
    public Token removeFirst() {
        return lastRemoved = tokens.removeFirst();
    }

    /**
     * Gets the last removed element if there is one.
     * @return the last removed element, T or null.
     */
    public Token getLastRemoved() {
        return lastRemoved;
    }

    /**
     * @return true if the TokenList is empty, false otherwise
     */
    public boolean isEmpty() {
        return tokens.isEmpty();
    }

    /**
     * @return the number of elements in the TokenList
     */
    public int size() {
        return tokens.size();
    }
}
