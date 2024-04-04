package provided.nodes;

import provided.*;

/**
 * Class for ReturnStmt nodes
 *
 * @author Ewen Cazuc <ec1291@rit.edu>
 */
public class ReturnStmtNode implements JottTree{

    private final ExprNode exprNode;
    private final String functionName;
    private final SymbolTable symbolTable;
    private final VariableTable variableTable;
    private final String filename;
    private final int startLine;
    private ReturnStmtNode(ExprNode exprNode, String functionName, SymbolTable symbolTable, VariableTable variableTable, String filename, int startLine) {
        this.exprNode = exprNode;
        this.functionName = functionName;
        this.symbolTable = symbolTable;
        this.variableTable = variableTable;
        this.filename = filename;
        this.startLine = startLine;
    }

    private ReturnStmtNode(String functionName, SymbolTable symbolTable, VariableTable variableTable, String filename, int startLine) {
        this.exprNode = null;
        this.functionName = functionName;
        this.symbolTable = symbolTable;
        this.variableTable = variableTable;
        this.filename = filename;
        this.startLine = startLine;
    }

    public static ReturnStmtNode parseReturnStmtNode(TokenDeque tokens, VariableTable variableTable, String functionName, SymbolTable symbolTable) throws JottTree.NodeParseException {
        if (tokens.isFirstOf(TokenType.R_BRACE)) // No expressions, empty params.
            return new ReturnStmtNode(functionName, symbolTable, variableTable, tokens.getLastRemoved().getFilename(), tokens.getLastRemoved().getLineNum());

        tokens.validateFirst("Return");
        tokens.removeFirst(); // Remove return
        ExprNode exprNode = ExprNode.parseExprNode(tokens, variableTable);
        

        tokens.validateFirst(TokenType.SEMICOLON);
        tokens.removeFirst(); // Remove semicolon
        return new ReturnStmtNode(exprNode, functionName, symbolTable, variableTable, tokens.getLastRemoved().getFilename(), tokens.getLastRemoved().getLineNum());
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
        if (exprNode != null) {
            exprNode.validateTree();
        }
        if (exprNode instanceof IDNode) {
            if (variableTable.hasVariable(((IDNode) exprNode).getIdStringValue())) {
                throw new NodeValidateException("Returned variable is not declared.", filename, startLine);
            }
            if (variableTable.isVariableInitialized(((IDNode) exprNode).getIdStringValue())) {
                throw new NodeValidateException("Returned variable is not initialized.", filename, startLine);
            }

        }
        if (ExprNode.getExprType(exprNode, variableTable, filename) != symbolTable.getFunctionReturnType(functionName)) {
            throw new NodeValidateException("Return type does not match function return type.", filename, startLine);
        }
    }
}
