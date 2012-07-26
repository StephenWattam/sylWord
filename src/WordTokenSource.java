import java.io.*;
import java.util.*;

/** Provides a way of reading single tokens from a stream.  Has knowledge of a Settings object, providing all tokens and sentence splitting rules.
  @author Stephen Wattam
 */
public class WordTokenSource{


	// A data source for reading one sentence at a time
	private SentenceBufferedInput sin; 

	// The buffer of words.
	ArrayList buffer = new ArrayList();

	// Token, IO settings as loaded by SylWord
	private Settings settings;


	/* -------------------------------------------[Constructor] -------------------------------------------*/
	/** Creates a WordTokenSource with some token-related settings and a source of data.

	  @param s The Settings object that defines EOS split points and other token items.
	  @param bin Where to read data, as a buffered reader.
	 */
	public WordTokenSource(Settings s, BufferedReader bin){
		settings = s;
		sin = new SentenceBufferedInput(s, bin);
	}

	/** Acquires a token from the source.  This handles all reading/writing from the source reader,
	  reading a sentence when required and caching the words.

	  @return A token from the source, or null if there are none left.
	  @throws IOException if the BufferedReader given in the constructor raises some error.
	 */
	public Token readToken() throws IOException{
		if(buffer.isEmpty())
			populateBuffer();


		// try to take a token from the beginning, add on the end.
		try{
			return (Token)buffer.remove(0);
		}catch(IndexOutOfBoundsException IOOBe){}

		// return nothing if no content
		return null;
	}

	/* -------------------------------------------[Processing Helpers] -------------------------------------------*/
	/** Populates the internal buffer with words.
	  Throws IOException when the data source does, returns null if the data source runs out of data.
	 */
	private void populateBuffer() throws IOException{
		String sentence = sin.readSentence();
		if(sentence == null)
			return;


		String eosChar = sentence.substring(sentence.length()-1);
		if(eosChar.matches(settings.sentenceBufferRegex))
			sentence = sentence.substring(0, sentence.length()-1);
		else
			eosChar = null;

		// get word candidates
		String[] wordCandidates = split(sentence.trim());

		// classify them into tokens
		//Token[] tokens = new Token[wordCandidates.length + 1];

		for(int i=0;i<wordCandidates.length;i++)
			buffer.add( new Token(wordCandidates[i]) );

		if(eosChar != null)
			buffer.add( new Token(eosChar, Token.EOS) );


	}	

	// Words are separated by whitespace.
	private String[] split(String sentence){
		return sentence.split("\\s");
	}
}
