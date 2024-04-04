package provided.nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenDeque;

/**
 * Class for Type nodes
 *
 * @author Ewen Cazuc <ec1291@rit.edu>
 * @author Adrienne Ressy <amr3032@rit.edu>
 **/
public class TypeNode implements JottTree {
    public enum VariableType {
        DOUBLE, INTEGER, STRING, BOOLEAN
    }
    private final VariableType type;
    private final int startLine;
    private final String filename;

    private TypeNode(Token token) {
        type = VariableType.valueOf(token.getToken().toUpperCase());
        startLine = token.getLineNum();
        filename = token.getFilename();
    }

    public static TypeNode parseTypeNode(TokenDeque tokens) throws NodeParseException {
        tokens.validateFirst("Double", "Integer", "String", "Boolean");
        return new TypeNode(tokens.removeFirst());
    }

    @Override
    public String convertToJott() {
        switch (type) {
            case DOUBLE -> {return "Double";}
            case INTEGER -> {return "Integer";}
            case STRING -> {return "String";}
            case BOOLEAN -> {return "Boolean";}
        }
        return "";
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
    public VariableType getType() {
        return type;
    }

    public int getStartLine() {
        return startLine;
    }

    public String getFilename() {
        return filename;
    }
}
