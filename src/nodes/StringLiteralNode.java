package src.nodes;

import src.JottTree;
import src.TokenDeque;
import src.TokenType;

/**
 * Class for String Literal nodes 
 *
 * @author Lianna Pottgen, <lrp2755@rit.edu> 
 */
public class StringLiteralNode implements JottTree {
    private final String stringValue;

    private StringLiteralNode(String stringValue) {
        this.stringValue = stringValue;
    }

    public static StringLiteralNode parseStringLiteralNode(TokenDeque tokens) throws NodeParseException {
        tokens.validateFirst(TokenType.STRING);

        String holder = tokens.removeFirst().getToken();
        holder = holder.substring(1, holder.length()-2);

        StringLiteralNode stringLiteral = new StringLiteralNode(holder);

        //return value
        return stringLiteral;
    }

    @Override
    public String convertToJott() {
        return "\""+stringValue+"\"";
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

