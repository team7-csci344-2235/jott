package provided.nodes;

import provided.JottTree;
import provided.TokenDeque;
import provided.TokenType;

/**
 * Class for ReturnStmt nodes
 *
 * @author Ewen Cazuc <ec1291@rit.edu>
 */
public class ReturnStmtNode implements JottTree{

    private final ExprNode exprNode;
    private ReturnStmtNode(ExprNode exprNode) {
        this.exprNode = exprNode;
    }

    private ReturnStmtNode() {
        this.exprNode = null;
    }

    public static ReturnStmtNode parseReturnStmtNode(TokenDeque tokens) throws JottTree.NodeParseException {
        if (tokens.isFirstOf(TokenType.R_BRACE)) // No expressions, empty params.
            return new ReturnStmtNode();

        tokens.validateFirst("Return");
        tokens.removeFirst(); // Remove return
        ExprNode exprNode = ExprNode.parseExprNode(tokens);
        

        tokens.validateFirst(TokenType.SEMICOLON);
        tokens.removeFirst(); // Remove semicolon
        return new ReturnStmtNode(exprNode);
    }

    @Override
    public String convertToJott() {
        if (exprNode == null) {
            return "";
        }
        return "Return " + exprNode.convertToJott() + ";" + "\n";
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

    public ExprNode getExprNode() {
        return exprNode;
    }
}
