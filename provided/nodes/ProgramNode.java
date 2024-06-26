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
        StringBuilder sb = new StringBuilder();
        className = className.substring(0, className.length()-5);
        
        sb.append("public class "+className+"{\n");

        if (functionDefNodes == null){
            sb.append("}");
            return sb.toString();
        }

        for(FunctionDefNode func : this.functionDefNodes) {
            sb.append(func.convertToJava(className));
        }

        sb.append("\n}");
        return sb.toString();
    }

    @Override
    public String convertToC() {
        //need to check if main is lal the way at the bottom
        StringBuilder sb = new StringBuilder();

        sb.append("#include <stdlib.h>\n");
        sb.append("#include <stdio.h>\n");
        sb.append("#include <string.h>\n");
        sb.append("#include <stdbool.h>\n\n");

        sb.append("/* Concatenates two strings, in accordance with the Jott spec.\n");
        sb.append(" * Unlike strcpy/strcat, this does *NOT* append to the memory locations\n");
        sb.append(" * provided. A new buffer is allocated and filled with the contents of\n");
        sb.append(" * the provided strings. */\n");
        sb.append("char *\n");
        sb.append("concat(char *s1, char *s2)\n");
        sb.append("{\n");
        sb.append("    size_t s1_length = strlen(s1);\n");
        sb.append("    size_t length = s1_length + strlen(s2);\n");
        sb.append("    char *new = malloc((length + /* NUL */ 1) * sizeof(*new));\n");
        sb.append("    strcpy(new, s1);\n");
        sb.append("    strcpy(new + s1_length, s2);\n");
        sb.append("    return new; /* Thanks for not requiring us to free, Professor J */\n");
        sb.append("}\n\n");

        if(functionDefNodes != null){
            for(FunctionDefNode func : this.functionDefNodes) {
                sb.append(func.convertToC());
            }
        }

        return sb.toString();
    }

    @Override
    public String convertToPython() {
        if (functionDefNodes == null) return "";

        StringBuilder sb = new StringBuilder();
        for (FunctionDefNode func : functionDefNodes)
            sb.append(func.convertToPython());

        // Add call to main.
        sb.append("main()\n\n"); // Python likes two lines of whitespace after.
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
