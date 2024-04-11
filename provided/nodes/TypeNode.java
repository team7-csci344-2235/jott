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
    /**
     * Enum for the different types of variables. Any should only be used for special parameters such as print,
     * which accepts any type.
     */
    public enum VariableType { DOUBLE, INTEGER, STRING, BOOLEAN, ANY }
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
        switch (type) {
            case DOUBLE -> {return "double";}
            case INTEGER -> {return "int";}
            case STRING -> {return "String";}
            case BOOLEAN -> {return "boolean";}
        }
        return "";
       
    }

    @Override
    public String convertToC() {
        return null;
    }

    @Override
    public String convertToPython(int tabNumber) {
        return "";
    }

    @Override
    public void validateTree() throws NodeValidateException {
        // This node cannot be invalid. To be a TypeNode at all, it must
        // have been properly tokenized as a type.
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
