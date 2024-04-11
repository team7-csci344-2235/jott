package provided.nodes;

import provided.JottTree;
import provided.TokenDeque;
import provided.TokenType;
import provided.VariableTable;

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
    private final VariableTable variableTable;

    private MathOpNode(int startLine, String filename, OperandNode firstOp, String value, OperandNode secondOp, VariableTable variableTable) {
        this.firstOp = firstOp;
        this.mathOpString = value;
        this.secondOp = secondOp;
        this.startLine = startLine;
        this.filename = filename;
        this.variableTable = variableTable;
    }

    public static MathOpNode parseMathNode(OperandNode firstOp, TokenDeque tokens, VariableTable variableTable) throws NodeParseException {
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
        return new MathOpNode(startLine, filename, firstOp, mathOpHolder, OperandNode.parseOperandNode(tokens, variableTable), variableTable);
    }

    @Override
    public String convertToJott() {
        return firstOp.convertToJott() + mathOpString + secondOp.convertToJott();
    }

    @Override
    public String convertToJava(String className) {
        return firstOp.convertToJava(className) + mathOpString + secondOp.convertToJava(className);
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
        firstOp.validateTree();
        secondOp.validateTree();

        //if variables are initialzed
        if(firstOp instanceof IDNode){
            if(!variableTable.hasVariable(((IDNode) firstOp).getIdStringValue())){
                throw new NodeValidateException("Variable " + ((IDNode) firstOp).getIdStringValue() + " undefined", filename, startLine);
            }
            else if(!variableTable.isVariableInitialized(((IDNode) firstOp).getIdStringValue())){
                throw new NodeValidateException("Variable " + ((IDNode) firstOp).getIdStringValue() + " must be initialized before use", filename, startLine);
            }
        }
        if(secondOp instanceof IDNode){
            if(!variableTable.hasVariable(((IDNode) secondOp).getIdStringValue())){
                throw new NodeValidateException("Variable " + ((IDNode) secondOp).getIdStringValue() + " undefined", filename, startLine);
            }
            else if(!variableTable.isVariableInitialized(((IDNode) secondOp).getIdStringValue())){
                throw new NodeValidateException("Variable " + ((IDNode) secondOp).getIdStringValue() + " must be initialized before use", filename, startLine);
            }
        }

        // Check for matching operand types
        if (OperandNode.getOperandType(firstOp, variableTable, filename) != OperandNode.getOperandType(secondOp, variableTable, filename))
            throw new NodeValidateException("Operand types do not match for math operation", filename, startLine);

        // Check for division by zero. I don't think we need to evaluate functions or variables here.
        if (Objects.equals(mathOpString, "/") && secondOp instanceof NumNode && ((NumNode) secondOp).isZero())
            throw new NodeValidateException("Division by zero is not allowed", filename, startLine);
    }

    @Override
    public int getStartLine() {
        return startLine;
    }

    public TypeNode.VariableType getOperandType() throws NodeValidateException {
        return OperandNode.getOperandType(firstOp, variableTable, filename);
    }
}

