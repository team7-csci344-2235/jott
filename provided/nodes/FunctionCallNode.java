package provided.nodes;

import provided.TokenDeque;
import provided.TokenType;
import provided.VariableTable;

/**
 * Class for function call nodes
 *
 * @author Ethan Hartman <ehh4525@rit.edu>
 */
public class FunctionCallNode implements OperandNode, BodyStmtNode {
    private final int startLine;
    private final String filename;
    private final IDNode idNode;
    private final ParamsNode parameters;
    private final VariableTable variableTable;

    private FunctionCallNode(int startLine, String filename, IDNode idNode, ParamsNode parameters, VariableTable variableTable) {
        this.idNode = idNode;
        this.parameters = parameters;
        this.startLine = startLine;
        this.filename = filename;
        this.variableTable = variableTable;
    }

    /**
     * Parses a function call node from the given tokens
     * @param tokens the tokens to parse
     * @return the parsed function call node
     * @throws NodeParseException if the tokens do not form a valid function call node
     */
    public static FunctionCallNode parseFunctionCallNode(TokenDeque tokens, VariableTable variableTable) throws NodeParseException {
        tokens.removeFirst(); // We know we'll have FC header first, so remove it
        tokens.validateFirst(TokenType.ID_KEYWORD);
        int startLine = tokens.getFirst().getLineNum();
        IDNode idNode = IDNode.parseIDNode(tokens);
        tokens.validateFirst(TokenType.L_BRACKET);
        tokens.removeFirst(); // Remove L bracket
        ParamsNode parameters = ParamsNode.parseParamsNode(tokens, idNode.getIdStringValue(), variableTable);
        tokens.validateFirst(TokenType.R_BRACKET);
        tokens.removeFirst();
        return new FunctionCallNode(startLine, tokens.getLastRemoved().getFilename(), idNode, parameters, variableTable);
    }

    @Override
    public String convertToJott() {
        return "::" + idNode.convertToJott() + "[" + parameters.convertToJott() + "]";
    }

    @Override
    public String convertToJava(String className) {
        return idNode.convertToJava(className) + "(" + parameters.convertToJava(className) + ")";
    }

    @Override
    public String convertToC() {
        return null;
    }

    @Override
    public String convertToPython(int tabNumber) {
        return null;
    }

    @Override
    public void validateTree() throws NodeValidateException {
        idNode.validateTree();
        // Ensure we have the function we want to call
        if (!variableTable.hasFunction(idNode.getIdStringValue()))
            throw new NodeValidateException("Call to unknown function: '" + idNode.convertToJott() + "'", filename, startLine);
        parameters.validateTree();
    }

    @Override
    public int getStartLine() {
        return startLine;
    }

    public IDNode getIdNode() {
        return idNode;
    }
}
