package src.nodes;

import src.JottTree;
import src.TokenDeque;
import src.TokenType;

/**
 * Class for RelOp nodes (<, >, =, ==, <=, >=)
 *
 * @author Lianna Pottgen, <lrp2755@rit.edu>  //infinite loop issues
 */
public class RelOpNode implements JottTree, ExprNode {
    private final String relationalValue;


    private RelOpNode(String relationalValue) {
        this.relationalValue = relationalValue;
    }

    public static RelOpNode parseRelOpNode(OperandNode firstOp, TokenDeque tokens) throws NodeParseException {
        //get information
        tokens.validateFirst(TokenType.REL_OP); 
        return new RelOpNode(tokens.removeFirst().getToken());
    }

    @Override
    public String convertToJott() {
        return relationalValue;
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

