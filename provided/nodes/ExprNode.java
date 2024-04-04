package provided.nodes;

import provided.JottTree;
import provided.TokenDeque;
import provided.TokenType;
import provided.VariableTable;

/**
 * Class for Expr nodes
 *
 * @author Adrienne Ressy <amr3032@rit.edu>
 */
public interface ExprNode extends JottTree {
    static ExprNode parseExprNode(TokenDeque tokens, VariableTable variableTable) throws NodeParseException {

        // Ensure first is of one of the following tokens.
        tokens.validateFirst(TokenType.STRING, TokenType.NUMBER, TokenType.FC_HEADER, TokenType.ID_KEYWORD);

        if (tokens.isFirstOf(TokenType.ID_KEYWORD)
                && tokens.isFirstOf("True", "False")) {
            return BoolNode.parseBoolNode(tokens);
        } else if (tokens.isFirstOf(TokenType.STRING)) {
            return StringLiteralNode.parseStringLiteralNode(tokens);
        } else {
            OperandNode operandNode = OperandNode.parseOperandNode(tokens, variableTable);

            if (tokens.isFirstOf(TokenType.REL_OP)) {
                return RelOpNode.parseRelOpNode(operandNode, tokens, variableTable);
            } else if (tokens.isFirstOf(TokenType.MATH_OP)) {
                return MathOpNode.parseMathNode(operandNode, tokens, variableTable);
            } 
            else {
                return operandNode;
            }
        }
    }

    /**
     * Get the start line of the expression
     * @return the start line of the expression
     */
    int getStartLine();
}
