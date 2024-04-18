package provided.nodes;

import provided.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

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

    public static BodyNode parseBodyNode(TokenDeque tokens, VariableTable variableTable, String functionName) throws NodeParseException {
        ArrayList<BodyStmtNode> bodyStmtNodes = new ArrayList<>();

        // Parse body statements. Keep in mind that zero body statements is
        // acceptable.
        while (!tokens.isFirstOf("Return") && !tokens.isFirstOf(TokenType.R_BRACE)) {
            bodyStmtNodes.add(BodyStmtNode.parseBodyStmtNode(tokens, variableTable, functionName));
        }

        return new BodyNode(bodyStmtNodes, ReturnStmtNode.parseReturnStmtNode(tokens, variableTable, functionName));
    }

    public boolean hasReturn() {
        return returnStmtNode.actuallyIsAReturn();
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
        StringBuilder sb = new StringBuilder();
        while (!bodyStmtNodes.isEmpty()) {
            BodyStmtNode node = bodyStmtNodes.getFirst();
            sb.append(node.convertToJava(className));

            if (node instanceof AsmtNode || node instanceof FunctionCallNode) {
                sb.append(";");
            }
            sb.append("\n");
            bodyStmtNodes.removeFirst();
        }

        if(returnStmtNode != null){
            sb.append(returnStmtNode.convertToJava(className));
        }

        return sb.toString();
    }

    @Override
    public String convertToC() {
        return null;
    }

    @Override
    public String convertToPython() {
        String body = (bodyStmtNodes.stream().map(JottTree::convertToPython).collect(Collectors.joining("\n")) + returnStmtNode.convertToPython()).replaceAll("\n", "\n\t");
        return "\t" + (body.isEmpty() ? "pass" : body) + "\n"; // Python requires at least one statement in a block, pass can be used to suffice.
    }

    @Override
    public void validateTree() throws NodeValidateException {
        for (BodyStmtNode node : bodyStmtNodes) {
            node.validateTree();
        }

        // Normally, if a function body doesn't have a return statement at the
        // end of it but the function is defined to return a type, then that's
        // an error. However, if the last BodyStmtNode of the body is an
        // IfStmtNode, then those returns will suffice.
        //
        // The internal consistency of the returns in the IfStmtNode are
        // already validated in that node, and the ReturnStmtNodes in the
        // IfStmtNode already validate that their return types are equal to
        // the function return types -- so we don't need to check that here.
        //
        // We just need to capture a potential exception when validating *our*
        // return statement node -- and if that indicates a lack of a return
        // when this function returns, *and* the last BodyStmtNode is indeed an
        // IfStmtNode, then ignore the NodeValidateException that we got --
        // the lack of a return is actually okay!
        //
        // XXX: It is an absolute kludge-and-a-half that we're checking
        // against a literal string value. If this message or the one in
        // ReturnStmtNode is changed without coordinating the too, we're
        // boned.

        try {
            returnStmtNode.validateTree();
        } catch (NodeValidateException ex) {
            if (ex.getOriginalMessage().equals("Function is missing return.")) {
                // This is the special path described above.
                if (bodyStmtNodes.size() == 0) {
                    throw ex;
                }
                BodyStmtNode last = bodyStmtNodes.get(bodyStmtNodes.size() - 1);
                if (! (last instanceof IfStmtNode
                        && ((IfStmtNode)last).returns()) ) {
                    throw ex;
                }
            } else {
                throw ex;
            }
        }
    }
}
