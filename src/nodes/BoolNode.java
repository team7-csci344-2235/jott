package src.nodes;

import src.JottTree;
import src.TokenDeque;
import src.TokenType;

/**
 * Class for Boolean nodes
 *
 * @author Lianna Pottgen, <lrp2755@rit.edu>
 */
public class BoolNode implements JottTree, ExprNode{
    private final String trueOrFalse;

    private BoolNode(String trueOrFalse) {
        this.trueOrFalse = trueOrFalse;
    }

    public static BoolNode parseBoolNode(TokenDeque tokens) throws NodeParseException {
        //get information
        tokens.validateFirst(TokenType.ID_KEYWORD); 

        //if "TRUE", "true", or any combination
            //return "True"
        //else if "FALSE", "false", or any combination
            //return "False"
        tokens.validateFirst("True", "False");
        return new BoolNode(tokens.removeFirst().getToken());
    }

    @Override
    public String convertToJott() {
        return trueOrFalse;
    }

    @Override
    public String convertToJava(String className) {
        return null;
    }

    @Override
    public String convertToC() {
        return null;
    }

    @Override
    public String convertToPython() {
        return null;
    }

    @Override
    public boolean validateTree() {
        return false;
    }
}

