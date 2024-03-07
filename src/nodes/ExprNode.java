package src.nodes;

import src.JottTree;
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
        if (tokens.isFirstOf(TokenType.STRING)) {
            if (tokens.isFirstOf("True") || tokens.isFirstOf("False")) {
//                return BoolNode.parseBoolNode(tokens);
                return null;
                //TODO adapt to Lianna's code
            } else {
//                return StringLitteralNode.parseStringLitteralNode(tokens);
                return null;
                //TODO adapt to Lianna's code
            }
        } else {
            OperandNode operandNode = OperandNode.parseOperandNode(tokens);
            if (tokens.isFirstOf(TokenType.REL_OP)) {
//                RelOpNode relOpNode = RelOpNode.parseRelOpNode(tokens);
//                tokens.validateFirst(TokenType.NUMBER, TokenType.FC_HEADER, TokenType.ID_KEYWORD);
//                OperandNode operandNode1 = OperandNode.parseOperandNode(tokens);
//                return ...
                return null;
            } else if (tokens.isFirstOf(TokenType.MATH_OP)) {
//                MathOpNode mathOpNode = MathOpNode.parseRelOpNode(tokens);
//                tokens.validateFirst(TokenType.NUMBER, TokenType.FC_HEADER, TokenType.ID_KEYWORD);
//                OperandNode operandNode1 = OperandNode.parseOperandNode(tokens);
//                return ...
                return null;
            }

            return OperandNode.parseOperandNode(tokens);
        }

    }
}
