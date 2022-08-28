import java.util.ArrayList;
public class DataClass
{
/* 
In this class, I create objects consisting of a sentiment in measuring 
how a certain text can be offensive or not. "TRUE" or "FALSE". Look at csv file 

followed by string text attribute, which is the text. 

The last attribute is an arraylist. This contains every single potential sentiment-biased word of EVERY object 
Ex: "I hate cats" - > ["hate", "cats"] maybe. I try to filter out "I" later. 
This will be relevant in the sentiment anaylsis later to see how frequently each word above appears in a positive casted 
vs negative casted sentence. (My intuition is that "hate" would appear more frequently in offensive text)
*/

public String sentiment; 
public String text; 
public ArrayList<String> wordsList;

//everything toString shawty 
//expected return >> true : I hate cats : ["I, "hate", "cats"]
public String toString()
	{	
		return (sentiment + " : " + text + " : " + wordsList); 
	}

public DataClass (String senty, String texty)
{
	this.sentiment = senty;
	this.text = texty; 
	wordsList = new ArrayList<>();

	//temporary list that contains the words of the text. 
	String [] words = this.text.split(" "); //indexof() method applicable
	// System.out.println(words);
	/*
	"The split() method takes a pattern and divides a String into an ordered list of substrings 
	by searching for the pattern, puts these substrings into an array, and returns the array."
	(https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String/split)
	as stated, the expected pattern is a space _ dividing each word...
	*/

	for(int i = 0; i < words.length; i++)
	{
		boolean haveWordinList = false;
		//interate through STOPWORDS (words without a sentiment, filler words), we wanna double iterate to check if 
		//any of the words in the text are filler words, we don't wanna add those to the wordList!
		for (int j = 0; j < HackNovaDataScanner.STOPWORDS.length; j++)
		{
			if (HackNovaDataScanner.STOPWORDS[j].equals(words[i]))
			{
				haveWordinList = true;
			}
			//only add it if the word if it we still don't have it 
			if (haveWordinList == false)
			{
				wordsList.add(words[i]);
			}
		}
	}
}
}