package src.nodes;

import src.JottTree;
import src.TokenDeque;
import src.TokenType;

import java.util.ArrayList;

/**
 * Class for Body nodes
 *
 * @author Ewen Cazuc <ec1291@rit.edu>
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

        // Parse body statements. Keep in mind that zero body statements is
        // acceptable.
        while (!tokens.isFirstOf("Return") && !tokens.isFirstOf(TokenType.R_BRACE)) {
            bodyStmtNodes1.add(BodyStmtNode.parseBodyStmtNode(tokens));
        }


        ReturnStmtNode returnStmtNode = ReturnStmtNode.parseReturnStmtNode(tokens);
        return new BodyNode(bodyStmtNodes1, returnStmtNode);
    }

    @Override
    public String convertToJott() {
        String result = "";
        while (!bodyStmtNodes.isEmpty()) {
            result = result + bodyStmtNodes.getFirst().convertToJott() + ";";
            result += "\n";
            bodyStmtNodes.removeFirst();
        }
        if(returnStmtNode!=null){
            return result + returnStmtNode.convertToJott();
        }
        else{
            return result;
        }
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
