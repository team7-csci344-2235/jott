package src.nodes;

import src.JottTree;
import src.TokenDeque;

/**
 * Class for Boolean nodes
 *
 * @author Lianna Pottgen, <lrp2755@rit.edu>
 */
public class BoolNode implements JottTree {
    private final IDNode trueOrFalse;

    private BoolNode(IDNode trueOrFalse) {
        this.trueOrFalse = trueOrFalse;
    }

    public static IDNode parseBoolNode(TokenDeque tokens) throws NodeParseException {
        IDNode trueOrFalseParse = IDNode.parseIDNode(tokens);
        return trueOrFalseParse;
    }

    @Override
    public String convertToJott() {
        return trueOrFalse.convertToJott();
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

