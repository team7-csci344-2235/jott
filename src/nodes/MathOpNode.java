package src.nodes;

import src.JottTree;
import src.TokenDeque;
import src.TokenType;

/**
 * Class for MathOp nodes
 *
 * @author Lianna Pottgen, <lrp2755@rit.edu>
 */
public class MathOpNode implements JottTree {
    private final String mathOpString;

    private MathOpNode(String value) {
        this.mathOpString = value;
    }

    public static MathOpNode parseMathNode(TokenDeque tokens) throws NodeParseException {
        //get information
        tokens.validateFirst(TokenType.ID_KEYWORD); 

        //if "/"
            //return "/"
        //else if "+"
            //return "+"
        //else if "-"
            //return "-"
        //else if "*"
            //return "*"
        tokens.validateFirst("/", "+", "-", "*");

        //return mathOperation;
        return new MathOpNode(tokens.removeFirst().getToken());
    }

    @Override
    public String convertToJott() {
        return mathOpString;
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

