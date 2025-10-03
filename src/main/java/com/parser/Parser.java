package com.parser;

import com.lexer.Lexer;
import com.lexer.Token;
import com.parser.expression.Expression;
import com.parser.parselets.*;
import com.parser.statement.*;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Parser {

    public final TokenCurser cur;
    private final HashMap<Token.TokenType, LedParselets> mapInfix = new HashMap<>();
    private final HashMap<Token.TokenType, NudParselets> mapPrefix = new HashMap<>();
    public final HashMap<Token.TokenType, LedParselets.Precedence> mapBp = new HashMap<>(); // bp = binding power


    public Parser(TokenCurser cur){
        this.cur = cur;
        populateMaps();
    }

    public boolean endExpression(){
        return cur.isEOF() || cur.match(Token.TokenType.SEMICOLON);
    }

    public Expression parseExpression(LedParselets.Precedence prec){
        Token t = cur.get();

        NudParselets PrefixFunc = mapPrefix.get(t.type);
        if (PrefixFunc == null){
            throw new ParseException("Expression doesn't start with a prefix token", t);
        }
        Expression left = PrefixFunc.parse(this, t);

        while(prec.bp < mapBp.get(cur.peek().type).bp) {
            Token Inft = cur.get();
            LedParselets InfixFunc = mapInfix.get(Inft.type);
            if (InfixFunc == null) {
                throw new ParseException("Expression doesn't continue with a Inifix token", Inft);
            }
            left = InfixFunc.parse(this, Inft, left);
        }
        return left;
    }

    public Statement parseStatement(){
        return new ReturnStatement(cur.peek().loc, parseExpression(LedParselets.Precedence.START));
    }

    public ReturnStatement parseReturnStatement(){
        cur.consume(Token.TokenType.KW_RETURN);
        Expression value = parseExpression(LedParselets.Precedence.START);
        return new ReturnStatement(value.loc, value);
    }

    public IfStatement parseIfStatement(){
        cur.consume(Token.TokenType.KW_IF); //if statement starts with if keyword

        Expression condition = parseExpression(LedParselets.Precedence.START);
        Statement then = parseBlockStatement();
        if(cur.peek().type == Token.TokenType.KW_ELSE){
            cur.consume(Token.TokenType.KW_ELSE);
            Statement _else = parseBlockStatement();
            return new IfStatement(then.loc, condition, then, _else);
        }
        return new IfStatement(then.loc, condition, then);
    }

    public BlockStatement parseBlockStatement(){
        cur.consume(Token.TokenType.LBRACE); //block statement starts with a {

        List<Statement> stmts = new ArrayList<>();
        while (cur.peek().type != Token.TokenType.RBRACE){ //block statement ends with a }
            stmts.add(parseStatement());
        }
        cur.consume(Token.TokenType.RBRACE);

        return new BlockStatement(stmts.get(0).loc, stmts);
    }

    public WhileStatement whileStatement(){
        cur.consume(Token.TokenType.KW_WHILE); //while statement starts with a while

        Expression condition = parseExpression(LedParselets.Precedence.START); //while condition
        Statement stmt = parseStatement();

        return new WhileStatement(condition.loc, stmt, condition);
    }


    public DoWhileStatement doWhileStatement(){
        cur.consume(Token.TokenType.KW_DO); //DOwhile statement starts with a DO

        Statement stmt = parseStatement();
        cur.consume(Token.TokenType.KW_WHILE); // MUST HAVE WHILE KEYWORD AT THE END;
        Expression condition = parseExpression(LedParselets.Precedence.START);

        return new DoWhileStatement(stmt.loc, stmt, condition);
    }


    public ForStatement forStatement(){
        cur.consume(Token.TokenType.KW_FOR); //for statement starts with a for

        Statement init = parseStatement();
        Expression condition = parseExpression(LedParselets.Precedence.START); //while condition
        Expression update = parseExpression(LedParselets.Precedence.START);
        Statement stmt = parseStatement();

        return new ForStatement(init.loc, stmt, condition, update, init);
    }

    private final OperatorParselet OPERTOR = new OperatorParselet();
    private final AssignmentParselet ASSIGNMENT = new AssignmentParselet();
    private final ConditionalParselet COND = new ConditionalParselet();
    private final PostfixParselet POSTFIX = new PostfixParselet();
    private final PrefixParselet PREFIX = new PrefixParselet();
    private final GroupParselet GROUP = new GroupParselet();
    private final PrimaryParselet PRIMARY = new PrimaryParselet();
    private final CallParselet CALL = new CallParselet();
    private final MemberAccessParselet ACCESS = new MemberAccessParselet();
    private final EqualityParselet EQ = new EqualityParselet();

    private void populateMaps(){
        // ----- Literals & identifiers -----
        mapPrefix.put(Token.TokenType.IDENTIFIER, PRIMARY);
        mapPrefix.put(Token.TokenType.INT_LITERAL, PRIMARY);
        mapPrefix.put(Token.TokenType.FLOAT_LITERAL, PRIMARY);
        mapPrefix.put(Token.TokenType.CHAR_LITERAL, PRIMARY);
        mapPrefix.put(Token.TokenType.STRING_LITERAL, PRIMARY);
        mapPrefix.put(Token.TokenType.TRUE_LITERAL, PRIMARY);
        mapPrefix.put(Token.TokenType.FALSE_LITERAL, PRIMARY);
        mapPrefix.put(Token.TokenType.NULL_LITERAL, PRIMARY);

        // ----- Prefix operators -----
        mapPrefix.put(Token.TokenType.OP_MINUS, PREFIX);
        mapPrefix.put(Token.TokenType.OP_NOT, PREFIX);
        mapPrefix.put(Token.TokenType.OP_INC, PREFIX);
        mapPrefix.put(Token.TokenType.OP_DEC, PREFIX);
        mapPrefix.put(Token.TokenType.OP_BIT_NOT, PREFIX);

        // ----- Grouping -----
        mapPrefix.put(Token.TokenType.LPAREN, GROUP);

        // ----- Infix binary operators -----
        mapInfix.put(Token.TokenType.OP_PLUS, OPERTOR);
        mapInfix.put(Token.TokenType.OP_MINUS, OPERTOR);
        mapInfix.put(Token.TokenType.OP_MUL, OPERTOR);
        mapInfix.put(Token.TokenType.OP_DIV, OPERTOR);
        mapInfix.put(Token.TokenType.OP_MOD, OPERTOR);

        mapInfix.put(Token.TokenType.OP_EQ, EQ);
        mapInfix.put(Token.TokenType.OP_NEQ, EQ);
        mapInfix.put(Token.TokenType.OP_LT, EQ);
        mapInfix.put(Token.TokenType.OP_GT, EQ);
        mapInfix.put(Token.TokenType.OP_LTE, EQ);
        mapInfix.put(Token.TokenType.OP_GTE, EQ);

        mapInfix.put(Token.TokenType.OP_AND, OPERTOR);
        mapInfix.put(Token.TokenType.OP_OR, OPERTOR);
        mapInfix.put(Token.TokenType.OP_BIT_AND, OPERTOR);
        mapInfix.put(Token.TokenType.OP_BIT_OR, OPERTOR);
        mapInfix.put(Token.TokenType.OP_BIT_XOR, OPERTOR);
        mapInfix.put(Token.TokenType.OP_SHL, OPERTOR);
        mapInfix.put(Token.TokenType.OP_SHR, OPERTOR);
        mapInfix.put(Token.TokenType.OP_USHR, OPERTOR);

        // ----- Assignment operators (usually right-associative) -----
        mapInfix.put(Token.TokenType.OP_ASSIGN, ASSIGNMENT);
        mapInfix.put(Token.TokenType.OP_ADD_ASSIGN, ASSIGNMENT);
        mapInfix.put(Token.TokenType.OP_SUB_ASSIGN, ASSIGNMENT);
        mapInfix.put(Token.TokenType.OP_MUL_ASSIGN, ASSIGNMENT);
        mapInfix.put(Token.TokenType.OP_DIV_ASSIGN, ASSIGNMENT);
        mapInfix.put(Token.TokenType.OP_MOD_ASSIGN, ASSIGNMENT);
        mapInfix.put(Token.TokenType.OP_SHL_ASSIGN, ASSIGNMENT);
        mapInfix.put(Token.TokenType.OP_SHR_ASSIGN, ASSIGNMENT);
        mapInfix.put(Token.TokenType.OP_USHR_ASSIGN, ASSIGNMENT);
        mapInfix.put(Token.TokenType.OP_AND_ASSIGN, ASSIGNMENT);
        mapInfix.put(Token.TokenType.OP_OR_ASSIGN, ASSIGNMENT);
        mapInfix.put(Token.TokenType.OP_XOR_ASSIGN, ASSIGNMENT);

        // ----- Postfix operators -----
        mapInfix.put(Token.TokenType.OP_INC, POSTFIX);
        mapInfix.put(Token.TokenType.OP_DEC, POSTFIX);

        // ----- Function call / member access -----
        mapInfix.put(Token.TokenType.LPAREN, CALL); // f(x)
        mapInfix.put(Token.TokenType.DOT, ACCESS);

        // ----- Ternary operator -----
        mapInfix.put(Token.TokenType.QUESTION, COND);

        // ----- LedParselets.Precedence mapping -----
        // Example: map every operator to its binding power
        mapBp.put(Token.TokenType.OP_PLUS, LedParselets.Precedence.SUM);
        mapBp.put(Token.TokenType.OP_MINUS, LedParselets.Precedence.SUM);
        mapBp.put(Token.TokenType.OP_MUL, LedParselets.Precedence.PRODUCT);
        mapBp.put(Token.TokenType.OP_DIV, LedParselets.Precedence.PRODUCT);
        mapBp.put(Token.TokenType.OP_MOD, LedParselets.Precedence.PRODUCT);
        mapBp.put(Token.TokenType.OP_EQ, LedParselets.Precedence.EQUALITY);
        mapBp.put(Token.TokenType.OP_NEQ, LedParselets.Precedence.EQUALITY);
        mapBp.put(Token.TokenType.OP_LT, LedParselets.Precedence.EQUALITY);
        mapBp.put(Token.TokenType.OP_GT, LedParselets.Precedence.EQUALITY);
        mapBp.put(Token.TokenType.OP_LTE, LedParselets.Precedence.EQUALITY);
        mapBp.put(Token.TokenType.OP_GTE, LedParselets.Precedence.EQUALITY);
        mapBp.put(Token.TokenType.OP_AND, LedParselets.Precedence.LOGICAL);
        mapBp.put(Token.TokenType.OP_OR, LedParselets.Precedence.LOGICAL);
        mapBp.put(Token.TokenType.OP_BIT_AND, LedParselets.Precedence.BITWISE);
        mapBp.put(Token.TokenType.OP_BIT_OR, LedParselets.Precedence.BITWISE);
        mapBp.put(Token.TokenType.OP_BIT_XOR, LedParselets.Precedence.BITWISE);
        mapBp.put(Token.TokenType.OP_SHL, LedParselets.Precedence.SHIFT);
        mapBp.put(Token.TokenType.OP_SHR, LedParselets.Precedence.SHIFT);
        mapBp.put(Token.TokenType.OP_USHR, LedParselets.Precedence.SHIFT);
        mapBp.put(Token.TokenType.OP_ASSIGN, LedParselets.Precedence.ASSIGNMENT);
        mapBp.put(Token.TokenType.OP_ADD_ASSIGN, LedParselets.Precedence.ASSIGNMENT);
        mapBp.put(Token.TokenType.OP_SUB_ASSIGN, LedParselets.Precedence.ASSIGNMENT);
        mapBp.put(Token.TokenType.OP_MUL_ASSIGN, LedParselets.Precedence.ASSIGNMENT);
        mapBp.put(Token.TokenType.OP_DIV_ASSIGN, LedParselets.Precedence.ASSIGNMENT);
        mapBp.put(Token.TokenType.OP_MOD_ASSIGN, LedParselets.Precedence.ASSIGNMENT);
        mapBp.put(Token.TokenType.OP_SHL_ASSIGN, LedParselets.Precedence.ASSIGNMENT);
        mapBp.put(Token.TokenType.OP_SHR_ASSIGN, LedParselets.Precedence.ASSIGNMENT);
        mapBp.put(Token.TokenType.OP_USHR_ASSIGN, LedParselets.Precedence.ASSIGNMENT);
        mapBp.put(Token.TokenType.OP_AND_ASSIGN, LedParselets.Precedence.ASSIGNMENT);
        mapBp.put(Token.TokenType.OP_OR_ASSIGN, LedParselets.Precedence.ASSIGNMENT);
        mapBp.put(Token.TokenType.OP_XOR_ASSIGN, LedParselets.Precedence.ASSIGNMENT);
        mapBp.put(Token.TokenType.OP_INC, LedParselets.Precedence.POSTFIX);
        mapBp.put(Token.TokenType.OP_DEC, LedParselets.Precedence.POSTFIX);
        mapBp.put(Token.TokenType.LPAREN, LedParselets.Precedence.CALL);
        mapBp.put(Token.TokenType.DOT, LedParselets.Precedence.CALL);
        mapBp.put(Token.TokenType.QUESTION, LedParselets.Precedence.CONDITIONAL);
        mapBp.put(Token.TokenType.EOF, LedParselets.Precedence.EOF);

        //DONT HAVE PREC BUT STILL NEED TO RETURN A NUMBER FROM THE MAP
        mapBp.put(Token.TokenType.RPAREN, LedParselets.Precedence.START);
        mapBp.put(Token.TokenType.COLON, LedParselets.Precedence.START);
        mapBp.put(Token.TokenType.SEMICOLON, LedParselets.Precedence.START);
    }

    public static void main(String[] args) throws IOException {
        Lexer lex = new Lexer("C:\\projJava\\javacompiler\\src\\test\\java\\parser\\javaExpressionTest.txt");
        lex.analyze();
        System.out.println(lex.toString());
        Parser par = new Parser(new TokenCurser(lex.tokens));

        while(par.cur.peek().type != Token.TokenType.EOF){
            System.out.println(par.parseExpression(LedParselets.Precedence.START));
            par.cur.consume(Token.TokenType.SEMICOLON);
        }
    }
    
}
