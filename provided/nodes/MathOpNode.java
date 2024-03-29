package provided.nodes;

import provided.JottTree;
import provided.TokenDeque;
import provided.TokenType;

/**
 * Class for MathOp nodes
 *
 * @author Lianna Pottgen, <lrp2755@rit.edu>
 */
public class MathOpNode implements JottTree, ExprNode {
    private final int startLine;
    private final String mathOpString;
    private final OperandNode firstOp;
    private final OperandNode secondOp;

    private MathOpNode(int startLine, OperandNode firstOp, String value, OperandNode secondOp) {
        this.firstOp = firstOp;
        this.mathOpString = value;
        this.secondOp = secondOp;
        this.startLine = startLine;
    }

    public static MathOpNode parseMathNode(OperandNode firstOp, TokenDeque tokens) throws NodeParseException {
        //get information
        //tokens.validateFirst(TokenType.ID_KEYWORD); 

        //if "/"
            //return "/"
        //else if "+"
            //return "+"
        //else if "-"
            //return "-"
        //else if "*"
            //return "*"
        tokens.validateFirst("/", "+", "-", "*");
        String mathOpHolder = tokens.removeFirst().getToken();
        int startLine = tokens.getLastRemoved().getLineNum();

        tokens.validateFirst(TokenType.NUMBER, TokenType.FC_HEADER, TokenType.ID_KEYWORD);
        OperandNode operandNode1 = OperandNode.parseOperandNode(tokens);

        //return mathOperation;
        return new MathOpNode(startLine, firstOp, mathOpHolder, operandNode1);
    }

    @Override
    public String convertToJott() {
        return firstOp.convertToJott() + mathOpString + secondOp.convertToJott();
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
        return;
    }

    @Override
    public TypeNode.VariableType getEvaluationVariableType() {
        // Note: The return type of math operation should always the same type as the first operand.
        // If they are different, we do not care because the validateTree method will throw the error.
        return firstOp.getEvaluationVariableType();
    }

    @Override
    public int getStartLine() {
        return startLine;
    }
}

