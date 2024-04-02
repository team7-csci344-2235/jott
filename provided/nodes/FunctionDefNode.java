package provided.nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenDeque;
import provided.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for function definition nodes
 *
 * @author Sebastian LaVine <sml1040@rit.edu>
 */
public class FunctionDefNode implements JottTree {

    private final int startLine;
    private final String filename;
    private final IDNode name;
    private final FunctionDefParamNode params;

    // Void is not represented as a TypeNode but could be a potential "return
    // value" (or lack thereof) for a function. If this is null but the object
    // is otherwise valid, then the function "returns Void".
    private final TypeNode maybeReturnType;

    private final FBody functionBody;

    private final Map<String, TypeNode.VariableType> variablesType;
    private final HashMap<String, ArrayList<TypeNode.VariableType>> programParamMap;

    private FunctionDefNode(int startLine, String filename, IDNode name, FunctionDefParamNode params,
                            TypeNode maybeReturnType, FBody functionBody, HashMap<String, ArrayList<TypeNode.VariableType>> programParamMap){
        this.name = name;
        this.params = params;
        this.maybeReturnType = maybeReturnType; // Note: null is a valid value
        this.functionBody = functionBody;
        this.variablesType = new HashMap<>();
        this.startLine = startLine;
        this.filename = filename;
        this.programParamMap = programParamMap;

        for (VarDecNode varDecNode : functionBody.getVarDecNodes())
            variablesType.put(varDecNode.getIdNode().getIdStringValue(), varDecNode.getTypeNode().getType());

        if (params != null) {
            variablesType.put(params.getFirstParamName().getIdStringValue(), params.getFirstParamType().getType());

            if (params.getTheRest() != null)
                for (FunctionDefParamTNode theRest : params.getTheRest())
                    variablesType.put(theRest.getIdNode().getIdStringValue(), theRest.getTypeNode().getType());
        }
    }

    public static FunctionDefNode parseFunctionDefNode(TokenDeque tokens, HashMap<String, ArrayList<TypeNode.VariableType>> programParamMap) throws NodeParseException {
        // Check that we start with a Def.
        tokens.validateFirst("Def");
        tokens.removeFirst();
        int startLine = tokens.getFirst().getLineNum();
        // Check that we've got an ID (name).
        IDNode name = IDNode.parseIDNode(tokens);

        // Get parameters.

        tokens.validateFirst(TokenType.L_BRACKET);
        tokens.removeFirst();

        FunctionDefParamNode params = null;
        if (tokens.getFirst().getTokenType() != TokenType.R_BRACKET) {
            params = FunctionDefParamNode.parseFunctionDefParamNode(tokens);
        }

        tokens.validateFirst(TokenType.R_BRACKET);
        tokens.removeFirst();

        // Get return type.

        tokens.validateFirst(TokenType.COLON);
        tokens.removeFirst();

        // Special case check for "Void" followed by TypeNode check.
        TypeNode returnType = null; // <- special case for "Void"
        Token maybeVoid = tokens.getFirst();
        if (maybeVoid.getTokenType() == TokenType.ID_KEYWORD
                && maybeVoid.getToken().equals("Void")) {
            tokens.removeFirst();
        } else {
            returnType = TypeNode.parseTypeNode(tokens);
        }


        // Get function body.
        tokens.validateFirst(TokenType.L_BRACE);
        tokens.removeFirst();

        FBody functionBody = FBody.parseFBodyNode(tokens);

        tokens.validateFirst(TokenType.R_BRACE);
        tokens.removeFirst();

        return new FunctionDefNode(startLine,tokens.getLastRemoved().getFilename() ,name, params, returnType, functionBody, programParamMap);
    }

    @Override
    public String convertToJott() {
        StringBuilder sb = new StringBuilder("Def ");
        sb.append(this.name.convertToJott());
        sb.append("[");
        if (this.params != null) {
            sb.append(this.params.convertToJott());
        }
        sb.append("]:");
        if (this.maybeReturnType == null) {
            sb.append("Void");
        } else {
            sb.append(this.maybeReturnType.convertToJott());
        }
        sb.append("{");
        sb.append("\n");
        sb.append(this.functionBody.convertToJott());
        sb.append("}");
        sb.append("\n");
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
        if (params != null) {
            ArrayList<TypeNode.VariableType> nodesParams = new ArrayList<>();
            nodesParams.add(params.getFirstParamType().getType());

            if(programParamMap.containsKey(name.getIdStringValue()))
                throw new NodeValidateException("Function " + name.getIdStringValue() + " is already defined", filename, startLine);

            programParamMap.put(name.getIdStringValue(), nodesParams);
        }
        // TODO probably lower node validation. I just moved this stuff from ProgramNode into here.
    }
}
