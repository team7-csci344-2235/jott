package provided.nodes;

import provided.JottTree;
import provided.SymbolTable;
import provided.TokenDeque;

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
    private final SymbolTable symbolTable;
    private final String filename;

    private ProgramNode(ArrayList<FunctionDefNode> functionDefNodes, String filename, SymbolTable symbolTable) {
        this.functionDefNodes = functionDefNodes;
        this.symbolTable = symbolTable;
        this.filename = filename;
    }

    private ProgramNode(String filename) {
        this.functionDefNodes = null;
        this.filename = filename;
        symbolTable = new SymbolTable();
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

        SymbolTable symbolTable = new SymbolTable();
        ArrayList<FunctionDefNode> functionDefNodes = new ArrayList<>();
        while (!tokens.isEmpty())
            functionDefNodes.add(FunctionDefNode.parseFunctionDefNode(tokens, symbolTable));

        return new ProgramNode(functionDefNodes, tokens.getLastRemoved().getFilename(), symbolTable);
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
        if (functionDefNodes == null){
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for(FunctionDefNode func : this.functionDefNodes) {
            sb.append(func.convertToJava(className));
        }
        return sb.toString();
    }

    @Override
    public String convertToC() {
        return null;
    }

    @Override
    public String convertToPython(int tabNumber) {
        if (functionDefNodes == null) return "";

        StringBuilder sb = new StringBuilder();
        for (FunctionDefNode func : this.functionDefNodes)
            sb.append(func.convertToPython(tabNumber));

        // Add call to main.
        sb.append("main()");
        return sb.toString();
    }

    @Override
    public void validateTree() throws NodeValidateException {
        // Validate all function definitions
        if (functionDefNodes != null)
            for (FunctionDefNode fdn : functionDefNodes) {
                // Note that we don't check for an exception or specify an
                // error message *here*. If this node is invalid, then an
                // exception further down the stack will provide a more
                // descriptive error message.
                fdn.validateTree();
            }

        // Check for a main function now that all functions have been defined
        if(!symbolTable.hasFunction("main"))
            throw new NodeValidateException("No main function defined", filename, 0);

        // Check for main function parameters
        if (symbolTable.getFunctionParams("main") != null)
            throw new NodeValidateException("main function should not have parameters", filename, 0);

        // Check that main returns Void.
        if (symbolTable.getFunctionReturnType("main") != null) {
            throw new NodeValidateException("main function must return Void", filename, 0);
        }
    }
}
