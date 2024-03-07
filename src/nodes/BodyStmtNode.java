package src.nodes;

import src.JottTree;
import src.TokenDeque;
import src.TokenType;

/**
 * Interface for Body Statement nodes
 *
 * @author Adrienne Ressy amr3032@g.rit.edu
 */
public interface BodyStmtNode extends JottTree {



    static BodyStmtNode parseBodyStmtNode(TokenDeque tokens) throws NodeParseException {
//        // Ensure first is of one of the following tokens.
//        tokens.validateFirst(TokenType.ID_KEYWORD, TokenType.NUMBER, TokenType.FC_HEADER, TokenType.MATH_OP);
//        switch (tokens.getFirst().getTokenType()) {
//            case TokenType.ID_KEYWORD -> {
//                tokens.removeFirst(); // Remove semicolon
//                return IDNode.parseIDNode(tokens);
//            }
//            case TokenType.NUMBER -> {
//                tokens.removeFirst(); // Remove semicolon
//                return NumNode.parseNumNode(tokens, false);
//            }
//            case TokenType.FC_HEADER -> {
//                tokens.removeFirst(); // Remove semicolon
//                return FunctionCallNode.parseFunctionCallNode(tokens);
//            }
//            case TokenType.MATH_OP -> {
//                tokens.validateFirst("-"); // Validate negative sign
//                tokens.removeFirst(); // Remove negative sign
//                tokens.validateFirst(TokenType.NUMBER); // Validate that there's a number
//                tokens.removeFirst(); // Remove semicolon
//                return NumNode.parseNumNode(tokens, true);
//            }
        return null;
    }
}
