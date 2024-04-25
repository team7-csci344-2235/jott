package provided.nodes;

import provided.JottTree;
import provided.TokenDeque;
import provided.TokenType;

public class FunctionDefParamTNode implements JottTree {

    private final IDNode idNode;
    private final TypeNode typeNode;

    private FunctionDefParamTNode(IDNode idNode, TypeNode typeNode) {
        this.idNode = idNode;
        this.typeNode = typeNode;
    }

    public static FunctionDefParamTNode parseFunctionDefParamTNode(TokenDeque tokens) throws NodeParseException {
        tokens.validateFirst(TokenType.COMMA);
        tokens.removeFirst(); // Remove comma
        IDNode idNode = IDNode.parseIDNode(tokens);
        tokens.validateFirst(TokenType.COLON);
        tokens.removeFirst(); // Remove colon
        TypeNode typeNode = TypeNode.parseTypeNode(tokens);
        return new FunctionDefParamTNode(idNode, typeNode);
    }

    @Override
    public String convertToJott() {
        return "," + idNode.convertToJott() + ": " + typeNode.convertToJott();
    }

    @Override
    public String convertToJava(String className) {
        return "," + typeNode.convertToJava(className) + " "+ idNode.convertToJava(className);
    }

    @Override
    public String convertToC() {
        return "," + typeNode.convertToC() + " "+ idNode.convertToC();
    }

    @Override
    public String convertToPython() {
        return idNode.convertToPython();
    }

    @Override
    public void validateTree() throws NodeValidateException {
        idNode.validateTree();
        typeNode.validateTree();
    }

    public IDNode getIdNode() {
        return idNode;
    }

    public TypeNode getTypeNode() {
        return typeNode;
    }
}
