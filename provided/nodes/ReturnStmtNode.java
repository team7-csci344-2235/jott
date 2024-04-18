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
    private final VariableTable variableTable;
    private final String filename;
    private final int startLine;
    private ReturnStmtNode(ExprNode exprNode, String functionName, VariableTable variableTable, String filename, int startLine) {
        this.exprNode = exprNode;
        this.functionName = functionName;
        this.variableTable = variableTable;
        this.filename = filename;
        this.startLine = startLine;
    }

    private ReturnStmtNode(String functionName, VariableTable variableTable, String filename, int startLine) {
        this.exprNode = null;
        this.functionName = functionName;
        this.variableTable = variableTable;
        this.filename = filename;
        this.startLine = startLine;
    }

    public static ReturnStmtNode parseReturnStmtNode(TokenDeque tokens, VariableTable variableTable, String functionName) throws JottTree.NodeParseException {
        if (tokens.isFirstOf(TokenType.R_BRACE)) // No expressions, empty params.
            return new ReturnStmtNode(functionName, variableTable, tokens.getLastRemoved().getFilename(), tokens.getLastRemoved().getLineNum());

        tokens.validateFirst("Return");
        tokens.removeFirst(); // Remove return
        ExprNode exprNode = ExprNode.parseExprNode(tokens, variableTable);
        

        tokens.validateFirst(TokenType.SEMICOLON);
        tokens.removeFirst(); // Remove semicolon
        return new ReturnStmtNode(exprNode, functionName, variableTable, tokens.getLastRemoved().getFilename(), tokens.getLastRemoved().getLineNum());
    }

    /**
     * ReturnStmtNode always "exists" even if the parent BodyNode doesn't have
     * a return.
     * This checks whether or not there is an actual return statement in
     * this thing.
     */
    public boolean actuallyIsAReturn() {
        return exprNode != null;
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
        if (exprNode == null) {
            return "";
        }
        return "return " + exprNode.convertToJava(className) + ";" + "\n";
    }

    @Override
    public String convertToC() {
        return null;
    }

    @Override
    public String convertToPython() {
        if (exprNode == null) {
            return "";
        }
        return "return " + exprNode.convertToPython();
    }

    @Override
    public void validateTree() throws NodeValidateException {
        if (exprNode != null) {
            exprNode.validateTree();
        }
        if (exprNode instanceof IDNode) {
            if (!variableTable.hasVariable(((IDNode) exprNode).getIdStringValue())) {
                throw new NodeValidateException("Returned variable is not declared.", filename, startLine);
            }
            if (!variableTable.isVariableInitialized(((IDNode) exprNode).getIdStringValue())) {
                throw new NodeValidateException("Returned variable is not initialized.", filename, startLine);
            }

        }

        var exprType = ExprNode.getExprType(exprNode, variableTable, filename);
        var funcReturnType = variableTable.getFunctionReturnType(functionName);
        if (exprType != funcReturnType) {
            if (exprType == null) {
                // XXX: this message string is magic! See BodyNode.java.
                throw new NodeValidateException("Function is missing return.",
                        filename, startLine);
            }
            throw new NodeValidateException("Return type does not match function return type.", filename, startLine);
        }
    }
}
