import java.util.ArrayList;
public class DataClass
{
public String sentiment; 
public String text; 
public ArrayList<String> wordsList;
public String toString()
	{	
		return (sentiment + " : " + text + " : " + wordsList); 
	}
public DataClass (String senty, String texty)
{
	this.sentiment = senty;
	this.text = texty; 
	wordsList = new ArrayList<>();
	String [] words = this.text.split(" ");
	for(int i = 0; i < words.length; i++)
	{
		boolean haveWordinList = false;
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
