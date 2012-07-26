import java.io.*;
import java.util.*;
import java.util.regex.*;

/** Provides token and sentence splitting rules for SylWord and its associated tokeniser routines.
  @author Stephen Wattam
 */
public class Settings{

	//Hierarchical list of tokens to look for
	public String[][] tokens;

	// Regex to identify the end of sentences
	public String sentenceBufferRegex = "([.!?])";

	//List of tokens to ignore.
	public String[] ignoreSet = {};


	/* -------------------------------------------[Constructor] -------------------------------------------*/
	/** Creates a Settings object by loading the configuration from a file.  Currently the only way to create one.

	  @param filepath The file to load from.
	  @throws IOException in the event that anything prevents file access.
	 */	
	public Settings(String filepath) throws IOException{
		// load details from file

		BufferedReader reader	= new BufferedReader(new FileReader(filepath));
		List lines		= new ArrayList();
		String tmpln		= reader.readLine();
		String[] line		= {};

		while(tmpln != null){
			line = loadTokenLine(tmpln);
			if(line.length > 0)
				lines.add(line);
			tmpln = reader.readLine();
		}

		// convert to array
		tokens = new String[lines.size()][];
		lines.toArray(tokens);
	}


	/* -------------------------------------------[Load Routines] -------------------------------------------*/
	// load as if from the settings file
	private String[] loadTokenLine(String rawstr){
		rawstr = rawstr.trim();

		// Skip comments and blank lines
		if(rawstr.matches("[^\\w]*#.*") || rawstr.length() == 0)
			return new String[0];


		// Process overrides
		Matcher m = Pattern.compile("[^\\w]*>(\\w+):(.*)").matcher(rawstr);
		if(m.find()){
			String key = m.group(1);
			String val = m.group(2);

			// TODO: make this more generic?
			if(key.equals("SentenceBoundary"))
				sentenceBufferRegex = val;
			if(key.equals("IgnoreSet"))
				ignoreSet= val.split("\\s");

			return new String[0];
		}


		// process
		return rawstr.split("\\s");
	}
}
