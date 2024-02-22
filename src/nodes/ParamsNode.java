package src.nodes;

import src.JottTree;
import src.TokenDeque;
import src.TokenType;

import java.util.ArrayList;

import static src.nodes.ProgramNode.JOTT_LIST_COLLECTOR;

/**
 * Class for function call nodes
 * Includes params_t since a separate node would have been unnecessary.
 *
 * @author Ethan Hartman <ehh4525@rit.edu>
 */
public class ParamsNode implements JottTree {
    private final ArrayList<ExprNode> expressions;

    private ParamsNode(ArrayList<ExprNode> expressions) {
        this.expressions = expressions;
    }

    private ParamsNode() {
        this.expressions = null;
    }

    /**
     * Parses a params node from the given tokens
     * @param tokens the tokens to parse
     * @return the parsed params node
     * @throws NodeParseException if the tokens do not form a valid params node
     */
    public static ParamsNode parseParamsNode(TokenDeque tokens) throws NodeParseException {
        if (tokens.isFirstOf(TokenType.R_BRACKET)) // No expressions, empty params.
            return new ParamsNode();

        ArrayList<ExprNode> expressions = new ArrayList<>();
        for (;;) {
            expressions.add(ExprNode.parseExprNode(tokens)); // We should have expressions here, let's parse.
            if (tokens.isFirstOf(TokenType.COMMA))
                tokens.removeFirst(); // Remove comma
            else
                return new ParamsNode(expressions);
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
    public boolean validateTree() {
        return false;
    }
}
