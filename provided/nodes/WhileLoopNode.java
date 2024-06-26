package provided.nodes;

import provided.TokenDeque;
import provided.TokenType;
import provided.VariableTable;

public class WhileLoopNode implements BodyStmtNode{
    private final ExprNode exprNode;
    private final BodyNode bodyNode;
    private WhileLoopNode(ExprNode exprNode, BodyNode bodyStmtNode) {
        this.exprNode = exprNode;
        this.bodyNode = bodyStmtNode;
    }

    public static WhileLoopNode parseWhileLoopNode(TokenDeque tokens, VariableTable variableTable, String functionName) throws NodeParseException {
        tokens.validateFirst("While");
        tokens.removeFirst(); // Remove while
        tokens.validateFirst(TokenType.L_BRACKET);
        tokens.removeFirst(); // Remove left bracket
        ExprNode exprNode = ExprNode.parseExprNode(tokens, variableTable);
        tokens.validateFirst(TokenType.R_BRACKET);
        tokens.removeFirst(); // Remove right bracket
        tokens.validateFirst(TokenType.L_BRACE);
        tokens.removeFirst(); // Remove left brace
        BodyNode bodyNode = BodyNode.parseBodyNode(tokens, variableTable, functionName);
        tokens.validateFirst(TokenType.R_BRACE);
        tokens.removeFirst(); // Remove right brace
        return new WhileLoopNode(exprNode, bodyNode);
    }

    @Override
    public String convertToJott() {
        return "While [" + exprNode.convertToJott() + "]{" + bodyNode.convertToJott() + "}";
    }

    @Override
    public String convertToJava(String className) {
        return "while(" + exprNode.convertToJava(className) + "){" + bodyNode.convertToJava(className) + "}";
    }

    @Override
    public String convertToC() {
        return "while(" + exprNode.convertToC() + "){" + bodyNode.convertToC() + "}";
    }

    @Override
    public String convertToPython() {
        return "while " + exprNode.convertToPython() + ":\n" + bodyNode.convertToPython();
    }

    @Override
    public void validateTree() throws NodeValidateException {
        exprNode.validateTree();
        bodyNode.validateTree();
    }
}
