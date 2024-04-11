package provided.nodes;

import provided.TokenDeque;
import provided.TokenType;

/**
 * Class for ID nodes
 *
 * @author Lianna Pottgen <lrp2755@rit.edu>
 */
public class IDNode implements OperandNode {
    private final int startLine;
    private final String idStringValue;

    private IDNode(int startLine, String idStringValue) {
        this.idStringValue = idStringValue;
        this.startLine = startLine;
    }

    public static IDNode parseIDNode(TokenDeque tokens) throws NodeParseException {
        //get information
        tokens.validateFirst(TokenType.ID_KEYWORD);
        return new IDNode(tokens.getFirst().getLineNum(), tokens.removeFirst().getToken());
    }

    @Override
    public String convertToJott(){
        return idStringValue;
    }

    @Override
    public String convertToJava(String className) {
        return idStringValue;
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

    @Override
    public int getStartLine() {
        return startLine;
    }

    public String getIdStringValue() {
        return idStringValue;
    }
}
