package provided.nodes;

import provided.JottTree;
import provided.TokenDeque;
import provided.TokenType;

import java.util.Objects;

/**
 * Class for MathOp nodes
 *
 * @author Lianna Pottgen, <lrp2755@rit.edu>
 * @author Ethan Hartman <ehh4525@rit.edu>
 */
public class MathOpNode implements JottTree, ExprNode {
    private final int startLine;
    private final String filename;
    private final String mathOpString;
    private final OperandNode firstOp;
    private final OperandNode secondOp;

    private MathOpNode(int startLine, String filename, OperandNode firstOp, String value, OperandNode secondOp) {
        this.firstOp = firstOp;
        this.mathOpString = value;
        this.secondOp = secondOp;
        this.startLine = startLine;
        this.filename = filename;
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
        String filename = tokens.getLastRemoved().getFilename();

        tokens.validateFirst(TokenType.NUMBER, TokenType.FC_HEADER, TokenType.ID_KEYWORD);
        OperandNode operandNode1 = OperandNode.parseOperandNode(tokens);

        //return mathOperation;
        return new MathOpNode(startLine, filename, firstOp, mathOpHolder, operandNode1);
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
        // TODO: Pass the symbol table to these operands so they know to check if they've been declared / initialized.
        firstOp.validateTree();
        secondOp.validateTree();

        // Check for matching operand types
        if (firstOp.getEvaluationVariableType() != secondOp.getEvaluationVariableType())
            throw new NodeValidateException("Operand types do not match for math operation", filename, startLine);

        // Check for division by zero. I don't think we need to evaluate functions or variables here.
        if (Objects.equals(mathOpString, "/") && secondOp instanceof NumNode && ((NumNode) secondOp).isZero())
            throw new NodeValidateException("Division by zero is not allowed", filename, startLine);
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

