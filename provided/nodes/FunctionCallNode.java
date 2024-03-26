package provided.nodes;

import provided.TokenDeque;
import provided.TokenType;

/**
 * Class for function call nodes
 *
 * @author Ethan Hartman <ehh4525@rit.edu>
 */
public class FunctionCallNode implements OperandNode, BodyStmtNode {
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
    public static FunctionCallNode parseFunctionCallNode(TokenDeque tokens) throws NodeParseException {
        tokens.removeFirst(); // We know we'll have FC header first, so remove it
        tokens.validateFirst(TokenType.ID_KEYWORD);
        IDNode idNode = IDNode.parseIDNode(tokens);
        tokens.validateFirst(TokenType.L_BRACKET);
        tokens.removeFirst(); // Remove L bracket
        ParamsNode parameters = ParamsNode.parseParamsNode(tokens);
        tokens.validateFirst(TokenType.R_BRACKET);
        tokens.removeFirst();
        return new FunctionCallNode(idNode, parameters);
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
