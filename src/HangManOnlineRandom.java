import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HangManOnlineRandom {
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
		HttpConnection userSet = new HttpConnection("http://rocky-everglades-1153.herokuapp.com/start?name="+"abc");
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
		while(true){
			//use token to get size of guessed word
			long startTime = System.currentTimeMillis();
			HttpConnection actualWord = new HttpConnection("http://rocky-everglades-1153.herokuapp.com/phrase?token="+userSet.token);
			if (actualWord.token ==  null){
				break;
			}
			String [] theWord = actualWord.token.split(" ");
			tries = new int[theWord.length];
			int index = 0;
			HttpConnection passedWord;
			String guessedString = "";
			//System.out.println(actualWord.token.length());
			while(true){				
				guessedString = getRandomWord(possibleWords, theWord, index);
				//System.out.println(guessedString);			
				passedWord = new HttpConnection("http://rocky-everglades-1153.herokuapp.com/guess?token="+userSet.token+"&guess="+guessedString);
				if (Integer.parseInt(passedWord.token) == actualWord.token.length()){
					break;
				}else{
					index  = setIndex(theWord, Integer.parseInt(passedWord.token), index);
				}
			}		
			
			long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			System.out.println(totalTime);
			System.out.println("Final answer: " + guessedString.replace('+', ' '));
		}
	}

	public static String getRandomWord(Map<Integer, ArrayList<String>> possibleWords, String [] theWord, int index){
		
		//String str = possibleWords.get(theWord[0].length()).get(tries[0]);
		
		String str = possibleWords.get(theWord[0].length()).get(tries[0]);
		for (int i = 1; i <= index; i++){
			str = str + "+" + possibleWords.get(theWord[i].length()).get(tries[i]);
		}
		
		for (int j = index + 1; j < theWord.length; j++){
			if (j != theWord.length){
				str = str + "+";
			}
			for (int k = 0; k < theWord[j].length(); k++){
				str = str + "?";
			}			
		}		
		return str;
	}
	
	public static int setIndex(String [] theWord, int numMatched, int index){
		int toReturn = -1; 	
		if (numMatched > 0){
			numMatched = numMatched - theWord.length + 1;
			for (int i = 0; i < theWord.length; i++){
				numMatched = numMatched - theWord[i].length();
				if (numMatched >= 0){
					toReturn = i;
				}else{
					break;
				}
			}
		}
		if (toReturn != -1 && toReturn == index){	
			return toReturn + 1;
		}else{
			tries[index]++;
			return index;
		}
	}


}
