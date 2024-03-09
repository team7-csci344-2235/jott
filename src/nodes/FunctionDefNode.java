package src.nodes;

import src.JottTree;
import src.Token;
import src.TokenDeque;
import src.TokenType;

/**
 * Class for function definition nodes
 *
 * @author TODO
 */
public class FunctionDefNode implements JottTree {

    private final IDNode name;
    private final FunctionDefParamNode params;

    // Void is not represented as a TypeNode but could be a potential "return
    // value" (or lack thereof) for a function. If this is null but the object
    // is otherwise valid, then the function "returns Void".
    private final TypeNode maybeReturnType;

    private final FBody functionBody;

    private FunctionDefNode(IDNode name, FunctionDefParamNode params,
            TypeNode maybeReturnType, FBody functionBody) {
        this.name = name;
        this.params = params;
        this.maybeReturnType = maybeReturnType; // Note: null is a valid value
        this.functionBody = functionBody;
    }

    public static FunctionDefNode parseFunctionDefNode(TokenDeque tokens) throws NodeParseException {
        // Check that we start with a Def.
        tokens.validateFirst(TokenType.ID_KEYWORD);
        Token maybeDef = tokens.removeFirst();
        if (maybeDef.getToken() != "Def") {
            throw new JottTree.NodeParseException(maybeDef, "Def");
        }

        // Check that we've got an ID (name).
        IDNode name = IDNode.parseIDNode(tokens);


        // Get parameters.

        tokens.validateFirst(TokenType.L_BRACKET);
        tokens.removeFirst();

        FunctionDefParamNode params =
            FunctionDefParamNode.parseFunctionDefParamNode(tokens);

        tokens.validateFirst(TokenType.R_BRACKET);
        tokens.removeFirst();


        // Get return type.

        tokens.validateFirst(TokenType.COLON);
        tokens.removeFirst();

        // Special case check for "Void" followed by TypeNode check.
        TypeNode returnType = null; // <- special case for "Void"
        Token maybeVoid = tokens.getFirst();
        if ( ! (maybeVoid.getTokenType() == TokenType.ID_KEYWORD
                && maybeVoid.getToken() == "Void") ) {
            returnType = TypeNode.parseTypeNode(tokens);
        }


        // Get function body.

        tokens.validateFirst(TokenType.L_BRACE);
        tokens.removeFirst();

        FBody functionBody = FBody.parseFBodyNode(tokens);

        tokens.validateFirst(TokenType.R_BRACE);
        tokens.removeFirst();

        return new FunctionDefNode(name, params, returnType, functionBody);
    }

    @Override
    public String convertToJott() {
        return "Def " + this.name.convertToJott() + "["
            + this.params.convertToJott() + "]: "
            + (this.maybeReturnType == null
                    ? "Void" : this.maybeReturnType.convertToJott())
            + "{" + this.functionBody.convertToJott() + "}";
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
