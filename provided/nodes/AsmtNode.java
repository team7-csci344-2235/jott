package provided.nodes;

import provided.TokenDeque;
import provided.TokenType;

public class AsmtNode implements BodyStmtNode{

    private final IDNode idNode;
    private final ExprNode exprNode;
    private AsmtNode(IDNode idNode, ExprNode exprNode) {
        this.idNode = idNode;
        this.exprNode = exprNode;
    }

    public static AsmtNode parseAsmtNode(TokenDeque tokens) throws NodeParseException {
        IDNode idNode = IDNode.parseIDNode(tokens);

        tokens.validateFirst(TokenType.ASSIGN);
        tokens.removeFirst(); // Remove equals

        ExprNode exprNode = ExprNode.parseExprNode(tokens);

        tokens.validateFirst(TokenType.SEMICOLON);
        tokens.removeFirst(); // Remove semicolon
        return new AsmtNode(idNode, exprNode);
    }

    @Override
    public String convertToJott() {
        return idNode.convertToJott() + " = " + exprNode.convertToJott();
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
