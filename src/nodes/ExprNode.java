package src.nodes;

import src.JottTree;
import src.TokenDeque;

/**
 * Class for Expr nodes
 *
 * @author TODO
 */
public class ExprNode implements JottTree {
    private ExprNode() {
        // TODO: Implement this method and arguments
    }

    public static ExprNode parseExprNode(TokenDeque tokens) throws NodeParseException {
        // TODO: Implement
        return null;
    }

    @Override
    public String convertToJott() {
        // TODO: Implement
        return null;
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
