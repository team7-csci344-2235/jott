package src.nodes;

import src.JottTree;
import src.Token;
import src.TokenDeque;
import src.TokenType;

/**
 * Class for Type nodes
 *
 * @author Ewen Cazuc <ec1291@rit.edu>
 * @author Adrienne Ressy <amr3032@rit.edu>
 **/
public class TypeNode implements JottTree {

    private final String type;
    private TypeNode(Token type) {
        this.type = type.getToken();
    }

    public static TypeNode parseTypeNode(TokenDeque tokens) throws NodeParseException {
        tokens.validateFirst("Double", "Integer", "String", "Boolean");
        return new TypeNode(tokens.removeFirst());
    }

    @Override
    public String convertToJott() {
        return type;
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
