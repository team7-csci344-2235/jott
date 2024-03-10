package src.nodes;

import src.JottTree;
import src.Token;
import src.TokenDeque;
import src.TokenType;

/**
 * Class for Expr nodes
 *
 * @author Adrienne Ressy <amr3032@rit.edu>
 */
public interface ExprNode extends JottTree {
    static ExprNode parseExprNode(TokenDeque tokens) throws NodeParseException {

        // Ensure first is of one of the following tokens.
        tokens.validateFirst(TokenType.STRING, TokenType.NUMBER, TokenType.FC_HEADER, TokenType.ID_KEYWORD);

        if (tokens.isFirstOf(TokenType.ID_KEYWORD)
                && tokens.isFirstOf("True", "False")) {
            return BoolNode.parseBoolNode(tokens);
        } else if (tokens.isFirstOf(TokenType.STRING)) {
            return StringLiteralNode.parseStringLiteralNode(tokens);
        } else {
            OperandNode operandNode = OperandNode.parseOperandNode(tokens);

            if (tokens.isFirstOf(TokenType.REL_OP)) {
                RelOpNode relOpNode = RelOpNode.parseRelOpNode(operandNode, tokens);
                return relOpNode;

            } else if (tokens.isFirstOf(TokenType.MATH_OP)) {
                MathOpNode mathOpNode = MathOpNode.parseMathNode(operandNode, tokens);

                return mathOpNode;
            } else {
                return operandNode; // Just the one operand.
            }
        }

    }
}
