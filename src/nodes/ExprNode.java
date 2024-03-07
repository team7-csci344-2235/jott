package src.nodes;

import src.JottTree;
import src.TokenDeque;

/**
 * Class for Expr nodes
 *
 * @author Adrienne Ressy <amr3032@rit.edu>
 */
public interface ExprNode extends JottTree {
    static ExprNode parseExprNode(TokenDeque tokens) throws NodeParseException {
        // TODO: Implement
        return null;
    }
}
