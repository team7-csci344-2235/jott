package src;

import java.util.ArrayDeque;
import java.util.List;

/**
 * Deque which stores tokens and keeps track of the last removed token.
 * Removes head in O(1) time.
 */
public class TokenDeque {
    private final ArrayDeque<Token> tokens;
    private Token lastRemoved;

    public TokenDeque(List<Token> tokens) {
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
     * Checks if the first token otherType equals the other and handles exceptions
     * @param otherType the TokenType to compare to
     * @throws JottTree.NodeParseException if the TokenList is empty or the first token otherType does not equal the other
     */
    public void validateFirst(TokenType otherType) throws JottTree.NodeParseException {
        if (tokens.isEmpty())
            throw new JottTree.NodeParseException(getLastRemoved().getLineNum(), otherType);
        else if (tokens.getFirst().getTokenType() != otherType)
            throw new JottTree.NodeParseException(tokens.getFirst(), otherType);
    }

    /**
     * Checks if the first token string equals the other and handles exceptions
     * @param otherStr the other string to compare the first token's token to
     * @throws JottTree.NodeParseException if the TokenList is empty or the first token's string does not equal the other string
     */
    public void validateFirst(String otherStr) throws JottTree.NodeParseException {
        if (tokens.isEmpty())
            throw new JottTree.NodeParseException(getLastRemoved().getLineNum(), otherStr);
        else if (!tokens.getFirst().getToken().equals(otherStr))
            throw new JottTree.NodeParseException(tokens.getFirst(), otherStr);
    }

    /**
     * Checks if the first token's TokenType is one of the given types.
     * If there is no first, returns false.
     * @param types the types to check for
     * @return true if the first token's TokenType is one of the given types, false otherwise
     */
    public boolean isFirstOf(TokenType... types) {
        if (!tokens.isEmpty())
            for (TokenType type : types)
                if (tokens.getFirst().getTokenType() == type)
                    return true;
        return false;
    }

    /**
     * Checks if the first token's string is one of the given strings.
     * If there is no first, returns false.
     * @param strings the strings to check for
     * @return true if the first token's string is one of the given strings, false otherwise
     */
    public boolean isFirstOf(String... strings) {
        if (!tokens.isEmpty())
            for (String string : strings)
                if (tokens.getFirst().getToken().equals(string))
                    return true;
        return false;
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
