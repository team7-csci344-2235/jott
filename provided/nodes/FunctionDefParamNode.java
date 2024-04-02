package provided.nodes;

import provided.JottTree;
import provided.TokenDeque;
import provided.TokenType;

import java.util.ArrayList;

import static provided.nodes.ProgramNode.JOTT_LIST_COLLECTOR;

public class FunctionDefParamNode implements JottTree {

    private final IDNode firstParamName;
    private final TypeNode firstParamType;
    private final ArrayList<FunctionDefParamTNode> theRest;

    private FunctionDefParamNode(IDNode name, TypeNode type,
                                 ArrayList<FunctionDefParamTNode> theRest) {
        this.firstParamName = name;
        this.firstParamType = type;
        this.theRest = theRest;
    }

    public static FunctionDefParamNode parseFunctionDefParamNode(TokenDeque tokens) throws NodeParseException {
        // Parse ID, followed by colon.
        IDNode firstParamName = IDNode.parseIDNode(tokens);
        tokens.validateFirst(TokenType.COLON);
        tokens.removeFirst();

        // Parse type.
        TypeNode firstParamType = TypeNode.parseTypeNode(tokens);

        // (Potentially) followed by more parameters.

        if(tokens.getFirst().getTokenType() == TokenType.R_BRACKET){
            return new FunctionDefParamNode(firstParamName,
                    firstParamType, null);
        }
        else{
            ArrayList<FunctionDefParamTNode> params = new ArrayList<>();
            for (;;) {
                params.add(FunctionDefParamTNode.parseFunctionDefParamTNode(tokens)); // We should have expressions here, let's parse.
                if (tokens.isFirstOf(TokenType.COMMA))
                    tokens.removeFirst(); // Remove comma
                else
                    return new FunctionDefParamNode(firstParamName, firstParamType, params);
            }
        }     
    }

    @Override
    public String convertToJott() {
        if (this.firstParamName == null) {
            return "";
        }
        if(theRest != null){
            return this.firstParamName.convertToJott() + ": "
                + this.firstParamType.convertToJott()
                + this.theRest.stream().map(FunctionDefParamTNode::convertToJott).collect(JOTT_LIST_COLLECTOR);
        }
        else{
            return this.firstParamName.convertToJott() + ": "
            + this.firstParamType.convertToJott();
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
    public void validateTree() throws NodeValidateException {
        return;
    }

    public IDNode getFirstParamName() {
        return firstParamName;
    }

    public TypeNode getFirstParamType() {
        return this.firstParamType;
    }

    public ArrayList<provided.nodes.FunctionDefParamTNode> getTheRest() {
        return theRest;
    }
}
