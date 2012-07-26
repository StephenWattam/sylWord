import java.io.*;
/** Counts syllables within words, when provided with a string.  Intended to run as a program. 
  @author Stephen Wattam
  @version 0.1b 
 */
public class SylWord{

	//Version number
	public static String VERSION = "0.1b";

	// ui and output use
	private String settingsFilepath;
	private String inputFilepath = "STDIN";
	private String outputFilepath = "STDOUT";

	// io systems
	private BufferedReader inputStream = new BufferedReader(new InputStreamReader(System.in));
	private PrintStream outputStream = System.out;

	// Settings
	private Settings settings;

	/* -------------------------------------------[Constructor] -------------------------------------------*/
	public SylWord(String[] args){
		try{
			processArguments(args);
			settings = new Settings(settingsFilepath);
		}catch(IOException IOe){
			System.err.println("Error opening IO resources specified.  Please check usage.\n");
			IOe.printStackTrace();
			quitError();
		}

		if(args.length > 2){
			System.out.println("SylWord " + VERSION);
			System.out.println("");

			System.out.println("Settings file: \t"	+ settingsFilepath);
			System.out.println("   Input File: \t "	+ inputFilepath);
			System.out.println("  Output File: \t "	+ outputFilepath);
			System.out.println("");
			System.out.println("  Sentence Rx: \t"      + settings.sentenceBufferRegex);
			System.out.println("   Ignore Set: \t"      + printArray(settings.ignoreSet));
			System.out.println("");
			System.out.print("       Tokens:");
			summariseTokens();
		}

		// Set up reading items
		WordTokenSource win = new WordTokenSource(settings, inputStream);
		Token t;

		// start processing TODO: make the settings and rules work nicely, remove debug
		try{
			while((t = classifyToken(win.readToken())) != null){
				outputStream.println(t.str + "\t" + t.type);
			}
		}catch(IOException IOe){
			System.err.println("IO Error processing input.");
			quitError();
		}
	}
	/* -------------------------------------------[Main Processing Loops] -------------------------------------------*/
	/** Classifies a token, annotating it with a string indicating that it is
	  an EOS marker, ACRONYM, NUMERAL or the number of syllables therein.

	  @param t The token to classify.
	  @return A classified token, with the same content as t.
	 */
	private Token classifyToken(Token t){
		if(t==null) 
			return null;

		// Is this already classified?
		if(t.type != null) 
			//System.out.println("EOS '" + t.type + "' == '" + Token.EOS + "'");
			return t;

		String word = t.str;
		if(word.matches("[0-9\\-+]+"))
			return new Token(word, Token.NUMERAL);
		if(word.length() > 1 && word.toUpperCase() == word && word.toUpperCase() != word.toLowerCase())
			return new Token(word, Token.ACRONYM);
		return new Token(word, "" + countSyllables(word));
	}

	/** Counts syllables within a string.

	  @return The number of syllables found in the string.
	 */
	private int countSyllables(String str){
		int count = 0;
		String[] splits;

		// remove ignore chars
		for(int i=0;i<settings.ignoreSet.length; i++)
			str = str.replaceAll(settings.ignoreSet[i], "");

		// add padding for more sensible regex
		str = " " + str.toLowerCase() + " ";

		for(int i=0;i<settings.tokens.length;i++){
			// level i
			for(int j=0;j<settings.tokens[i].length;j++){
				splits = str.split(settings.tokens[i][j].toLowerCase());
				//System.out.println("count: " + count + ", token: " + str + ", split: " + settings.tokens[i][j] + ", array: " + printArray(splits));
				if(splits.length == 0) // token matched whole string
					count += 1;
				else // token split the string
					count += splits.length - 1;
				str = concatStrArray(splits);
			}
		}

		return count;
	}
	/* -------------------------------------------[Processing Helpers] -------------------------------------------*/
	// 1.4 compatible version of generic-based copy code.
	// FIXME: this is very slow.  Move to System.arrayCopy
	private String concatStrArray(String[] arr){
		String result = "";
		for(int i=0; i<arr.length; i++)
			result += arr[i];
		return result;
	}

	// debug only.
	private String printArray(String[] arr){
		String str = "[";
		for(int i=0;i<arr.length;i++)
			str += "'" + arr[i] + "', ";

		return str + arr.length + "]";
	}



	/* -------------------------------------------[UI Helpers] -------------------------------------------*/
	// prints a summary of tokens.
	public void summariseTokens(){
		for(int i=0; i<settings.tokens.length; i++){
			System.out.print("\n\t\tLevel " + i + ":\t");
			for(int j=0;j<settings.tokens[i].length; j++){
				System.out.print(settings.tokens[i][j] + " ");
			}
		}
		System.out.print("\n");
	}


	// Process arguments for the program, loading them into instance vars.
	private void processArguments(String[] args) throws IOException{
		if(args.length <= 0 || args.length > 3){
			showHelp();
			quitNicely();
		}

		settingsFilepath = args[0];

		// Load the input file
		if(args.length >= 2){
			inputFilepath = args[1];
			inputStream = new BufferedReader(new FileReader(inputFilepath));
		}

		// Load the output file
		if(args.length == 3){
			outputFilepath = args[2];
			outputStream = new PrintStream(new FileOutputStream(outputFilepath));
		}
	}

	private void showHelp(){
		System.out.println("SylWord");
		System.out.println("");
		System.out.println("Usage:");
		System.out.println("Java SylWord VOWEL_FILE [INPUT [OUTPUT]]");
		System.out.println("");
		System.out.println("Omit OUTPUT to output to stdout.");
		System.out.println("Omit INPUT to read from stdin.");
	}

	// Exit with a success code.
	private void quitNicely(){
		System.exit(0);
	}

	// Exit with a specific code 
	private void quitError(int code){
		System.exit(code);
	}

	// Exit with a general failure code
	private void quitError(){
		System.exit(1);
	}

	/* -------------------------------------------[main] -------------------------------------------*/
	public static void main(String[] args){
		new SylWord(args);
	}

}
