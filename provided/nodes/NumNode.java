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
    private NumNode(int startLine, String num) {
        this.num = num;
        this.startLine = startLine;
    }

    public static NumNode parseNumNode(TokenDeque tokens, boolean isNegative) {
        Token number = tokens.removeFirst();
        return new NumNode(number.getLineNum(),
            (isNegative) ? "-" + number.getToken() : number.getToken()
        );
    }

    @Override
    public String convertToJott() {
        return num;
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
    public void validateTree() throws NodeValidateException {
        return;
    }

    @Override
    public TypeNode.VariableType getEvaluationVariableType() {
        return num.contains(".") ? TypeNode.VariableType.DOUBLE : TypeNode.VariableType.INTEGER;
    }

    @Override
    public int getStartLine() {
        return startLine;
    }
}
