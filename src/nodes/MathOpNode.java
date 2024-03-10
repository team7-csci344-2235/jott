package src.nodes;

import src.JottTree;
import src.TokenDeque;
import src.TokenType;

/**
 * Class for MathOp nodes
 *
 * @author Lianna Pottgen, <lrp2755@rit.edu>
 */
public class MathOpNode implements JottTree, ExprNode {
    private final String mathOpString;

    private MathOpNode(String value) {
        this.mathOpString = value;
    }

    public static MathOpNode parseMathNode(OperandNode firstOp, TokenDeque tokens) throws NodeParseException {
        //get information
        tokens.validateFirst(TokenType.ID_KEYWORD); 
        return new MathOpNode(tokens.removeFirst().getToken());
    }

    @Override
    public String convertToJott() {
        return mathOpString;
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

