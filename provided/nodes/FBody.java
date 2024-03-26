package provided.nodes;

import provided.JottTree;
import provided.TokenDeque;
import provided.TokenType;

import java.util.ArrayList;

/**
 * Class for FBody nodes
 *
 * @author Adrienne Ressy <amr3032@rit.edu>
 */
public class FBody implements JottTree {

    private final ArrayList<VarDecNode> varDecNodes;
    private final BodyNode bodyNode;

    public FBody(ArrayList<VarDecNode> varDecNodes, BodyNode bodyNode) {
        this.varDecNodes = varDecNodes;
        this.bodyNode = bodyNode;
    }

    public static FBody parseFBodyNode(TokenDeque tokens) throws NodeParseException {
        ArrayList<VarDecNode> varDecNodes1 = new ArrayList<>();

        // Check for variable declarations at the start of the function body.
        // Note that no variable declarations is acceptable.
        while ((!tokens.isFirstOf(TokenType.ID_KEYWORD, TokenType.FC_HEADER, TokenType.R_BRACE)
                && (!tokens.isFirstOf("Return")) || tokens.isFirstOf("Double", "Integer", "String", "Boolean"))) {
            varDecNodes1.add(VarDecNode.parseVarDecNode(tokens));
        }

        BodyNode bodyNode1 = BodyNode.parseBodyNode(tokens);
        return new FBody(varDecNodes1, bodyNode1);
    }

    @Override
    public String convertToJott() {
        String result = "";
        while (!varDecNodes.isEmpty()) {
            result = result + varDecNodes.getFirst().convertToJott();
            varDecNodes.removeFirst();
        }
        return result + bodyNode.convertToJott();
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
