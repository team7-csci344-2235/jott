package provided.nodes;

import provided.TokenDeque;
import provided.TokenType;
import provided.VariableTable;

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
    static OperandNode parseOperandNode(TokenDeque tokens, VariableTable variableTable) throws NodeParseException {
        // Ensure first is of one of the following tokens.
        tokens.validateFirst(TokenType.ID_KEYWORD, TokenType.NUMBER, TokenType.FC_HEADER, TokenType.MATH_OP);
        switch (tokens.getFirst().getTokenType()){
            case ID_KEYWORD-> { return IDNode.parseIDNode(tokens); }
            case NUMBER -> { return NumNode.parseNumNode(tokens, false); }
            case FC_HEADER -> { return FunctionCallNode.parseFunctionCallNode(tokens, variableTable); }
            case MATH_OP -> {
                tokens.validateFirst("-"); // Validate negative sign
                tokens.removeFirst(); // Remove negative sign
                tokens.validateFirst(TokenType.NUMBER); // Validate that there's a number
                return NumNode.parseNumNode(tokens, true);
            }
        }

        return null;
    }

    /**
     * Gets the type of the operand node
     * @param operandNode the operand node
     * @param variableTable the variable table
     * @param filename the filename
     * @return the type of the operand node
     * @throws NodeValidateException if the operand node is invalid
     */
    static TypeNode.VariableType getOperandType(OperandNode operandNode, VariableTable variableTable, String filename) throws NodeValidateException {
        if (operandNode instanceof IDNode) {
            String functionName = ((IDNode) operandNode).getIdStringValue();
            if (!variableTable.hasVariable(functionName))
                throw new NodeValidateException("Use of undeclared variable: '" + functionName + "'", filename, operandNode.getStartLine());
            return variableTable.getVariableType(functionName);
        } else if (operandNode instanceof NumNode) {
            return ((NumNode) operandNode).getVariableType();
        } else if (operandNode instanceof FunctionCallNode) {
            String functionName = ((FunctionCallNode) operandNode).getIdNode().getIdStringValue();
            if (!variableTable.hasFunction(functionName))
                throw new NodeValidateException("Call to unknown function: '" + functionName + "'", filename, operandNode.getStartLine());
            return variableTable.getFunctionReturnType(functionName);
        }
        return null;
    }
}
