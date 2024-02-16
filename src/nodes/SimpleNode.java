package src.nodes;

import src.JottTree;

/**
 * Class for a simple node... These really just implement JottTree and
 * return themselves on converting to Jott.
 * Meant for single tokens.
 *
 * @author Ethan Hartman <ehh4525@rit.edu>
 */
public class SimpleNode implements JottTree {
    protected final String value;

    public SimpleNode(String value) {
        this.value = value;
    }

    @Override
    public String convertToJott() {
        return value;
    }

    @Override
    public String convertToJava(String className) {
        return value;
    }

    @Override
    public String convertToC() {
        return value;
    }

    @Override
    public String convertToPython() {
        return value;
    }

    @Override
    public boolean validateTree() {
        return false;
    }
}
