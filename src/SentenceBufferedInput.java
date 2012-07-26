import java.io.*;
import java.util.regex.*;
/** Provides a buffered reader where the unit read is not a line or a byte, but a sentence.
  @author Stephen Wattam
 */
public class SentenceBufferedInput{
	//Where to read raw data from.
	BufferedReader src;

	// persistent buffer.
	String buffer = "";

	// Stop adding to the buffer when the regex from settings is seen.
	Settings settings;

	/* -------------------------------------------[Constructor] -------------------------------------------*/
	/** Creates a new Sentence source with given token and sentence identification rules.

	  Because this class reads per-line, it may be slow if text is not multi-line.

	  @param s A Settings object providing rules on how to identify sentences.
	  @param bin Where to read data as buffered by line.
	 */
	public SentenceBufferedInput(Settings s, BufferedReader bin){
		src = bin;
		settings = s;
	}


	/* -------------------------------------------[Main Routines] -------------------------------------------*/
	/** Read a sentence from the buffer.

	  @throws IOException in the event anything prevents reading the data source.
	 */
	public String readSentence() throws IOException{
		String line;

		// Read at least one sentence
		while(!buffer.matches(".*" + settings.sentenceBufferRegex + ".*")){
			// if this is the end of the file, assume it ends the sentence.
			line = src.readLine();
			if(line == null)
				if(buffer.length() == 0)
					return null;
				else{
					String tmp = buffer;
					buffer = "";
					return tmp;
				}
			buffer += line;
		}


		Pattern p = Pattern.compile(settings.sentenceBufferRegex);
		Matcher m = p.matcher(buffer);
		m.find();

		//System.out.println("Buffer: '" + buffer + "'");
		//System.out.println("-------> " + m.find() );

		// prune the first sentence off.
		String sentence = buffer.substring(0, m.start()+1);
		buffer			= buffer.substring(sentence.length());

		//System.out.println("Sentence: " + sentence);

		// return the first sentence from the list.
		return sentence;
	}
}
