package provided.nodes;

import provided.JottTree;
import provided.TokenDeque;
import provided.TokenType;

/**
 * Class for Boolean nodes
 *
 * @author Lianna Pottgen, <lrp2755@rit.edu>
 */
public class BoolNode implements JottTree, ExprNode{
    private final int startLine;
    private final String trueOrFalse;

    private BoolNode(int startLine, String trueOrFalse) {
        this.trueOrFalse = trueOrFalse;
        this.startLine = startLine;
    }

    public static BoolNode parseBoolNode(TokenDeque tokens) throws NodeParseException {
        //get information
        tokens.validateFirst(TokenType.ID_KEYWORD); 

        //if "TRUE", "true", or any combination
            //return "True"
        //else if "FALSE", "false", or any combination
            //return "False"
        tokens.validateFirst("True", "False");
        return new BoolNode(tokens.getFirst().getLineNum(), tokens.removeFirst().getToken());
    }

    @Override
    public String convertToJott() {
        return trueOrFalse;
    }

    @Override
    public String convertToJava(String className) {
        if(trueOrFalse.toUpperCase().equals("TRUE")){
            return "true";
        }
        else{
            return "false";
        }
        
    }

    @Override
    public String convertToC() {
        if(trueOrFalse.toUpperCase().equals("TRUE")){
            return "true";
        }
        else{
            return "false";
        }
    }

    @Override
    public String convertToPython() {
        return trueOrFalse;
    }

    @Override
    public void validateTree() throws NodeValidateException {
        return;
    }

    @Override
    public int getStartLine() {
        return startLine;
    }
}

