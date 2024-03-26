package provided;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Interface for all Jott parse tree nodes
 *
 * @author Scott C Johnson
 */
public interface JottTree {
    /**
     * Exception for when a node is parsed incorrectly
     *
     * @author Ethan Hartman <ehh4525@rit.edu>
     */
    class NodeParseException extends Exception {
        public NodeParseException(Token got, String... expected) {
            super("Parsing Error at line " + got.getLineNum() +
                    ": Expected " + String.join(" or ", expected) +
                    " but got '" + got.getToken() + "' instead.");
        }

        public NodeParseException(int previousLine, String... expected) {
            super("Parsing Error after line " + previousLine +
                    ": Expected " + String.join(" or ", expected) +
                    " but got nothing instead.");
        }

        public NodeParseException(Token got, TokenType... expected) {
            super("Parsing Error at line " + got.getLineNum() +
                    ": Expected token types " + Arrays.stream(expected).map(TokenType::name).collect(Collectors.joining(" or ")) +
                    " but got token type '" + got.getTokenType() + "' instead.");
        }

        public NodeParseException(Token got, String expected) {
            super("Parsing Error at line " + got.getLineNum() +
                    ": Expected '" + expected +
                    "' but got '" + got.getToken() + "' instead.");
        }

        public NodeParseException(int previousLine, TokenType... expected) {
            super("Parsing Error after line " + previousLine +
                    ": Expected token types " + Arrays.stream(expected).map(TokenType::name).collect(Collectors.joining(" or ")) +
                    " but got nothing instead.");
        }
    }

    /**
     * Will output a string of this tree in Jott
     * @return a string representing the Jott code of this tree
     */
    public String convertToJott();

    /**
     * Will output a string of this tree in Java
     * @return a string representing the Java code of this tree
     */
    public String convertToJava(String className);

    /**
     * Will output a string of this tree in C
     * @return a string representing the C code of this tree
     */
    public String convertToC();

    /**
     * Will output a string of this tree in Python
     * @return a string representing the Python code of this tree
     */
    public String convertToPython();

    /**
     * This will validate that the tree follows the semantic rules of Jott
	 * Errors validating will be reported to System.err
     * @return true if valid Jott code; false otherwise
     */
    public boolean validateTree();
}
