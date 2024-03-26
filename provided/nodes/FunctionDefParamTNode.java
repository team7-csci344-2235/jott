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
        String temp = ","+ idNode.convertToJott() + ": " + typeNode.convertToJott();
        if(temp.equals(", ")){
            return "";
        }
        else{
            return temp;
        }
        //return ", "+ idNode.convertToJott() + ": " + typeNode.convertToJott();
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
