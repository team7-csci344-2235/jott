package src.nodes;

import src.JottTree;
import java.util.ArrayList;
import static src.JottParser.LIST_SEP;

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
        StringBuilder result = new StringBuilder();

        // Convert expressions to Jott representation
        if (expressions != null && !expressions.isEmpty())
            for (int i = 0; i < expressions.size(); i++) {
                if (i > 0) result.append(LIST_SEP);
                result.append(expressions.get(i).convertToJott());
            }

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
