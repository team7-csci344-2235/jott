package src.nodes;

import src.JottTree;
import src.TokenDeque;
import src.TokenType;

import java.util.ArrayList;

public class FunctionDefParamNode implements JottTree {

    private final IDNode firstParamName;
    private final TypeNode firstParamType;
    private final FunctionDefParamTNode theRest;

    private FunctionDefParamNode(IDNode name, TypeNode type,
            FunctionDefParamTNode theRest) {
        this.firstParamName = name;
        this.firstParamType = type;
        this.theRest = null;
    }

    public static FunctionDefParamNode parseFunctionDefParamNode(TokenDeque tokens) throws NodeParseException {
        // Parse ID, followed by colon.
        IDNode firstParamName = IDNode.parseIDNode(tokens);
        tokens.validateFirst(TokenType.COLON);
        tokens.removeFirst();

        // Parse type.
        TypeNode firstParamType = TypeNode.parseTypeNode(tokens);

        // (Potentially) followed by more parameters.
        if (tokens.isEmpty()) {
            // Then we're done, there's only one argument.
            return new FunctionDefParamNode(firstParamName,
                    firstParamType, null);
        }

        return new FunctionDefParamNode(firstParamName, firstParamType,
                FunctionDefParamTNode.parseFunctionDefParamTNode(tokens));
    }

    @Override
    public String convertToJott() {
        if (this.firstParamName == null) {
            return "";
        }
        return this.firstParamName.convertToJott() + ": "
            + this.firstParamType.convertToJott()
            + this.theRest.convertToJott();
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
