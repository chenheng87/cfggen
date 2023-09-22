// Generated from D:/work/mygithub/cfggen/app/src/main/java/configgen/schema/cfg/Cfg.g4 by ANTLR 4.13.1
package configgen.schema.cfg;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class CfgLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, TLIST=6, TMAP=7, TBASE=8, REF=9, 
		LISTREF=10, COMMENT=11, SEMI=12, EQ=13, LP=14, RP=15, LB=16, RB=17, LC=18, 
		RC=19, DOT=20, COMMA=21, COLON=22, PLUS=23, MINUS=24, STRING_CONSTANT=25, 
		INTEGER_CONSTANT=26, IDENT=27, HEX_INTEGER_CONSTANT=28, FLOAT_CONSTANT=29, 
		WS=30;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "TLIST", "TMAP", "TBASE", "REF", 
			"LISTREF", "COMMENT", "SEMI", "EQ", "LP", "RP", "LB", "RB", "LC", "RC", 
			"DOT", "COMMA", "COLON", "PLUS", "MINUS", "DECIMAL_DIGIT", "HEXADECIMAL_DIGIT", 
			"ESCAPE_SEQUENCE", "SIMPLE_ESCAPE_SEQUENCE", "HEXADECIMAL_ESCAPE_SEQUENCE", 
			"UNICODE_ESCAPE_SEQUENCE", "STRING_CONSTANT", "SCHAR_SEQUENCE", "SCHAR", 
			"INTEGER_CONSTANT", "IDENT", "HEX_INTEGER_CONSTANT", "FLOAT_CONSTANT", 
			"FLOATLIT", "DECIMALS", "EXPONENT", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'struct'", "'interface'", "'table'", "'<'", "'>'", "'list'", "'map'", 
			null, "'->'", "'=>'", null, "';'", "'='", "'('", "')'", "'['", "']'", 
			"'{'", "'}'", "'.'", "','", "':'", "'+'", "'-'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, "TLIST", "TMAP", "TBASE", "REF", 
			"LISTREF", "COMMENT", "SEMI", "EQ", "LP", "RP", "LB", "RB", "LC", "RC", 
			"DOT", "COMMA", "COLON", "PLUS", "MINUS", "STRING_CONSTANT", "INTEGER_CONSTANT", 
			"IDENT", "HEX_INTEGER_CONSTANT", "FLOAT_CONSTANT", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public CfgLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Cfg.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\u001e\u013b\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014"+
		"\u0002\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017"+
		"\u0002\u0018\u0007\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a"+
		"\u0002\u001b\u0007\u001b\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d"+
		"\u0002\u001e\u0007\u001e\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!"+
		"\u0007!\u0002\"\u0007\"\u0002#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002"+
		"&\u0007&\u0002\'\u0007\'\u0002(\u0007(\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0003\u0007"+
		"\u0092\b\u0007\u0001\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001\n"+
		"\u0001\n\u0001\n\u0001\n\u0005\n\u009e\b\n\n\n\f\n\u00a1\t\n\u0001\u000b"+
		"\u0001\u000b\u0001\f\u0001\f\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001"+
		"\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001"+
		"\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001"+
		"\u0015\u0001\u0015\u0001\u0016\u0001\u0016\u0001\u0017\u0001\u0017\u0001"+
		"\u0018\u0001\u0018\u0001\u0019\u0001\u0019\u0001\u001a\u0001\u001a\u0001"+
		"\u001a\u0003\u001a\u00c4\b\u001a\u0001\u001b\u0001\u001b\u0001\u001b\u0001"+
		"\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0004\u001c\u00cd\b\u001c\u000b"+
		"\u001c\f\u001c\u00ce\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0004"+
		"\u001d\u00d5\b\u001d\u000b\u001d\f\u001d\u00d6\u0001\u001e\u0001\u001e"+
		"\u0003\u001e\u00db\b\u001e\u0001\u001e\u0001\u001e\u0001\u001f\u0004\u001f"+
		"\u00e0\b\u001f\u000b\u001f\f\u001f\u00e1\u0001 \u0001 \u0003 \u00e6\b"+
		" \u0001!\u0003!\u00e9\b!\u0001!\u0004!\u00ec\b!\u000b!\f!\u00ed\u0001"+
		"!\u0001!\u0001!\u0001!\u0001!\u0001!\u0001!\u0001!\u0001!\u0003!\u00f9"+
		"\b!\u0001\"\u0001\"\u0005\"\u00fd\b\"\n\"\f\"\u0100\t\"\u0001#\u0003#"+
		"\u0103\b#\u0001#\u0001#\u0001#\u0004#\u0108\b#\u000b#\f#\u0109\u0001$"+
		"\u0001$\u0003$\u010e\b$\u0001$\u0001$\u0001%\u0001%\u0001%\u0003%\u0115"+
		"\b%\u0001%\u0003%\u0118\b%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0003"+
		"%\u0120\b%\u0003%\u0122\b%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0003"+
		"%\u012a\b%\u0001&\u0004&\u012d\b&\u000b&\f&\u012e\u0001\'\u0001\'\u0001"+
		"\'\u0003\'\u0134\b\'\u0001\'\u0001\'\u0001(\u0001(\u0001(\u0001(\u0000"+
		"\u0000)\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b"+
		"\u0006\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u001b"+
		"\u000e\u001d\u000f\u001f\u0010!\u0011#\u0012%\u0013\'\u0014)\u0015+\u0016"+
		"-\u0017/\u00181\u00003\u00005\u00007\u00009\u0000;\u0000=\u0019?\u0000"+
		"A\u0000C\u001aE\u001bG\u001cI\u001dK\u0000M\u0000O\u0000Q\u001e\u0001"+
		"\u0000\u000b\u0002\u0000\n\n\r\r\u0001\u000009\u0003\u000009AFaf\u000b"+
		"\u0000\"\"\'\'//??\\\\bbffnnrrttvv\u0004\u0000\n\n\r\r\"\"\\\\\u0002\u0000"+
		"++--\u0003\u0000AZ__az\u0004\u000009AZ__az\u0002\u0000XXxx\u0002\u0000"+
		"EEee\u0003\u0000\t\n\r\r  \u0150\u0000\u0001\u0001\u0000\u0000\u0000\u0000"+
		"\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000"+
		"\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b"+
		"\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001"+
		"\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001"+
		"\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001"+
		"\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001"+
		"\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001"+
		"\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000\u0000#\u0001\u0000\u0000"+
		"\u0000\u0000%\u0001\u0000\u0000\u0000\u0000\'\u0001\u0000\u0000\u0000"+
		"\u0000)\u0001\u0000\u0000\u0000\u0000+\u0001\u0000\u0000\u0000\u0000-"+
		"\u0001\u0000\u0000\u0000\u0000/\u0001\u0000\u0000\u0000\u0000=\u0001\u0000"+
		"\u0000\u0000\u0000C\u0001\u0000\u0000\u0000\u0000E\u0001\u0000\u0000\u0000"+
		"\u0000G\u0001\u0000\u0000\u0000\u0000I\u0001\u0000\u0000\u0000\u0000Q"+
		"\u0001\u0000\u0000\u0000\u0001S\u0001\u0000\u0000\u0000\u0003Z\u0001\u0000"+
		"\u0000\u0000\u0005d\u0001\u0000\u0000\u0000\u0007j\u0001\u0000\u0000\u0000"+
		"\tl\u0001\u0000\u0000\u0000\u000bn\u0001\u0000\u0000\u0000\rs\u0001\u0000"+
		"\u0000\u0000\u000f\u0091\u0001\u0000\u0000\u0000\u0011\u0093\u0001\u0000"+
		"\u0000\u0000\u0013\u0096\u0001\u0000\u0000\u0000\u0015\u0099\u0001\u0000"+
		"\u0000\u0000\u0017\u00a2\u0001\u0000\u0000\u0000\u0019\u00a4\u0001\u0000"+
		"\u0000\u0000\u001b\u00a6\u0001\u0000\u0000\u0000\u001d\u00a8\u0001\u0000"+
		"\u0000\u0000\u001f\u00aa\u0001\u0000\u0000\u0000!\u00ac\u0001\u0000\u0000"+
		"\u0000#\u00ae\u0001\u0000\u0000\u0000%\u00b0\u0001\u0000\u0000\u0000\'"+
		"\u00b2\u0001\u0000\u0000\u0000)\u00b4\u0001\u0000\u0000\u0000+\u00b6\u0001"+
		"\u0000\u0000\u0000-\u00b8\u0001\u0000\u0000\u0000/\u00ba\u0001\u0000\u0000"+
		"\u00001\u00bc\u0001\u0000\u0000\u00003\u00be\u0001\u0000\u0000\u00005"+
		"\u00c3\u0001\u0000\u0000\u00007\u00c5\u0001\u0000\u0000\u00009\u00c8\u0001"+
		"\u0000\u0000\u0000;\u00d0\u0001\u0000\u0000\u0000=\u00d8\u0001\u0000\u0000"+
		"\u0000?\u00df\u0001\u0000\u0000\u0000A\u00e5\u0001\u0000\u0000\u0000C"+
		"\u00f8\u0001\u0000\u0000\u0000E\u00fa\u0001\u0000\u0000\u0000G\u0102\u0001"+
		"\u0000\u0000\u0000I\u010d\u0001\u0000\u0000\u0000K\u0129\u0001\u0000\u0000"+
		"\u0000M\u012c\u0001\u0000\u0000\u0000O\u0130\u0001\u0000\u0000\u0000Q"+
		"\u0137\u0001\u0000\u0000\u0000ST\u0005s\u0000\u0000TU\u0005t\u0000\u0000"+
		"UV\u0005r\u0000\u0000VW\u0005u\u0000\u0000WX\u0005c\u0000\u0000XY\u0005"+
		"t\u0000\u0000Y\u0002\u0001\u0000\u0000\u0000Z[\u0005i\u0000\u0000[\\\u0005"+
		"n\u0000\u0000\\]\u0005t\u0000\u0000]^\u0005e\u0000\u0000^_\u0005r\u0000"+
		"\u0000_`\u0005f\u0000\u0000`a\u0005a\u0000\u0000ab\u0005c\u0000\u0000"+
		"bc\u0005e\u0000\u0000c\u0004\u0001\u0000\u0000\u0000de\u0005t\u0000\u0000"+
		"ef\u0005a\u0000\u0000fg\u0005b\u0000\u0000gh\u0005l\u0000\u0000hi\u0005"+
		"e\u0000\u0000i\u0006\u0001\u0000\u0000\u0000jk\u0005<\u0000\u0000k\b\u0001"+
		"\u0000\u0000\u0000lm\u0005>\u0000\u0000m\n\u0001\u0000\u0000\u0000no\u0005"+
		"l\u0000\u0000op\u0005i\u0000\u0000pq\u0005s\u0000\u0000qr\u0005t\u0000"+
		"\u0000r\f\u0001\u0000\u0000\u0000st\u0005m\u0000\u0000tu\u0005a\u0000"+
		"\u0000uv\u0005p\u0000\u0000v\u000e\u0001\u0000\u0000\u0000wx\u0005b\u0000"+
		"\u0000xy\u0005o\u0000\u0000yz\u0005o\u0000\u0000z\u0092\u0005l\u0000\u0000"+
		"{|\u0005i\u0000\u0000|}\u0005n\u0000\u0000}\u0092\u0005t\u0000\u0000~"+
		"\u007f\u0005l\u0000\u0000\u007f\u0080\u0005o\u0000\u0000\u0080\u0081\u0005"+
		"n\u0000\u0000\u0081\u0092\u0005g\u0000\u0000\u0082\u0083\u0005f\u0000"+
		"\u0000\u0083\u0084\u0005l\u0000\u0000\u0084\u0085\u0005o\u0000\u0000\u0085"+
		"\u0086\u0005a\u0000\u0000\u0086\u0092\u0005t\u0000\u0000\u0087\u0088\u0005"+
		"s\u0000\u0000\u0088\u0089\u0005t\u0000\u0000\u0089\u0092\u0005r\u0000"+
		"\u0000\u008a\u008b\u0005r\u0000\u0000\u008b\u008c\u0005e\u0000\u0000\u008c"+
		"\u0092\u0005s\u0000\u0000\u008d\u008e\u0005t\u0000\u0000\u008e\u008f\u0005"+
		"e\u0000\u0000\u008f\u0090\u0005x\u0000\u0000\u0090\u0092\u0005t\u0000"+
		"\u0000\u0091w\u0001\u0000\u0000\u0000\u0091{\u0001\u0000\u0000\u0000\u0091"+
		"~\u0001\u0000\u0000\u0000\u0091\u0082\u0001\u0000\u0000\u0000\u0091\u0087"+
		"\u0001\u0000\u0000\u0000\u0091\u008a\u0001\u0000\u0000\u0000\u0091\u008d"+
		"\u0001\u0000\u0000\u0000\u0092\u0010\u0001\u0000\u0000\u0000\u0093\u0094"+
		"\u0005-\u0000\u0000\u0094\u0095\u0005>\u0000\u0000\u0095\u0012\u0001\u0000"+
		"\u0000\u0000\u0096\u0097\u0005=\u0000\u0000\u0097\u0098\u0005>\u0000\u0000"+
		"\u0098\u0014\u0001\u0000\u0000\u0000\u0099\u009a\u0005/\u0000\u0000\u009a"+
		"\u009b\u0005/\u0000\u0000\u009b\u009f\u0001\u0000\u0000\u0000\u009c\u009e"+
		"\b\u0000\u0000\u0000\u009d\u009c\u0001\u0000\u0000\u0000\u009e\u00a1\u0001"+
		"\u0000\u0000\u0000\u009f\u009d\u0001\u0000\u0000\u0000\u009f\u00a0\u0001"+
		"\u0000\u0000\u0000\u00a0\u0016\u0001\u0000\u0000\u0000\u00a1\u009f\u0001"+
		"\u0000\u0000\u0000\u00a2\u00a3\u0005;\u0000\u0000\u00a3\u0018\u0001\u0000"+
		"\u0000\u0000\u00a4\u00a5\u0005=\u0000\u0000\u00a5\u001a\u0001\u0000\u0000"+
		"\u0000\u00a6\u00a7\u0005(\u0000\u0000\u00a7\u001c\u0001\u0000\u0000\u0000"+
		"\u00a8\u00a9\u0005)\u0000\u0000\u00a9\u001e\u0001\u0000\u0000\u0000\u00aa"+
		"\u00ab\u0005[\u0000\u0000\u00ab \u0001\u0000\u0000\u0000\u00ac\u00ad\u0005"+
		"]\u0000\u0000\u00ad\"\u0001\u0000\u0000\u0000\u00ae\u00af\u0005{\u0000"+
		"\u0000\u00af$\u0001\u0000\u0000\u0000\u00b0\u00b1\u0005}\u0000\u0000\u00b1"+
		"&\u0001\u0000\u0000\u0000\u00b2\u00b3\u0005.\u0000\u0000\u00b3(\u0001"+
		"\u0000\u0000\u0000\u00b4\u00b5\u0005,\u0000\u0000\u00b5*\u0001\u0000\u0000"+
		"\u0000\u00b6\u00b7\u0005:\u0000\u0000\u00b7,\u0001\u0000\u0000\u0000\u00b8"+
		"\u00b9\u0005+\u0000\u0000\u00b9.\u0001\u0000\u0000\u0000\u00ba\u00bb\u0005"+
		"-\u0000\u0000\u00bb0\u0001\u0000\u0000\u0000\u00bc\u00bd\u0007\u0001\u0000"+
		"\u0000\u00bd2\u0001\u0000\u0000\u0000\u00be\u00bf\u0007\u0002\u0000\u0000"+
		"\u00bf4\u0001\u0000\u0000\u0000\u00c0\u00c4\u00037\u001b\u0000\u00c1\u00c4"+
		"\u00039\u001c\u0000\u00c2\u00c4\u0003;\u001d\u0000\u00c3\u00c0\u0001\u0000"+
		"\u0000\u0000\u00c3\u00c1\u0001\u0000\u0000\u0000\u00c3\u00c2\u0001\u0000"+
		"\u0000\u0000\u00c46\u0001\u0000\u0000\u0000\u00c5\u00c6\u0005\\\u0000"+
		"\u0000\u00c6\u00c7\u0007\u0003\u0000\u0000\u00c78\u0001\u0000\u0000\u0000"+
		"\u00c8\u00c9\u0005\\\u0000\u0000\u00c9\u00ca\u0005x\u0000\u0000\u00ca"+
		"\u00cc\u0001\u0000\u0000\u0000\u00cb\u00cd\u00033\u0019\u0000\u00cc\u00cb"+
		"\u0001\u0000\u0000\u0000\u00cd\u00ce\u0001\u0000\u0000\u0000\u00ce\u00cc"+
		"\u0001\u0000\u0000\u0000\u00ce\u00cf\u0001\u0000\u0000\u0000\u00cf:\u0001"+
		"\u0000\u0000\u0000\u00d0\u00d1\u0005\\\u0000\u0000\u00d1\u00d2\u0005u"+
		"\u0000\u0000\u00d2\u00d4\u0001\u0000\u0000\u0000\u00d3\u00d5\u00033\u0019"+
		"\u0000\u00d4\u00d3\u0001\u0000\u0000\u0000\u00d5\u00d6\u0001\u0000\u0000"+
		"\u0000\u00d6\u00d4\u0001\u0000\u0000\u0000\u00d6\u00d7\u0001\u0000\u0000"+
		"\u0000\u00d7<\u0001\u0000\u0000\u0000\u00d8\u00da\u0005\'\u0000\u0000"+
		"\u00d9\u00db\u0003?\u001f\u0000\u00da\u00d9\u0001\u0000\u0000\u0000\u00da"+
		"\u00db\u0001\u0000\u0000\u0000\u00db\u00dc\u0001\u0000\u0000\u0000\u00dc"+
		"\u00dd\u0005\'\u0000\u0000\u00dd>\u0001\u0000\u0000\u0000\u00de\u00e0"+
		"\u0003A \u0000\u00df\u00de\u0001\u0000\u0000\u0000\u00e0\u00e1\u0001\u0000"+
		"\u0000\u0000\u00e1\u00df\u0001\u0000\u0000\u0000\u00e1\u00e2\u0001\u0000"+
		"\u0000\u0000\u00e2@\u0001\u0000\u0000\u0000\u00e3\u00e6\b\u0004\u0000"+
		"\u0000\u00e4\u00e6\u00035\u001a\u0000\u00e5\u00e3\u0001\u0000\u0000\u0000"+
		"\u00e5\u00e4\u0001\u0000\u0000\u0000\u00e6B\u0001\u0000\u0000\u0000\u00e7"+
		"\u00e9\u0007\u0005\u0000\u0000\u00e8\u00e7\u0001\u0000\u0000\u0000\u00e8"+
		"\u00e9\u0001\u0000\u0000\u0000\u00e9\u00eb\u0001\u0000\u0000\u0000\u00ea"+
		"\u00ec\u00031\u0018\u0000\u00eb\u00ea\u0001\u0000\u0000\u0000\u00ec\u00ed"+
		"\u0001\u0000\u0000\u0000\u00ed\u00eb\u0001\u0000\u0000\u0000\u00ed\u00ee"+
		"\u0001\u0000\u0000\u0000\u00ee\u00f9\u0001\u0000\u0000\u0000\u00ef\u00f0"+
		"\u0005t\u0000\u0000\u00f0\u00f1\u0005r\u0000\u0000\u00f1\u00f2\u0005u"+
		"\u0000\u0000\u00f2\u00f9\u0005e\u0000\u0000\u00f3\u00f4\u0005f\u0000\u0000"+
		"\u00f4\u00f5\u0005a\u0000\u0000\u00f5\u00f6\u0005l\u0000\u0000\u00f6\u00f7"+
		"\u0005s\u0000\u0000\u00f7\u00f9\u0005e\u0000\u0000\u00f8\u00e8\u0001\u0000"+
		"\u0000\u0000\u00f8\u00ef\u0001\u0000\u0000\u0000\u00f8\u00f3\u0001\u0000"+
		"\u0000\u0000\u00f9D\u0001\u0000\u0000\u0000\u00fa\u00fe\u0007\u0006\u0000"+
		"\u0000\u00fb\u00fd\u0007\u0007\u0000\u0000\u00fc\u00fb\u0001\u0000\u0000"+
		"\u0000\u00fd\u0100\u0001\u0000\u0000\u0000\u00fe\u00fc\u0001\u0000\u0000"+
		"\u0000\u00fe\u00ff\u0001\u0000\u0000\u0000\u00ffF\u0001\u0000\u0000\u0000"+
		"\u0100\u00fe\u0001\u0000\u0000\u0000\u0101\u0103\u0007\u0005\u0000\u0000"+
		"\u0102\u0101\u0001\u0000\u0000\u0000\u0102\u0103\u0001\u0000\u0000\u0000"+
		"\u0103\u0104\u0001\u0000\u0000\u0000\u0104\u0105\u00050\u0000\u0000\u0105"+
		"\u0107\u0007\b\u0000\u0000\u0106\u0108\u00033\u0019\u0000\u0107\u0106"+
		"\u0001\u0000\u0000\u0000\u0108\u0109\u0001\u0000\u0000\u0000\u0109\u0107"+
		"\u0001\u0000\u0000\u0000\u0109\u010a\u0001\u0000\u0000\u0000\u010aH\u0001"+
		"\u0000\u0000\u0000\u010b\u010e\u0003-\u0016\u0000\u010c\u010e\u0003/\u0017"+
		"\u0000\u010d\u010b\u0001\u0000\u0000\u0000\u010d\u010c\u0001\u0000\u0000"+
		"\u0000\u010d\u010e\u0001\u0000\u0000\u0000\u010e\u010f\u0001\u0000\u0000"+
		"\u0000\u010f\u0110\u0003K%\u0000\u0110J\u0001\u0000\u0000\u0000\u0111"+
		"\u0112\u0003M&\u0000\u0112\u0114\u0003\'\u0013\u0000\u0113\u0115\u0003"+
		"M&\u0000\u0114\u0113\u0001\u0000\u0000\u0000\u0114\u0115\u0001\u0000\u0000"+
		"\u0000\u0115\u0117\u0001\u0000\u0000\u0000\u0116\u0118\u0003O\'\u0000"+
		"\u0117\u0116\u0001\u0000\u0000\u0000\u0117\u0118\u0001\u0000\u0000\u0000"+
		"\u0118\u0122\u0001\u0000\u0000\u0000\u0119\u011a\u0003M&\u0000\u011a\u011b"+
		"\u0003O\'\u0000\u011b\u0122\u0001\u0000\u0000\u0000\u011c\u011d\u0003"+
		"\'\u0013\u0000\u011d\u011f\u0003M&\u0000\u011e\u0120\u0003O\'\u0000\u011f"+
		"\u011e\u0001\u0000\u0000\u0000\u011f\u0120\u0001\u0000\u0000\u0000\u0120"+
		"\u0122\u0001\u0000\u0000\u0000\u0121\u0111\u0001\u0000\u0000\u0000\u0121"+
		"\u0119\u0001\u0000\u0000\u0000\u0121\u011c\u0001\u0000\u0000\u0000\u0122"+
		"\u012a\u0001\u0000\u0000\u0000\u0123\u0124\u0005i\u0000\u0000\u0124\u0125"+
		"\u0005n\u0000\u0000\u0125\u012a\u0005f\u0000\u0000\u0126\u0127\u0005n"+
		"\u0000\u0000\u0127\u0128\u0005a\u0000\u0000\u0128\u012a\u0005n\u0000\u0000"+
		"\u0129\u0121\u0001\u0000\u0000\u0000\u0129\u0123\u0001\u0000\u0000\u0000"+
		"\u0129\u0126\u0001\u0000\u0000\u0000\u012aL\u0001\u0000\u0000\u0000\u012b"+
		"\u012d\u00031\u0018\u0000\u012c\u012b\u0001\u0000\u0000\u0000\u012d\u012e"+
		"\u0001\u0000\u0000\u0000\u012e\u012c\u0001\u0000\u0000\u0000\u012e\u012f"+
		"\u0001\u0000\u0000\u0000\u012fN\u0001\u0000\u0000\u0000\u0130\u0133\u0007"+
		"\t\u0000\u0000\u0131\u0134\u0003-\u0016\u0000\u0132\u0134\u0003/\u0017"+
		"\u0000\u0133\u0131\u0001\u0000\u0000\u0000\u0133\u0132\u0001\u0000\u0000"+
		"\u0000\u0133\u0134\u0001\u0000\u0000\u0000\u0134\u0135\u0001\u0000\u0000"+
		"\u0000\u0135\u0136\u0003M&\u0000\u0136P\u0001\u0000\u0000\u0000\u0137"+
		"\u0138\u0007\n\u0000\u0000\u0138\u0139\u0001\u0000\u0000\u0000\u0139\u013a"+
		"\u0006(\u0000\u0000\u013aR\u0001\u0000\u0000\u0000\u0017\u0000\u0091\u009f"+
		"\u00c3\u00ce\u00d6\u00da\u00e1\u00e5\u00e8\u00ed\u00f8\u00fe\u0102\u0109"+
		"\u010d\u0114\u0117\u011f\u0121\u0129\u012e\u0133\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}