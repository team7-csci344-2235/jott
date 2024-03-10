package src.nodes;

import org.w3c.dom.Node;
import src.JottTree;
import src.TokenDeque;
import src.TokenType;

import java.util.NoSuchElementException;

/**
 * Interface for operand nodes
 *
 * @author Ethan Hartman <ehh4525@rit.edu>
 * @author Lianna Pottgen <lrp2755@rit.edu>
 */
public interface OperandNode extends ExprNode {
    /**
     * Parses an operand node from the given tokens
     * @param tokens the tokens to parse
     * @return the parsed operand node
     * @throws NodeParseException if the tokens do not form a valid operand node
     */
    static OperandNode parseOperandNode(TokenDeque tokens) throws NodeParseException {
        // Ensure first is of one of the following tokens.
        tokens.validateFirst(TokenType.ID_KEYWORD, TokenType.NUMBER, TokenType.FC_HEADER, TokenType.MATH_OP);
        switch (tokens.getFirst().getTokenType()) {
            case SEMICOLON -> {return SEMICOLON}
            case ID_KEYWORD-> { return IDNode.parseIDNode(tokens); }
            case NUMBER -> { return NumNode.parseNumNode(tokens, false); }
            case FC_HEADER -> { return FunctionCallNode.parseFunctionCallNode(tokens); }
            case MATH_OP -> {
                tokens.validateFirst("-"); // Validate negative sign
                tokens.removeFirst(); // Remove negative sign
                tokens.validateFirst(TokenType.NUMBER); // Validate that there's a number
                return NumNode.parseNumNode(tokens, true);
            }
        }

        return null;
    }
}
