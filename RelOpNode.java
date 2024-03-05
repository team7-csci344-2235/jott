package src.nodes;

import src.JottTree;
import src.TokenDeque;

/**
 * Class for RelOp nodes (<, >, =, ==, <=, >=)
 *
 * @author Lianna Pottgen, <lrp2755@rit.edu>  //infinite loop issues
 */
public class RelOpNode implements JottTree {
    private final RelOpNode relationalValue;

    private RelOpNode(RelOpNode relationalValue) {
        this.relationalValue = relationalValue;
    }

    public static RelOpNode parseRelOpNode(TokenDeque tokens) throws NodeParseException {
        RelOpNode relationalValue = RelOpNode.parseRelOpNode(tokens);
        return relationalValue;
    }

    @Override
    public String convertToJott() {
        return relationalValue.convertToJott();
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

