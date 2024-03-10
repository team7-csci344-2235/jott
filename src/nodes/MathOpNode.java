package src.nodes;

import src.JottTree;
import src.TokenDeque;
import src.TokenType;

/**
 * Class for MathOp nodes
 *
 * @author Lianna Pottgen, <lrp2755@rit.edu>
 */
public class MathOpNode implements JottTree, ExprNode {
    private final String mathOpString;
    private final OperandNode firstOp;
    private final OperandNode secondOp;

    private MathOpNode(OperandNode firstOp, String value, OperandNode secondOp) {
        this.firstOp = firstOp;
        this.mathOpString = value;
        this.secondOp = secondOp;
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

        tokens.validateFirst(TokenType.NUMBER, TokenType.FC_HEADER, TokenType.ID_KEYWORD);
        OperandNode operandNode1 = OperandNode.parseOperandNode(tokens);

        //return mathOperation;
        return new MathOpNode(firstOp, mathOpHolder, operandNode1);
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
    public boolean validateTree() {
        return false;
    }
}

