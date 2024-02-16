package src.nodes;

import src.JottTree;

import java.util.ArrayList;

public class ProgramNode implements JottTree {
    private final static String LIST_SEP = ", ";

    // TODO probably change functionDefs to be arraylist of FunctionDefNode
    private final ArrayList<JottTree> functionDefNodes;

    public ProgramNode(ArrayList<JottTree> functionDefNodes) {
        this.functionDefNodes = functionDefNodes;
    }

    /**
     * Often times, we may convert a list of JottTree, lets set this as our method to reduce code copies.
     * @param list of tree nodes to convert to Jott.
     * @return Jott String representation of the JottTree list.
     */
    public static String arrListToJott(ArrayList<JottTree> list) {
        if (list != null && !list.isEmpty()) {
            StringBuilder result = new StringBuilder();
            // Convert expressions to Jott representation
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) result.append(LIST_SEP);
                result.append(list.get(i).convertToJott());
            }

            return result.toString();
        }
        return "";
    }

    @Override
    public String convertToJott() {
        return arrListToJott(functionDefNodes);
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
