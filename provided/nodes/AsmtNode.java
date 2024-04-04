package provided.nodes;

import provided.TokenDeque;
import provided.TokenType;
import provided.VariableTable;

public class AsmtNode implements BodyStmtNode{

    private final IDNode idNode;
    private final ExprNode exprNode;
    private final int startLine;
    private final String filename;

    private AsmtNode(IDNode idNode, ExprNode exprNode, int startLine, String filename) {
        this.idNode = idNode;
        this.exprNode = exprNode;
        this.startLine = startLine;
        this.filename = filename;
    }

    public static AsmtNode parseAsmtNode(TokenDeque tokens, VariableTable variableTable) throws NodeParseException {
        IDNode idNode = IDNode.parseIDNode(tokens);

        tokens.validateFirst(TokenType.ASSIGN);
        tokens.removeFirst(); // Remove equals

        ExprNode exprNode = ExprNode.parseExprNode(tokens, variableTable);

        tokens.validateFirst(TokenType.SEMICOLON);
        tokens.removeFirst(); // Remove semicolon

        return new AsmtNode(idNode, exprNode, tokens.getLastRemoved().getLineNum(), tokens.getLastRemoved().getFilename());
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
    public void validateTree() throws NodeValidateException {
        if (this.idNode.getEvaluationVariableType() != this.exprNode.getEvaluationVariableType()) {
            throw new NodeValidateException("Types don't make in assigment", filename, startLine);
        }
    }

}
