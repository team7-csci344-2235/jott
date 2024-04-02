package provided.nodes;

import provided.JottTree;
import provided.TokenDeque;
import provided.TokenType;

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
        StringBuilder sb = new StringBuilder();
        while (!bodyStmtNodes.isEmpty()) {
            BodyStmtNode node = bodyStmtNodes.getFirst();
            sb.append(node.convertToJott());

            // If statements / while loops do not have semicolons.
            if (node instanceof AsmtNode || node instanceof FunctionCallNode) {
                sb.append(";");
            }
            sb.append("\n");
            bodyStmtNodes.removeFirst();
        }

        if(returnStmtNode != null){
            sb.append(returnStmtNode.convertToJott());
        }

        return sb.toString();
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
        return;
    }

    public boolean isReturnable() {
        if (returnStmtNode.getExprNode() != null) {
            return true;
        }
        return false;
    }

    public ReturnStmtNode getReturnStmtNode() {
        return returnStmtNode;
    }
}
