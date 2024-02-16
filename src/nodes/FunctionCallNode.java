package src.nodes;

import src.JottTree;

/**
 * Class for function call nodes
 * < func_call > -> :: < id >[ < params > ]
 *
 * @author Ethan Hartman <ehh4525@rit.edu>
 */
public class FunctionCallNode implements JottTree {
    private final String functionName;
    private final ParamsNode parameters;

    public FunctionCallNode(String functionName, ParamsNode parameters) {
        this.functionName = functionName;
        this.parameters = parameters;
    }

    @Override
    public String convertToJott() {
        return "::" + functionName + "[" + parameters.convertToJott() + "]";
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
