package src.nodes;

import src.JottTree;
import src.TokenDeque;

public class BodyNode implements JottTree {

    private final BodyStmtNode bodyStmtNode;
    private final ReturnStmtNode returnStmtNode;
    private BodyNode(BodyStmtNode bodyStmtNode, ReturnStmtNode returnStmtNode) {
        this.bodyStmtNode = bodyStmtNode;
        this.returnStmtNode = returnStmtNode;
    }

    public static BodyNode parseBodyNode(TokenDeque tokens) throws NodeParseException {
        BodyStmtNode bodyStmtNode = BodyStmtNode.parseBodyStmtNode(tokens);
        ReturnStmtNode returnStmtNode = ReturnStmtNode.parseReturnStmtNode(tokens);
        return new BodyNode(bodyStmtNode, returnStmtNode);
    }

    @Override
    public String convertToJott() {
        return bodyStmtNode.convertToJott() + returnStmtNode.convertToJott();
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
