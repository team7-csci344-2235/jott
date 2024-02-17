package src;

import src.nodes.*;

import java.util.ArrayList;
import java.util.Set;

/**
 * This class is responsible for paring Jott Tokens
 * into a Jott parse tree.
 *
 * @author Ethan Hartman <ehh4525@rit.edu>
 */
public class JottParser {
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
     * < function_def >* < EOF >
     *
     * @return the root of the Jott Parse Tree represented by the tokens.
     *         or null upon an error in parsing.
     */
    public JottTree parseProgram() {
        ArrayList<JottTree> functionDefs = parseFunctionDefs();

        // Ensure that the parsing reached the end of the input
        if (currentIndex < tokens.size()) {
            // Handle syntax error: Unexpected tokens after function definitions
            // ...
        }

        return new ProgramNode(functionDefs);
    }

    private ArrayList<JottTree> parseFunctionDefs() {
        // TODO create FunctionDefNode, finish implementation.
        return null;
    }


    // Parse simple objects such as: variable types, booleans
    private JottTree parseSimple(TokenType desiredType, Set<String> validStrings) {
        // Check if the current token represents a valid type
        if (currentIndex < tokens.size() && isValidSimpleToken(desiredType, validStrings, 0)) {
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
        while (currentIndex < tokens.size() && isCurTokenType(TokenType.COMMA, 0)) {
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
                isCurTokenType(TokenType.FC_HEADER, 0) &&
                isCurTokenType(TokenType.L_BRACKET, 1) &&
                isCurTokenType(TokenType.ID_KEYWORD, 2)) {

            currentIndex += 2; // Skip colons, left bracket and move to identifier

            String functionName = tokens.get(currentIndex++).getToken();

            // Parse parameters
            ParamsNode parameters = parseParameters();

            // Ensure that the closing square bracket is present
            if (currentIndex < tokens.size() && isCurTokenType(TokenType.R_BRACKET, 0)) {
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
        if (currentIndex < tokens.size() && isValidSimpleToken(TokenType.MATH_OP, "-", 0)) {
            isNegative = true;
            currentIndex++;
        }

        if (currentIndex < tokens.size()) {
            // Check if the operand is a number or identifier
            if (isCurTokenType(TokenType.NUMBER, 0) || isCurTokenType(TokenType.ID_KEYWORD, 0))
                return new OperandNode(tokens.get(currentIndex++).getToken(), isNegative);

            // Check if the operand is a function call
            if (currentIndex + 1 < tokens.size() &&
                    isCurTokenType(TokenType.ID_KEYWORD, 1) &&
                    isCurTokenType(TokenType.FC_HEADER, 0))
                return new OperandNode(parseFunctionCall());
        }

        // Handle syntax error: Invalid operand
        // ...

        return null;
    }

    // Check if the token at (current index + offset) is a certain type
    private boolean isCurTokenType(TokenType desiredType, int tokenIdxOffset) {
        return tokens.get(tokenIdxOffset).getTokenType().equals(desiredType);
    }

    // Check if the token at (current index + offset) is a certain type and token is contained within validStrings
    private boolean isValidSimpleToken(TokenType desiredType, Set<String> validStrings, int tokenIdxOffset)  {
        return isCurTokenType(desiredType, tokenIdxOffset) && validStrings.contains(tokens.get(currentIndex + tokenIdxOffset).getToken());
    }

    // Check if the token at (current index + offset) is a certain type and token is equal to the validString
    private boolean isValidSimpleToken(TokenType desiredType, String validString, int tokenIdxOffset)  {
        return isCurTokenType(desiredType, tokenIdxOffset) && validString.equals(tokens.get(currentIndex + tokenIdxOffset).getToken());
    }
}
