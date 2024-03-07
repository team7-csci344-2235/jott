package src.nodes;

import src.JottTree;
import src.Token;
import src.TokenDeque;
import src.TokenType;

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

        for (;;) {
            varDecNodes1.add(VarDecNode.parseVarDecNode(tokens));
            if (tokens.isFirstOf("If") || tokens.isFirstOf("While") || tokens.isFirstOf(TokenType.ID_KEYWORD) || tokens.isFirstOf(TokenType.FC_HEADER) || tokens.isFirstOf("Return"))
                break; //if the next node is a body node we break
        }

        BodyNode bodyNode1 = BodyNode.parseBodyNode(tokens);
        return new FBody(varDecNodes1, bodyNode1);
    }

    @Override
    public String convertToJott() {
        String result = "";
        while (!varDecNodes.isEmpty()) {
            result = result + varDecNodes.getFirst().convertToJott() + " ";
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
