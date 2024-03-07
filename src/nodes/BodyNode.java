package src.nodes;

import src.JottTree;
import src.TokenDeque;

import java.util.ArrayList;

/**
 * Class for Body nodes
 *
 * @author Adrienne Ressy amr3032@g.rit.edu
 */
public class BodyNode implements JottTree {

    private final ArrayList<BodyStmtNode> bodyStmtNodes;
    private final ReturnStmtNode returnStmtNode;
    private BodyNode(ArrayList<BodyStmtNode> bodyStmtNodes, ReturnStmtNode returnStmtNode) {
        this.bodyStmtNodes = bodyStmtNodes;
        this.returnStmtNode = returnStmtNode;
    }

    public static BodyNode parseBodyNode(TokenDeque tokens) throws NodeParseException {
        ArrayList<BodyStmtNode> bodyStmtNodes1 = new ArrayList<>();

        for (;;) {
            bodyStmtNodes1.add(BodyStmtNode.parseBodyStmtNode(tokens)); // We should have expressions here, let's parse.
            if (tokens.isFirstOf("Return"))
                break; //if it's a return statement we break
        }

        ReturnStmtNode returnStmtNode = ReturnStmtNode.parseReturnStmtNode(tokens);
        return new BodyNode(bodyStmtNodes1, returnStmtNode);
    }

    @Override
    public String convertToJott() {
        String result = "";
        while (!bodyStmtNodes.isEmpty()) {
            result = result + bodyStmtNodes.getFirst().convertToJott() + " ";
            bodyStmtNodes.removeFirst();
        }
        return result + returnStmtNode.convertToJott();
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
