/** Represents a single word, EOS marker, numeral or acronym within SylWord.
  @author Stephen Wattam
 */
public class Token{

	// FIXME: should be a separate enum.
	public static final String ACRONYM  = "ACRONYM";
	public static final String NUMERAL  = "NUMERAL";
	public static final String EOS	= "EOS";
	public static final String WORD	= "WORD";

	// Main data storage
	public String str;
	public String type = null;

	/** Creates a new Token with a null type and a given content.
	  @param str The content of the 'str' field in the Token
	 */
	public Token(String str){
		this.str = str;
	}

	/** Creates a new Token with both a type and a given content.
	  @param str The content of the 'str' field in the Token
	  @param type The type field, may be any string.
	 */
	public Token(String str, String type){
		this.str = str;
		this.type = type;
	}

}
