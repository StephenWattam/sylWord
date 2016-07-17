# SylWord
The purpose of SylWord is to identify and count syllables in words by way of
iteratively identifying and discounting tokens.

## Config Format
The config format consists of the following:

 1) Whitespace-separated lists of tokens, searched "left-first and top-first" 
    on a first-match basis.
 2) Comments (any line beginning with a '#' or otherwise empty)
 3) Directives (key-value pairs of the form >key:value)
    * IgnoreSet:        A whitespace-separated list of tokens to be discounted 
			from the analysis
    * SentenceBoundary: A regexp describing the way to identify the end of 
                        sentences.  Default is ([.!?\-'])

See some of the data in testdata for a sample.

## Compilation
If you're on unix, the makefile should provide you with a working set of 
classes in ./cls/

If you're not on unix, the following command should compile you a fresh set:

    $ javac -source 1.4 -d cls -sourcepath src src/*.java

## Usage
Simply run the SylWord class.  Running it without argumentds produces the 
following help message:

    $ java SylWord 
    SylWord

    Usage:
    Java SylWord VOWEL_FILE [INPUT [OUTPUT]]

    Omit OUTPUT to output to stdout.
    Omit INPUT to read from stdin.

The example in the test script runs the following:
$ java SylWord vowels.txt in.txt /tmp/fulltest_out.txt


## OUTLINE
What I would like to have is a program that will identify word lengths in a text (in terms of spoken syllables rather than characters).  This is to help me with my work on word-length distributions, primarily in the Celtic languages.  The task is perhaps rather less difficult than it may sound, because the number of syllables in a word can be identified easily by counting the number of diphthongs/vowels in the word.
 
The input would be a plain text file, probably in UTF-8 format because most of these languages have some accented characters (and Welsh has one that is not part of the usual Western European character set).
 
The output would be a vertical tab-separated file of the same text, with the word length for each word (or a default comment) printed in the second column.  The default comments would be to alert the analyst (i.e. me) to words which probably need manual intervention before the stats are processed further - basically acronyms, abbreviations, and numerals.  An easy rough rule of thumb for acronyms would be words of more than two characters in all-caps, and for abbreviations words with no vowels counted in them.  EOS marks end of sentence.
 
### Example input:
 
    Dyrchefwch eich calonnau PCB. Yr ydym yn eu dyrchafael 1986 i'r Arglwydd. Dïolchwn i'n Harglwydd Dduw.
 
### Example output:
 
    Dyrchefwch    3
    eich 1
    calonnau  3
    PCB  ACRONYM
    .    EOS
    Yr   1
    ydym 2
    yn   1
    eu   1
    dyrchafael    3
    1986 NUMERAL
    i'r  1
    Arglwydd  2
    .    EOS
    Dïolchwn  3
    i'n  1
    Harglwydd 2
    Dduw 1
    .    EOS
 
The inventory of vowels and diphthongs would be stored in a separate editable file, enabling different languages to be processed.  An example for the above sentences might be as follows (though I'm not wedded to the format here):
 
### vowels

    e y w i a o u ï

### diphthongs

    ei au eu ae uw io wy
 
The diphthongs would have priority over single vowels when counting (so, e.g., "Dduw" would be matched by the "uw" diphthong rule and there would then be nothing left over to be counted by the single vowel rules).
 
I wondered whether I might also need an extra inventory of two- or three-letter exception rules, but I don't think so for these languages.  (However, in Latin, for example, the letter "i" can be a vowel or consonant, depending on its position in relation to other letters…)
 
System preferences: EITHER compatible with j2re1.4.2\_18 running under Ubuntu 8.10 [i.e. my home laptop - it's not on the net and I don't have any other compilers/interpreters installed, as far as I know…] OR a PC program that will run on the Windows machine in the office [pref. without installation permissions].  (I do also have Wine installed on the laptop!)
