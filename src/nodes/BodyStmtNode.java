package src.nodes;

import src.JottTree;
import src.TokenDeque;
import src.TokenType;

import java.awt.dnd.InvalidDnDOperationException;

/**
 * Interface for Body Statement nodes
 *
 * @author Adrienne Ressy <amr3032@rit.edu>
 */
public interface BodyStmtNode extends JottTree {

    static BodyStmtNode parseBodyStmtNode(TokenDeque tokens) throws NodeParseException {
        // Ensure first is of one of the following tokens.
        tokens.validateFirst(TokenType.NUMBER, TokenType.FC_HEADER, TokenType.ID_KEYWORD);
        if (tokens.isFirstOf("If")) {
            IfStmtNode ifStmtNode = IfStmtNode.parseIfStmtNode(tokens);
            tokens.validateFirst(TokenType.SEMICOLON);
            tokens.removeFirst(); // Remove semicolon
            return ifStmtNode;
        } else if (tokens.isFirstOf("While")) {
            WhileLoopNode whileLoopNode = WhileLoopNode.parseWhileLoopNode(tokens);
            tokens.validateFirst(TokenType.SEMICOLON);
            tokens.removeFirst(); // Remove semicolon
            return whileLoopNode;
        } else if (tokens.isFirstOf(TokenType.FC_HEADER)) {
            FunctionCallNode functionCallNode = FunctionCallNode.parseFunctionCallNode(tokens);
            tokens.validateFirst(TokenType.SEMICOLON);
            tokens.removeFirst(); // Remove semicolon
            return functionCallNode;
        }else if (tokens.isFirstOf(TokenType.ID_KEYWORD)) {
            AsmtNode asmtNode = AsmtNode.parseAsmtNode(tokens);
            tokens.validateFirst(TokenType.SEMICOLON);
            tokens.removeFirst(); // Remove semicolon
            return asmtNode;
        }
        return null;
    }
}
