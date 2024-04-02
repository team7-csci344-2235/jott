package provided.nodes;

import provided.JottTree;
import provided.TokenDeque;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Class for Program nodes
 *
 * @author Ethan Hartman <ehh4525@rit.edu>
 */
public class ProgramNode implements JottTree {
    public final static Collector<CharSequence, ?, String> JOTT_LIST_COLLECTOR = Collectors.joining(", ");

    private final ArrayList<FunctionDefNode> functionDefNodes;

    private final Map<String, ArrayList<TypeNode.VariableType>> programsParamsType;

    private final String filename;

    private ProgramNode(ArrayList<FunctionDefNode> functionDefNodes, String filename) throws NodeValidateException{
        this.functionDefNodes = functionDefNodes;
        this.programsParamsType = new HashMap<>();
        this.filename = filename;


        for (FunctionDefNode functionDefNode : this.functionDefNodes) {
            //For every function in the program we check if it has params and if so we add them in the map

            if (functionDefNode.getParams() != null) {
                ArrayList<TypeNode.VariableType> nodesParams = new ArrayList<>();
                nodesParams.add(functionDefNode.getParams().getFirstParamType().getType());

                if (functionDefNode.getParams().getTheRest() != null) {
                    nodesParams.add(functionDefNode.getParams().getTheRest().getTypeNode().getType());
                }
                if(this.programsParamsType.containsKey(functionDefNode.getName().getIdStringValue())){
                    throw new NodeValidateException("Function " + functionDefNode.getName().getIdStringValue() + " is already defined", filename, functionDefNode.getStartLine());
                }
                this.programsParamsType.put(functionDefNode.getName().getIdStringValue(), nodesParams);
            }
        }
    }

    private ProgramNode(String filename) {
        this.functionDefNodes = null;
        this.programsParamsType = new HashMap<>();
        this.filename = filename;
    }

    /**
     * Parses CircularBuffer of tokens into a ProgramNode.
     *
     * @param tokens: Current set of tokens
     * @return the root of the Jott Parse Tree represented by the tokens.
     */
    public static ProgramNode parseProgramNode(TokenDeque tokens) throws NodeParseException, NodeValidateException {
        if (tokens.isEmpty())
            return new ProgramNode(tokens.getLastRemoved().getFilename());

        ArrayList<FunctionDefNode> functionDefNodes = new ArrayList<>();
        while (!tokens.isEmpty())
            functionDefNodes.add(FunctionDefNode.parseFunctionDefNode(tokens));

        return new ProgramNode(functionDefNodes, tokens.getLastRemoved().getFilename());
    }

    @Override
    public String convertToJott() {
        if (functionDefNodes == null) return "";

        StringBuilder sb = new StringBuilder();
        for (FunctionDefNode func : this.functionDefNodes) {
            sb.append(func.convertToJott());
        }
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
        if(!this.programsParamsType.containsKey("main")){
            throw new NodeValidateException("No main function defined",filename , 0);
        }
        for (FunctionDefNode fdn : this.functionDefNodes) {
            // Note that we don't check for an exception or specify an
            // error message *here*. If this node is invalid, then an
            // exception further down the stack will provide a more
            // descriptive error message.
            fdn.validateTree();
        }
    }
}
