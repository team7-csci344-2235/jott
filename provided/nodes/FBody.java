package provided.nodes;

import provided.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

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

    public static FBody parseFBodyNode(TokenDeque tokens, VariableTable variableTable, String functionName) throws NodeParseException {
        ArrayList<VarDecNode> varDecNodes = new ArrayList<>();

        // Check for variable declarations at the start of the function body.
        // Note that no variable declarations is acceptable.
        while ((!tokens.isFirstOf(TokenType.ID_KEYWORD, TokenType.FC_HEADER, TokenType.R_BRACE)
                && (!tokens.isFirstOf("Return")) || tokens.isFirstOf("Double", "Integer", "String", "Boolean"))) {
            varDecNodes.add(VarDecNode.parseVarDecNode(tokens, variableTable));
        }

        return new FBody(varDecNodes, BodyNode.parseBodyNode(tokens, variableTable, functionName));
    }

    @Override
    public String convertToJott() {
        StringBuilder result = new StringBuilder();
        while (!varDecNodes.isEmpty()) {
            result.append(varDecNodes.getFirst().convertToJott());
            varDecNodes.removeFirst();
        }
        return result + bodyNode.convertToJott();
    }

    @Override
    public String convertToJava(String className) {
        StringBuilder result = new StringBuilder();
        while (!varDecNodes.isEmpty()) {
            result.append(varDecNodes.getFirst().convertToJava(className));
            varDecNodes.removeFirst();
        }
        return result + bodyNode.convertToJava(className);
    }

    @Override
    public String convertToC() {
        return null;
    }

    @Override
    public String convertToPython() {
        String varDecStr = "";
        if (!varDecNodes.isEmpty())
            varDecStr = "\t" + varDecNodes.stream().map(VarDecNode::convertToPython).collect(Collectors.joining("\n")).replaceAll("\n", "\n\t") + "\n";
        return varDecStr + bodyNode.convertToPython();
    }

    @Override
    public void validateTree() throws NodeValidateException {
        for (VarDecNode varDecNode : varDecNodes)
            varDecNode.validateTree();

        bodyNode.validateTree();
    }
}
