package provided.nodes;

import provided.JottTree;
import provided.TokenDeque;
import provided.TokenType;
import provided.VariableTable;

public class VarDecNode implements JottTree {

    private final TypeNode typeNode;
    private final IDNode idNode;
    private final VariableTable variableTable;

    private VarDecNode(TypeNode typeNode, IDNode idNode, VariableTable variableTable) {
        this.typeNode = typeNode;
        this.idNode = idNode;
        this.variableTable = variableTable;
    }

    /**
     * Parses a var_dec node from the given tokens
     *
     * @param tokens the tokens to parse
     * @param variableTable the variable table to use in validation
     * @return the parsed var_dec node
     * @throws NodeParseException if the tokens do not form a valid function call node
     */
    public static VarDecNode parseVarDecNode(TokenDeque tokens, VariableTable variableTable) throws NodeParseException {
        TypeNode typeNode = TypeNode.parseTypeNode(tokens);
        IDNode idNode = IDNode.parseIDNode(tokens);
        tokens.validateFirst(TokenType.SEMICOLON);
        tokens.removeFirst(); // Remove semicolon
        return new VarDecNode(typeNode, idNode, variableTable);
    }

    @Override
    public String convertToJott() {
        return typeNode.convertToJott() + " " + idNode.convertToJott() + ";";
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
        typeNode.validateTree();
        idNode.validateTree();
        if (!variableTable.tryDeclareVariable(idNode.getIdStringValue(), typeNode.getType()))
            throw new NodeValidateException("Variable " + idNode.getIdStringValue() + " already declared.", typeNode.getFilename(), typeNode.getStartLine());
    }
}
