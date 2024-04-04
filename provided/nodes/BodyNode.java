package provided.nodes;

import provided.*;

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

    public static BodyNode parseBodyNode(TokenDeque tokens, VariableTable variableTable, String functionName, SymbolTable symbolTable) throws NodeParseException {
        ArrayList<BodyStmtNode> bodyStmtNodes = new ArrayList<>();

        // Parse body statements. Keep in mind that zero body statements is
        // acceptable.
        while (!tokens.isFirstOf("Return") && !tokens.isFirstOf(TokenType.R_BRACE)) {
            bodyStmtNodes.add(BodyStmtNode.parseBodyStmtNode(tokens, variableTable, functionName,symbolTable));
        }

        return new BodyNode(bodyStmtNodes, ReturnStmtNode.parseReturnStmtNode(tokens, variableTable, functionName, symbolTable));
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
        for(BodyStmtNode node : bodyStmtNodes) {
            node.validateTree();
        }
        if(returnStmtNode != null) {
            returnStmtNode.validateTree();
        }
    }
}
