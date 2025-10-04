package com.parser;

import com.lexer.Token;
import com.parser.parselets.*;
import com.parser.statement.ReturnStatement;
import com.parser.statementsParselets.*;

import java.util.HashMap;

public class Grammer {
    
    //lookup maps for the parser
    public static final HashMap<Token.TokenType, StatementsParselets> mapStatementPars = new HashMap<>();
    public static final HashMap<Token.TokenType, LedParselets> mapInfix = new HashMap<>();
    public static final HashMap<Token.TokenType, NudParselets> mapPrefix = new HashMap<>();
    public static final HashMap<Token.TokenType, LedParselets.Precedence> mapBp = new HashMap<>(); // bp = binding power

    //implnetion of parselet expression functions
    private static final OperatorParselet OPERTOR = new OperatorParselet();
    private static final AssignmentParselet ASSIGNMENT = new AssignmentParselet();
    private static final ConditionalParselet COND = new ConditionalParselet();
    private static final PostfixParselet POSTFIX = new PostfixParselet();
    private static final PrefixParselet PREFIX = new PrefixParselet();
    private static final GroupParselet GROUP = new GroupParselet();
    private static final PrimaryParselet PRIMARY = new PrimaryParselet();
    private static final CallParselet CALL = new CallParselet();
    private static final MemberAccessParselet ACCESS = new MemberAccessParselet();
    private static final EqualityParselet EQ = new EqualityParselet();

    //implnetion of parselet Statements functions
    private static final ReturnParselet RETURN = new ReturnParselet();
    private static final IfParselet IF = new IfParselet();
    private static final BlockParselets BLOCK = new BlockParselets();
    private static final WhileParselet WHILE = new WhileParselet();
    private static final DoWhileParselet DOWHILE = new DoWhileParselet();
    private static final ForParselet FOR=  new ForParselet();

    static {
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

        // ----- mapStatementPars mapping -----
        mapStatementPars.put(Token.TokenType.KW_RETURN, RETURN);
        mapStatementPars.put(Token.TokenType.KW_IF, IF);
        mapStatementPars.put(Token.TokenType.LBRACE, BLOCK);
        mapStatementPars.put(Token.TokenType.KW_WHILE, WHILE);
        mapStatementPars.put(Token.TokenType.KW_DO, DOWHILE);
        mapStatementPars.put(Token.TokenType.KW_FOR, FOR);

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
        mapBp.put(Token.TokenType.LBRACE, LedParselets.Precedence.START);
    }
}
