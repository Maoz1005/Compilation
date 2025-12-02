import java_cup.runtime.*;

%%

%class Lexer
%line
%column
%cupsym TokenNames
%cup
   
%{
	private Symbol symbol(int type)               {return new Symbol(type, yyline, yycolumn);}
	private Symbol symbol(int type, Object value) {return new Symbol(type, yyline, yycolumn, value);}
	public int getLine()    { return yyline + 1; } 
	public int getCharPos() { return yycolumn;   } 
%}

/***********************/
/* MACRO DECALARATIONS */
/***********************/
LineTerminator	= \r|\n|\r\n
WhiteSpace		= {LineTerminator} | [ \t]
LEADING_ZERO 	= 0[0-9]+
INTEGER			= 0 | [1-9][0-9]*
LETTER			= [a-zA-Z]
ID				= {LETTER}([a-zA-Z0-9]*)
STRING			= \"{LETTER}*\" /* Strings that contain non-letter characters are lexical errors. */
BRACKET			= \(|\)|\[\]\{\} /*  ( or ) or [ or ] or { or }   */
MARK			= \?|\! /*  ? or !  */
OP				= \+|\-|\*|\/ /*  + or - or * or /  */
DOT				= \.|\; /*  . or ;  */
NO_STAR			= [0-9]|{LETTER}|{BRACKET}|{MARK}|{DOT}|{WhiteSpace}|\+|\-|\/
NO_SLASH		= [0-9]|{LETTER}|{BRACKET}|{MARK}|{DOT}|{WhiteSpace}|\+|\-|\*
T1_COMMENT 		= \/\/([a-zA-Z]|[0-9]|\(|\)|\[|\]|\{|\}|\?|\!|\+|\-|\*|\/|\.|\;|[ \t])*{LineTerminator} 
T2_COMMENT		= \/\* ( ({NO_STAR})* (\*{NO_SLASH})* )* (\*)? \*\/ /* NoS: (\*)? in case of single asterisk followed by no chars (Our TA is a genius.) */
SKIP  			= {WhiteSpace} | {T1_COMMENT} | {T2_COMMENT}
T1_EOF			= \/\/([a-zA-Z]|[0-9]|\(|\)|\[|\]|\{|\}|\?|\!|\+|\-|\*|\/|\.|\;|[ \t])* 
T2_EOF			= \/\* ( ({NO_STAR})* (\*{NO_SLASH})* )* (\*)? 
ERROR_COMMENT 	= {T1_EOF} | {T2_EOF} 

%%

<YYINITIAL> {
	"("                 { return symbol(TokenNames.LPAREN); }              // LPAREN: "("
	")"                 { return symbol(TokenNames.RPAREN); }              // RPAREN: ")"
	"["                 { return symbol(TokenNames.LBRACK); }              // LBRACK: "["
	"]"                 { return symbol(TokenNames.RBRACK); }              // RBRACK: "]"
	"{"                 { return symbol(TokenNames.LBRACE); }              // LBRACE: "{"
	"}"                 { return symbol(TokenNames.RBRACE); }              // RBRACE: "}"
	"nil"               { return symbol(TokenNames.NIL); }                 // NIL: "nil"
	"+"                 { return symbol(TokenNames.PLUS); }                // PLUS: "+"
	"-"                 { return symbol(TokenNames.MINUS); }               // MINUS: "-"
	"*"                 { return symbol(TokenNames.TIMES); }               // TIMES: "*"
	"/"                 { return symbol(TokenNames.DIVIDE); }              // DIVIDE: "/"
	","                 { return symbol(TokenNames.COMMA); }               // COMMA: ","
	"."                 { return symbol(TokenNames.DOT); }                 // DOT: "."
	";"                 { return symbol(TokenNames.SEMICOLON); }           // SEMICOLON: ";"
	"int"               { return symbol(TokenNames.TYPE_INT); }            // TYPE_INT: "int"
	"string"			{ return symbol(TokenNames.TYPE_STRING); }         // TYPE_STRING: "string"
	"void"              { return symbol(TokenNames.TYPE_VOID); }           // TYPE_VOID: "void"
	":="                { return symbol(TokenNames.ASSIGN); }              // ASSIGN: ":="
	"="                 { return symbol(TokenNames.EQ); }                  // EQ: "="
	"<"                 { return symbol(TokenNames.LT); }                  // LT: "<"
	">"                 { return symbol(TokenNames.GT); }                  // GT: ">"
	"array"             { return symbol(TokenNames.ARRAY); }               // ARRAY: "array"
	"class"             { return symbol(TokenNames.CLASS); }               // CLASS: "class"
	"extends"           { return symbol(TokenNames.EXTENDS); }             // EXTENDS: "extends"
	"return"            { return symbol(TokenNames.RETURN); }              // RETURN: "return"
	"while"             { return symbol(TokenNames.WHILE); }               // WHILE: "while"
	"if"                { return symbol(TokenNames.IF); }                  // IF: "if"
	"new"               { return symbol(TokenNames.NEW); }                 // NEW: "new"
  	{LEADING_ZERO}      {return symbol(TokenNames.ERROR); }
	{INTEGER}			{ return symbol(TokenNames.INT, new Integer(yytext()));}
	{STRING}			{ return symbol(TokenNames.STRING, new String(yytext()));}
	{ID}				{ return symbol(TokenNames.ID,     new String(yytext()));}   
	{SKIP}		{ /* just skip what was found, do nothing */ }
  	{ERROR_COMMENT} 		{return symbol(TokenNames.ERROR); }
	<<EOF>>				{ return symbol(TokenNames.EOF);}
}
