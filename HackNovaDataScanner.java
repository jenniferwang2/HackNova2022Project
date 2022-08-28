import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;

public class HackNovaDataScanner
{	
	/* This is my main method. 

	I'm doing a few things here. 

	First, I will create an arraylist of DataClass objects. This will be my main dataset. From here, I intend to split classify 
	individual object elements based on their assigned sentiment. Maybe. 

	An n object will have a sentiment and text. Both will be strings since I'm going off of a preexisting dataset (csv)
	LATER FUNCTIONS MIGHT DEPEND ON DATA FORMATING!!!!!!
	*/

	public static void main(String [] args)
    {

	    ArrayList<DataClass> listofDataObjects = new ArrayList<DataClass>();
	    // main dataset 

	    //SCANNING CSV FILE: 

	    Scanner scanner;
	    try 
	    {
	    	scanner = new Scanner(new File("tester.csv"));
	    	while(scanner.hasNext())
	    	{
	    		String line = scanner.nextLine();

	    		DataClass text = new DataClass(findSentiment(line), findText(line)); //these two functions called upon might depend on the formating of the data
	    		listofDataObjects.add(text);
	    		//new object consisting of sentiment and text 
	    	}
	    }
	    catch (Exception e)
	    {   
            System.out.println("you messed up here up here");
            System.out.println(e);
	    	return;
	    }
	    //PURELY TESTING OR USER INTERACTION, might not need. 
	    //predictions depends on words object, 
	        //all of it is in the testing function, this is all the data 
	        //so my words arraylist is my complete database, while as the other wordTracker object 
	        //found in my predict method is only 80 percent of the arraylist...I can't access it 
	        //from outside the scope of the method so I define it again  
	    double score = testing(listofDataObjects);
	    System.out.println(score + "% accurate");
	    ArrayList<wordTracker> words = createDatabase(listofDataObjects);

	    Scanner inputSentence = new Scanner(System.in);
	        while(true) //I create a while loop so I can continue to use the user interaction part 
	            //of my code 
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
	    //IGNORE
		}


	/*
	THIS METHOD determines the sentiment of a text. Calls upon predict method and wordTracker class. Please see. 
	EDIT.
	*/
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

        //Creates database of words and frequency in positive and negative texts 
    public static ArrayList<wordTracker> createDatabase(ArrayList<DataClass> listofDataObjects)
    {   
        //initialize database and boolean 
        ArrayList<wordTracker> wordz = new ArrayList<wordTracker>();
        boolean found = false; 

            //loop through list of text
            for (int i  = 0; i < listofDataObjects.size(); i++)
            {   
                //loop through each texts's wordsList 
                for (int j  = 0; j < listofDataObjects.get(i).wordsList.size(); j++)
                {   
                    //String set to current word in wordsList 
                    String s = listofDataObjects.get(i).wordsList.get(j); 
                    found = false; //set boolean to false 
                    int index = -1; 
                
                    // is  current word (s), is it found in database?
                    for (int a = 0; a < wordz.size(); a++)
                    {   
                        if (wordz.get(a).word.equals(s))
                        //(listofDataObjects.get(i).wordsList.get(j).equals(s))
                        {   
                            found = true;
                            index = a;
                            break;
                        }
                    }

                    //if it already exists 
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

                    //if if it doesn't exist, add it
                    else 
                    {
                    //fix this issue
                    wordTracker newWord = new wordTracker(s);
                    // newWord.sentiment.equals();

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
        //System.out.println(words);
        return wordz;    
    }

    public static boolean predict (String sentence, ArrayList<wordTracker> words,
                                 ArrayList<DataClass> listofDataObjects)
    //arraylist is the database of words and frequency 
    {       

        //probability totals: 
        double positiveLikely = 1;
        double negativeLikely = 1;

        double positiveFrequency = 1;
        double negativeFrequency = 1;

        //total texts. 
        double totalTexts = listofDataObjects.size(); 

        //store number of positive and negative texts. 
        double  totalPositive = 0;
        double totalNegative = 0;

        //find total number of positive and negative texts
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
        //System.out.println("positive likelyhood: " + positiveLikely + "% negative likely: " + negativeLikely + "% ");


        return(positiveLikely > negativeLikely);
    }

        //SPLITS UP INTO TWO LISTS, TESTS 
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