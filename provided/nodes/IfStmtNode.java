package provided.nodes;

import provided.*;

import java.util.ArrayList;

/**
 * Class for if statements.
 *
 * @author Sebastian LaVine <sml1040@rit.edu>
 */
public class IfStmtNode implements BodyStmtNode {

    public static class ElseNode implements JottTree {
        private final BodyNode body;

        private ElseNode(BodyNode body) {
            this.body = body;
        }

        public static ElseNode parseElseNode(TokenDeque tokens, VariableTable variableTable, String functionName, SymbolTable symbolTable) throws NodeParseException {
            // Check that we start with a Else.
            tokens.validateFirst(TokenType.ID_KEYWORD);
            Token maybeElseif = tokens.removeFirst();
            if (!maybeElseif.getToken().equals("Else")) {
                throw new JottTree.NodeParseException(maybeElseif, "Else");
            }


            // Get the body.

            tokens.validateFirst(TokenType.L_BRACE);
            tokens.removeFirst();

            BodyNode body = BodyNode.parseBodyNode(tokens, variableTable, functionName, symbolTable);

            tokens.validateFirst(TokenType.R_BRACE);
            tokens.removeFirst();

            return new ElseNode(body);
        }

        @Override
        public String convertToJott() {
            return "Else {" + this.body.convertToJott() + "}";
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
            body.validateTree();
        }
    }

    public static class ElseIfNode implements JottTree {
        private final ExprNode expr;
        private final BodyNode body;

        private ElseIfNode(ExprNode expr, BodyNode body) {
            this.expr = expr;
            this.body = body;
        }

        public static ElseIfNode parseElseIfNode (TokenDeque tokens, VariableTable variableTable, String functionName, SymbolTable symbolTable) throws NodeParseException {
            // Check that we start with a Elseif.
            tokens.validateFirst(TokenType.ID_KEYWORD);
            Token maybeElseif = tokens.removeFirst();
            if (!maybeElseif.getToken().equals("Elseif")) {
                throw new JottTree.NodeParseException(maybeElseif, "Elseif");
            }


            // Get the condition expression

            tokens.validateFirst(TokenType.L_BRACKET);
            tokens.removeFirst();

            ExprNode expr = ExprNode.parseExprNode(tokens, variableTable);

            tokens.validateFirst(TokenType.R_BRACKET);
            tokens.removeFirst();


            // Get the body.

            tokens.validateFirst(TokenType.L_BRACE);
            tokens.removeFirst();

            BodyNode body = BodyNode.parseBodyNode(tokens, variableTable, functionName, symbolTable);

            tokens.validateFirst(TokenType.R_BRACE);
            tokens.removeFirst();

            return new ElseIfNode(expr, body);
        }

        @Override
        public String convertToJott() {
            return "Elseif [" + this.expr.convertToJott() + "] {"
                + this.body.convertToJott() + "}";
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
            body.validateTree();
        }
    }

    private final ExprNode expr;
    private final BodyNode body;
    private final ArrayList<ElseIfNode> elseIfs;
    private final ElseNode else_;

    private IfStmtNode(ExprNode expr, BodyNode body, ArrayList<ElseIfNode> elseIfs,  ElseNode else_) {
        this.expr = expr;
        this.body = body;
        this.elseIfs = elseIfs;
        this.else_ = else_;
    }

    public static IfStmtNode parseIfStmtNode(TokenDeque tokens, VariableTable variableTable, String functionName, SymbolTable symbolTable) throws NodeParseException {
        // Check that we start with a If.
        tokens.validateFirst(TokenType.ID_KEYWORD);
        Token maybeDef = tokens.removeFirst();
        if (!maybeDef.getToken().equals("If")) {
            throw new JottTree.NodeParseException(maybeDef, "If");
        }


        // Get the condition expression.

        tokens.validateFirst(TokenType.L_BRACKET);
        tokens.removeFirst();

        ExprNode expr = ExprNode.parseExprNode(tokens, variableTable);
        //tokens.removeFirst();
        
        tokens.validateFirst(TokenType.R_BRACKET);
        tokens.removeFirst();


        // Get the body.

        tokens.validateFirst(TokenType.L_BRACE);
        tokens.removeFirst();

        BodyNode body = BodyNode.parseBodyNode(tokens, variableTable, functionName, symbolTable);

        tokens.validateFirst(TokenType.R_BRACE);
        tokens.removeFirst();


        // Get potential else-ifs.
        ArrayList<ElseIfNode> elseIfs = new ArrayList<>();
        while (!tokens.isEmpty()) {
            Token next = tokens.getFirst();
            if (next.getTokenType() != TokenType.ID_KEYWORD
                    || !next.getToken().equals("Elseif")) {
                break;
            }

            elseIfs.add(ElseIfNode.parseElseIfNode(tokens, variableTable, functionName, symbolTable));
        }


        // Get potential else.
        ElseNode else_ = null;
        Token maybeElse = tokens.getFirst();
        if (maybeElse.getTokenType() == TokenType.ID_KEYWORD
                && maybeElse.getToken().equals("Else")) {
            else_ = ElseNode.parseElseNode(tokens, variableTable, functionName, symbolTable);
        }

        return new IfStmtNode(expr, body, elseIfs, else_);
    }

    @Override
    public String convertToJott() {
        StringBuilder sb = new StringBuilder("If [");
        sb.append(this.expr.convertToJott());
        sb.append("]{");
        sb.append("\n");
        sb.append("");
        sb.append(this.body.convertToJott());
        sb.append("        }");
        sb.append("\n");
        for (ElseIfNode ei : this.elseIfs) {
            sb.append(" ");
            sb.append(ei.convertToJott());
        }
        if (this.else_ != null) {
            sb.append(" ");
            sb.append(this.else_.convertToJott());
        }

        return sb.toString();
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
        body.validateTree();
    }
}
