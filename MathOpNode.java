package src.nodes;

import src.JottTree;
import src.TokenDeque;

/**
 * Class for MathOp nodes
 *
 * @author Lianna Pottgen, <lrp2755@rit.edu>
 * 
 * //same issue with re Op
 */
public class MathOpNode implements JottTree {
    private final IDNode value;
    private MathOpNode(IDNode value) {
        this.value = value;
    }

    public static IDNode parseMathNode(TokenDeque tokens) throws NodeParseException {
        IDNode value = IDNode.parseIDNode(tokens);
        return value;
    }

    @Override
    public String convertToJott() {
        return value.convertToJott();
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

