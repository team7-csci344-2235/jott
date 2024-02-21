package src;

import src.nodes.*;

import java.util.ArrayList;

/**
 * This class is responsible for paring Jott Tokens
 * into a Jott parse tree.
 *
 * @author Ethan Hartman <ehh4525@rit.edu>
 */
public class JottParser {
    /**
     * Parses the given tokens into a Jott parse tree
     *
     * @param tokens the tokens to parse
     * @return the parsed Jott parse tree
     */
    public static JottTree parse(ArrayList<Token> tokens) {
        try {
            return ProgramNode.parseProgramNode(new TokenDequeue(tokens));
        } catch (JottTree.NodeParseException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
