import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HangManOnlineBForce {
	static int [] tries;
	public static void main(String[] args) throws MalformedURLException {
		// TODO Auto-generated method stub

		// get words and splits
		HttpConnection words = new HttpConnection("http://rocky-everglades-1153.herokuapp.com/dict");
		System.out.println(words.token);
		String [] dataWords = words.token.substring(2, words.token.length()-2).split("', '");

		// Test whether the words are in correct form
		/*for (int i = 0; i < dataWords.length; i++){
		System.out.print(dataWords[i] + " ");
		}*/

		//User name name set up for connection and get token
		//Scanner in = new Scanner(System.in);
		//System.out.println("Please enter user name:");
		//String userName = in.nextLine();
		HttpConnection userSet = new HttpConnection("http://rocky-everglades-1153.herokuapp.com/start?name="+"abcd");
		System.out.println(userSet.token);

		//Put all possible words in to hash table to access the group of same size words easily
		Map<Integer, ArrayList<String>> possibleWords = new HashMap<Integer,ArrayList<String>>();
		for (int i = 0; i < dataWords.length; i++){
			if (possibleWords.containsKey(dataWords[i].length())){
				ArrayList<String> temp = new ArrayList<String>();
				temp = possibleWords.get(dataWords[i].length());
				temp.add(dataWords[i]);
				possibleWords.put(dataWords[i].length(), temp);
			}else{
				ArrayList<String> temp = new ArrayList<String>();
				temp.add(dataWords[i]);
				possibleWords.put(dataWords[i].length(), temp);
			}				
		}
		int k = 0;
		while(k < 3){
			k++;
			//use token to get size of guessed word
			long startTime = System.currentTimeMillis();
			HttpConnection actualWord = new HttpConnection("http://rocky-everglades-1153.herokuapp.com/phrase?token="+userSet.token);

			String [] theWord = actualWord.token.split(" ");
			tries = new int[theWord.length];
			HttpConnection passedWord;
			String guessedString = "";
			//System.out.println(actualWord.token.length());
			while(true){

				guessedString = possibleWords.get(theWord[0].length()).get(tries[0]);
				for (int i = 1; i < theWord.length; i++){
					guessedString = guessedString + "+" + possibleWords.get(theWord[i].length()).get(tries[i]);
				}
				//System.out.println(guessedString);			
				passedWord = new HttpConnection("http://rocky-everglades-1153.herokuapp.com/guess?token="+userSet.token+"&guess="+guessedString);
				if (Integer.parseInt(passedWord.token) == actualWord.token.length()){
					break;
				}else{
					findIndexToUpdate(possibleWords, theWord);
				}
			}		
			long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			System.out.println(totalTime);
			System.out.println("Final answer: " + guessedString.replace('+', ' '));
		}
	}

	public static void findIndexToUpdate(Map<Integer, ArrayList<String>> possibleWords, String [] theWord){
		for (int i = tries.length - 1; i >= 0; i--){
			if (possibleWords.get(theWord[i].length()).size() > tries[i] + 1){
				tries[i]++;
				for (int j = i + 1; j < tries.length; j++){
					tries [j] = 0;
				}
				break;
			}
		}
	}
}
