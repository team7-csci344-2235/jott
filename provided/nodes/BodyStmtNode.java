package provided.nodes;

import provided.*;

/**
 * Interface for Body Statement nodes
 *
 * @author Adrienne Ressy <amr3032@rit.edu>
 */
public interface BodyStmtNode extends JottTree {

    static BodyStmtNode parseBodyStmtNode(TokenDeque tokens, VariableTable variableTable, String functionName) throws NodeParseException {
        // Ensure first is of one of the following tokens.
        tokens.validateFirst(TokenType.NUMBER, TokenType.FC_HEADER, TokenType.ID_KEYWORD);
        if (tokens.isFirstOf("If")) {
            return IfStmtNode.parseIfStmtNode(tokens, variableTable, functionName);
        } else if (tokens.isFirstOf("While")) {
            return WhileLoopNode.parseWhileLoopNode(tokens, variableTable, functionName);
        } else if (tokens.isFirstOf(TokenType.FC_HEADER)) {
            FunctionCallNode functionCallNode = FunctionCallNode.parseFunctionCallNode(tokens, variableTable);
            tokens.validateFirst(TokenType.SEMICOLON);
            tokens.removeFirst(); // Remove semicolon
            return functionCallNode;
        }else if (tokens.isFirstOf(TokenType.ID_KEYWORD)) {
            return AsmtNode.parseAsmtNode(tokens, variableTable);
        }
        return null;
    }
}
