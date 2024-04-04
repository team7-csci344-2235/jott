package provided.nodes;

import provided.JottTree;
import provided.TokenDeque;
import provided.TokenType;
import provided.VariableTable;

/**
 * Class for RelOp nodes (<, >, =, ==, <=, >=)
 *
 * @author Lianna Pottgen, <lrp2755@rit.edu>  //infinite loop issues
 */
public class RelOpNode implements JottTree, ExprNode {
    private final int startLine;
    private final String filename;
    private final String relationalValue;
    private final OperandNode firstOpStr;
    private final OperandNode secondOpStr;
    private final VariableTable variableTable;

    private RelOpNode(int startLine, String filename, OperandNode firstOp, String relationalValue, OperandNode secondOp, VariableTable variableTable) {
        this.firstOpStr = firstOp;
        this.relationalValue = relationalValue;
        this.secondOpStr = secondOp;
        this.startLine = startLine;
        this.filename = filename;
        this.variableTable = variableTable;
    }

    public static RelOpNode parseRelOpNode(OperandNode firstOp, TokenDeque tokens, VariableTable variableTable) throws NodeParseException {
        //get information
        tokens.validateFirst(TokenType.REL_OP); 

        //if ">"
            //return ">"
        //else if "<"
            //return "<"
        //else if "<="
            //return "<="
        //else if ">="
            //return ">="
        //else if "=="
            //return "=="
        tokens.validateFirst(">", "<", "<=", ">=", "==");
        String relOpHolder = tokens.removeFirst().getToken();
        int startLine = tokens.getLastRemoved().getLineNum();

        tokens.validateFirst(TokenType.NUMBER, TokenType.FC_HEADER, TokenType.ID_KEYWORD);
        OperandNode operandNode1 = OperandNode.parseOperandNode(tokens, variableTable);

        //return relationalOperation;
        return new RelOpNode(startLine, tokens.getLastRemoved().getFilename(), firstOp, relOpHolder, operandNode1, variableTable);
    }

    @Override
    public String convertToJott() {
        return firstOpStr.convertToJott() +relationalValue+ secondOpStr.convertToJott();
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
        firstOpStr.validateTree();
        secondOpStr.validateTree();

        //if variables are initialzed
        if(firstOpStr instanceof IDNode){
            if(!variableTable.hasVariable(((IDNode) firstOpStr).getIdStringValue()) || !variableTable.isVariableInitialized(((IDNode) firstOpStr).getIdStringValue())){
                throw new NodeValidateException("Variable " + ((IDNode) firstOpStr).getIdStringValue() + " must be initialized before use", filename, startLine);
            }
        }
        if(secondOpStr instanceof IDNode){
            if(!variableTable.hasVariable(((IDNode) secondOpStr).getIdStringValue()) || !variableTable.isVariableInitialized(((IDNode) secondOpStr).getIdStringValue())){
                throw new NodeValidateException("Variable " + ((IDNode) secondOpStr).getIdStringValue() + " must be initialized before use", filename, startLine);
            }
        }
        // Check for matching operand types
        if (OperandNode.getOperandType(firstOpStr, variableTable, filename) != OperandNode.getOperandType(secondOpStr, variableTable, filename))
            throw new NodeValidateException("Operand types do not match for math operation", filename, startLine);

    }

    @Override
    public int getStartLine() {
        return startLine;
    }
}

