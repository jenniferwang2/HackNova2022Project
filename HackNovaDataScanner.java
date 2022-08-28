import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
public class HackNovaDataScanner
{	
	public static void main(String [] args)
    {
	    ArrayList<DataClass> listofDataObjects = new ArrayList<DataClass>();
	    // main dataset 
	    Scanner scanner;
	    try 
	    {
	    	scanner = new Scanner(new File("tester.csv"));
	    	while(scanner.hasNext())
	    	{
	    		String line = scanner.nextLine();
	    		DataClass text = new DataClass(findSentiment(line), findText(line)); 
	    		listofDataObjects.add(text);
	    	}
	    }
	    catch (Exception e)
	    {   
            System.out.println(e);
	    	return;
	    }
	    double score = testing(listofDataObjects);
	    System.out.println(score + "% accurate");
	    ArrayList<wordTracker> words = createDatabase(listofDataObjects);
	    Scanner inputSentence = new Scanner(System.in);
	        while(true) 
	        {
	            System.out.println("Enter sentence or file ending with (.txt) :");
	            String sentence = inputSentence.nextLine();
	            if(sentence.equals(""))
	            {
	                break;
	            }
	            else if ( sentence.endsWith(".txt"))
	            {
	                boolean prediction = fileSentiment(sentence, words, listofDataObjects);
	                System.out.println("Prediction of file: " + prediction);
	            }
	            else
	            {
	            boolean prediction = predict(sentence, words, listofDataObjects);
	            System.out.println("Prediction of sentence is positive: " + prediction);
	            }
	        }
		}
	public static boolean fileSentiment(String filename, ArrayList<wordTracker> words, ArrayList<DataClass> listofDataObjects)
    {   
        int total = 0; 
        int positive = 0;
        boolean sentiment = false; 
        try
        {
            Scanner s = new Scanner(new File(filename));
            while (s.hasNext())
            {   
                String line = s.nextLine(); 
                boolean result = predict(line, words, listofDataObjects);        
                if (result)
                {
                    positive += 1; 
                }
                total += 1; 
            }
        }
        catch (Exception e)
        {   
            System.out.println("ERROR");
        }
        if (total == 0)
        {   
            System.out.println("File is empty");
            return(false);
        }
        if(positive/total > 0.5)
        {
            sentiment = true; 
        }
        return(sentiment);
    }
    public static ArrayList<wordTracker> createDatabase(ArrayList<DataClass> listofDataObjects)
    {   
        ArrayList<wordTracker> wordz = new ArrayList<wordTracker>();
        boolean found = false; 
            for (int i  = 0; i < listofDataObjects.size(); i++)
            {   
                for (int j  = 0; j < listofDataObjects.get(i).wordsList.size(); j++)
                {  
                    String s = listofDataObjects.get(i).wordsList.get(j); 
                    found = false; 
                    int index = -1; 
                    for (int a = 0; a < wordz.size(); a++)
                    {   
                        if (wordz.get(a).word.equals(s))
                        {   
                            found = true;
                            index = a;
                            break;
                        }
                    }
                    if (found)
                    {   
                        //checks for sentiment 
                        if (listofDataObjects.get(i).sentiment.equals("nothate"))
                        {   
                            //adds one to sentiment count if it's positive etc.
                            wordz.get(index).positive += 1; 
                        }
                        else
                        {
                            wordz.get(index).negative += 1; 
                        }
                    }
                    else 
                    {
                    wordTracker newWord = new wordTracker(s);
                    if (listofDataObjects.get(i).sentiment.equals("nothate"))
                        {   
                            newWord.positive = 1;
                        }
                    else
                        {
                            newWord.negative = 1;
                        }
                    wordz.add(newWord);
                    }
                }
            }
        return wordz;    
    }
    public static boolean predict (String sentence, ArrayList<wordTracker> words,
                                 ArrayList<DataClass> listofDataObjects)
    {       
        double positiveLikely = 1;
        double negativeLikely = 1;
        double positiveFrequency = 1;
        double negativeFrequency = 1; 
        double totalTexts = listofDataObjects.size(); 
        double  totalPositive = 0;
        double totalNegative = 0;
        for (int i = 0; i < listofDataObjects.size(); i++)
        {   
            //checks if it is positive 
            if (listofDataObjects.get(i).sentiment.equals("nothate"))
            {
                totalPositive +=1; 
            }
            else
            {
                totalNegative +=1; 
            }
        }
        //split sentence into words, store in list. 
        String [] splitSentenceWords = sentence.split(" "); 
        //loop through each word in 
        for (int i = 0; i < splitSentenceWords.length; i++)
        {   
            //string current word 
            String currentWord = splitSentenceWords[i];
            //how many times does current word appears in a positive text from database?
            for (int j  = 0; j < words.size(); j++)
            {   
                //get it's positive frequency 
                if (words.get(j).word.equals(currentWord))
                {
                    positiveFrequency = (words.get(j).positive + 1)/ totalPositive; 
                    positiveLikely = positiveFrequency * positiveLikely; 
                    negativeFrequency = (words.get(j).negative + 1)/ totalNegative; 
                    negativeLikely = negativeFrequency * negativeLikely;
                }
            }
        }  
        positiveLikely = positiveLikely * 100;
        negativeLikely = negativeLikely * 100;
        return(positiveLikely > negativeLikely);
    }
    public static double testing (ArrayList<DataClass> listofDataObjects)
    {
        ArrayList<DataClass> training = new ArrayList<DataClass>();
        ArrayList<DataClass> testing = new ArrayList<DataClass>();
        for (int i = 0; i < listofDataObjects.size(); i++)
        {
            //every fifth text
            if (i%5 == 0) //if divislble by five/every fifth text
            {
                testing.add(listofDataObjects.get(i));
            }
            else 
            {
                training.add(listofDataObjects.get(i));
            }
        }
        ArrayList<wordTracker> trainWords = createDatabase(training);
        boolean test = false; 
        int correct = 0;
        for (int i = 0; i < testing.size(); i++)
        {
            test = predict(testing.get(i).text, trainWords, listofDataObjects);
            if(test == testing.get(i).sentiment.equals("nothate")) {
                correct += 1;
            }
        }
        double totalSize = testing.size();
        double percent = correct / totalSize * 100;
        return(percent);
    }
    public static String findSentiment(String line)
    {   
        return line.split(",")[0];
    }
    public static String findText(String line)
    {
        return line.split(",")[1];
    }
    public static final String[] STOPWORDS = new String[]
    {
        "i", "", " ",
        "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours", 
        "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", 
        "herself", "it", "its", "itself", "they", "them", "their", "theirs", "themselves", 
        "what", "which", "who", "whom", "this", "that", "these", "those", "am", "is", 
        "are", "was", "were", "be", "been", "being", "have", "has", "had", "having", "do", 
        "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", 
        "as", "until", "while", "of", "at", "by", "for", "with", "about", "against", 
        "between", "into", "through", "during", "before", "after", "above", "below", "to", 
        "from", "up", "down", "in", "out", "on", "off", "over", "under", "again", 
        "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", 
        "any", "both", "each", "few", "more", "most", "other", "some", "such", "no", "nor",
        "not", "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will",
        "just", "don", "should", "now"
    };


}
