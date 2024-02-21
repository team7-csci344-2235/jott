package src.nodes;

import src.TokenDequeue;
import src.TokenType;

import java.util.NoSuchElementException;

/**
 * Class for function call nodes
 *
 * @author Ethan Hartman <ehh4525@rit.edu>
 */
public class FunctionCallNode implements OperandNode {
    private final IDNode idNode;
    private final ParamsNode parameters;

    private FunctionCallNode(IDNode idNode, ParamsNode parameters) {
        this.idNode = idNode;
        this.parameters = parameters;
    }

    /**
     * Parses a function call node from the given tokens
     * @param tokens the tokens to parse
     * @return the parsed function call node
     * @throws NodeParseException if the tokens do not form a valid function call node
     */
    public static FunctionCallNode parseFunctionCallNode(TokenDequeue tokens) throws NodeParseException {
        try {
            tokens.removeFirst(); // We know we'll have FC header first, so remove it
            if (tokens.isFirstOfType(TokenType.ID_KEYWORD)) {
                IDNode idNode = IDNode.parseIDNode(tokens);
                if (tokens.isFirstOfType(TokenType.L_BRACKET)) {
                    ParamsNode parameters = ParamsNode.parseParamsNode(tokens);
                    if (tokens.isFirstOfType(TokenType.R_BRACKET)) {
                        tokens.removeFirst();
                        return new FunctionCallNode(idNode, parameters);
                    } else {
                        throw new NodeParseException(tokens.getFirst(), TokenType.R_BRACKET);
                    }
                } else {
                    throw new NodeParseException(tokens.getFirst(), TokenType.L_BRACKET);
                }
            } else {
                throw new NodeParseException(tokens.getFirst(), TokenType.ID_KEYWORD);
            }
        } catch (NoSuchElementException e) {
            // If any of the getFirst calls throw an exception, it means we have no more tokens
            throw new NodeParseException(tokens.getLastRemoved().getLineNum(), TokenType.ID_KEYWORD);
        }
    }

    @Override
    public String convertToJott() {
        return "::" + idNode.convertToJott() + "[" + parameters.convertToJott() + "]";
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
