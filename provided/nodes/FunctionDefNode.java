package provided.nodes;

import provided.*;

import java.util.ArrayList;

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
    private final SymbolTable symbolTable;

    private FunctionDefNode(int startLine, String filename, IDNode name, FunctionDefParamNode params,
                            TypeNode maybeReturnType, FBody functionBody, SymbolTable symbolTable){
        this.name = name;
        this.params = params;
        this.maybeReturnType = maybeReturnType; // Note: null is a valid value
        this.functionBody = functionBody;
        this.startLine = startLine;
        this.filename = filename;
        this.symbolTable = symbolTable;
    }

    public static FunctionDefNode parseFunctionDefNode(TokenDeque tokens, SymbolTable symbolTable) throws NodeParseException {
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

        // Create function body and give it a new localized variable table.
        FBody functionBody = FBody.parseFBodyNode(tokens, symbolTable.createVariableTable());

        tokens.validateFirst(TokenType.R_BRACE);
        tokens.removeFirst();

        return new FunctionDefNode(startLine,tokens.getLastRemoved().getFilename(), name, params, returnType,
                functionBody, symbolTable);
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
        ArrayList<TypeNode.VariableType> nodesParams = null;
        name.validateTree();
        if (maybeReturnType != null) maybeReturnType.validateTree();
        if (params != null) {
            nodesParams = new ArrayList<>();
            nodesParams.add(params.getFirstParamType().getType());

            if (params.getTheRest() != null)
                for (FunctionDefParamTNode theRest : params.getTheRest())
                    nodesParams.add(theRest.getTypeNode().getType());

            params.validateTree();
        }

        // Try to add the function to the symbol table. Yes, params can be null.
        if (!symbolTable.tryAddFunction(name.getIdStringValue(), nodesParams, maybeReturnType != null ? maybeReturnType.getType() : null))
            throw new NodeValidateException("Function " + name.getIdStringValue() + " already exists.", filename, startLine);

        // Finish validation.
        functionBody.validateTree();
    }
}
