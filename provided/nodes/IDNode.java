package provided.nodes;

import provided.TokenDeque;
import provided.TokenType;

/**
 * Class for ID nodes
 *
 * @author Lianna Pottgen <lrp2755@rit.edu>
 */
public class IDNode implements OperandNode { 
    private final String idStringValue;

    private IDNode(String idStringValue) {
        this.idStringValue = idStringValue;
    }

    public static IDNode parseIDNode(TokenDeque tokens) throws NodeParseException {
        //get information
        tokens.validateFirst(TokenType.ID_KEYWORD);
        return new IDNode(tokens.removeFirst().getToken());
    }

    @Override
    public String convertToJott(){
        return idStringValue;
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
