package provided.nodes;

import provided.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

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

        public static ElseNode parseElseNode(TokenDeque tokens, VariableTable variableTable, String functionName) throws NodeParseException {
            // Check that we start with a Else.
            tokens.validateFirst(TokenType.ID_KEYWORD);
            Token maybeElseif = tokens.removeFirst();
            if (!maybeElseif.getToken().equals("Else")) {
                throw new JottTree.NodeParseException(maybeElseif, "Else");
            }


            // Get the body.

            tokens.validateFirst(TokenType.L_BRACE);
            tokens.removeFirst();

            BodyNode body = BodyNode.parseBodyNode(tokens, variableTable, functionName);

            tokens.validateFirst(TokenType.R_BRACE);
            tokens.removeFirst();

            return new ElseNode(body);
        }

        public boolean hasReturn() {
            return this.body.hasReturn();
        }

        @Override
        public String convertToJott() {
            return "Else {" + this.body.convertToJott() + "}";
        }

        @Override
        public String convertToJava(String className) {
            return "else{" + this.body.convertToJava(className) + "}";
        }

        @Override
        public String convertToC() { 
            return "else{" + this.body.convertToC() + "}";
        }

        @Override
        public String convertToPython() {
            return "else:\n" + this.body.convertToPython();
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

        public static ElseIfNode parseElseIfNode (TokenDeque tokens, VariableTable variableTable, String functionName) throws NodeParseException {
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

            BodyNode body = BodyNode.parseBodyNode(tokens, variableTable, functionName);

            tokens.validateFirst(TokenType.R_BRACE);
            tokens.removeFirst();

            return new ElseIfNode(expr, body);
        }

        public boolean hasReturn() {
            return this.body.hasReturn();
        }

        @Override
        public String convertToJott() {
            return "Elseif [" + this.expr.convertToJott() + "] {"
                + this.body.convertToJott() + "}";
        }

        @Override
        public String convertToJava(String className) {
            return "else if(" + this.expr.convertToJava(className) + "){"
                + this.body.convertToJava(className) + "}";
        }

        @Override
        public String convertToC() {
            return "else if(" + this.expr.convertToC() + "){"
                + this.body.convertToC() + "}";
        }

        @Override
        public String convertToPython() {
            return "elif " + this.expr.convertToPython() + ":\n" + this.body.convertToPython();
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
    private boolean returns;

    private final String filename;
    private final int startLine;

    private IfStmtNode(ExprNode expr, BodyNode body,
            ArrayList<ElseIfNode> elseIfs,  ElseNode else_,
            String filename, int startLine) {
        this.expr = expr;
        this.body = body;
        this.elseIfs = elseIfs;
        this.else_ = else_;
        this.returns = false;
        this.filename = filename;
        this.startLine = startLine;
    }

    public static IfStmtNode parseIfStmtNode(TokenDeque tokens, VariableTable variableTable, String functionName) throws NodeParseException {
        // Check that we start with a If.
        tokens.validateFirst(TokenType.ID_KEYWORD);
        Token maybeDef = tokens.removeFirst();
        if (!maybeDef.getToken().equals("If")) {
            throw new JottTree.NodeParseException(maybeDef, "If");
        }

        String filename = tokens.getLastRemoved().getFilename();
        int lineNum = tokens.getLastRemoved().getLineNum();
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

        BodyNode body = BodyNode.parseBodyNode(tokens, variableTable, functionName);

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

            elseIfs.add(ElseIfNode.parseElseIfNode(tokens, variableTable, functionName));
        }


        // Get potential else.
        ElseNode else_ = null;
        Token maybeElse = tokens.getFirst();
        if (maybeElse.getTokenType() == TokenType.ID_KEYWORD
                && maybeElse.getToken().equals("Else")) {
            else_ = ElseNode.parseElseNode(tokens, variableTable, functionName);
        }

        return new IfStmtNode(expr, body, elseIfs, else_, filename, lineNum);
    }

    /**
     * Returns whether or not this if statement has return paths.
     * NOTE: this value will NOT necessarily be correct if it is called before
     * this node has been validated.
     */
    public boolean returns() {
        return this.returns;
    }

    @Override
    public String convertToJott() {
        StringBuilder sb = new StringBuilder("If [");
        sb.append(this.expr.convertToJott());
        sb.append("]{");
        sb.append("\n");
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
        StringBuilder sb = new StringBuilder("if(");
        sb.append(this.expr.convertToJava(className));
        sb.append("){");
        sb.append("\n");
        sb.append(this.body.convertToJava(className));
        sb.append("        }");
        sb.append("\n");
        for (ElseIfNode ei : this.elseIfs) {
            sb.append(" ");
            sb.append(ei.convertToJava(className));
        }
        if (this.else_ != null) {
            sb.append(" ");
            sb.append(this.else_.convertToJava(className));
        }

        return sb.toString();
    }

    @Override
    public String convertToC() {
        StringBuilder sb = new StringBuilder("if(");
        sb.append(this.expr.convertToC());
        sb.append("){");
        sb.append("\n");
        sb.append(this.body.convertToC());
        sb.append("        }");
        sb.append("\n");
        for (ElseIfNode ei : this.elseIfs) {
            sb.append(" ");
            sb.append(ei.convertToC());
        }
        if (this.else_ != null) {
            sb.append(" ");
            sb.append(this.else_.convertToC());
        }

        return sb.toString();
    }

    @Override
    public String convertToPython() {
        String result = "if " + this.expr.convertToPython() + ":\n" + this.body.convertToPython();
        if (elseIfs != null) {
            result += elseIfs.stream().map(elseIfNode
                    -> elseIfNode.convertToPython()).collect(Collectors.joining("\n"));
        }
        if (else_ != null) {
            result += else_.convertToPython();
        }
        return result;
    }

    @Override
    public void validateTree() throws NodeValidateException {
        // This validates the bodies of the constituent parts of the if
        // statement. Then, it checks that the if statement follows the return
        // path rule in the grammar.
        //
        // An if statement can only return if *all* of the branches
        // (if/elif/else) return.
        //
        // So after we check the bodies, we check the presence (or lack
        // thereof) of return statements based on that rule.

        body.validateTree();
        for (ElseIfNode ein : elseIfs) {
            ein.validateTree();
        }
        if (else_ != null) {
            else_.validateTree();
        }

        // Now we check the returns.
        if (else_ != null) {
            if (body.hasReturn()) {
                this.returns = true;
            }
            if (else_.hasReturn() != this.returns) {
                throw new NodeValidateException("All branches of an if-statement must return, or all not return.",
                        filename, startLine);
            }
            for (ElseIfNode ein : elseIfs) {
                if (ein.hasReturn() != this.returns) {
                    throw new NodeValidateException("All branches of an if-statement must return, or all not return.",
                            filename, startLine);
                }
            }
        } else {
            if (body.hasReturn()) {
                this.returns = true;
            }
            for (ElseIfNode ein : elseIfs) {
                if (ein.hasReturn() != this.returns) {
                    throw new NodeValidateException("All branches of an if-statement must return, or all not return.",
                            filename, startLine);
                }
            }
        }

    }
}
