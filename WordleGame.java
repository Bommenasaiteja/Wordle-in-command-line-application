import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;



class LangDict{

	  List<String> words = Collections.emptyList();
	LangDict(String file){
	// In this constructor we convert the words.txt file to list (words) ..to perform operations on that list
		try {
      this.words = Files.readAllLines(Paths.get(file), StandardCharsets.UTF_8);
    	} catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }}


	public boolean contains(String word)
	{
		//this method will check whether the given string means word is avaliable in words list or not
		//it return true if it is available,else it return false
		return words.contains(word);
	}

	public String randomWord(){
		//this method  return a random word from the words list
		return words.get(new Random().nextInt(words.size()));
	}
}

class WordleBoard{
	//rowCount means number of chances of guessing ...i take 6 chance defaulty
	//colCount means the length of the word ...i take length as 5 defaulty
	private int rowCount;
	private int colCount;
	private ArrayList<String> guesses =new ArrayList<String>();
	private String answer;
	private LangDict wordBank;

	public WordleBoard(String filePath){
		this.rowCount = 6;
		this.colCount = 5;
		this.wordBank =new LangDict(filePath);
		this.answer =wordBank.randomWord();
		//here answer is assigned to a random word in the words list
	}

	public int height(){
		return rowCount;
	}

	public int width(){
		return colCount;
	}

	public int countGuesses(){
			return guesses.size();
		}
	public final String nthGuess(int n)
	{
			return guesses.get(n);
	}
	public final String getAnswer()
	{
			return answer;
	}
	public boolean isGameOver()
	{
			return didWin() || countGuesses() == height();
	}
	public boolean didWin(){
			return guesses.contains(getAnswer());
	}


	public void guess(String str)
	{
			if(str.length()==width() && wordBank.contains(str)){
				guesses.add(str);
			}
	}
}




//display the wordle 

class Display{

	private static String title ="================\nW O R D L E\n================\n";
	private static String leftPadding =" ";

	private enum Color{
		BLACH("\033[0;100m"),
		RED("\033[0;101m"),
		GREEN("\033[0;102m"),
		YELLOW("\033[0;103m"),
    		BLUE("\033[0;104m"),
    		PURPLE("\033[0;105m"),
    		CYAN("\033[0;106m"),
    		WHITE("\033[0;37m");

    	private final String ansi_background;
    	private final static String ANSI_RESET = "\u001B[0m";
 		private Color(String ansi_background){
 			this.ansi_background=ansi_background;
 		}
	}

	//setters
	public static void setTitle(String str){
		title=str;
	}

	public static void setLeftPadding(String str){
		leftPadding = str;
	}

	//public methods
	public static void print(WordleBoard board){
		System.out.println(title);
		for(int i=0;i<board.height();i++)
		{
			if(i<board.countGuesses())
			{
				Display.printComparison(board.nthGuess(i),board.getAnswer(),"|");
			}
			else{
				paddedPrint(" |".repeat(board.width()-1));
			}

			if(i !=board.height()-1){
				paddedPrint("-".repeat(board.width()*2-1));
			}else{
				paddedPrint("");
			}
		}
	}


	public static void promptForWord(){
		System.out.print("Enter a word:\n");
	}
	public static void printAnswer(String str){
		System.out.println("The answer is:\n"+str);
	}
	public static void clear(){
		System.out.print("\033\143");
		System.out.flush();
	}

	//print Helper

	private static void printComparison(String toPrint,String forComparison,String delimeter){
		String output="";
		for(int n=0;n<toPrint.length();n++)
		{
			char nthChar = toPrint.charAt(n);
			if(n<forComparison.length()&&nthChar==forComparison.charAt(n)){
				output +=highLightText(Character.toString(nthChar),Color.GREEN);
			}
			else if(forComparison.contains(String.valueOf(nthChar))){
				output += highLightText(Character.toString(nthChar),Color.YELLOW);
			}
			else{
				output += nthChar;
			}

			if(n != toPrint.length()-1){
				output+=delimeter;
			}
		}

		paddedPrint(output);
	}

	private static void paddedPrint(String str){
		System.out.println(leftPadding+str);
	}
	private static String highLightText(String text,Color forHighlight){

		return forHighlight.ansi_background+text+Color.ANSI_RESET;
	}
}



// Main function 

public class WordleGame{

		private final static boolean DEBUG_MODE =false;

public static void main(String[] args){

     
     WordleBoard board = new WordleBoard("/home/sjbommena/Desktop/WorldeProject/words.txt");
//Words.txt is the file used for wordleBoard generation...it contains all the 5 letters words

    Scanner input = new Scanner(System.in);
		//the loop will continue untill isGameOver is true
		while(!board.isGameOver()){
			Display.clear();
			Display.print(board);
			// i use the if block for debug purpose....u want to verifying the th application it will helpful 
			if(DEBUG_MODE){
				Display.printAnswer(board.getAnswer());
			}
			Display.promptForWord();
			board.guess(input.nextLine().toLowerCase());
		}

		Display.clear();
		Display.print(board);
		Display.printAnswer(board.getAnswer());

		input.close();
		

   }


}
