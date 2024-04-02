package provided;

import provided.nodes.*;

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
    public static JottTree parse(ArrayList<Token> tokens, boolean validateMode) {
        try {
            var prog = ProgramNode.parseProgramNode(new TokenDeque(tokens));
            if (validateMode)
                prog.validateTree();
            return prog;
        } catch (JottTree.NodeParseException | JottTree.NodeValidateException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
