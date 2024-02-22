package src.nodes;

import src.TokenDeque;

/**
 * Class for Number nodes
 *
 * @author TODO
 */
public class NumNode implements OperandNode {
    private NumNode() {
        // TODO: Implement this method and arguments
    }

    public static NumNode parseNumNode(TokenDeque tokens, boolean isNegative) throws NodeParseException {
        // TODO: Implement this method
        return null;
    }

    @Override
    public String convertToJott() {
        // TODO: Implement this method
        return null;
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
