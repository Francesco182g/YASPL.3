// Lexer
//@copy Francesco Garofalo 2018

package compilers;

import Lexer.SymbolTable;
import java_cup.runtime.*;
import java.lang.*;

/**
 * Analisi Lessicale
 * 
 * @author Francesco Garofalo
 *
 */


%%

/*-- Declarations for JFlex --*/
%class Lexer	
%unicode 								
%cupsym ParserSym 	
%public
%line
%column
%cup

%{
  StringBuilder string = new StringBuilder();
  
  	private Symbol symbol(int type)
	{
		return new Symbol(type);
	}

	private Symbol symbol(int type, String value)
	{
		Symbol toReturn = null;
		if(type == ParserSym.ID){
			toReturn = new Symbol(type, yyline, yycolumn, SymbolTable.addIdentifiers(value));
		}else{
			toReturn = new Symbol(type, value);
		}
		return toReturn;
	} 
  
%}

/*regular definition YASPL.3*/
			
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]

/* COMMENTS */
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}

TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
// Comment can be the last line of the file, without line terminator.
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}?
DocumentationComment = "/**" {CommentContent} "*"+ "/"
CommentContent       = ( [^*] | \*+ [^/*] )*


Identifier = [:jletter:] [:jletterdigit:]*

DigitInt = 0 | [1-9][0-9]*
DigitDouble = (0 | [1-9][0-9]*)\.[0-9]+
OctDigit = [0-7]

/* string and character literals */
//StringCharacter = [^\r\n\"\\]
SingleCharacter = [^\r\n\'\\]

%state STRING_CONST, CHAR_CONST 

%eofval{
    return symbolFactory.newSymbol("EOF",EOF);
%eofval}


%%

<YYINITIAL> {
  \'{SingleCharacter}?\' {		//System.out.println("Stampo il valore della variabile char di riferimento: " + yytext().substring(0,1));
  								return new Symbol(ParserSym.CHAR_CONST, yytext().substring(1,2));}
  \'{SingleCharacter}{SingleCharacter}+\'  { throw new RuntimeException("Lexical Error: Char dichiarato in maniera sbagliata"); }
	{Comment} { /*Ignore*/}
	{WhiteSpace} {/*Ignore*/}
	"head" {return new Symbol(ParserSym.HEAD);}
	"start" {return new Symbol(ParserSym.START);}
	";" 	{return new Symbol(ParserSym.SEMI);}
	"int"	{return new Symbol(ParserSym.INT);}
	"bool"  {return new Symbol(ParserSym.BOOL);}
	"double" {return new Symbol(ParserSym.DOUBLE);}
	"string" {return new Symbol(ParserSym.STRING);}
	"char"   {return new Symbol(ParserSym.CHAR);}
	","		{return new Symbol(ParserSym.COMMA);}
	"def"	{return new Symbol(ParserSym.DEF);}
	"("		{return new Symbol(ParserSym.LPAR);}
	")"		{return new Symbol(ParserSym.RPAR);}
	"{"		{return new Symbol(ParserSym.LGPAR);}
	"}"		{return new Symbol(ParserSym.RGPAR);}
	"<-"	{return new Symbol(ParserSym.READ);}
	"->"	{return new Symbol(ParserSym.WRITE);}
	"+"		{return new Symbol(ParserSym.PLUS);}
	"-"		{return new Symbol(ParserSym.MINUS);}
	"*"		{return new Symbol(ParserSym.TIMES);}
	"/"		{return new Symbol(ParserSym.DIV);}
	"true"  {return new Symbol(ParserSym.TRUE);}
	"false" {return new Symbol(ParserSym.FALSE);}
	"="		{return new Symbol(ParserSym.ASSIGN);}
	"if"	{return new Symbol(ParserSym.IF);}
	"then"	{return new Symbol(ParserSym.THEN);}
	"while" {return new Symbol(ParserSym.WHILE);}
	"do"	{return new Symbol(ParserSym.DO);}
	"else"  {return new Symbol(ParserSym.ELSE);}
	">"		{return new Symbol(ParserSym.GT);}
	">="	{return new Symbol(ParserSym.GE);}
	"<"		{return new Symbol(ParserSym.LT);}
	"<="	{return new Symbol(ParserSym.LE);}
	"=="	{return new Symbol(ParserSym.EQ);}
	"not"	{return new Symbol(ParserSym.NOT);}
	"and"	{return new Symbol(ParserSym.AND);}
	"or"	{return new Symbol(ParserSym.OR);}
	"-"		{return new Symbol(ParserSym.UMINUS);}
	"in"	{return new Symbol(ParserSym.IN);}
	"out"	{return new Symbol(ParserSym.OUT);}
	"inout"	{return new Symbol(ParserSym.INOUT);}
		
	{DigitInt}  {return new Symbol(ParserSym.INT_CONST, yytext()); }
	{DigitDouble}  {return new Symbol(ParserSym.DOUBLE_CONST, yytext()); }	
	{Identifier} { return symbol(ParserSym.ID, yytext());}
	
	
    /*When found " String_Const*/
    \"   { yybegin(STRING_CONST);string.setLength(0);  }


  /* When found ' Char_Const */
  \'                             { yybegin(CHAR_CONST); }
}



<STRING_CONST> {

      \"                             { yybegin(YYINITIAL); }
      [^\n\r\"\\]+                   {
                         string.append( yytext() ); 
                         return new Symbol(ParserSym.STRING_CONST, new String(yytext()));}
      \\t                            { string.append('\t'); }
      \\n                            { string.append('\n'); }

      \\r                            { string.append('\r'); }
      \\\"                           { string.append('\"'); }
      \\                             { string.append('\\'); }
}


  /* error cases */
  
  \\.                            { throw new RuntimeException("Illegal escape sequence \""+yytext()+"\""); }
  {LineTerminator}               { throw new RuntimeException("Unterminated character literal at end of line"); }


/* error fallback */
[^]                              { throw new RuntimeException("Illegal character \""+yytext()+
                                                              "\" at line "+yyline+", column "+yycolumn); }
                                                              
<<EOF>>                          { return new Symbol(ParserSym.EOF); }
                                                          