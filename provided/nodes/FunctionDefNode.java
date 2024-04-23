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
    private final VariableTable variableTable;

    private FunctionDefNode(int startLine, String filename, IDNode name, FunctionDefParamNode params,
                            TypeNode maybeReturnType, FBody functionBody, SymbolTable symbolTable, VariableTable variableTable){
        this.name = name;
        this.params = params;
        this.maybeReturnType = maybeReturnType; // Note: null is a valid value
        this.functionBody = functionBody;
        this.startLine = startLine;
        this.filename = filename;
        this.symbolTable = symbolTable;
        this.variableTable = variableTable;
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

        // Variable table. Contains information about parameters as well as
        // variables declared in the function body.
        // XXX: Yes, it is scuffed that we have both this and the symbolTable
        // at the top level. This is because we have gone with registering
        // declarations in the `validateTree` level, so this object must
        // stick around in the FunctionDefNode until then.
        var variableTable = symbolTable.createVariableTable();

        // TODO: give variableTable to functiondefparamnode, or otherwise
        // get the info from it to put into variableTable

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
        FBody functionBody = FBody.parseFBodyNode(tokens, variableTable, name.getIdStringValue());

        tokens.validateFirst(TokenType.R_BRACE);
        tokens.removeFirst();

        return new FunctionDefNode(startLine,tokens.getLastRemoved().getFilename(), name, params, returnType,
                functionBody, symbolTable, variableTable);
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
        //public 
        StringBuilder sb = new StringBuilder("public static ");
        if (this.maybeReturnType == null) {
            sb.append("void ");
        }
        else{
            //public String 
            sb.append(this.maybeReturnType.convertToJava(className)+ " ");
        }

        sb.append(this.name.convertToJava(className)+ "(");

        if(this.name.convertToJava(className).equals("main")){
            sb.append("String args[]");
        }
        else if (this.params != null) {
            sb.append(this.params.convertToJava(className));
        }
        sb.append("){");
        sb.append("\n");
        sb.append(this.functionBody.convertToJava(className));
        sb.append("}");
        sb.append("\n");
        return sb.toString();
    }

    @Override
    public String convertToC() {
        StringBuilder sb = new StringBuilder();
        if (this.maybeReturnType == null) {
            sb.append("void ");
        }
        else{
            sb.append(this.maybeReturnType.convertToC()+ " ");
        }

        sb.append(this.name.convertToC()+ "(");

        if (this.params != null) {
            sb.append(this.params.convertToC());
        }
        sb.append("){");
        sb.append("\n");
        sb.append(this.functionBody.convertToC());
        sb.append("}");
        sb.append("\n");
        return sb.toString();
    }

    @Override
    public String convertToPython(int tabNumber) {
        String result = "";
        result += "def " + name.convertToPython(1) + "(";
        if(params != null) {
            result += params.convertToPython(1);
        }
        result += "):\n";
        return result;
    }

    @Override
    public void validateTree() throws NodeValidateException {
        ArrayList<TypeNode.VariableType> nodesParams = null;
        name.validateTree();
        if (maybeReturnType != null) maybeReturnType.validateTree();
        if (params != null) {
            var firstId = params.getFirstParamId();
            var firstType = params.getFirstParamType();

            nodesParams = new ArrayList<>();
            nodesParams.add(firstType.getType());

            // Try to add the variable to the variable table.
            if (!variableTable.tryDeclareParamVariable(firstId.getIdStringValue(), firstType.getType()))
                throw new NodeValidateException("Variable " + firstId.getIdStringValue() + " already declared.",
                        firstType.getFilename(), firstType.getStartLine());

            if (params.getTheRest() != null)
                for (FunctionDefParamTNode param : params.getTheRest()) {
                    var id = param.getIdNode();
                    var type = param.getTypeNode();
                    nodesParams.add(type.getType());

                    if (!variableTable.tryDeclareParamVariable(id.getIdStringValue(), type.getType()))
                        throw new NodeValidateException("Variable " + id.getIdStringValue() + " already declared.",
                                type.getFilename(), type.getStartLine());
                }

            params.validateTree();
        }

        // Try to add the function to the symbol table. Yes, params can be null.
        if (!symbolTable.tryAddFunction(name.getIdStringValue(), nodesParams, maybeReturnType != null ? maybeReturnType.getType() : null))
            throw new NodeValidateException("Function " + name.getIdStringValue() + " already exists.", filename, startLine);

        // Finish validation.
        functionBody.validateTree();
    }
}
