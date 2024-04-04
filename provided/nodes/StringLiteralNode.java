package provided.nodes;

import provided.JottTree;
import provided.TokenDeque;
import provided.TokenType;

/**
 * Class for String Literal nodes 
 *
 * @author Lianna Pottgen, <lrp2755@rit.edu> 
 */
public class StringLiteralNode implements JottTree, ExprNode {
    private final int startLine;
    private final String stringValue;

    private StringLiteralNode(int startLine, String stringValue) {
        this.stringValue = stringValue;
        this.startLine = startLine;
    }

    public static StringLiteralNode parseStringLiteralNode(TokenDeque tokens) throws NodeParseException {
        tokens.validateFirst(TokenType.STRING);

        String holder = tokens.removeFirst().getToken();
        holder = holder.substring(1, holder.length()-1);

        //return value
        return new StringLiteralNode(tokens.getLastRemoved().getLineNum(), holder);
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
    public void validateTree() throws NodeValidateException {
        return;
    }

    @Override
    public int getStartLine() {
        return startLine;
    }
}

