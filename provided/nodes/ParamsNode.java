package provided.nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenDeque;
import provided.TokenType;

import java.util.ArrayList;

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
    private final String filename;

    private ParamsNode(String filename, int startLine, ArrayList<ExprNode> expressions) {
        this.expressions = expressions;
        this.filename = filename;
        this.startLine = startLine;
    }

    private ParamsNode(String filename, int startLine) {
        this.expressions = null;
        this.filename = filename;
        this.startLine = startLine;
    }

    /**
     * Parses a params node from the given tokens
     * @param tokens the tokens to parse
     * @return the parsed params node
     * @throws NodeParseException if the tokens do not form a valid params node
     */
    public static ParamsNode parseParamsNode(TokenDeque tokens) throws NodeParseException {
        Token lastToken = tokens.getLastRemoved();
        if (tokens.isFirstOf(TokenType.R_BRACKET)) // No expressions, empty params.
            return new ParamsNode(lastToken.getFilename(), lastToken.getLineNum());

        ArrayList<ExprNode> expressions = new ArrayList<>();
        for (;;) {
            expressions.add(ExprNode.parseExprNode(tokens)); // We should have expressions here, let's parse.
            if (tokens.isFirstOf(TokenType.COMMA))
                tokens.removeFirst(); // Remove comma
            else
                return new ParamsNode(lastToken.getFilename(), lastToken.getLineNum(), expressions);
        }
    }

    @Override
    public String convertToJott() {
        if (expressions == null) return "";
        return expressions.stream().map(ExprNode::convertToJott).collect(JOTT_LIST_COLLECTOR);
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
    public void validateTree() throws NodeValidateException {
        // We will need to get the name of the function we are calling to check the required parameters.
        int defParamsLengthPlaceholder = 0;
        if ((expressions == null ? 0 : expressions.size()) != defParamsLengthPlaceholder) // Param length check
            throw new NodeValidateException("Invalid number of parameters in function call", filename, startLine);
        else if (expressions == null) return; // No expressions required, no need to validate further.

        // Assuming we have some type of ArrayList defining the required function parameter types.
        ArrayList<TypeNode.VariableType> requiredTypes = new ArrayList<>(); // Placeholder

        // Validate expressions and check if they match the required types.
        ExprNode expr;
        for (int i = 0; i < expressions.size(); i++) {
            expr = expressions.get(i);
            expr.validateTree();
            TypeNode.VariableType evalType = expr.getEvaluationVariableType();
            if (evalType != requiredTypes.get(i))
                throw new NodeValidateException("Invalid parameter type in function call: " + expr.convertToJott() + "\nExpected: '" + requiredTypes.get(i) + "' but got type: '" + evalType + "' instead.", filename, expr.getStartLine());
        }
    }
}
