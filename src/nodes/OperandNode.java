package src.nodes;

import src.JottTree;
import src.TokenDequeue;
import src.TokenType;

import java.util.NoSuchElementException;

/**
 * Interface for operand nodes
 *
 * @author Ethan Hartman <ehh4525@rit.edu>
 */
public interface OperandNode extends JottTree {
    /**
     * Parses an operand node from the given tokens
     * @param tokens the tokens to parse
     * @return the parsed operand node
     * @throws NodeParseException if the tokens do not form a valid operand node
     */
    static OperandNode parseOperandNode(TokenDequeue tokens) throws NodeParseException {
        switch (tokens.getFirst().getTokenType()) {
            case TokenType.ID_KEYWORD -> { return IDNode.parseIDNode(tokens); }
            case TokenType.NUMBER -> { return NumNode.parseNumNode(tokens, false); }
            case TokenType.FC_HEADER -> { return FunctionCallNode.parseFunctionCallNode(tokens); }
            case TokenType.MATH_OP -> {
                tokens.validateFirst("-"); // Validate negative sign
                tokens.removeFirst(); // Remove negative sign
                tokens.validateFirst(TokenType.NUMBER); // Validate that there's a number
                return NumNode.parseNumNode(tokens, true);
            }
        }

        return null;
    }
}
