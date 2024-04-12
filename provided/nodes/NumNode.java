package provided.nodes;

import provided.Token;
import provided.TokenDeque;

/**
 * Class for Number nodes
 *
 * @author Ewen Cazuc
 * @author Sebastian LaVine <sml1040@rit.edu>
 */
public class NumNode implements OperandNode {
    private final int startLine;
    private final String num;
    private final TypeNode.VariableType variableType;

    private NumNode(int startLine, String num) {
        this.num = num;
        this.startLine = startLine;
        this.variableType = num.contains(".") ? TypeNode.VariableType.DOUBLE : TypeNode.VariableType.INTEGER;
    }

    public static NumNode parseNumNode(TokenDeque tokens, boolean isNegative) {
        Token number = tokens.removeFirst();
        return new NumNode(number.getLineNum(),
            (isNegative) ? "-" + number.getToken() : number.getToken()
        );
    }

    public boolean isZero() {
        // Replace all decimals and zeros, then check if the string is empty
        return num.replace(".", "").replace("0", "").isEmpty();
    }

    @Override
    public String convertToJott() {
        return num;
    }

    @Override
    public String convertToJava(String className) {
        return num;
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
    public void validateTree() throws NodeValidateException {}

    @Override
    public int getStartLine() {
        return startLine;
    }

    public TypeNode.VariableType getVariableType() {
        return variableType;
    }
}
