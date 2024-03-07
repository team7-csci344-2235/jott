package src.nodes;

import src.JottTree;
import src.TokenDeque;
import src.TokenType;

/**
 * Class for RelOp nodes (<, >, =, ==, <=, >=)
 *
 * @author Lianna Pottgen, <lrp2755@rit.edu>  //infinite loop issues
 */
public class RelOpNode implements JottTree {
    private final String relationalValue;

    private RelOpNode(String relationalValue) {
        this.relationalValue = relationalValue;
    }

    public static RelOpNode parseRelOpNode(TokenDeque tokens) throws NodeParseException {
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

        RelOpNode relationalOperation = new RelOpNode(tokens.removeFirst().getToken());

        //return relationalOperation;
        return relationalOperation;
    }

    @Override
    public String convertToJott() {
        return relationalValue;
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

