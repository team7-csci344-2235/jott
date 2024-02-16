package src.nodes;

import src.JottTree;
import java.util.ArrayList;

/**
 * Class for function call nodes
 * < params > -> < expr >< params_t >* | nil
 * params_t is handled within this class as expressions are taken in as a list.
 *
 * @author Ethan Hartman <ehh4525@rit.edu>
 */
public class ParamsNode implements JottTree {
    private final ArrayList<JottTree> expressions;

    public ParamsNode(ArrayList<JottTree> expressions) {
        this.expressions = expressions;
    }

    @Override
    public String convertToJott() {
        return ProgramNode.arrListToJott(expressions);
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
