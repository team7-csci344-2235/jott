package provided.nodes;

import provided.JottTree;
import provided.TokenDeque;
import provided.TokenType;

public class VarDecNode implements JottTree {

    private final TypeNode typeNode;
    private final IDNode idNode;

    private VarDecNode(TypeNode typeNode, IDNode idNode) {
        this.typeNode = typeNode;
        this.idNode = idNode;
    }

    /**
     * Parses a var_dec node from the given tokens
     * @param tokens the tokens to parse
     * @return the parsed var_dec node
     * @throws NodeParseException if the tokens do not form a valid function call node
     */
    public static VarDecNode parseVarDecNode(TokenDeque tokens) throws NodeParseException {
        TypeNode typeNode = TypeNode.parseTypeNode(tokens);
        IDNode idNode = IDNode.parseIDNode(tokens);
        tokens.validateFirst(TokenType.SEMICOLON);
        tokens.removeFirst(); // Remove semicolon
        return new VarDecNode(typeNode, idNode);
    }

    @Override
    public String convertToJott() {
        return typeNode.convertToJott() + " " + idNode.convertToJott() + ";";
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
