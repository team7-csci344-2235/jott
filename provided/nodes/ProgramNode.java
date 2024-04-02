package provided.nodes;

import provided.JottTree;
import provided.TokenDeque;
import provided.nodes.TypeNode.VariableType;

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

    private final Map<String, ArrayList<VariableType>> programParamTypes;

    private final String filename;

    private ProgramNode(ArrayList<FunctionDefNode> functionDefNodes, String filename, HashMap<String, ArrayList<VariableType>> programParamTypes) {
        this.functionDefNodes = functionDefNodes;
        this.programParamTypes = programParamTypes;
        this.filename = filename;
    }

    private ProgramNode(String filename) {
        this.functionDefNodes = null;
        this.programParamTypes = new HashMap<>();
        this.filename = filename;
    }

    /**
     * Parses CircularBuffer of tokens into a ProgramNode.
     *
     * @param tokens: Current set of tokens
     * @return the root of the Jott Parse Tree represented by the tokens.
     */
    public static ProgramNode parseProgramNode(TokenDeque tokens) throws NodeParseException {
        if (tokens.isEmpty())
            return new ProgramNode(tokens.getLastRemoved().getFilename());

        HashMap<String, ArrayList<VariableType>> programParamTypes = new HashMap<>();
        ArrayList<FunctionDefNode> functionDefNodes = new ArrayList<>();
        while (!tokens.isEmpty())
            functionDefNodes.add(FunctionDefNode.parseFunctionDefNode(tokens, programParamTypes));

        return new ProgramNode(functionDefNodes, tokens.getLastRemoved().getFilename(), programParamTypes);
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
        if(!programParamTypes.containsKey("main"))
            throw new NodeValidateException("No main function defined", filename, 0);

        if (functionDefNodes != null)
            for (FunctionDefNode fdn : functionDefNodes) {
                // Note that we don't check for an exception or specify an
                // error message *here*. If this node is invalid, then an
                // exception further down the stack will provide a more
                // descriptive error message.
                fdn.validateTree();
            }
    }
}
