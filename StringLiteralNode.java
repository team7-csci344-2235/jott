package src.nodes;

import src.JottTree;
import src.TokenDeque;

/**
 * Class for String Literal nodes 
 *
 * @author Lianna Pottgen, <lrp2755@rit.edu> 
 */
public class StringLiteralNode implements JottTree {
    private final IDNode stringValue;

    private StringLiteralNode(IDNode stringValue) {
        this.stringValue = stringValue;
    }

    public static IDNode parseStringLiteralNode(TokenDeque tokens) throws NodeParseException {
        IDNode stringValueParsed = IDNode.parseIDNode(tokens);
        return stringValueParsed;
    }

    @Override
    public String convertToJott() {
        return stringValue.convertToJott();
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

