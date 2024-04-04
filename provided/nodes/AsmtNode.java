package provided.nodes;

import provided.TokenDeque;
import provided.TokenType;
import provided.VariableTable;

public class AsmtNode implements BodyStmtNode{

    private final IDNode idNode;
    private final ExprNode exprNode;
    private final VariableTable variableTable;
    private final int startLine;
    private final String filename;

    private AsmtNode(IDNode idNode, ExprNode exprNode, VariableTable variableTable, int startLine, String filename) {
        this.idNode = idNode;
        this.exprNode = exprNode;
        this.variableTable = variableTable;
        this.filename = filename;
        this.startLine = startLine;
    }

    public static AsmtNode parseAsmtNode(TokenDeque tokens, VariableTable variableTable) throws NodeParseException {
        IDNode idNode = IDNode.parseIDNode(tokens);

        tokens.validateFirst(TokenType.ASSIGN);
        tokens.removeFirst(); // Remove equals

        ExprNode exprNode = ExprNode.parseExprNode(tokens, variableTable);

        tokens.validateFirst(TokenType.SEMICOLON);
        tokens.removeFirst(); // Remove semicolon

        return new AsmtNode(idNode, exprNode, variableTable, tokens.getLastRemoved().getLineNum(), tokens.getLastRemoved().getFilename());
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
        this.idNode.validateTree();
        this.exprNode.validateTree();
        if (!variableTable.hasVariable(idNode.getIdStringValue()) ) {
            throw new NodeValidateException("Variable " + idNode.getIdStringValue() + " not declared", filename, startLine);
        }
        if (variableTable.getVariableType(idNode.getIdStringValue()) != ExprNode.getExprType(exprNode, variableTable, filename)) {
            throw new NodeValidateException("ID type doesn't match expression type.", filename, startLine);
        }
        variableTable.assignVariable(idNode.getIdStringValue());
    }

}
