package src.nodes;

import src.JottTree;

/**
 * Class for operand nodes
 * < operand > -> <id > | <num > | < func_call > | -< num >
 *
 * @author Ethan Hartman <ehh4525@rit.edu>
 */
public class OperandNode implements JottTree {
    private String value;
    private boolean isNegative;
    private FunctionCallNode functionCallNode;

    // Constructor for identifiers and numbers
    public OperandNode(String value, boolean isNegative) {
        this.value = value;
        this.isNegative = isNegative;
    }

    // Constructor for function calls
    public OperandNode(FunctionCallNode functionCallNode) {
        this.functionCallNode = functionCallNode;
    }

    @Override
    public String convertToJott() {
        StringBuilder result = new StringBuilder();

        if (isNegative)
            result.append("-");

        if (value != null)
            result.append(value);
        else if (functionCallNode != null)
            result.append(functionCallNode.convertToJott());

        return result.toString();
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
