package provided.nodes;

import provided.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static provided.nodes.ProgramNode.JOTT_LIST_COLLECTOR;

/**
 * Class for function call nodes
 * Includes params_t since a separate node would have been unnecessary.
 *
 * @author Ethan Hartman <ehh4525@rit.edu>
 */
public class ParamsNode implements JottTree {
    private final int startLine;
    private final ArrayList<ExprNode> expressions;
    private final String filename, functionName;
    private final VariableTable variableTable;

    private ParamsNode(String filename, int startLine, ArrayList<ExprNode> expressions, String functionName, VariableTable variableTable) {
        this.expressions = expressions;
        this.filename = filename;
        this.startLine = startLine;
        this.functionName = functionName;
        this.variableTable = variableTable;
    }

    /**
     * Returns the expressions that make up this ParamsNode.
     * @return constituent expressions
     */
    public ArrayList<ExprNode> getExpressions() { return this.expressions; }

    /**
     * Parses a params node from the given tokens
     * @param tokens the tokens to parse
     * @return the parsed params node
     * @throws NodeParseException if the tokens do not form a valid params node
     */
    public static ParamsNode parseParamsNode(TokenDeque tokens, String functionName, VariableTable variableTable) throws NodeParseException {
        ArrayList<ExprNode> expressions = null;
        Token lastToken = tokens.getLastRemoved();
        if (!tokens.isFirstOf(TokenType.R_BRACKET)) { // Has expressions.
            expressions = new ArrayList<>();
            for (;;) {
                expressions.add(ExprNode.parseExprNode(tokens, variableTable)); // We should have expressions here, let's parse.
                if (tokens.isFirstOf(TokenType.COMMA))
                    tokens.removeFirst(); // Remove comma
                else break;
            }
        }
        return new ParamsNode(lastToken.getFilename(), lastToken.getLineNum(), expressions, functionName, variableTable);
    }

    @Override
    public String convertToJott() {
        if (expressions == null) return "";
        return expressions.stream().map(ExprNode::convertToJott).collect(JOTT_LIST_COLLECTOR);
    }

    @Override
    public String convertToJava(String className) {
        if (expressions == null){
            return "";
        }
        return this.expressions.stream().map(ignore -> ignore.convertToJava(className)).collect(JOTT_LIST_COLLECTOR);
    }

    @Override
    public String convertToC() {
        if (expressions == null) return "";
        return expressions.stream().map(ExprNode::convertToC).collect(JOTT_LIST_COLLECTOR);
    }

    @Override
    public String convertToPython() {
        if (expressions == null) {
            return "";
        }
        return expressions.stream().map(JottTree::convertToPython).collect(Collectors.joining(", "));
    }

    @Override
    public void validateTree() throws NodeValidateException {
        // At this point, we know the function is defined.
        ArrayList<TypeNode.VariableType> requiredTypes = variableTable.getFunctionParams(functionName);
        if ((expressions == null ? 0 : expressions.size()) != (requiredTypes != null ? requiredTypes.size() : 0)) // Param length check
            throw new NodeValidateException("Invalid number of parameters in function call", filename, startLine);
        else if (expressions == null) return; // No expressions required, no need to validate further.

        // Validate expressions and check if they match the required types.
        ExprNode expr;
        for (int i = 0; i < expressions.size(); i++) {
            expr = expressions.get(i);
            expr.validateTree();

            TypeNode.VariableType evalType = switch (expr) {
                case BoolNode ignored -> TypeNode.VariableType.BOOLEAN;
                case RelOpNode ignored -> TypeNode.VariableType.BOOLEAN;
                case StringLiteralNode ignored -> TypeNode.VariableType.STRING;
                case OperandNode operandNode -> OperandNode.getOperandType(operandNode, variableTable, filename);
                case MathOpNode mathOpNode -> mathOpNode.getOperandType();
                default -> throw new IllegalStateException("Unexpected value: " + expr); // Should never happen.
            };

            if (requiredTypes.get(i) != TypeNode.VariableType.ANY && evalType != requiredTypes.get(i))
                throw new NodeValidateException("Invalid parameter type in function call: '" + expr.convertToJott() + "'\nExpected: '" + requiredTypes.get(i) + "' but got type: '" + evalType + "' instead.", filename, expr.getStartLine());
        }
    }
}
