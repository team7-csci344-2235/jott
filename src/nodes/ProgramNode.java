package src.nodes;

import src.JottTree;
import src.TokenDeque;

import java.util.ArrayList;
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

    private ProgramNode(ArrayList<FunctionDefNode> functionDefNodes) {
        this.functionDefNodes = functionDefNodes;
    }

    private ProgramNode() {
        this.functionDefNodes = null;
    }

    /**
     * Parses CircularBuffer of tokens into a ProgramNode.
     *
     * @param tokens: Current set of tokens
     * @return the root of the Jott Parse Tree represented by the tokens.
     */
    public static ProgramNode parseProgramNode(TokenDeque tokens) throws NodeParseException {
        if (tokens.isEmpty())
            return new ProgramNode();

        ArrayList<FunctionDefNode> functionDefNodes = new ArrayList<>();
        while (!tokens.isEmpty())
            functionDefNodes.add(FunctionDefNode.parseFunctionDefNode(tokens));

        return new ProgramNode(functionDefNodes);
    }

    @Override
    public String convertToJott() {
        if (functionDefNodes == null) return "";
        return functionDefNodes.stream().map(FunctionDefNode::convertToJott).collect(JOTT_LIST_COLLECTOR);
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
