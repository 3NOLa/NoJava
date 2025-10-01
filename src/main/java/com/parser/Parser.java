package com.parser;

import com.lexer.Lexer;
import com.lexer.Token;
import com.parser.expression.EqualityExpression;
import com.parser.expression.Expression;
import com.parser.parselets.*;

import java.io.IOException;
import java.util.HashMap;

public class Parser {

    public final TokenCurser cur;
    private final HashMap<Token.TokenType, LedParselets> mapInifix = new HashMap<>();
    private final HashMap<Token.TokenType, NudParselets> mapPrefix = new HashMap<>();
    public final HashMap<Token.TokenType, LedParselets.Precedence> mapBp = new HashMap<>(); // bp = binding power


    public Parser(TokenCurser cur){
        this.cur = cur;
        populateMaps();
    }

    public boolean endExpression(){
        if(cur.isEOF() || cur.match(Token.TokenType.SEMICOLON))
            return true;
        return false;
    }

    public Expression parseExpression(LedParselets.Precedence prec){
        Token t = cur.get();

        NudParselets PrefixFunc = mapPrefix.get(t.type);
        if (PrefixFunc == null){
            throw new ParseException("Expression doesn't start with a prefix token", t);
        }
        Expression left = PrefixFunc.parse(this, t);

        //System.out.println(cur.peek().toString());

        while(!endExpression() && prec.bp < mapBp.get(cur.peek().type).bp) {
            Token Inft = cur.get();
            LedParselets InfixFunc = mapInifix.get(Inft.type);
            if (InfixFunc == null) {
                throw new ParseException("Expression doesn't continue with a Inifix token", Inft);
            }
            left = InfixFunc.parse(this, Inft, left);
        }
        return left;
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
        mapInifix.put(Token.TokenType.OP_PLUS, OPERTOR);
        mapInifix.put(Token.TokenType.OP_MINUS, OPERTOR);
        mapInifix.put(Token.TokenType.OP_MUL, OPERTOR);
        mapInifix.put(Token.TokenType.OP_DIV, OPERTOR);
        mapInifix.put(Token.TokenType.OP_MOD, OPERTOR);

        mapInifix.put(Token.TokenType.OP_EQ, EQ);
        mapInifix.put(Token.TokenType.OP_NEQ, EQ);
        mapInifix.put(Token.TokenType.OP_LT, EQ);
        mapInifix.put(Token.TokenType.OP_GT, EQ);
        mapInifix.put(Token.TokenType.OP_LTE, EQ);
        mapInifix.put(Token.TokenType.OP_GTE, EQ);

        mapInifix.put(Token.TokenType.OP_AND, OPERTOR);
        mapInifix.put(Token.TokenType.OP_OR, OPERTOR);
        mapInifix.put(Token.TokenType.OP_BIT_AND, OPERTOR);
        mapInifix.put(Token.TokenType.OP_BIT_OR, OPERTOR);
        mapInifix.put(Token.TokenType.OP_BIT_XOR, OPERTOR);
        mapInifix.put(Token.TokenType.OP_SHL, OPERTOR);
        mapInifix.put(Token.TokenType.OP_SHR, OPERTOR);
        mapInifix.put(Token.TokenType.OP_USHR, OPERTOR);

        // ----- Assignment operators (usually right-associative) -----
        mapInifix.put(Token.TokenType.OP_ASSIGN, ASSIGNMENT);
        mapInifix.put(Token.TokenType.OP_ADD_ASSIGN, ASSIGNMENT);
        mapInifix.put(Token.TokenType.OP_SUB_ASSIGN, ASSIGNMENT);
        mapInifix.put(Token.TokenType.OP_MUL_ASSIGN, ASSIGNMENT);
        mapInifix.put(Token.TokenType.OP_DIV_ASSIGN, ASSIGNMENT);
        mapInifix.put(Token.TokenType.OP_MOD_ASSIGN, ASSIGNMENT);
        mapInifix.put(Token.TokenType.OP_SHL_ASSIGN, ASSIGNMENT);
        mapInifix.put(Token.TokenType.OP_SHR_ASSIGN, ASSIGNMENT);
        mapInifix.put(Token.TokenType.OP_USHR_ASSIGN, ASSIGNMENT);
        mapInifix.put(Token.TokenType.OP_AND_ASSIGN, ASSIGNMENT);
        mapInifix.put(Token.TokenType.OP_OR_ASSIGN, ASSIGNMENT);
        mapInifix.put(Token.TokenType.OP_XOR_ASSIGN, ASSIGNMENT);

        // ----- Postfix operators -----
        mapInifix.put(Token.TokenType.OP_INC, POSTFIX);
        mapInifix.put(Token.TokenType.OP_DEC, POSTFIX);

        // ----- Function call / member access -----
        mapInifix.put(Token.TokenType.LPAREN, CALL); // f(x)
        mapInifix.put(Token.TokenType.DOT, ACCESS);

        // ----- Ternary operator -----
        mapInifix.put(Token.TokenType.QUESTION, COND);

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
    }

    public static void main(String[] args) throws IOException {
        Lexer lex = new Lexer("C:\\projJava\\javacompiler\\src\\test\\java\\parser\\javaExpressionTest.txt");
        lex.analyze();
        System.out.println(lex.toString());
        Parser par = new Parser(new TokenCurser(lex.tokens));

        while(par.cur.peek().type != Token.TokenType.EOF){
            System.out.println(par.parseExpression(LedParselets.Precedence.START));
        }
    }
    
}
