package provided.nodes;

import provided.JottTree;
import provided.TokenDeque;
import provided.TokenType;

/**
 * Class for RelOp nodes (<, >, =, ==, <=, >=)
 *
 * @author Lianna Pottgen, <lrp2755@rit.edu>  //infinite loop issues
 */
public class RelOpNode implements JottTree, ExprNode {
    private final String relationalValue;
    private final OperandNode firstOpStr;
    private final OperandNode secondOpStr;

    private RelOpNode(OperandNode firstOp, String relationalValue, OperandNode secondOp) {
        this.firstOpStr = firstOp;
        this.relationalValue = relationalValue;
        this.secondOpStr = secondOp;
    }

    public static RelOpNode parseRelOpNode(OperandNode firstOp, TokenDeque tokens) throws NodeParseException {
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

        tokens.validateFirst(TokenType.NUMBER, TokenType.FC_HEADER, TokenType.ID_KEYWORD);
        OperandNode operandNode1 = OperandNode.parseOperandNode(tokens);

        //return relationalOperation;
        return new RelOpNode(firstOp, relOpHolder, operandNode1);
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
}

