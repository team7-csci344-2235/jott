package src.nodes;

import src.TokenDeque;

/**
 * Class for ID nodes
 *
 * @author Lianna Pottgen <lrp2755@rit.edu>
 * //same unbreakable loop as the last couple of ones
 */
public class IDNode implements OperandNode { 
    private final IDNode idStringValue;

    private IDNode(IDNode idStringValue) {
        this.idStringValue = idStringValue;
    }

    public static IDNode parseIDNode(TokenDeque tokens) throws NodeParseException {
        IDNode idStringValueParsed = IDNode.parseIDNode(tokens);
        return idStringValueParsed;
    }

    @Override
    public String convertToJott(){
        return idStringValue.convertToJott();
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
