public class wordTracker
{
	public String word; 
	public int positive; 
	public int negative; 

	public String toString()
	{	
		return word + ": +" + positive + " -" + negative; 
	}
	public wordTracker(String word)
	{
		this.word = word; 
	}
}