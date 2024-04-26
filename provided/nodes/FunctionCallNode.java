package provided.nodes;

import provided.TokenDeque;
import provided.TokenType;
import provided.VariableTable;

/**
 * Class for function call nodes
 *
 * @author Ethan Hartman <ehh4525@rit.edu>
 */
public class FunctionCallNode implements OperandNode, BodyStmtNode {
    private final int startLine;
    private final String filename;
    private final IDNode idNode;
    private final ParamsNode parameters;
    private final VariableTable variableTable;

    private FunctionCallNode(int startLine, String filename, IDNode idNode, ParamsNode parameters, VariableTable variableTable) {
        this.idNode = idNode;
        this.parameters = parameters;
        this.startLine = startLine;
        this.filename = filename;
        this.variableTable = variableTable;
    }

    /**
     * Parses a function call node from the given tokens
     * @param tokens the tokens to parse
     * @return the parsed function call node
     * @throws NodeParseException if the tokens do not form a valid function call node
     */
    public static FunctionCallNode parseFunctionCallNode(TokenDeque tokens, VariableTable variableTable) throws NodeParseException {
        tokens.removeFirst(); // We know we'll have FC header first, so remove it
        tokens.validateFirst(TokenType.ID_KEYWORD);
        int startLine = tokens.getFirst().getLineNum();
        IDNode idNode = IDNode.parseIDNode(tokens);
        tokens.validateFirst(TokenType.L_BRACKET);
        tokens.removeFirst(); // Remove L bracket
        ParamsNode parameters = ParamsNode.parseParamsNode(tokens, idNode.getIdStringValue(), variableTable);
        tokens.validateFirst(TokenType.R_BRACKET);
        tokens.removeFirst();
        return new FunctionCallNode(startLine, tokens.getLastRemoved().getFilename(), idNode, parameters, variableTable);
    }

    @Override
    public String convertToJott() {
        return "::" + idNode.convertToJott() + "[" + parameters.convertToJott() + "]";
    }

    @Override
    public String convertToJava(String className) {
        if(idNode.convertToJava(className).equals("print")){
            //might need to be quoted
            return "System.out.println(" + parameters.convertToJava(className) + ")";
        }
        if(idNode.convertToJava(className).equals("length")){
            return parameters.convertToJava(className)+".length()";
        }
        if(idNode.convertToJava(className).equals("concat")){
            String temp = parameters.convertToJava(className);
            //might be space not comma
            String[] array = temp.split(", ");
            
            String returnVal = "";
            if(array.length != 0){
                returnVal += array[0];
            
                for(int x=1; x<array.length; x++){
                    returnVal += " + " + array[x];
                }
            }
            return returnVal;
        }
        return idNode.convertToJava(className) + "(" + parameters.convertToJava(className) + ")";
    }

    @Override
    public String convertToC() {
        if(idNode.convertToC().equals("print")){
            return "/* TODO implement print ("
                + parameters.convertToC() + ") */";
            //String ret = "printf(";
            //String params = parameters.convertToC();

            //////??????????????????
            //ret += ")";
        }

        if(idNode.convertToC().equals("length")){
            return "strlen("+parameters.convertToC()+")";
        }

        // `concat` is implemented as a function at the top of each C
        // program we generate. See ProgramNode::convertToC for details.

        return idNode.convertToC() + "(" + parameters.convertToC() + ")";
    }

    @Override
    public String convertToPython() {
        return switch(idNode.getIdStringValue()){
            case "length" -> "len(" + parameters.convertToPython() + ")";
            case "concat" -> parameters.convertToPython().replace(", ", " + ");
            default -> idNode.convertToPython() + "(" + parameters.convertToPython() + ")";
        };
    }

    @Override
    public void validateTree() throws NodeValidateException {
        idNode.validateTree();
        // Ensure we have the function we want to call
        if (!variableTable.hasFunction(idNode.getIdStringValue()))
            throw new NodeValidateException("Call to unknown function: '" + idNode.convertToJott() + "'", filename, startLine);
        parameters.validateTree();
    }

    @Override
    public int getStartLine() {
        return startLine;
    }

    public IDNode getIdNode() {
        return idNode;
    }
}
