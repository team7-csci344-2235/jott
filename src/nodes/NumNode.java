package src.nodes;

import src.Token;
import src.TokenDeque;

/**
 * Class for Number nodes
 *
 * @author TODO
 */
public class NumNode implements OperandNode {

    private final String num;
    private NumNode(Token num) {
        this.num = num.getToken();
    }

    public static NumNode parseNumNode(TokenDeque tokens, boolean isNegative) throws NodeParseException {
        if (isNegative) {
            tokens.removeFirst();
            return new NumNode(tokens.getFirst());
        } else {
            return new NumNode(tokens.getFirst());
        }
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
    public boolean validateTree() {
        return false;
    }
}
