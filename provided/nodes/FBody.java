package provided.nodes;

import provided.*;

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

    public static FBody parseFBodyNode(TokenDeque tokens, VariableTable variableTable, String functionName, SymbolTable symbolTable) throws NodeParseException {
        ArrayList<VarDecNode> varDecNodes = new ArrayList<>();

        // Check for variable declarations at the start of the function body.
        // Note that no variable declarations is acceptable.
        while ((!tokens.isFirstOf(TokenType.ID_KEYWORD, TokenType.FC_HEADER, TokenType.R_BRACE)
                && (!tokens.isFirstOf("Return")) || tokens.isFirstOf("Double", "Integer", "String", "Boolean"))) {
            varDecNodes.add(VarDecNode.parseVarDecNode(tokens, variableTable));
        }

        return new FBody(varDecNodes, BodyNode.parseBodyNode(tokens, variableTable, functionName, symbolTable));
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
        for (VarDecNode varDecNode : varDecNodes)
            varDecNode.validateTree();

        bodyNode.validateTree();
    }
}
