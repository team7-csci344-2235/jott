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
    private final String relationalValue;
    private final OperandNode firstOpStr;
    private final OperandNode secondOpStr;

    private RelOpNode(int startLine, OperandNode firstOp, String relationalValue, OperandNode secondOp) {
        this.firstOpStr = firstOp;
        this.relationalValue = relationalValue;
        this.secondOpStr = secondOp;
        this.startLine = startLine;
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
        return new RelOpNode(startLine, firstOp, relOpHolder, operandNode1);
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
        return;
    }

    @Override
    public int getStartLine() {
        return startLine;
    }
}

