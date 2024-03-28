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
            super("Syntax Error:\n" +
                    "Expected " + String.join(" or ", expected) +
                            " but got " + got.getToken() + " instead \n" +
                    got.getFilename() + ":" + got.getLineNum());
        }

        public NodeParseException(int previousLine, String filename, String... expected) {
            super("Syntax Error:\n" +
                    "Expected " + String.join(" or ", expected) + " but got nothing instead \n" +
                    filename + ":" + previousLine);
        }

        public NodeParseException(Token got, TokenType... expected) {
            super("Syntax Error:\n" +
                    "Expected " + Arrays.stream(expected).map(TokenType::name).collect(Collectors.joining(" or ")) +
                    " but got " + got.getToken() + " instead \n" +
                    got.getFilename() + ":" + got.getLineNum());
        }

        public NodeParseException(Token got, String expected) {
            super("Syntax Error:\n" +
                    "Expected " + expected +
                    " but got " + got.getToken() + " ainstead \n" +
                    got.getFilename() + ":" + got.getLineNum());
        }

        public NodeParseException(int previousLine, String filename, TokenType... expected) {
            super("Syntax Error:\n" +
                    "Expected " + Arrays.stream(expected).map(TokenType::name).collect(Collectors.joining(" or ")) + " but got nothing instead \n" +
                    filename + ":" + previousLine);
        }
    }

    /**
     * Exception for when a node is invalid.
     * @author Sebastian LaVine <sml1040@rit.edu>
     */
    class NodeValidateException extends Exception {
        public NodeValidateException(String message, String filename, int line) {
            super("Semantic Error:\n"
                    + message + "\n"
                    + filename + ":" + line);
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
     * @return void if valid Jott code; throws NodeValidateException if not
     */
    public void validateTree() throws NodeValidateException;
}
