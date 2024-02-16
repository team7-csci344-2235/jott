package src;

import src.nodes.FunctionCallNode;
import src.nodes.OperandNode;
import src.nodes.ParamsNode;
import src.nodes.SimpleNode;

import java.util.ArrayList;
import java.util.Set;

/**
 * This class is responsible for paring Jott Tokens
 * into a Jott parse tree.
 *
 * @author Ethan Hartman <ehh4525@rit.edu>
 */
public class JottParser {
    public final static String LIST_SEP = ", ";
    private final static Set<String>
            BOOLEANS = Set.of("True", "False"),
            VAR_TYPES = Set.of("Double", "Integer", "String", "Boolean");
    private final ArrayList<Token> tokens;
    private int currentIndex;

    /**
     * @param tokens the ArrayList of Jott tokens to parse
     */
    public JottParser(ArrayList<Token> tokens) {
        this.tokens = tokens;
        this.currentIndex = 0;
    }

    /**
     * Parses the parser's ArrayList of Jott tokens into a Jott Parse Tree.
     * @return the root of the Jott Parse Tree represented by the tokens.
     *         or null upon an error in parsing.
     */
    public JottTree parse() {
        // TODO
        return null;
    }

    // Parse simple objects such as: variable types, booleans
    private JottTree parseSimple(TokenType desiredType, Set<String> validStrings) {
        // Check if the current token represents a valid type
        if (currentIndex < tokens.size() && isValidSimpleToken(tokens.get(currentIndex), desiredType, validStrings)) {
            return new SimpleNode(tokens.get(currentIndex++).getToken());
        } else {
            // Handle syntax error: Invalid type
            // ...
            return null;
        }
    }

    private JottTree parseExpression() {
        // TODO
        return null;
    }

    private ArrayList<JottTree> parseParamsTail() {
        ArrayList<JottTree> paramsTail = new ArrayList<>();

        // Parse additional expressions (if any) separated by commas
        while (currentIndex < tokens.size() && isType(tokens.get(currentIndex), TokenType.COMMA)) {
            // Consume comma
            currentIndex++;

            // Parse predicted expression and append it
            paramsTail.add(parseExpression());
        }

        return paramsTail;
    }

    private ParamsNode parseParameters() {
        ArrayList<JottTree> expressions = new ArrayList<>();

        // Parse the first expression (maybe)
        JottTree firstExpression = parseExpression();
        if (firstExpression != null)
            expressions.add(firstExpression);

        // Parse additional expressions (if any)
        ArrayList<JottTree> tailParams = parseParamsTail();
        if (!(expressions.isEmpty() || tailParams.isEmpty())) {
            expressions.addAll(tailParams);
        } else {
            // Handle syntax error: Expected expression before comma [,a...]
            // ...
        }

        return new ParamsNode(expressions);
    }

    private FunctionCallNode parseFunctionCall() {
        if (currentIndex + 2 < tokens.size() &&
                isType(tokens.get(currentIndex), TokenType.FC_HEADER) &&
                isType(tokens.get(currentIndex + 1), TokenType.L_BRACKET) &&
                isType(tokens.get(currentIndex + 2), TokenType.ID_KEYWORD)) {

            currentIndex += 2; // Skip colons, left bracket and move to identifier

            String functionName = tokens.get(currentIndex++).getToken();

            // Parse parameters
            ParamsNode parameters = parseParameters();

            // Ensure that the closing square bracket is present
            if (currentIndex < tokens.size() && tokens.get(currentIndex).getTokenType() == TokenType.R_BRACKET) {
                currentIndex++;
                return new FunctionCallNode(functionName, parameters);
            } else {
                // Handle syntax error: Missing closing square bracket
                // ...
            }
        }

        // Handle syntax error: Not enough tokens for function call
        // ...
        return null;
    }

    private JottTree parseOperand() {
        boolean isNegative = false;

        // Check for negative sign
        if (currentIndex < tokens.size() && isValidSimpleToken(tokens.get(currentIndex), TokenType.MATH_OP, "-")) {
            isNegative = true;
            currentIndex++;
        }

        if (currentIndex < tokens.size()) {
            // Check if the operand is a number or identifier
            if (isType(tokens.get(currentIndex), TokenType.NUMBER) || isType(tokens.get(currentIndex), TokenType.ID_KEYWORD))
                return new OperandNode(tokens.get(currentIndex++).getToken(), isNegative);

            // Check if the operand is a function call
            if (currentIndex + 1 < tokens.size() && isType(tokens.get(currentIndex + 1), TokenType.ID_KEYWORD) && isType(tokens.get(currentIndex), TokenType.FC_HEADER))
                return new OperandNode(parseFunctionCall());
        }

        // Handle syntax error: Invalid operand
        // ...

        return null;
    }

    // Check if the given token is a certain type
    private boolean isType(Token token, TokenType desiredType) {
        return token.getTokenType().equals(desiredType);
    }

    // Check if the given token is a certain type and token is contained within validStrings
    private boolean isValidSimpleToken(Token token, TokenType desiredType, Set<String> validStrings)  {
        return isType(token, desiredType) && validStrings.contains(token.getToken());
    }

    // Check if the given token is a certain type and token is equal to the validString
    private boolean isValidSimpleToken(Token token, TokenType desiredType, String validString)  {
        return isType(token, desiredType) && validString.equals(token.getToken());
    }
}
