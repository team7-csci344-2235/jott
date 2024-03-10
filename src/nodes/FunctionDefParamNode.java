package src.nodes;

import src.JottTree;
import src.TokenDeque;
import src.TokenType;

import java.util.ArrayList;

import static src.nodes.ProgramNode.JOTT_LIST_COLLECTOR;

public class FunctionDefParamNode implements JottTree {

    private final IDNode firstParamName;
    private final TypeNode firstParamType;
    private final ArrayList<FunctionDefParamTNode> functionDefParamTNodes;

private FunctionDefParamNode(IDNode name, TypeNode type,
                             ArrayList<FunctionDefParamTNode> functionDefParamTNodes) {
    this.firstParamName = name;
    this.firstParamType = type;
    this.functionDefParamTNodes = functionDefParamTNodes;
}

    public static FunctionDefParamNode parseFunctionDefParamNode(TokenDeque tokens) throws NodeParseException {
        // Parse ID, followed by colon.
        IDNode firstParamName = IDNode.parseIDNode(tokens);
        tokens.validateFirst(TokenType.COLON);
        tokens.removeFirst();

        // Parse type.
        TypeNode firstParamType = TypeNode.parseTypeNode(tokens);

        // (Potentially) followed by more parameters.
        ArrayList<FunctionDefParamTNode> functionDefParamTNodes1 = new ArrayList<>();
        while (!tokens.isFirstOf(TokenType.R_BRACE)) {
            functionDefParamTNodes1.add(FunctionDefParamTNode.parseFunctionDefParamTNode(tokens));
        }

        return new FunctionDefParamNode(firstParamName, firstParamType,functionDefParamTNodes1);
    }

    @Override
    public String convertToJott() {
        if (this.firstParamName == null) {
            return "";
        }
        return this.firstParamName.convertToJott() + ": "
            + this.firstParamType.convertToJott()
            + functionDefParamTNodes.stream().map(FunctionDefParamTNode::convertToJott).collect(JOTT_LIST_COLLECTOR);
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
