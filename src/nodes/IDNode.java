package src.nodes;

import src.TokenDequeue;

/**
 * Class for ID nodes
 *
 * @author TODO
 */
public class IDNode implements OperandNode {
    private IDNode() {
        // TODO: Implement this method and arguments
    }

    public static IDNode parseIDNode(TokenDequeue tokens) throws NodeParseException {
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
